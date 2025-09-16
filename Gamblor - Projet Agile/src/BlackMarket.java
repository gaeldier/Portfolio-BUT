

import java.util.HashMap;

public class BlackMarket {
    private HashMap<ItemsBlackMarket,Integer> itemList;

    BlackMarket(){
        this.itemList = new HashMap<ItemsBlackMarket,Integer>();
        this.itemList.put(ItemsBlackMarket.REIN, ItemsBlackMarket.REIN.maxshop);
        this.itemList.put(ItemsBlackMarket.POUMON, ItemsBlackMarket.POUMON.maxshop);
        this.itemList.put(ItemsBlackMarket.SANG, ItemsBlackMarket.SANG.maxshop);
    }

    public HashMap<ItemsBlackMarket,Integer> getItems() {
        return this.itemList;
    }

    public void buy(ItemsBlackMarket item, Joueur joueur){
        if(itemList.get(item) <= 0){
            System.out.println("Vous voulez mourir ? Vous pouvez pas vendre autant de " + item.name);
        }
        else{
            joueur.addWallet(item.gain);
            joueur.addStress(item.loss);
            itemList.replace(item, itemList.get(item), itemList.get(item)-1);
            System.out.println("vous avez vendu un " + item.name);
            System.out.println("");
            System.out.println("Vous avez maintenant " + joueur.getWallet() + "$");
            System.out.println("Vous avez maintenant " + joueur.getStress() + " de stress");
            try {
                 Thread.sleep(1000); // délai de 1 seconde
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            clearScreen();
        }
    }

        void clearScreen() {
        for (int i = 0; i < 50; ++i) {
            System.out.println();
        }
    }
}

