
package modele;

import java.util.Random;

/**
 *
 *
 * Représente un plateau (labyrinthe) généré avec l'algorithme Binary Tree
 * orienté.
 * <p>
 * L'algorithme parcourt les cellules de coordonnées impaires (grille logique)
 * et,
 * pour chacune, ouvre aléatoirement un passage dans l'une des deux directions
 * autorisées par l'Orientation (dx1, dy1) ou (dx2, dy2), si cela reste dans les
 * limites internes du plateau. Cette variante orientée permet de produire des
 * labyrinthes aux motifs contrôlés (ex. diagonale montante ou descendante).
 * </p>
 * <p>
 * Convention interne :<br>
 * - plateau[y][x] == true : mur<br>
 * - plateau[y][x] == false : passage<br>
 * Des bordures pleines sont ajoutées tout autour (matrice agrandie de +2 en
 * largeur et hauteur) pour simplifier les tests limites.
 * </p>
 * <p>
 * Remarque : privilégier une orientation NORD_EST ou NORD_OUEST afin d'obtenir
 * des labyrinthes moins linéaires. Une orientation tournée vers le SUD produit
 * souvent des motifs trop simples (couloirs descendants répétitifs).
 * </p>
 * <p>
 * Complexité temporelle de la génération : O((lignes * colonnes) / 4)
 * (itération
 * uniquement sur les cellules impaires). Complexité spatiale : O(lignes *
 * colonnes).
 * </p>
 *
 * 
 * 
 * @author Dawid Banas, Gaël Dierynck
 * @version 1.0
 * @see AbstractPlateau
 */
public class PlateauBinaryTree extends AbstractPlateau {

    private final Random rand = new Random();
    private final Orientation orientation;

    /**
     * Construit un plateau pour l'algorithme Binary Tree orienté.
     * Ajuste automatiquement les dimensions demandées pour qu'elles soient impaires
     * (condition nécessaire : les cellules de travail sont aux indices impairs).
     *
     * @param ligne       nombre de lignes désiré (doit être > 0 ; sera rendu impair
     *                    si nécessaire)
     * @param colonne     nombre de colonnes désiré (doit être > 0 ; sera rendu
     *                    impair si nécessaire)
     * @param orientation orientation contrôlant les deux directions potentielles
     *                    d'ouverture
     * @throws IllegalArgumentException si ligne <= 0 ou colonne <= 0
     */
    public PlateauBinaryTree(int ligne, int colonne, Orientation orientation) {

        // !!! Utiliser orientation NORD-EST ou NORD-OUEST car SUD trop simple
        if (ligne <= 0 || colonne <= 0)
            throw new IllegalArgumentException("Dimensions > 0");

        ligne = (ligne % 2 == 0) ? ligne + 1 : ligne;
        colonne = (colonne % 2 == 0) ? colonne + 1 : colonne;

        plateau = new boolean[ligne + 2][colonne + 2];
        this.orientation = orientation;
    }

    /**
     * Fabrique et génère un labyrinthe orienté via l'algorithme Binary Tree.
     * Étapes :
     * 1. Création du plateau avec dimensions impaires.
     * 2. Pose des bordures externes (murs).
     * 3. Exécution de la génération orientée.
     * 4. Placement d'une entrée puis d'une sortie à au moins distanceMin.
     *
     * @param ligne       nombre de lignes initial souhaité (ajusté en impair)
     * @param colonne     nombre de colonnes initial souhaité (ajusté en impair)
     * @param distanceMin distance minimale exigée entre l'entrée et la sortie
     * @param orientation orientation déterminant les deux directions de percement
     * @return instance de PlateauBinaryTree entièrement générée
     */
    public static PlateauBinaryTree genererLabyrinthe(int ligne, int colonne, int distanceMin,
            Orientation orientation) {

        PlateauBinaryTree p = new PlateauBinaryTree(ligne, colonne, orientation);

        // Bordures externes
        for (int x = 0; x < p.getNbColonne(); x++) {
            p.setMur(new Cases(x, 0));
            p.setMur(new Cases(x, p.getNbLigne() - 1));
        }
        for (int y = 0; y < p.getNbLigne(); y++) {
            p.setMur(new Cases(0, y));
            p.setMur(new Cases(p.getNbColonne() - 1, y));
        }

        p.generateBinaryTree();

        p.placerEntree();
        p.placerSortie(distanceMin);
        return p;
    }

    /**
     * Génère le labyrinthe selon l'algorithme Binary Tree orienté.
     */
    private void generateBinaryTree() {

        // Tout mettre en murs
        for (int y = 1; y < getNbLigne() - 1; y++)
            for (int x = 1; x < getNbColonne() - 1; x++)
                plateau[y][x] = true;

        for (int y = 1; y < getNbLigne() - 1; y += 2) {
            for (int x = 1; x < getNbColonne() - 1; x += 2) {

                plateau[y][x] = false;

                boolean peut1 = peutCreuser(x, y, orientation.dx1, orientation.dy1);
                boolean peut2 = peutCreuser(x, y, orientation.dx2, orientation.dy2);

                if (peut1 && peut2) {
                    if (rand.nextBoolean())
                        ouvrir(x, y, orientation.dx1, orientation.dy1);
                    else
                        ouvrir(x, y, orientation.dx2, orientation.dy2);
                } else if (peut1)
                    ouvrir(x, y, orientation.dx1, orientation.dy1);
                else if (peut2)
                    ouvrir(x, y, orientation.dx2, orientation.dy2);
            }
        }
    }

    /**
     * Vérifie si l'on peut creuser (ouvrir) à partir d'une cellule de référence
     * (x, y) dans la direction (dx, dy) en restant dans les limites internes
     * (exclut les bordures externes).
     *
     * @param x  abscisse de la cellule de base (impair attendue en génération)
     * @param y  ordonnée de la cellule de base (impair attendue en génération)
     * @param dx delta en x (provenant de l'orientation)
     * @param dy delta en y (provenant de l'orientation)
     * @return true si la cellule cible (x + dx, y + dy) est strictement à
     *         l'intérieur des murs frontières
     */
    private boolean peutCreuser(int x, int y, int dx, int dy) {
        int nx = x + dx;
        int ny = y + dy;
        return nx > 0 && nx < getNbColonne() - 1 && ny > 0 && ny < getNbLigne() - 1;
    }

    /**
     * Ouvre (creuse) un passage depuis la cellule (x, y) vers (x + dx, y + dy).
     * Pose un passage en mettant la case cible à false (non-mur).
     *
     * @param x  abscisse de départ
     * @param y  ordonnée de départ
     * @param dx delta en x
     * @param dy delta en y
     */
    private void ouvrir(int x, int y, int dx, int dy) {
        plateau[y + dy][x + dx] = false;
    }
}
