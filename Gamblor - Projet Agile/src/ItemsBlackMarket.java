

public enum ItemsBlackMarket {
    REIN("Rein", 0,100,1,30), POUMON("Poumon",1,200,1,20), SANG("Poche de Sang",2,10,3,5);

    String name;
    int id;
    int gain;
    int maxshop;
    int loss;

    ItemsBlackMarket(String name, int id, int gain, int maxshop, int loss) {
        
        this.name = name;
        this.id = id;
        this.gain = gain;
        this.maxshop = maxshop;
        this.loss = loss;
    }
}