#!/bin/bash
#==============================================
# Déploiement d'un Client (Dolibarr)
# SAÉ 4B.01 - Hébergement Dolibarr
#==============================================
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../config.conf"
source "$SCRIPT_DIR/../formattage.conf"

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <nom_client>"
    exit 1
fi

CLIENT_NAME="$1"
DB_NAME="dolibarr_${CLIENT_NAME}"
DB_USER="dolibarr_${CLIENT_NAME}"
DB_PASS="pass_${CLIENT_NAME}" # TODO: Générer un mot de passe fort
CONTAINER_NAME="dolibarr_${CLIENT_NAME}"
DOMAIN="${CLIENT_NAME}.saule08.iutinfo.fr" # Pour test local avec /etc/hosts

echo_info "=== Déploiement du client : $CLIENT_NAME ==="

# 1. Création de la Base de Données
echo_info "Création de la base de données sur $IP_DATABASE..."
# Vérifie si l'utilisateur existe déjà pour éviter l'erreur (cd /tmp pour éviter le warning de permission home)
ssh "$VM_USER@$IP_DATABASE" "cd /tmp && (sudo -u postgres psql -tAc \"SELECT 1 FROM pg_roles WHERE rolname='$DB_USER'\" | grep -q 1 || sudo -u postgres psql -c \"CREATE USER $DB_USER WITH PASSWORD '$DB_PASS';\")"
# Vérifie si la DB existe, sinon la crée
ssh "$VM_USER@$IP_DATABASE" "cd /tmp && (sudo -u postgres psql -tAc \"SELECT 1 FROM pg_database WHERE datname='$DB_NAME'\" | grep -q 1 || sudo -u postgres psql -c \"CREATE DATABASE $DB_NAME OWNER $DB_USER;\")"
echo_success "Base de données '$DB_NAME' créée avec l'utilisateur '$DB_USER'."

# 2. Lancement du conteneur Dolibarr
echo_success "Lancement du conteneur Dolibarr sur $IP_DOLIBARR..."
# Nettoyage si conteneur existant
ssh "$VM_USER@$IP_DOLIBARR" "sudo docker rm -f $CONTAINER_NAME 2>/dev/null || true"

# Création du dossier pour les documents
ssh "$VM_USER@$IP_DOLIBARR" "mkdir -p /home/$VM_USER/clients/$CLIENT_NAME/documents"

# On utilise -P pour publier le port 80 sur un port aléatoire de l'hôte
# On passe les infos de connexion DB via variables d'environnement
# Suppression de l'ancien conteneur s'il existe (pour permettre la mise à jour des paramètres)
ssh "$VM_USER@$IP_DOLIBARR" "docker rm -f $CONTAINER_NAME 2>/dev/null || true"
echo_info "Lancement du conteneur Docker pour $CLIENT_NAME..."

ssh "$VM_USER@$IP_DOLIBARR" "sudo docker run -d --name $CONTAINER_NAME \
  --restart always \
  -P \
  -v /home/$VM_USER/clients/$CLIENT_NAME/documents:/var/www/html/dolibarr/documents \
  -e DOLI_DB_HOST=$IP_DATABASE \
  -e DOLI_DB_HOST_PORT=5432 \
  -e DOLI_DB_TYPE=pgsql \
  -e DOLI_DB_USER=$DB_USER \
  -e DOLI_DB_PASSWORD=$DB_PASS \
  -e DOLI_DB_NAME=$DB_NAME \
  -e DOLI_ADMIN_LOGIN=admin \
  -e DOLI_ADMIN_PASSWORD=admin \
  -e DOLI_INSTALL_AUTO=1 \
  -e DOLI_PROD=0 \
  -e DOLI_URL_ROOT="/${CLIENT_NAME}" \
  tuxgasy/dolibarr:latest"

echo_success "Conteneur Docker lancé pour $CLIENT_NAME."
# 3. Récupération du port public mapping
echo_info "Récupération du port..."
RAW_PORT=$(ssh "$VM_USER@$IP_DOLIBARR" "docker port $CONTAINER_NAME 80/tcp")
# Format attendu: 0.0.0.0:12345
PORT=$(echo "$RAW_PORT" | awk -F: '{print $2}')
echo_success "   -> Port attribué : $PORT"

# 4. Configuration du Proxy (Traefik)
echo_info "Configuration du Reverse Proxy sur $IP_PROXY..."
cat > "$SCRIPT_DIR/temp_client.yml" <<EOF
http:
  routers:
    ${CLIENT_NAME}-router:
      rule: "Host(\`${DOMAIN}\`) || PathPrefix(\`/${CLIENT_NAME}\`)"
      service: ${CLIENT_NAME}-service
  services:
    ${CLIENT_NAME}-service:
      loadBalancer:
        servers:
          - url: "http://${IP_DOLIBARR}:${PORT}"
EOF

echo_info "Déploiement de la configuration pour $CLIENT_NAME..."
scp "$SCRIPT_DIR/temp_client.yml" "$VM_USER@$IP_PROXY:~/traefik/dynamic/${CLIENT_NAME}.yml"
rm "$SCRIPT_DIR/temp_client.yml"
echo_success "Configuration du Reverse Proxy déployée pour $CLIENT_NAME."

# 5. Configuration Apache Alias dans le conteneur (Pour gérer le sous-dossier sans strip)
echo_info "Configuration Apache (Alias /${CLIENT_NAME})..."
# On attend un peu que Dolibarr et Apache démarrent
sleep 15
# On utilise sh -c pour éviter les problèmes de shell bash
ssh "$VM_USER@$IP_DOLIBARR" "docker exec ${CONTAINER_NAME} sh -c \"echo 'Alias /${CLIENT_NAME} /var/www/html' > /etc/apache2/conf-available/alias-${CLIENT_NAME}.conf\""
ssh "$VM_USER@$IP_DOLIBARR" "docker exec ${CONTAINER_NAME} sh -c \"a2enconf alias-${CLIENT_NAME} && service apache2 reload\"" || echo "Attention: Configuration Alias échouée"

echo_success "   Déploiement terminé pour $CLIENT_NAME !"
echo "   URL: http://$IP_PROXY/${CLIENT_NAME}/ (si PathPrefix utilisé)"
echo "   URL Locale (Tunnel): http://localhost:8080/${CLIENT_NAME}/"
echo ""
echo "   Si vous voyez une erreur 'relation llx_user does not exist' :"
echo "   Allez sur : http://localhost:8080/${CLIENT_NAME}/install/"
echo "   Et suivez les étapes (tout est pré-rempli)."
