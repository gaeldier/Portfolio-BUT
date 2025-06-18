/**
 * Signale qu'un des critères n'est pas valide.
 */
public class InvalidDataException extends Exception {
    
    /**
     * Construit une nouvelle InvalidStructureException sans message détaillé.
     */
    public InvalidDataException() {
        super();
    }

     /**
     * Construit une nouvelle InvalidStructureException avec le message détaillé spécifié.
     * 
     * @param msg le message détaillé
     */
    public InvalidDataException(String m) {
        super(m);
    }
}
