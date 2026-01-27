package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestNiveau {

    @BeforeEach
    void setUp() {
        Niveau.resetCounter();
    }

    @Test
    void testNumNiveauAutoIncrement() {
        Niveau n1 = new Niveau();
        Niveau n2 = new Niveau();
        Niveau n3 = new Niveau();

        assertEquals(1, n1.getNumNiveau());
        assertEquals(2, n2.getNumNiveau());
        assertEquals(3, n3.getNumNiveau());
    }

    @Test
    void testPeutEtreFaisInitial() {
        Niveau n1 = new Niveau();
        Niveau n2 = new Niveau();

        assertTrue(n1.peutEtreFais());
        assertFalse(n2.peutEtreFais());
    }

    @Test
    void testDifficulteFaiteIndividuelle() {
        Niveau n = new Niveau();

        // par défaut toutes false
        assertFalse(n.isFacileFait());
        assertFalse(n.isMoyenFait());
        assertFalse(n.isDifficileFait());

        n.setDifficulteFaite(0, true);
        assertTrue(n.isFacileFait());
        assertFalse(n.isMoyenFait());
        assertFalse(n.isDifficileFait());

        n.setDifficulteFaite(1, true);
        assertTrue(n.isMoyenFait());

        n.setDifficulteFaite(2, true);
        assertTrue(n.isDifficileFait());
    }

    @Test
    void testDifficulteFaiteParNom() {
        Niveau n = new Niveau();

        n.setDifficulteeFaite("Facile");
        assertTrue(n.isFacileFait());
        assertFalse(n.isMoyenFait());
        assertFalse(n.isDifficileFait());

        n.setDifficulteeFaite("Moyen");
        assertTrue(n.isMoyenFait());

        n.setDifficulteeFaite("Difficile");
        assertTrue(n.isDifficileFait());
    }

    @Test
    void testEstFait() {
        Niveau n = new Niveau();
        assertFalse(n.estFait());

        n.setDifficulteFaite(0, true);
        assertTrue(n.estFait());

        Niveau n2 = new Niveau();
        n2.setDifficulteFaite(1, true);
        assertTrue(n2.estFait());

        Niveau n3 = new Niveau();
        n3.setDifficulteFaite(2, true);
        assertTrue(n3.estFait());
    }

    @Test
    void testPeutEtreFaisSetter() {
        Niveau n = new Niveau();
        n.setPeutEtreFait(false);
        assertFalse(n.peutEtreFais());

        n.setPeutEtreFait(true);
        assertTrue(n.peutEtreFais());
    }

    @Test
    void testScoreNiveau() {
        Niveau n = new Niveau();
        assertEquals(0, n.getScoreNiveau());

        n.setScoreNiveau(10, "Facile");
        assertEquals(10, n.getScoreNiveau());

        n.setScoreNiveau(20, "Moyen");
        assertEquals(30, n.getScoreNiveau());

        n.setScoreNiveau(5, "Difficile");
        assertEquals(35, n.getScoreNiveau());
    }

    @Test
    void testMultipleNiveauxScores() {
        Niveau n1 = new Niveau();
        Niveau n2 = new Niveau();

        n1.setScoreNiveau(10, "Facile");
        n2.setScoreNiveau(15, "Facile");

        assertEquals(10, n1.getScoreNiveau());
        assertEquals(15, n2.getScoreNiveau());
    }

    @Test
    void testDifficulteIndexOutOfBounds() {
        Niveau n = new Niveau();

        // vérification des exceptions IndexOutOfBounds pour get
        assertThrows(IndexOutOfBoundsException.class, () -> n.getDifficulteFaite(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> n.getDifficulteFaite(3));

        // vérification des exceptions IndexOutOfBounds pour set
        assertThrows(IndexOutOfBoundsException.class, () -> n.setDifficulteFaite(-1, true));
        assertThrows(IndexOutOfBoundsException.class, () -> n.setDifficulteFaite(3, true));
    }
}
