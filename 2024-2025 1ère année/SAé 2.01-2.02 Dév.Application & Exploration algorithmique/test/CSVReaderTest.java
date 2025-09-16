import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;

public class CSVReaderTest {

    CSVReader csvReader;
    String[] data;
    String file;

    @BeforeEach
    public void initialisation() {

        data = new String[] {
                "male;no;yes;vegetarian,nonuts;vegetarian,nonuts;sports,music;same;male;Doe;John;2005-01-01;FR",
                "female;no;no;nonuts;nonuts;music;other;female;Smith;Jane;2006-02-02;GE",
                "male;no;yes;vegetarian,nonuts;vegetarian,nonuts;sports;same;male;Joe;Donatien;2005-01-01;FR",
                "female;yes;no;none;none;music,sports,books;none;male;Bridget;Emma;2004-03-03;IT"};
        csvReader = new CSVReader();
        file = "./res/Ado.csv";
    }

    @Test
    void testVerifiyData() {
        boolean result = false;
        try {
            csvReader.verifyData(data);
            result = true;
        } catch (InvalidStructureException e) {
            System.err.println(e.getMessage());
        }
        assertTrue(result);
        result = false;
        data = new String[] {};
        try {
            csvReader.verifyData(data);
            result = true;
        } catch (InvalidStructureException e) {
            System.err.println(e.getMessage());
        }
        assertFalse(result);
        data = new String[] { "male;/;yes;/;vegetarian,nonuts;sports;other;male;Doe;John;2005-01-01;FR",
                "female;no;/;nonuts;/;music;Smith;Jane;2006-02-02;GE" };
        try {
            csvReader.verifyData(data);
            result = true;
        } catch (InvalidStructureException e) {
            System.err.println(e.getMessage());
        }
        assertFalse(result);
    }

    @Test
    void testGetNbLinesFile() {
        int result = 0;
        try {
            result = csvReader.getNbLinesFile(" ");
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        assertEquals(0, result);
        try {
            result = csvReader.getNbLinesFile(file);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        assertEquals(4, result);
    }

    @Test
    void testVerifyCSV() {
        boolean result = false;
        try {
            result = csvReader.verifyCSV(" ");
            ;

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (InvalidStructureException e) {
            System.err.println(e.getMessage());
        }
        assertFalse(result);

        try {
            result = csvReader.verifyCSV(file);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (InvalidStructureException e) {
            System.err.println(e.getMessage());
        }
        assertTrue(result);
    }

    @Test
    void testGetDataFromCSV() {
        String[] result = new String[] {};
        try {
            result = csvReader.getDataFromCSV(" ");
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (InvalidStructureException e) {
            System.err.println(e.getMessage());
        }
        assertNotEquals(result, data);
        try {
            result = csvReader.getDataFromCSV(file);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (InvalidStructureException e) {
            System.err.println(e.getMessage());
        }
        assertTrue(Arrays.equals(result, data));
    }
}