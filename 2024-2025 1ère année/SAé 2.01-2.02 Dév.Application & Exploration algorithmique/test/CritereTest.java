import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CritereTest {
    Appariement app = new Appariement();
    Adolescents adoH1 = new Adolescents("male", "no", "yes", "nonuts", "nonuts", "sports", "other", "male", "Doe", "John", "2005-01-01", "FR");
    Adolescents adoG1 = new Adolescents("female", "yes", "no", "nonuts", "nonuts", "music", "other", "female", "Smith", "Jane", "2006-02-02", "GE");
    Adolescents adoH2 = new Adolescents("male", "no", "no", "nonuts", "nonuts", "sports", "other", "male", "Doe", "John", "2005-01-01", "FR");

    @BeforeEach
    public void initialization() {
        this.app.addAdolescent(adoH1);
        this.app.addAdolescent(adoG1);
        this.app.addAdolescent(adoH2);
    }
    
    @Test
    public void testInit() {
        assertEquals(3, app.size());
    }

    @Test
    public void testIsInvalid() {
        try {
            Critere.isInvalid();
        } catch (InvalidDataException e) {
            assertEquals("Erreur de saisie ! Le critère ne correspond pas", e.getMessage());
            return;
        }
    }
}