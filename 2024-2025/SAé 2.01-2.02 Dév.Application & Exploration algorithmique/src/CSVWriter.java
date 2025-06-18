import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe d'outil pour exporter nos listes vers un fichier CSV spécifié
 */

public class CSVWriter {

    /**
     * 
     * 
     * 
     * @param data ensemble de données à exporter au format CSV
     * @param path chemin vers le fichier à exporter
     * @throws IOException Si une erreur concernant l'IO se produit
     */
    public void exportToCSV(String[] data,String path) throws IOException {
        FileWriter writer = new FileWriter(path);
        for(String ligne : data) {
            try {
                writer.write(ligne + "\n");
            } catch (Exception e) {
                System.out.println("Erreur lors de l'écriture dans le fichier CSV : " + e.getMessage());
            }
        }
        writer.close();
        System.out.println("Exportation vers le fichier CSV réussie : " + path);
    }
}
