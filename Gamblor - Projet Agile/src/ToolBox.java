import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.FileReader;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.FileInputStream;


public class ToolBox {

    public static void scoreWriter(String scoreMax, String nomUtil, String argentRestant, String stress,
            String filepath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath, true))) {
            bw.write("Joueur :" + nomUtil + "  a fini avec " + scoreMax + "  point(s), " + argentRestant
                    + "€ restant(s) dans son porte-monnaie et " + stress + " de stress.");
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Aie, ça s'est mal passé je crois");
            e.printStackTrace();
        }
    }

    public static String scoreReader(String filepath) {
        String res = "";
        try (Reader fr = new FileReader(filepath)) {
            StringBuilder sb = new StringBuilder();
            int c = fr.read();
            while (c != -1) {
                sb.append((char) c);
                c = fr.read();
            }
            res = sb.toString();
            return res;
        } catch (IOException e) {
            System.out.println("Aie, ça s'est mal passé pour lire je crois");
            e.printStackTrace();
            res = "NULL";
            return res;
        }
    }


    public static void saveSerialScore(Joueur j) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("./res/Scores/scores.kar")))) {
            oos.writeObject((Object) j);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String loadSerialScore() {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("./res/Scores/scores.kar")))) {
            Joueur tmp = (Joueur) ois.readObject();
            return tmp.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
