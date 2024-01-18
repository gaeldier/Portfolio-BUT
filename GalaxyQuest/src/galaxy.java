import extensions.CSVFile;
import extensions.File;

class galaxy extends Program {
    //score et random sont glabaux, pour pouvoir bénéficier de la valeur des deux partout
    int score = 0;
    int random(int min, int max){
        return (int) (random() * (max + 1)) + min;
    }

    //Fonction principale
    void algorithm() {
        String choix;

        do {
            File menu = newFile("./ressources/menu.txt");
            while (ready(menu)){
                println(readLine(menu));
            }
                println("Entrez votre nom :");
                choix = readString();
                println("Choisissez un mode de jeu");
                while(!equals(choix,"1")||!equals(choix,"2")||!equals(choix,"3")||!equals(choix,"4")||!equals(choix,"5")){
                    choix = readString();
                if (equals(choix,"1")) {
                    println("Lancement du jeu...");
                    jouer();
                } else if (equals(choix, "2")) {
                    println("Votre score final est : " + score);
                    println("Au revoir !");
                    sauvegarder();
                    return;
                } else if (equals(choix,"3")) {
                    println("Choisissez votre fiche de révision en entrant le nom de la fiche révision");
                    revisions();
                    break;
                }else if (equals(choix, "4")){
                    menuScore();
                }
                else {
                    println("Choix invalide. Veuillez choisir à nouveau.    1-Rejouer 2-Quitter");
                }
                }
        } while (equals(choix,"2"));

    }
    //Fonction du menu de scores, non complet
    void menuScore(){
      File menu = newFile("ressources/menuScores.txt");
        while (ready(menu)){
            println(readLine(menu));
        }
        println("Le dernier score était de : ");
        CSVFile fichierScore = loadCSV("ressources/saved.csv");
        int ligne = rowCount(fichierScore);
        String cell = getCell(fichierScore,ligne,0);
            println(cell+"");
	
    }
 /*   //analyse la saisie du mode révision et cherche le fichier correspondant
    boolean fichier(String saisie, String dossier) {
        boolean res = false;
        String[] fichiers = getAllFilesFromDirectory(dossier);
        for (int idx = 0; idx < length(fichiers); idx++) {
            if (equals(saisie, fichiers[idx])) {
                res = true;
            }
        }
        return res;
    }*/
    
    //Fonction du menu de révisions.
    void revisions(){
        String saisie = readString();
        String[] listefichier =  getAllFilesFromDirectory("ressources/planets/");
        String rev = "ressources/planets/";
        saisie = toLowerCase(saisie);
        do{
        if(equals(saisie, "apollo11") ||equals(saisie, "jupiter") ||equals(saisie, "mars") ||equals(saisie, "mercure") ||equals(saisie, "neptune") ||equals(saisie, "saturne") ||equals(saisie, "ssolaire") || equals(saisie, "terre") ||equals(saisie, "uranus") ||equals(saisie, "venus")){
            saisie = rev + saisie + ".txt";
            File fichier = newFile(saisie);
            while(ready(fichier)){
                println(readLine(fichier));
            }
        }
    }while(equals(saisie,"retour"));
    }

    //Fonction principale du Jeu, load les questions et vérifie la réponse
    int jouer() {
        CSVFile planetes = loadCSV("./ressources/questionrep.csv");
        int ligne = random(0, rowCount(planetes)-1);
                String cell = getCell(planetes,ligne,0);
                    print(cell + "");
                    println();           
                    String reponse = readString();
                    reponse = toUpperCase(reponse);
                    reponse = toLowerCase(reponse);
                    print(getCell(planetes, ligne, 1)); 
                    println(" était la réponse");
                    if (equals(reponse,getCell(planetes, ligne,1))) {
                        println("Bonne réponse !");
                        score += 10;
                        println("Vous gagnez 10 points !    1-Rejouer 2-Quitter" );
                    } else {
                        println("Mauvaise réponse.    1-Rejouer 2-Quitter");
                    }
                    return score;
                    }
    
    //Fonction de Sauvegarde, un peu cassée
    void sauvegarder(){
        getAllFilesFromDirectory("./ressources/");
        CSVFile saved = loadCSV("ressources/saved.csv");
        String[][] scored = new String[rowCount(saved)+1][1];
        for(int i = 0;i<length(score,1);i++){
            scored[i][0] = getCell(saved, i, 0);
        }
        saveCSV(scored, "ressources/saved.csv");
    } 
}
