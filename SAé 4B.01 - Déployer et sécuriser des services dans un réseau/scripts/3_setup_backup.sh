#!/bin/bash
#==============================================
# Configuration des Sauvegardes Automatiques
# SAÉ 4B.01 - Hébergement Dolibarr
#==============================================
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../config.conf"
source "$SCRIPT_DIR/../formattage.conf"


echo_info "=== Configuration des Sauvegardes ==="

# 1. Génération de la clé SSH sur la VM Backup si nécessaire
echo_info "Keys Generation on Backup VM..."
ssh "$VM_USER@$IP_BACKUP" "[ -f ~/.ssh/id_rsa ] || ssh-keygen -t rsa -N '' -f ~/.ssh/id_rsa"
ssh "$VM_USER@$IP_BACKUP" "sudo apt-get update >/dev/null && sudo apt-get install -y postgresql-client rsync sshpass >/dev/null"

# 2. Récupération de la clé publique de Backup
BACKUP_PUB_KEY=$(ssh "$VM_USER@$IP_BACKUP" "cat ~/.ssh/id_rsa.pub")

# 3. Distribution de la clé vers DB et Dolibarr
echo_info "Distribution de la clé SSH de Backup vers DB et Dolibarr..."
# Vers DB (pour pg_dump)
ssh "$VM_USER@$IP_DATABASE" "mkdir -p ~/.ssh && echo '$BACKUP_PUB_KEY' >> ~/.ssh/authorized_keys"

# Configuration PostgreSQL pour autoriser Backup (Trust ou MD5)
# On autorise l'IP de backup en TRUST pour faciliter pg_dump sans mot de passe
echo_info "Configuration PG_HBA sur DB..."
ssh "$VM_USER@$IP_DATABASE" "sudo grep -q '$IP_BACKUP' /etc/postgresql/*/main/pg_hba.conf || echo \"host all all $IP_BACKUP/32 trust\" | sudo tee -a /etc/postgresql/*/main/pg_hba.conf"
ssh "$VM_USER@$IP_DATABASE" "sudo systemctl reload postgresql"

# Vers Dolibarr (pour documents)
ssh "$VM_USER@$IP_DOLIBARR" "mkdir -p ~/.ssh && echo '$BACKUP_PUB_KEY' >> ~/.ssh/authorized_keys"

# 4. Création du script de backup sur la VM Backup
echo_info "Création du script de backup distant..."
cat > "$SCRIPT_DIR/temp_backup_script.sh" <<EOF
#!/bin/bash
DATE=\$(date +%Y-%m-%d_%H-%M-%S)
BACKUP_DIR="/home/$VM_USER/backups/\$DATE"
mkdir -p "\$BACKUP_DIR"

# Backup PostgreSql
echo "Backup DB..."
# On liste toutes les DB qui commencent par 'dolibarr_' depuis le client distant
# Nécessite 'export PGPASSWORD' si md5, ou 'trust' dans pg_hba.conf
DBS=\$(psql -h $IP_DATABASE -U postgres -t -c "SELECT datname FROM pg_database WHERE datname LIKE 'dolibarr_%'")

for DB in \$DBS; do
    echo "  -> \$DB"
    pg_dump -h $IP_DATABASE -U postgres \$DB > "\$BACKUP_DIR/\$DB.sql"
done

# Backup Documents
echo "Backup Documents..."
rsync -avz -e "ssh -o StrictHostKeyChecking=no" $VM_USER@$IP_DOLIBARR:/home/$VM_USER/clients/ "\$BACKUP_DIR/documents/"

# Rétention (7 jours)
find /home/$VM_USER/backups/ -maxdepth 1 -type d -mtime +7 -exec rm -rf {} \;
EOF
echo_info "Script de backup créé localement, déploiement sur VM Backup..."
scp "$SCRIPT_DIR/temp_backup_script.sh" "$VM_USER@$IP_BACKUP:~/scripts/daily_backup.sh"
rm "$SCRIPT_DIR/temp_backup_script.sh"
ssh "$VM_USER@$IP_BACKUP" "chmod +x ~/scripts/daily_backup.sh"

# 5. Ajout au Cron (tous les jours à 2h du matin)
echo_info "Planification Cron..."
ssh "$VM_USER@$IP_BACKUP" "(crontab -l 2>/dev/null | grep -v 'daily_backup.sh'; echo '0 2 * * * /home/$VM_USER/scripts/daily_backup.sh >> /home/$VM_USER/backups/backup.log 2>&1') | crontab -"

echo_success "Configuration Backup terminée !"
