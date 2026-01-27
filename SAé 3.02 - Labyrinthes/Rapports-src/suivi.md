# Suivi du Travail effectué

**Certaines taches jugées plus conséquentes ont le nom de leurs auteurs, s'il n'est pas présent c'est que tout le monde y a contribué.**

## Semaine 40

Réalisation des fiches descriptives des CU pour le rendu analyse :
- Gaël Dierynck, Mark Zavadskyi, Ylann Wattrelos et Dawid Banas ont réalisé `Lancer mode progression`, `afficher la progression`, `charger un profil`, `déplacer le joueur`.

Nous avons aussi réalisé ensemble une première version de diagramme de classes.

## Semaine 41

Grâce à notre professeure Iovka Boneva, nous avons pu revoir notre UML et diagramme CU pour améliorer et optimiser notre projet.
La maquette du projet a ainsi été commencée par Ylann Wattrelos et Gaël Dierynck sur Figma.


## Semaine 42

Mise à jour de l'UML et des CU en fonction des modifications apportées au projet, notamment le refactoring des diverses classes.
Les rendus de la matière **Analyses** ont été correctement déposés et relus par notre professeur.
Début de l'implémentation des classes de base du projet: `Labyrinthe`, `Joueur`, `ModeJeu`, `ModeLibre`, `ModeProgression`, `Cases` par **Gaël Dierynck** et **Dawid Banas**.
Ylann Wattrelos a commencé l'implémentation de la vue avec JavaFX, notamment la classe `AccueilControleur` et la scène d'accueil.

## Semaine 43

Poursuite de l'implémentation des classes de base du projet par Gaël Dierynck, Dawid Banas et Ylann Wattrelos : `ToolBox`, `ModeProgressionLoader`, `Joueur`, `Labyrinthe`, `ModeJeu`, `ModeLibre`, `ModeProgression`. Des exceptions personnalisées ont été créées pour gérer les erreurs spécifiques au labyrinthe : `WrongDeplacementException`. 
Ylann Wattrelos a continué l'implémentation de la vue avec JavaFX, notamment la classe `JeuLibreControleur` et la scène de jeu en mode libre.
Côté vue, `PlateauObservateur`, `NiveauControleur` ont été implémentés.

Côté modèle, la ToolBox a été enrichie avec des méthodes de sérialisation et désérialisation pour sauvegarder et charger les profils des joueurs ainsi que d'une méthode de lecture de CSV pour gérer les données liées aux labyrinthes et difficultés.
Le **mode progression** a été implémenté avec la gestion des niveaux et difficultés - Par Gaël Dierynck et Ylann Wattrelos.

Le rendu pour la matière **Développement** sera effectué ce vendredi 24 octobre, avant minuit.
Recréation de l'UML pour refléter les changements apportés au projet.

## Semaine 44 : Pause pédagogique

## Semaine 45 

Poursuite des modifications du projet avec l'ajout de nouvelles textures plus lisibles pour le joueur.
Ajout d'une classe Abstraite `AbstractPlateau` par Dawid Banas pour factoriser le code commun entre les différentes implémentations de plateau. Toutes les types de plateaux fonctionnent désormais avec cette classe abstraite.
Refactorisation du code pour améliorer la lisibilité (Renommage des classes de génération de labyrinthe `PlateauParfait` et `PlateauAleatoire`) et la maintenabilité.

## Semaine 46

Ylann a mis à jour le jeu pour accueillir un nouveau niveau et augmenter la difficulté directement donnée dans le mode progression.

## Semaine 47 : Finalisation du projet

Dawid Banas a implementé la génération de labyrinthe avec l'algorithme "Binary Tree" dans la classe `PlateauBinaryTree`, qui hérite de `AbstractPlateau`. Cette nouvelle méthode de génération offre une alternative intéressante aux méthodes précédentes, en créant des labyrinthes avec une structure particulière (Génération par rapport à une **orientation**).

Ylann Wattrelos a finalisé la vue, permettant de régler la portée de celle-ci et d'optimiser l'affichage pour réduire les latences lors du déplacement du joueur.
Un système de compteur de pas a été ajouté pour suivre les déplacements du joueur dans le labyrinthe, afin de calculer un **score**.

Gaël Dierynck a refactorisé le code des labyrinthes pour qu'ils puissent être générés avec la vue (ABR) et implementé la génération aléatoire des sorties et entrées des labyrinthes en mode parfaits, pour suivre avec le paramètre `distanceMax` entre ceux ci.

Dawid Banas a complété les tests pour chaque nouvelle fonctionnalité ajoutée, assurant la stabilité et la fiabilité du code.

Divers corrections de bugs et améliorations mineures ont été apportées par tous les membres de l'équipe pour finaliser le projet avant la date de rendu finale.

