#!/bin/bash
#==============================================
# Configuration SSH Automatisée
# SAÉ 4B.01 - Hébergement Dolibarr
#==============================================
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
#source "$SCRIPT_DIR/../config.conf"
source "$SCRIPT_DIR/../formattage.conf"


echo "=== Configuration des Clés SSH ==="

# 1. Générer une clé SSH si elle n'existe pas
if [ ! -f ~/.ssh/id_rsa.pub ]; then
    echo_info "Génération d'une nouvelle clé SSH..."
    ssh-keygen -t rsa -b 4096 -f ~/.ssh/id_rsa -N ""
fi

# 2. Copier la clé sur chaque VM
echo_info "Copie de la clé publique sur les VMs (Mot de passe 'user' requis)..."

for ip in "$IP_PROXY" "$IP_DOLIBARR" "$IP_DATABASE" "$IP_BACKUP"; do
    echo_info "--> Configuration pour $ip..."
    # On teste d'abord si la connexion sans mot de passe marche déjà
    if ssh -o BatchMode=yes -o ConnectTimeout=5 "$VM_USER@$ip" "echo OK" &>/dev/null; then
        echo_success "Connexion déjà configurée."
    else
        echo "Veuillez entrer le mot de passe pour $ip :"
        ssh-copy-id -o StrictHostKeyChecking=no "$VM_USER@$ip"
    fi
done

echo_success "Configuration SSH terminée !"
