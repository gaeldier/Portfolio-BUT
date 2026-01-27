package modele;

import java.util.*;

/**
 * Classe représentant un plateau de labyrinthe généré avec un algorithme
 * aléatoire.
 * <p>
 * Cette classe hérite de {@link AbstractPlateau} et ajoute la génération d'un
 * labyrinthe
 * avec un chemin garanti entre l'entrée et la sortie, ainsi que la possibilité
 * d'ajouter
 * des murs aléatoires selon un pourcentage.
 * </p>
 * @author Dawid Banas, Gaël Dierynck, Ylann Wattrelos
 * @version 1.0
 * @see AbstractPlateau
 */
public class PlateauAleatoire extends AbstractPlateau {

    /**
     * Générateur de nombres aléatoires utilisé pour placer les murs et le chemin.
     */
    private final Random rand = new Random();

    /**
     * Constructeur privé.
     * <p>
     * Les dimensions sont automatiquement augmentées de 2 pour inclure les
     * bordures.
     * </p>
     * 
     * @param ligne   nombre de lignes intérieures
     * @param colonne nombre de colonnes intérieures
     */
    private PlateauAleatoire(int ligne, int colonne) {
        ligne += 2;
        colonne += 2;
        plateau = new boolean[ligne][colonne];
    }

    /**
     * Génère un plateau (labyrinthe) avec des murs aléatoires et un chemin garanti
     * entre l'entrée et la sortie.
     * <p>
     * Les bordures sont automatiquement ajoutées, et l'entrée/sortie sont dégagées.
     * </p>
     * 
     * @param ligne       nombre de lignes intérieures
     * @param colonne     nombre de colonnes intérieures
     * @param pourcentage proportion de cases à transformer en murs (entre 0 et 1)
     * @return le plateau généré
     * @throws IllegalArgumentException si le pourcentage n'est pas dans [0,1]
     */
    public static PlateauAleatoire genererLabyrinthe(int ligne, int colonne, double pourcentage) {
        if (pourcentage < 0 || pourcentage > 1)
            throw new IllegalArgumentException("Le pourcentage doit être entre 0 et 1");

        PlateauAleatoire p = new PlateauAleatoire(ligne, colonne);
        p.shuffleEntreeSortie();

        // Bordures
        for (int x = 0; x < p.getNbColonne(); x++) {
            p.setMur(new Cases(x, 0));
            p.setMur(new Cases(x, p.getNbLigne() - 1));
        }
        for (int y = 0; y < p.getNbLigne(); y++) {
            p.setMur(new Cases(0, y));
            p.setMur(new Cases(p.getNbColonne() - 1, y));
        }

        p.enleverMurEntreeSortie();

        // Génération du chemin
        Set<Cases> cheminAleatoire = p.genererCheminAleatoire(p.getNbLigne(), p.getNbColonne());

        // Ajout des murs
        p.placerMur(pourcentage, cheminAleatoire);

        return p;
    }

    /**
     * Génère un chemin aléatoire entre deux cases (entrée et sortie) pour garantir
     * qu'un chemin existe dans le labyrinthe.
     *
     * @param nbLigne   nombre total de lignes du plateau (avec bordures)
     * @param nbColonne nombre total de colonnes du plateau (avec bordures)
     * @return ensemble de cases représentant le chemin
     */
    private Set<Cases> genererCheminAleatoire(int nbLigne, int nbColonne) {
        Set<Cases> chemin = new HashSet<>();
        chemin.add(entree);
        int x = entree.getX();
        int y = entree.getY() + 1;
        chemin.add(new Cases(x, y));

        int securite = nbLigne * nbColonne * 10;

        while ((x != sortie.getX() || y != sortie.getY()) && securite-- > 0) {
            int dx = Integer.compare(sortie.getX(), x);
            int dy = Integer.compare(sortie.getY(), y);

            if (rand.nextDouble() < 0.6) {
                if (rand.nextBoolean())
                    x += dx;
                else
                    y += dy;
            } else {
                switch (rand.nextInt(4)) {
                    case 0 -> x--;
                    case 1 -> x++;
                    case 2 -> y--;
                    case 3 -> y++;
                }
            }

            // Limitation aux bornes intérieures
            if (x < 1)
                x = 1;
            if (y < 1)
                y = 1;
            if (x >= nbColonne - 1)
                x = nbColonne - 2;
            if (y >= nbLigne - 1)
                y = nbLigne - 2;

            chemin.add(new Cases(x, y));
        }

        return chemin;
    }

