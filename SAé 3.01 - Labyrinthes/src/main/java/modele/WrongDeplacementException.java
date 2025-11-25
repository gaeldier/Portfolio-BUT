package modele;

/**
 * Exception runtime signalant qu'un déplacement est invalide (mouvement dans un
 * mur).
 * 
 * @author Gaël Dierynck
 */
public class WrongDeplacementException extends RuntimeException {
    public WrongDeplacementException() {
        super();
    }

    public WrongDeplacementException(String s) {
        super(s);
    }

    public WrongDeplacementException(Throwable cause) {
        super(cause);
    }

    public WrongDeplacementException(String message, Throwable cause) {
        super(message, cause);
    }
}
