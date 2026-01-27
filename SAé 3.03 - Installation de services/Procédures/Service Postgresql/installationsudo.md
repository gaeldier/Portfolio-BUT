# Installation et configuration de sudo

Sous Linux, l’utilisateur `root` possède tous les droits sur le système. Pour des raisons de sécurité, il est déconseillé de travailler directement avec ce compte.

La commande sudo permet :
- d’exécuter une commande avec les droits administrateur
- de garder une trace des commandes exécutées
- d’éviter de partager le mot de passe de root

## 👨🏻‍💻 Installation de sudo
Comme la commande `sudo` n’est pas disponible, il faut l’installer avec le compte root :
```bash
su -
```

Nous pouvons l'installer ensuite en exécutant les commandes suivantes :
```bash
apt update
apt install sudo
```

- La première commande se charge de mettre à jour les dépôts du gestionnaire de paquets `apt`, propre à Debian, afin d'être sûr d'avoir les versions les plus récentes des outils souhaités.
- La deuxième commande installe l'outil `sudo` sur l'ordinateur.

## 👤 Autoriser un utilisateur à utiliser sudo
Sous Debian, les utilisateurs membres du groupe `sudo` peuvent utiliser la commande `sudo`.

Nous devons ajouter l’utilisateur `user` au groupe `sudo` :

```bash
usermod -aG sudo user
```
Explication des options :
`-a` : ajout au groupe sans retirer les groupes existants
`-G sudo` : groupe sudo

Pour que la modification soit effective, l’utilisateur doit :
- se déconnecter
- puis se reconnecter

Vérifier que l’utilisateur appartient bien au groupe sudo :
```bash
groups
```

Le groupe `sudo` devrait être présent dans la liste.

## 📔 Informations supplémentaires:
- [Documentation de `sudo`](https://www.sudo.ws/)

---

- Page précédente: [Sommaire](README.md)
- Page suivante: [Installation de PostgreSQL](postgresql.md)


