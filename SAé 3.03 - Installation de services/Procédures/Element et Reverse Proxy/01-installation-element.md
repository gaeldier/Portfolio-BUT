# Installation et configuration de Element Web
---

**Element Web** est un client Matrix fonctionnant exclusivement côté navigateur.

Contrairement à Synapse :
- il ne possède pas de serveur applicatif
- il est composé uniquement de fichiers statiques (HTML, CSS, JavaScript)

Pour le rendre accessible, il est donc nécessaire d’utiliser un serveur web.

## ⚖️ Choix du serveur web

Deux solutions principales ont été étudiées :

**Apache**
- Très répandu
- Configuration riche
- Plus lourd en ressources

**Nginx (choisi)**
- Léger et performant
- Excellente gestion des fichiers statiques
- Très adapté au reverse proxy

**Choix retenu :** Nginx, car il sera également utilisé pour le reverse proxy.

## 📦 Installation des dépendances

Pour installer le package element-web, il faut exécuter les commandes suivantes dans le terminal de la machine virtuelle:
```bash
sudo apt install -y wget apt-transport-https
```

Ensuite, il faut ajouter la clé officielle Element :
```bash
sudo wget -O /usr/share/keyrings/element-io-archive-keyring.gpg https://packages.element.io/debian/element-io-archive-keyring.gpg
```

Puis, ajouter le dépôt element :
```bash
echo "deb [signed-by=/usr/share/keyrings/element-io-archive-keyring.gpg] https://packages.element.io/debian/ default main" | sudo tee /etc/apt/sources.list.d/element-io.list
```

Et mettre à jour la liste des paquets :
```bash
sudo apt update
```

## 📥 Installation de Element Web
Il faut utiliser la commande suivante pour l'installation:
```bash
sudo apt install element-web
```
Les fichiers sont installés dans : `/usr/share/element-web`

Maintenant que element-web est installé, nous devons rendre accessible le serveur Element Web sur le port `8080` de la machine virtuelle, en reliant element-web au serveur nginx.

Pour cela nous devons modifier le fichier de configuration en faisant :
```bash
sudo nano /etc/element-web/config.json
```

avec le contenu suivant :
```json
"default_server_config": {
    "m.homeserver": {
        "base_url": "http://localhost:8080",
        "server_name": "saule13.iutinfo.fr"
    },
    "m.identity_server": {
        "base_url": "https://vector.im"
    }
}
```

Une fois que cela est fait, nous devons configurer Nginx pour Element Web en créant le fichier suivant :

```bash
sudo nano /etc/nginx/sites-available/element-web.conf
```

et en mettant dans ce fichier :
```nginx
server {
    listen 8080;
    server_name matrix;

    root /usr/share/element-web;
    index index.html;

    client_max_body_size 50M;

    location / {
        try_files $uri $uri/ =404;
    }
}
```

Ensuite, nous devons créer le lien symbolique avec :
```bash
sudo ln -s /etc/nginx/sites-available/element-web.conf /etc/nginx/sites-enabled/
```

Il suffit ensuite de le rédemarrer en faisant :
```bash
sudo systemctl restart nginx
```

## 🧪 Tests

Afin de vérifier le bon fonctionnement, nous pouvons depuis la machine virtuelle accéder à `http://localhost:8080`.

L’interface **Element Web** devrait s’afficher.

## 📔 Informations supplémentaires:
- [Documentation de `nginx`](https://nginx.org/en/docs/)

---

- Page précédente: [Sommaire](README.md)
- Page suivante: [Installation d'un reverse proxy](02-reverse-proxy.md)