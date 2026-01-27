/**
 * Crée un nouveau nœud avec la case et la distance spécifiées.
 *
 * @param c la case associée à ce nœud (ne doit pas être null)
 * @param distance la distance associée à cette case (par exemple en nombre de pas)
 * Retourne la case associée à ce nœud.
 *
 * @return l'objet Cases associé
 * Retourne la distance stockée dans ce nœud.
 *
 * @return la distance (entier)
 * @author Ylann Wattrelos
 * @version 1.0
 */
package modele;

@SuppressWarnings("ClassCanBeRecord")
public class Noeuds {
    private final Cases c;
    private final int distance;

    public Noeuds(Cases c, int distance) {
        this.c = c;
        this.distance = distance;
    }

    public Cases getCase() {
        return c;
    }

    public int getDistance() {
        return distance;
    }
}
