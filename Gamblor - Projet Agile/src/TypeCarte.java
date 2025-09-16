public enum TypeCarte {
    AS("As", 11),
    DEUX("Deux", 2),
    TROIS("Trois", 3),
    QUATRE("Quatre", 4),
    CINQ("Cinq", 5),
    SIX("Six", 6),
    SEPT("Sept", 7),
    HUIT("Huit", 8),
    NEUF("Neuf", 9),
    DIX("Dix", 10),
    VALET("Valet", 10),
    DAME("Dame", 10),
    ROI("Roi", 10);

    private final String nom;
    private final int poids;

    TypeCarte(String nom, int poids) {
        this.nom = nom;
        this.poids = poids;
    }

    public String getNom() {
        return nom;
    }

    public int getPoids() {
        return poids;
    }
}
