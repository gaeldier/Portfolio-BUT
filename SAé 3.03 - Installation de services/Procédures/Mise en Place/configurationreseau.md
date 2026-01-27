# Configuration réseau

Une machine qui héberge un service (comme un serveur Matrix) doit conserver la même IP pour y accéder facilement et configurer les enregistrements DNS ou le routage.

## 📶 Modifications à apporter au réseau

On utilise ici l'interface `enp0s3`. Elle peut changer selon les machines. Remplacez-la par celle qui correspond à l'IP que vous avez (visible avec `ip addr show`).

```bash
ip addr show
```

Arrêter l'interface (pour modifier la configuration réseau)

```bash
sudo ifdown enp0s3
```

Modifier `/etc/network/interfaces` (exemple avec `nano`) :
```bash
sudo nano /etc/network/interfaces
```

Remplacez le contenu (ou ajoutez) pour l'interface :
```
iface enp0s3 inet static
    address 10.42.xx.1
    gateway 10.42.0.1
```
- La première ligne annonce au système une configuration réseau statique.
- La deuxième ligne configure notre adresse IP comme étant `10.42.xx.1` (`.1` car il s'agit de la première machine dans notre plage réseau, `.0` étant réservé au réseau), avec comme masque /24.
- La troisème ligne configure l'adresse à utiliser comme routeur.

> Il faut remplacer `xx` par la valeur qui est attribuée dans le fichier Moodle (exemple : si on vous a attribué `10.42.131.1`, alors `xx` = `131`).

## 📄 Paramétrage du serveur du protocole DNS

Nous allons paramétrer le serveur du protocole DNS (Domain Name System). Pour cela nous devons modifier `/etc/resolv.conf` pour modifier le fichier suivant :

```bash
sudo nano /etc/resolv.conf
```

Ajouter :

```
nameserver 10.42.0.1
```

## ⚙️ Redémarrage de l'interface réseau
Une fois les deux fichiers configurés, on peut redémarrer l'interface réseau `enp0s3`:
```bash
sudo ifup enp0s3
```

Ensuite, nous pouvons vérifier l'adresse et la route :
```bash
ip addr show enp0s3
ip route show
```

Nous pouvons également tester la résolution DNS :
```bash
ping -c 3 google.com
```

Et enfin, nous pouvons redémarrer la machine pour vérifier la persistance :
```bash
sudo reboot
```

## 📔 Informations supplémentaires:
- [Documentation de `/etc/network/interfaces` et `/etc/resolv.conf`](https://www.debian.org/doc/manuals/debian-reference/ch05.fr.html)

---

- Page précédente: [Connexion au terminal de la VM](connexionterminal.md)
- Page suivante: [Mise à jour du système et installation des outils de base](miseajoursysteme.md)