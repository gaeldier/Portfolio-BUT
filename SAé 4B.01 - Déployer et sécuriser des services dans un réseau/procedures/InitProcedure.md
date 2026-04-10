# Procédure d'Initialisation Globale

Cette procédure documente le fonctionnement du script principal `install_all.sh` qui démarre l'installation complète de l'infrastructure.

## 1. Vue d'ensemble
Le script `install_all.sh` automatise l'enchaînement des différentes étapes de déploiement, de la création des machines virtuelles jusqu'à la configuration des sauvegardes.

## 2. Étapes du déploiement

### Création des VMs
Le script lance `scripts/0_create_vms.sh` pour s'assurer que les quatre machines virtuelles nécessaires sont créées, démarrées et configurées avec leurs IPs statiques.

### Configuration Réseau
Le script charge les variables d'environnement depuis `config.conf` et définit les adresses IP statiques pour chaque service :
- **Proxy** : `10.42.147.50`
- **Dolibarr** : `10.42.147.51`
- **Database** : `10.42.147.52`
- **Backup** : `10.42.147.53`

### Configuration SSH
Appel du script `scripts/1_setup_ssh.sh` pour configurer les accès sécurisés par clés SSH entre la machine hôte et les VMs, permettant ainsi l'automatisation sans mot de passe.

### Installation de l'Infrastructure
Exécution de `scripts/2_install_infrastructure.sh` pour installer les composants logiciels :
- **PostgreSQL** sur la VM Database.
- **Docker** sur les VMs Dolibarr et Proxy.
- **Traefik** sur la VM Proxy pour le routage des flux.

### Configuration des Sauvegardes
Lancement de `scripts/3_setup_backup.sh` pour mettre en place les scripts de backup automatique et les tâches planifiées (cron) sur la VM Backup.

## 3. Utilisation Post-Installation
Une fois l'installation terminée, l'infrastructure est prête à accueillir des clients. Le déploiement d'un nouveau client se fait via la commande suivante :

```bash
./scripts/4_deploy_new_client.sh <nom_du_client>
```

Etape suivante --> [Procédure 0 - Initialisation et Configuration des VMs](Procedure0.md) <--