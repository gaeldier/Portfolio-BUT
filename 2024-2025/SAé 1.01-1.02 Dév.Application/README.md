# S1.01-02 | iJava Fighter
___
### Jeu ludo-éducatif iJava Fighter 

**Lancez** le jeu avec la commande suivante :
```shell
./run.sh
```

Si jamais vous avez apporté des modifications au code, **ou n'avez jamais lancé le jeu** exécutez la commande suivante pour compiler le projet :
```shell
./compile.sh
```
___
## But du jeu : 
Le jeu est un fighter tour par tour où un joueur se bat en résolvant des calculs mathématiques plus ou moins simples contre la machine
pour tenter de rester en vie le plus longtemps possible !

Le jeu débute avec un nombre de choix qui influenceront la difficulté du jeu.
Le joueur peut choisir entre 2 classes de personnages, chacune ayant des caractéristiques différentes.
 - #### Chevalier : 
    - Points de vie : 150
    - Attaque : normale
- #### Ninja : 
    - Points de vie : 100
    - Attaque : plus puissante

- #### Le mode hardcore
    - L'ennemi fera des dégâts bien plus conséquents et ses points de vie se verront augmenter aussi.

Le joueur peut choisir entre 4 niveaux de difficulté, chaque niveau rajoutera un opérateur de calcul possible (ex: niveau 1 : +, niveau 2 : +, -, niveau 3 : +, -, *, niveau 4 : +, -, *, /).

Étant infini, le but du jeu est d'avoir le plus gros score en battant le plus d'ennemis possibles avant de ne plus avoir de points de vie.
Les scores sont stockés dans un fichier `scores.txt` à la racine du projet, ou dans le menu principal du jeu. 


#### De Amaury Vanhoutte et Gaël Dierynck pour l'IUT de Lille, @2025
___
___
## Configuration requise :

- Java 8 ou supérieur
- Un ordinateur avec un système d'exploitation Linux, Windows ou Mac OS ( Linux recommandé )
- Un écran de résolution 1920x1080, une autre résolution affectera la qualité et la pertinence de l'affichage
- Un clavier 
___
## Environnement de tests :

Testé sur un Asus Zenbook 14 :
- Processeur : AMD Ryzen 5 5600H
- RAM : 16 Go
- Système d'exploitation : Debian 12 (Bookworm)
- Résolution : 1920x1080
- Java : 17.0.13

Testé sur un Honor Magicbook 14 : 
- Processeur : Intel Core i5-12500H
- RAM : 16 Go
- Système d'exploitation : Debian 12 (Bookworm)
- Résolution : 2160x1440
- Java : 17.0.13
___