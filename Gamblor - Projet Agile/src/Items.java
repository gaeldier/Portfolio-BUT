public enum Items {
    PAINKILLERS("ANTI DOULEUR", 0,25,5,6), COFFEE("CAFE",1,10,10,5), SNOOP("SNOOP",2,10,1,1), CIGARETTE("CIGARETTE", 3, 40, 10, 20), DIAMOND("DIAMANT", 4, 1000, 1, 1), PIZZA("PIZZA", 5, 15, 3, 1), EGG("OEUF DOUTEUX", 6, 1, 1, 1);

    String name;
    int id;
    int price;
    int maxshop;
    int maxuser;

    Items(String name, int id, int price, int maxshop, int maxuser) {
        
        this.name = name;
        this.id = id;
        this.price = price;
        this.maxshop = maxshop;
        this.maxuser = maxuser;
    }
}
