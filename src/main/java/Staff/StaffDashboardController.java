package Staff;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class StaffDashboardController {

    @FXML
    private ComboBox<String> languageBox;

    @FXML
    public void initialize() {
        // Add supported languages
        languageBox.getItems().addAll("English", "Nepali");
        languageBox.setValue("English");

        languageBox.setOnAction(e -> {
            String selected = languageBox.getValue();
            Locale newLocale;
            switch (selected) {
                case "Nepali" -> newLocale = new Locale("np");
                default -> newLocale = new Locale("en");
            }
            reloadUI(newLocale);
        });
    }

    private void reloadUI(Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Messages", locale);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Staff/StaffDashboard.fxml"), bundle);
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) languageBox.getScene().getWindow();
            stage.setScene(scene);

            // Restore language selection after reload
            StaffDashboardController controller = loader.getController();
            controller.languageBox.setValue(locale.getLanguage().equals("np") ? "Nepali" : "English");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void setLogin() {
        System.out.println("Logout clicked");
        Stage stage = (Stage) languageBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void setBooking() {
        System.out.println("Booking clicked");
    }
}
