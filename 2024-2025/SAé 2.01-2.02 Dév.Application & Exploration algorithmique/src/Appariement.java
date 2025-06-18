import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Appariement.java
 * 
 * Cette classe gère les adolescents et leurs critères pour le processus
 * d'appariement.
 * Elle permet d'ajouter, de supprimer et de vérifier les critères des
 * adolescents.
 * Elle gère également les incohérences dans les données des adolescents.
 */

public class Appariement {

    /**
     * Liste des adolescents
     */
    private ArrayList<Adolescents> listAdolescents;

    /**
     * Constructeur de la classe Appariement
     * 
     * @param a Liste d'adolescents à ajouter
     */
    public Appariement(ArrayList<Adolescents> a) {
        this.listAdolescents = new ArrayList<>();
        this.listAdolescents = a;
    }

    /**
     * Constructeur par défaut de la classe Appariement
     */
    public Appariement() {
        this(new ArrayList<>());
    }

    /**
     * Ajoute un adolescent à la liste
     * 
     * @param a L'adolescent à ajouter
     */
    public void addAdolescent(Adolescents a) {
        this.listAdolescents.add(a);
    }

    /**
     * Supprime un adolescent à la liste à une position donnée
     * 
     * @param id La position à laquelle ajouter l'adolescent
     */
    public void removeAdolescent(int id) {
        this.listAdolescents.remove(id);
    }

    /**
     * Supprime un adolescent de la liste
     * 
     * @param a L'adolescent à supprimer
     */
    public void removeAdolescent(Adolescents a) {
        this.listAdolescents.remove(a);
    }

    /**
     * Supprime un adolescent de la liste en fonction de son nom et prénom
     * 
     * @param nom    Le nom de l'adolescent à supprimer
     * @param prenom Le prénom de l'adolescent à supprimer
     */
    public void removeAdolescent(String nom, String prenom) {
        for (Adolescents ado : listAdolescents) {
            if (ado.getNom().equals(nom) && ado.getPrenom().equals(prenom)) {
                listAdolescents.remove(ado);
            }
        }
    }

    /**
     * Ajoute une liste d'adolescents à la liste
     * 
     * @param a La liste d'adolescents à ajouter
     */
    public void addAdolescent(ArrayList<Adolescents> a) {
        this.listAdolescents.addAll(a);
    }

    /**
     * Récupère la liste d'adolescents
     * 
     * @return La liste d'adolescents
     */
    public int size() {
        return listAdolescents.size();
    }

    /**
     * Récupère la taille maximale du tableau d'adolescents
     * 
     * @return La taille maximale du tableau
     */
    public int sizeDansTableauMAX() {
        return this.listAdolescents.size() * this.listAdolescents.size();
    }

    public String getCritere(int i, String crit) {
        return listAdolescents.get(i).getValeurCritere(Critere.valueOf(crit));
    }

    /**
     * Modifie un critère d'un adolescent
     * 
     * @param i    L'indice de l'adolescent dans la liste
     * @param crit Le critère à modifier
     */
    public void removeCritere(int i, String crit) {
        listAdolescents.get(i).setCritereToNull(null);
        System.out.println("Critere [" + crit + "] supprimé de " + listAdolescents.get(i).getNom());
    }

    /**
     * Supprime un certain nombre d'adolescents de la liste
     * 
     * @param nb Le nombre d'adolescents à supprimer
     */
    public void removeAdolescents(int nb) {
        for (int i = 0; i < nb; i++) {
            listAdolescents.remove(i);
        }
    }

    /**
     * Vérifie les incohérences dans les données des adolescents
     * 
     * @throws InvalidDataException Si une incohérence est trouvée
     */
    public void incoherencesInAdolescents() throws InvalidDataException {
        for (Adolescents ado : listAdolescents) {
            boolean premiereAlerte = ado.getValeurCritere(Critere.GUEST_ANIMAL_ALLERGY).equals("yes")
                    && ado.getValeurCritere(Critere.HOST_HAS_ANIMAL).equals("yes");
            String result1 = premiereAlerte ? "Incohérence dans les données de l'adolescent " + ado.getNom()
                    : "Données valides !";
            System.out.println(result1);
            listAdolescents.remove(ado);
            ado.critereInvalide();
        }
    }

