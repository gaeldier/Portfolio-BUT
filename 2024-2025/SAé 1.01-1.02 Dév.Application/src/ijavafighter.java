import extensions.File;
import extensions.CSVFile;

class ijavaFighter extends Program {

    // Structure du programme :
    /*
     * Constructeurs
     * Fonctions principales
     * Fonctions de jeu
     * Fonctions de calcul
     * Fonctions du mode 1v1 (Peut contenir des bugs)
     * Tests
     */
    /*  Un bug peut survenir lors du choix de la difficulté, en fonction de plusieurs circonstances de jeu, le choix 1 ou 4 peut compter comme une erreur ou non.
          Le patch est programmé et le bug ne devrait pas apparaître le jour de l'accueil des collégiens.*/
          
    // _______________________________________________________________________________________________________________________
    // Constructeur Personnage
    Personnage personnage(int vie, String nom, boolean chevalier, boolean ninja, boolean hardcore) {
        Personnage p = new Personnage();
        p.vie = vie;
        p.nom = nom;
        p.chevalier = chevalier;
        p.ninja = ninja;
        p.hardcore = hardcore;
        return p;
    }

    Personnage joueur;
    Personnage joueur2;
    Personnage adversaire;
    int niveau;
    int reponse;
    int input;
    boolean finished;

    // _______________________________________________________________________________________________________________________
    // Fonctions principales
    void algorithm() {
        menuPrincipal();
        // enableKeyTypedInConsole(true);
    }

    void menuPrincipal() {
        // enableKeyTypedInConsole(true);
        String choixUtilisateur = "";
        clearScreen();
        File menuPrincipal1 = newFile("ressources/menus/menuPrincipal1.txt");
        while (ready(menuPrincipal1)) {
            println(readLine(menuPrincipal1));
        }
        do {
            choixUtilisateur = readString();
            if (equals(choixUtilisateur, "1")) {
                jouer();
            } else if (equals(choixUtilisateur, "2")) {
                afficherScores();
            } else if (equals(choixUtilisateur, "3")) {
                println("A bientôt !");
                System.exit(0);
            } else {
                println("Saisie incorrecte");
            }

        } while (!equals(choixUtilisateur, "1") && !equals(choixUtilisateur, "2") && !equals(choixUtilisateur, "3"));
    }

    void jouer() {
        clearScreen();
        File menuJouer1 = newFile("ressources/menus/menuJouer1.txt");
        while (ready(menuJouer1)) {
            println(readLine(menuJouer1));
        }
        String choixUtilisateur = "";
        do {
            choixUtilisateur = readString();
            if (equals(choixUtilisateur, "1")) {
                tourDeJeu();
            } else if (equals(choixUtilisateur, "3")) {
                regles();
            } else if (equals(choixUtilisateur, "4")) {
                menuPrincipal();
            } else if (equals(choixUtilisateur, "2")) {
                joueurVSjoueur();
            } else {
                println("Saisie incorrecte");
            }
        } while (!equals(choixUtilisateur, "1") && !equals(choixUtilisateur, "2") && !equals(choixUtilisateur, "3")
                && !equals(choixUtilisateur, "4"));
    }

    void regles() {
        clearScreen();
        File regles = newFile("ressources/menus/menuRegles1.txt");
        while (ready(regles)) {
            println(readLine(regles));
        }
        String choixUtilisateur = "";
        do {
            choixUtilisateur = readString();
            if (equals(choixUtilisateur, "1")) {
                jouer();
            } else {
                println("Saisie incorrecte");
            }
        } while (!equals(choixUtilisateur, "1"));
    }

