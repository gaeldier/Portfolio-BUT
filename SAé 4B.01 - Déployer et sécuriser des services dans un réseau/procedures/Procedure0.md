# Procédure 0 - Initialisation et Configuration des VMs

Cette procédure documente le fonctionnement du script `scripts/0_create_vms.sh`

> [!IMPORTANT]
>
> Pour suivre cette procédure, il est nécessaire d'être connecté sur la machine de virtualisation. Suivez [cette procédure](https://gitlab.univ-lille.fr/etu/2025-2026/s303/i-hamiti-vanhoutte/-/blob/main/procedures/connexion_machine_virtu_procedure.md) si ce n'est pas le cas.


## 1. Initialisation
Le script définit les paramètres de sécurité Bash et charge la configuration. Il commence par **supprimer les VMs existantes** pour garantir une installation propre.

```bash
# Suppression des VMs si elles existent
vmiut rm ${VMS[@]} || true
```

## 2. Clé SSH
Le script vérifie la présence d'une clé SSH publique locale. Si elle est absente, il la génère. Cette clé sera injectée dans les VMs pour permettre les connexions sans mot de passe.
```bash
# 1. Vérification de la clé SSH locale (pour l'injecter)
PUB_KEY_FILE="$HOME/.ssh/id_rsa.pub"
if [ ! -f "$PUB_KEY_FILE" ]; then
    echo "Génération d'une clé SSH locale..."
    ssh-keygen -t rsa -N "" -f "$HOME/.ssh/id_rsa"
fi
PUB_KEY_CONTENT=$(cat "$PUB_KEY_FILE")
```

## 3. Préparation du Bootstrap
Un script temporaire `remote_bootstrap_root.sh` est créé. Il sera exécuté en tant que `root` sur chaque VM pour :

```bash
# 2. Création du script de bootstrap (exécuté par root sur les VMs)
BOOTSTRAP_SCRIPT="$SCRIPT_DIR/remote_bootstrap_root.sh"
cat > "$BOOTSTRAP_SCRIPT" <<EOF
#!/bin/bash
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
```
- Installer `sudo`.
- Configurer l'utilisateur `user` avec des privilèges sudo sans mot de passe.
- Injecter la clé SSH publique dans `authorized_keys` avec les permissions.

## 4. Création des VMs
Le script boucle sur la liste des machines (`proxy`, `dolibarr`, `database`, `backup`) :
1. **Création** : `vmiut create` puis `vmiut start`.
2. **Attente** : Une pause de 20 secondes permet l'initialisation du système.
3. **Application du Bootstrap** : Exécution du script de configuration initiale via `vmiut exec -s`. Une seconde tentative est prévue en cas d'échec initial.

## 5. Configuration Réseau (IP Statique)
Pour chaque VM, une configuration réseau statique est appliquée. Le script réécrit complètement le fichier `/etc/network/interfaces` :

```bash
#mini-script temporaire SPÉCIFIQUE à la VM

    TMP_IP_SCRIPT="$SCRIPT_DIR/tmp_ip_${vm}.sh"
    wait

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
EOF

    chmod +x "$TMP_IP_SCRIPT"
```


Après l'application, l'ip est inscrite temporairement.

```bash
 # exécution sur la VM distante
    if vmiut exec -s "$TMP_IP_SCRIPT" "$vm"; then
        echo "L'IP $IP_DONNEE a été appliquée sur '$vm'."
    else
        echo "Erreur : changement d'IP sur '$vm'."
    fi
    # rm du script tempo à la fin
    rm -f "$TMP_IP_SCRIPT"

```

## 6. Finalisation
Une fois toutes les VMs configurées, le script :

```bash
#VMIUT ne rafraîchit pas les infos, on redémarre de force pour qu'il sync les IPs 
if vmiut restart ${VMS[@]} ; then
    echo "Redémarrage des VMs pour rafraîchir les IPs"
else
    echo "Erreur lors du redémarrage des VMs"
fi 
wait 
echo "Récupération des IPs des VMs..."
source "$SCRIPT_DIR/../config.conf" # recharge les IPs mises à jour & les futures variables d'environnement


# Nettoyage
rm -f "$BOOTSTRAP_SCRIPT" "$TMP_IP_SCRIPT"
```
1. **Redémarre toutes les VMs** simultanément pour valider la persistance de la configuration et rafraîchir les données dans `vmiut`.
2. **Supprime les scripts temporaires** utilisés pour le bootstrap et la configuration IP.

---

Etape suivante  --> [Procédure 1 - Configuration de l'accès SSH](Procedure1.md) <--

Etape précédante --> [ <--

