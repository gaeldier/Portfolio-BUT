# Changement du nom d'hôte de la VM

Changer le nom d'hôte de la machine virtuelle, permet de faire en sorte que la VM utilise un nom de domaine personnalisé (ex : `matrix`).

Pour cela, nous devons modifier les fichiers `/etc/hosts` et `/etc/hostname` :
```bash
echo "10.42.xx.1 matrix" >> /etc/hosts
echo matrix > /etc/hostname
```
- La première ligne fait correspondre l'IP au nom "matrix"
- La deuxième ligne met à jour le nom d'hôte de la machine
Si vous ne comprenez pas ">>" ou ">", cliquez [ici](../../GuideDesExpressionsInconnues.md) pour voir sa signification dans le guide des expressions inconnues.

Ensuite, nous pouvons appliquer le nouveau nom d'hôte sans redémarrer :
```bash
hostnamectl set-hostname matrix
```

---

- Page précédente: [Mise à jour du système et installation des outils de base](miseajoursysteme.md)
- Page suivante: [Configuration du service Postgresql](../Service%20Postgresql/README.md)