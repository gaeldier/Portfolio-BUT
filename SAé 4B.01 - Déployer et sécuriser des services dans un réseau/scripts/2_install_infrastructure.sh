#!/bin/bash
#==============================================
# Installation Automatisée des Services
# SAÉ 4B.01 - Hébergement Dolibarr
#==============================================
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../config.conf"
source "$SCRIPT_DIR/../formattage.conf"


echo_info "=== Installation des Services ==="
echo_info "Note: La configuration SSH sans mot de passe doit être valide."

# 1. Base de Données (PostgreSQL)
echo_info "Installation sur VM Database ($IP_DATABASE)..."
# Installation
ssh "$VM_USER@$IP_DATABASE" "sudo apt-get update >/dev/null && sudo apt-get install -y postgresql >/dev/null"
# Configuration pg_hba.conf pour autoriser Dolibarr
# On ajoute la ligne si elle n'existe pas déjà
ssh "$VM_USER@$IP_DATABASE" "sudo grep -q '$IP_DOLIBARR' /etc/postgresql/*/main/pg_hba.conf || echo \"host all all $IP_DOLIBARR/32 md5\" | sudo tee -a /etc/postgresql/*/main/pg_hba.conf"
echo_success "Configuration de pg_hba.conf terminée."

# Configuration listen_addresses (Force à *)
# On utilise sed pour modifier la ligne existante (commentée ou non)
ssh "$VM_USER@$IP_DATABASE" "sudo sed -i \"s/^#*listen_addresses = .*/listen_addresses = '*' /\" /etc/postgresql/*/main/postgresql.conf"
# Assurance supplémentaire : si le fichier ne contenait pas la ligne standard
ssh "$VM_USER@$IP_DATABASE" "grep -q \"listen_addresses = '*'\" /etc/postgresql/*/main/postgresql.conf || echo \"listen_addresses = '*'\" | sudo tee -a /etc/postgresql/*/main/postgresql.conf"
echo_success "Configuration de listen_addresses terminée."

# Restart 
ssh "$VM_USER@$IP_DATABASE" "sudo systemctl restart postgresql"
echo_success "Service PostgreSQL redémarré."

# 2. Application (Docker)
echo_info "Installation sur VM Dolibarr ($IP_DOLIBARR)..."
ssh "$VM_USER@$IP_DOLIBARR" "command -v docker >/dev/null || (sudo apt-get update >/dev/null && sudo apt-get install -y docker.io rsync >/dev/null)"
ssh "$VM_USER@$IP_DOLIBARR" "sudo usermod -aG docker $VM_USER"
# Activation de l'IP Forwarding pour Docker (évite les problèmes de connectivité)
ssh "$VM_USER@$IP_DOLIBARR" "sudo sysctl -w net.ipv4.ip_forward=1 >/dev/null && echo 'net.ipv4.ip_forward=1' | sudo tee /etc/sysctl.d/99-docker.conf"
# Redémarrage Docker si nécessaire pour appliquer les règles iptables
ssh "$VM_USER@$IP_DOLIBARR" "sudo systemctl enable docker && sudo systemctl restart docker"

# 3. Proxy (Traefik) 
echo_info "Installation sur VM Proxy ($IP_PROXY)..."
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
echo_info "Configuration de Traefik terminée, déploiement en cours..."
scp "$SCRIPT_DIR/temp_traefik.yml" "$VM_USER@$IP_PROXY:~/traefik/traefik.yml"
rm "$SCRIPT_DIR/temp_traefik.yml"
echo_success "Fichier de configuration Traefik déployé."

# Docker Run Traefik
ssh "$VM_USER@$IP_PROXY" "sudo docker rm -f traefik 2>/dev/null || true"
ssh "$VM_USER@$IP_PROXY" "sudo docker run -d --name traefik --restart always -p 80:80 -p 8080:8080 -v /home/$VM_USER/traefik/traefik.yml:/etc/traefik/traefik.yml -v /home/$VM_USER/traefik/dynamic:/etc/traefik/dynamic traefik:v2.10"

# 4. Backup
echo "Installation sur VM Backup ($IP_BACKUP)..."
ssh "$VM_USER@$IP_BACKUP" "sudo apt-get update >/dev/null && sudo apt-get install -y postgresql-client rsync sshpass >/dev/null"
ssh "$VM_USER@$IP_BACKUP" "mkdir -p ~/scripts ~/backups"
# Infra terminée
echo_success "Installation des services terminée !"
