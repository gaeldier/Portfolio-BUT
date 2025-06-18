import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Classe outil pour déserialiser un {@link AdolescentsArray} à un emplacement donné
 */
public class DeSerializer {

     /**
     * Déserialise l'adolescent {@link AdolescentsArray} du fichier au chemin spécifié 
     * 
     *
     * @param filepath le chemin donné pour la sortie 
     * @return l'objet AdolescentsArray
     */
    public static AdolescentsArray DeSerialize(String filepath) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(new File(filepath)))) {
            return (AdolescentsArray) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
    