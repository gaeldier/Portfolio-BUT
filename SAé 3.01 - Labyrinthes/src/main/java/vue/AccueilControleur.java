package vue;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import modele.Joueur;
import modele.ListeJoueursSerializable;
import modele.ModeProgression;
import modele.ToolBox;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class AccueilControleur {
    protected static String nomJoueur;
    protected static ListeJoueursSerializable listeJoueur;
    public final static String FILEPATH = "res/listJoueur";
    @FXML
    private AnchorPane root;

    public void initialize() {
        File fichierListeJoueurs = new File(FILEPATH);

        if (!fichierListeJoueurs.exists()) {

            File parentDir = fichierListeJoueurs.getParentFile();

            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    System.err.println("ERREUR FATALE: Impossible de créer le répertoire: " + parentDir.getAbsolutePath());
                }
            }
            listeJoueur = new ListeJoueursSerializable();
        }else {
            listeJoueur = ToolBox.DeSerialize(FILEPATH);
        }
        Media media = new Media(
                Objects.requireNonNull(getClass().getResource("/videos/exampleLabyrinth.mp4")).toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        MediaView mediaView = new MediaView(player);

        mediaView.fitWidthProperty().bind(root.widthProperty());
        mediaView.fitHeightProperty().bind(root.heightProperty());
        mediaView.setPreserveRatio(false);

        root.getChildren().add(0, mediaView);

        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
    }

    @FXML
    private void enlargeText(MouseEvent e) {
        Text txt = (Text) e.getSource();
        txt.setText("> " + txt.getText());
        ScaleTransition st = new ScaleTransition(Duration.millis(200), txt);
        st.setToX(1.2);
        st.setToY(1.2);
        st.play();
    }

    @FXML
    private void resetText(MouseEvent e) {
        Text txt = (Text) e.getSource();
        txt.setText(txt.getText().substring(2));
        ScaleTransition st = new ScaleTransition(Duration.millis(200), txt);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    @FXML
    private void allerVersJeuLibre(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vue/plateau.fxml"));
        JeuLibreControleur controleur1 = new JeuLibreControleur();
        fxmlLoader.setController(controleur1);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1000, 1000);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Jeu Libre");
    }

    @FXML
    private void quitter() {
        Platform.exit();
    }

    @FXML
    private void nouveauJoueur() {
        while (entrerPseudo(true) && !nomJoueur.equals("annuler")) afficherErreur("Le nom que vous avez rentré est déjà utilisait");
        if(nomJoueur.equals("annuler")) return;
        NiveauControleur.j = new Joueur(AccueilControleur.nomJoueur, 0, ModeProgression.mpl.getNbNiveau());
        AccueilControleur.listeJoueur.add(NiveauControleur.j);
        this.allerVersVueNiveau();
    }

    @FXML
    private void reprendrePartie() {
        while (entrerPseudo(false) && !nomJoueur.equals("annuler")) afficherErreur("Le nom que vous avez rentré n'existe pas");
        if(nomJoueur.equals("annuler")) return;
        for(Joueur j : AccueilControleur.listeJoueur.getListeJoueurs()){
            if(j.equals(new Joueur(nomJoueur, 0,0))) NiveauControleur.j = j;
        }
        this.allerVersVueNiveau();
    }

    @FXML
    private void allerVersVueNiveau(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vue/niveau.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 1000, 1000);

            Stage stage = (Stage) this.root.getScene().getWindow();

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Jeu Libre");
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    private boolean entrerPseudo(boolean nouveauJoueur) {
        TextField pseudoField = new TextField();
        pseudoField.setPromptText("Votre nom");

        VBox content = new VBox(10);
        content.setPadding(new Insets(5, 5, 5, 5));
        content.getChildren().add(pseudoField);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nom");
        dialog.setHeaderText("Veuillez entrer votre nom :");
        dialog.getDialogPane().setContent(content);

        ButtonType validerButton = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(validerButton, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == validerButton) {
            Set<String> allPseudo = AccueilControleur.listeJoueur.getAllPseudo();
            AccueilControleur.nomJoueur = pseudoField.getText();
            if (nouveauJoueur && !allPseudo.contains(pseudoField.getText())) {
                return false;
            } else
                return nouveauJoueur || !allPseudo.contains(pseudoField.getText());
        }else if(result.isPresent() && result.get() == ButtonType.CANCEL){
            nomJoueur = "annuler";
        }
        return false;
    }

    private void afficherErreur(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Nom invalide");
        alert.setHeaderText("Erreur sur le nom");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
