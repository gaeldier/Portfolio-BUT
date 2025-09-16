/**
 * Représente un tableau d'adolescents sous forme de paires clé-valeur, avec un score associé.
 * Cette classe encapsule une structure de données HashMap où chaque clé et valeur sont des objets Adolescents.
 * Elle fournit des méthodes pour accéder, modifier et manipuler ces paires, ainsi que pour gérer le score associé.
 *
 * <p>
 * Les principales fonctionnalités incluent :
 * <ul>
 *   <li>Initialisation avec différentes sources de données (HashMap ou tableau d'AdolescentsArray)</li>
 *   <li>Suppression des paires où un adolescent est associé à lui-même</li>
 *   <li>Accès aux hôtes et invités dans les paires</li>
 *   <li>Gestion du score</li>
 *   <li>Affichage textuel de l'ensemble des paires et du score</li>
 * </ul>
 * </p>
 *
 * @author Gaël Dierynck, Romain Harlaut, Amaury Vanhoutte
 * @version 1.0
 */
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class AdolescentsArray implements java.io.Serializable {
    private double score;
    private HashMap<Adolescents, Adolescents> hmAA;
    private final String path = "./res/config/config.class";
    File binaryFile = new File(path);
    

    public AdolescentsArray(HashMap<Adolescents, Adolescents> hmAA, double score) throws IOException {
        this.hmAA= hmAA;
        this.score = score;
        if(binaryFile.exists()) Appariement.automaticLoader(path);
    }

    public AdolescentsArray(HashMap<Adolescents, Adolescents> hmAA) throws IOException {
        this(hmAA, 0.0); // Default score of 0.0
    }

    public AdolescentsArray(AdolescentsArray[] aa) {
        this.hmAA = new HashMap<>();
        for (AdolescentsArray a : aa) {
            this.hmAA.putAll(a.getMap());
        }
        //this.score = 4.2; // Default score of 0.0
    }

    //Constructor for JavaFX serialization
    public AdolescentsArray(AdolescentsArray o) {
        this.hmAA = new HashMap<>(o.getMap());
        this.score = o.score; // Copy the score from the original object
    }

    public HashMap<Adolescents, Adolescents> getMap() {
        return this.hmAA;
    }

    /**
     * Permet de regarder et de retirer une paire où un adolescent et son hôte sont les mêmes. 
     * 
     * @param o l'autre AdolescentsArray
     */
    public void removeSelfPairing(AdolescentsArray o) {
        for(Adolescents ado : o.getMap().keySet()) {
            if(ado.equals(o.getMap().get(ado))) {
                o.getMap().remove(ado);
            }
        }
    }

    public Adolescents getAdolescentsGuest(Adolescents key) {
        return this.hmAA.get(key);
    }

    public Set<Adolescents> getHosts(Adolescents key) {
        return this.hmAA.keySet();
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdolescentsArray[]: \n");
        for (Adolescents ado : hmAA.keySet()) {
            sb.append(ado.toString()).append(" paired with ").append(hmAA.get(ado).toString()).append("\n");
        }
        sb.append("Score: ").append(score);
        return sb.toString();
    }
}

