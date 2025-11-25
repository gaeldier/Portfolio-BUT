package modele;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.util.*;

public class TestToolBox {

    @Test
    public void testRecupAllPseudo_basicCSV() throws Exception {
        Path temp = Files.createTempFile("test", ".csv");
        Files.writeString(temp,
                "pseudo;score\n" +
                "Alice;10\n" +
                "Bob;20\n");

        List<String[]> res = ToolBox.recupAllPseudo(temp.toString());

        Assertions.assertEquals(2, res.size());
        Assertions.assertArrayEquals(new String[]{"Alice", "10"}, res.get(0));
        Assertions.assertArrayEquals(new String[]{"Bob", "20"}, res.get(1));
    }

    @Test
    public void testRecupAllPseudo_ignorerLignesVides() throws Exception {
        Path temp = Files.createTempFile("test", ".csv");
        Files.writeString(temp,
                "pseudo;score\n" +
                "Alice;10\n" +
                "   \n" +
                "Bob;20\n");

        List<String[]> res = ToolBox.recupAllPseudo(temp.toString());

        Assertions.assertEquals(2, res.size());
    }

    @Test
    public void testRecupAllPseudo_fileNotFound() {
        List<String[]> res = ToolBox.recupAllPseudo("fichier_inexistant.csv");
        Assertions.assertTrue(res.isEmpty());
    }

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        ListeJoueursSerializable ljs = new ListeJoueursSerializable();
        ljs.add(new Joueur("Alice", 0, 0));
        ljs.add(new Joueur("Bob", 0, 0));

        Path temp = Files.createTempFile("save", ".bin");

        ToolBox.serialize(ljs, temp.toString());
        ListeJoueursSerializable load = ToolBox.DeSerialize(temp.toString());

        Assertions.assertEquals(ljs.getListJoueur().size(), load.getListJoueur().size());

        Set<String> nomsOrig = new HashSet<>();
        for(Joueur j : ljs.getListJoueur())
            nomsOrig.add(j.getNom());
        Set<String> nomsLoad = new HashSet<>();
        for(Joueur j : load.getListJoueur())
            nomsLoad.add(j.getNom());

        Assertions.assertEquals(nomsOrig, nomsLoad);
    }

    @Test
    public void testDeserialize_invalidFile() throws Exception {
        Path temp = Files.createTempFile("invalid", ".bin");
        Files.writeString(temp, "pas un fichier serialisé");

        ListeJoueursSerializable load = ToolBox.DeSerialize(temp.toString());
        Assertions.assertNotNull(load);
        Assertions.assertTrue(load.getListJoueur().isEmpty());
    }
}
