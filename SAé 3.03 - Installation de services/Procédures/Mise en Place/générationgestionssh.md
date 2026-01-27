# Génération et gestion des clés SSH

## 🎯 Objectif
Pour faciliter et sécuriser la connexion via `ssh` au serveur de virtualisation, nous allons générer une paire de clés SSH et ajouter la clé publique sur le serveur afin de nous connecter sans mot de passe.

## 🔑 Création d'une paire de clés SSH
Dans votre terminal (sur votre machine physique), lancez :
```bash
ssh-keygen -t ed25519 -C
```
- **Emplacement :** Appuyez sur `Entrée` pour le chemin par défaut.
- **Passphrase :** Fortement recommandée pour protéger votre clé privée sur votre disque.

- **Clé privée :** `~/.ssh/id_rsa` (NE PAS LA PARTAGER)
- **Clé publique :** `~/.ssh/id_rsa.pub` (c'est celle qu'on partage)

## 🔐 Copier la clé sur le serveur
Pour automatiser la connexion vers le serveur de virtualisation :
```
ssh-copy-id <votre_login>@dattier.iutinfo.fr
```

On vous demandera le mot de passe une dernière fois. Après cette étape, votre clé publique sera ajoutée dans `~/.ssh/authorized_keys` sur le serveur et les connexions futures utiliseront la clé.

**Si `ssh-copy-id` n'est pas disponible**, vous pouvez le faire manuellement :
- Affichez la clé publique :
```bash
cat ~/.ssh/id_rsa.pub
```
- Sur le serveur (après vous être connecté avec le mot de passe), éditez (ou créez) `~/.ssh/authorized_keys` et collez la ligne de la clé publique dedans (une clé par ligne). Assurez-vous des permissions :
```bash
mkdir -p ~/.ssh
chmod 700 ~/.ssh
# coller la clé dans ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys
```

## ⚙️ Utiliser l'agent SSH pour éviter de taper la passphrase à chaque fois (optionnel)
Un agent SSH garde en mémoire votre clé privée déchiffrée pendant la session afin d'éviter de taper la passphrase à chaque connexion.

Pour cela, démarrer ou utiliser l'agent (la plupart des environnements graphiques le font automatiquement). Sinon :
```bash
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_rsa
```
Entrez la passphrase une seule fois. L'agent la gardera pour la durée de la session.

---

- Page précédente: [Connexion SSH au serveur de virtualisation](connectionssh.md)
- Page suivante: [Création et gestion d’une machine virtuelle](creationvm.md)