    void afficherScores() {
        clearScreen();
        File scores = newFile("ressources/menus/menuScores.txt");
        while (ready(scores)) {
            println(readLine(scores));
        }
        CSVFile fichier = loadCSV("ressources/scores.csv");
        // Si le fichier est vide, affiche un message indiquant qu'aucun score n'est
        // enregistré
        if (rowCount(fichier) == 0) {
            println("| Aucun score enregistré                                              |");
            // Si le fichier contient quelque chose, affiche tous les scores
        } else {
            for (int indice = 0; indice < rowCount(fichier); indice++) {
                println("| " + getCell(fichier, indice, 0) + " | " + getCell(fichier, indice, 1) + " |\n");
            }
        }
        println("");
        println("Appuyez sur 1 pour revenir au menu principal");
        String choixUtilisateur = "";
        do {
            choixUtilisateur = readString();
            if (equals(choixUtilisateur, "1")) {
                menuPrincipal();
            } else {
                println("Saisie incorrecte");
            }
        } while (!equals(choixUtilisateur, "1"));
    }

    void save(String pseudo, String destination) {
        CSVFile fichier = loadCSV("ressources/scores.csv");
        String[][] scores = new String[rowCount(fichier) + 1][2];
        String points = "" + (niveau - 1);
        for (int indice = 0; indice < rowCount(fichier); indice++) {
            scores[indice][0] = getCell(fichier, indice, 0); // Pseudo
            scores[indice][1] = getCell(fichier, indice, 1); // Score
        }
        scores[rowCount(fichier)][0] = pseudo;
        scores[rowCount(fichier)][1] = points;
        saveCSV(scores, destination);
    }

    /**
     * Cette méthode représente un tour de jeu.
     * Elle initialise le jeu, gère la boucle principale du jeu et traite les
     * actions du joueur et de l'adversaire.
     * Le jeu continue jusqu'à ce que les points de vie du joueur ou de l'adversaire
     * atteignent zéro.
     * 
     * La méthode effectue les étapes suivantes :
     * 1. Efface l'écran et initialise le jeu.
     * 2. Affiche le nom du joueur et commence le jeu.
     * 3. Entre dans une boucle où le joueur et l'adversaire attaquent chacun leur
     * tour.
     * 4. Affiche les barres de vie et invite le joueur à choisir un niveau de
     * difficulté.
     * 5. Pose une question en fonction du niveau de difficulté choisi.
     * 6. Vérifie la réponse du joueur et calcule les dégâts à l'adversaire si la
     * réponse est correcte.
     * 7. Détermine aléatoirement si l'adversaire attaque le joueur et calcule les
     * dégâts.
     * 8. Vérifie si le jeu est terminé en vérifiant les points de vie du joueur et
     * de l'adversaire.
     * 9. Termine le jeu et affiche le score final si le jeu est terminé.
     * Cette javadoc de fonction est peut être obsolète
     */
    void tourDeJeu() {
        clearScreen();
        int score = 0;
        initialiserJeu();
        File affichage2 = newFile("ressources/jeu/affichage2.txt");
        while (ready(affichage2)) {
            println(readLine(affichage2));
        }
        String choixUtilisateur = "";
        do {
            choixUtilisateur = readString();
            if (equals(choixUtilisateur, "1")) {
                adversaire = personnage(100, "Adversaire", false, false, false);
                adversaire.vie = 100 + (niveau - 1) * 10; // Augmente les points de vie de l'adversaire à chaque niveau
            } else if (equals(choixUtilisateur, "2")) {
                adversaire = personnage(170, "Adversaire Hardcore", false, false, true);
                adversaire.vie = 180 + (niveau - 1) * 10;
            } else {
                println("Saisie incorrecte");
            }
        } while (!equals(choixUtilisateur, "1") && !equals(choixUtilisateur, "2"));

        int niveauDifficulte;
        String pseudo = joueur.nom;
        println("La partie va commencer " + pseudo + " !");
        delay(2000);

        while (joueur.vie > 0) {

            clearScreen();
            File jouer3 = newFile("ressources/jeu/jouer3.txt");
            while (ready(jouer3)) {
                println(readLine(jouer3));
            }
            afficherBarresDeVie();
            delay(2500); // le wait est là pour que le joueur puisse voir les barres de vie
            clearScreen();
            File jouer2 = newFile("ressources/jeu/jouer2.txt");
            while (ready(jouer2)) {
                println(readLine(jouer2));
            }
            do {
                niveauDifficulte = readInt();
            } while (niveauDifficulte < 1 || niveauDifficulte > 4);
            clearScreen();
            println("Répondez à la question pour infliger des dégâts à l'adversaire.");
            choixDifficulte(niveauDifficulte);
            int degats;
            boolean ninja = joueur.ninja;
            boolean fin = false;
            boolean reponseCorrecte = verifierReponse(reponse, input);

            if (reponseCorrecte) {
                if (ninja) {
                    degats = calculerDegats(niveauDifficulte, reponseCorrecte) + 10; // Ajoute 10 dégâts supplémentaires
                                                                                     // si le joueur est un ninja
                } else {
                    degats = calculerDegats(niveauDifficulte, reponseCorrecte);
                }
                adversaire.vie -= degats;
                println("Vous avez infligé " + degats + " dégâts à l'adversaire.");
                delay(2500);
                if (adversaire.vie <= 0) {
                    fin = true;
                }
            } else {
                degats = 0;
                println("Réponse incorrecte. Vous n'infligez aucun dégât.");
                delay(2500);
            }
            boolean hardcore = adversaire.hardcore;
            if (!fin) {
                if (hardcore) {
                    if (random() < 0.5) {
                        int degatsAdversaire = 20 + (niveau - 1) * 5; // Augmente les dégâts de l'adversaire à chaque
                                                                      // niveau
                        joueur.vie -= degatsAdversaire;
                        println("L'adversaire vous a infligé " + degatsAdversaire + " dégâts.");
                        delay(2500);
                    } else {
                        println("L'adversaire a raté son attaque.");
                        delay(2500);
                    }
                } else if (random() < 0.4) { // Probabilité de 40% de faire des degats

                    int degatsAdversaire = 15 + (niveau - 1) * 5; // Augmente les dégâts de l'adversaire à chaque niveau
                    joueur.vie -= degatsAdversaire;
                    println("L'adversaire vous a infligé " + degatsAdversaire + " dégâts.");
                    delay(2500);
                } else {
                    println("L'adversaire a raté son attaque.");
                    delay(2500);
                }
            }

            String message = finDeManche(pseudo, score);
            println(message);
            delay(2500);
        }
    }

