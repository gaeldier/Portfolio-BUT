import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CSVReader {

    /**
     * Vérifie la structure d'un fichier CSV.
     * 
     * @param file Le chemin du fichier CSV à vérifier.
     * @return true si la structure est valide, false sinon.
     * @throws FileNotFoundException Si le fichier n'est pas trouvé.
     * @throws InvalidStructureException Si la structure du fichier est invalide.
     */
    public boolean verifyCSV(String file) throws FileNotFoundException, InvalidStructureException {
        boolean result = true;
        Scanner sc = new Scanner(new File(file));
        sc.useDelimiter(";|\n");
        while (sc.hasNextLine()) {
            for (int idx = 0; idx < 12; idx++) {
                if (!sc.hasNext()) {
                    sc.close();
                    throw new InvalidStructureException("Structure du fichier invalide");
                }
                sc.next();
            }
        }
        sc.close();
        return result;
    }

    /**
     * Vérifie la structure des données d'un tableau de chaînes de caractères.
     * 
     * @param data Le tableau de chaînes de caractères à vérifier.
     * @return true si la structure est valide, false sinon.
     * @throws InvalidStructureException Si la structure du tableau est invalide.
     */
    public boolean verifyData(String[] data) throws InvalidStructureException {
        if (data == null || data.length == 0) throw new InvalidStructureException("Structure du fichier invalide.");
        boolean result = true;
        int idx=0;
        while(idx<data.length && result) {
            String[] res = data[idx].split(";");
            if(res.length != 12) throw new InvalidStructureException("Structure du fichier invalide.");
            idx++;
        }
        return result;
    }

    /**
     * Récupère les données d'un fichier CSV.
     * 
     * @param file Le chemin du fichier CSV à lire.
     * @return Un tableau de chaînes de caractères contenant les données du fichier.
     * @throws FileNotFoundException Si le fichier n'est pas trouvé.
     * @throws InvalidStructureException Si la structure du fichier est invalide.
     */
    public String[] getDataFromCSV(String file) throws FileNotFoundException, InvalidStructureException {
        String[] data = new String[0];
        int nbLines = this.getNbLinesFile(file);
        data = new String[nbLines];
        if (verifyCSV(file)) {
            Scanner sc = new Scanner(new File(file));
            sc.useDelimiter("\n");
            for (int idx = 0; idx < nbLines; idx++) {
                data[idx] = sc.nextLine();
            }
            sc.close();
        }
        return data;
    }

    /**
     * Compte le nombre de lignes dans un fichier.
     * 
     * @param path Le chemin du fichier à lire.
     * @return Le nombre de lignes dans le fichier.
     * @throws FileNotFoundException Si le fichier n'est pas trouvé.
     */
    public int getNbLinesFile(String path) throws FileNotFoundException {
        int nbLines = 0;
        try {
            File file = new File(path);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                nbLines++;
                sc.nextLine();
            }
            sc.close();
        }
        catch (FileNotFoundException e) {System.err.println(e.getMessage());}
        return nbLines;
    }
}
