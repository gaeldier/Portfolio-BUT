package modele;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe représentant le joueur dans le labyrinthe
 *
 * @author Gaël Dierynck
 * @version 1.0
 * @since 1.0
 */
public class Joueur implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String nom;
    private int score;
    @SuppressWarnings("FieldMayBeFinal")
    private List<Niveau> niveaux;

    public Joueur(String n, int sc, int nbNiveau) {
        Niveau.resetCounter();
        this.nom = n;
        this.score = sc;
        this.niveaux = new ArrayList<>();
        for (int i = 0; i < nbNiveau; i++) {
            this.niveaux.add(new Niveau());
        }
    }

    public String getNom() {
        return nom;
    }

    private void setNewScore() {
        this.score = 0;
        for (Niveau n : this.niveaux) {
            this.score += n.getScoreNiveau();
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int niveau, int sc, String difficulte) {
        this.niveaux.get(niveau - 1).setScoreNiveau(sc, difficulte);
        this.setNewScore();
    }

    public void setNom(String name) {
        this.nom = name;
    }

    public List<Niveau> getlistNiveau() {
        return this.niveaux;
    }

    /**
     * Indique qu'un niveau a été effectué et met à jour le niveau suivant
     * pour qu'il puisse être fait.
     * 
     * @param niveau      le numéro du niveau effectué
     * @param difficultee la difficulté avec laquelle le niveau a été effectué
     */
    public void niveauEffectue(int niveau, String difficultee) {
        this.niveaux.get(niveau - 1).setDifficulteeFaite(difficultee);
        if (niveau < this.niveaux.size())
            this.niveaux.get(niveau).setPeutEtreFait(true);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Joueur joueur = (Joueur) o;
        return Objects.equals(nom, joueur.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nom);
    }
}