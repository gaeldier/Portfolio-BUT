package modele;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class TestLabyrinthe {

    private Labyrinthe labAleatoire;
    private Labyrinthe labParfait;
    private Labyrinthe labBinary;

    @BeforeEach
    void setUp() {
        labAleatoire = new Labyrinthe(5, 5, 0.2);
        labParfait = new Labyrinthe(5, 5, 5);
        labBinary = new Labyrinthe(5, 5, 5, Orientation.NORD_EST);
    }

    @Test
    void testLabyrintheAleatoireCreation() {
        assertNotNull(labAleatoire.getPlateau());
        assertEquals(labAleatoire.getEntree(), labAleatoire.getPosJoueur());
    }

    @Test
    void testLabyrintheParfaitCreation() {
        assertNotNull(labParfait.getPlateau());
        assertEquals(labParfait.getEntree(), labParfait.getPosJoueur());
    }

    @Test
    void testLabyrintheBinaryTreeCreation() {
        assertNotNull(labBinary.getPlateau());
        assertEquals(labBinary.getEntree(), labBinary.getPosJoueur());
    }

    @Test
    void testDeplacementValide() {
        Labyrinthe lab = new Labyrinthe(5, 5, 0);
        Cases pos = lab.getPosJoueur();

        // déplacement vers EST si pas de mur
        Cases next = new Cases(pos.getX() + 1, pos.getY());
        if (!lab.estMur(next)) {
            lab.deplacementJoueur(Direction.EST);
            assertEquals(next, lab.getPosJoueur());
            assertEquals(pos, lab.getOldPosJoueur());
        }
    }

    @Test
    void testDeplacementMur() {
        Labyrinthe lab = new Labyrinthe(5, 5, 1.0); // tout est mur sauf chemin
        Cases pos = lab.getPosJoueur();

        // essayer de forcer un déplacement dans un mur
        Cases mur = new Cases(pos.getX() + 1, pos.getY());
        if (lab.estMur(mur)) {
            WrongDeplacementException ex = assertThrows(WrongDeplacementException.class,
                    () -> lab.deplacementJoueur(Direction.EST));
            assertTrue(ex.getMessage().contains("mur détecté"));
        }
    }

    @Test
    void testEstBordure() {
        Labyrinthe lab = new Labyrinthe(5, 5, 0);
        Cases bordure = new Cases(0, 0);
        Cases entree = lab.getEntree();
        Cases sortie = lab.getSortie();
        Cases joueur = lab.getPosJoueur();

        assertFalse(lab.estBordure(entree));
        assertFalse(lab.estBordure(sortie));
        assertFalse(lab.estBordure(joueur));
        assertTrue(lab.estBordure(bordure));
    }
}
