package modele;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestCases {

    @Test
    public void testConstructeurEtGetters() {
        Cases c = new Cases(3, 5);
        assertEquals(3, c.getX());
        assertEquals(5, c.getY());
    }

    @Test
    public void testSetters() {
        Cases c = new Cases(0, 0);
        c.setX(10);
        c.setY(20);
        assertEquals(10, c.getX());
        assertEquals(20, c.getY());
    }

    @Test
    public void testEqualsReflexive() {
        Cases c = new Cases(1, 2);
        assertEquals(c, c);
    }

    @Test
    public void testEqualsSymmetric() {
        Cases c1 = new Cases(4, 7);
        Cases c2 = new Cases(4, 7);
        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    public void testEqualsDifferent() {
        Cases c1 = new Cases(1, 2);
        Cases c2 = new Cases(2, 1);
        assertNotEquals(c1, c2);
    }

    @Test
    public void testEqualsFalseDifferentY() {
        Cases c1 = new Cases(1, 1);
        Cases c2 = new Cases(1, 2);
        assertNotEquals(c1, c2);
    }

    @Test
    public void testEqualsFalseNull() {
        Cases c = new Cases(1, 1);
        assertNotEquals(null, c);
    }

    @Test
    public void testEqualsFalseAutreClasse() {
        Cases c = new Cases(1, 1);
        Object autre = new Object();
        assertNotEquals(c, autre);
    }

    @Test
    public void testHashCodeCoherent() {
        Cases c = new Cases(5, 10);
        int hash1 = c.hashCode();
        int hash2 = c.hashCode();
        assertEquals(hash1, hash2);
    }
}
