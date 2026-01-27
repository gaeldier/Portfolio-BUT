# Connexion SSH au serveur de virtualisation

## 🎯 Objectif
Se connecter au serveur `dattier.iutinfo.fr` pour contrôler les machines virtuelles.

## 🖥️ Connexion SSH
Ouvrez un terminal sur votre machine physique et lancez la commande de connexion :
```bash
ssh <votre_login>@dattier.iutinfo.fr
```
**Vérification de l'empreinte (Sécurité) :** SSH vous demandera si vous faites confiance au serveur. Vous verrez un message similaire à :
```
The authenticity of host 'dattier.iutinfo.fr (172.18.48.20)' can't be established.
ED25519 key fingerprint is SHA256:QynRpdPucTVcwhMrD3824pqUviVFCgPwxwhkDyGyVSg.
Are you sure you want to continue connecting (yes/no/[fingerprint])?
```

> Si l'empreinte correspond, tapez yes pour valider. Cette étape protège contre les attaques de type Man-in-the-middle. Cette vérification garantit que vous vous connectez bien au serveur officiel.

---

- Page précédente: [Sommaire](README.md)
- Page suivante: [Génération et gestion des clés SSH](générationgestionssh.md)