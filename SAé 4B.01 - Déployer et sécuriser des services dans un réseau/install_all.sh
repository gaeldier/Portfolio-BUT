#!/bin/bash
#==============================================
# INSTALLATEUR PRINCIPAL
# SAÉ 4B.01 - Hébergement Dolibarr Automatisé
#==============================================
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Démarrage de l'installation complète..."

# 0. Création des VMs (si inexistantes)
echo "Etape 0: Vérification / Création des VMs"
bash "$SCRIPT_DIR/scripts/0_create_vms.sh"
sleep 3
clear
echo "étape 0 terminée."
# 1. Configuration des IPs (Vérification et Chargement)
echo "Etape 1: Récupération des IPs mises à jour "
source "$SCRIPT_DIR/config.conf"


# réécriture des ip en dur, vmiut info n'est pas fiable 
export IP_PROXY="10.42.147.50"
export IP_DOLIBARR="10.42.147.51"
export IP_DATABASE="10.42.147.52"
export IP_BACKUP="10.42.147.53"

echo "   Proxy: $IP_PROXY"
echo "   Dolibarr: $IP_DOLIBARR"
echo "   Database: $IP_DATABASE"
echo "   Backup: $IP_BACKUP"

# 2. Configuration SSH
echo -e "\n--- 2. Configuration SSH (Clés) ---"
bash "$SCRIPT_DIR/scripts/1_setup_ssh.sh"

# 3. Installation Infrastructure (Docker, Postgres, Traefik)
echo -e "\n--- 3. Installation Infrastructure ---"
bash "$SCRIPT_DIR/scripts/2_install_infrastructure.sh"

# 4. Configuration Backup
echo -e "\n--- 4. Configuration Backup Automatique ---"
bash "$SCRIPT_DIR/scripts/3_setup_backup.sh"

echo -e "\n INSTALLATION TERMINÉE !"
echo "---------------------------------------------------"
echo "Pour installer un nouveau client Dolibarr, placez-vous dans le dossier 'livrable_sae' :"
echo "   $ cd $(dirname "$0")"
echo "   $ ./scripts/4_deploy_new_client.sh <nom_du_client>"
echo "---------------------------------------------------"
