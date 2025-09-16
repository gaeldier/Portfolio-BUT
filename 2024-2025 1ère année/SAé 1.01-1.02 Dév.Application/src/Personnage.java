public class Personnage {
    int vie;
    String nom;
    boolean chevalier;
    boolean ninja;
    boolean hardcore;
    /*  Du côté de ce qu'on devait faire au minimum, j'ai pensé aux "différents personnages", 
    /mais ai-je le droit aux constructeurs dans les classes importées ? Exemple ci-dessous
    /*
    boolean tank; 
    public Personnage(String nom, boolean tank, int vie) {
        if (tank) {
            vie = 150;
        } else {
            vie = 100;
        }
        this.nom = nom;
        this.tank = tank;
        this.vie = vie;
    }*/
}
