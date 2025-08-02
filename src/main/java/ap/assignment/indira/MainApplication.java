package ap.assignment.indira;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Launching Initially with English Bundle
        ResourceBundle bundle = ResourceBundle.getBundle("Messages", Locale.ENGLISH);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/ap/assignment/View/Login/Login.fxml"), bundle);
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle(bundle.getString("window.title"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}