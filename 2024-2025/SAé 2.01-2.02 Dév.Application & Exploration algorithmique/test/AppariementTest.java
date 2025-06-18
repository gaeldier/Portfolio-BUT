import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppariementTest {

    ArrayList<Adolescents> listAdolescents = new ArrayList<>();
    Appariement ap = new Appariement(listAdolescents);
    Adolescents adoH1 = new Adolescents("male", "no", "yes", "nonuts", "nonuts", "sports", "other", "male", "Vanhoutte",
            "Amaury", "2005-01-01", "FR");
    Adolescents adoG1 = new Adolescents("female", "no", "no", "nonuts", "nonuts", "music", "other", "female",
            "Dierynck", "Gaël", "2006-02-02", "GE");
    Adolescents adoH2 = new Adolescents("male", "no", "no", "nonuts", "nonuts", "sports", "other", "male", "Harlaut",
            "Romain", "2005-01-01", "FR");

    @BeforeEach
    public void initialization() {
        this.listAdolescents.add(adoG1);
        this.listAdolescents.add(adoH1);
        this.listAdolescents.add(adoH2);
        this.ap = new Appariement(listAdolescents);
    }

    @Test
    public void testInit() {
        assertEquals(3, ap.size());
    }

    @Test
    public void testRemoveAdolescent() {
        assertEquals(3, ap.size());
        ap.removeAdolescent("Vanhoutte", "Amaury");
        ap.removeAdolescent(adoG1);
        assertEquals(1, ap.size());

    }

    @Test
    public void testAddAdolescent() {
        Adolescents adoG2 = new Adolescents("male", "no", "no", "nonuts", "nonuts", "music", "other", "female", "Smith",
                "Jane", "2006-02-02", "GE");
        ap.addAdolescent(adoG2);
        assertEquals(4, ap.size());
    }

    @Test
    public void testIncoherencesInAdolescents() {
        assertEquals(3, ap.size());
        Adolescents adoProblematique = new Adolescents("male", "yes", "yes", "nonuts", "nonuts", "music", "other",
                "female", "Baratheon", "Robert", "2006-02-02", "GE");
        ap.addAdolescent(adoProblematique);
        assertEquals(4, ap.size());
        try {
            ap.incoherencesInAdolescents();
        } catch (InvalidDataException e) {
            System.out.println("L'adolescent n'est pas valide, suppression de " + adoProblematique.getPrenom() + " "
                    + adoProblematique.getNom());
        } finally {
            System.out.println(listAdolescents);
        }
        assertEquals(3, ap.size()); // L'adolescent problématique a bien été supprimé
    }

    @Test
    public void testGetNextAdolescent() {
        assertEquals(adoH2, ap.getNextAdolescent(ap.getAl(), adoH1)); // C'est bien adoH2 après adoH1
        assertEquals(adoH2, ap.getNextAdolescent(ap.getAl(), adoH1)); // On demande le dernier élément + 1, on récupère le dernier élément.
        assertNotEquals(adoH2, ap.getNextAdolescent(ap.getAl(), adoG1)); // adoH2 n'est pas après adoG1
    }

    @Test
    public void testAppariementTotal() {
        AdolescentsArray[] aa = new AdolescentsArray[ap.sizeDansTableauMAX()];
        try {
            ap.incoherencesInAdolescents(); // Vérification des incohérences avant l'appariement
            for (int i = 0; i < aa.length; ++i) {
                aa[i] = new AdolescentsArray(ap.appariementTotal(ap.getHashMapPair()));
                aa[i].removeSelfPairing(aa[i]);
                System.out.println(aa[i].toString());
            }
        } catch (InvalidDataException | IOException e) {
            System.out.println("Erreur dans l'appariement : " + e.getMessage());
        }
    }

    @Test
    public void testAutomaticLoader() {
        AdolescentsArray a;
        String path = "./test/resTest/config.class";
        File f = new File(path);
        try{
            a = Appariement.automaticLoader(path);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

}