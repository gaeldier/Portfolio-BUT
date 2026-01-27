package modele;

/**
 * Représente les directions cardinales avec une description textuelle associée.
 * Chaque direction est associée à une chaîne de caractères qui décrit son
 * orientation.
 * 
 * @author Gaël Dierynck
 */
public enum Direction {
    NORD("haut"), SUD("bas"), EST("droite"), OUEST("gauche");

    final String value;

    Direction(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
