import java.util.Random;

/**
 * 
 * @author Kylian Allard Guerente
 * @version 1.0
 */

public class EventsCreator {

    private Random rand = new Random();


    private int random(int limit){
        int nb = rand.nextInt(limit);
        return nb;
    }

    private char randomType(){
        int aleaType = random(3);
        char type = 's';
        if(aleaType == 1){
            type = 'w';
        }
        else if(aleaType == 2){
            type = 'c';
        }

        return type;
    }

    private int nbTypes(char type){
        int nbTypes = 0;
        Events[] nbEvents = Events.values();
        for (int i = 0; i < nbEvents.length; i++){
            if(nbEvents[i].getTYPE() == type){
                nbTypes++;
            }
        }
        return nbTypes;

    }

    public Events rechercheEvent(char type, Events[] nbEvents){
        int nbTypes = nbTypes('w');
        random(nbTypes);
        int iType = 0;
        for (int i = 0; i < nbEvents.length; i++){
            if(nbEvents[i].getTYPE()== type){
                iType++;
                if(iType == nbEvents[i].getId()){
                    return nbEvents[i];
                }
            }
        }
        return Events.REDUC;
    }
    
    
    public Events randEvent(){
        char type = randomType();
        Events[] nbEvents = Events.values();
        if (type == 'w'){
            return rechercheEvent('w', nbEvents);
        }
        else if(type == 's'){
            return rechercheEvent('s', nbEvents);
        }
        else{
            return rechercheEvent('c', nbEvents);
        }
    }

}