    // _______________________________________________________________________________________________________________________
    // Fonctions de jeu
    void initialiserAdversaire() {

        adversaire = personnage(120, "Adversaire", false, false, true);
        adversaire.vie = 120 + (niveau - 1) * 10; // Augmente les points de vie de l'adversaire à chaque niveau

    }

    void initialiserJeu() {
        File affichage1 = newFile("ressources/jeu/affichage1.txt");
        while (ready(affichage1)) {
            println(readLine(affichage1));
        }
        String choixUtilisateur = "";
        do {
            choixUtilisateur = readString();
            if (equals(choixUtilisateur, "1")) {
                joueur = personnage(150, choixPseudo(), true, false, false);
            } else if (equals(choixUtilisateur, "2")) {
                joueur = personnage(100, choixPseudo(), false, true, false);
            } else {
                println("Saisie incorrecte");
            }
        } while (!equals(choixUtilisateur, "1") && !equals(choixUtilisateur, "2"));

        niveau++;
        initialiserAdversaire();
    }

    void afficherBarresDeVie() {
        boolean chevalier = joueur.chevalier;
        if (chevalier) {
            joueur.vie = 150;
        }
        println(" _____________________");
        println("   || Niveau : " + niveau);
        println("   || " + joueur.nom + " : " + joueur.vie + " HP");
        println("   || VS");
        println("   || " + adversaire.nom + " : " + adversaire.vie + " HP");
        println("   || wait ...");
        println(" _____________________");

    }

