
---
title: SAE S2.02 -- Rapport graphes
subtitle: Équipe A1
author: Romain Harlaut, Amaury Vanhoutte, Gaël Dierynck, 
date: 2025
---

# Version 1

## Choix pour la modélisation

### Forte affinité
![alt text](image.png)
&nbsp;

Cette paire, nommée H1 et V1 possèdent une forte affinité en raison de leur critères de **genre, d'hobbies et de différence d'âge**.


### Faible affinité
![alt text](image-1.png)
&nbsp;

Secondement, la paire H2 V2 démontre faible affinité maximale, où les critères de **genre, d'hobbies et de différence d'âge** ne sont pas du tout remplis. 

### Arbitrage entre les critères d'affinité

Pour finir les trois possibilités, voici des paires illustrant un brassage entre les critères d'affinités. Les paires ne sont ni fortes, ni faibles en affinité.

##### La paire H3,V3 | Une affinité basée sur un hobbie et une affinité de genre  

![H3, V3](image-2.png)

##### La paire H4,V4 | Une affinité basée sur un hobbie et une affinité d'âge 

![H4, V4](image-3.png)

##### La paire H5,V5 | Une affinité basée sur un hobbie, une affinité de genre d'un côté et l'âge 

![H5, V5](image-4.png)

&nbsp;


## Exemple complet

![alt text](image-6.png)
&nbsp;

Voici quatre hôtes **A, B, C, D** ainsi que quatre visiteurs **W, X, Y, Z**.

L'appariement le plus optimisé est illustré ci-dessous.

![Paires les plus optimisées](image-7.png)

&nbsp;

**Les paires sont liées selon les meilleurs critères.**



## Score d'affinité

_**Ci-dessous, le pseudo code de notre fonction de calcul d'affinité basée sur les critères demandés pour ce rapport**_

_`score_affinité_1(hôte, visiteur)`_

```JAVA
double score_affinité_1(hote, visiteur):

double score = 5 // valeur initiale du score

//Contrainte d'âge
si  valeur absolue (hôte.birth_date - visiteur.birth_date) <= 18 mois
score = score - 1

//Contrainte d'hobbies
N = nombre_hobbies_en_commun(hôte, visiteur)
si N = 0
score = score
sinon
si N = 1
score = score - 1
sinon
score = score - 2 // On tombe dans le cas où ils possèdent plus de 2 hobbies en commun

//Contrainte de genres 

// Si le genre de l'hôte ne correspond pas à la préférence du visiteur 
// et si le genre du visiteur ne correspond pas à l'hôte
si hôte.gender != visiteur.pair_gender ET visiteur.gender !=hôte.pair_gender
score = 0
sinon

// Si le genre de l'hôte correspond à la préférence du visiteur 
// et si le genre du visiteur correspond à l'hôte
si hôte.gender == visiteur.pair_gender ET visiteur.gender == hôte.pair_gender
score = score - 2
sinon

// Si le genre de préférence de l'hôte et du visiteur ne sont pas spécifiés 
si hôte.pair_gender == other ET visiteur.pair_gender == other
score = score - 2
sinon

// Si le genre de préférence de l'hôte n'est pas spécifié 
// et que le genre de préférence du visiteur correspond à l'hôte
si hôte.pair_gender == other ET visiteur.pair_gender == hôte.gender 
score = score - 2
sinon

// Si le genre du visiteur correspond à la préférence de l'hôte 
// et que le visiteur de spécifie pas de genre 
si hôte.pair_gender == visiteur.gender ET visiteur.pair_gender == other
score = score - 1 
sinon

// Si le genre de préférence de l'hôte correspond au genre du visiteur 
// et que le genre de préférence du visiteur ne correspond pas à celui de l'hôte
si hôte.pair_gender == visiteur.gender ET visiteur.pair_gender != hôte.gender
score = score - 1
sinon

// Si le genre du visiteur ne correspond pas à la préférence de l'hôte 
// et que le genre de l'hôte correspond à la préférence du visiteur
si hôte.pair_gender != visiteur.gender ET visiteur.pair_gender == hôte.gender
score = score - 1
sinon

// Si le genre de l'hôte correspond à la préférence du visiteur ou
// que le genre du visiteur correspond à la préférence de l'hôte
si hôte.gender == visiteur.pair_gender OU visiteur.gender == hôte.pair_gender
score = score - 1
sinon
score = score - 1 //Le genre est forcément other, l'un des deux est satisfait.

retourner score
```

## Retour sur l'exemple


![alt text](image-8.png)
&nbsp;

Après exécution du programme de calcul de poids minimal pour notre graphe, nous obtenons un coût minimal de 5 et notre appariement identifié comme le meilleur correspond bien au résultat donné par l'application: 

