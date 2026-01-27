# Guide d'installation d'un Reverse Proxy pour Synapse avec Nginx

## 1. Introduction
Le reverse proxy permet de ne pas exposer directement le serveur Synapse et de gérer la redirection des requêtes HTTP. Cette procédure décrit les étapes pour installer et configurer Nginx comme reverse proxy.

### Objectifs
- Installer Nginx sur une machine dédiée.
- Configurer Nginx pour rediriger les requêtes vers Synapse (10.42.xx.1:8008).
- Vérifier le fonctionnement du reverse proxy.
- Mettre en place la redirection SSH pour accéder au proxy depuis la machine physique.

## 2. Préparation de la machine virtuelle
Se connecter en SSH :
```bash
ssh user@10.42.xx.2
```

## 3. Installation de Nginx
Mettre à jour les paquets :
```bash
sudo apt update
```
Installer Nginx :
```bash
sudo apt install nginx -y
```
Vérifier l'état de Nginx :
```bash
systemctl status nginx
```
Tester localement :
```bash
curl http://10.42.xx.2
```

## 4. Configuration du Reverse Proxy

1. Supprimer le fichier de configuration par défaut :
```bash
sudo rm /etc/nginx/sites-enabled/default
```

2. Modifier les fichiers /etc/hosts de chaque machine pour ajouter les entrées suivantes :

```bash
10.42.131.1	matrix
10.42.147.1	postgresql
10.42.147.2	element
10.42.147.3	rproxy
```
Puis sur toutes les machines, exécuter :
```bash
sudo reboot # Pour appliquer les modifications
```


Modifier la configuration par défaut de Nginx :
```bash
sudo nano /etc/nginx/sites-available/default
```

Remplacer le contenu par :
```nginx
server {
    listen 80;
    server_name _;

    location / {
        proxy_pass http://10.42.xx.1:8008;

        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### Explications :
- `listen 80` : Nginx écoute sur le port HTTP standard.
- `proxy_pass` : redirige les requêtes vers Synapse.
- `X-Forwarded-*` : permet à Synapse de connaître l'adresse IP et le protocole du client.

---

## 5. Redémarrage de Nginx
Vérifier la configuration :
```bash
sudo nginx -t
```
Recharger Nginx :
```bash
sudo systemctl reload nginx
```

## 6. Tests
### Depuis la VM :
```bash
curl http://localhost
```
### Depuis la machine Synapse :
```bash
curl http://10.42.xx.2
```
### Depuis la machine physique (via redirection SSH) :
```bash
curl http://localhost:9090
```
Si une réponse JSON de Synapse est reçue, le reverse proxy fonctionne correctement.

## 7. Redirection SSH
Depuis la machine physique, exécuter :
```bash
ssh -L 9090:10.42.xx.2:80 user@10.42.xx.2
```
- Les requêtes sur `localhost:9090` sont redirigées vers le reverse proxy.
- Synapse n'est plus exposé directement sur le port 8008.

--- 
## Reverse proxy sur vm séparée

creer la vm, init et nom perso

installer nginx

supprimer le fichier default dans sites-available pour récuperer le port 80

sudo rm /etc/nginx/sites-enabled/default


créer un nouveau fichier de conf dans sites-available/reverse-proxy.conf
```nginx
server {
    listen 80;
    server_name _;

    location / {
        proxy_pass http://10.42.xx.1:8008;

        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

faire le lien symbolique dans sites-enabled

```bash
sudo ln -s /etc/nginx/sites-available/reverse-proxy.conf /etc/nginx/sites-enabled/
```

tester nginx :thumbsup:
```bash
sudo nginx -t
```
recharger nginx
```bash
sudo systemctl reload nginx
```
tester le reverse proxy depuis la vm reverse proxy
```bash
curl http://localhost
```
tester depuis la machine synapse
```bash
curl http://10.42.xx.2
```
### Si tout est ok, faire la redirection ssh depuis la machine physique
```bash
ssh -L 9090:10.42.xx.2:80
