package modele;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlateauBinaryTree {

    @Test
    public void testDimensionsImpairesAvecBordures() {
        PlateauBinaryTree p = PlateauBinaryTree.genererLabyrinthe(6, 10, 5, Orientation.NORD_EST);
        assertEquals(9, p.getNbLigne());
        assertEquals(13, p.getNbColonne());
    }

    @Test
    public void testBorduresSontDesMurs() {
        PlateauBinaryTree p = PlateauBinaryTree.genererLabyrinthe(9, 9, 6, Orientation.NORD_OUEST);

        for (int x = 0; x < p.getNbColonne(); x++) {
            assertTrue(p.estMur(new Cases(x, 0)));
            assertTrue(p.estMur(new Cases(x, p.getNbLigne() - 1)));
        }

        for (int y = 0; y < p.getNbLigne(); y++) {
            assertTrue(p.estMur(new Cases(0, y)));
            assertTrue(p.estMur(new Cases(p.getNbColonne() - 1, y)));
        }
    }

    @Test
    public void testEntreeSortieLibres() {
        PlateauBinaryTree p = PlateauBinaryTree.genererLabyrinthe(9, 9, 5, Orientation.NORD_EST);

        assertFalse(p.estMur(p.getEntree()));
        assertFalse(p.estMur(p.getSortie()));
    }
}
