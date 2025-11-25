/**
 * Lit un fichier CSV, ignore la première ligne (en-tête) et retourne le contenu.
 * Chaque ligne est divisée en un tableau de chaînes de caractères en utilisant le point-virgule comme délimiteur.
 * Les lignes vides sont filtrées.
 *
 * @param fichier Le chemin du fichier CSV à lire.
 * @return Une liste de tableaux de chaînes de caractères, où chaque tableau représente une ligne du fichier.
 *         Retourne une liste vide en cas d'erreur de lecture du fichier.
 * @author Gaël Dierynck
 * @version 1.0
 */
package modele;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.List;

/**
 * Classe ToolBox ;
 * Possède plusieurs méthodes 'outils' tel qu'un Sérialiseur & un import vers
 * fichier CSV ainsi que leur contraire
 * des méthodes pour vérifier la validité du tel fichier CSV sont fournies
 * 
 * @author Gaël Dierynck
 * @version 1.0
 */

public class ToolBox {

    public static List<String[]> recupAllPseudo(String fichier) {
        Path path = Paths.get(fichier);
        try (java.util.stream.Stream<String> lines = Files.lines(path, java.nio.charset.StandardCharsets.UTF_8)) {
            return lines.skip(1).filter(line -> !line.trim().isEmpty()).map(line -> line.split(";"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier de sauvegarde : " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Serialise l'adolescent {@link ListeJoueursSerializable} dans un chemin
     * spécifié
     * 
     *
     * @param ljs      {@link ListeJoueursSerializable} l'objet à serialiser
     * @param filepath le chemin donné pour la sortie
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void serialize(ListeJoueursSerializable ljs, String filepath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filepath))) {
            oos.writeObject((Object) ljs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Déserialise l'adolescent {@link ListJoueurSerializable} du fichier au chemin
     * spécifié
     * 
     *
     * @param filepath le chemin donné pour la sortie
     * @return l'objet ListeJoueursSerializable
     */
    @SuppressWarnings({ "JavadocReference" })
    public static ListeJoueursSerializable DeSerialize(String filepath) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filepath))) {
            return (ListeJoueursSerializable) ois.readObject();
        } catch (Exception e) {
            return new ListeJoueursSerializable();
        }
    }
}
