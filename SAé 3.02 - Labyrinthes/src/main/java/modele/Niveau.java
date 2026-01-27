package modele;

import java.io.Serial;
import java.io.Serializable;

/**
 * Représente un niveau de jeu avec identifiant auto-incrémenté et suivi de
 * l'état
 * et du temps réalisé pour trois difficultés : 0 = facile, 1 = moyen, 2 =
 * difficile.
 * Chaque instance reçoit un numéro unique basé sur le compteur statique
 * {@code counter}
 * (démarrant à 1). Les tableaux {@code difficulteFaite} et {@code tempsFait}
 * contiennent
 * respectivement l'état (true si réalisé) et le temps enregistré pour chaque
 * difficulté.
 * Les méthodes d'accès et de modification vérifient les indices des difficultés
 * implicitement via l'accès au tableau (peuvent lancer
 * {@link IndexOutOfBoundsException}
 * si l'indice n'est pas dans [0,2]).
 * Non thread-safe.
 * Méthodes importantes :
 * - getNumNiveau() : retourne le numéro du niveau.
 * - getDifficulteFaite(int) / setDifficulteFaite(int, boolean) :
 * lecture/écriture de l'état.
 * - getTempsFait(int) / setTempsFait(int, double) : lecture/écriture des temps.
 * - isFacileFait(), isMoyenFais(), isDifficileFait() : raccourcis pour chaque
 * difficulté.
 * - resetCounter() : réinitialise le compteur statique à 1 (utile pour les
 * tests).
 * 
 * @author Gaël Dierynck, Ylann Wattrelos
 */
public class Niveau implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int num;
    private static int counter = 1;
    private boolean peutEtreFait;
    @SuppressWarnings("FieldMayBeFinal")
    private boolean[] difficulteFaite;
    private final int[] scoreDifficulte;

    public Niveau() {
        this.num = Niveau.counter;
        Niveau.counter++;
        this.difficulteFaite = new boolean[] { false, false, false };
        this.scoreDifficulte = new int[] { 0, 0, 0 };
        this.peutEtreFait = this.num == 1;
    }

    public int getNumNiveau() {
        return this.num;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean getDifficulteFaite(int difficulte) {
        return this.difficulteFaite[difficulte];
    }

    public void setDifficulteFaite(int difficulte, boolean fait) {
        this.difficulteFaite[difficulte] = fait;
    }

    public boolean isFacileFait() {
        return this.difficulteFaite[0];
    }

    public boolean isMoyenFait() {
        return this.difficulteFaite[1];
    }

    public boolean isDifficileFait() {
        return this.difficulteFaite[2];
    }

    public static void resetCounter() {
        Niveau.counter = 1;
    }

    public void setDifficulteeFaite(String difficultee) {
        switch (difficultee) {
            case "Facile":
                this.difficulteFaite[0] = true;
                break;
            case "Moyen":
                this.difficulteFaite[1] = true;
                break;
            case "Difficile":
                this.difficulteFaite[2] = true;
                break;
        }
    }

    public boolean estFait() {
        return this.difficulteFaite[0] || this.difficulteFaite[1] || this.difficulteFaite[2];
    }

    public boolean peutEtreFais() {
        return this.peutEtreFait;
    }

    public void setPeutEtreFait(Boolean b) {
        this.peutEtreFait = b;
    }

    public void setScoreNiveau(int score, String difficulte) {
        switch (difficulte) {
            case "Facile":
                this.scoreDifficulte[0] = score;
                break;
            case "Moyen":
                this.scoreDifficulte[1] = score;
                break;
            case "Difficile":
                this.scoreDifficulte[2] = score;
                break;
        }
    }

    public int getScoreNiveau() {
        return this.scoreDifficulte[0] + this.scoreDifficulte[1] + this.scoreDifficulte[2];
    }
}
