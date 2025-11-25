import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/vue/menu.fxml")));

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Accueil");
        stage.setMaximized(true);

        stage.show();
    }
}
