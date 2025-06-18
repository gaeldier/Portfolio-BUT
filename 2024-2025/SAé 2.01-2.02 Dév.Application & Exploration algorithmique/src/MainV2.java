import java.io.IOException;
import java.util.ArrayList;

public class MainV2 {
        public static void main(String[] args) {
                ArrayList<Adolescents> listAdolescents = new ArrayList<>();
                Appariement ap = new Appariement(listAdolescents);
                Adolescents adoH1 = new Adolescents("male", "no", "yes", "nonuts", "nonuts", "sports", "other", "male",
                                "Vanhoutte", "Amaury", "2005-01-01", "FR");
                Adolescents adoG1 = new Adolescents("female", "no", "no", "nonuts", "nonuts", "music", "other",
                                "female",
                                "Dierynck", "Gaël", "2006-02-02", "GE");
                Adolescents adoH2 = new Adolescents("male", "no", "no", "nonuts", "nonuts", "sports", "other", "male",
                                "Harlaut", "Romain", "2005-01-01", "FR");
                Adolescents adoH5 = new Adolescents("male", "no", "no", "nonuts", "nonuts", "sports", "other", "male",
                                "Sanders", "Colonel", "2005-01-01", "FR");

                listAdolescents.add(adoG1);
                listAdolescents.add(adoH1);
                listAdolescents.add(adoH2);
                listAdolescents.add(adoH5);
                ap = new Appariement(listAdolescents);
                try {

                        AdolescentsArray[] aa = new AdolescentsArray[ap.sizeDansTableauMAX()];
                        aa = ap.appariementTotal(ap.getHashMapPair());
                        for (int i = 0; i < aa.length; ++i) {
                                System.out.println(aa[i].toString()); 
                        }

                        AdolescentsArray[] aa2 = new AdolescentsArray[ap.sizeDansTableauMAX()];
                        for (int i = 0; i < aa2.length; i++) {
                                aa2[i] = new AdolescentsArray(ap.appariementTotal(ap.getHashMapPair()));
                                aa[1].removeSelfPairing(aa[1]);
                                System.out.println(aa2[i]);

                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
