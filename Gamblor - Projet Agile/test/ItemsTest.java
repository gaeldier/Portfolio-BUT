import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ItemsTest {
    Items coffee = Items.COFFEE;
    Items snoop = Items.SNOOP;

    @Test 
    public void valueTest() {
        assertEquals(coffee.maxuser, 5);
        assertEquals(snoop.maxshop, 1);
        assertEquals(coffee.id, 2);
    }
}
