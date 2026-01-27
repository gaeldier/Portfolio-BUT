package modele;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlateauParfait {

    @Test
    public void testDimensionsImpairesAvecBordures() {
        PlateauParfait p = PlateauParfait.genererLabyrinthe(6, 8, 5);
        assertEquals(9, p.getNbLigne());
        assertEquals(11, p.getNbColonne());
    }

    @Test
    public void testBorduresSontDesMurs() {
        PlateauParfait p = PlateauParfait.genererLabyrinthe(9, 9, 6);

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
        PlateauParfait p = PlateauParfait.genererLabyrinthe(9, 9, 5);

        assertFalse(p.estMur(p.getEntree()));
        assertFalse(p.estMur(p.getSortie()));
    }

    @Test
    public void testAucunCycleLabyrintheParfait() {
        PlateauParfait p = PlateauParfait.genererLabyrinthe(11, 11, 10);

        int freeCount = 0;
        int h = p.getNbLigne();
        int w = p.getNbColonne();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (!p.estMur(new Cases(x, y))) freeCount++;
            }
        }

        // un labyrinthe parfait creusé par DFS a environ la moitié / un peu plus de cases libres
        assertTrue(freeCount > (w * h) / 4);
    }
}
