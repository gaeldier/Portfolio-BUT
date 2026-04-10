# Procédure 2 - Installation de l'Infrastructure

Cette procédure documente le fonctionnement du script `scripts/2_install_infrastructure.sh`, détaillant l'installation et la configuration des services sur les différentes machines (Base de données, Dolibarr, Proxy et Backup).

## 1. Prérequis et Initialisation
Le script nécessite que les machines soient créées et que l'accès SSH sans mot de passe soit opérationnel (voir. [Procédure 0 - Initialisation et Configuration des VMs](Procedure0.md) -&- [Procédure 1 - Configuration de l'accès SSH](Procedure1.md)).

## 2. Base de Données
L'installation cible la VM `database` ($IP_DATABASE) :

```bash
# 1. Base de Données (PostgreSQL)
echo "Installation sur la base de données ($IP_DATABASE)..."

# Installation du paquet
ssh "$VM_USER@$IP_DATABASE" "sudo apt-get update >/dev/null && sudo apt-get install -y postgresql >/dev/null"

# Autorisation d'accès pour Dolibarr dans pg_hba.conf
ssh "$VM_USER@$IP_DATABASE" "sudo grep -q '$IP_DOLIBARR' /etc/postgresql/*/main/pg_hba.conf || echo \"host all all $IP_DOLIBARR/32 md5\" | sudo tee -a /etc/postgresql/*/main/pg_hba.conf"

# Configuration du listen_addresses sur '*'
ssh "$VM_USER@$IP_DATABASE" "sudo sed -i \"s/^#*listen_addresses = .*/listen_addresses = '*' /\" /etc/postgresql/*/main/postgresql.conf"
ssh "$VM_USER@$IP_DATABASE" "grep -q \"listen_addresses = '*'\" /etc/postgresql/*/main/postgresql.conf || echo \"listen_addresses = '*'\" | sudo tee -a /etc/postgresql/*/main/postgresql.conf"

ssh "$VM_USER@$IP_DATABASE" "sudo systemctl restart postgresql"
```

- **Installation :** Mise à jour des dépôts et installation du paquet `postgresql`.
- **Accès réseau :** Ajout d'une règle dans `pg_hba.conf` pour autoriser uniquement la VM Dolibarr ($IP_DOLIBARR).
- **Écoute :** Modification du paramètre `listen_addresses` dans `postgresql.conf` pour accepter les connexions sur toutes les interfaces (`*`).
  
`(Relance du service pour appliquer les changements)`

## 3. Application Dolibarr
Configuration de la VM `dolibarr` ($IP_DOLIBARR) :

```bash
# 2. Application (Docker)
echo "Installation sur dolibarr ($IP_DOLIBARR)..."
ssh "$VM_USER@$IP_DOLIBARR" "command -v docker >/dev/null || (sudo apt-get update >/dev/null && sudo apt-get install -y docker.io rsync >/dev/null)"
ssh "$VM_USER@$IP_DOLIBARR" "sudo usermod -aG docker $VM_USER"

# Forwarding IP pour Docker
ssh "$VM_USER@$IP_DOLIBARR" "sudo sysctl -w net.ipv4.ip_forward=1 >/dev/null && echo 'net.ipv4.ip_forward=1' | sudo tee /etc/sysctl.d/99-docker.conf"
ssh "$VM_USER@$IP_DOLIBARR" "sudo systemctl enable docker && sudo systemctl restart docker"
```

- **Environnement :** Installation de Docker et rsync.
- **Droits :** Ajout de l'utilisateur aux groupes Docker pour éviter l'usage de `sudo`.
- **Réseau :** Activation de l'IP Forwarding (`net.ipv4.ip_forward=1`) pour assurer la connectivité réseau interne des conteneurs Docker.

## 4. Reverse Proxy / Traefik 
Configuration de la VM `proxy` ($IP_PROXY) pour la gestion du trafic entrant :

```bash
# 3. Proxy (Traefik) 
echo "Installation sur le proxy ($IP_PROXY)..."
ssh "$VM_USER@$IP_PROXY" "command -v docker >/dev/null || (sudo apt-get update >/dev/null && sudo apt-get install -y docker.io >/dev/null)"
ssh "$VM_USER@$IP_PROXY" "sudo usermod -aG docker $VM_USER"

# Configuration Traefik
ssh "$VM_USER@$IP_PROXY" "mkdir -p ~/traefik/dynamic"
cat > "$SCRIPT_DIR/temp_traefik.yml" <<EOF
api:
  dashboard: true
  insecure: true
entryPoints:
  web:
    address: ':80'
providers:
  file:
    directory: /etc/traefik/dynamic
    watch: true
EOF
scp "$SCRIPT_DIR/temp_traefik.yml" "$VM_USER@$IP_PROXY:~/traefik/traefik.yml"
rm "$SCRIPT_DIR/temp_traefik.yml"

# Docker Run Traefik
ssh "$VM_USER@$IP_PROXY" "sudo docker rm -f traefik 2>/dev/null || true"
ssh "$VM_USER@$IP_PROXY" "sudo docker run -d --name traefik --restart always -p 80:80 -p 8080:8080 -v /home/$VM_USER/traefik/traefik.yml:/etc/traefik/traefik.yml -v /home/$VM_USER/traefik/dynamic:/etc/traefik/dynamic traefik:v2.10"
```

- **Installation :** Mise en place de Docker.
- **Fichiers de bord :** Création du répertoire de configuration du fichier `traefik.yml`. Définition des points d'entrées (port 80).
- **Déploiement :** Lancement du conteneur Traefik avec montage des volumes pour la configuration statique et dynamique.

## 5. Backup
Préparation de la VM `backup` ($IP_BACKUP) :

```bash
# 4. Backup
echo "Installation sur le backup ($IP_BACKUP)..."
ssh "$VM_USER@$IP_BACKUP" "sudo apt-get update >/dev/null && sudo apt-get install -y postgresql-client rsync sshpass >/dev/null"
ssh "$VM_USER@$IP_BACKUP" "mkdir -p ~/scripts ~/backups"
```

- **Outils :** Installation du client PostgreSQL (pour les dumps), rsync (transfert de fichiers) et sshpass.
- **Structure :** Création des dossiers `~/scripts` et `~/backups` pour l'organisation des données.

---

Etape précédante --> [Procédure 1 - Configuration de l'accès SSH](Procedure1.md) <--  

Etape suivante --> [Procédure 3 - Configuration des Sauvegardes](Procedure3.md) <--