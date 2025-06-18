import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;
import java.io.FileNotFoundException;
import javafx.scene.control.cell.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import java.util.Optional;


public class MonController {

    @FXML
    private StackPane stackPane;

    // Toolbar
    @FXML
    private Button boutonAccueil;
    @FXML
    private Button boutonAffectations;
    @FXML
    private Button boutonPaire;

    // Accueil
    @FXML
    private Pane pageAccueil;
    @FXML
    private Button boutonCommencer;
    @FXML
    private Label labelBienvenue;

    // Affectations
    @FXML
    private Pane pageAffectations;
    @FXML
    private Button boutonPlus;
    @FXML
    private Label labelCharge;

    // Affectations suite
    @FXML
    private Pane pageListe;
    @FXML
    private Label labelListe;
    @FXML
    private TableView<Adolescents> tableViewListe;
    @FXML
    private TableColumn<Adolescents, String> columnNom;
    @FXML
    private TableColumn<Adolescents, String> columnPrenom;
    @FXML
    private TableColumn<Adolescents, String> columnGenre;
    @FXML
    private TableColumn<Adolescents, String> columnDateNaissance;
    @FXML
    private TableColumn<Adolescents, String> columnPays;
    @FXML
    private TableColumn<Adolescents, String> columnAllergie;
    @FXML
    private TableColumn<Adolescents, String> columnAnimal;
    @FXML
    private TableColumn<Adolescents, String> columnGFood;
    @FXML
    private TableColumn<Adolescents, String> columnHFood;
    @FXML
    private TableColumn<Adolescents, String> columnHobbies;
    @FXML
    private TableColumn<Adolescents, String> columnHistory;
    @FXML
    private TableColumn<Adolescents, String> columnPairGenre;
    @FXML
    private Pane pageCréerAsso;
    @FXML
    private Label LabelCreerAsso;
    @FXML
    private Button boutonPlus2;

    // Affectations suite 2
    @FXML
    private Pane pageListe1;
    @FXML
    private TableView<Adolescents> tableViewListe2;
    @FXML
    private TableColumn<Adolescents, String> columnNom2;
    @FXML
    private TableColumn<Adolescents, String> columnPrenom2;
    @FXML
    private TableColumn<Adolescents, String> columnGenre2;
    @FXML
    private TableColumn<Adolescents, String> columnDateNaissance2;
    @FXML
    private TableColumn<Adolescents, String> columnPays2;
    @FXML
    private TableColumn<Adolescents, String> columnAllergie2;
    @FXML
    private TableColumn<Adolescents, String> columnAnimal2;
    @FXML
    private TableColumn<Adolescents, String> columnGFood2;
    @FXML
    private TableColumn<Adolescents, String> columnHFood2;
    @FXML
    private TableColumn<Adolescents, String> columnHobbies2;
    @FXML
    private TableColumn<Adolescents, String> columnHistory2;
    @FXML
    private TableColumn<Adolescents, String> columnPairGenre2;
    @FXML
    private Pane pageCréerAsso1;
    @FXML
    private Button boutonParamètre;
    @FXML
    private CheckBox checkBox1;
    @FXML
    private CheckBox checkBox2;
    @FXML
    private CheckBox checkBox3;
    @FXML
    private CheckBox checkBox4;
    @FXML
    private CheckBox checkBox5;
    @FXML
    private Label labelListe1; // Pour afficher la paire sélectionnée
    @FXML
    private Button boutonValider1; // Pour valider la paire
    @FXML
    private Button boutonDesister;

    // Paire(s)
    @FXML
    private Pane pagePaires;
    @FXML
    private TableView<PaireAdo> tableViewPaires;
    @FXML
    private TableColumn<PaireAdo, String> colAdo1;
    @FXML
    private TableColumn<PaireAdo, String> colAdo2;
    @FXML
    private TableColumn<PaireAdo, String> colScore;

    // Pour stocker les index sélectionnés
    private final ArrayList<Integer> selectedIndexes = new ArrayList<>();
    private final ArrayList<PaireAdo> pairesValidees = new ArrayList<>();

    // Mode édition pour la table
    private boolean modeEdition = false;

