# Procédure 4 - Déploiement d'un nouveau client

Cette procédure documente le fonctionnement du script `scripts/4_deploy_new_client.sh`.

## 1. Initialisation et Paramètres
Le script prend un argument unique : le nom du client. Il définit ensuite les variables nécessaires pour la base de données et le conteneur.

```bash
CLIENT_NAME="$1"
DB_NAME="dolibarr_${CLIENT_NAME}"
DB_USER="dolibarr_${CLIENT_NAME}"
DB_PASS="pass_${CLIENT_NAME}"
CONTAINER_NAME="dolibarr_${CLIENT_NAME}"
DOMAIN="${CLIENT_NAME}.dolibarr.lan"
```

## 2. Création de la Base de Données
Le script se connecte à la VM Database pour créer un utilisateur et une base de données PostgreSQL dédiés au client, si ils n'existent pas déjà.

```bash
# Création utilisateur et base de données
ssh "$VM_USER@$IP_DATABASE" "cd /tmp && (sudo -u postgres psql -tAc \"SELECT 1 FROM pg_roles WHERE rolname='$DB_USER'\" | grep -q 1 || sudo -u postgres psql -c \"CREATE USER $DB_USER WITH PASSWORD '$DB_PASS';\")"
ssh "$VM_USER@$IP_DATABASE" "cd /tmp && (sudo -u postgres psql -tAc \"SELECT 1 FROM pg_database WHERE datname='$DB_NAME'\" | grep -q 1 || sudo -u postgres psql -c \"CREATE DATABASE $DB_NAME OWNER $DB_USER;\")"
```

## 3. Déploiement du Conteneur Dolibarr
Sur la VM Dolibarr, le script :
1. Supprime l'ancien conteneur s'il existe.
2. Crée un répertoire local pour les documents du client.
3. Lance un nouveau conteneur `tuxgasy/dolibarr` avec les variables d'environnement configurées pour la connexion à la base de données.

```bash
ssh "$VM_USER@$IP_DOLIBARR" "sudo docker run -d --name $CONTAINER_NAME \
  --restart always \
  -P \
  -v /home/$VM_USER/clients/$CLIENT_NAME/documents:/var/www/html/dolibarr/documents \
  -e DOLI_DB_HOST=$IP_DATABASE \
  ...
  tuxgasy/dolibarr:latest"
```

## 4. Configuration du Reverse Proxy
Le script récupère le port dynamique attribué par Docker et génère un fichier de configuration pour Traefik sur la VM Proxy. Cela permet d'accéder au client via une URL spécifique.

```bash
# Récupération du port
RAW_PORT=$(ssh "$VM_USER@$IP_DOLIBARR" "docker port $CONTAINER_NAME 80/tcp")
PORT=$(echo "$RAW_PORT" | awk -F: '{print $2}')

# Envoi de la configuration Traefik
scp "$SCRIPT_DIR/temp_client.yml" "$VM_USER@$IP_PROXY:~/traefik/dynamic/${CLIENT_NAME}.yml"
```

## 5. Finalisation Apache
Pour permettre l'accès via un chemin (ex: `http://IP/nom_client/`), le script configure un alias Apache à l'intérieur du conteneur Dolibarr.

```bash
ssh "$VM_USER@$IP_DOLIBARR" "docker exec ${CONTAINER_NAME} sh -c \"echo 'Alias /${CLIENT_NAME} /var/www/html' > /etc/apache2/conf-available/alias-${CLIENT_NAME}.conf\""
ssh "$VM_USER@$IP_DOLIBARR" "docker exec ${CONTAINER_NAME} sh -c \"a2enconf alias-${CLIENT_NAME} && service apache2 reload\""
```

