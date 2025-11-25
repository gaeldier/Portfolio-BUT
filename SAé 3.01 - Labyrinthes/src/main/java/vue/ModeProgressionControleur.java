package vue;

import controleur.JeuControleur;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import modele.ModeProgression;

import java.util.Optional;

public class ModeProgressionControleur extends PlateauObserveur{
    @FXML
    @SuppressWarnings("unused")
    public void initialize() {
        this.plateau = new GridPane();
        if(NiveauControleur.niveau == 3) this.vueRestreinte = true;
        else if(NiveauControleur.niveau == 5) this.porteLimite = true;
        else if(NiveauControleur.niveau == 6) this.apparation = true;
        else this.vueRestreinte = false;
        this.champDeVision = 5;
        this.jouer();
    }

    public void update() {
        this.jeuFin();
        if (this.vueRestreinte)
            this.updateAvecRestriction();
        else
            this.updateSansRestriction();
    }

    public void jouer() {
        this.l = new ModeProgression(NiveauControleur.niveau, NiveauControleur.difficulte);
        l.addObserver(this);
        this.controleur = new JeuControleur(l);
        super.jouer();
    }

    private void jeuFin() {
        if (l.aGagner()) {
            this.finish.seek(Duration.ZERO);
            this.finish.play();
            Platform.runLater(() -> {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Fin");
                dialog.setHeaderText("Bravo, vous avez fini le niveau " + NiveauControleur.niveau);
                dialog.setContentText("Voulez vous rééssayer ou quitter");

                ButtonType recommencerButton = new ButtonType("Rééssayer", ButtonBar.ButtonData.OK_DONE);
                ButtonType quitterButton = new ButtonType("Quitter", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(recommencerButton, quitterButton);

                Optional<ButtonType> result = dialog.showAndWait();

                if (result.isPresent() && result.get() == recommencerButton) {
                    this.jouer();
                } else {
                    this.updateScoreJoueur();
                    revenirPagePrecedente();
                }
            });
        }
    }

    private void updateScoreJoueur(){
        NiveauControleur.j.niveauEffectue(NiveauControleur.niveau, NiveauControleur.difficulte);
        int score;
        switch(NiveauControleur.difficulte){
            case "Facile":
                score = 1000 * (this.l.getDistanceEntreeSortie() / nbPasJoueur );
                NiveauControleur.j.setScore(NiveauControleur.niveau, score, NiveauControleur.difficulte);
                break;
            case "Moyen":
                score = (int) (1000 * 1.5) * (this.l.getDistanceEntreeSortie() / nbPasJoueur);
                NiveauControleur.j.setScore(NiveauControleur.niveau, score, NiveauControleur.difficulte);
                break;
            case "Difficile":
                score = (1000 * 2) * (this.l.getDistanceEntreeSortie() / nbPasJoueur );
                NiveauControleur.j.setScore(NiveauControleur.niveau, score, NiveauControleur.difficulte);
                break;
        }
    }

    protected void revenirPagePrecedente() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("niveau.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = (Stage) conteneurPrincipale.getScene().getWindow();

            stage.getScene().setRoot(root);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}