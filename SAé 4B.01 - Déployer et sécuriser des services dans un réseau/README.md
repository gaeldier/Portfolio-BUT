#
![](ressources/Dolibarr.png)

# SAE 4B.01 : Déployer et sécuriser des services dans un réseau

## Membres de l'équipe
- [Gael DIERYNCK](mailto:gael.dierynck.etu@univ-lille.fr)
- [Amaury VANHOUTTE](mailto:amaury.vanhoutte.etu@univ-lille.fr)

#### Scénario
On trouve assez facilement des offres d’hébergement de la solution Dolibarr permettant aux entreprises de se décharger des questions d’installation, de paramétrage et de maintenance (par exemple ici et ici).

Globalement, sur ces offres on retrouve :

    - la possibilité d’avoir une installation dans une version spécifique
    - une isolation des différents clients (chacun son installation)
    - un accès HTTPS
    - une sauvegarde des données
    - la sécurité

Dans le cadre de cette SAÉ, on vous propose de vous mettre en position **d’hébergeur**. **Vous devrez donc mettre en place une infrastructure permettant de réaliser l’hébergement de différents serveurs Dolibarr pour des clients.** Dans la mesure où vous pourrez être amenés à réaliser plusieurs installations en fonction des besoins des clients, il devient primordial d’automatiser le processus de déploiment. **L’élément central de cette SAÉ sera donc la production de scripts shell permettant ce déploiment automatique.**

## Procédures à suivre :

Installation des scripts, informations complémentaires et **démarrage** de l'installation de l’infrastructure : 

- [Procédure d'Initialisation Globale](procedures/InitProcedure.md)  
- [Procédure 0 - Initialisation et Configuration des VMs](procedures/Procedure0.md)  
- [Procédure 1 - Configuration de l'accès SSH](procedures/Procedure1.md)  
- [Procédure 2 - Installation de l'Infrastructure](procedures/Procedure2.md)  
- [Procédure 3 - Configuration des Sauvegardes](procedures/Procedure3.md)  
- [Procédure 4 - Déploiement de Dolibarr](procedures/Procedure4.md)  
