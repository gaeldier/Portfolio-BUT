/**
 * Contrôleur du jeu.
 * Fournit les classes de contrôle pour gérer les interactions utilisateur
 * et délègue les déplacements au modèle (Labyrinthe).
 * @author Mark Zavadskyi, Ylann Wattrelos
 */
package controleur;

import modele.*;

@SuppressWarnings("ClassCanBeRecord")
public class JeuControleur {
    private final Labyrinthe labyrinthe;

    public JeuControleur(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
    }

    public void deplacementJoueur(Direction direction) throws WrongDeplacementException {
        this.labyrinthe.deplacementJoueur(direction);
    }

}
