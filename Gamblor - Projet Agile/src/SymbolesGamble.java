import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public enum SymbolesGamble {
    SPADE("./res/spade.txt"), HEART("./res/heart.txt"), STAR("./res/star.txt");

    private final String PATH;

    private SymbolesGamble(String path) {
        this.PATH = path;
    }

    public String returnPath() {
        return this.PATH;
    }

    public static void returnAscii(SymbolesGamble symb) {
        SymbolesGamble.printArt(symb);
    }

    private static void printArt(SymbolesGamble symb) {
        try (Reader fr = new FileReader(symb.returnPath())) {
            StringBuilder sb = new StringBuilder();
            int c = fr.read();
            while (c != -1) {
                sb.append((char) c);
                c = fr.read();
            }
            System.out.println(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + symb.returnPath());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Reading error: " + e.getMessage());
        }
    }

}