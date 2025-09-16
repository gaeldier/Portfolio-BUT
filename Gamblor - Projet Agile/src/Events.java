/**
 * 
 * @author Kylian Allard Guerente
 * @version 1.0
 */

public enum Events {
    //Types sont wallet 'w', 'c' casino et 's' stress
    //Message sont customs
    TUITION_SCOLAIRE(1,'w', "Votre fils part à l'université"),
    TROUVER(2,'w', "Vous trouvez de l'argent par terre"),
    SOUHAIT(3,'w', "Vous faites un voeu"),
    MORT_GM(4,'w', "Un de vos proches est mort"), 

    VOLER(5,'w', "Vous vous faites voler"),
    TOMBER(6,'w', "Vous laissez tombre votre portefeuille dans un endroit inacessible"),
    MALADE(7,'w', "Un de vos proches tombe terriblement malade et vous n'avez pas d'assurances"),

    CHIEN(1,'s', "Vous voyez un petit chien"), 
    BALADE(2,'s', "Vous faites une petite balade"), 
    RESPIRE(3,'s', "Vous respirez un bon coup"), 
    ENFANCE(4,'s', "Vous repensez à votre enfance"), 

    VOITURE(5,'s',"Vous avez failli rentrer dans un camion avec votre voiture"), 
    MORT_IDOL(6,'s',"Un de vos nombreux idoles est mort"), 
    ANGOISSE(7,'s',"Vous faites une crise d'angoisse"), 
    AMIS(8,'s',"Vous vous disputez avec vos amis"), 

    REDUC(1,'c',"Le casino est généreux et offre des réductions"),
    DOUBLE(2,'c',"Un vent de richesse arrive, les revenus et les pertes sont doublés"),
    COUT(3,'c',"Un vent de pauvreté arrive, les coûts sont doublés")
    ;
    private final int id;
    private final char TYPE;
    private final String message;
    
    private Events(int id, char type, String mess){
        this.id = id;
        this.TYPE = type;
        this.message = mess;
    }

    public String getMessage() {
        return message;
    }

    public char getTYPE() {
        return TYPE;
    }

    public int getId() {
        return id;
    }

    public boolean equals(Events oth){
        if(oth==null) return false;
        if(this==oth) return true;
        if(this.id != oth.id) return false;
        if(!this.message.equals(oth.getMessage())) return false;
        if(this.TYPE != oth.getTYPE()) return false;
        return true;
    }
}
