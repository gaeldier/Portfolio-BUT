# Installation et configuration de PostgreSQL

PostgreSQL est un système de gestion de bases de données relationnelles, comparable à MySQL, MariaDB ou Oracle. Dans notre contexte, PostgreSQL est utilisé par Synapse pour gérer les utilisateurs de l’instance Matrix.

## 📦 Installation de PostgreSQL
Maintenant que `sudo` est installé et que les droits administrateur ont été accordés à notre utilisateur, il n’est plus nécessaire de se connecter au compte `root` avec la commande `su` : il suffit désormais de précéder les commandes de `sudo`.

Nous pouvons mettre à jour la liste des paquets :
```bash
sudo apt update
```

Puis, installons PostgreSQL et les outils complémentaires :
```bash
sudo apt install postgresql postgresql-contrib
```

Une fois l’installation terminée, **PostgreSQL** peut démarrer automatiquement.

On peut vérifier l'état du service avec la commande :
```bash
sudo systemctl status postgresql
```

Si **PostgreSQL** n'a pas été démarré automatiquement, nous pouvons le lancer manuellement avec la commande suivante:
```bash
sudo systemctl start postgresql
```

Le service doit être :
- actif (running)
- sans message d’erreur critique

## 👤 Création de l’utilisateur PostgreSQL `matrix`

Maintenant que PostgreSQL est installé et activé sur la machine virtuelle, nous devons configurer son serveur.

Pour cela, nous devons créer un utilisateur avec comme nom: `matrix` et mot de passe: `matrix`. Depuis un shell de l’utilisateur user (et non postgres), nous devons entrer la commande suivante :

```bash
sudo -u postgres createuser -P matrix
```

- L’option `-P` force la saisie d’un mot de passe
- Mot de passe utilisé ici : `matrix`

Nous pouvons ensuite créer la base de données appartenant à cet utilisateur :
```bash
sudo -u postgres createdb -O matrix matrix
```

## 🔌 Connexion à la base de données `matrix`

Après que la base de données a été créée, nous pouvons nous y connecter.

La connexion doit obligatoirement se faire en TCP (et non via socket local) :

```bash
psql -U matrix -d matrix -h localhost -W
```

Explication des options :
- `-U` : utilisateur PostgreSQL
- `-d` : base de données
- `-h localhost` : force une connexion réseau
- `-W` : demande le mot de passe

Une fois le mot de passe saisi, vous accéderez au prompt du shell **psql**, qui vous permettra de communiquer avec le serveur PostgreSQL, soit à l’aide de commandes SQL, soit via des commandes spéciales, comme `\?`, qui affiche la liste de toutes les commandes spéciales disponibles.

Nous pouvons effectuer des tests pour vérifier le bon fonctionnement de **PostgreSQL** :

**Création d’une table de test :**
```sql
CREATE TABLE test (
    bonjour TEXT,
    aurevoir TEXT
);
```

**Insertion d’une ligne :**
```sql
INSERT INTO test VALUES ('salut', 'salut');
```

**Lecture des données :**
```sql
SELECT * FROM test;
```

Si la ligne s’affiche correctement, **PostgreSQL fonctionne**.

Pour sortir de `psql`, on utilise la commande `exit`.

---

- Page précédente: [Installation et configuration de sudo](installationsudo.md)
- Page suivante: [Installation et configuration du Serveur Synapse](../Installation%20Synapse/README.md)
