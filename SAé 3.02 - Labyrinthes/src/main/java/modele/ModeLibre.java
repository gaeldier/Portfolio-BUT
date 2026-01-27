package modele;

/**
 * Représente le mode de jeu "Libre" d'un labyrinthe.
 * Dans ce mode, le labyrinthe est généré avec des dimensions et une densité de
 * murs spécifiées,
 * mais les points d'entrée et de sortie sont choisis de manière aléatoire sur
 * les bords du plateau.
 * Cette classe hérite de {@link Labyrinthe} et utilise {@link PlateauAleatoire
 * genererLabyrinthe(int, int, double, int[], int[])}
 * pour la création effective du labyrinthe.
 * 
 * @see Labyrinthe
 * @see PlateauAleatoire
 * @author Gaël Dierynck
 * @version 1.0
 */
public class ModeLibre extends Labyrinthe {

    public ModeLibre(int nbLigne, int nbColonne, int dif, String generation, Orientation orientation) {
        super(2, 2, 0.0);
        switch (generation) {
            case "aleatoire":
                this.creerLabyrintheAleatoire(nbLigne, nbColonne, dif / 100.0);
                break;
            case "bfs":
                this.creerLabyrintheParfait(nbLigne, nbColonne, dif);
                break;
            case "tree":
                this.creerLabyrintheBinaryTree(nbLigne, nbColonne, dif, orientation);
        }
    }

}