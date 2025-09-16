import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class Roulette extends Machine {
    private int[] rouletteNumberResult = new int[1];

    public int greatNbRandom() {
        Random rng = new Random();
        return rng.nextInt(37);
    }

    public int[] rouletteResult() {
        this.rouletteNumberResult[0] = greatNbRandom();
        return this.rouletteNumberResult;
    }

    public int[] getRouletteResult() {
        return this.rouletteNumberResult;
    }

    public void afficherResultatRoulette(int[] resultatCouleur, int[] resultatNb){
        String res = "";
        if(resultatCouleur[0] == 0) {
            res = "La boule est tombée sur \u001B[31m Rouge\u001B[0m " + resultatNb[0];
        }
        if(resultatCouleur[0] == 1) {
            res = "La boule est tombée sur Noir" + resultatNb[0];
        }
        if(resultatCouleur[0] == 2) {
            res = "La boule est tombée sur \u001B[32m Vert\u001B[0m " + resultatNb[0];
        }
        System.out.println(res);
    }

    public void handleResultRoulette(int pari, int[] resultatCouleur, int[] resultatNb, String modePari, String inputJoueurCouleur, String inputJoueurNb, Joueur joueur) {
        if ((modePari.equals("1") && resultatCouleur[0] == Integer.parseInt(inputJoueurCouleur)) || (modePari.equals("2") && resultatNb[0] == Integer.parseInt(inputJoueurNb)) || (modePari.equals("3") && resultatCouleur[0] == Integer.parseInt(inputJoueurCouleur) && resultatNb[0] == Integer.parseInt(inputJoueurNb))) {
            File win = new File("res/Win.txt");
            try (Scanner fileScanner = new Scanner(win)) {
                while (fileScanner.hasNextLine()) {
                    System.out.println(fileScanner.nextLine());
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de la lecture du menu principal.");
            }
            joueur.substractStress(20);
            joueur.addWallet(pari*10);
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
            joueur.addStress(20);
            afficherStats(joueur);
        }
    }


}
