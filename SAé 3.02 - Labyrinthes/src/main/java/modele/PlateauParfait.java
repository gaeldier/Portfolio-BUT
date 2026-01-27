/**
 * Représente un labyrinthe parfait (un seul chemin simple entre deux cellules libres) généré
 * par un parcours en profondeur récursif (algorithme DFS sur une grille de cellules mur / vide).
 * Le plateau interne est entouré d'une bordure de murs afin de simplifier les contrôles de limites.*
 * Invariants :
 * - Les dimensions utiles (sans bordure) sont forcées à être impaires afin de conserver une structure
 *   de couloirs de largeur 1 séparés par des murs.
 * - Une cellule libre est représentée par la valeur boolean false, un mur par true.
 * Structure :
 * - Le tableau 'plateau' est dimensionné avec +2 en largeur et hauteur (bordure externe).
 * - Le percement s'effectue par bonds de 2 cases (on creuse la case cible et la case intermédiaire),
 *   ce qui maintient une alternance mur / passage.*
 * Algorithme :
 * - Génération : remplissage initial de murs puis DFS récursif pseudo-aléatoire (ordre des directions mélangé).
 * - Entrée et sortie sont placées après la génération, avec une contrainte de distance minimale pour la sortie.*
 * Complexité :
 * - Génération : O(W * H) où W = nombre de colonnes internes, H = nombre de lignes internes.*
 * Usage :
 * - Utiliser la méthode statique genererLabyrinthe(...) pour obtenir une instance prête à l'emploi.
 * @author Dawid Banas, Gaël Dierynck
 * @version 1.0
 * @see AbstractPlateau
 */

package modele;

import java.util.Random;

/**
 * Labyrinthe parfait généré avec DFS (algorithme de parcours en profondeur).
 * 
 * @author Dawid Banas, Gaël Dierynck
 * @version 1.0
 * @see AbstractPlateau
 */
public class PlateauParfait extends AbstractPlateau {

    private final Random rand = new Random();

    /**
     * Tableau constant des 4 directions cardinales (dx, dy) :
     * EST (1, 0), OUEST (-1, 0), SUD (0, 1), NORD (0, -1).
     * Utilisé pour le déplacement lors de la génération DFS.
     */

    private static final int[][] DIRS = {
            { 1, 0 }, // EST
            { -1, 0 }, // OUEST
            { 0, 1 }, // SUD
            { 0, -1 } // NORD
    };

    /**
     * Construit un plateau parfait avec dimensions demandées.
     * Les dimensions sont ajustées pour être impaires (si paire, +1) afin de
     * préserver l'algorithme de génération.*
     * Paramètres :
     * 
     * @param ligne   nombre de lignes souhaité (sera ajusté à impair si nécessaire)
     * @param colonne nombre de colonnes souhaité (sera ajusté à impair si
     *                nécessaire)*
     *                Exceptions :
     * @throws IllegalArgumentException si ligne <= 0 ou colonne <= 0
     */
    public PlateauParfait(int ligne, int colonne) {
        if (ligne <= 0 || colonne <= 0)
            throw new IllegalArgumentException("Dimensions > 0");

        ligne = (ligne % 2 == 0) ? ligne + 1 : ligne;
        colonne = (colonne % 2 == 0) ? colonne + 1 : colonne;

        // tableau + bordures
        plateau = new boolean[ligne + 2][colonne + 2];
    }

    /**
     * Méthode fabrique générant un labyrinthe parfait complet avec entrée et
     * sortie.*
     * Étapes :
     * 1. Création de l'instance.
     * 2. Pose des bordures externes (mur tout autour).
     * 3. Génération du réseau interne par DFS.
     * 4. Placement de l'entrée.
     * 5. Placement de la sortie à une distance minimale.*
     * Paramètres :
     * 
     * @param ligne       nombre de lignes (sera forcé à impair si nécessaire)
     * @param colonne     nombre de colonnes (sera forcé à impair si nécessaire)
     * @param distanceMin distance minimale exigée entre l'entrée et la sortie
     *                    (interprétation dépend
     *                    de l'implémentation dans placerSortie)*
     *                    Retour :
     * @return une instance de PlateauParfait entièrement générée
     */
    public static PlateauParfait genererLabyrinthe(int ligne, int colonne, int distanceMin) {

        PlateauParfait p = new PlateauParfait(ligne, colonne);

        // bordures
        for (int x = 0; x < p.getNbColonne(); x++) {
            p.setMur(new Cases(x, 0));
            p.setMur(new Cases(x, p.getNbLigne() - 1));
        }
        for (int y = 0; y < p.getNbLigne(); y++) {
            p.setMur(new Cases(0, y));
            p.setMur(new Cases(p.getNbColonne() - 1, y));
        }

        // DFS
        p.generateDFS();
        p.placerEntree();
        p.placerSortie(distanceMin);

        return p;
    }

    /**
     * Initialise la grille interne en remplissant toutes les cases de murs puis
     * lance
     * le percement aléatoire via la fonction récursive carve à partir de la cellule
     * (1,1).*
     * Détails :
     * - Ignore les bordures (y et x de 1 à longueur-2).
     * - Point de départ fixe : coin supérieur gauche interne.
     */
    private void generateDFS() {

        // remplit tout de murs
        for (int y = 1; y < plateau.length - 1; y++)
            for (int x = 1; x < plateau[0].length - 1; x++)
                plateau[y][x] = true;

        carve(1, 1);
    }

    /**
     * Fonction récursive de percement (DFS).*
     * Principe :
     * - Marque la cellule courante comme vide.
     * - Mélange l'ordre des directions cardinales.
     * - Pour chaque direction : saute de 2 cases (nx, ny). Si la cible est dans les
     * bornes et encore mur :
     * - Ouvre la case intermédiaire (y + dy, x + dx).
     * - Ouvre la case cible (ny, nx).
     * - Recurse sur la case cible.*
     * Paramètres :
     * 
     * @param x coordonnée colonne interne (>=1 et < nbColonne-1)
     * @param y coordonnée ligne interne (>=1 et < nbLigne-1)*
     *          Effets :
     *          - Modifie le tableau 'plateau' en créant des passages.*
     *          Attention :
     *          - La structure parfaite est garantie car chaque cellule est visitée
     *          une seule fois et aucun cycle n'est créé.
     */
    private void carve(int x, int y) {
        plateau[y][x] = false;

        int[] order = { 0, 1, 2, 3 };
        shuffle(order);

        for (int i : order) {
            int dx = DIRS[i][0];
            int dy = DIRS[i][1];

            int nx = x + dx * 2;
            int ny = y + dy * 2;

            if (nx > 0 && nx < getNbColonne() - 1 &&
                    ny > 0 && ny < getNbLigne() - 1 &&
                    plateau[ny][nx]) {

                // creuse la case intermédiaire
                plateau[y + dy][x + dx] = false;

                // creuse la case cible
                plateau[ny][nx] = false;

                carve(nx, ny);
            }
        }
    }

    /**
     * Mélange en place un tableau d'indices représentant les directions.*
     * Algorithme :
     * - Fisher-Yates (Knuth Shuffle), complexité O(n).*
     * Paramètres :
     * 
     * @param array tableau d'entiers à permuter (modifié en place)
     */

    private void shuffle(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }
}
