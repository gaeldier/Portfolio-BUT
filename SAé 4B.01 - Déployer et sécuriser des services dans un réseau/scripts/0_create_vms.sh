#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Source des "echo_info" etc.. Pour un affichage dans le style de systemd
source "$SCRIPT_DIR/../formattage.conf"


# Liste des VMs à créer
VMS=("proxy" "dolibarr" "database" "backup")

echo_info "0. Vérification / Création des VMs "

# 0. Supprime les VM  
    vmiut rm ${VMS[@]} || true
    echo_info "Suppression des VMs déjà existantes"

# 1. Vérification de la clé SSH locale (pour l'injecter)
PUB_KEY_FILE="$HOME/.ssh/id_rsa.pub"
if [ ! -f "$PUB_KEY_FILE" ]; then
    echo_info "Génération d'une clé SSH locale..."
    ssh-keygen -t rsa -N "" -f "$HOME/.ssh/id_rsa"
fi
PUB_KEY_CONTENT=$(cat "$PUB_KEY_FILE")

# 2. Création du script de bootstrap (exécuté par root sur les VMs)
BOOTSTRAP_SCRIPT="$SCRIPT_DIR/remote_bootstrap_root.sh"
cat > "$BOOTSTRAP_SCRIPT" <<EOF
#!/bin/bash

set -euo pipefail
# Ce script est exécuté en tant que ROOT sur la VM distante via 'vmiut exec'

# 1. Installation de sudo 
if ! command -v sudo &> /dev/null; then
    apt-get update >/dev/null 2>&1
    apt-get install -y sudo >/dev/null 2>&1
fi

# 2. Configuration utilisateur 'user'
# Création si n'existe pas (normalement il existe sur l'image de base)
id -u user &>/dev/null || useradd -m -s /bin/bash user

# Ajout au groupe sudo
usermod -aG sudo user

# Sudo sans mot de passe
echo "user ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/user-nopasswd
chmod 0440 /etc/sudoers.d/user-nopasswd

# 3. Injection Clé SSH
mkdir -p /home/user/.ssh
# On ajoute la clé seulement si elle n'y est pas déjà
if ! grep -qF "$PUB_KEY_CONTENT" /home/user/.ssh/authorized_keys 2>/dev/null; then
    echo "$PUB_KEY_CONTENT" >> /home/user/.ssh/authorized_keys
fi

# Permissions SSH strictes
chown -R user:user /home/user/.ssh
chmod 700 /home/user/.ssh
chmod 600 /home/user/.ssh/authorized_keys

EOF
source "$SCRIPT_DIR/../config.conf"
chmod +x "$BOOTSTRAP_SCRIPT"


# 3. Boucle sur les VMs
for vm in "${VMS[@]}"; do
    echo_info "Vérification de la VM '$vm'..."
    
    # Vérifie si la VM existe
    if ! vmiut info "$vm" &>/dev/null; then
        echo "La VM '$vm' n'existe pas. Création en cours..."
        vmiut create "$vm"
        echo_success "VM '$vm' créée."
        echo_info "Démarrage de la VM '$vm'..."
        vmiut start "$vm" 
        sleep 30
        echo_success "Machine '$vm' démarrée."
    else
        echo_info "La VM '$vm' existe déjà."
        # On s'assure qu'elle est démarrée
        if ! vmiut info "$vm" | grep -q "etat=running"; then
            echo_info "Démarrage de la VM '$vm'..."
            vmiut start "$vm" 
            sleep 30
            echo_success "Machine '$vm' démarrée."
        fi
    fi

    sleep 15
    echo_info "Application du bootstrap (SSH + Sudo) sur '$vm'..."
    # On exécute le script de bootstrap via vmiut exec
    if vmiut exec -s "$BOOTSTRAP_SCRIPT" "$vm"; then
        echo_success "Bootstrap réussi sur '$vm'."
    else
        echo_warning "Échec ou VM non prête. Nouvelle tentative dans 10s..."
        sleep 15
        vmiut exec -s "$BOOTSTRAP_SCRIPT" "$vm" || echo_error "Impossible d'appliquer le bootstrap sur '$vm' (ssh/réseau pas encore prêt ?)"
    fi
    sleep 5

    # Changement des ip des machines pour correspondre à nos plages
    # Dans une future version de vmiut, on pourra exécuter : vmiut exec -s ./changeIp.sh "$vm" "$vm"
    echo "Configuration de l'IP pour la machine '$vm'..."
    
    # Affecte une IP spécifique à chaque VM selon son nom
    case "$vm" in
        "proxy")    IP_DONNEE="10.42.147.50" ;;
        "dolibarr") IP_DONNEE="10.42.147.51" ;;
        "database") IP_DONNEE="10.42.147.52" ;;
        "backup")   IP_DONNEE="10.42.147.53" ;;
        *)          
            echo "Erreur : VM inconnue ($vm)."
            continue 
            ;;
    esac

    # Mini script qui réécrit la configuration réseau avec nos plages d'IPs
    TMP_IP_SCRIPT="$SCRIPT_DIR/tmp_ip_${vm}.sh"

    cat > "$TMP_IP_SCRIPT" <<EOF
    #!/bin/bash
    sudo ip addr flush dev enp0s3 && 
    sudo ip addr add $IP_DONNEE/24 dev enp0s3

    cat <<ConfigInterfaces | sudo tee /etc/network/interfaces >/dev/null
auto lo
iface lo inet loopback

allow-hotplug enp0s3
iface enp0s3 inet static
    address $IP_DONNEE
    netmask 255.255.0.0
    gateway 10.42.0.1
ConfigInterfaces
EOF

    chmod +x "$TMP_IP_SCRIPT"

    # exécution sur la VM distante
    if vmiut exec -s "$TMP_IP_SCRIPT" "$vm"; then
        echo_success "L'IP $IP_DONNEE a été appliquée sur '$vm'."
    else
        echo_error "Erreur : changement d'IP sur '$vm'."
    fi
done


#VMIUT ne rafraîchit pas les infos, on redémarre de force pour qu'il sync les IPs & pour appliquer la configuration réseau permanente
for vm in "${VMS[@]}"; do
    vmiut restart "$vm"
done
echo_success "Redémarrage des VMs pour rafraîchir les IPs"


echo_info "Récupération des IPs des VMs..."
source "$SCRIPT_DIR/../config.conf" # recharge les IPs mises à jour & les futures variables d'environnement


# Nettoyage
rm -f "$BOOTSTRAP_SCRIPT" "$TMP_IP_SCRIPT"

echo "=== Fin de l'initialisation des VMs ==="
