package vue;

import controleur.JeuControleur;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import modele.ModeLibre;
import modele.Orientation;

import java.util.Objects;
import java.util.Optional;

public class JeuLibreControleur extends PlateauObserveur {
    private int nbLigne;
    private int nbColonne;
    private int difficulte;
    private boolean isCanceled = false;
    private String generation;
    private Orientation orientation;


    @SuppressWarnings("unused")
    public void initialize() {
        boolean success = false;
        while (!success) {
            success = afficherConfiguration();
            if (isCanceled) {
                return;
            }
            if (!success) {
                afficherErreur();
            }

            if(generation.equals("parfait")) {
                afficherConfigurationParfait();

                if(isCanceled) return;
            }
        }

        this.plateau = new GridPane();

        this.jouer();
    }

    private boolean afficherConfiguration() {
        Label parfait = new Label("Labyrinthe parfait");
        Label aleatoire = new Label("Labyrinthe aléatoire");
        ToggleButton toggleSwitch = new ToggleButton();
        toggleSwitch.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
        toggleSwitch.getStyleClass().add("toggle-switch");
        toggleSwitch.setText(null);

        StackPane thumb = new StackPane();
        thumb.getStyleClass().add("switch-thumb");
        toggleSwitch.setGraphic(thumb);

        HBox hbox = new HBox(10, aleatoire, toggleSwitch, parfait);

        TextField lignesField = new TextField();
        lignesField.setPromptText("Nombre de lignes");
        TextField colonnesField = new TextField();
        colonnesField.setPromptText("Nombre de colonnes");
        TextField pourcentageField = new TextField();
        pourcentageField.setPromptText("Pourcentage");
        TextField distanceField = new TextField();
        distanceField.setPromptText("Distance entre l'entrée et la sortie");

        VBox box = new VBox(10, hbox, lignesField, colonnesField, pourcentageField);
        toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue){
                box.getChildren().remove(pourcentageField);
                box.getChildren().add(distanceField);
            }else {
                box.getChildren().remove(distanceField);
                box.getChildren().add(pourcentageField);
            }
        });
        box.setPadding(new Insets(10));

        final Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Configuration du labyrinthe");
        dialog.setHeaderText("Veuillez entrer les paramètres du labyrinthe :");
        dialog.getDialogPane().setContent(box);

        dialog.getDialogPane().setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        ButtonType validerButton = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(validerButton, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isEmpty() || result.get() == ButtonType.CANCEL) {
            revenirPagePrecedente();
            isCanceled = true;
            return true;
        }

        try {
            nbLigne = Integer.parseInt(lignesField.getText());
            nbColonne = Integer.parseInt(colonnesField.getText());
            if(toggleSwitch.isSelected()) {
                difficulte = Integer.parseInt(distanceField.getText());
                generation = "parfait";
                return nbLigne > 1 && nbColonne > 1 && difficulte > 0;
            }
            else {
                difficulte = Integer.parseInt(pourcentageField.getText());
                generation = "aleatoire";
                return nbLigne > 1 && nbColonne > 1 && difficulte >= 0 && difficulte <= 100;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void afficherConfigurationParfait() {

        Label bfs = new Label("Parcours en Largeur");
        Label binaryTree = new Label("Arbre Binaire");
        ToggleButton toggleSwitch = new ToggleButton();
        toggleSwitch.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
        toggleSwitch.getStyleClass().add("toggle-switch");
        toggleSwitch.setText(null);

        StackPane thumb = new StackPane();
        thumb.getStyleClass().add("switch-thumb");
        toggleSwitch.setGraphic(thumb);

        HBox hbox = new HBox(10, bfs, toggleSwitch, binaryTree);

        ComboBox<Orientation> orientationComboBox = new ComboBox<>();
        orientationComboBox.getItems().addAll(Orientation.values());
        orientationComboBox.setValue(Orientation.NORD_EST);
        orientationComboBox.setVisible(false);

        toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> orientationComboBox.setVisible(newValue != null && newValue));

        VBox box = new VBox(10, hbox, orientationComboBox);
        box.setPadding(new Insets(10));

        final Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Configuration Labyrinthe Parfait");
        dialog.setHeaderText("Algorithme et options de génération :");
        dialog.getDialogPane().setContent(box);

        ButtonType validerButton = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(validerButton, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isEmpty() || result.get() == ButtonType.CANCEL) {
            revenirPagePrecedente();
            isCanceled = true;
            return;
        }

        generation = toggleSwitch.isSelected() ? "tree" : "bfs";

        if (toggleSwitch.isSelected() && orientationComboBox.getValue() != null) {
            this.orientation = orientationComboBox.getValue();
        } else if (toggleSwitch.isSelected()) {
            this.orientation = Orientation.NORD_EST;
        }
    }

    private void afficherErreur() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Configuration invalide");
        alert.setHeaderText("Erreur de configuration");
        alert.setContentText("Votre configuration est incorrecte. Veuillez réessayer.");
        alert.showAndWait();
    }

    public void jouer() {this.l = new ModeLibre(this.nbLigne, this.nbColonne, this.difficulte, generation, orientation);
        l.addObserver(this);
        this.controleur = new JeuControleur(l);
        super.jouer();
    }
}
