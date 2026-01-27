sudo -u postgres createuser -P matrix

sudo -u postgres createdb -O matrix matrix

Se connecter :
psql -U matrix -d matrix -h localhost -W

Faire une table test 

create table test(bonjour text, aurevoir text);

Insérer une ligne dans la table test
insert into test values ('salut', 'salut');

Voir le contenu de la table test
select * from test;
tout qui marche 👍


Pour créer l'utilisateur matrix avec un mot de passe :
```bash
sudo -u postgres createuser --pwprompt matrix
``` 

sudo -u postgres createdb --encoding=UTF8 --locale=C --template=template0 --owner=matrix matrix

\l pour vérifier la création & utilisateurs

se connecter avec le compte matrix : 
psql -U matrix -d matrix -h localhost -d matrix

Modifier le fichier pg_hba.conf pour autoriser les connexions
```bash
nano /etc/postgresql/12/main/pg_hba.conf
```
Ajouter la ligne suivante à la fin du fichier :
```
host    matrix     matrix    10.42.131.1/32 scram-sha-256
``` 
modifier postgresql.conf pour écouter sur l'IP de la VM
```bash
nano /etc/postgresql/12/main/postgresql.conf
```
Trouver la ligne :
```#listen_addresses = 'localhost'
```
```
et la remplacer par :
```listen_addresses = '*'
```
## restart postgresql
```bash
systemctl restart postgresql 
```

```
# Pour toujours regarder si le service postgresql n'est pas en erreur (running/active avec systemctl mais érroné dans les logs)
```bash
pg_lsclusters
```
On verifie si la ligne est verte, si elle est rouge on va chercher dans les logs avec 

```bash
cat /var/log/postgresql/postgresql-12-main.log ## Ou dans logfile indiqué
```

