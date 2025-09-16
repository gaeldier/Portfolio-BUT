import java.io.File;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Gamblor {

    private static Machine machine;
    private static Roulette roulette;
    private static Shop shop;
    private static BlackMarket blackmarket;
    private Joueur joueur;

    public Gamblor() {
        Gamblor.machine = new Machine();
        Gamblor.roulette = new Roulette();
        Gamblor.shop = new Shop();
        Gamblor.blackmarket = new BlackMarket();
        this.joueur = new Joueur(1000, 0, 0);
    }

    public Gamblor(Machine m, Joueur j) {
        Gamblor.machine = m;
        this.joueur = j;
    }

    public void actionEntry() {
        clearScreen();
        while (!gameLoseEnd(joueur)) {
            gameOver();
            Scanner sc = new Scanner(System.in);
            File jeu = new File("res/jeu.txt");
            try (Scanner fileScanner = new Scanner(jeu)) {
                while (fileScanner.hasNextLine()) {
                    System.out.println(fileScanner.nextLine());
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de la lecture du menu jeu.");
            }
            roulette.afficherStats(joueur);
            String entry = sc.nextLine();
            while (!entry.equals("S") && !entry.equals("s")) {
                gameOver();
                while (!entry.equals("P") && !entry.equals("p")) {
                    System.out.println("Oups ! Ce choix n’est pas valide.");
                    entry = sc.nextLine();
                }
                clearScreen();
                File mise = new File("res/mise.txt");
                try (Scanner fileScanner = new Scanner(mise)) {
                    while (fileScanner.hasNextLine()) {
                        System.out.println(fileScanner.nextLine());

                    }
                    System.out.println("Argent : " + joueur.getWallet() + "€");
                    System.out.println("Stress : " + joueur.getStress() + "/100");
                } catch (Exception e) {
                    System.out.println("Erreur lors de la lecture du menu mise.");
                }
                String valeur = sc.nextLine();
                clearScreen();
                Boolean valide = true;
                if (valeur.length() <= 0) {
                    System.out.println("Oups ! Choix pas valide.");
                    actionEntry();
                }
                for (int i = 0; i < valeur.length(); i++) {
                    if (valeur.charAt(i) < '0' || valeur.charAt(i) > '9') {
                        valide = false;
                    }
                }
                if (valide) {
                    int pari = 0;
                    if (valeur.length() > 6) {
                        System.out.println("Entrez une valeur entre 10 et 100.");
                    } else {
                        pari = Integer.parseInt(valeur);
                    }
                    if (pari < 10 || pari > 100) {
                        System.out.println("Entrez une valeur entre 10 et 100.");
                    } else {
                        if (joueur.getWallet() < pari) {
                            System.out.println("Vous n'avez pas assez pour parier cela.");
                        } else {
                            if (machine.getReduc()) {
                                joueur.substractWallet(pari / 2);
                            } else {
                                joueur.substractWallet(pari);
                            }
                            machine.setReduc(false);
                            machine.machineResult();
                            machine.afficherResultat(machine.getResult());
                            machine.handleResult(machine.getResult(), joueur, pari);
                            machine.eventAleatoire(joueur);
                            machine.cptTour();
                        }
                    }
                } else {
                    System.out.println("entrez une valeur numérique à parier");
                }
                File jeu3 = new File("res/jeu3.txt");
                try (Scanner fileScanner = new Scanner(jeu3)) {
                    while (fileScanner.hasNextLine()) {
                        System.out.println(fileScanner.nextLine());
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors de la lecture du menu jeu.");
                }
                entry = sc.nextLine();
            }
            afficherMenuPrincipal();
        }
        gameOver();
    }

    public void actionEntryRoulette() {
        clearScreen();
        while (!gameLoseEnd(joueur)) {
            Scanner sc = new Scanner(System.in);
            String color = "";
            String nb = "";
            String mode = "";
            File jeu2 = new File("res/jeu2.txt");
            try (Scanner fileScanner = new Scanner(jeu2)) {
                while (fileScanner.hasNextLine()) {
                    System.out.println(fileScanner.nextLine());
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de la lecture du menu jeu.");
            }
            roulette.afficherStats(joueur);
            String entry = sc.nextLine();
            while (!entry.equals("S") && !entry.equals("s")) {
                gameOver();
                while (!entry.equals("P") && !entry.equals("p")) {
                    gameOver();
                    System.out.println("Oups ! Ce choix n’est pas valide.");
                    entry = sc.nextLine();
                }
                clearScreen();
                File mise2 = new File("res/mise2.txt");
                try (Scanner fileScanner = new Scanner(mise2)) {
                    while (fileScanner.hasNextLine()) {
                        System.out.println(fileScanner.nextLine());
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors de la lecture du menu jeu.");
                }
                roulette.afficherStats(joueur);
                String valeur = sc.nextLine();
                clearScreen();
                Boolean valide = true;
                if (valeur.length() <= 0) {
                    System.out.println("Oups ! Choix pas valide.");
                    actionEntryRoulette();
                }
                for (int i = 0; i < valeur.length(); i++) {
                    if ((valeur.charAt(i) < '0' || valeur.charAt(i) > '9')) {
                        valide = false;
                    }
                }
                if (valide) {
                    int pari = 0;
                    if (valeur.length() > 6) {
                        valide = false;
                    } else {
                        pari = Integer.parseInt(valeur);
                        if (pari < 100 || pari > 1000) {
                            valide = false;
                        }
                    }
                    if (!valide) {
                        System.out.println("Entrez une valeur entre 100 et 1000.");
                    } else {
                        if (joueur.getWallet() < pari) {
                            File poor = new File("res/topoor.txt");
                            try (Scanner fileScanner = new Scanner(poor)) {
                                while (fileScanner.hasNextLine()) {
                                    System.out.println(fileScanner.nextLine());
                                }
                            } catch (Exception e) {
                                System.out.println("Erreur lors de la lecture du menu jeu.");
                            }
                        } else {
                            if (machine.getReduc()) {
                                joueur.substractWallet(pari / 2);
                            } else {
                                joueur.substractWallet(pari);
                            }
                            File choix = new File("res/choix.txt");
                            try (Scanner fileScanner = new Scanner(choix)) {
                                while (fileScanner.hasNextLine()) {
                                    System.out.println(fileScanner.nextLine());
                                }
                            } catch (Exception e) {
                                System.out.println("Erreur lors de la lecture du menu jeu.");
                            }
                            File choix2 = new File("res/choix2.txt");
                            try (Scanner fileScanner = new Scanner(choix2)) {
                                while (fileScanner.hasNextLine()) {
                                    System.out.println(fileScanner.nextLine());
                                }
                            } catch (Exception e) {
                                System.out.println("Erreur lors de la lecture du menu jeu.");
                            }
                            mode = sc.nextLine();
                            switch (mode) {
                                case "1":
                                    clearScreen();
                                    File choix3 = new File("res/choix3.txt");
                                    try (Scanner fileScanner = new Scanner(choix3)) {
                                        while (fileScanner.hasNextLine()) {
                                            System.out.println(fileScanner.nextLine());
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Erreur lors de la lecture du menu jeu.");
                                    }
                                    color = sc.nextLine();
                                    if (!color.equals("0") && !color.equals("1") && !color.equals("2")) {
                                        System.out.println(
                                                "Entrée invalide !!! Appuyez sur entrée pour revenir au début !");
                                        sc.nextLine();
                                        actionEntryRoulette();
                                    }
                                    break;
                                case "2":
                                    clearScreen();
                                    File choix4 = new File("res/choix4.txt");
                                    try (Scanner fileScanner = new Scanner(choix4)) {
                                        while (fileScanner.hasNextLine()) {
                                            System.out.println(fileScanner.nextLine());
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Erreur lors de la lecture du menu jeu.");
                                    }
                                    nb = sc.nextLine();
                                    if (Integer.parseInt(nb) < 0 || Integer.parseInt(nb) > 36) {
                                        System.out.println(
                                                "Entrée invalide !!! Appuyez sur entrée pour revenir au début !");
                                        sc.nextLine();
                                        actionEntryRoulette();
                                    }
                                    break;
                                case "3":
                                    clearScreen();
                                    File choix33 = new File("res/choix3.txt");
                                    try (Scanner fileScanner = new Scanner(choix33)) {
                                        while (fileScanner.hasNextLine()) {
                                            System.out.println(fileScanner.nextLine());
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Erreur lors de la lecture du menu jeu.");
                                    }

                                    color = sc.nextLine();
                                    if (!color.equals("0") && !color.equals("1") && !color.equals("2")) {
                                        System.out.println(
                                                "Entrée invalide !!! Appuyez sur entrée pour revenir au début !");
                                        sc.nextLine();
                                        actionEntryRoulette();
                                    }
                                    clearScreen();
                                    File choix44 = new File("res/choix4.txt");
                                    try (Scanner fileScanner = new Scanner(choix44)) {
                                        while (fileScanner.hasNextLine()) {
                                            System.out.println(fileScanner.nextLine());
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Erreur lors de la lecture du menu jeu.");
                                    }
                                    nb = sc.nextLine();
                                    if (Integer.parseInt(nb) < 0 || Integer.parseInt(nb) > 36) {
                                        System.out.println(
                                                "Entrée invalide !!! Appuyez sur entrée pour revenir au début !");
                                        sc.nextLine();
                                        actionEntryRoulette();
                                    }
                                    break;
                                default:
                                    System.out
                                            .println("Entrée invalide !!! Appuyez sur entrée pour revenir au début !");
                                    sc.nextLine();
                                    actionEntryRoulette();
                                    break;
                            }

                            roulette.machineResult();
                            roulette.rouletteResult();
                            roulette.handleResultRoulette(pari, roulette.getResult(), roulette.getRouletteResult(),
                                    mode, color, nb, joueur);
                            roulette.afficherResultatRoulette(roulette.getResult(), roulette.getRouletteResult());
                            roulette.eventAleatoire(joueur);
                            roulette.cptTour();
                            gameOver();
                        }
                    }
                } else {
                    System.out.println("entrez une valeur numérique à parier");
                }
                gameOver();
                System.out.println("Appuyez sur [P] pour tirer, tapez [S] pour arrêter.");
                entry = sc.nextLine();
            }
            afficherMenuPrincipal();
        }
        gameOver();
    }

    public void actionEntryBlackjack() {
        Scanner sc = new Scanner(System.in);
        Blackjack blackjack = new Blackjack();

        blackjack.jouerBlackjack(joueur);

        if (gameLoseEnd(joueur)) {
            gameOver();
        } else {
            afficherMenuMachine();
        }
    }

    public void jouerBlackjack() {
        actionEntryBlackjack();
    }

    public static void gambleTime(Gamblor game) {
        game.afficherMenuPrincipal();
        game.actionEntry();
    }

    public void afficherMenuPrincipal() {
        clearScreen();
        String score = ToolBox.scoreReader("./res/Scores/scores.txt");
        Scanner sc = new Scanner(System.in);
        File menuPrincipal = new File("res/menus/MenuPrincipal1.txt");
        try (Scanner fileScanner = new Scanner(menuPrincipal)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du menu principal.");
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File menuPrincipal2 = new File("res/menus/MenuPrincipal2.txt");
        try (Scanner fileScanner = new Scanner(menuPrincipal2)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du menu principal.");
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File menuPrincipal3 = new File("res/menus/MenuPrincipal3.txt");
        try (Scanner fileScanner = new Scanner(menuPrincipal3)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du menu principal.");
        }
        String choixUtilisateur = sc.nextLine();
        while (!choixUtilisateur.equals("1") && !choixUtilisateur.equals("2") && !choixUtilisateur.equals("3")
                && !choixUtilisateur.equals("4") && !choixUtilisateur.equals("scores")
                && !choixUtilisateur.equals("demo")) {
            System.out.println("Oups ! Ce choix n’est pas valide.");
            choixUtilisateur = sc.nextLine();
        }
        if (choixUtilisateur.equals("1")) {
            afficherMenuMachine();
        }
        if (choixUtilisateur.equals("demo")) {
            afficherDemo();
        }
        if (choixUtilisateur.equals("2")) {
            menuMainShop();
        }
        if (choixUtilisateur.equals("3")) {
            accessItems();
        } else if (choixUtilisateur.equals("scores")) {
            afficherScores(score);
        } else {

            ToolBox.scoreWriter(String.valueOf(machine.getCptTour()), joueur.getNom(),
                    String.valueOf(joueur.getWallet()), String.valueOf(joueur.getStress()),
                    "./res/Scores/scores.txt");

            ToolBox.saveSerialScore(joueur);
            System.exit(0);
        }
        sc.close();

    }

    public void menuMainShop() {
        clearScreen();
        File shop1 = new File("res/shop1.txt");
        try (Scanner fileScanner = new Scanner(shop1)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du menu principal.");
        }
        File shop2 = new File("res/shop2.txt");
        try (Scanner fileScanner = new Scanner(shop2)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du menu principal.");
        }
        Scanner sc = new Scanner(System.in);
        String choix = sc.nextLine();
        while (!choix.equals("1") && !choix.equals("2") && !choix.equals("3")) {
            System.out.println("Oups ! Ce choix n’est pas valide.");
            choix = sc.nextLine();
        }
        if (choix.equals("1")) {
            menuShop();
        } else if (choix.equals("2")) {
            menuBackMarket();
        } else {
            afficherMenuPrincipal();
        }
    }

    public void afficherMenuBlackMarket() {
        System.out.println("=====  Que souhaitez-vous vendre ?  =====");
        int i = 1;
        for (ItemsBlackMarket item : blackmarket.getItems().keySet()) {
            int stock = blackmarket.getItems().get(item);
            if (stock > 0) {
                System.out.println(i + ". " + item.name + " (+" + item.gain + "$, stress +" + item.loss + "), " + stock
                        + " disponibles");
            }
            i++;
        }
        System.out.println("99. Revenir au menu principal");
    }

    public void menuBackMarket() {
        clearScreen();
        Scanner sc = new Scanner(System.in);
        while (true) {
            afficherMenuBlackMarket();
            String choix = sc.nextLine();

            if (choix.equals("99")) {
                afficherMenuPrincipal();
                break;
            }

            int choixInt;
            try {
                choixInt = Integer.parseInt(choix);
            } catch (NumberFormatException e) {
                System.out.println("Oups ! Ce choix n’est pas valide.");
                continue;
            }

            // Associer le numéro à l'item correspondant
            ItemsBlackMarket[] items = blackmarket.getItems().keySet().toArray(new ItemsBlackMarket[0]);
            if (choixInt >= 1 && choixInt <= items.length) {
                ItemsBlackMarket item = items[choixInt - 1];
                blackmarket.buy(item, joueur);
            } else {
                System.out.println("Oups ! Ce choix n’est pas valide.");
            }
        }
    }

    public void afficherScores(String scores) {
        clearScreen();
        System.out.println("=====  Scores  =====");
        System.out.println(scores);
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Scores dans le fichier kar \n");
        try {
            ToolBox.loadSerialScore();
        } catch (Exception e) {
            System.out.println("Tu as joué ?");
            System.out.println("\n");
            System.out.println("\n");
            System.out.println("\n");
            System.out.println("\n");
            System.out.println("\n");
            System.out.println("\n");
            System.out.println("\n");
            e.printStackTrace();
            afficherMenuPrincipal();

        }
        System.out.println("Appuyez sur q pour quitter");
        Scanner sc = new Scanner(System.in);
        String choix = sc.nextLine();
        while (!choix.equals("q")) {
            System.out.println("Oups ! Ce choix n’est pas valide.");
            choix = sc.nextLine();
        }
    }

    public void afficherDemo() {
        clearScreen();
        File demo = new File("res/demo.txt");
        try (Scanner fileScanner = new Scanner(demo)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du menu machine.");
        }
    }

    public void afficherMenuMachine() {
        clearScreen();
        File menuMachine = new File("res/menus/MenuMachine.txt");
        try (Scanner fileScanner = new Scanner(menuMachine)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du menu machine.");
        }
        Scanner sc = new Scanner(System.in);
        String choixUtilisateur = sc.nextLine();
        while (!choixUtilisateur.equals("1") && !choixUtilisateur.equals("2") && !choixUtilisateur.equals("3")
                && !choixUtilisateur.equals("4") && !choixUtilisateur.equals("5")) {
            System.out.println("Oups ! Ce choix n’est pas valide.");
            choixUtilisateur = sc.nextLine();
        }
        if (choixUtilisateur.equals("1")) {
            actionEntry();
        }
        if (choixUtilisateur.equals("2")) {
            actionEntryRoulette();
        }
        if (choixUtilisateur.equals("3")) {
            jouerBlackjack();
        }
        if (choixUtilisateur.equals("4")) {
            afficherMenuPrincipal();
        }
        sc.close();
    }

    public void afficherMenuShop() {
        System.out.println("=====  Que souhaitez-vous acheter ?  =====");
        System.out.println("=====  Veuillez saisir le numéro correspondant à votre choix :  =====");
        System.out.println("Il vous reste " + joueur.getWallet() + "$");
        System.out.println("1. Anti douleur (" + Items.PAINKILLERS.price + "$), "
                + shop.getItems().get(Items.PAINKILLERS) + " en stock");
        System.out.println("2. Café(" + Items.COFFEE.price + "$), " + shop.getItems().get(Items.COFFEE) + " en stock");
        System.out.println("3. Snoop(" + Items.SNOOP.price + "$), " + shop.getItems().get(Items.SNOOP) + " en stock");
        System.out.println(
                "4. Cigarettes(" + Items.CIGARETTE.price + "$), " + shop.getItems().get(Items.CIGARETTE) + " en stock");
        System.out.println("5. Diamant(" + Items.DIAMOND.price + "$), " + shop.getItems().get(Items.DIAMOND)
                + " en stock (ne se reaprovisionne pas)");
        System.out.println("6. Pizza(" + Items.PIZZA.price + "$), " + shop.getItems().get(Items.PIZZA) + " en stock");
        System.out
                .println("7. Oeuf Douteux(" + Items.EGG.price + "$), " + shop.getItems().get(Items.EGG) + " en stock");
        System.out.println("8. Réapprovisionner le stock du magasin(5$)");
        System.out.println("99. Revenir au menu principal");
    }

    public void menuShop() {
        clearScreen();
        Scanner sc = new Scanner(System.in);
        afficherMenuShop();
        String choixUtilisateur = sc.nextLine();

        while (!choixUtilisateur.equals("99")) {
            while (!choixUtilisateur.equals("1") && !choixUtilisateur.equals("2") && !choixUtilisateur.equals("3")
                    && !choixUtilisateur.equals("4") && !choixUtilisateur.equals("5") && !choixUtilisateur.equals("6")
                    && !choixUtilisateur.equals("7") && !choixUtilisateur.equals("8")
                    && !choixUtilisateur.equals("99")) {
                System.out.println("Oups ! Ce choix n’est pas valide.");
                choixUtilisateur = sc.nextLine();
            }
            if (choixUtilisateur.equals("1")) {
                shop.buy(Items.PAINKILLERS, joueur);
                afficherMenuShop();
                choixUtilisateur = sc.nextLine();
            }
            if (choixUtilisateur.equals("2")) {
                shop.buy(Items.COFFEE, joueur);
                afficherMenuShop();
                choixUtilisateur = sc.nextLine();
            }
            if (choixUtilisateur.equals("3")) {
                shop.buy(Items.SNOOP, joueur);
                afficherMenuShop();
                choixUtilisateur = sc.nextLine();
            }
            if (choixUtilisateur.equals("4")) {
                shop.buy(Items.CIGARETTE, joueur);
                afficherMenuShop();
                choixUtilisateur = sc.nextLine();
            }
            if (choixUtilisateur.equals("5")) {
                shop.buy(Items.DIAMOND, joueur);
                afficherMenuShop();
                choixUtilisateur = sc.nextLine();
            }
            if (choixUtilisateur.equals("6")) {
                shop.buy(Items.PIZZA, joueur);
                afficherMenuShop();
                choixUtilisateur = sc.nextLine();
            }
            if (choixUtilisateur.equals("7")) {
                shop.buy(Items.EGG, joueur);
                afficherMenuShop();
                choixUtilisateur = sc.nextLine();
            }
            if (choixUtilisateur.equals("8")) {
                shop.refresh(joueur);
                afficherMenuShop();
                choixUtilisateur = sc.nextLine();
            }
        }
        afficherMenuPrincipal();
    }

    public void accessItems() {
        clearScreen();
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Anti douleur (" + Items.PAINKILLERS.price + "$), "
                + joueur.getItems().get(Items.PAINKILLERS) + " dans votre sac");
        System.out.println(
                "2. Café(" + Items.COFFEE.price + "$), " + joueur.getItems().get(Items.COFFEE) + " dans votre sac");
        System.out.println(
                "3. Snoop(" + Items.SNOOP.price + "$), " + joueur.getItems().get(Items.SNOOP) + " dans votre sac");
        System.out.println("4. Cigarettes(" + Items.CIGARETTE.price + "$), " + joueur.getItems().get(Items.CIGARETTE)
                + " dans votre sac");
        System.out.println("5. Diamant(" + Items.DIAMOND.price + "$), " + joueur.getItems().get(Items.DIAMOND)
                + " dans votre sac (exemplaire unique)");
        System.out.println(
                "6. Pizza(" + Items.PIZZA.price + "$), " + joueur.getItems().get(Items.PIZZA) + " dans votre sac");
        System.out.println("7. Oeuf Douteux(" + Items.EGG.price + "$), " + joueur.getItems().get(Items.EGG)
                + " dans votre sac (ça empeste)");
        System.out.println("99. Revenir au menu principal");
        System.out.println("Votre stress est de " + joueur.getStress());
        System.out.println("Choisissez un objet à consommer (entrez son numéro) :");
        switch (sc.nextLine()) {
            case "2":
                if (joueur.consumeItem(Items.COFFEE)) {

                    joueur.substractStress(2);
                    System.out.println("Un bon café... Vous vous sentez plus détendu. Stress -2.");
                    waitToclear();
                }
                accessItems();
                sc.nextLine();
                break;

            case "1":
                if (joueur.consumeItem(Items.PAINKILLERS)) {
                    joueur.substractStress(7);
                    System.out.println("Vous avalez un antidouleur. Stress -7");
                    waitToclear();
                }
                accessItems();
                sc.nextLine();
                break;

            case "3":
                if (joueur.consumeItem(Items.SNOOP)) {

                    int val = nbRandom() - 50;
                    joueur.substractStress(val);
                    if (val < 0) {
                        System.out.println("Vous avez pris un Snoop ? Votre stress a augmenté de " + val * -1);
                    } else {
                        System.out.println("Vous avez pris un Snoop ? Votre stress a augmenté de " + val);
                    }
                    waitToclear();
                }
                accessItems();
                sc.nextLine();
                break;

            case "4":
                if (joueur.consumeItem(Items.CIGARETTE)) {
                    joueur.substractStress(10);
                    System.out.println("Vous fumez un bon coup. Stress -10");
                    waitToclear();
                }
                accessItems();
                sc.nextLine();
                break;

            case "5":
                if (joueur.consumeItem(Items.DIAMOND)) {
                    joueur.substractStress(100);
                    System.out.println("Votre diamant vous rassure. Stress -100");
                    waitToclear();
                }
                accessItems();
                sc.nextLine();
                break;

            case "6":
                if (joueur.consumeItem(Items.PIZZA)) {
                    joueur.substractStress(15);
                    System.out.println("délicieux. Stress -15");
                    waitToclear();
                }
                accessItems();
                sc.nextLine();
                break;

            case "7":
                if (joueur.consumeItem(Items.EGG)) {
                    joueur.addStress(20);
                    System.out.println("Pourquoi? Stress +20");
                    waitToclear();
                }
                accessItems();
                sc.nextLine();
                break;

            case "99":
                afficherMenuPrincipal();
                break;

            default:
                System.out.println("Oups ! Ce choix n’est pas valide.");
                waitToclear();
                accessItems();
                sc.nextLine();
                break;
        }
    }

    public void waitToclear() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void gameOver() {
        if (gameLoseEnd(joueur)) {
            afficherGameOver();
            System.out.println("Souhaitez vous recommencer ?");
            System.out.println("1. Oui");
            System.out.println("2. Non, Quitter le jeu");
            ToolBox.scoreWriter(String.valueOf(machine.getCptTour()), joueur.getNom(),
                    String.valueOf(joueur.getWallet()), String.valueOf(joueur.getStress()), "./res/Scores/scores.txt");
            Scanner sc = new Scanner(System.in);
            String choixUtilisateur = sc.nextLine();
            while (!choixUtilisateur.equals("1") && !choixUtilisateur.equals("2")) {
                System.out.println("Oups ! Ce choix n’est pas valide.");
                choixUtilisateur = sc.nextLine();
            }
            if (choixUtilisateur.equals("1")) {
                joueur.substractStress(joueur.getStress());
                joueur.addWallet(1000);
                afficherMenuPrincipal();
                joueur.setTourMax(this.getCptTourFromMachine());
            } else if (choixUtilisateur.equals("2")) {
                joueur.setTourMax(this.getCptTourFromMachine());
                ToolBox.saveSerialScore(joueur);
                System.exit(0);
            }
            sc.close();

        }
    }

    public boolean gameLoseEnd(Joueur j) {
        if (j.getStress() >= 100) {
            return true;
        }
        if (j.getWallet() <= 0) {
            return true;
        }
        return false;
    }

    public void afficherGameOver() {
        clearScreen();
        File gameover = new File("./res/gameover.txt");
        try (Scanner fileScanner = new Scanner(gameover)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du Game Over.");
        }
    }

    void clearScreen() {
        for (int i = 0; i < 50; ++i) {
            System.out.println();
        }
    }

    private int nbRandom() {
        Random rng = new Random();
        return rng.nextInt(100);
    }

    public int getCptTourFromMachine() {
        return machine.getCptTour();
    }
}