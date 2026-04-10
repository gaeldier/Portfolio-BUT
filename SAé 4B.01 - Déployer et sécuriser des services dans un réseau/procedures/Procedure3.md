# Procédure 3 - Configuration des Sauvegardes

Cette procédure documente le fonctionnement du script `scripts/3_setup_backup.sh`, qui configure un système de sauvegarde automatique centralisé sur la machine de backup pour les données de la base de données et les fichiers de Dolibarr.

## 1. Initialisation de la VM Backup 
Configuration de la machine `backup` :

```bash
# 1. Clé SSH sur la VM Backup si nécessaire
echo "Génération de la clé sur la VM Backup..."
ssh "$VM_USER@$IP_BACKUP" "[ -f ~/.ssh/id_rsa ] || ssh-keygen -t rsa -N '' -f ~/.ssh/id_rsa"
ssh "$VM_USER@$IP_BACKUP" "sudo apt-get update >/dev/null && sudo apt-get install -y postgresql-client rsync sshpass >/dev/null"

# 2. Récupération de la clé publique de Backup
BACKUP_PUB_KEY=$(ssh "$VM_USER@$IP_BACKUP" "cat ~/.ssh/id_rsa.pub")
```

- **Clé SSH :** Génération d'une paire de clés RSA `(si absente)` pour permettre à la machine de backup de se connecter aux autres VMs.
- **Dépendances :** Installation du client PostgreSQL, de rsync et de sshpass.

## 2. Établissement SSH
Pour permettre les sauvegardes sans intervention humaine :

```bash
# 3. Distribution de la clé vers DB et Dolibarr
echo "Distribution de la clé SSH vers DB et Dolibarr..."
# Autorisation SSH sur la DB
ssh "$VM_USER@$IP_DATABASE" "mkdir -p ~/.ssh && echo '$BACKUP_PUB_KEY' >> ~/.ssh/authorized_keys"

//...

# Autorisation SSH sur Dolibarr
ssh "$VM_USER@$IP_DOLIBARR" "mkdir -p ~/.ssh && echo '$BACKUP_PUB_KEY' >> ~/.ssh/authorized_keys"
```

- **Récupération :** Le script extrait la clé publique de la machine de backup.
- **Distribution :** Cette clé est ajoutée aux fichiers `authorized_keys` des machines `database` et `dolibarr`.

## 3. Autorisation PostgreSQL
Sur la machine de base de données, le fichier `pg_hba.conf` est modifié pour autoriser l'IP de la machine de backup en mode `trust`. Cela permet l'utilisation de `pg_dump` sans saisie de mot de passe depuis le serveur de sauvegarde.

```bash
# Configuration PostgreSQL pour autoriser Backup (Trust ou MD5)
# On autorise l'IP de backup en TRUST pour faciliter pg_dump sans mot de passe
echo "Configuration PG_HBA sur la base de données..."
ssh "$VM_USER@$IP_DATABASE" "sudo grep -q '$IP_BACKUP' /etc/postgresql/*/main/pg_hba.conf || echo \"host all all $IP_BACKUP/32 trust\" | sudo tee -a /etc/postgresql/*/main/pg_hba.conf"
ssh "$VM_USER@$IP_DATABASE" "sudo systemctl reload postgresql"
```

## 4. Script de sauvegarde distant 
Un script `daily_backup.sh` est généré et déposé sur la machine de backup. Ses actions sont :

```bash
# 4. Script de sauvegarde sur la VM Backup
echo "Création du script de sauvegarde distant..."
cat > "$SCRIPT_DIR/temp_backup_script.sh" <<EOF
#!/bin/bash
DATE=\$(date +%Y-%m-%d_%H-%M-%S)
BACKUP_DIR="/home/$VM_USER/backups/\$DATE"
mkdir -p "\$BACKUP_DIR"

# Backup PostgreSQL
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

scp "$SCRIPT_DIR/temp_backup_script.sh" "$VM_USER@$IP_BACKUP:~/scripts/daily_backup.sh"
rm "$SCRIPT_DIR/temp_backup_script.sh"
ssh "$VM_USER@$IP_BACKUP" "chmod +x ~/scripts/daily_backup.sh"
```
- **Horodatage :** Création d'un dossier par backup basé sur la date et l'heure.
- **Sauvegarde SQL :** Identification automatique des bases commençant par `dolibarr_` et extraction via `pg_dump`.
- **Sauvegarde Filesystem :** Synchronisation du dossier `/home/user/clients/` de la VM Dolibarr vers le stockage local via `rsync`.
- **Rétention :** Suppression automatique des sauvegardes de plus de 7 jours.

## 5. Automatisation 
Le script est ajouté à la `crontab` de l'utilisateur sur la machine de backup. Il est configuré pour s'exécuter tous les jours à **2h00 du matin**, avec redirection des logs dans `~/backups/backup.log`.

```bash
# 5. Planification Cron (2h du matin)
echo "Configuration du cron..."
ssh "$VM_USER@$IP_BACKUP" "(crontab -l 2>/dev/null | grep -v 'daily_backup.sh'; echo '0 2 * * * /home/$VM_USER/scripts/daily_backup.sh >> /home/$VM_USER/backups/backup.log 2>&1') | crontab -"
```

---

Etape précédante --> [Procédure 2 - Installation de l'Infrastructure](Procedure2.md) <--

Etape suivante --> [Procédure 4 - Déploiement d'un nouveau client](Procedure4.md) <--