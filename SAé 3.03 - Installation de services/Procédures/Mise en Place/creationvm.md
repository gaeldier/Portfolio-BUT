# Création et gestion d’une machine virtuelle

> **Important :** toutes ces commandes doivent être exécutées **sur** `dattier.iutinfo.fr` (la machine de virtualisation). Connectez-vous à celle-ci (utilisez `ssh dattier.iutinfo.fr` ou l'alias `ssh virt` si vous l'avez configuré).

## 📋 Utilisation de `vmiut`

Le service fournit un utilitaire `vmiut` pour créer/contrôler les VM. Tapez `vmiut` sans argument pour voir l'aide.

| **ACTION** | **COMMANDE**             | **NOTE**                                 |
|------------|--------------------------|------------------------------------------|
| Créer      | `vmiut creer <nom>`      | Clone le modèle Debian par défaut.       |
| Lister     | `vmiut lister`           | Affiche l'ID unique de vos VM.           |
| Démarrer   | `vmiut demarrer <nom>`   | Allume la machine virtuelle.             |
| Infos      | `vmiut info`             | Important : Notez l'adresse IP affichée. |
| Arrêter    | `vmiut arreter <nom>`    | Éteint proprement la VM.                 |
| Supprimer  | `vmiut supprimer <nom>`  | Action irréversible.                     |

> **Conseil** : arrêtez vos VM à la fin de chaque séance pour libérer les ressources.

# Création de la machine virtuelle
Maintenant que nous nous sommes familiarisés avec l'outil `vmiut`, pour ce projet nous utiliserons une machine virtuelle avec ces caractéristiques:
- **Distribution :** Debian GNU
- **Utilisateur standard :** `username:user`, `password:user`
- **Administrateur :** `username:root`, `password:root`

Nous exécuterons donc la commande suivante:
```bash
vmiut creer matrix
```

---

- Page précédente: [Génération et gestion des clés SSH](générationgestionssh.md)
- Page suivante: [Connexion au terminal de la VM](connexionterminal.md)