    // Initialisation
    @FXML
    public void initialize() {
        switchPane(pageAccueil);
        boutonAccueil.setOnAction(event -> handleAccueil(event));
        boutonCommencer.setOnAction(event -> handleAffectations(event));
        boutonAffectations.setOnAction(event -> handleAffectations(event));
        boutonPlus.setOnAction(event -> handlePlus(event));
        boutonPlus2.setOnAction(event -> handlePlus2(event));
        boutonValider1.setOnAction(event -> handleValider1(event));
        boutonPaire.setOnAction(event -> handlePaires(event));
        boutonParamètre.setOnAction(this::handleParametre);
        boutonDesister.setOnAction(event -> {
            PaireAdo selectedPaire = tableViewPaires.getSelectionModel().getSelectedItem();
            if (selectedPaire != null) {
                // Choix de l'ado à désister
                Alert choix = new Alert(Alert.AlertType.CONFIRMATION);
                choix.setTitle("Désistement");
                choix.setHeaderText("Quel adolescent désiste ?");
                ButtonType ado1Btn = new ButtonType(selectedPaire.ado1.getNom() + " " + selectedPaire.ado1.getPrenom());
                ButtonType ado2Btn = new ButtonType(selectedPaire.ado2.getNom() + " " + selectedPaire.ado2.getPrenom());
                ButtonType cancelBtn = ButtonType.CANCEL;
                choix.getButtonTypes().setAll(ado1Btn, ado2Btn, cancelBtn);

                Optional<ButtonType> result = choix.showAndWait();
                if (result.isPresent() && result.get() != cancelBtn) {
                    Adolescents desist = (result.get() == ado1Btn) ? selectedPaire.ado1 : selectedPaire.ado2;
                    gererDesistement(desist);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Sélectionnez une paire d'abord.");
                alert.showAndWait();
            }
        });

        // Configuration des colonnes de la TableView
        columnNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        columnPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        columnGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        columnDateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        columnPays.setCellValueFactory(new PropertyValueFactory<>("paysOrigine"));
        columnAllergie.setCellValueFactory(new PropertyValueFactory<>("gallergy"));
        columnAnimal.setCellValueFactory(new PropertyValueFactory<>("hanimal"));
        columnGFood.setCellValueFactory(new PropertyValueFactory<>("food"));
        columnHFood.setCellValueFactory(new PropertyValueFactory<>("HostFood"));
        columnHobbies.setCellValueFactory(new PropertyValueFactory<>("hobbies"));
        columnHistory.setCellValueFactory(new PropertyValueFactory<>("history"));
        columnPairGenre.setCellValueFactory(new PropertyValueFactory<>("pairGenre"));

        columnNom2.setCellValueFactory(new PropertyValueFactory<>("nom"));
        columnPrenom2.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        columnGenre2.setCellValueFactory(new PropertyValueFactory<>("genre"));
        columnDateNaissance2.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        columnPays2.setCellValueFactory(new PropertyValueFactory<>("paysOrigine"));
        columnAllergie2.setCellValueFactory(new PropertyValueFactory<>("gallergy"));
        columnAnimal2.setCellValueFactory(new PropertyValueFactory<>("hanimal"));
        columnGFood2.setCellValueFactory(new PropertyValueFactory<>("food"));
        columnHFood2.setCellValueFactory(new PropertyValueFactory<>("HostFood"));
        columnHobbies2.setCellValueFactory(new PropertyValueFactory<>("hobbies"));
        columnHistory2.setCellValueFactory(new PropertyValueFactory<>("history"));
        columnPairGenre2.setCellValueFactory(new PropertyValueFactory<>("pairGenre"));

        // Initialisation de la TableView des paires
        colAdo1.setCellValueFactory(new PropertyValueFactory<>("ado1"));
        colAdo2.setCellValueFactory(new PropertyValueFactory<>("ado2"));
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        colAdo1.setCellValueFactory(cellData -> {
            MonController.PaireAdo ado = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(ado.getNom() + " " + ado.getPrenom());
        });
        colAdo2.setCellValueFactory(cellData -> {
            MonController.PaireAdo ado = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(ado.getNom2() + " " + ado.getPrenom2());
        });
        colScore.setCellValueFactory(cellData -> {
            MonController.PaireAdo ado = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(ado.getScore());
        });

        // Gestion des CheckBox pour la sélection de paires
        CheckBox[] checkBoxes = { checkBox1, checkBox2, checkBox3, checkBox4, checkBox5 };
        for (int i = 0; i < checkBoxes.length; i++) {
            final int idx = i;
            checkBoxes[i].selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    if (selectedIndexes.size() < 2) {
                        selectedIndexes.add(idx);
                    } else {
                        checkBoxes[idx].setSelected(false); // Empêche plus de 2
                    }
                } else {
                    selectedIndexes.remove((Integer) idx);
                }
                updatePairDisplay1();
            });
        }
        boutonValider1.setVisible(false);

        tableViewPaires.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                PaireAdo selectedPaire = tableViewPaires.getSelectionModel().getSelectedItem();
                if (selectedPaire != null) {
                    Adolescents ado1 = selectedPaire.ado1;
                    Adolescents ado2 = selectedPaire.ado2;
                    double score = selectedPaire.score;

                    String info = "Adolescent 1 :" + "                    " + "Adolescent 2 :\n"

                            + "Nom : " + ado1.getNom() + "                      " + "Nom : " + ado2.getNom() + "\n"
                            + "Prénom : " + ado1.getPrenom() + "                    " + "Prénom : " + ado2.getPrenom()
                            + "\n"
                            + "Genre : " + ado1.getGenre() + "                     " + "Genre : " + ado2.getGenre()
                            + "\n"
                            + "Allergie : " + ado1.getGallergy() + "                      " + "Allergie : "
                            + ado2.getGallergy() + "\n"
                            + "Animal : " + ado1.getHanimal() + "                     " + "Animal : "
                            + ado2.getHanimal() + "\n"
                            + "GFood : " + ado1.getFood() + "                    " + "GFood : " + ado2.getFood() + "\n"
                            + "HFood : " + ado1.getFood() + "                    " + "HFood : " + ado2.getFood() + "\n"
                            + "History : " + ado1.getHistory() + "                    " + "History : "
                            + ado2.getHistory() + "\n\n"
                            + "Score de compatibilité : " + score;

                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle("Détails de la paire");
                    alert.setHeaderText("Informations sur la paire sélectionnée");

                    javafx.scene.control.TextArea textArea = new javafx.scene.control.TextArea(info);
                    textArea.setEditable(false);
                    textArea.setWrapText(true);
                    textArea.setMaxWidth(Double.MAX_VALUE);
                    textArea.setMaxHeight(Double.MAX_VALUE);

                    alert.getDialogPane().setContent(textArea);
                    alert.showAndWait();
                }
            }
        });
    }

    // Méthode utilitaire pour changer de page
    private void switchPane(Pane pane) {
        stackPane.getChildren().forEach(child -> child.setVisible(false));
        pane.setVisible(true);
    }

    // Gestion des boutons de la toolbar
    @FXML
    private void handleAccueil(ActionEvent event) {
        switchPane(pageAccueil);
    }

    @FXML
    private void handleAffectations(ActionEvent event) {
        switchPane(pageAffectations);
    }

    @FXML
    private void handlePaires(ActionEvent event) {
        switchPane(pagePaires);
        tableViewPaires.getItems().setAll(pairesValidees); // Affiche les paires validées
    }

    // Gestion du bouton "+" dans pageAffectations
    @FXML
    private void handlePlus(ActionEvent event) {
        switchPane(pageListe);
        ArrayList<Adolescents> liste = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader();
            String[] lignes = reader.getDataFromCSV("./res/Ado.csv");
            for (String ligne : lignes) {
                String[] champs = ligne.split(";");
                if (champs.length == 12) {
                    Adolescents ado = new Adolescents(
                            champs[0], champs[1], champs[2], champs[3], champs[4], champs[5],
                            champs[6], champs[7], champs[8], champs[9], champs[10], champs[11]);
                    if (!isAdoApparie(ado)) { // <-- on filtre ici
                        liste.add(ado);
                    }
                }
            }
        } catch (FileNotFoundException | InvalidStructureException e) {
            System.err.println("Erreur lors du chargement du fichier : " + e.getMessage());
        }
        tableViewListe.getItems().setAll(liste);
    }

    @FXML
    private void handlePlus2(ActionEvent event) {
        // Affiche un pop-up de choix
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Création d'association");
        alert.setHeaderText("Voulez-vous créer une association manuelle ou automatique ?");
        ButtonType buttonManuelle = new ButtonType("Manuelle", ButtonData.LEFT);
        ButtonType buttonAuto = new ButtonType("Automatique", ButtonData.RIGHT);
        ButtonType buttonConfig = new ButtonType("Importer une configuration", ButtonData.OTHER);
        alert.getButtonTypes().setAll(buttonManuelle, buttonAuto, ButtonType.CANCEL, buttonConfig);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonManuelle) {
            switchPane(pageListe1);
            ArrayList<Adolescents> liste = new ArrayList<>();
            try {
                CSVReader reader = new CSVReader();
                String[] lignes = reader.getDataFromCSV("./res/Ado.csv");
                for (String ligne : lignes) {
                    String[] champs = ligne.split(";");
                    if (champs.length == 12) {
                        Adolescents ado = new Adolescents(
                                champs[0], champs[1], champs[2], champs[3], champs[4], champs[5],
                                champs[6], champs[7], champs[8], champs[9], champs[10], champs[11]);
                        if (!isAdoApparie(ado)) {
                            liste.add(ado);
                        }
                    }
                }
            } catch (FileNotFoundException | InvalidStructureException e) {
                System.err.println("Erreur lors du chargement du fichier : " + e.getMessage());
            }
            tableViewListe2.getItems().setAll(liste);
            refreshAffectationsList();

        } else if (result.isPresent() && result.get() == buttonAuto) {
            // Mode automatique : crée la meilleure paire possible et affiche pagePaires
            ArrayList<Adolescents> liste = new ArrayList<>();
            try {
                CSVReader reader = new CSVReader();
                String[] lignes = reader.getDataFromCSV("./res/Ado.csv");
                for (String ligne : lignes) {
                    String[] champs = ligne.split(";");
                    if (champs.length == 12) {
                        Adolescents ado = new Adolescents(
                                champs[0], champs[1], champs[2], champs[3], champs[4], champs[5],
                                champs[6], champs[7], champs[8], champs[9], champs[10], champs[11]);
                        if (!isAdoApparie(ado)) {
                            liste.add(ado);
                        }
                    }
                }
            } catch (FileNotFoundException | InvalidStructureException e) {
                System.err.println("Erreur lors du chargement du fichier : " + e.getMessage());
            }

            // Cherche la meilleure paire compatible (exemple : score max)
            PaireAdo meilleurePaire = null;
            double meilleurScore = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < liste.size(); i++) {
                for (int j = i + 1; j < liste.size(); j++) {
                    Adolescents ado1 = liste.get(i);
                    Adolescents ado2 = liste.get(j);
                    if (ado1.compatibility(ado2) && ado2.compatibility(ado1)) {
                        double score = ado1.getScorePourAppariement(ado2);
                        if (score > meilleurScore) {
                            meilleurScore = score;
                            meilleurePaire = new PaireAdo(ado1, ado2, score);
                        }
                    }
                }
            }
            if (meilleurePaire != null) {
                pairesValidees.add(meilleurePaire);
                tableViewPaires.getItems().setAll(pairesValidees);
                refreshAffectationsList();
                switchPane(pagePaires);
            } else {
                Alert noPair = new Alert(Alert.AlertType.INFORMATION, "Aucune paire compatible trouvée.");
                noPair.showAndWait();
            }
        } else if (result.isPresent() && result.get() == buttonConfig) {
            // Ouvre un FileChooser pour sélectionner le fichier de configuration
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Importer une configuration");
            fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Fichiers binaires", "*.bin"));
            java.io.File selectedFile = fileChooser.showOpenDialog(stackPane.getScene().getWindow());

            if (selectedFile != null) {
                AdolescentsArray imported = DeSerializer.DeSerialize(selectedFile.getAbsolutePath());
                if (imported != null) {
                    // Ajoute chaque paire importée à la liste des paires validées
                    for (Map.Entry<Adolescents, Adolescents> entry : imported.getMap().entrySet()) {
                        Adolescents ado1 = entry.getKey();
                        Adolescents ado2 = entry.getValue();
                        double score = ado1.getScorePourAppariement(ado2);
                        PaireAdo paire = new PaireAdo(ado1, ado2, score);
                        pairesValidees.add(paire);
                    }
                    tableViewPaires.getItems().setAll(pairesValidees);
                    refreshAffectationsList();
                    switchPane(pagePaires);
                } else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'importation du fichier.");
                    alert2.showAndWait();
                }
            }
        }
    }

    // Affiche la paire sélectionnée dans labelListe1
    private void updatePairDisplay1() {
        if (selectedIndexes.size() == 2) {
            Adolescents ado1 = tableViewListe2.getItems().get(selectedIndexes.get(0));
            Adolescents ado2 = tableViewListe2.getItems().get(selectedIndexes.get(1));
            // Vérifie la compatibilité
            boolean compatible = ado1.compatibility(ado2) && ado2.compatibility(ado1);
            double score = ado1.getScorePourAppariement(ado2);

            if (compatible) {
                String txt = "Paire sélectionnée : \n" +
                        ado1.getNom() + " " + ado1.getPrenom() + " et " +
                        ado2.getNom() + " " + ado2.getPrenom() +
                        "\nScore de \ncompatibilité : " + "\n" + score;
                labelListe1.setText(txt);
                boutonValider1.setVisible(true);
            } else {
                String txt = "Incompatible !\n" +
                        ado1.getNom() + " " + ado1.getPrenom() + " et " +
                        ado2.getNom() + " " + ado2.getPrenom() +
                        "\nne peuvent pas\n être appariés.";
                labelListe1.setText(txt);
                boutonValider1.setVisible(false);
            }
        } else {
            labelListe1.setText("Sélectionnez\n 2 adolescents\n pour former\n une paire.");
            boutonValider1.setVisible(false);
        }
    }

    @FXML
    private void handleValider1(ActionEvent event) {
        if (selectedIndexes.size() == 2) {
            Adolescents ado1 = tableViewListe2.getItems().get(selectedIndexes.get(0));
            Adolescents ado2 = tableViewListe2.getItems().get(selectedIndexes.get(1));
            boolean compatible = ado1.compatibility(ado2) && ado2.compatibility(ado1);
            double score = ado1.getScorePourAppariement(ado2);

            if (compatible) {
                // Crée et enregistre la paire
                PaireAdo paire = new PaireAdo(ado1, ado2, score);
                pairesValidees.add(paire);
                tableViewPaires.getItems().setAll(pairesValidees); // Rafraîchit la TableView

                // Rafraîchit la liste des ados non appariés
                refreshAffectationsList();

                // Affiche la page des paires
                switchPane(pagePaires);
            }

            // Réinitialise la sélection
            selectedIndexes.clear();
            CheckBox[] checkBoxes = { checkBox1, checkBox2, checkBox3, checkBox4, checkBox5 };
            for (CheckBox cb : checkBoxes)
                cb.setSelected(false);
            updatePairDisplay1();
        }
    }

    @FXML
    private void handleParametre(ActionEvent event) {
        System.out.println("clic paramètre");
        modeEdition = true;

        ObservableList<String> gallergyOptions = FXCollections.observableArrayList("yes", "no");
        ObservableList<String> hanimalOptions = FXCollections.observableArrayList("yes", "no");
        ObservableList<String> gfoodOptions = FXCollections.observableArrayList("none", "vegetarian", "nonuts");
        ObservableList<String> hfoodOptions = FXCollections.observableArrayList("none", "vegetarian", "nonuts");
        ObservableList<String> historyOptions = FXCollections.observableArrayList("same", "other", "none");

        // Pour rendre tableViewListe2 éditable :
        tableViewListe2.setEditable(true);

        // Gallergy
        columnAllergie2.setCellFactory(ComboBoxTableCell.forTableColumn(gallergyOptions));
        columnAllergie2.setOnEditCommit(eventEdit -> {
            Adolescents ado = eventEdit.getRowValue();
            ado.setGallergy(eventEdit.getNewValue());
            tableViewListe2.refresh();
        });

        // Hanimal
        columnAnimal2.setCellFactory(ComboBoxTableCell.forTableColumn(hanimalOptions));
        columnAnimal2.setOnEditCommit(eventEdit -> {
            Adolescents ado = eventEdit.getRowValue();
            ado.setHanimal(eventEdit.getNewValue());
            tableViewListe2.refresh();
        });

        // Gfood
        columnGFood2.setCellFactory(ComboBoxTableCell.forTableColumn(gfoodOptions));
        columnGFood2.setOnEditCommit(eventEdit -> {
            Adolescents ado = eventEdit.getRowValue();
            ado.setGuestFood(eventEdit.getNewValue());
            tableViewListe2.refresh();
        });

        // Hfood
        columnHFood2.setCellFactory(ComboBoxTableCell.forTableColumn(hfoodOptions));
        columnHFood2.setOnEditCommit(eventEdit -> {
            Adolescents ado = eventEdit.getRowValue();
            ado.setHostFood(eventEdit.getNewValue());
            tableViewListe2.refresh();
        });

        // History
        columnHistory2.setCellFactory(ComboBoxTableCell.forTableColumn(historyOptions));
        columnHistory2.setOnEditCommit(eventEdit -> {
            Adolescents ado = eventEdit.getRowValue();
            ado.setHistory(eventEdit.getNewValue());
            tableViewListe2.refresh();
        });

        tableViewListe2.setEditable(true);
    }

    private boolean isAdoApparie(Adolescents ado) {
        for (PaireAdo paire : pairesValidees) {
            if (paire.ado1.equals(ado) || paire.ado2.equals(ado)) {
                return true;
            }
        }
        return false;
    }

    private void gererDesistement(Adolescents desist) {
        // Retirer toutes les paires où l'ado désisté est présent
        pairesValidees.removeIf(paire -> paire.ado1.equals(desist) || paire.ado2.equals(desist));
        tableViewPaires.getItems().setAll(pairesValidees);

        // Rechercher un nouveau correspondant parmi les ados non appariés
        ArrayList<Adolescents> candidats = new ArrayList<>();
        for (Adolescents ado : tableViewListe.getItems()) {
            if (!isAdoApparie(ado) && !ado.equals(desist)) {
                candidats.add(ado);
            }
        }

        Adolescents meilleur = null;
        double meilleurScore = Double.NEGATIVE_INFINITY;
        for (Adolescents candidat : candidats) {
            if (desist.compatibility(candidat) && candidat.compatibility(desist)) {
                double score = desist.getScorePourAppariement(candidat);
                if (score > meilleurScore) {
                    meilleurScore = score;
                    meilleur = candidat;
                }
            }
        }

        if (meilleur != null) {
            PaireAdo nouvellePaire = new PaireAdo(desist, meilleur, meilleurScore);
            pairesValidees.add(nouvellePaire);
            tableViewPaires.getItems().setAll(pairesValidees);
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Nouvelle paire trouvée :\n" +
                            desist.getNom() + " " + desist.getPrenom() + " & " +
                            meilleur.getNom() + " " + meilleur.getPrenom() +
                            "\nScore : " + meilleurScore);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Aucun correspondant compatible trouvé pour " +
                            desist.getNom() + " " + desist.getPrenom());
            alert.showAndWait();
        }
    }

    public static class PaireAdo {
        private final Adolescents ado1;
        private final Adolescents ado2;
        private final double score;

        public PaireAdo(Adolescents ado1, Adolescents ado2, double score) {
            this.ado1 = ado1;
            this.ado2 = ado2;
            this.score = score;
        }

        public String getPrenom() {
            return ado1.getPrenom();
        }

        public String getNom() {
            return ado1.getNom();
        }

        public String getNom2() {
            return ado2.getNom();
        }

        public String getPrenom2() {
            return ado2.getPrenom();
        }

        public String getAdo1() {
            return ado1.getNom() + " " + ado1.getPrenom();
        }

        public String getAdo2() {
            return ado2.getNom() + " " + ado2.getPrenom();
        }

        public String getScore() {
            return String.valueOf(score);
        }
    }

    private void refreshAffectationsList() {
        ArrayList<Adolescents> liste = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader();
            String[] lignes = reader.getDataFromCSV("./res/Ado.csv");
            for (String ligne : lignes) {
                String[] champs = ligne.split(";");
                if (champs.length == 12) {
                    Adolescents ado = new Adolescents(
                            champs[0], champs[1], champs[2], champs[3], champs[4], champs[5],
                            champs[6], champs[7], champs[8], champs[9], champs[10], champs[11]);
                    if (!isAdoApparie(ado)) {
                        liste.add(ado);
                    }
                }
            }
        } catch (FileNotFoundException | InvalidStructureException e) {
            System.err.println("Erreur lors du chargement du fichier : " + e.getMessage());
        }
        tableViewListe.getItems().setAll(liste);
    }
}