import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Classe outil pour serialiser {@link AdolescentsArray} dans un fichier.
 */
public class Serializer {

    /**
     * Serialise l'adolescent {@link AdolescentsArray} dans un chemin spécifié
     * 
     *
     * @param a {@link AdolescentsArray} l'objet à serialiser 
     * @param filepath le chemin donné pour la sortie 
     */
    public static void serialize(AdolescentsArray a, String filepath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(new File(filepath)))) {
            oos.writeObject((Object) a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
