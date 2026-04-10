# Procédure 1 - Configuration de l'accès SSH

Cette procédure documente le fonctionnement du script `scripts/1_setup_ssh.sh`. Ce script permet d'automatiser l'ajout de votre clé SSH publique sur l'ensemble des VMs créées.

## 1. Génération de la clé locale
Le script vérifie si une clé SSH (`id_rsa`) existe déjà sur votre machine locale. Si aucune clé n'est trouvée, il en génère une automatiquement sans mot de passe.

```bash
if [ ! -f ~/.ssh/id_rsa.pub ]; then
    ssh-keygen -t rsa -b 4096 -f ~/.ssh/id_rsa -N ""
fi
```

## 2. Détection des IPs
Le script utilise les variables d'environnement chargées depuis `config.conf` pour identifier les adresses IP de chaque VM (`proxy`, `dolibarr`, `database`, `backup`).

## 3. Déploiement de la clé
Pour chaque machine, le script effectue les actions suivantes :
1. **Test de connexion** : Il vérifie si une connexion sans mot de passe est déjà opérationnelle vers l'IP cible.
2. **Copie de la clé** : Si la connexion échoue, il utilise `ssh-copy-id` pour envoyer votre clé publique vers la VM. 

> [!NOTE]
> Lors de cette étape, le mot de passe de l'utilisateur distant (`user` par défaut) vous sera demandé une seule fois par machine si la clé n'est pas déjà présente.

## 4. Finalisation
Une fois le script terminé, vous pourrez vous connecter à n'importe quelle VM via la commande `ssh user@IP` sans avoir à saisir de mot de passe.

---

Etape suivante  --> [Procédure 2 - Installation de l'Infrastructure](Procedure2.md) <--
  
Etape précédente  --> [Procédure 0 - Initialisation et Configuration des VMs](Procedure0.md) <--