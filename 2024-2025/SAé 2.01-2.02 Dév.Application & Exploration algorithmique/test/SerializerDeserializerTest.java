import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SerializerDeserializerTest {
    AdolescentsArray aExporter;
    AdolescentsArray aImporter;
    AdolescentsArray tmp;
    public String filepath ="./test/resTest/config.class";

    @BeforeEach
    public void init() {
        ArrayList<Adolescents> listAdolescents = new ArrayList<>();
        Appariement ap = new Appariement(listAdolescents);
        Adolescents adoH1 = new Adolescents("male", "no", "yes", "nonuts", "nonuts", "sports", "other", "male",
                "Vanhoutte", "Amaury", "2005-01-01", "FR");
        Adolescents adoG1 = new Adolescents("female", "no", "no", "nonuts", "nonuts", "music", "other", "female",
                "Dierynck", "Gaël", "2006-02-02", "GE");
        Adolescents adoH2 = new Adolescents("male", "no", "no", "nonuts", "nonuts", "sports", "other", "male",
                "Harlaut", "Romain", "2005-01-01", "FR");
        Adolescents adoH5 = new Adolescents("male", "no", "no", "nonuts", "nonuts", "sports", "other", "male",
                "Sanders", "Colonel", "2005-01-01", "FR");
        listAdolescents.add(adoG1);
        listAdolescents.add(adoH1);
        listAdolescents.add(adoH2);
        listAdolescents.add(adoH5);
        ap = new Appariement(listAdolescents);
        try {
            AdolescentsArray aExporter = new AdolescentsArray(ap.getHashMapPair());
            System.out.println(aExporter.getMap());
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void SerializerTest() {
        Serializer.serialize(aExporter, filepath);
        File f = new File(filepath);
        boolean isPresent = f.exists();
        assertTrue(isPresent);
    }

    @Test
    public void DeSerializerTest() {
        tmp = DeSerializer.DeSerialize(filepath);
        assertEquals(tmp, aExporter);
    }
}