    public ArrayList<Adolescents> getAl() {
        return listAdolescents;
    }

    public String toString() {
        return "Adolescents :" + listAdolescents.toString();
    }

    /**
     * Récupère l'adolescent suivant dans la liste
     * 
     * @param listeAdos La liste d'adolescents
     * @param debut     L'adolescent de départ
     * @return L'adolescent suivant
     */
    public Adolescents getNextAdolescent(ArrayList<Adolescents> listeAdos, Adolescents debut) {
        int index = listeAdos.indexOf(debut);
        if ((index + 1) > listeAdos.lastIndexOf(listeAdos.getLast())) {
            Adolescents first = listeAdos.get(0);
            Adolescents last = listeAdos.get(listeAdos.size() - 1);
            listeAdos.set(0, last);
            listeAdos.set(listeAdos.size() - 1, first);
            return getNextAdolescent(listeAdos, debut);
        } else {
            return listeAdos.get(index + 1);
        }
    }

    /**
     * Récupère une paire d'adolescents pour l'appariement
     * 
     * @return Une HashMap contenant les paires d'adolescents
     */
    public HashMap<Adolescents, Adolescents> getHashMapPair() {
        HashMap<Adolescents, Adolescents> paireAdo = new HashMap<>();

        for(int i = 0; i <this.listAdolescents.size();++i) {
            for(int j = 1; j<this.listAdolescents.size();++j) {
                paireAdo.put(listAdolescents.get(i), listAdolescents.get(j));
            }
        }

        /*for (Adolescents ado : listAdolescents) {
            Adolescents tmp = getNextAdolescent(listAdolescents, ado);
            paireAdo.put(ado, tmp);
            System.out.println("Paire d'adolescents : " + ado.toString() + " avec " + tmp.toString()); // Debug
        }*/
        System.out.println("\n");
        return paireAdo;
    }

    /**
     * Récupère les adolescents appariés avec leurs scores
     *
     * @param paireAdo La HashMap contenant les paires d'adolescents
     * @return Un tableau d'AdolescentsArray contenant les paires d'adolescents et
     *         leurs scores
     */
    public AdolescentsArray[] appariementTotal(HashMap<Adolescents, Adolescents> paireAdo) throws IOException{
        AdolescentsArray[] res = new AdolescentsArray[listAdolescents.size()];
    
        for (int i = 0; i < res.length; ++i) {
            for (HashMap.Entry<Adolescents, Adolescents> entry : paireAdo.entrySet()) {
                // for (int i = 0; i < res.length; ++i) {
                HashMap<Adolescents, Adolescents> hm = new HashMap<>();
                hm.put(entry.getKey(), entry.getValue());
                Arrays.fill(res, new AdolescentsArray(hm, entry.getKey().getScorePourAppariement(entry.getValue())));
                Serializer.serialize(res[i], "./res/config/config.class");
            }
        }
        return res;
    }


    /**
     * 
     * Cette méthode statique sert à regarder si un fichier de configuration existe déjà à un endroit donné.
     * Si c'est le cas, elle le charge, sinon, elle lève une {@link IOException}
     * 
     * @param path Le chemin donné 
     * @return L'objet {@link AdolescentsArray} déserialisé au chemin donné
     * @throws IOException
     */
    public static AdolescentsArray automaticLoader(String path) throws IOException{
        File binaryFile = new File(path);

        if (!binaryFile.exists() || !binaryFile.isFile()) {
            throw new IOException("Binary file not found at: " + path + ",\n Création d'un nouveau fichier après la validité de la configuration");
        } else {
            return DeSerializer.DeSerialize(path);
        }
    }
}
