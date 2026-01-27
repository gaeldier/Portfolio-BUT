
## 📄 Partie 3 : `03-installation-synapse.md`

### Installation de Synapse

[Image de l'architecture Matrix Federation]

1. **Dépôts officiels** :
```bash
sudo apt install -y lsb-release wget apt-transport-https
wget -O /usr/share/keyrings/matrix-org-archive-keyring.gpg https://packages.matrix.org/debian/matrix-org-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/matrix-org-archive-keyring.gpg] https://packages.matrix.org/debian/ $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/matrix-org.list
sudo apt update

```


2. **Installation** :
```bash
sudo apt install -y matrix-synapse-py3 yamllint

```


*Indiquez `machine-physique.iutinfo.fr:8008` lors de la configuration.*

3. **Configuration réseau** dans `/etc/matrix-synapse/homeserver.yaml` :
Modifiez la section `listeners` pour autoriser l'écoute sur l'IP de la VM :
```yaml
listeners:
  - port: 8008
    bind_addresses:
      - 127.0.0.1
      - 10.42.xx.1

```

---

- Page précédente: [Accès au service depuis la machine physique](02-acces-distant.md)
- Page suivante: [Utilisation d’une base PostgreSQL](04-psql.md)


