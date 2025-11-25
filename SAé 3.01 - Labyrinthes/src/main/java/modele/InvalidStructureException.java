package modele;

/**
 * Signale que la structure du fichier parcouru est invalide.
 * 
 * @author Gaël Dierynck
 */
@SuppressWarnings("unused")
public class InvalidStructureException extends Exception {

    /**
     * Construit une nouvelle InvalidStructureException sans message détaillé.
     */
    public InvalidStructureException() {
        super();
    }

    /**
     * Construit une nouvelle InvalidStructureException avec le message détaillé
     * spécifié.
     *
     * @param m le message détaillé
     */
    public InvalidStructureException(String m) {
        super(m);
    }
}
