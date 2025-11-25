/**
 * ModeProgression est une implémentation spécifique de Labyrinthe représentant
 * un mode de jeu basé sur une progression par niveaux et difficultés.
 * <p>
 * Cette classe utilise un ModeProgressionLoader (mpl) pour obtenir les
 * paramètres de génération (nombre de lignes, nombre de colonnes et pourcentage
 * de murs) correspondant au niveau et à la difficulté demandés. Le labyrinthe
 * est ensuite créé via la méthode creerLabyrinthe héritée de Labyrinthe.
 * </p>
 *
 * <p>Responsabilités :</p>
 * <ul>
 *   <li>Fournir un point d'entrée simple permettant d'instancier un labyrinthe
 *       adapté à un niveau et une difficulté donnés.</li>
 *   <li>Déléguer la définition des dimensions et du taux de murs au
 *       ModeProgressionLoader (mpl).</li>
 * </ul>
 *
 * <p>Remarques :</p>
 * <ul>
 *   <li>La constante statique {@code mpl} est le chargeur partagé pour tous les
 *       objets ModeProgression.</li>
 *   <li>Les valeurs de {@code niveau} et {@code difficulte} doivent être
 *       conformes aux attentes de ModeProgressionLoader ; en cas de valeurs
 *       invalides, le comportement dépendra des validations mises en œuvre par
 *       ce chargeur.</li>
 * </ul>

 * Construit un ModeProgression pour un niveau et une difficulté donnés.
 *
 * <p>Comportement :</p>
 * <ol>
 *   <li>Appelle le constructeur parent avec des valeurs par défaut minimales.</li>
 *   <li>Interroge {@code mpl} pour obtenir :
 *     <ul>
 *       <li>le nombre de lignes via {@code getNbLignes(niveau)},</li>
 *       <li>le nombre de colonnes via {@code getNbColonnes(niveau)},</li>
 *       <li>le pourcentage de murs via {@code getPourcentageMur(niveau, difficulte)}.</li>
 *     </ul>
 *   </li>
 *   <li>Crée le labyrinthe réel en appelant {@code creerLabyrinthe} avec ces
 *       paramètres.</li>
 * </ol>
 *
 * @param niveau index ou identifiant du niveau de progression (doit être
 *               supporté par ModeProgressionLoader)
 * @param difficulte nom ou clé représentant la difficulté (ex. "facile",
 *                   "moyen", "difficile") telle que comprise par
 *                   ModeProgressionLoader
 *
 * @see ModeProgressionLoader#getNbLignes(int)
 * @see ModeProgressionLoader#getNbColonnes(int)
 * @see ModeProgressionLoader#getPourcentageMur(int, String)
 * @author Gaël Dierynck
 */
package modele;

public class ModeProgression extends Labyrinthe {
    public static final ModeProgressionLoader mpl = new ModeProgressionLoader();

    public ModeProgression(int niveau, String difficulte) {
        super(2, 2, 0.0);

        switch (niveau) {
            case 1, 2, 3:
                this.creerLabyrintheAleatoire(mpl.getNbLignes(niveau), mpl.getNbColonnes(niveau),
                        (double) mpl.getDifficulte(niveau, difficulte) / 100.0);
                break;
            case 4, 5, 6:
                this.creerLabyrintheBinaryTree(mpl.getNbLignes(niveau), mpl.getNbColonnes(niveau),
                        mpl.getDifficulte(niveau, difficulte), Orientation.NORD_EST);
                break;
        }
    }
}
