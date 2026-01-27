package modele;

/**
 * Signale qu'un des critères n'est pas valide.
 * 
 * @author Gaël Dierynck
 */
@SuppressWarnings("unused")
public class InvalidDataException extends Exception {

    /**
     * Construit une nouvelle InvalidStructureException.
     */
    public InvalidDataException() {
        super();
    }

    /**
     * Construit une nouvelle InvalidStructureException avec le message détaillé
     * spécifié.
     *
     * @param m le message détaillé
     */
    public InvalidDataException(String m) {
        super(m);
    }
}
