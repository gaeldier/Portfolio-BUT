package modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlateauAleatoire {

    @Test
    public void testGenerationDimensions() {
        PlateauAleatoire p = PlateauAleatoire.genererLabyrinthe(5, 7, 0.2);
        assertEquals(7, p.getNbLigne());
        assertEquals(9, p.getNbColonne());
    }

    @Test
    public void testEntreeSortieDegagees() {
        PlateauAleatoire p = PlateauAleatoire.genererLabyrinthe(6, 6, 0.4);
        assertFalse(p.estMur(p.getEntree()), "L'entrée doit être libre");
        assertFalse(p.estMur(p.getSortie()), "La sortie doit être libre");
    }

    @Test
    public void testBorduresSontDesMurs() {
        PlateauAleatoire p = PlateauAleatoire.genererLabyrinthe(4, 4, 0.1);

        for (int x = 0; x < p.getNbColonne(); x++) {
            Cases haut = new Cases(x, 0);
            Cases bas = new Cases(x, p.getNbLigne() - 1);

            if (!p.estEntree(haut) && !p.estSortie(haut))
                assertTrue(p.estMur(haut));

            if (!p.estEntree(bas) && !p.estSortie(bas))
                assertTrue(p.estMur(bas));
        }

        for (int y = 0; y < p.getNbLigne(); y++) {
            Cases gauche = new Cases(0, y);
            Cases droite = new Cases(p.getNbColonne() - 1, y);

            if (!p.estEntree(gauche) && !p.estSortie(gauche))
                assertTrue(p.estMur(gauche));

            if (!p.estEntree(droite) && !p.estSortie(droite))
                assertTrue(p.estMur(droite));
        }
    }
}
