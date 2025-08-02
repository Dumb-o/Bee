// ✅ AdminDashboardController.java (no major changes needed, minor cleanup)
package Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AdminDashboardController {

    @FXML
    private ScrollPane contentPane;

    @FXML
    private ComboBox<String> languageBox;

    private Locale currentLocale = new Locale("en");

    @FXML
    public void initialize() {
        languageBox.getItems().addAll("English", "Nepali");
        languageBox.setValue("English");

        languageBox.setOnAction(e -> {
            String selected = languageBox.getValue();
            switch (selected) {
                case "Nepali" -> currentLocale = new Locale("np");
                default -> currentLocale = new Locale("en");
            }
        });
    }

    private void reloadUI() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Messages", currentLocale);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Admin/AdminDashboard.fxml"), bundle);
            Parent root = loader.load();

            var stage = (javafx.stage.Stage) languageBox.getScene().getWindow();
            stage.getScene().setRoot(root);

            AdminDashboardController controller = loader.getController();
            controller.languageBox.setValue(currentLocale.getLanguage().equals("np") ? "Nepali" : "English");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setResources(ResourceBundle.getBundle("Messages", currentLocale));
            Parent view = loader.load();
            contentPane.setContent(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setDashboard(ActionEvent event) {
        reloadUI();
    }

    @FXML
    private void setBooking(ActionEvent event) {
        loadContent("/ap/assignment/View/Admin/Bookings.fxml");
    }

    @FXML
    private void setAccounts(ActionEvent event) {
        loadContent("/ap/assignment/View/Admin/Accounts.fxml");
    }

    @FXML
    private void setFestival(ActionEvent event) {
        loadContent("/ap/assignment/View/Admin/Festival.fxml");
    }

    @FXML
    private void setGuide(ActionEvent event) {
        loadContent("/ap/assignment/View/Admin/Guide.fxml");
    }

    @FXML
    private void setStaff(ActionEvent event) {
        loadContent("/ap/assignment/View/Admin/Staff.fxml");
    }

    @FXML
    private void setAttractions(ActionEvent event) {
        loadContent("/ap/assignment/View/Admin/Attractions.fxml");
    }

    @FXML
    private void setLogin(ActionEvent event) {
        System.out.println("Logout clicked");
        Stage stage = (Stage) languageBox.getScene().getWindow();
        stage.close();
    }
}
