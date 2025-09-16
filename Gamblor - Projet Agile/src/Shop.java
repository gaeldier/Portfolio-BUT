import java.util.HashMap;

public class Shop {

    private HashMap<Items, Integer> itemList;

    Shop() {
        this.itemList = new HashMap<Items, Integer>();
        this.itemList.put(Items.PAINKILLERS, Items.PAINKILLERS.maxshop);
        this.itemList.put(Items.COFFEE, Items.COFFEE.maxshop);
        this.itemList.put(Items.SNOOP, Items.SNOOP.maxshop);
        this.itemList.put(Items.CIGARETTE, Items.CIGARETTE.maxshop);
        this.itemList.put(Items.DIAMOND, Items.DIAMOND.maxshop);
        this.itemList.put(Items.PIZZA, Items.PIZZA.maxshop);
        this.itemList.put(Items.EGG, Items.EGG.maxshop);
    }

    public HashMap<Items, Integer> getItems() {
        return this.itemList;
    }

    public void buy(Items item, Joueur joueur) {
        if (item.price > joueur.getWallet()) {
            System.out.println("vous n'avez pas assez d'argent pour acheter cet objet");
        } else if (item.maxuser <= joueur.getItems().get(item)) {
            System.out.println("vous n'avez pas assez d'espace pour acheter cet objet");
        } else if (itemList.get(item) <= 0) {
            System.out.println("le magasin est en rupture de stock pour cet objet");
        } else {
            joueur.substractWallet(item.price);
            joueur.addItem(item);
            itemList.replace(item, itemList.get(item), itemList.get(item) - 1);
            clearScreen();
            System.out.println("vous avez acheté un " + item.name);

        }
    }

    public void refresh(Joueur joueur) {
        if (joueur.getWallet() < 5) {
            System.out.println("vous n'avez pas assez d'argent pour refresh");
        } else {
            itemList.replace(Items.PAINKILLERS, Items.PAINKILLERS.maxshop);
            itemList.replace(Items.COFFEE, Items.COFFEE.maxshop);
            itemList.replace(Items.SNOOP, Items.SNOOP.maxshop);
            itemList.replace(Items.CIGARETTE, Items.CIGARETTE.maxshop);
            itemList.replace(Items.PIZZA, Items.PIZZA.maxshop);
            joueur.substractWallet(5);
        }
    }

    void clearScreen() {
        for (int i = 0; i < 50; ++i) {
            System.out.println();
        }
    }
}
