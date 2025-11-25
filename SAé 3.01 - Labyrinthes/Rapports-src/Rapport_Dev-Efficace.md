Groupe J5 'IUTerrain' - Rapport sur le développement efficace
=================================

### Membres du groupe et implication :

- Ylann Wattrelos : 30%
- Gaël Dierynck : 30%
- Dawid Banas : 30%
- Mark Zavadskyi : 10%
--- 

## Structures de données utilisées : 

#### Labyrinthe aléatoire :


- Le plateau est représenté par une **matrice 2D** de boolean, c'est une représentation spatiale directe du labyrinthe. L'accès aux cases est donc en temps constant O(1) via leurs coordonnées.
**Utilisation :** Variable `plateau` dans `AbstractPlateau`

- Pour vérifier qu'un chemin existe entre l'entrée et la sortie, nous utilisons un `Set<Cases>` pour stocker les cases visitées lors de la génération du labyrinthe. Le set permet de ne pas avoir de doublons et offre des opérations d'insertion et de recherche en temps moyen O(1).
**Utilisation :** Variable `Set<Cases> chemin` dans `PlateauAleatoire`.

- Concernant le placement des murs, nous avons opté pour un algorithme de positionnement aléatoire.
Afin de privilégier la performance et la rapidité de génération, nous avons fait le choix de ne pas vérifier systématiquement si une case contient déjà un mur avant d'en placer un nouveau.
Cette approche nous a permis d'accélérer significativement le processus, même si cela implique de ne pas garantir l'obtention d'un nombre de murs rigoureusement exact. Le gain en performance justifie cette légère imprécision dans le décompte final. 

```JAVA
ALGORITHME GenererLabyrinthe(lignes, colonnes, pourcentage, entree, sortie)
    // 1. Initialisation du plateau vide avec bordures
    Plateau  (Tableau 2D de booléens (dimensions + bordures))
    PlacerBordures(Plateau) // Murs sur tout le périmètre
    
    // 2. Garantie de solvabilité : Génération d'un chemin entre entrée et sortie
    // Structure utilisée : Ensemble (Set) pour recherche rapide
    CheminGaranti = GenererCheminAleatoire(entree, sortie, lignes, colonnes)
    
    // 3. Application de la difficulté : Placement des murs aléatoires
    PlacerMursAleatoires(Plateau, pourcentage, CheminGaranti)
    
    RETOURNER Plateau
FIN ALGORITHME
```
```JAVA
Pour le chemin aléatoire : 
SOUS-ALGO GenererCheminAleatoire(depart, arrivee, lignes, colonnes)
    Chemin = Nouvel Ensemble<Case>()
    PositionActuelle = depart
    Ajouter PositionActuelle à Chemin
    Securite = limite d'itérations (anti-boucle infinie)

    TANT QUE (PositionActuelle != arrivee ET Securite > 0) FAIRE
        
        // Logique de déplacement (Approche de Drunkard Walk)
        Si (NombreAleatoire() < 0.6) ALORS
            // 60% de chance : Se rapprocher de la sortie
            Si (NombreAleatoire() est VRAI) ALORS
                Deplacer X vers arrivee.X
            SINON
                Deplacer Y vers arrivee.Y
        SINON
            // 40% de chance : Mouvement totalement aléatoire (Haut/Bas/Gauche/Droite)
            Choisir direction aléatoire
            Deplacer PositionActuelle
        
        // Correction si hors limites (rester à l'intérieur des bordures)
        Contraindre PositionActuelle aux bornes du plateau
        
        Ajouter PositionActuelle à Chemin
        Decrémenter Securite
    FIN TANT QUE
    
    RETOURNER Chemin
FIN SOUS-ALGO
```
```JAVA
SOUS-ALGO PlacerMursAleatoires(Plateau, pourcentage, CheminGaranti)
    Si pourcentage == 1.0 ALORS
        Remplir tout sauf CheminGaranti
    SINON
        CibleMur = (lignes * colonnes) * pourcentage
        MursPlaces = 0
        
        TANT QUE (MursPlaces < CibleMur) FAIRE
            // Tirage aléatoire de coordonnées
            x = Aleatoire(1, colonnes - 1)
            y = Aleatoire(1, lignes - 1)
            CaseTest = Case(x, y)
            
            // Condition critique : Ne pas bloquer le chemin garanti
            SI (CaseTest N'EST PAS dans CheminGaranti ET Plateau[y][x] N'EST PAS un mur) ALORS
                Plateau[y][x] = VRAI (Mur)
                MursPlaces = MursPlaces + 1
            FIN SI
        FIN TANT QUE
    FIN SI
FIN SOUS-ALGO
```