    int calculerDegats(int niveauDifficulte, boolean res) {
        int baseDegats = 10;
        int degats = baseDegats + (niveau - 1) * 5; // Augmente les dégâts en fonction du niveau
        if (niveauDifficulte == 2) {
            degats += 5; // Ajoute 5 dégâts supplémentaires si le niveau de difficulté est 2
        }
        if (niveauDifficulte == 3) {
            degats += 10; // Ajoute 10 dégâts supplémentaires si le niveau de difficulté est 3
        }
        if (niveauDifficulte == 4) {
            degats += 15; // Ajoute 15 dégâts supplémentaires si le niveau de difficulté est 4
        }
        if (res) {
            degats *= 2; // Double les dégâts si la réponse est correcte et en moins de 5 secondes
        }
        return degats;
    }

    boolean tempsCritiques(long timeElapsed) {
        boolean critique = false;
        if (timeElapsed < 5000) {
            critique = true;
        } else {
            critique = false;
        }
        return critique;
    }

    // Permet à l'utilisateur de choisir son pseudo
    String choixPseudo() {
        clearScreen();
        File jouer1 = newFile("ressources/jeu/jouer1.txt");
        while (ready(jouer1)) {
            println(readLine(jouer1));
        }
        String pseudo = readString();
        return pseudo;
    }

    boolean verifierReponse(int reponseAttendue, int reponseUtilisateur) {
        boolean res = false;
        if (reponseAttendue == reponseUtilisateur) {
            res = true;
        }
        return res;
    }

    String choixDifficulte(int niveauDifficulte) {
        String difficulte = "";
        if (niveauDifficulte == 1) {
            additions();

        } else if (niveauDifficulte == 2) {
            double rand = random();
            int randInt = (int) (rand * 2);
            if (randInt == 0) {
                soustractions();
            } else {
                additions();
            }

        } else if (niveauDifficulte == 3) {
            double rand = random();
            int randInt = (int) (rand * 2);
            if (randInt % 2 == 0) {
                multiplications();
            } else if (randInt % 2 != 0) {
                soustractions();
            } else if (randInt % 2 == 1) {
                additions();
            }

        } else if (niveauDifficulte == 4) {
            double rand = random();
            int randInt = (int) (rand * 3);
            if (randInt == 0) {
                divisions();
            } else if (randInt == 1) {
                multiplications();
            } else if (randInt == 2) {
                soustractions();
            } else if (randInt == 3) {
                additions();
            }
        } else {
            difficulte = "Niveau de difficulté incorrect";
        }
        return difficulte;
    }

    String finDeManche(String pseudo, int score) {
        String message = "";
        if (adversaire.vie <= 0) {
            clearScreen();
            println("Vous avez vaincu l'adversaire !");
            score++;
            println("Vous avez actuellement " + score + " points");
            niveau++;
            initialiserAdversaire();
            println("Bienvenue au niveau " + niveau + " !");
            delay(2500);
            save(pseudo, "ressources/scores.csv");
        } else if (joueur.vie <= 0) {
            clearScreen();
            println("Vous avez été vaincu par l'adversaire !");
            println("Votre score final est de " + score + " points");
            delay(3500);
            save(pseudo, "ressources/scores.csv");
            menuPrincipal();
        }
        return message;
    }

    // _______________________________________________________________________________________________________________________
    // Fonctions de calcul
    // Exemple de décomposition pour une seule fonction de calcul
    void additions() {
        // Génération du calcul
        boolean res = false;
        double random1 = random();
        double random2 = random();
        int randInt = (int) (random1 * 10);
        int randInt2 = (int) (random2 * 10);
        // Affichage
        String question = "Combien font" + randInt + "+" + randInt2 + " ?";
        println(question);
        // La réponse du calcul passe en variable globale reponse
        int reponseAttendue = randInt + randInt2;
        reponse = reponseAttendue;
        long startTime = System.currentTimeMillis();
        int reponseUtilisateur = readInt();
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        input = reponseUtilisateur;
        // On vérifie la reponse avec la fonction verifierReponse
        if (verifierReponse(reponseAttendue, reponseUtilisateur)) {
            println("Bravo !");
            if (tempsCritiques(timeElapsed)) {
                res = true;
                println("Critique !");
            }
        } else {
            println("Mauvaise réponse !");
        }
    }

