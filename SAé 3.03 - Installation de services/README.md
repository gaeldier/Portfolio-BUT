<p align="center">
  <img src="Ressources/matrixlogo.png">
</p>

# SAÉ 3.03 – Déploiement d'une application

Ce dépôt contient l’ensemble des **procédures détaillées** réalisées dans le cadre de la **SAÉ – Déploiement d’une application**.  
L’objectif du projet est de déployer une solution de communication basée sur le standard **Matrix**, en utilisant :
- **Synapse** comme serveur Matrix
- **Element Web** comme client
- Une **machine virtuelle Debian** hébergée sur l’infrastructure de virtualisation de l’IUT
- Une base de données **PostgreSQL**

*Toutes les étapes sont documentées afin de garantir la **reproductibilité** du déploiement.*

## 1. 🖥️ Mise en Place de l’Environnement
### [Procédures](Procédures/Mise%20en%20Place/README.md)
- [1. Connexion SSH au serveur de virtualisation](Procédures/Mise%20en%20Place/connectionssh.md)
- [2. Génération et gestion des clés SSH](Procédures/Mise%20en%20Place/générationgestionssh.md)
- [3. Création et gestion d’une machine virtuelle](Procédures/Mise%20en%20Place/creationvm.md)
- [4. Configuration réseau](Procédures/Mise%20en%20Place/configurationreseau.md)
- [5. Connexion au terminal de la VM](Procédures/Mise%20en%20Place/connexionterminal.md)
- [6. Mise à jour du système et installation des outils de base](Procédures/Mise%20en%20Place/miseajoursysteme.md)
- [7. Changement du nom d'hôte de la VM](Procédures/Mise%20en%20Place/changementnomhote.md)

## 2. 🗃️ Installation du Serveur de Données
### [Procédures](Procédures/Service%20Postgresql/README.md)
- [1. Installation de sudo](Procédures/Service%20Postgresql/installationsudo.md)
- [2. Installation de PostgreSQL](Procédures/Service%20Postgresql/postgresql.md)

## 3. 🌐 Installation et configuration du Serveur Synapse
### [Procédures](Procédures/Installation%20Synapse/README.md)
- [1. Mise en place d’un service HTTP de test (nginx)](Procédures/Installation%20Synapse/01-service-test-nginx.md)
- [2. Accès au service depuis la machine physique](Procédures/Installation%20Synapse/02-acces-distant.md)
- [3. Installation de Synapse](Procédures/Installation%20Synapse/03-installation-synapse.md)
- [4. Utilisation d’une base PostgreSQL](Procédures/Installation%20Synapse/04-psql.md)
- [5. Création des utilisateurs Matrix](Procédures/Installation%20Synapse/05-utilisateurs.md)

## 4. 💻 Configuration du client Element et Reverse Proxy
### [Procédures](Procédures/Element%20et%20Reverse%20Proxy/README.md)
- [1. Installation et configuration de Element Web](Procédures/Element%20et%20Reverse%20Proxy/01-installation-element.md)
- [2. Installation d'un reverse proxy](Procédures/Element%20et%20Reverse%20Proxy/02-reverse-proxy.md)

## 5. 🏛️ Migration vers l'Architecture Finale
### [Procédures](Procédures/Architecture%20Finale/README.md)

## Auteurs
Réalisé par:
- Dawid BANAS
- Gaël DIERYNCK
- Mark ZAVADSKYI