#### Labyrinthe parfait :

**Génération avec l'algorithme DFS :**

- Le plateau est également une matrice 2D de boolean.

- Cette fois-ci, nous utilisons une méthode pour creuser des passages entre les cases en fonction de l'algorithme DFS, garantissant qu'il y ait toujours un espace entre les murs. C'est donc un labyrinthe parfait.

**Pseudocode de l'algorithme DFS :**

```JAVA
ALGORITHME GenererLabyrintheParfait
ENTRÉES: lignes, colonnes, distanceMin
SORTIE: Un plateau de labyrinthe parfait avec Entrée et Sortie

DEBUT
    // 1. Initialisation (PlateauParfait.java)
    // Ajustement des dimensions pour être impaires (nécessaire pour l'algo des murs épais)
    SI lignes est pair ALORS lignes = lignes + 1
    SI colonnes est pair ALORS colonnes = colonnes + 1

    // Création du tableau rempli entièrement de MURS (True)
    plateau = Nouveau Tableau[lignes][colonnes] initialisé à VRAI

    // 2. Génération du labyrinthe (generateDFS)
    // On commence à creuser à partir d'une case impaire fixe, ex: (1, 1)
    CreuserDFS(1, 1)

    // 3. Placement Entrée et Sortie (AbstractPlateau.java)
    // L'entrée est placée aléatoirement sur une case vide
    PlacerEntree() 
    
    // La sortie est déterminée par un BFS partant de l'entrée
    PlacerSortie(distanceMin) 
FIN
```
Du côté de la fonction récursive de creusage DFS :
```JAVA
SOUS-ALGO CreuserDFS(x, y)
    plateau[y][x] = FAUX (Case libre)

    // Liste des 4 directions cardinales (Nord, Sud, Est, Ouest)
    Directions = [ (0,-1), (0,1), (1,0), (-1,0) ]
    Melanger(Directions) // Aléatoire important pour la structure du labyrinthe

    POUR CHAQUE (dx, dy) DANS Directions FAIRE
        // On regarde le voisin à 2 cases de distance (saut d'un mur potentiel)
        nx = x + (dx * 2)
        ny = y + (dy * 2)

        SI (nx, ny) est dans les limites ET plateau[ny][nx] EST UN MUR ALORS
            // On casse le mur intermédiaire
            plateau[y + dy][x + dx] = FAUX
            // On casse le mur de destination
            plateau[ny][nx] = FAUX
            
            // Appel Récursif (C'est ici que la Pile est utilisée implicitement)
            CreuserDFS(nx, ny)
        FIN SI
    FIN POUR
FIN SOUS-ALGO
```
Avantages de cette approche :

- Labyrinthe parfait garanti, chaque case est accessible car il relie tous les noeuds, comme un graph connexe sans cycles.

- Avec BFS pour placer la sortie, on s'assure que la distance minimale entre entrée et sortie est respectée.

Strucutures de données utilisées :
- **Matrice 2D** de boolean pour le plateau.
**Utilisation :** Variable `boolean[][] plateau` dans `AbstractPlateau`.

- Pile **implicite** via la récursion pour le DFS.

