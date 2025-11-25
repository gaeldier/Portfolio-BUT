package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestJoueur {

    private Joueur joueur;

    @BeforeEach
    void setUp() {
        joueur = new Joueur("alberto", 0, 3); // 3 niveaux
    }

    @Test
    void testCreationJoueur() {
        assertEquals("alberto", joueur.getNom());
        assertEquals(0, joueur.getScore());

        List<Niveau> niveaux = joueur.getlistNiveau();
        assertEquals(3, niveaux.size());

        // le premier niveau peut être fait, les autres non
        assertTrue(niveaux.get(0).peutEtreFais());
        assertFalse(niveaux.get(1).peutEtreFais());
        assertFalse(niveaux.get(2).peutEtreFais());

        // numéros corrects
        assertEquals(1, niveaux.get(0).getNumNiveau());
        assertEquals(2, niveaux.get(1).getNumNiveau());
        assertEquals(3, niveaux.get(2).getNumNiveau());
    }

    @Test
    void testNiveauEffectuerDebloqueSuivant() {
        joueur.niveauEffectue(1, "Facile");

        Niveau n1 = joueur.getlistNiveau().get(0);
        Niveau n2 = joueur.getlistNiveau().get(1);

        assertTrue(n1.isFacileFait());
        assertTrue(n2.peutEtreFais()); // le niveau 2 doit être débloqué
    }

    @Test
    void testScoreMiseAJour() {
        joueur.setScore(1, 50, "Facile");
        joueur.setScore(2, 30, "Moyen");

        assertEquals(80, joueur.getScore());

        // vérifie que les scores sont bien dans les bons niveaux
        assertEquals(50, joueur.getlistNiveau().get(0).getScoreNiveau());
        assertEquals(30, joueur.getlistNiveau().get(1).getScoreNiveau());
    }

    @Test
    void testScoreAdditionDifficultes() {
        // même niveau, différentes difficultés
        joueur.setScore(1, 40, "Facile");
        joueur.setScore(1, 20, "Moyen");
        joueur.setScore(1, 10, "Difficile");

        assertEquals(40 + 20 + 10, joueur.getlistNiveau().get(0).getScoreNiveau());
        assertEquals(70, joueur.getScore());
    }

    @Test
    void testNiveauEffectuerNeDebloqueRienPourDernier() {
        joueur.niveauEffectue(3, "Moyen");

        // Rien après le dernier niveau → pas d’erreur, ni crash
        assertTrue(joueur.getlistNiveau().get(2).isMoyenFait());
    }

    @Test
    void testChangementNom() {
        joueur.setNom("Bob");
        assertEquals("Bob", joueur.getNom());
    }
}
