

## 📄 Partie 4 : `04-base-donnees.md`

### Utilisation d’une base PostgreSQL

Synapse nécessite des paramètres spécifiques pour fonctionner correctement avec PostgreSQL (encodage UTF8 et collation C).

1. **Préparation de la base** (Côté serveur PostgreSQL) :
```bash
sudo -u postgres psql -c "CREATE DATABASE matrix WITH ENCODING='UTF8' LC_COLLATE='C' LC_CTYPE='C';"

```


2. **Configuration de Synapse** :
Dans `homeserver.yaml`, remplacez le bloc `database` par :
```yaml
database:
  name: psycopg2
  args:
    user: synapse_user
    password: votre_password
    dbname: matrix
    host: 10.42.xx.1
    port: 5432

```


3. **Vérification** :
Installez le client pour tester la connexion manuellement :
```bash
sudo apt install -y postgresql-client
psql -h 10.42.xx.1 -U synapse_user -d matrix

```






4. **Maintenance et Logs** :
* **Logs Synapse** : `/var/log/matrix-synapse/homeserver.log`
* **Statut DB** : `pg_lsclusters` (Vérifiez que la ligne est verte, allez voir les logs indiqués en cas de problème).


---

- Page précédente: [Installation de Synapse](03-installation-synapse.md)
- Page suivante: [Création des utilisateurs Matrix](05-utilisateurs.md)