    void soustractions() {
        boolean res = false;
        double random = random();
        double random2 = random();
        int randInt = (int) (random * 10);
        int randInt2 = (int) (random2 * 10);
        String question = "Combien font" + randInt + "-" + randInt2 + " ?";
        println(question);
        int reponseAttendue = randInt - randInt2;
        reponse = reponseAttendue;
        long startTime = System.currentTimeMillis();
        int reponseUtilisateur = readInt();
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        input = reponseUtilisateur;
        if (verifierReponse(reponse, reponseUtilisateur)) {
            println("Bravo !");
            if (tempsCritiques(timeElapsed)) {
                res = true;
                println("Critique !");
            }
        } else {
            println("Mauvaise réponse !");
        }
    }

    void multiplications() {
        boolean res = false;
        double random = random();
        double random2 = random();
        int randInt = (int) (random * 10);
        int randInt2 = (int) (random2 * 10);
        String question = "Combien font" + randInt + "*" + randInt2 + " ?";
        println(question);
        int reponseAttendue = randInt * randInt2;
        reponse = reponseAttendue;
        long startTime = System.currentTimeMillis();
        int reponseUtilisateur = readInt();
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        input = reponseUtilisateur;
        if (verifierReponse(reponse, reponseUtilisateur)) {
            println("Bravo !");
            if (tempsCritiques(timeElapsed)) {
                res = true;
                println("Critique !");
            }
        } else {
            println("Mauvaise réponse !");
        }
    }

    void divisions() {
        boolean res = false;
        double random = random();
        double random2 = random();
        int randInt = (int) (random * 10);
        int randInt2 = (int) (random2 * 10);
        if (randInt2 == 0) {
            randInt2 = 1;
        }
        String question = "Combien font" + randInt + "/" + randInt2 + " ?";
        println(question);
        int reponseAttendue = randInt / randInt2;
        reponse = reponseAttendue;
        long startTime = System.currentTimeMillis();
        int reponseUtilisateur = readInt();
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        input = reponseUtilisateur;
        if (verifierReponse(reponse, reponseUtilisateur)) {
            println("Bravo !");
            if (tempsCritiques(timeElapsed)) {
                res = true;
                println("Critique !");
            }
        } else {
            println("Mauvaise réponse !");
        }
    }

    // _______________________________________________________________________________________________________________________
    // Fonctions du mode 1v1
    void initialiserArene() {
        String choixUser1 = "";
        String choixUser2 = "";
        println("Joueur 1, entrez votre pseudo");
        String pseudo1 = choixPseudo();
        println("Joueur 2, entrez votre pseudo");
        String pseudo2 = choixPseudo();

        do {
            println("Joueur 1, choisissez votre personnage : Chevalier (1) ou Ninja (2)");
            choixUser1 = readString();
            println("Joueur 2, choisissez votre personnage : Chevalier (1) ou Ninja (2)");
            choixUser2 = readString();
            if (equals(choixUser1, "1") || equals(choixUser2, "1")) {
                joueur = personnage(150, pseudo1, true, false, false);
                joueur2 = personnage(150, pseudo2, true, false, false);
            } else if (equals(choixUser1, "2") || equals(choixUser2, "2")) {
                joueur = personnage(100, pseudo2, false, true, false);
                joueur2 = personnage(100, pseudo2, false, true, false);
            } else {
                println("Saisie incorrecte");
            }
        } while (!equals(choixUser1, "1") && !equals(choixUser1, "2") && !equals(choixUser2, "1")
                && !equals(choixUser2, "2"));
    }

