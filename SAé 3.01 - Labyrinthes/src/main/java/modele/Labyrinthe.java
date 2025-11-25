package modele;

import java.util.*;

import vue.Observers;

/**
 * Représente la structure et l'état d'un labyrinthe.
 * Cette classe abstraite gère le plateau de jeu, la position du joueur,
 * ainsi que les points d'entrée et de sortie.
 * Elle fournit les mécanismes de base pour la création et l'interaction avec le
 * labyrinthe.
 * 
 * @see PlateauAleatoire
 * @see Cases
 * @author Gaël Dierynck, Dawid Banas, Ylann Wattrelos
 * @version 1.1
 */
public class Labyrinthe {
    private AbstractPlateau plateau;
    private Cases posJoueur;
    private Cases oldPosJoueur;
    protected List<Observers> observers = new ArrayList<>();

    /**
     * Constructeur pour le mode libre & autre d'un labyrinthe aléatoire
     * 
     * @param nbLigne   Nombre de lignes
     * @param nbColonne Nombre de colonnes
     * @param pMur      Pourcentage de murs
     */
    public Labyrinthe(int nbLigne, int nbColonne, double pMur) {
        this.plateau = PlateauAleatoire.genererLabyrinthe(nbLigne, nbColonne, pMur);
        this.posJoueur = this.plateau.getEntree();
    }

    public Labyrinthe(int nbLigne, int nbColonne, int distanceMin) {
        this.plateau = PlateauParfait.genererLabyrinthe(nbLigne, nbColonne, distanceMin);
        this.posJoueur = this.plateau.getEntree();
    }

    public Labyrinthe(int nbLigne, int nbColonne, int distanceMin, Orientation orientation) {
        this.plateau = PlateauBinaryTree.genererLabyrinthe(nbLigne, nbColonne, distanceMin, orientation);
        this.posJoueur = this.plateau.getEntree();
    }

    public void addObserver(Observers o) {
        this.observers.add(o);
    }

    @SuppressWarnings("unused")
    public void removeObserver(Observers o) {
        this.observers.remove(o);
    }

    protected void notifyObservers() {
        for (Observers o : observers) {
            o.update();
        }
    }

    // Si cette méthode de création est appelée, le labyrinthe sera toujours
    // aléatoire
    protected void creerLabyrintheAleatoire(int nbLigne, int nbColonne, double difficulte) {
        this.plateau = PlateauAleatoire.genererLabyrinthe(nbLigne, nbColonne, difficulte);
        this.posJoueur = this.plateau.getEntree();
    }

    // Si cette méthode de création est appelée, le labyrinthe sera toujours parfait
    // avec l'algorithme BFS
    protected void creerLabyrintheParfait(int nbLigne, int nbColonne, int distanceMin) {
        this.plateau = PlateauParfait.genererLabyrinthe(nbLigne, nbColonne, distanceMin);
        this.posJoueur = this.plateau.getEntree();
    }

    // Si cette méthode de création est appelée, le labyrinthe sera toujours parfait
    // avec l'algorithme binary tree
    protected void creerLabyrintheBinaryTree(int nbLigne, int nbColonne, int distanceMin,
            @SuppressWarnings("SameParameterValue") Orientation orientation) {
        this.plateau = PlateauBinaryTree.genererLabyrinthe(nbLigne, nbColonne, distanceMin, orientation);
        this.posJoueur = this.plateau.getEntree();
    }

    /**
     * Permet de prédire la position future du joueur, pour vérifier la validité du
     * déplacement (Il ne faudrait pas qu'il tombe dans les backrooms)
     * 
     * @param direction La direction dans laquelle le joueur souhaite se déplacer
     * 
     * @throws WrongDeplacementException Si le déplacement est invalide (dans un
     *                                   mur) //Exception Runtime
     * 
     * @see WrongDeplacementException
     */
    public void deplacementJoueur(Direction direction) {
        this.oldPosJoueur = this.posJoueur;
        // On clone la position actuelle du joueur
        Cases posAnnoncee = new Cases(this.posJoueur.getX(), this.posJoueur.getY());
        switch (direction) {
            case NORD:
                posAnnoncee.setY(this.posJoueur.getY() - 1);
                break;
            case SUD:
                posAnnoncee.setY(this.posJoueur.getY() + 1);
                break;
            case EST:
                posAnnoncee.setX(this.posJoueur.getX() + 1);
                break;
            case OUEST:
                posAnnoncee.setX(this.posJoueur.getX() - 1);
                break;
        }
        if (this.estMur(posAnnoncee)) {
            String msg = "Déplacement impossible : mur détecté à la position (" + posAnnoncee.getY() + ", "
                    + posAnnoncee.getX() + ")";
            throw new WrongDeplacementException(msg);
        } else {
            this.posJoueur = posAnnoncee;
            notifyObservers();
        }
    }

    public AbstractPlateau getPlateau() {
        return plateau;
    }

    public Cases getPosJoueur() {
        return posJoueur;
    }

    public Cases getEntree() {
        return this.plateau.getEntree();
    }

    public Cases getSortie() {
        return this.plateau.getSortie();
    }

    public int getNbLigne() {
        return this.plateau.getNbLigne();
    }

    public int getNbColonne() {
        return this.plateau.getNbColonne();
    }

    public boolean estEntree(Cases c) {
        return this.plateau.estEntree(c);
    }

    public boolean estSortie(Cases c) {
        return this.plateau.estSortie(c);
    }

    public boolean estMur(Cases c) {
        return this.plateau.estMur(c);
    }

    public boolean estJoueur(Cases c) {
        return this.posJoueur.equals(c);
    }

    public Cases getOldPosJoueur() {
        return this.oldPosJoueur;
    }

    public boolean aGagner() {
        return posJoueur.equals(this.plateau.getSortie());
    }

    /**
     * Vérifie si une case est une bordure du plateau.
     * <p>
     * Les cases correspondant à la position du joueur, à l'entrée ou à la sortie ne
     * sont pas considérées comme des bordures.
     * </p>
     * 
     * @param c la case à tester
     * @return true si la case est une bordure, false sinon
     */
    public boolean estBordure(Cases c) {
        if (estJoueur(c) || estEntree(c) || estSortie(c))
            return false;
        return c.getX() == 0 || c.getX() == this.getNbColonne() - 1 || c.getY() == 0
                || c.getY() == this.getNbLigne() - 1;
    }

    public int getDistanceEntreeSortie() {
        return this.plateau.getDistanceEntreeSortie();
    }
}