    /**
     * Place aléatoirement des murs sur le plateau tout en évitant le chemin
     * garanti.
     * 
     * @param pourcentage proportion de cases à transformer en murs
     * @param chemin      ensemble de cases devant rester libres
     */
    public void placerMur(double pourcentage, Set<Cases> chemin) {
        if (pourcentage == 1.0)
            remplirMur(chemin);
        else {
            int nbMur = (int) ((getNbLigne() * getNbColonne()) * pourcentage);
            int nbMurPlace = 0;

            while (nbMurPlace < nbMur) {
                int x = rand.nextInt(1, getNbColonne() - 1);
                int y = rand.nextInt(1, getNbLigne() - 1);

                if (!chemin.contains(new Cases(x, y))) {
                    setMur(new Cases(x, y));
                    nbMurPlace++;
                }
            }
        }
    }

    /**
     * Remplit toutes les cases non incluses dans le chemin avec des murs.
     * 
     * @param chemin ensemble de cases devant rester libres
     */
    private void remplirMur(Set<Cases> chemin) {
        for (int y = 1; y < getNbLigne() - 1; y++) {
            for (int x = 1; x < getNbColonne() - 1; x++) {
                if (!chemin.contains(new Cases(x, y)))
                    setMur(new Cases(x, y));
            }
        }
    }

    /**
     * Mélange la position de l'entrée/sortie aléatoirement selon la position en Y
     * (le x sera toujours 0 pour l'entrée et tailleX-1 pour la sortie)
     */
    private void shuffleEntreeSortie() {
        if (getNbLigne() <= 0 || getNbColonne() <= 0)
            throw new IllegalArgumentException("dimensions invalides.");

        // Cas limite: une seule case disponible
        if (getNbLigne() == 1 && getNbColonne() == 1) {
            throw new IllegalStateException("Ton labyrinthe est trop petit");
        }

        final int entreeLigne = 0;
        final int sortieLigne = getNbLigne() - 1;

        int entreeColonne = new Random().nextInt(1, getNbColonne() - 1);

        int sortieColonne = new Random().nextInt(1, getNbColonne() - 1);

        if (getEntree() == null) {
            this.entree = new Cases(entreeColonne, entreeLigne);
        } else {
            getEntree().setX(entreeColonne);
            getEntree().setY(entreeLigne);
        }
        if (getSortie() == null) {
            this.sortie = new Cases(sortieColonne, sortieLigne);
        } else {
            getSortie().setX(sortieColonne);
            getSortie().setY(sortieLigne);
        }
        this.setDistanceEntreeSortie();
    }

    /**
     * Calcule et stocke la distance minimale entre l'entrée et la sortie du labyrinthe
     * en utilisant une recherche en largeur (BFS).
     */
    private void setDistanceEntreeSortie(){
        List<Cases> casesMarquee = new ArrayList<>();
        Queue<Noeuds> f = new LinkedList<>();

        f.offer(new Noeuds(entree, 0));
        casesMarquee.add(entree);

        while (!f.isEmpty()) {
            Noeuds c = f.poll();
            int dist = c.getDistance();

            if (c.getCase().equals(this.sortie)) {
                this.distanceEntreeSortie = dist;
            }

            for (Cases v : this.voisinsPossibles(c.getCase(), casesMarquee)) {
                casesMarquee.add(v);
                f.offer(new Noeuds(v, dist + 1));
            }
        }
    }
}
