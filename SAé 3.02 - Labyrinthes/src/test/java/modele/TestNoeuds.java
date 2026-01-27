package modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestNoeuds {

    @Test
    void testCreationNoeud() {
        Cases c = new Cases(2, 3);
        Noeuds n = new Noeuds(c, 5);

        assertEquals(c, n.getCase());
        assertEquals(5, n.getDistance());
    }

    @Test
    void testDistanceZero() {
        Cases c = new Cases(0, 0);
        Noeuds n = new Noeuds(c, 0);

        assertEquals(0, n.getDistance());
        assertEquals(c, n.getCase());
    }

    @Test
    void testDistanceNegative() {
        Cases c = new Cases(1, 1);
        Noeuds n = new Noeuds(c, -7);

        assertEquals(-7, n.getDistance());
        assertEquals(c, n.getCase());
    }

    @Test
    void testCaseReferenceIsSame() {
        Cases c = new Cases(4, 4);
        Noeuds n = new Noeuds(c, 10);

        assertSame(c, n.getCase());
    }
}