    void joueurVSjoueur() {
        clearScreen();
        initialiserArene();
        println("Préparez vous, guerriers !");
        delay(1500);
        int[] tours = new int[] { 0, 1, 2, 3, 4, 5 };
        while (joueur.vie > 0 && joueur2.vie > 0) {
            for (int i = 0; i < length(tours); ++i) {
                afficherVie1V1(joueur, joueur2);
                delay(2500);
                if (joueur.vie <= 0 || joueur2.vie <= 0) {
                    finDePartie(joueur, joueur2);
                } else if (estPair(i)) {
                    round(joueur);
                } else if (!estPair(i)) {
                    round(joueur2);
                }
            }
        }
    }

    boolean estPair(int n) {
        boolean res = false;
        if (n % 2 == 0) {
            res = true;
        }
        return res;
    }

    boolean inputCorrect(String input) {
        boolean res = false;
        if (equals(input,"1") || equals(input,"2") || equals(input,"3") || equals(input,"4")) {
            res = true;
        }
        for (char a = 'A'; a <= 'z'; a++) {
            if (equals(input, String.valueOf(a))) {
                res = false;
            }
        }
        return res;
    }

    void round(Personnage joueur) {
        println(joueur.nom + ", Choisissez un niveau de difficulté entre 1 et 4");
        String niveauDifficulte = "";
        do {
            niveauDifficulte = readString();
            if (!inputCorrect(niveauDifficulte)) {
                println("Niveau de difficulté incorrect, veuillez réessayer");
                niveauDifficulte = readString();
            }
            choixDifficulte(stringToInt(niveauDifficulte));
        } while (inputCorrect(niveauDifficulte));
        int degats;
        boolean bonneReponse = verifierReponse(reponse, input);
        if (bonneReponse) {
            degats = calculerDegats(stringToInt(niveauDifficulte), bonneReponse);
            joueur2.vie -= degats;
            println("Vous avez infligé " + degats + " dégâts à l'adversaire.");
            delay(2500);
        } else {
            degats = 0;
            println("Réponse incorrecte. Vous n'infligez aucun dégât.");
            delay(2500);
        }
    }

    void finDePartie(Personnage joueur, Personnage joueur2) {
        if (joueur.vie < joueur2.vie) {
            println(joueur2.nom + " a gagné !, il termine avec " + joueur2.vie + " points de vie");
        } else if (joueur.vie > joueur2.vie) {
            println(joueur.nom + " a gagné !, il termine avec " + joueur.vie + " points de vie");
        } else {
            println("Egalité !, vous terminez tous les deux avec " + joueur.vie + " points de vie");
        }
        delay(4000);
        menuPrincipal();
    }

    void afficherVie1V1(Personnage joueur, Personnage joueur2) {
        println(" _____________________");
        println("   || " + joueur.nom + " : " + joueur.vie + " HP");
        println("   || VS");
        println("   || " + joueur2.nom + " : " + joueur2.vie + " HP");
        println("   || wait ...");
        println(" _____________________");
    }

    // _______________________________________________________________________________________________________________________
    // Miscellanées
    void keyTypedInConsole(char key) {
        switch (key) {
            case 'q':
                println("J'espère que vous avez bien sauvegardé ! ");
                System.exit(0);
        }
    }

    // _______________________________________________________________________________________________________________________
    // Tests des vérifications de réponse
    void testVerifierReponse() {
        additions();
        assertEquals(true, verifierReponse(reponse, input));
        additions();
        assertEquals(false, verifierReponse(reponse, input));
        soustractions();
        assertEquals(true, verifierReponse(reponse, input));
        soustractions();
        assertEquals(false, verifierReponse(reponse, input));
    }

    // Ce test vérifie si le critique en fonction du temps de réponse fonctionne
    void testTempsCritiques() {
        assertTrue(tempsCritiques(3453));
        assertFalse(tempsCritiques(5100));
    }

    void testInputCorrect() {
        assertTrue(inputCorrect("1"));
        assertTrue(inputCorrect("2"));
        assertTrue(inputCorrect("3"));
        assertTrue(inputCorrect("4"));
        assertFalse(inputCorrect("5"));
        assertFalse(inputCorrect("a"));
        assertFalse(inputCorrect("Z"));
    }
}