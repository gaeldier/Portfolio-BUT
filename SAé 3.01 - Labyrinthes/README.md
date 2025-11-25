# IUT'ERRAIN 🎮

Un jeu de labyrinthe développé en Java avec JavaFX.

## Description

IUT'ERRAIN est un jeu de labyrinthe où le joueur doit trouver son chemin de l'entrée jusqu'à la sortie. Le jeu propose deux modes:

- **Mode Libre**: Générez des labyrinthes personnalisés avec les paramètres de votre choix
- **Mode Progression**: Suivez une série de niveaux avec une difficulté croissante

## Fonctionnalités

- 🎯 Plusieurs modes de jeu
- 🎮 Contrôles intuitifs
- 🌫️ Option de vue restreinte pour plus de défi
- 🎵 Effets sonores
- 💾 Sauvegarde de la progression
- 🏆 Système de score

## Technologies utilisées

- Java 17
- JavaFX
- JUnit 5 pour les tests
- Maven pour la gestion des dépendances

## Installation

1. Clonez le dépôt:

```bash
git clone https://gitlab.univ-lille.fr/sae302/2025/J5_SAE3.3.git
```

2. Installez les dépendances avec Maven:

```bash
mvn install
```

3. Lancez l'application:

```bash
mvn javafx:run
```

## Comment jouer

- Utilisez les touches ZQSD/WASD ou les flèches directionnelles pour vous déplacer
- Atteignez la sortie pour terminer le niveau
- En mode libre, personnalisez:
  - La taille du labyrinthe
  - La densité des murs
  - L'activation de la vue restreinte

## Tests

Le projet inclut une suite de tests unitaires. Pour les exécuter:

```bash
mvn test
```

## Structure du projet

```
src/
├── main/
│   ├── java/          # Code source
│   └── resources/     # Ressources (images, sons, etc.)
└── test/
    └── java/          # Tests unitaires
```

## Auteurs

- Gaël Dierynck
- Dawid Banas
- Ylann Wattrelos
- Mark Zavadskyi

**Nombre de commits :** 304

## Environnement requis :

- Java 17 ou supérieur
- Maven 3.6 ou supérieur


## Licence

Ce projet est sous licence MIT.

[suivi.md](./Rapports-src/suivi.md)  
[rapport dev efficace](./Rapports-src/Rapport_Dev-Efficace.md)  
[rappord qualité de dév](./Rapports-src/Rapport_Dev-Qualité.md)
