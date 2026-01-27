# SAÉ B – Déploiement d’une application - Installation et configuration de Synapse
---

## Objectif
Ce document décrit pas à pas l’installation et la configuration d’un serveur Matrix basé sur **Synapse** sur une machine virtuelle Debian, accessible depuis la machine physique via un tunnel SSH et utilisant une base de données **PostgreSQL**.

## Pré-requis

Avoir complété les étapes précédentes:
- [Mise en place de l’environnement et création de la VM](miseenplace.md)
- [Installation et configuration d'un serveur de données](servicepostgresql.md)

## 1. Accès à un service HTTP sur la VM
---

### But
Vérifier que la machine virtuelle peut héberger un service HTTP fonctionnel.

### 1.1 Installation d’un premier service pour tester (nginx)
Sur la machine virtuelle, en tant que root :
```bash
apt update
apt install -y nginx
```

### 1.2 Vérification du service
```bash
systemctl status nginx
```

Le service doit être en état `active (running)`.

### 1.3 Installation de curl
```bash
apt install -y curl
```

### 1.4 Test local
```bash
curl http://localhost -I
```

La page HTML par défaut de nginx doit s’afficher.

## 2. Accès au service depuis la machine physique
---

### Pourquoi l’accès direct est impossible
- Le service s’exécute sur la VM
- La VM est dans le réseau privé `10.42.0.0/16`
- Ce réseau n’est pas routé vers le réseau physique

La machine physique ne peut donc pas accéder directement au service.

### 2.1 Mise en place d’un tunnel SSH
Depuis la machine physique :

```bash
ssh -L 9090:localhost:80 user@10.42.xx.1
```

- Le port `9090` de la machine physique est redirigé vers le port `80` de la VM
- L’accès se fait via l’URL :

```
http://machine-physique.iutinfo.fr:9090
```

### 2.2 Configuration permanente via ~/.ssh/config
Sur la machine physique :

```ini
Host vm
    HostName 10.42.xx.1
    User user
    ProxyJump virt
    LocalForward 9090 localhost:80 # Pour nginx
    LocalForward 8008 localhost:8008 # Pour Synapse
```

## 3. Installation de Synapse
---

### Installation du paquet sous Debian

**Ajout du dépôt officiel :**
```bash
apt install -y lsb-release wget apt-transport-https
wget -O /usr/share/keyrings/matrix-org-archive-keyring.gpg https://packages.matrix.org/debian/matrix-org-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/matrix-org-archive-keyring.gpg] https://packages.matrix.org/debian/ $(lsb_release -cs) main" > /etc/apt/sources.list.d/matrix-org.list
apt update
```

**Installation de Synapse :**
```bash
apt install -y matrix-synapse-py3
```

Lors de l’installation, indiquer comme nom de serveur :

```
machine-physique.iutinfo.fr:8008
```

Les logs sont disponibles dans :
```
/var/log/matrix-synapse/homeserver.log
```

Installer yamllint :
```bash
apt install -y yamllint
```

### Paramétrage de l’accès à distance

Par défaut, Synapse écoute uniquement sur `127.0.0.1`.

Modifier le fichier :
```
/etc/matrix-synapse/homeserver.yaml
```

Exemple de configuration :

```yaml
listeners:
  - port: 8008
    tls: false
    type: http
    x_forwarded: true
    bind_addresses:
      - 127.0.0.1
      - 10.42.xx.1
    resources:
      - names: [client, federation]
        compress: false
```

Redémarrer Synapse :
```bash
systemctl restart matrix-synapse
```

### Configuration pour un réseau privé

Dans `homeserver.yaml`, cherchez la clé `trusted_key_servers` et modifiez-la :

Ajouter :
```yaml
trusted_key_servers: []
```

## Utilisation d’une base PostgreSQL
---

Synapse nécessite un encodage et une collation spécifiques (`UTF8` et `C`).

### 1. Supprimer l'ancienne base :
```bash
sudo -u postgres dropdb matrix
```

### 2. Recréer la base correctement :
```bash
sudo -u postgres psql 
CREATE DATABASE matrix WITH OWNER matrix TEMPLATE template0 ENCODING 'UTF8' LC_COLLATE='C' LC_CTYPE='C';
# Ou avec la synthaxe bash ci-dessus. 
```

### 3. Configuration Synapse :
Dans `homeserver.yaml`, remplacez la section `database` (sqlite3) par :
```yaml
database:
  name: psycopg2
  args:
    user: synapse_user
    password: votre_password
    database: matrix
    host: 10.42.xx.1
    port: 5432
    cp_min: 5
    cp_max: 10
```

Parler du blabla avec 
registration_shared_secret: [chaîne privée] &
Dans le fichier de configuration /etc/matrix-synapse/homeserver.yaml, ajoutez ces lignes avant le registration_shared_secret afin de permettre l'enregistrement de nouveaux utilisateurs via l'interface web (Element):
```bash
echo "enable_registration: true
enable_registration_without_verification: true" >> /etc/matrix-synapse/homeserver.yaml

echo "registration_shared_secret: "salut"" >> /etc/matrix-synapse/homeserver.yaml
```
décomenter la ligne server_name: "sae"
Pour la création des utilisateurs plus tard avec create_new_matrix_user 



une fois tout installé, on peut installer psql temporairement pour vérifier la connexion à la base de données

```bash 
apt install -y postgresql-client
psql -h 10.42.147.1 -U synapse_user -d matrix
```
Si l'invite de mot de passe apparaît et que la connexion s'établit, tout est bon 
Tester l'accès à Synapse depuis la machine physique via le tunnel SSH :
http://localhost:8008/ 

## Création des utilisateurs Matrix

Utiliser la commande suivante sur la VM pour créer un utilisateur :
```bash
register_new_matrix_user -c /etc/matrix-synapse/homeserver.yaml http://localhost:8008
```
# Pour toujours regarder si le service Synapse n'est pas en erreur (running/active avec systemctl mais érroné dans les logs)
```bash
pg_lsclusters
```
On verifie si la ligne est verte, si elle est rouge on va chercher dans les logs avec 
```bash
cat /var/log/matrix-synapse/homeserver.log ## Ou dans logfile indiqué
```
4. Récapitulons !

votre fichier `homeserver.yaml` doit contenir la configuration de la base de données PostgreSQL comme suit :

```yaml
server_name: "saule13.iutinfo.fr:8008"
pid_file: "/var/run/matrix-synapse.pid"

listeners:
  - port: 8008
    tls: false
    type: http
    x_forwarded: true
    bind_addresses:
      - 127.0.0.1
      - 10.42.131.1
    resources:
      - names: [client, federation]
        compress: false

database:
  name: psycopg2
  args:
    user: matrix
    password: matrix
    dbname: matrix
    host: 10.42.147.1
    port: 5432
    cp_min: 5
    cp_max: 10

log_config: "/etc/matrix-synapse/log.yaml"
media_store_path: /var/lib/matrix-synapse/media
signing_key_path: "/etc/matrix-synapse/homeserver.signing.key"
trusted_key_servers: []
enable_registration: true
enable_registration_without_verification: true
registration_shared_secret: "salut"
report_stats: no
```

On crée un utilisateur test avec la commande register_new_matrix_user
```bash
register_new_matrix_user -c /etc/matrix-synapse/homeserver.yaml # La commande étant assez documentée et automatisée, on laisse l'utilisateur saisir son identifiant et son mot de passe.
```
