import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdolescentsTest {

        ArrayList<Adolescents> al = new ArrayList<>();
        Adolescents adoH1 = new Adolescents("male", "no", "yes", "nonuts", "vegetarian,nonuts", "sports", "other", "male", "Doe", "John", "2005-01-01", "FR");
        Adolescents adoG1 = new Adolescents("female", "no", "no", "nonuts", "nonuts", "music", "other", "female", "Smith", "Jane", "2006-02-02", "GE");

        Adolescents adoH2 = new Adolescents("male", "no", "no", "nonuts", "vegetarian", "sports", "other", "male", "Doe", "John", "2005-09-01", "FR");
        Adolescents adoG2 = new Adolescents("female", "yes", "no", "nonuts", "nonuts", "music", "null", "female", "Smith", "Jane", "2006-02-02", "ES");

        Adolescents adoH3 = new Adolescents("male", "no", "yes", "nonuts,vegetarian", "nonuts", "sports", "other", "male", "Doe", "John", "2005-01-01", "FR");
        Adolescents adoG3 = new Adolescents("male", "no", "yes", "vegetarian", "nonuts", "sports", "same","male", "Ligma", "Marc", "2003-03-03", "FR");


    @BeforeEach
    public void initialization() {
        this.al.add(adoG1);
        this.al.add(adoH1);
        this.al.add(adoH2);
        this.al.add(adoG2);
        this.al.add(adoH3);
        this.al.add(adoG3);
    }

    @Test
    public void testAnimalAllergie() {

        assertEquals(-1, adoH1.animal_allergie(adoG1)); // L'hôte ne possede pas d'animaux et l'invité n'est pas allergique

        assertEquals(-1, adoH2.animal_allergie(adoG2)); // L'hôte n'a pas d'animaux et l'invité est allergique

        assertEquals(-1, adoH3.animal_allergie(adoG3)); // L'hôte a des animaux et l'invité n'est pas allergique

    }

    @Test
    public void testRegimeAlimentaire() {

        //On regarde dans adoH.hfood s'il y a un régime demandé par adoG.gfood
        assertEquals(-1, adoH1.regimeAlimentaire(adoG1)); // L'hôte a "nonuts,vegetarian" et l'invité a "vegetarian"

        //On regarde dans adoH2.hfood s'il y a un régime demandé par adoG2.gfood
        assertEquals(1, adoH2.regimeAlimentaire(adoG2)); // L'hôte a "vegetarian" et l'invité a "nonuts"
    }

    @Test
    public void testListCreator() {

        // On teste la méthode listCreator pour les deux adolescents en vérifiant que la liste créée contient les bons éléments

        ArrayList<String> res1 =  adoH3.listCreator(adoH3.getFood());
        ArrayList<String> res2 =  adoG3.listCreator(adoG3.getFood());
        ArrayList<String> expected = new ArrayList<>();
        ArrayList<String> expected2 = new ArrayList<>();

        expected.add("nonuts");
        expected.add("vegetarian");
        expected2.add("vegetarian");

        assertEquals(expected, res1);
        assertEquals(expected2, res2);

    }

    @Test
    public void testHistory() {

        // On teste la méthode history pour les deux adolescents en vérifiant que l'historique est correct

        assertEquals(5, adoH1.history(adoG1)); // Les deux ont un historique "other" et "other"

        assertEquals(1, adoH1.history(adoG2)); // L'hôte a "other" et l'invité a "null"

        assertEquals(0, adoH1.history(adoG3)); // L'hôte a "other" et l'invité a "same"

    }

    @Test
    public void testCompareNaissance() {

        // On teste la méthode compareNaissance pour les deux adolescents en vérifiant que la différence d'âge est correcte

        int result = adoH2.compareNaissance(adoG2);
        assertEquals(0, result); // Ils ont bien moins d'un an et demi d'écart

        int result2 = adoH1.compareNaissance(adoG3);
        assertEquals(1, result2); // Ils ont plus d'un an et demi d'écart

    }

    @Test
    public void testCompatibility() {

        // On teste la méthode compatibility pour les deux adolescents en vérifiant qu'ils sont compatibles

        Adolescents adoH1 = new Adolescents("male", "no", "yes", "nonuts", "vegetarian,nonuts", "sports","same", "male", "Doe", "John", "2005-09-01", "FR");
        Adolescents adoG1 = new Adolescents("female", "no", "no", "vegetarian,nonuts", "nonuts", "music,sports", "same", "female", "Smith", "Jane", "2006-02-02", "GE");
        boolean result = adoH1.compatibility(adoG1);
        // Ils sont compatibles car ils possèdent tous les critères requis
        assertTrue(result); 

        Adolescents adoH2 = new Adolescents("male", "no", "yes", "nonuts,vegetarian", "nonuts", "sports", "other", "male", "Doe", "John", "2005-01-01", "FR");
        Adolescents adoG2 = new Adolescents("male", "no", "yes", "vegetarian", "nonuts", "sports", "other","male", "Ligma", "Marc", "2005-06-11", "ES");
        boolean result2 = adoH2.compatibility(adoG2);

        assertFalse(result2); // Ils ne sont pas compatibles car ils n'ont pas le même régime alimentaire

        Adolescents adoH3 = new Adolescents("male", "no", "yes", "nonuts", "vegetarian,nonuts", "sports","same", "male", "Doe", "John", "2005-09-01", "FR");
        Adolescents adoG3 = new Adolescents("female", "no", "no", "vegetarian,nonuts", "vegetarian,nonuts", "music", "same", "female", "Smith", "Jane", "2006-02-02", "GE");
        boolean result3 = adoH3.compatibility(adoG3);

        assertFalse(result3); // Ils ne sont pas compatibles car ils sont pas du même pays et ils n'ont pas de hobbies en commun
        
    }

    @Test
    public void testCompatibleAvecFr(){

        // On teste la méthode compatibleAvecFr pour les deux adolescents en vérifiant qu'ils sont compatibles

        Adolescents adoH1 = new Adolescents("male", "no", "yes", "nonuts", "vegetarian,nonuts", "sports","same", "male", "Doe", "John", "2005-09-01", "FR");
        Adolescents adoG1 = new Adolescents("female", "no", "no", "vegetarian,nonuts", "nonuts", "music,sports", "same", "female", "Smith", "Jane", "2006-02-02", "IT");
        assertEquals(0, adoH1.compatibleAvecFr(adoG1)); 

        Adolescents adoH2 = new Adolescents("male", "no", "yes", "nonuts", "vegetarian,nonuts", "sports","same", "male", "Doe", "John", "2005-09-01", "FR");
        Adolescents adoG2 = new Adolescents("female", "no", "no", "vegetarian,nonuts", "nonuts", "music", "same", "female", "Smith", "Jane", "2006-02-02", "IT");
        assertEquals(5, adoH2.compatibleAvecFr(adoG2)); 

        Adolescents adoH3 = new Adolescents("male", "no", "yes", "nonuts", "vegetarian,nonuts", "sports,music","same", "male", "Doe", "John", "2005-09-01", "IT");
        Adolescents adoG3 = new Adolescents("female", "no", "no", "vegetarian,nonuts", "nonuts", "music,sports", "same", "female", "Smith", "Jane", "2006-02-02", "FR");
        assertEquals(0, adoH3.compatibleAvecFr(adoG3));

        Adolescents adoH4 = new Adolescents("male", "no", "yes", "nonuts", "vegetarian,nonuts", "sports,coding,music,hiking,reading","same", "male", "Doe", "John", "2005-09-01", "IT");
        Adolescents adoG4 = new Adolescents("female", "no", "no", "vegetarian,nonuts", "nonuts", "animals,computer science,food", "same", "female", "Smith", "Jane", "2006-02-02", "FR");
        assertEquals(5, adoH4.compatibleAvecFr(adoG4));

    }

    @Test
    public void testSetCritereToNull() {

        // On teste la méthode setCritereToNull pour les deux adolescents en vérifiant que le critère est bien mis à null

        adoG1.setCritereToNull(Critere.GUEST_ANIMAL_ALLERGY);
        assertNull(null, adoG1.getValeurCritere(Critere.GUEST_ANIMAL_ALLERGY));

        assertEquals("vegetarian", adoH2.getValeurCritere(Critere.HOST_FOOD));
        adoH2.setCritereToNull(Critere.HOST_FOOD);
        assertNull(null, adoH2.getValeurCritere(Critere.HOST_FOOD));

    }

    @Test
    public void testCritereInvalide() {
        
        int compteurPreuve = 0;
        Adolescents ado = new Adolescents("male", "maybe", "sometimes", "nonuts", "vegetarian", "sports", "other", "male", "Doe", "John", "2005-01-01", "FR");
        try{
            ado.critereInvalide();
        } catch (InvalidDataException e) {
            assertEquals("Erreur de saisie ! Le critère ne correspond pas" , e.getMessage());
            compteurPreuve++;
        }
        assertEquals(1, compteurPreuve); // On s'assure qu'il y a bien une exception levée
    }

    @Test
    public void testGetScorePourAppariement() {

        // On teste la méthode getScorePourAppariement pour les deux adolescents en vérifiant que le score est correct
        adoH1.getScorePourAppariement(adoG1);
        assertEquals(13, adoH1.getScorePourAppariement(adoG1)); // Ils sont compatibles, donc le score est 0
    }
}