# Installation d'un reverse proxy
---

Un **reverse proxy** est un serveur intermédiaire qui reçoit les requêtes des clients et les transmet à un serveur interne.

Il permet notamment :
- de ne pas exposer directement le serveur applicatif
- d’améliorer les performances
- de centraliser la sécurité
- de préparer la mise en place du TLS

## ⚖️ Choix du reverse proxy

Le choix d’un reverse proxy dépend fortement du contexte d’utilisation, des performances attendues et de la complexité de l’infrastructure. Dans notre cas, Nginx apparaît comme la solution la plus adaptée pour un environnement classique orienté web.

**Solutions étudiées :**
- Nginx
- Apache
- Traefik
- HAProxy

**Choix retenu:** __Nginx__

**Performances**
- Nginx offre une faible latence et gère efficacement un grand nombre de requêtes simultanées, ce qui le rend parfaitement adapté aux applications web courantes.

**TLS / SSL**
- La gestion du chiffrement est robuste et maîtrisée via OpenSSL, permettant un contrôle précis des certificats et des paramètres de sécurité.

**Contenus statiques**
- Nginx est nativement conçu pour servir des fichiers statiques, contrairement à Traefik et HAProxy.

**Simplicité et flexibilité**
- Sa configuration est simple pour des cas standards tout en restant extensible pour des architectures plus avancées.

**Sécurité**
- Nginx intègre des mécanismes clés : SSL/TLS, contrôle d’accès IP, rate limiting et WAF.

✅ **Nginx** est le **choix idéal** pour un reverse proxy web performant, polyvalent et facile à maintenir, sans la complexité d’outils plus spécialisés comme HAProxy ou Traefik.

## 🖥️ Déployer une nouvelle machine virtuelle

Tout d'abord, nous devons déployer une toute nouvelle machine virtuelle, dédiée uniquement pour le reverse proxy avec comme critères:
- Nom : `rproxy`
- IP : `10.42.xx.2`
- Accès SSH activé

Pour cela, nous pouvons suivre les procédures de création et de configuration de machines virtuelles vus dans la première partie : [ici](../Mise%20en%20Place/README.md)

## 📦 Installation de Nginx

Une fois que la nouvelle machine virtuelle est créée et configurée, il faut installer sur cette machine un serveur nginx, qui redirigera les requêtes vers notre instance Matrix.

Pour cela, nous pouvons exécuter les commandes vus précédemment dans la [troisième partie](../Installation%20Synapse/01-service-test-nginx.md)

```bash
sudo apt update
sudo apt install nginx -y

systemctl status nginx
```

## ⚙️ Configuration du reverse proxy

Une fois que nginx est installé, nous pouvons configurer le reverse proxy pour rediriger les requêtes vers notre instance de Matrix.

Pour cela, nous devons supprimer la configuration par défaut :
```bash
sudo rm /etc/nginx/sites-enabled/default
```

et créer la configuration suivante :
```bash
sudo nano /etc/nginx/sites-available/reverse-proxy.conf
```

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

Après, il suffit de créer le lien symbolique avec la commande suivante :
```bash
sudo ln -s /etc/nginx/sites-available/reverse-proxy.conf /etc/nginx/sites-enabled/
```

## 🔄 Redémarrage et tests

Pour vérifier que tout fonctionne correctement sans erreurs, nous pouvons tester la configuration :
```bash
sudo nginx -t
```

Puis, recharger Nginx :
```bash
sudo systemctl reload nginx
```

Et tester localement :
```bash
curl http://localhost
```

## 🔀 Redirection SSH

Si tout fonctionne, nous pouvons faire la redirection ssh depuis la machine physique :
```bash
ssh -L 9090:10.42.xx.2:80 user@10.42.xx.2
```
- `localhost:9090`, reverse proxy
- Synapse n’est plus exposé directement

---

- Page précédente: [Installation et configuration de Element Web](01-installation-element.md)
- Page suivante: [Migration vers l'Architecture Finale](../Architecture%20Finale/README.md)