package ap.assignment.indira;

import Model.Staff;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegisterController {

    @FXML private ResourceBundle resources;
    @FXML private TextField emailField;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField nationalityField;
    @FXML private DatePicker dobPicker;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private Button registerButton;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
    }

    @FXML
    public void onRegister(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String name = nameField.getText();
        String address = addressField.getText();
        String nationality = nationalityField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();
        String dob = (dobPicker.getValue() != null) ? dobPicker.getValue().toString() : "";

        // Create a data object
        Staff data = new Staff (email, name, address, nationality, phone, password, dob);

        // Convert to JSON and save
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data);

        // Choose a file path (you can customize this)
        String filePath = "/Data/staff.json";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        }

        // Optional: show confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Registration data saved successfully!");
        alert.showAndWait();
    }

    @FXML
    public void onBack(ActionEvent event) throws IOException {
        // Load resource bundle for localization
        ResourceBundle bundle = ResourceBundle.getBundle("Messages", Locale.ENGLISH);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/ap/assignment/View/Login/Login.fxml"), bundle);
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        // Get the current stage from the event source
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        stage.setTitle(bundle.getString("window.title"));
        stage.setScene(scene);
        stage.show();
    }
}