#### **Relation entre A&W=0**, c'est bien notre paire la plus optimisée.
___
# Version 2

Ci-dessous, **A1, B1, C1, D1** désignent des noms d'hôtes. Ils possèdent tous des **hobbies**, un **genre**, une **préférence de genre**, une **date de naissance**, un **potentiel animal** et un **régime alimentaire** propre à leur foyer.

Quant à **W1, X1, Y1, Z1**, ce sont des noms de visiteurs. Ils possèdent les mêmes variables qu'un hôte, à l'exception de **potentiel animal** et de **régime alimentaire**. Ils possèdent donc une **allergie aux animaux potentielle** et une **contrainte alimentaire potentielle**.

Démonstration dans la figure 5 ci-dessous.

&nbsp;

![Paires de la version 2](image-11.png)


## Exemple avec appariement total

Dans ce cas de figure, il existe des incompatibilités entre certains hôtes et certains visiteurs, mais il est possible de trouver un appariement qui respecte les contraintes rédhibitoires.
&nbsp;

L'appariement est illustré ci-dessous, nous avons choisi d'appairer les hôtes et les visiteurs en utilisant notre algorithme de calcul d'affinité, les paires illustrées si dessous sont donc celles qui possèdent le degré le plus haut d'affinité.
&nbsp;

![Appariement total](image-10.png)

L'appariement que nous considerons le plus optimisé contient A1-Y1 ou C1-X1 car elles possèdent le plus de critères d'affinité. **Tout en respectant les contraintes rédhibitoires pour les autres paires**.