- **Liste** pour les directions cardinales, mélangée aléatoirement pour varier la structure du labyrinthe et éviter les motifs répétitifs.
**Utilisation :** Variables `int[] DIRS` dans `CreuserDFS`.

- **File** On utilise une file pour garentir une distance requise entre l'entrée et la sortie lors de leur placement avec BFS.
**Utilisation :** Variable `Queue<Cases>` dans `placerSortie`.

**Génération avec l'algorithme Binary Tree :**

- Le plateau reste une matrice 2D de boolean.

Avantages de cette approche :
- Pour chaque case (sauf coins superieurs (selon l'orientation)), l'algorithme ouvre exactement un passage vers un voisin. Une structure d'arbre binaire est ainsi créée, creusant jusqu'aux racines(coins). Le labyrinthe est donc connexe, toutes les cases sont accessibles car **un** passage vers une case voisine est toujours ouvert.

- La validation du chemin entre entrée et sortie est toujours assurée par le placement de ceux-ci dans `AbstractPlateau` via un BFS.

**Pseudocode de l'algorithme Binary Tree :**

```JAVA
ALGORITHME GenererLabyrintheBinaryTree
ENTRÉES: lignes, colonnes, distanceMin, Orientation (dx1, dy1, dx2, dy2)
SORTIE: Un plateau de labyrinthe parfait avec biais directionnel
DEBUT
    // 1. Initialisation (PlateauBinaryTree.java)
    // Ajustement pour dimensions impaires
    SI lignes est pair ALORS lignes = lignes + 1
    SI colonnes est pair ALORS colonnes = colonnes + 1

    // Remplissage initial : Tout est un MUR
    plateau = Nouveau Tableau[lignes][colonnes] initialisé à VRAI

    // 2. Génération itérative (generateBinaryTree)
    // On parcourt les cases "cellules" (indices impairs) une par une
    POUR y DE 1 À lignes - 1 PAR PAS DE 2 FAIRE
        POUR x DE 1 À colonnes - 1 PAR PAS DE 2 FAIRE
            
            // On creuse la case actuelle
            plateau[y][x] = FAUX

            // On vérifie si on peut creuser dans les deux directions imposées
            // (Exemple : Nord et Est)
            PeutCreuserDir1 = EstDansLesLimites(x + dx1, y + dy1)
            PeutCreuserDir2 = EstDansLesLimites(x + dx2, y + dy2)

            // Logique de décision binaire
            SI (PeutCreuserDir1 ET PeutCreuserDir2) ALORS
                // Si les deux sont possibles, on choisit au hasard (Pile ou Face)
                SI (RandomBooleen()) ALORS
                    OuvrirMur(x, y, dx1, dy1)
                SINON
                    OuvrirMur(x, y, dx2, dy2)
                FIN SI
            SINON SI (PeutCreuserDir1) ALORS
                // Si on est sur un bord et qu'on ne peut aller que direction 1
                OuvrirMur(x, y, dx1, dy1)
            SINON SI (PeutCreuserDir2) ALORS
                // Si on est sur un bord et qu'on ne peut aller que direction 2
                OuvrirMur(x, y, dx2, dy2)
            FIN SI
            // Si aucune n'est possible (le coin opposé), on ne fait rien.
            
        FIN POUR
    FIN POUR

    // 3. Placement Entrée et Sortie (AbstractPlateau.java)
    PlacerEntree() 
    PlacerSortie(distanceMin) // Utilise BFS pour garantir l'accès
FIN ALGORITHME
```
Du côté de la fonction d'ouverture des murs :
```JAVA
SOUS-ALGO OuvrirMur(x, y, dx, dy)
    // Creuse le mur intermédiaire et la case cible
    plateau[y + dy][x + dx] = FAUX
FIN SOUS-ALGO
```
Structures de données utilisées :

- **Matrice 2D** de boolean pour le plateau.
**Utilisation :** Variable `boolean[][] plateau` dans `AbstractPlateau`.

Enumération pour l'orientation :
- **Enumération** Permet de stocker les déplacements pour paramétrer le labyrinthe sans dupliquer le code.
**Utilisation :** variables `dx1, dy1..` dans `PlateauBinaryTree`.

- **File** comme décrit précédemment pour le placement de la sortie.
**Utilisation :** Variable `Queue<Cases>` dans `placerSortie`.

- **Liste** pour stocker les cases visitées lors du BFS, afin d'éviter de traiter plusieurs fois la même case.
**Utilisation :** Variable `List<Cases> visitees` dans `placerSortie`.

## Efficacité, tableaux comparitfs : 
### Mesure du temps

#### Génération aléatoire

| Lignes | Colonnes | Temps moyen (s) à 20% | Temps moyen (s) à 30% | Temps moyen (s) à 50% |
|-------|---------|-----------------------|-----------------------|-----------------------|
| 20    | 40      | 0.006571958           | 0.002396959           | 0.002580042           |
| 40    | 80      | 0.008224583           | 0.0025285             | 0.002405125           |
| 80    | 160     | 0.01167475            | 0.006982542           | 0.027037084           |
| 160   | 320     | 0.033345084           | 0.027981791           | 0.032209417           |
| 320   | 640     | 0.110944125           | 0.107051709           | 0.107628333           |

#### Génération avec un parcours en largeur

| Lignes | Colonnes | distance | Temps moyen (s) |
|--------|----------|----------|-----------------|
| 20     | 40       | 20       | 0.006888209     |
| 40     | 80       | 20       | 0.005618917     |
| 80     | 160      | 20       | 0.042204125     |
| 160    | 320      | 20       | 0.5984065       |
| 320    | 640      | 20       | 19.088747209    |

#### Génération avec un arbre binaire

| Lignes | Colonnes | distance | Temps moyen (s) |
|--------|----------|----------|-----------------|
| 20     | 40       | 20       | 0.005823917     |
| 40     | 80       | 20       | 0.004632916     |
| 80     | 160      | 20       | 0.044230792     |
| 160    | 320      | 20       | 0.608928666     |
| 320    | 640      | 20       | 14.718825458    |

Bilan : une génération par arbre binaire est plus efficace qu'une génération par DFS pour des grandes tailles de labyrinthe, car elle évite la surcharge de la récursion et gère mieux la mémoire.

Avec la puissance de nos machines, les temps ne sont pas distinguables pour des petites tailles de labyrinthe. On reconnait cependant une plus grande efficacité pour l'algorithme binaire dès que la taille augmente.

## Complément et bilan :

Les efforts donnés pendant le développement du projet prennent forme dans le choix des algorithmes de génération de labyrinthe : **Dawid Banas** a recherché et s'est documenté longuement sur plusieurs algorithmes, leurs avantages et inconvénients, avant de proposer deux méthodes complémentaires **(DFS et Binary Tree)** qui répondent à nos besoins de labyrinthe parfaits avec des caractéristiques différentes. (Algorithmes retrouvables dans les classes `PlateauParfait` et `PlateauBinaryTree`).

L'utilisation des algorithmes est calquée pour maximiser l'efficacité du code tout en garantissant la qualité du labyrinthe généré. Par exemple, **l'algorithme DFS** utilise la récursion pour une implémentation claire et concise, tandis que **l'algorithme Binary Tree** est itératif, ce qui peut être plus efficace en termes de mémoire pour de très grands labyrinthes.

Cependant, les limites de ces algorithmes se révèlent dans la compléxité d'implémentation et de compréhension. 

Côté améliorations futures, il serait intéressant d'explorer des algorithmes plus avancés comme **A\*** pour la génération de labyrinthes avec des contraintes spécifiques, ou encore des algorithmes basés sur des graphes pour créer des labyrinthes avec des caractéristiques particulières (comme des zones thématiques ou des défis spécifiques).
