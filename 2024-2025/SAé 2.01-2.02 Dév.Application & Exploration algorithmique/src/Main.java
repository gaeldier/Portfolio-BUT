/*
 * @author Gaël Dierynck
 * @version 1.0
 * 
 * Cette classe sert à démontrer la façon dont nous traitons les incohérences dans une possible nouvelle instance d'Adolescents.
 * Elle servira d'exemple pour les futurs usages et instanciations de la classe Appariement, et donc, d'adolescents.
 */
public class Main {
    
    public static void main(String[] args) {
        // Create an instance of Appariement
        Appariement appariement = new Appariement();

        // On ajoute des adolescents problématiques, l'instanciation d'adolescents doit TOUJOURS être faite avec un try/catch
        try{
            Adolescents adoProblematiqueAnimal = new Adolescents("male", "yes", "yes", "nonuts", "nonuts", "sports", "other", "male", "Doe", "John", "2005-01-01", "FR");
            Adolescents adoProblematiqueCritere = new Adolescents("male", "no", "j'ai un chat", "nonuts", "nonuts", "sports", "other", "male", "Doe", "John", "2005-01-01", "FR");
            Adolescents adoProblematiqueLesDeux = new Adolescents("male", "yes", "yes", "nonuts", "nonuts", "sports", "other", "male", "Doe", "John", "2005-01-01", "FR");
            appariement.addAdolescent(adoProblematiqueAnimal);
            appariement.addAdolescent(adoProblematiqueCritere);
            appariement.addAdolescent(adoProblematiqueLesDeux);
            appariement.incoherencesInAdolescents();
        } catch (InvalidDataException e) {
            System.out.println("Erreur de saisie ! Le critère ne correspond pas");
        }
    
    }
}