## Exemple sans appariement total
![Exemple pour l'appariement non total](image-12.png)
Dans ce cas de figure, il existe des compatibilités mais **elles ne permettent pas de trouver un appariement total**.

#### Paires compatibles :
![Appariement non total](image-13.png)

Nous pouvons constater que seulement deux paires sont compatibles, les autres ne remplissant pas les critères rédhibitoires.
&nbsp;

Nous considérons donc que les paires D2-Z2 ou C2-W2 sont les plus optimisées car elles possèdent le plus de critères d'affinité **et sont les seules à respecter les contraines rédhibitoires**.
## Score d'affinité


```JAVA
double score_affinité_2(hote, visiteur)
double score = 5 // valeur initiale du score
// Contraintes rédhibitoires
//Si l'hôte a un animal et que le visiteur a une allergie
si hôte.host_has_animal == yes ET visiteur.guest_animal_allergy == yes
score = score + 5
// Si le régime alimentaire de l'hôte ne contient pas les éléments du régime alimentaire du visiteur
si hôte.List<host_food> ne contient les éléments de visiteur.List<guest_food_constraint>
score = score +5
retourner score

```

## Retour sur l'exemple 1

Après exécution du programme de calcul de poids minimal pour l'exemple de l'appariement total, nous obtenons un coût minimal de **5** et nos appariements identifiés comme les meilleurs **correspondent bien** au résultat donné par l'application:

![Calcul sur l'appariement total](image-14.png)
&nbsp;

## Retour sur l'exemple 2

Après exécution du programme de calcul de poids minimal pour l'exemple de l'appariement non total, nous obtenons un coût minimal de **26** et nos appariements identifiés comme les meilleurs **correspondent bien** au résultat donné par l'application:

![Calcul sur l'appariement non total](image-15.png)

&nbsp;


## Robustesse de la modélisation (question difficile)

  Parlons de la robustesse de la modélisation, c'est-à-dire de la capacité de notre fonction à respecter les contraintes rédhibitoires, quel que soit le jeu de données : 
  Dans notre cas, la fonction `score_affinité_2` garantira toujours le respect des contraintes rédhibitoires, quel que soit le jeu de données; la fonction est toujours appelée avant la fonction `score_affinité_1` et cette dernière ne s'exécute que si les contraintes rédhibitoires sont respectées (score<=5). 

En pseudo code pour illustrer la possible version finale : 

```JAVA
double score = score_affinité_2(hôte, visiteur)
si score <=5
score += score_affinité_1(hôte, visiteur)
sinon
// On ne fait rien, on ne peut pas appairer à cause des contraintes rédhibitoires
retourner score
```
### Notre code peut donc garantir le respect des contraintes rédhibitoires, quel que soit le jeu de données.

____

# Version 3

Ci-dessous, H1, H2, etc. désignent des noms d'hôtes et V1, V2, etc désignent des noms de visiteurs. Ils possèdent tous les critères énumérés jusqu'à présent.
&nbsp;

![Exemple 1 - Version 3](image-16.png)
&nbsp;

## Équilibrage entre affinité / incompatibilité

Les paires ci-dessus, sont quasi équivalentes pour l'affectation. Certaines de ces paires ne respectent pas les contraintes considérées rédhibitoires dans la Version 2, d'autres les respectent.
&nbsp;

**Notre but est donc de trouver un équilibre entre les contraintes rédhibitoires et les contraintes d'affinité ainsi que de trouver un moyen de les compenser.**
&nbsp;

| Rappel des contraintes rédhibitoires |  |
|-----:|---------------|
|Incompatibilité de régime alimentaire|+5 points|
|Incompatibilité d'allergie|+5 points|
|Incompatibilité historique OTHER |+15 points|
&nbsp;

| Rappel des affinités |  |
|-----:|---------------|
|Hobbies (1 seul)|-1 point|
|Hobbies (2 ou plus)|-2 points|
|Genre (1 seul)|-1 point|
|Genre (2 ou plus)|-2 points|
|Âge |-1 point|



**Décortiquons les paires ci-dessus :**
&nbsp;

- H1, V1 : Ils possèdent une forte affinité, mais ne respectent pas les contraintes rédhibitoires. Ils possèdent donc un score 10 d'incompatibilité - 5 d'affinité = Total 5
&nbsp;

- H2, V2 : Ils possèdent une faible affinité, ne respectent **pas toutes** les contraintes rédhibitoires. Ils possèdent donc un score 5 d'incompatibilité - 3 d'affinité = Total 2
&nbsp;

- H3, V3 : Ils possèdent une très faible affinité et respectent les contraintes rédhibitoires. Ils possèdent donc un score 0 d'incompatibilité + 5 d'affinité = Total 5
&nbsp;

- H4, V4 : Ils possèdent une affinité faible, mais respectent les contraintes rédhibitoires. Néanmoins, la colonne `history` est remplie par `other`, ce qui fait que le score d'affinité est de **15**.

-> On arrête alors le traitement. H4 & V4 ne sont pas compatibles.

## Score d'affinité

**Ci-dessous, le pseudo code de score_affinité_3**

_`score_affinité_3(hôte, visiteur)`_

```JAVA
double score_affinité_3(hôte, visiteur)
double score = 5 // valeur initiale du score
// Contraintes rédhibitoires
si hôte.history == other OU visiteur.history == other
retourner score = 15 //Contrainte rédhibitoire totale
sinon
score = score_affinité_2(hôte, visiteur) // On appelle la fonction de score d'affinité 2
score = score_affinité_1(hôte, visiteur) // On appelle la fonction de score d'affinité 1 
retourner score
```
**En partant du fait que les fonctions score_affinité possèdent une variable score commune pour ne pas additionner 5 au début de chaque appel de fonction.**

## Retour sur l'exemple

#### Résultats de `score_affinité_3`, reprenons nos résultats calculés précédemment :

_**Remarque** : Le score le plus bas est le meilleur._

- H1, V1 : Ils possèdent une forte affinité, mais ne respectent pas les contraintes rédhibitoires. Ils possèdent donc un score 10 d'incompatibilité - 5 d'affinité = Total 5
&nbsp;

- H2, V2 : Ils possèdent une faible affinité, ne respectent **pas toutes** les contraintes rédhibitoires. Ils possèdent donc un score 5 d'incompatibilité - 3 d'affinité = Total 2
&nbsp;

- H3, V3 : Ils possèdent une très faible affinité et respectent les contraintes rédhibitoires. Ils possèdent donc un score 0 d'incompatibilité + 5 d'affinité = Total 5
&nbsp;

- H4, V4 : Ils possèdent une affinité faible, mais respectent les contraintes rédhibitoires. Néanmoins, la colonne `history` est remplie par `other`, ce qui fait que le score d'affinité est de **15**.

#### Résultat : 

**On obtient des scores proches, peu importe si les contraintes rédhibitoires sont respectées ou non ainsi que pour les affinités.**
Nous pouvons donc affirmer que des contraintes rédhibitoires peuvent être compensées par des affinités et vice versa. 

Des scores proches peuvent être considérés comme équivalents, mais pas forcément identiques. 

**L'utilisateur doit en priorité savoir si les contraintes rédhibitoires sont un réel frein pour l'appariement.**

### Pour résumer, nous avons donc :
- H1, V1 : 5
- H2, V2 : 2
- H3, V3 : 5
- H4, V4 : 15
&nbsp;

Sur une échelle de **0 à 15**, nous obtenons des scores proches. Mais attention à ne pas mésinterpréter les scores en fonction de l'affinité et des contraintes rédhibitoires.
&nbsp;

Un **15** est clairement un frein à l'appariement, mais un **5** peut être par exemple considéré comme une affinité forte ou faible selon les contraintes rédhibitoires et inversement.
&nbsp;

___