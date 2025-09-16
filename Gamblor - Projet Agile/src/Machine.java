import java.io.File;
import java.util.Random;
import java.util.Scanner;


public class Machine {

    private int[] result = new int [3];
    private int cptTour = 1;
    private boolean reduc = false;
    private int cout = 1;
    private int bonus = 1; 
    private EventsCreator eventCreator = new EventsCreator();

    private int nbRandom() {
        Random rng = new Random();
        return rng.nextInt(3);
    }

    public int getCout() {
        return cout;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    public int getCptTour() {
        return cptTour;
    }

    public boolean getReduc() {
        return this.reduc;
    }

    public void setReduc(boolean reduc) {
        this.reduc = reduc;
    }
    
    public int[] machineResult() {
        for (int i = 0; i < 3; i++) {
            this.result[i] = nbRandom();
        }
        return this.result;
    }

    public int[] getResult() {
        return this.result;
    }


    public void afficherRessorces(int i) {
        switch(i) {
            case 0:
                SymbolesGamble.returnAscii(SymbolesGamble.SPADE);
                break;
            case 1:
                SymbolesGamble.returnAscii(SymbolesGamble.STAR);
                break;
            case 2:
                SymbolesGamble.returnAscii(SymbolesGamble.HEART);
                break;
        }
    }

    public void afficherResultat(int[] resultat){;
        for (int i = 0; i<resultat.length; ++i) {
            this.afficherRessorces(getResult()[i]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleResult(int[] resultat, Joueur joueur, int pari) {
        if ((resultat[0] == resultat[1]) && (resultat[0] == resultat[2])) {
            File win = new File("res/Win.txt");
            try (Scanner fileScanner = new Scanner(win)) {
                while (fileScanner.hasNextLine()) {
                    System.out.println(fileScanner.nextLine());
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de la lecture du menu principal.");
            }
            joueur.substractStress(10);
            joueur.addWallet(pari*10*this.bonus);
            joueur.checkMinStress();
            afficherStats(joueur);
        }

        else {
            File lose = new File("res/Lose.txt");
            try (Scanner fileScanner = new Scanner(lose)) {
                while (fileScanner.hasNextLine()) {
                    System.out.println(fileScanner.nextLine());
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de la lecture du menu principal.");
            }
            joueur.addStress(10);
            afficherStats(joueur);
        }
    }

    public void afficherStats(Joueur joueur) {
        System.out.println("votre stress : " + joueur.getStress() + " votre Wallet : " + joueur.getWallet());
    }

    public void cptTour(){
        this.cptTour++;
    }

    public void eventAleatoire(Joueur j){
        if(this.cptTour > 10){
            if(this.cptTour%3 == 0){
                Events e = eventCreator.randEvent();
                System.out.println(e.getMessage());
                consequenceEvent(e, j);
            }
        }
    }

    public void consequenceEvent(Events e, Joueur j){
        if (e.equals(Events.MORT_GM)){
            j.addWallet(100);
            System.out.println("Vous gagnez 100$");
        }
        else if(e.equals(Events.TROUVER)){
           j.addWallet(20);
           System.out.println("Vous perdez 20$");
        }
        else if(e.equals(Events.TUITION_SCOLAIRE)){
           j.substractWallet(50);
           System.out.println("Vous perdez 50$");
        }
        else if(e.equals(Events.SOUHAIT)){
           j.substractWallet(30);
           System.out.println("Vous perdez 30$");
        }
        else if(e.equals(Events.VOLER)){
           j.substractWallet(20);
           System.out.println("Vous perdez 20$");
        }
        else if(e.equals(Events.TOMBER)){
           j.substractWallet(30);
           System.out.println("Vous perdez 30$");
        }
        else if(e.equals(Events.MALADE)){
           j.substractWallet(50);
           System.out.println("Vous perdez 50$");
        }
        else if(e.equals(Events.VOITURE)){
           j.addStress(20);
           System.out.println("Votre stress monte de 20");
        }
        else if(e.equals(Events.MORT_IDOL)){
           j.addStress(20);
           System.out.println("Votre stress monte de 20");
        }
        else if(e.equals(Events.ANGOISSE)){
           j.addStress(10);
           System.out.println("Votre stress monte de 10");
        }
        else if(e.equals(Events.AMIS)){
           j.addStress(10);
           System.out.println("Votre stress monte de 10");
        }
        else if(e.equals(Events.CHIEN)){
           j.substractStress(20);
           System.out.println("Votre stresse baisse de 20");
        }
        else if(e.equals(Events.BALADE)){
           j.substractStress(10);
           System.out.println("Votre stresse baisse de 10");
        }
        else if(e.equals(Events.RESPIRE)){
           j.substractStress(10);
           System.out.println("Votre stresse baisse de 10");
        }
        else if(e.equals(Events.ENFANCE)){
           j.substractStress(30);
           System.out.println("Votre stresse baisse de 30");
        }
        else if(e.equals(Events.REDUC)){
            this.reduc=true;
            System.out.println("Votre prochain tirage est à moitié prix");
        }
        else if(e.equals(Events.DOUBLE)){
           this.bonus = 2;
           System.out.println("Votre prochain tirage rapporte double");
        }
        else if(e.equals(Events.COUT)){
           this.cout = 2;
           System.out.println("Votre prochain tirage coûte le double du prix");
        }
    }
}