import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CSVWriterTest {
    CSVWriter csvWriter;
    String[] data;
    String file;

    @BeforeEach
    void initialisation() {
        data = new String[] {
                "genre;gallergy;hanimal;gfood;hfood;hobbies;history;pairgender;name;forename;birthdate;country",
                "female;yes;no;nonuts;nonuts;music;null;female;Smith;Jane;2006-02-02;ES",
                "male;no;yes;vegetarian,nonuts;vegetarian,nonuts;sports;same;male;Ligma;Marc;2003-03-03;FR" };
        csvWriter = new CSVWriter();
        file = "./res/test.csv";
    }

    @Test
    void testExportToCSV() {
        boolean result = false;
        try {
            csvWriter.exportToCSV(data, file);
            result = true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        assertTrue(result);
    }
}
