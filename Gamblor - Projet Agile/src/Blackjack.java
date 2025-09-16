import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Blackjack {

    private static final String[] COULEURS = { "♠", "♥", "♦", "♣" };
    private static final int MISE_MIN = 10;

    private List<Carte> mainJoueur;
    private List<Carte> mainCroupier;
    private Random random;

    public Blackjack() {
        this.mainJoueur = new ArrayList<>();
        this.mainCroupier = new ArrayList<>();
        this.random = new Random();
    }

    private static class Carte {
        TypeCarte type;
        String couleur;

        public Carte(TypeCarte type, String couleur) {
            this.type = type;
            this.couleur = couleur;
        }

        public int getValeurNumerique() {
            return type.getPoids();
        }

        @Override
        public String toString() {
            return type.getNom() + couleur;
        }

    }

    public void jouerBlackjack(Joueur joueur) {
        Scanner sc = new Scanner(System.in);

        clearScreen();
        File bk = new File("res/blackjack.txt");
        try (Scanner fileScanner = new Scanner(bk)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du menu principal.");
        }

        if (joueur.getWallet() < MISE_MIN) {
            System.out.println("Vous n'avez pas assez d'argent pour jouer au blackjack !");
            System.out.println(" Mise minimum : " + MISE_MIN + "€");
            System.out.println("Votre argent : " + joueur.getWallet() + "€");
            System.out.println("Stress : " + joueur.getStress() + "/100");
            sc.nextLine();
            return;
        }

        System.out.println("Votre argent : " + joueur.getWallet() + "€");
        System.out.println("Stress : " + joueur.getStress() + "/100");
        String entry = sc.nextLine();
        while (!entry.equals("P") && !entry.equals("p") && !entry.equals("S") && !entry.equals("s")) {
            System.out.println("Oups ! Ce choix n’est pas valide.");
            entry = sc.nextLine();
        }
        clearScreen();
        int mise = demanderMise(joueur, sc);
        joueur.substractWallet(mise);

        mainJoueur.clear();
        mainCroupier.clear();

        mainJoueur.add(piocherCarte());
        mainCroupier.add(piocherCarte());
        mainJoueur.add(piocherCarte());
        mainCroupier.add(piocherCarte());

        afficherJeu(false);

        if (calculerScore(mainJoueur) == 21) {
            System.out.println(" BLACKJACK ! Vous avez 21 !");
            if (calculerScore(mainCroupier) == 21) {
                System.out.println("Le croupier a aussi 21. Égalité !");
                joueur.addWallet(mise);
            } else {
                int gain = (int) (mise * 2.5);
                System.out.println("Vous gagnez " + gain + "€ !");
                joueur.addWallet(gain);
                joueur.substractStress(5);
            }
        } else {
            boolean joueurContinue = true;
            while (joueurContinue && calculerScore(mainJoueur) < 21) {
                System.out.print("\n[H] (tirer) ou [S] (rester) ? ");
                String choix = sc.nextLine().toUpperCase();

                if (choix.equals("H")) {
                    mainJoueur.add(piocherCarte());
                    afficherJeu(false);

                    if (calculerScore(mainJoueur) > 21) {
                        System.out.println("BUST ! Vous dépassez 21 !");
                        joueurContinue = false;
                    }
                } else if (choix.equals("S")) {
                    joueurContinue = false;
                } else {
                    System.out.println("Choix invalide ! Tapez H ou S.");
                }
            }

            if (calculerScore(mainJoueur) <= 21) {
                System.out.println("\nTour du croupier...");
                afficherJeu(true);

                while (calculerScore(mainCroupier) < 17) {
                    System.out.println("Le croupier tire une carte...");
                    mainCroupier.add(piocherCarte());
                    afficherJeu(true);
                }

                determinerGagnant(joueur, mise);
            } else {
                System.out.println("Vous avez perdu " + mise + "€");
                int stressAjoute = mise / 5 + 3;
                joueur.addStress(stressAjoute);
                System.out.println("Votre stress augmente de " + stressAjoute + " points.");
            }

        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Argent : " + joueur.getWallet() + "€");
        System.out.println("Stress : " + joueur.getStress() + "/100");
        System.out.println();
        System.out.print("Nouvelle Partie ? Appuyez sur P");
        System.out.println("\nAppuyez sur S pour retourner au menu des machines");

        String fin = sc.nextLine().toUpperCase();

        while (!fin.equals("P") && !fin.equals("S")) {
            System.out.println("Oups ! Ce choix n’est pas valide.");
            fin = sc.nextLine();
        }
        if (fin.equals("P")) {
            jouerBlackjack(joueur);
        } else if (fin.equals("S")) {
            ToolBox.saveSerialScore(joueur);
            return;
        }
    }

    public int demanderMise(Joueur joueur, Scanner sc) {
        clearScreen();
        System.out.println("Votre argent : " + joueur.getWallet() + "€");
        System.out.println("Mise minimum : " + MISE_MIN + "€");
        System.out.print("Combien voulez-vous miser ? ");

        try {
            int mise = Integer.parseInt(sc.nextLine());
            if (mise < MISE_MIN) {
                System.out.println("Mise trop faible ! Mise minimum : " + MISE_MIN + "€");
                return demanderMise(joueur, sc);
            } else if (mise > joueur.getWallet()) {
                System.out.println("Vous n'avez pas assez d'argent !");
                return demanderMise(joueur, sc);
            }
            return mise;
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un nombre valide !");
            return demanderMise(joueur, sc);
        }
    }

    public Carte piocherCarte() {
        TypeCarte[] types = TypeCarte.values();
        TypeCarte type = types[random.nextInt(types.length)];
        String couleur = COULEURS[random.nextInt(COULEURS.length)];
        return new Carte(type, couleur);
    }

    public int calculerScore(List<Carte> main) {
        int score = 0;
        int nombreAs = 0;

        for (Carte carte : main) {
            score += carte.getValeurNumerique();
            if (carte.type == TypeCarte.AS) {
                nombreAs++;
            }
        }

        while (score > 21 && nombreAs > 0) {
            score -= 10;
            nombreAs--;
        }

        return score;
    }

    public void afficherJeu(boolean montrerToutCroupier) {

        clearScreen();
        System.out.print("Croupier : ");
        if (montrerToutCroupier) {
            for (Carte carte : mainCroupier) {
                System.out.print("[" + carte + "] ");
            }
            System.out.println("(Total: " + calculerScore(mainCroupier) + ")");
        } else {
            System.out.print("[" + mainCroupier.get(0) + "] [??] ");
            System.out.println("(Total: ?)");
        }

        System.out.println();

        System.out.print("Vous : ");
        for (Carte carte : mainJoueur) {
            System.out.print("[" + carte + "] ");
        }
        System.out.println("(Total: " + calculerScore(mainJoueur) + ")");
    }

    public void determinerGagnant(Joueur joueur, int mise) {
        int scoreJoueur = calculerScore(mainJoueur);
        int scoreCroupier = calculerScore(mainCroupier);

        System.out.println("\nRÉSULTATS :");
        System.out.println("Votre score : " + scoreJoueur);
        System.out.println("Score croupier : " + scoreCroupier);

        if (scoreCroupier > 21) {
            System.out.println("Le croupier bust ! Vous gagnez !");
            int gain = mise * 2;
            joueur.addWallet(gain);
            joueur.substractStress(3);
            File win = new File("res/Win.txt");
            try (Scanner fileScanner = new Scanner(win)) {
                while (fileScanner.hasNextLine()) {
                    System.out.println(fileScanner.nextLine());
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de la lecture du menu principal.");
            }
            System.out.println("Vous gagnez " + gain + "€ !");
        } else if (scoreJoueur > scoreCroupier) {
            System.out.println("Vous gagnez !");
            System.out.println();
            int gain = mise * 2;
            joueur.addWallet(gain);
            joueur.substractStress(3);
            File win = new File("res/Win.txt");
            try (Scanner fileScanner = new Scanner(win)) {
                while (fileScanner.hasNextLine()) {
                    System.out.println(fileScanner.nextLine());
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de la lecture du menu principal.");
            }
            System.out.println("Vous gagnez " + gain + "€ !");
        } else if (scoreJoueur == scoreCroupier) {
            System.out.println("Égalité ! Mise remboursée.");
            joueur.addWallet(mise);
        } else {
            System.out.println("Vous perdez...");
            int stressAjoute = mise / 5 + 2;
            joueur.addStress(stressAjoute);
            System.out.println("Votre stress augmente de " + stressAjoute + " points.");
            System.out.println();
            File lose = new File("res/Lose.txt");
            try (Scanner fileScanner = new Scanner(lose)) {
                while (fileScanner.hasNextLine()) {
                    System.out.println(fileScanner.nextLine());
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de la lecture du menu principal.");
            }
        }
    }

    public void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
