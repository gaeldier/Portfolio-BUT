/**
 * Classe ListeJoueursSerializable qui implémente l'interface Serializable.
 * Cette classe permet de sérialiser et désérialiser une liste de joueurs.
 * 
 * @author Gaël Dierynck
 * @version 1.0
 */
package modele;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ListeJoueursSerializable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("FieldMayBeFinal")
    private Set<Joueur> listeJoueurs;

    public ListeJoueursSerializable() {
        this.listeJoueurs = new HashSet<>();
    }

    public Set<Joueur> getListeJoueurs() {
        return listeJoueurs;
    }

    public void add(Joueur j) {
        this.listeJoueurs.add(j);
    }

    public Set<String> getAllPseudo() {
        Set<String> res = new HashSet<>();
        for (Joueur j : this.listeJoueurs) {
            res.add(j.getNom());
        }
        return res;
    }

    public Set<Joueur> getListJoueur() {
        return this.listeJoueurs;
    }
}