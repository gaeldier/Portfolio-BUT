import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * La classe Adolescents représente un adolescent avec des informations
 * personnelles
 * et des critères spécifiques pour évaluer la compatibilité avec un autre
 * adolescent.
 */
public class Adolescents {
    private Map<Critere, String> details;

    private String nom;
    private String prenom;
    private String genre;
    private LocalDate dateNaissance;
    private String paysOrigine;

    /**
     * Constructeur pour initialiser un objet Adolescents avec ses détails.
     *
     * @param genre         Le genre de l'adolescent.
     * @param gallergy      Allergie aux animaux de l'invité.
     * @param hanimal       Présence d'animaux chez l'hôte.
     * @param gfood         Régime alimentaire de l'invité.
     * @param hfood         Régime alimentaire de l'hôte.
     * @param hobbies       Loisirs de l'adolescent.
     * @param history       Historique de l'adolescent.
     * @param pairGenre     Genre de la paire.
     * @param nom           Nom de l'adolescent.
     * @param prenom        Prénom de l'adolescent.
     * @param dateNaissance Date de naissance de l'adolescent.
     * @param paysOrigine   Pays d'origine de l'adolescent.
     */

    public Adolescents(String genre, String gallergy, String hanimal, String gfood, String hfood, String hobbies,
            String history, String pairGenre, String nom, String prenom, String dateNaissance, String paysOrigine) {
        details = new HashMap<>();
        details.put(Critere.GUEST_ANIMAL_ALLERGY, gallergy);
        details.put(Critere.HOST_HAS_ANIMAL, hanimal);
        details.put(Critere.GUEST_FOOD, gfood);
        details.put(Critere.HOST_FOOD, hfood);
        details.put(Critere.HOBBIES, hobbies);
        details.put(Critere.PAIR_GENDER, pairGenre);
        details.put(Critere.HISTORY, history);
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.dateNaissance = LocalDate.parse(dateNaissance);
        this.paysOrigine = paysOrigine;
    }

    public void setGallergy(String gallergy) {
        details.put(Critere.GUEST_ANIMAL_ALLERGY, gallergy);
    }

    public void setHanimal(String hanimal) {
        details.put(Critere.HOST_HAS_ANIMAL, hanimal);
    }

    public void setGuestFood(String guestFood) {
        details.put(Critere.GUEST_FOOD, guestFood);
    }

    public void setHostFood(String hostFood) {
        details.put(Critere.HOST_FOOD, hostFood);
    }

    public void setHistory(String history) {
        details.put(Critere.HISTORY, history);
    }

    public String getGallergy(){
        return details.get(Critere.GUEST_ANIMAL_ALLERGY);
    }

    public String getHanimal(){
        return details.get(Critere.HOST_HAS_ANIMAL);
    }

    public String getHobbies(){
        return details.get(Critere.HOBBIES);
    }

    public String getHistory(){
        return details.get(Critere.HISTORY);
    }

    public String getPairGenre(){
        return details.get(Critere.PAIR_GENDER);
    }

    /**
     * Compare la date de naissance de cet adolescent avec un autre.
     *
     * @param other L'autre adolescent à comparer.
     * @return 0 si les dates sont compatibles (1 an et demi max), 1 sinon.
     */

    public int compareNaissance(Adolescents other) {
        LocalDate dateOther = other.dateNaissance;
        LocalDate plusUnAnEtDemi = dateOther.minusMonths(18);
        LocalDate moinsUnAnEtDemi = dateOther.plusMonths(18);

        if (this.dateNaissance.isAfter(plusUnAnEtDemi) && this.dateNaissance.isBefore(moinsUnAnEtDemi)) {
            return 0;
        }
        return 1;
    }

    /**
     * Vérifie si l'adolescent a une allergie aux animaux.
     *
     * @param other L'autre adolescent à comparer.
     * @return -1 si l'adolescent n'a pas d'allergie, 0 sinon.
     */

    public int animal_allergie(Adolescents other) {
        if (other.getValeurCritere(Critere.GUEST_ANIMAL_ALLERGY).equals("no")) {
            return -1;
        }
        if (other.getValeurCritere(Critere.GUEST_ANIMAL_ALLERGY).equals("yes")
                && this.getValeurCritere(Critere.HOST_HAS_ANIMAL).equals("no")) {
            return -1;
        }
        return 0;
    }

    /**
     * Récupère la valeur d'un critère spécifique.
     *
     * @param critere Le critère à récupérer.
     * @return La valeur du critère.
     */

    public String getValeurCritere(Critere critere) {
        return details.get(critere);

    }

    /**
     * Contrainte en fonction des régimes alimentaires.
     *
     * @param other L'autre adolescent.
     * @return -1 si compatible, 1 sinon.
     */

    public int regimeAlimentaire(Adolescents other) {
        ArrayList<String> regimesVisiteurList = listCreator(other.getValeurCritere(Critere.GUEST_FOOD));
        ArrayList<String> regimesHoteList = listCreator(this.getValeurCritere(Critere.HOST_FOOD));
        for (String regime : regimesVisiteurList) {
            if (regimesHoteList.contains(regime)) {
                return -1;
            }
        }
        return 1;
    }

    /**
     * Crée une liste à partir d'une chaîne de contraintes.
     *
     * @param contrainte La chaîne de contraintes.
     * @return Une liste des contraintes.
     */

