# Element - Client Matrix
---
Element est un client Matrix open source, disponible en version web, desktop et mobile. Il permet de se connecter à un serveur Matrix (comme Synapse) pour envoyer et recevoir des message. 


sudo apt install -y wget apt-transport-https
‍
sudo wget -O /usr/share/keyrings/element-io-archive-keyring.gpg https://packages.element.io/debian/element-io-archive-keyring.gpg
‍
echo "deb [signed-by=/usr/share/keyrings/element-io-archive-keyring.gpg] https://packages.element.io/debian/ default main" | sudo tee /etc/apt/sources.list.d/element-io.list

sudo apt update

sudo apt install element-web

Mettre son nom de serveur par défaut : 

```bash
sudo nano /etc/element-web/config.json
```
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

puis dans créer element-web dans /etc/nginx/sites-available/element-web.conf

```nginx
server {
    listen 8080; // Port d'écoute
    server_name matrix; // NOm du serveur

    root /usr/share/element-web;
    index index.html;

    client_max_body_size 50M; // Pour autoriser l'envoi de fichiers (autres que des messages textes)

    location / {
        try_files $uri $uri/ =404;
    }

    # Configuration de la sécurité et des headers
    location /_matrix/ {
        proxy_pass "http://10.42.131.1:8008"; // Adresse du serveur Synapse
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}  

```
créer le lien symbolique : 
```bash
sudo ln -s /etc/nginx/sites-available/element-web.conf /etc/nginx/sites-enabled/
```

Redémarrer Nginx : 
```bash
sudo systemctl restart nginx
```
 Et ça tourne ! rendez vous sur http://localhost:8080 

---