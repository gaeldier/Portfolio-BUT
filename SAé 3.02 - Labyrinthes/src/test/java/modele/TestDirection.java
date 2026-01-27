package modele;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TestDirection {

    @Test
    public void testGetValue() {
        assertEquals("haut", Direction.NORD.getValue());
        assertEquals("bas", Direction.SUD.getValue());
        assertEquals("droite", Direction.EST.getValue());
        assertEquals("gauche", Direction.OUEST.getValue());
    }

    @Test
    public void testEnumValues() {
        Direction[] directions = Direction.values();
        assertEquals(4, directions.length);
        assertArrayEquals(
                new Direction[] { Direction.NORD, Direction.SUD, Direction.EST, Direction.OUEST },
                directions);
    }

    @Test
    public void testValueOf() {
        assertEquals(Direction.NORD, Direction.valueOf("NORD"));
        assertEquals(Direction.SUD, Direction.valueOf("SUD"));
        assertEquals(Direction.EST, Direction.valueOf("EST"));
        assertEquals(Direction.OUEST, Direction.valueOf("OUEST"));
    }

    @Test
    public void testToString() {
        // par défaut, toString() renvoie le nom de la constante
        assertEquals("NORD", Direction.NORD.toString());
    }
}
