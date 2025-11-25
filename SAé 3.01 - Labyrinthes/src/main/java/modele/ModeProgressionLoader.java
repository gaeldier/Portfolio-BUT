package modele;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe ModeProgressionLoader est responsable du chargement des données des
 * niveaux
 * pour le mode de jeu "Progression" à partir d'un fichier CSV.
 * Elle lit les configurations de chaque niveau, telles que la taille de la
 * grille,
 * les pourcentages de murs pour différentes difficultés, et si la vue est
 * restreinte.
 * 
 * @author Gaël Dierynck, Ylann Wattrelos
 */
public class ModeProgressionLoader {

    public final String PATH = "src/main/resources/csv/niveau.csv";
    private final List<Integer> niveauxNbLigne = new ArrayList<>();
    private final List<Integer> niveauxNbColonne = new ArrayList<>();
    private final List<Integer> niveauxFacile = new ArrayList<>();
    private final List<Integer> niveauxMoyen = new ArrayList<>();
    private final List<Integer> niveauxDifficile = new ArrayList<>();

    public ModeProgressionLoader() {
        List<String[]> donnee = ToolBox.recupAllPseudo(PATH);
        for (String[] s : donnee)
            this.rangerDonnee(s);
    }

    private void rangerDonnee(String[] donneeNiveau) {
        this.niveauxNbLigne.add(Integer.parseInt(donneeNiveau[0]));
        this.niveauxNbColonne.add(Integer.parseInt(donneeNiveau[1]));
        this.niveauxFacile.add(Integer.parseInt(donneeNiveau[2]));
        this.niveauxMoyen.add(Integer.parseInt(donneeNiveau[3]));
        this.niveauxDifficile.add(Integer.parseInt(donneeNiveau[4]));

    }

    public int getNbNiveau() {
        return this.niveauxNbColonne.size();
    }

    public int getNbLignes(int niveau) {
        return this.niveauxNbLigne.get(niveau - 1);
    }

    public int getNbColonnes(int niveau) {
        return this.niveauxNbColonne.get(niveau - 1);
    }

    public int getDifficulte(int niveau, String difficulte) {
        return switch (difficulte) {
            case "Facile" -> this.niveauxFacile.get(niveau - 1);
            case "Moyen" -> this.niveauxMoyen.get(niveau - 1);
            case "Difficile" -> this.niveauxDifficile.get(niveau - 1);
            default -> 0;
        };
    }
}