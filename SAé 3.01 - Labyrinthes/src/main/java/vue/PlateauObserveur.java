package vue;

import controleur.JeuControleur;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import modele.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public abstract class PlateauObserveur implements Observers {
    protected Labyrinthe l;
    protected GridPane plateau;
    protected JeuControleur controleur;
    protected int tailleCase;
    protected boolean vueRestreinte;
    protected boolean porteLimite;
    protected boolean apparation;
    protected int champDeVision;
    private MediaPlayer bonk;
    protected MediaPlayer finish;
    protected boolean[][] casePeutEtreVue;
    protected int nbPasJoueur;
    @FXML
    protected AnchorPane conteneurPrincipale;
    @FXML
    protected VBox conteneur;

    @Override
    public void update() {
        this.jeuFin();
        if (this.vueRestreinte)
            this.updateAvecRestriction();
        else
            this.updateSansRestriction();
    }

    protected void updateSansRestriction() {
        nbPasJoueur += 1;
        if(porteLimite) {
            calculVisibilite();
            Platform.runLater(this.plateau::requestFocus);
        }else if(apparation){
            calculVisibilite();
        }
        Cases oldCaseJoueur = l.getOldPosJoueur();
        Cases newCaseJoueur = l.getPosJoueur();

        StackPane oldCell = this.recupCell(oldCaseJoueur.getY(), oldCaseJoueur.getX());
        StackPane newCell = this.recupCell(newCaseJoueur.getY(), newCaseJoueur.getX());

        if (oldCell != null)
            setCellBackground(oldCell, oldCaseJoueur.getX(), oldCaseJoueur.getY());
        if (newCell != null)
            setCellBackground(newCell, newCaseJoueur.getX(), newCaseJoueur.getY());
        Platform.runLater(this.plateau::requestFocus);
    }

    void updateAvecRestriction() {
        nbPasJoueur += 1;
        this.creerPlateauAvecRestriction();
        this.conteneur.getChildren().remove(this.plateau);
        this.conteneur.getChildren().add(this.plateau);
        Platform.runLater(this.plateau::requestFocus);
    }

    private void jeuFin() {
        if (l.aGagner()) {
            this.finish.seek(Duration.ZERO);
            this.finish.play();
            Platform.runLater(() -> {
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Fin");
                dialog.setHeaderText("Bravo, vous avez fini le labyrinthe");
                dialog.setContentText("Voulez vous recommencer ou quitter");

                ButtonType recommencerButton = new ButtonType("Recommencer", ButtonBar.ButtonData.OK_DONE);
                ButtonType quitterButton = new ButtonType("Quitter", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(recommencerButton, quitterButton);

                Optional<ButtonType> result = dialog.showAndWait();

                if (result.isPresent() && result.get() == recommencerButton) {
                    this.jouer();
                } else {
                    revenirPagePrecedente();
                }
            });
        }
    }

    protected void creerPlateauSansRestriction() {
        this.calculVisibilite();
        this.plateau.getChildren().clear();
        this.plateau.setHgap(0);
        this.plateau.setVgap(0);
        for (int y = 0; y < l.getNbLigne(); y++) {
            for (int x = 0; x < l.getNbColonne(); x++) {
                if(nbPasJoueur == 0) {
                    StackPane cell = new StackPane();
                    cell.setPrefSize(this.tailleCase, this.tailleCase);

                    this.setCellBackground(cell, x, y);

                    this.plateau.add(cell, x, y);
                }
            }
        }

        this.plateau.setAlignment(Pos.CENTER);
    }

    protected void creerPlateauAvecRestriction() {
        this.plateau.getChildren().clear();
        this.plateau.setHgap(0);
        this.plateau.setVgap(0);
        int rayon = (this.champDeVision - 1) / 2;
        int debutLigne = this.l.getPosJoueur().getY() - rayon;
        int debutColonne = this.l.getPosJoueur().getX() - rayon;

        for (int y = 0; y < this.champDeVision; y++) {
            for (int x = 0; x < this.champDeVision; x++) {
                int reelX = debutColonne + x;
                int reelY = debutLigne + y;

                StackPane cell = new StackPane();
                cell.setPrefSize(this.tailleCase, this.tailleCase);

                this.setCellBackground(cell, reelX, reelY);

                this.plateau.add(cell, x, y);
            }
        }
        this.plateau.setAlignment(Pos.CENTER);
    }

    private void setCellBackground(StackPane cell, int x, int y) {
        if (x < 0 || x >= l.getNbColonne() || y < 0 || y >= l.getNbLigne()) {
            cell.setStyle("-fx-background-color: #4D4F5C;");
            return;
        }
        Cases c = new Cases(x, y);
        Image img;

        if(l.estBordure(c))
            img = new Image(Objects.requireNonNull(getClass().getResource("/img/wall.png")).toExternalForm());
        else if ((this.vueRestreinte || estCaseVisible(c)) && l.estJoueur(c))
            img = new Image(Objects.requireNonNull(getClass().getResource("/img/joueur.png")).toExternalForm());
        else if (l.estEntree(c))
            img = new Image(Objects.requireNonNull(getClass().getResource("/img/entree.png")).toExternalForm());
        else if (l.estSortie(c))
            img = new Image(Objects.requireNonNull(getClass().getResource("/img/sortie.png")).toExternalForm());
        else if ((this.vueRestreinte || estCaseVisible(c)) && l.estMur(c))
            img = new Image(Objects.requireNonNull(getClass().getResource("/img/wall.png")).toExternalForm());
        else
            img = new Image(Objects.requireNonNull(getClass().getResource("/img/path.png")).toExternalForm());

        BackgroundImage bg = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(this.tailleCase, this.tailleCase, false, false, false, false));

        cell.setBackground(new Background(bg));
    }

    private boolean estCaseVisible(Cases c){
        return casePeutEtreVue[c.getY()][c.getX()];
    }

    private StackPane recupCell(int ligne, int colonne) {
        for (Node cell : this.plateau.getChildren()) {
            int row = GridPane.getRowIndex(cell);
            int col = GridPane.getColumnIndex(cell);

            if (ligne == row && colonne == col)
                return (StackPane) cell;
        }
        return null;
    }

    protected void initialiserControlesClavier() {
        plateau.setFocusTraversable(true);
        plateau.requestFocus();

        plateau.setOnKeyPressed(event -> {
            try {
                switch (event.getCode()) {
                    case UP,W -> controleur.deplacementJoueur(Direction.NORD);
                    case DOWN,S -> controleur.deplacementJoueur(Direction.SUD);
                    case LEFT,A -> controleur.deplacementJoueur(Direction.OUEST);
                    case RIGHT,D -> controleur.deplacementJoueur(Direction.EST);
                }
            } catch (WrongDeplacementException e) {
                this.bonk.seek(Duration.ZERO);
                this.bonk.play();
                if (this.vueRestreinte) this.updateAvecRestriction();
                if(!this.vueRestreinte) this.updateSansRestriction();
            }
        });
    }

    public void afficherMap() {
        this.tailleCase = 10;
        GridPane map = new GridPane();
        map.getChildren().clear();
        map.setHgap(0);
        map.setVgap(0);
        for (int y = 0; y < l.getNbLigne(); y++) {
            for (int x = 0; x < l.getNbColonne(); x++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(this.tailleCase, this.tailleCase);
                if (l.getEntree().equals(new Cases(x, y))) {
                    BackgroundImage bg = new BackgroundImage(
                            new Image(
                                    Objects.requireNonNull(getClass().getResource("/img/entree.png")).toExternalForm()),
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            new BackgroundSize(this.tailleCase, this.tailleCase, false, false, false, false));
                    cell.setBackground(new Background(bg));
                } else {
                    this.setCellBackground(cell, x, y);
                }
                map.add(cell, x, y);
            }
        }
        this.tailleCase = 50;
        map.setAlignment(Pos.CENTER);
        this.conteneurPrincipale.getChildren().add(map);
        AnchorPane.setTopAnchor(map, 20.0);
        AnchorPane.setRightAnchor(map, 20.0);
    }

    protected void jouer() {
        nbPasJoueur = 0;

        this.bonk = new MediaPlayer(
                new Media(Objects.requireNonNull(getClass().getResource("/sons/bonk.mp3")).toExternalForm()));
        this.bonk.setVolume(0.5);
        this.finish = new MediaPlayer(
                new Media(Objects.requireNonNull(getClass().getResource("/sons/tada.mp3")).toExternalForm()));
        this.finish.setVolume(0.5);
        this.tailleCase = Math.min((int) ((Screen.getPrimary().getVisualBounds().getHeight() * 0.75) / l.getNbLigne()),
                (int) ((Screen.getPrimary().getVisualBounds().getWidth() * 0.75) / l.getNbColonne()));

        if (this.vueRestreinte) {
            this.tailleCase = 50;
            champDeVision = 5;
            this.creerPlateauAvecRestriction();
            this.afficherMap();
        } else if(this.porteLimite) {
            champDeVision = 4;
            this.creerPlateauSansRestriction();
        }else if (this.apparation){
            champDeVision = 4;
            this.creerPlateauSansRestriction();
        }else {
            this.creerPlateauSansRestriction();
        }

        this.conteneur.getChildren().clear();
        this.conteneur.getChildren().add(this.plateau);

        Platform.runLater(this::initialiserControlesClavier);
    }

    private void initialiserCase(boolean visibilite){
        for (boolean[] booleans : casePeutEtreVue) {
            Arrays.fill(booleans, visibilite);
        }
    }

    protected void calculVisibilite(){
        if(nbPasJoueur == 0 || casePeutEtreVue == null) casePeutEtreVue = new boolean[l.getNbLigne()][l.getNbColonne()];
        if(porteLimite) resetPlateau();
        if(!apparation) initialiserCase(!this.porteLimite);

        Cases joueur = l.getPosJoueur();
        for (int y = joueur.getY() - champDeVision; y <= joueur.getY() + champDeVision; y++) {
            for (int x = joueur.getX() - champDeVision; x <= joueur.getX() + champDeVision; x++) {

                if (x < 0 || x >= l.getNbColonne() || y < 0 || y >= l.getNbLigne()) {
                    continue;
                }

                Cases cible = new Cases(x, y);

                if (this.porteLimite && (Math.abs(joueur.getX() - x) + Math.abs(joueur.getY() - y) > champDeVision)) {
                    continue;
                }

                if (peutVoir(joueur, cible)) {
                    casePeutEtreVue[y][x] = true;
                    if(nbPasJoueur > 0 && (apparation || porteLimite)){
                        apparationCase(x, y);
                    }
                }
            }
        }
    }

    private void resetPlateau(){
        Image img = new Image(Objects.requireNonNull(getClass().getResource("/img/path.png")).toExternalForm());
        BackgroundImage bg = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(this.tailleCase, this.tailleCase, false, false, false, false));
        for(int y = 0; y < this.l.getNbLigne(); y++){
            for(int x = 0; x < this.l.getNbColonne(); x++){
                if((y > 0 && y < this.l.getNbLigne() - 1) && (x > 0 && x < this.l.getNbColonne() - 1) && this.casePeutEtreVue[y][x]){
                    StackPane cell = recupCell(y, x);
                    assert cell != null;
                    cell.setBackground(new Background(bg));
                }
            }
        }
    }

    private void apparationCase(int x, int y){
        StackPane cell = recupCell(y, x);
        setCellBackground(cell, x, y);
    }

    protected boolean peutVoir(Cases source, Cases cible){
        if (source.equals(cible)) return true;
        int dx = cible.getX() - source.getX();
        int dy = cible.getY() - source.getY();
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        for (int i = 1; i <= steps; i++) {

            int currentX = (int) Math.round(source.getX() + (double)i * dx / steps);
            int currentY = (int) Math.round(source.getY() + (double)i * dy / steps);

            Cases currentCase = new Cases(currentX, currentY);

            if (l.estMur(currentCase) && !currentCase.equals(cible)) {
                return false;
            }
        }
        return true;
    }

    protected void revenirPagePrecedente() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("menu.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.application.Platform.runLater(() -> {
                javafx.stage.Stage stage = (Stage) conteneurPrincipale.getScene().getWindow();
                stage.getScene().setRoot(root);
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    protected void retour() {
        Platform.runLater(() -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setHeaderText("Etes-vous sûr de vouloir quitter en pleine partie, vous ne pourrez pas revenir en arrière");

            ButtonType quitterButton = new ButtonType("Quitter", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(quitterButton, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == quitterButton) {
                revenirPagePrecedente();
            }
        });
    }

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
}