    public ArrayList<String> listCreator(String contrainte) {
        ArrayList<String> res = new ArrayList<>();
        String[] regimeVisiteur = contrainte.split(",");
        for (String regime : regimeVisiteur) {
            res.add(regime);
        }
        return res;
    }

    /**
     * Contrainte en fonction de l'historique.
     *
     * @param other L'autre adolescent.
     * @return Une valeur représentant la compatibilité (-5 compatible (fort), 5
     *         incompatible (fort)).
     */

    public int history(Adolescents other) {
        if (this.getValeurCritere(Critere.HISTORY).equals("other")
                && other.getValeurCritere(Critere.HISTORY).equals("other")) {
            return 5;
        } else if (other.getValeurCritere(Critere.HISTORY).equals("other")
                && this.getValeurCritere(Critere.HISTORY).equals("null")) {
            return 1;
        } else if (this.getValeurCritere(Critere.HISTORY).equals("other")
                && other.getValeurCritere(Critere.HISTORY).equals("null")) {
            return 1;
        } else if (this.getValeurCritere(Critere.HISTORY).equals("same")
                && other.getValeurCritere(Critere.HISTORY).equals("same")) {
            return -5;
        }
        return 0;
    }

    /**
     * Regarde si un des adolescents est français pour corréler avec le critère d'appariement
     * 
     * @param other L'autre adolescent.
     * @return  Une valeur de compatibilité
     */

    public int compatibleAvecFr(Adolescents other) {
        ArrayList<String> hobbiesVisiteurList = listCreator(other.getValeurCritere(Critere.HOBBIES));
        ArrayList<String> hobbiesHoteList = listCreator(this.getValeurCritere(Critere.HOBBIES));
        if (this.getPaysOrigine().equals(Pays.FR) || other.getPaysOrigine().equals(Pays.FR)) {
            for (String hobbie : hobbiesVisiteurList) {
                if (hobbiesHoteList.contains(hobbie)) {
                    return 0;
                }
            }
        }
        return 5;
    }

    /**
     * Vérifie la compatibilité globale avec un autre adolescent.
     *
     * @param other L'autre adolescent.
     * @return false si pas compatible, true sinon.
     */

    public boolean compatibility(Adolescents other) {
        if (this.compatibleAvecFr(other) == 5)
            return false;
        if (this.history(other) == 5)
            return false;
        if (this.regimeAlimentaire(other) == 1)
            return false;
        if (this.animal_allergie(other) > 0)
            return false;
        return true;
    }

    // Getters
    public Map<Critere, String> getDetails() {
        return details;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getGenre() {
        return genre;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public Pays getPaysOrigine() {
        return Pays.valueOf(paysOrigine);
    }

    public String getFood() {
        return details.get(Critere.GUEST_FOOD);
    }

    public String getHostFood() {
        return details.get(Critere.HOST_FOOD);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((details == null) ? 0 : details.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        result = prime * result + ((prenom == null) ? 0 : prenom.hashCode());
        result = prime * result + ((genre == null) ? 0 : genre.hashCode());
        result = prime * result + ((dateNaissance == null) ? 0 : dateNaissance.hashCode());
        result = prime * result + ((paysOrigine == null) ? 0 : paysOrigine.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Adolescents other = (Adolescents) obj;
        if (details == null) {
            if (other.details != null)
                return false;
        } else if (!details.equals(other.details))
            return false;
        if (nom == null) {
            if (other.nom != null)
                return false;
        } else if (!nom.equals(other.nom))
            return false;
        if (prenom == null) {
            if (other.prenom != null)
                return false;
        } else if (!prenom.equals(other.prenom))
            return false;
        if (genre == null) {
            if (other.genre != null)
                return false;
        } else if (!genre.equals(other.genre))
            return false;
        if (dateNaissance == null) {
            if (other.dateNaissance != null)
                return false;
        } else if (!dateNaissance.equals(other.dateNaissance))
            return false;
        if (paysOrigine == null) {
            if (other.paysOrigine != null)
                return false;
        } else if (!paysOrigine.equals(other.paysOrigine))
            return false;
        return true;
    }

    
    /**
     * Vérifie si les critères sont valides.
     *
     * @throws InvalidDataException Si un critère est invalide.
     */
    public void critereInvalide() throws InvalidDataException {
        for (Critere c : details.keySet()) {
            if (c.getCategory() == "B") {
                    if (!details.get(c).equals("yes") || !details.get(c).equals("no")) {
                        Critere.isInvalid();
                    }
            }
        }
    }

    /**
     * Met un critère à null.
     *
     * @param critere Le critère à mettre à null.
     */

    public void setCritereToNull(Critere critere) {
        details.replace(critere, null);
    }


    /**
     * @return Cet adolescent sous forme de chaîne de caractères 
     */

    public String toString() {
        return "Adolescent : " + nom + " " + prenom;
    }
    
    
    /**
     * Calcule le score pour l'appariement avec un autre adolescent.
     *
     * @param other L'autre adolescent.
     * @return Le score d'appariement.
     */

    public double getScorePourAppariement(Adolescents other) {
        double score = 5;
        score += compatibleAvecFr(other);
        score += history(other);
        score += regimeAlimentaire(other);
        score += animal_allergie(other);
        return score;
    }
}