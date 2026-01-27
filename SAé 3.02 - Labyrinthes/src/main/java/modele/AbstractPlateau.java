package modele;

import java.util.*;

/**
 * Classe abstraite représentant un plateau de labyrinthe.
 * <p>
 * Cette classe contient le code commun pour la gestion des murs et des cases
 * d'un plateau.
 * Les classes spécifiques de labyrinthe (ex. {@link PlateauAleatoire},
 * {@link PlateauParfait})
 * hériteront de cette classe pour implémenter leurs propres algorithmes de
 * génération.
 * </p>
 * 
 * <p>
 * L'état du plateau est stocké dans un tableau 2D de booléens :
 * <ul>
 * <li>true : case mur</li>
 * <li>false : case libre</li>
 * </ul>
 * </p>
 * @author Dawid Banas, Gaël Dierynck, Ylann Wattrelos
 */
public abstract class AbstractPlateau {

    /**
     * Tableau représentant le plateau de labyrinthe.
     * Chaque case est true si c'est un mur, false sinon.
     */
    protected boolean[][] plateau;
    protected Cases entree;
    protected Cases sortie;
    protected int distanceEntreeSortie;

    /**
     * Retourne le nombre de lignes du plateau.
     * 
     * @return nombre total de lignes
     */
    public int getNbLigne() {
        return plateau.length;
    }

    /**
     * Retourne le nombre de colonnes du plateau.
     * 
     * @return nombre total de colonnes
     */
    public int getNbColonne() {
        return plateau[0].length;
    }

    public int getDistanceEntreeSortie() {
        return this.distanceEntreeSortie;
    }

    public Cases getEntree() {
        return entree;
    }

    public Cases getSortie() {
        return sortie;
    }

    public boolean estEntree(Cases c) {
        return this.entree.equals(c);
    }

    public boolean estSortie(Cases c) {
        return this.sortie.equals(c);
    }

    /**
     * Vérifie si une case est un mur.
     * <p>
     * Les cases hors limites sont considérées comme des murs.
     * </p>
     * 
     * @param c la case à tester
     * @return true si la case est un mur ou hors limites, false sinon
     */
    public boolean estMur(Cases c) {
        return notInBounds(c) || plateau[c.getY()][c.getX()];
    }

    /**
     * Place un mur sur une case spécifique si elle est dans les limites.
     * 
     * @param c la case où placer le mur
     */
    public void setMur(Cases c) {
        if (!notInBounds(c))
            plateau[c.getY()][c.getX()] = true;
    }

    /**
     * Retire un mur sur une case spécifique si elle est dans les limites.
     *
     */
    public void enleverMurEntreeSortie() {
        this.plateau[entree.getY()][entree.getX()] = false;
        this.plateau[sortie.getY()][sortie.getX()] = false;
    }

    /**
     * Réinitialise toutes les cases intérieures du plateau en retirant les murs.
     * <p>
     * Les bordures (première et dernière ligne/colonne) restent inchangées.
     * </p>
     */
    public void resetPlateau() {
        for (int y = 1; y < getNbLigne() - 1; y++)
            for (int x = 1; x < getNbColonne() - 1; x++)
                plateau[y][x] = false;
    }

    /**
     * Vérifie si une case est hors des limites du plateau.
     * <p>
     * Cette méthode est utilisée en interne pour éviter les erreurs d'accès au
     * tableau.
     * </p>
     * 
     * @param c la case à tester
     * @return true si la case est hors limites ou null, false sinon
     */
    protected boolean notInBounds(Cases c) {
        if (c == null)
            return true;
        int x = c.getX(), y = c.getY();
        return y < 0 || y >= getNbLigne() || x < 0 || x >= getNbColonne();
    }

    protected void placerEntree() {
        Random r = new Random();
        int x = r.nextInt(1, getNbColonne() - 1);
        int y = r.nextInt(1, getNbLigne() - 1);
        if (x % 2 == 0)
            x++;
        if (y % 2 == 0)
            y++;
        this.entree = new Cases(x, y);
    }

    /**
     * Compte le nombre de cases pour arriver jusqu'au paramètre souhaité et marquer
     * la case comme sortie
     * <p>
     * Cette methode sert à calculer un score pour les chaque niveau
     * </p>
     *
     * @param distanceMin la case qui sera jugée "sortie" du labyrinthe
     */
    protected void placerSortie(int distanceMin) {
        List<Cases> casesMarquee = new ArrayList<>();
        Queue<Noeuds> f = new LinkedList<>();

        Cases caseLaPlusLoin = entree;
        int distanceMaxTrouvee = 0;

        f.offer(new Noeuds(entree, 0)); // distance initiale 0
        casesMarquee.add(entree);
        if (distanceMin < (((getNbLigne() - 3) * (getNbColonne() - 3)) / 4))
            distanceMin += ((getNbLigne() - 3) * (getNbColonne() - 3)) / 10;

        while (!f.isEmpty()) {
            Noeuds c = f.poll();
            int dist = c.getDistance();

            if (dist > distanceMaxTrouvee) {
                caseLaPlusLoin = c.getCase();
                distanceMaxTrouvee = dist;
            }

            if (dist == distanceMin) {
                this.sortie = c.getCase();
                this.distanceEntreeSortie = dist;
            }

            for (Cases v : this.voisinsPossibles(c.getCase(), casesMarquee)) {
                casesMarquee.add(v);
                f.offer(new Noeuds(v, dist + 1));
            }
        }
        if (sortie == null)
            sortie = caseLaPlusLoin;
    }

    /**
     * Retourne les voisins accessibles d'une case donnée qui n'ont pas encore été
     * marqués.
     * 
     * @param c la case de la quelle on veut trouver les voisins
     * @param casesMarquee les cases qu'on a ou qu'on vas deja visiter
     * @return la liste des cases voisine a @c et qui n'on pas etaient marquée
     */
    protected List<Cases> voisinsPossibles(Cases c, List<Cases> casesMarquee) {
        int currentx = c.getX();
        int currenty = c.getY();
        List<Cases> voisins = new ArrayList<>();

        int[][] directions = {
                { -1, 0 }, // haut
                { 1, 0 }, // bas
                { 0, -1 }, // gauche
                { 0, 1 } // droite
        };

        for (int[] d : directions) {
            int newx = currentx + d[0];
            int newy = currenty + d[1];
            if (newx >= 0 && newy >= 0 && newx < this.getNbColonne() && newy < this.getNbLigne()
                    && !this.estMur(new Cases(newx, newy)) && !this.caseMarquer(casesMarquee, new Cases(newx, newy))) {
                voisins.add(new Cases(newx, newy));
            }
        }

        return voisins;
    }

    protected boolean caseMarquer(List<Cases> casesMarquee, Cases c) {
        return casesMarquee.contains(c);
    }
}
