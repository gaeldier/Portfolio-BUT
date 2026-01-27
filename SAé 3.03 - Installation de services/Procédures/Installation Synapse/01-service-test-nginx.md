

## 📄 Partie 1 : `01-service-test_nginx.md`

### Mise en place d’un service HTTP de test (nginx)

Avant d'installer Synapse, nous validons que la VM peut exposer un service Web.

1. **Installation de Nginx** :
```bash
sudo apt update
sudo apt install -y nginx curl

```


2. **Vérification du statut** :
```bash
sudo systemctl status nginx

```


3. **Test local** :
Exécutez cette commande pour vérifier que le serveur répond sur lui-même :
```bash
curl http://localhost -I

```

---

- Page précédente: [Sommaire](README.md)
- Page suivante: [Mise en place d’un service HTTP de test (nginx)](02-acces-distant.md)


