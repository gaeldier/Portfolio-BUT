/**
 * Enumération des critères de compatibilité entre adolescents.
 * Chaque critère est associé à une catégorie.
 * Les catégories sont utilisées pour déterminer le type de critère.
 *
 */

public enum Critere {

    GUEST_ANIMAL_ALLERGY("B"),
    HOST_HAS_ANIMAL("B"),
    GUEST_FOOD("T"),
    HOST_FOOD("T"),
    HOBBIES("T"),
    GENDER("T"),
    PAIR_GENDER("T"),
    HISTORY("T");

    private final String category;

    private Critere(String category) {
        this.category = category;
    }

    private Critere() {
        this.category = null;
    }

    /**
     * Chaque instance de l'énumération possède une méthode permettant de récupérer
     * la catégorie associée.
     * 
     */

    public String getCategory() {
        return category;
    }

    /**
     * Méthode statique qui lève une exception InvalidDataException si le critère
     * n'est pas valide.
     * 
     * @throws InvalidDataException si le critère n'est pas valide
     */
    public static void isInvalid() throws InvalidDataException {
        throw new InvalidDataException("Erreur de saisie ! Le critère ne correspond pas");
    }
}
