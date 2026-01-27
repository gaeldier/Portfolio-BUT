package vue;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modele.Joueur;
import modele.Niveau;
import modele.ToolBox;
import java.io.IOException;
import java.util.Optional;

public class NiveauControleur {
    public static Joueur j;
    protected static int niveau;
    protected static String difficulte;
    ListView<Niveau> niveauListView = new ListView<>();
    @FXML
    private AnchorPane conteneurPrincipale;
    @FXML
    private VBox conteneur;
    @FXML
    private Text score;

    @FXML
    protected void assombrirButton(MouseEvent e) {
        Button button = (Button) e.getSource();

        button.setStyle(
                "-fx-background-color: #41495C; -fx-background-radius: 20; -fx-border-color: #6f6d6d; -fx-border-radius: 20; -fx-border-width: 2;");
    }

    @FXML
    protected void resetButton(MouseEvent e) {
        Button button = (Button) e.getSource();
        button.setStyle(
                "-fx-background-color: #41425C; -fx-border-color: #6f6d6d; -fx-border-radius: 20; -fx-border-width: 2;");
    }

    @FXML
    protected void retour() {
        Platform.runLater(() -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setHeaderText("Etes-vous sûr de vouloir retourner au menu principale");

            ButtonType quitterButton = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(quitterButton, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == quitterButton) {
                ToolBox.serialize(AccueilControleur.listeJoueur, AccueilControleur.FILEPATH);
                revenirPagePrecedente();
            }
        });
    }

@FXML
protected void supprimerProfil() {
        Platform.runLater(() -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setHeaderText("Etes-vous sûr de vouloir supprimer votre profil");

            ButtonType quitterButton = new ButtonType("Supprimer", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(quitterButton, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == quitterButton) {
                AccueilControleur.listeJoueur.getListJoueur().remove(NiveauControleur.j);
                ToolBox.serialize(AccueilControleur.listeJoueur, AccueilControleur.FILEPATH);
                revenirPagePrecedente();
            }
        });
}

    protected void revenirPagePrecedente() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("menu.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = (javafx.stage.Stage) conteneurPrincipale.getScene().getWindow();

            stage.getScene().setRoot(root);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void initialize(){
        ObservableList<Niveau> ol = FXCollections.observableArrayList(j.getlistNiveau());
        niveauListView.setCellFactory(lv -> new ListCell<>() {
            String STYLE = "-fx-background-radius: 20px;-fx-border-color: rgba(255, 255, 255, 0.4);-fx-border-width: 2px;-fx-border-radius: 20px;-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 10, 0, 0, 5);-fx-text-fill: white;";
            @Override
            protected void updateItem(Niveau item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle(null);
                    setPadding(Insets.EMPTY);
                    setMinHeight(USE_COMPUTED_SIZE);
                } else {
                    HBox niveauBande = new HBox(15);
                    niveauBande.setAlignment(Pos.CENTER);
                    niveauBande.setMaxWidth(Double.MAX_VALUE);
                    niveauBande.setPadding(new Insets(0, 30, 0, 30));
                    HBox.setHgrow(niveauBande, Priority.ALWAYS);

                    Label niveauLabel = new Label("Niveau " + item.getNumNiveau());
                    niveauLabel.setStyle("-fx-text-fill: white;-fx-font-size: 30;");
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    HBox indicateurs = new HBox(5);
                    indicateurs.setAlignment(Pos.CENTER_RIGHT);

                    Circle c1 = createDifficultyCircle(item.isFacileFait());
                    Circle c2 = createDifficultyCircle(item.isMoyenFait());
                    Circle c3 = createDifficultyCircle(item.isDifficileFait());

                    indicateurs.getChildren().addAll(c1, c2, c3);

                    niveauBande.getChildren().addAll(niveauLabel, spacer, indicateurs);
                    niveauBande.setMinHeight(100);

                    HBox boutonsDifficulte = new HBox(20);
                    boutonsDifficulte.setAlignment(Pos.CENTER);
                    boutonsDifficulte.setPadding(new Insets(10, 0, 10, 0));

                    Button btnFacile = createDifficultyButton(item.getNumNiveau(), "Facile");
                    Button btnMoyen = createDifficultyButton(item.getNumNiveau(),"Moyen");
                    Button btnDifficile = createDifficultyButton(item.getNumNiveau(),"Difficile");

                    boutonsDifficulte.getChildren().addAll(btnFacile, btnMoyen, btnDifficile);

                    boutonsDifficulte.setVisible(false);
                    boutonsDifficulte.setManaged(false);

                    VBox cellContent = new VBox(0);
                    cellContent.getChildren().addAll(niveauBande, boutonsDifficulte);

                    if(item.peutEtreFais()){
                        STYLE = "-fx-background-color: rgba(65, 66, 92, 0.6);" + STYLE;
                        cellContent.setOnMouseEntered(e -> {
                            boutonsDifficulte.setVisible(true);
                            boutonsDifficulte.setManaged(true);
                            niveauBande.setStyle(STYLE + "-fx-border-color: #085b7e;");
                        });

                        cellContent.setOnMouseExited(e -> {
                            boutonsDifficulte.setVisible(false);
                            boutonsDifficulte.setManaged(false);
                            niveauBande.setStyle(STYLE);
                        });
                    }else{
                        STYLE = "-fx-background-color: rgba(96, 96, 107, 0.6);" + STYLE;
                    }
                    niveauBande.setStyle(STYLE);

                    cellContent.setMinHeight(50);

                    setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-background-insets: 5px 0 5px 0;");
                    setPadding(Insets.EMPTY);

                    setGraphic(cellContent);
                    setText(null);
                }
            }
        });
        niveauListView.setStyle("-fx-background-color: transparent;-fx-background-radius: 20px;-fx-border-color: transparent;-fx-control-inner-background: transparent;-fx-cell-border-color: transparent;-fx-hbar-policy: never;-fx-vbar-policy: never;");
        niveauListView.setItems(ol);
        conteneur.getChildren().add(niveauListView);
        this.setScore();
    }

    private void setScore(){
        this.score.setText("Votre score : " + NiveauControleur.j.getScore());
    }

    private Button createDifficultyButton(int niveau, String text) {
        Button button = new Button(text);
        final String BASE_STYLE = "-fx-background-color: #41425C; -fx-background-radius: 20; -fx-border-color: #6f6d6d; -fx-border-radius: 20; -fx-border-width: 2; -fx-text-fill: white;";
        final String HOVER_STYLE = "-fx-background-color: #41495C; -fx-background-radius: 20; -fx-border-color: #6f6d6d; -fx-border-radius: 20; -fx-border-width: 2; -fx-text-fill: white;";

        button.setStyle(BASE_STYLE);
        button.setPrefWidth(120);

        button.setOnMouseEntered(e -> button.setStyle(HOVER_STYLE));
        button.setOnMouseExited(e -> button.setStyle(BASE_STYLE));

        button.setOnMouseClicked(e -> {
            NiveauControleur.niveau = niveau;
            NiveauControleur.difficulte = text;
            this.loadNiveau(e);
        });

        return button;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void loadNiveau(Event event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vue/plateau.fxml"));
            ModeProgressionControleur controleur2 = new ModeProgressionControleur();
            fxmlLoader.setController(controleur2);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 1000, 1000);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Jeu Libre");
        }catch(IOException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private Circle createDifficultyCircle(boolean complete) {
        Circle circle = new Circle(15);

        if (complete) {
            circle.setFill(Color.web("#66FF66"));
        } else {
            circle.setFill(Color.web("#AAAAAA"));
        }

        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(1);

        return circle;
    }
}