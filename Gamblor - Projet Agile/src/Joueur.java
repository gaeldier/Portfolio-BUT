import java.io.Serializable;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * @author Amaury Vanhoutte, Gaël Dierynck, Yanis Mekki
 * @version 1.0
 */

public class Joueur implements Serializable {
    private String nom;
    private int wallet;
    private int stress;
    transient private HashMap<Items,Integer> itemList;
    transient private final int ID;
    transient private static int cpt;
    private int tourMax;

    public Joueur(int wallet, int stress, int iD) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 50; ++i) {
            System.out.println();
        }
        File nom = new File("res/nom.txt");
        try (Scanner fileScanner = new Scanner(nom)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du nom");
        }
        this.nom = sc.nextLine();
        this.wallet = wallet;
        this.stress = stress;
        this.itemList = new HashMap<Items, Integer>();
        this.itemList.put(Items.PAINKILLERS, 0);
        this.itemList.put(Items.COFFEE, 0);
        this.itemList.put(Items.SNOOP, 0);
        this.itemList.put(Items.CIGARETTE, 0);
        this.itemList.put(Items.DIAMOND, 0);
        this.itemList.put(Items.PIZZA, 0);
        this.itemList.put(Items.EGG, 0);
        this.ID = Joueur.cpt++;
    }

    public String getNom() {
        return nom;
    }

    public int getWallet() {
        return wallet;
    }

    public void setTourMax(int tour) {
        this.tourMax = tour;
    }
    
    public HashMap<Items,Integer> getItems() {
        return this.itemList;
    }

    public boolean addItem(Items item) {
        if (!itemList.containsKey(item)) {
            return false;
        }
        itemList.replace(item, itemList.get(item), itemList.get(item) + 1);
        return true;
    }

    public boolean consumeItem(Items item) {
        if (!itemList.containsKey(item)) {
            return false;
        } else if (itemList.get(item) <= 0) {
            System.out.println("VOUS NE POSSEDEZ PAS CET OBJET");
            return false;
        }
        itemList.replace(item, itemList.get(item), itemList.get(item) - 1);
        return true;
    }

    public void addWallet(int moneyAdded) {
        this.wallet = this.wallet + moneyAdded;
    }

    public void substractWallet(int moneySub) {
        this.wallet = this.wallet - moneySub;
        if (this.wallet < 0) {
            this.wallet = 0;
        }
    }

    public int getStress() {
        return stress;
    }

    public void addStress(int stressAdded) {
        this.stress = this.stress + stressAdded;
        if (this.stress > 100) {
            this.stress = 100;
        }
    }

    public void substractStress(int stressSub) {
        this.stress = this.stress - stressSub;
        if (this.stress < 0) {
            this.stress = 0;
        }
    }

    public int getID() {
        return ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Joueur other = (Joueur) obj;
        if (nom == null) {
            if (other.nom != null)
                return false;
        } else if (!nom.equals(other.nom))
            return false;
        if (wallet != other.wallet)
            return false;
        if (stress != other.stress)
            return false;
        if (ID != other.ID)
            return false;
        return true;
    }

    public void checkMinStress() {
        if (this.stress < 0) {
            this.stress = 0;
        }
    }

    public String toString() {
        return "Joueur :" + this.getNom() + "  a fini avec " + this.tourMax + "  point(s), " + this.getWallet()
                    + "€ restant(s) dans son porte-monnaie et " + this.getStress() + " de stress.";
    }
}