# Mise à jour du système et installation des outils de base

Le modèle distribué peut être ancien et il est toujours important d'avoir les dernières versions des logiciels sur les machines. Mettre à jour nos paquets assure sécurité et compatibilité.

Nous allons donc procéder à une mise à jour du système.

## 💼 Étapes (en tant que root)
- Taper `ssh root@10.42.xx.1` depuis la machine physique sera ** refusé**. C'est normal : la connexion root par SSH est désactivée pour la sécurité.
- **Procédure recommandée :**
  - Se connecter avec l'utilisateur `user` :
    ```bash
    ssh user@10.42.xx.1
    ```
  - Devenir root sur la VM :
    ```bash
    su --login
    ```
  - `--login` (ou `-l`) : exécute un *login shell* pour root qui charge l'environnement de root, son PATH et ses variables.

## 🗂️ Mise à jour et installation des outils via `apt`

Une fois connecté au compte administrateur, nous pouvons exécuter les commandes suivantes :

```bash
apt update && apt full-upgrade -y
```

> `full-upgrade` peut remplacer ou supprimer des paquets pour résoudre des dépendances. On vous demandera parfois d'accepter l'installation d'un nouveau noyau ou la configuration de GRUB — cochez `/dev/sda` si demandé.

Nous pouvons ensuite redémarrer la machine :
```bash
reboot
```

Et installer des outils utiles:
```bash
apt install -y vim less tree rsync
```
- `vim` : éditeur texte avancé
- `less` : pagineur pour lire un fichier page par page
- `tree` : affiche l'arborescence des dossiers
- `rsync` : outil de synchronisation de fichiers

## ⚙️ Configuration du client SSH sur la machine physique (fichier `~/.ssh/config`)
Nous pouvons créer des alias pour simplifier les commandes SSH et permettre le `ProxyJump` (se connecter à une VM via la machine de virtualisation).

Exemple de fichier `~/.ssh/config` :

Modifier `~/.ssh/config` sur la machine physique et ajouter :

```
Host virt
    HostName <votre_login>@dattier.iutinfo.fr
    User login
    ForwardAgent yes

Host vm
    HostName 10.42.xx.1
    User user
    ProxyJump virt
```

- `ForwardAgent yes` : permet d'utiliser l'agent SSH (donc les clés) lorsqu'on saute via la machine de virtualisation.
- Après cela :
  - `ssh virt` → connecte à la machine de virtualisation.
  - `ssh vm` → connecte automatiquement à la VM (via la machine de virtualisation).

## 📔 Informations supplémentaires:
- [Guide Debian pour `apt`](https://www.debian.org/doc/manuals/aptitude/)

---

- Page précédente: [Configuration réseau](configurationreseau.md)
- Page suivante: [Changement du nom d'hôte de la VM](changementnomhote.md)