package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestAbstractPlateau {

    // implémentation minimale du plateau
    static class PlateauTest extends AbstractPlateau {
        public PlateauTest(int lignes, int colonnes) {
            plateau = new boolean[lignes][colonnes];
        }

        @Override
        protected void placerSortie(int distanceMin) {
            super.placerSortie(distanceMin);
        }
    }

    private AbstractPlateau plateau;

    @BeforeEach
    void setUp() {
        plateau = new PlateauTest(5, 5);
        plateau.placerEntree();
    }

    @Test
    void testGetNbLigneEtColonne() {
        assertEquals(5, plateau.getNbLigne());
        assertEquals(5, plateau.getNbColonne());
    }

    @Test
    void testEstMur() {
        plateau = new PlateauTest(5, 5);

        assertTrue(plateau.estMur(new Cases(-1, 2)));
        assertTrue(plateau.estMur(new Cases(2, -1)));
        assertTrue(plateau.estMur(new Cases(5, 1)));
        assertTrue(plateau.estMur(new Cases(1, 5)));

        plateau.plateau[2][2] = false;
        assertFalse(plateau.estMur(new Cases(2, 2)));

        plateau.plateau[3][1] = true;
        assertTrue(plateau.estMur(new Cases(1, 3)));
    }

    @Test
    void testSetMur() {
        Cases c = new Cases(2, 2);
        plateau.setMur(c);
        assertTrue(plateau.plateau[2][2]);
    }

    @Test
    void testEnleverMurEntreeSortie() {
        plateau.placerSortie(4);
        plateau.plateau[plateau.getEntree().getY()][plateau.getEntree().getX()] = true;
        plateau.plateau[plateau.getSortie().getY()][plateau.getSortie().getX()] = true;

        plateau.enleverMurEntreeSortie();
        assertFalse(plateau.plateau[plateau.getEntree().getY()][plateau.getEntree().getX()]);
        assertFalse(plateau.plateau[plateau.getSortie().getY()][plateau.getSortie().getX()]);
    }

    @Test
    void testResetPlateau() {
        // mettre des murs partout sauf bordures
        for (int y = 1; y < plateau.getNbLigne() - 1; y++)
            for (int x = 1; x < plateau.getNbColonne() - 1; x++)
                plateau.plateau[y][x] = true;

        plateau.resetPlateau();

        for (int y = 1; y < plateau.getNbLigne() - 1; y++)
            for (int x = 1; x < plateau.getNbColonne() - 1; x++)
                assertFalse(plateau.plateau[y][x]);
    }

    @Test
    void testEstEntreeEtSortie() {
        plateau.placerSortie(4);
        assertTrue(plateau.estEntree(plateau.getEntree()));
        assertTrue(plateau.estSortie(plateau.getSortie()));
    }

    @Test
    void testPlacerSortieDistance() {
        plateau.placerSortie(4);
        assertNotNull(plateau.getSortie());
        assertTrue(plateau.getDistanceEntreeSortie() >= 4);
    }
}
