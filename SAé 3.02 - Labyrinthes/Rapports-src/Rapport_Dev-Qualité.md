# Rapport de Développement - Qualité du Code
**Groupe J5 'IUTerrain'**

## Membres du groupe
* Gaël Dierynck
* Ylann Wattrelos
* Dawid Banas
* Mark Zavadskyi

---

## Introduction
Ce document analyse la qualité du code source du projet de labyrinthe en Java. Il met en lumière les choix architecturaux, la structure des classes, et la manière dont elles interagissent pour offrir une expérience de jeu fluide et maintenable.

**Légende du rapport :**
* **Pourquoi** : Justification de l'existence de la classe.
* **Rôle** : Responsabilités techniques et fonctionnelles.

---

### 1. Le Cœur du Jeu (Logique Centrale)
Ces classes sont le moteur de l'application. Elles gèrent l'état d'une partie active.

#### `Labyrinthe`
* **Pourquoi :** C'est la classe maîtresse du modèle. Elle centralise tout ce qui concerne une partie en cours : le plateau, la position du joueur, l'entrée et la sortie.
* **Rôle :**
    * Elle sert de **façade** : le contrôleur parle principalement à elle.
    * Elle implémente le pattern **Observer** (via `observers`, `addObserver`, `notifyObservers`) pour prévenir la vue (`PlateauObserveur`) quand le joueur bouge.
    * Elle gère les règles : `deplacementJoueur`, `aGagner`, `estMur`.

#### `AbstractPlateau`
* **Pourquoi :** Pour définir le contrat commun à tous les types de terrains, quelle que soit leur forme de génération.
* **Rôle :** Elle contient la grille de données brutes (`plateau : boolean[][]`) et fournit les outils géométriques de base : vérifier si une case est un mur (`estMur`), ou calculer des distances (`getNbCasesMinimale`).

#### `Cases`
* **Pourquoi :** Pour manipuler les coordonnées (X, Y) sous forme d'objet unique plutôt que de passer deux entiers séparés partout.
* **Rôle :** Facilite le passage de paramètres. Elle redéfinit `equals` et `hashCode`, ce qui permet d'utiliser des `Cases` comme clés dans des `Map` ou des `Set` (utilisé par exemple dans `PlateauAleatoire` pour le chemin).

#### `Direction` (Enum)
* **Pourquoi :** Pour sécuriser les déplacements et éviter les erreurs liées aux chaînes de caractères ("haut", "bas").
* **Rôle :** Liste les 4 constantes (`NORD`, `SUD`, `EST`, `OUEST`) utilisées par la méthode `deplacementJoueur`.

---

### 2. Les Algorithmes de Génération (L'Architecture)
Ce projet utilise l'héritage pour proposer des labyrinthes variés.

#### `PlateauParfait`
* **Pourquoi :** Pour générer un labyrinthe "classique" où il existe un chemin unique entre deux points (sans boucles).
* **Rôle :** Utilise l'algorithme **DFS** (Depth First Search) via la méthode `generateDFS` pour creuser des chemins de manière exhaustive.

#### `PlateauBinaryTree`
* **Pourquoi :** Pour créer un labyrinthe avec une texture très spécifique (souvent des longs couloirs en diagonale), généré très rapidement.
* **Rôle :** Utilise l'algorithme de l'Arbre Binaire (`generateBinaryTree`). Il a besoin d'une `Orientation` pour savoir vers quelle diagonale "biaiser" le labyrinthe.

#### `PlateauAleatoire`
* **Pourquoi :** Pour créer des niveaux plus chaotiques ou non structurés.
* **Rôle :** Il peut placer des murs au hasard selon un pourcentage, ou générer un chemin aléatoire (`genererCheminAleatoire`) pour s'assurer qu'il existe au moins une solution avant de remplir le reste.

#### `Orientation` (Enum)
* **Pourquoi :** C'est une dépendance spécifique pour le `PlateauBinaryTree`.
* **Rôle :** Définit les directions de creusement (ex: `NORD_EST`, `SUD_OUEST`) pour l'algorithme binaire.

#### `ModeProgressionLoader`
* **Pourquoi :** Pour ne pas polluer le code du `ModeProgression` avec des listes de chiffres.
* **Rôle :** Stocke "en dur" ou charge la configuration de chaque niveau (taille, difficulté, si c'est un labyrinthe parfait ou non) via des listes (`niveauxDifficile`, `niveauxNbLigne`, etc.).

---

### 3. Les Modes de Jeu
Ces classes configurent le `Labyrinthe` selon le choix de l'utilisateur.

#### `ModeLibre`
* **Pourquoi :** Permet de lancer une partie "Custom" (personnalisée).
* **Rôle :** C'est une sous-classe de `Labyrinthe` qui prend tous les paramètres bruts dans son constructeur (taille, pourcentage de murs, type d'algo) pour créer un plateau sur mesure.

#### `ModeProgression`
* **Pourquoi :** C'est le mode "Campagne" ou "Histoire".
* **Rôle :** Au lieu de demander la taille, on lui donne juste un numéro de niveau (`niveau : int`). Il se configure tout seul en allant chercher les infos dans le `ModeProgressionLoader`.

---

### 4. Données Utilisateur et Sauvegarde
Ces classes gèrent la persistance des données (ce qui reste quand on éteint le jeu).

#### `Joueur`
* **Pourquoi :** Représente un utilisateur réel.
* **Rôle :** Stocke le nom, le score global, et la liste des objets `Niveau` (son historique). C'est l'objet principal qui est sérialisé.

#### `Niveau`
* **Pourquoi :** Pour suivre la progression précise du joueur sur un niveau donné.
* **Rôle :** Sait si le niveau est débloqué (`peutEtreFais`), s'il est terminé (`estFait`), et quel score a été obtenu pour chaque difficulté (`scoreDifficulte`).

#### `ListeJoueursSerializable`
* **Pourquoi :** C'est la "Base de données" du jeu.
* **Rôle :** Encapsule un `Set<Joueur>`. C'est cet objet unique qui est envoyé dans le fichier de sauvegarde, permettant de sauver tous les joueurs d'un coup.

#### `ToolBox`
* **Pourquoi :** Classe utilitaire technique.
* **Rôle :** Contient les méthodes statiques `serialize` et `DeSerialize`. Elle sépare la logique métier (le jeu) de la logique technique (écrire sur le disque dur).

---

### 5. Utilitaires de Calcul

#### `Noeuds`
* **Pourquoi :** Classe helper pour les algorithmes de recherche de chemin (Pathfinding).
* **Rôle :** Elle couple une `Case` avec une `distance` (int). Elle est utilisée par `AbstractPlateau` (probablement dans `getNbCasesMinimale`) pour calculer le chemin le plus court (algorithme BFS) sans modifier la classe `Cases` elle-même.

## UML

Vous pouvez retrouver le diagramme UML dans ce fichier : [diagramme UML](../uml-output/Labyrinthes.urm.puml) et l'ouvrir avec l'extension PlantUML de VSCode, IntelliJ ou via le site [PlantUML Online](https://www.plantuml.com/plantuml/uml/).