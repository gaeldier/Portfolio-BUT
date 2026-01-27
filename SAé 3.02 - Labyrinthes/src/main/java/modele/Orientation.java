/**
 * Représente les orientations diagonales disponibles (NORD_EST, NORD_OUEST, SUD_EST, SUD_OUEST).*
 * Chaque valeur d'énumération définit une orientation diagonale comme la combinaison
 * de deux vecteurs orthogonaux exprimés par des décalages entiers (dx, dy).
 * Ces vecteurs correspondent aux deux composantes cardinales qui forment la diagonale.
 * Convention de coordonnées :
 * - l'axe x croît vers l'est (valeurs positives → déplacement vers la droite),
 * - l'axe y croît vers le sud (valeurs positives → déplacement vers le bas).
 * Exemples d'interprétation des constantes :
 * - NORD_EST :  direction 1 = (0, -1) -> nord, direction 2 = (1, 0) -> est
 * - NORD_OUEST : direction 1 = (0, -1) -> nord, direction 2 = (-1, 0) -> ouest
 * - SUD_EST :   direction 1 = (0, 1)  -> sud,  direction 2 = (1, 0)  -> est
 * - SUD_OUEST : direction 1 = (0, 1)  -> sud,  direction 2 = (-1, 0) -> ouest
 * Champs publics :
 * - dx1, dy1 : composantes du premier vecteur de direction.
 * - dx2, dy2 : composantes du second vecteur de direction.
 * Constructeur :
 * - Orientation(int dx1, int dy1, int dx2, int dy2) : initialise les quatre composantes
 *   dans l'ordre indiqué.
 * @author Dawid Banas
 * @version 1.0
 */
package modele;

public enum Orientation {

    NORD_EST(0, -1, 1, 0),
    NORD_OUEST(0, -1, -1, 0),
    SUD_EST(0, 1, 1, 0), // trop facile
    SUD_OUEST(0, 1, -1, 0); // trop facile

    public final int dx1, dy1; // direction 1
    public final int dx2, dy2; // direction 2

    Orientation(int dx1, int dy1, int dx2, int dy2) {
        this.dx1 = dx1;
        this.dy1 = dy1;
        this.dx2 = dx2;
        this.dy2 = dy2;
    }
}
