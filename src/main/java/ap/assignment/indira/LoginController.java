package ap.assignment.indira;

import Model.Staff;
import com.google.gson.GsonBuilder;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    public void onLoginClick(ActionEvent event) {
        String enteredEmail = emailField.getText();
        String enteredPassword = passwordField.getText();

        if (validateLogin(enteredEmail, enteredPassword)) {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle("Messages", Locale.ENGLISH);

                String fxmlPath;
                if (enteredEmail.toLowerCase().contains("@admin")) {
                    fxmlPath = "/ap/assignment/View/Admin/AdminDashboard.fxml";
                } else {
                    fxmlPath = "/ap/assignment/View/Staff/StaffDashboard.fxml";
                }

                // Check FXML path
                var fxmlUrl = getClass().getResource(fxmlPath);
                if (fxmlUrl == null) {
                    throw new IllegalStateException("FXML not found at: " + fxmlPath);
                }

                FXMLLoader loader = new FXMLLoader(fxmlUrl, bundle);
                Parent root = loader.load();

                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle(
                        enteredEmail.toLowerCase().contains("@admin") ?
                                bundle.getString("title.administrator") :
                                bundle.getString("title.guide")
                );

            } catch (IOException e) {
                System.err.println("Error loading FXML: " + e.getMessage());
                e.printStackTrace();
            } catch (IllegalStateException e) {
                System.err.println("IllegalStateException: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    @FXML
    public void onRegisterClick(ActionEvent event) {
        try {
            // Load Register.fxml from the same package
            ResourceBundle bundle = ResourceBundle.getBundle("Messages");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Login/Register.fxml"));
            loader.setResources(bundle);  // Set the bundle here
            Parent registerRoot = loader.load();

            // Get current stage (window) from any control, here from loginButton
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set new scene for registration screen
            stage.setScene(new Scene(registerRoot));
            stage.setTitle("Register Staff");
            stage.show();

        } catch (IOException e) {
            System.err.println("Error loading Register FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateLogin(String email, String password) {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/Data/staff.json"))) {
            // Create a GsonBuilder and register the StringPropertyAdapter and LocalDateAdapter
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(StringProperty.class, new StringPropertyAdapter())  // Register the custom StringProperty adapter
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())  // Register the custom LocalDate adapter
                    .create();

            Type listType = new TypeToken<List<Staff>>(){}.getType();
            List<Staff> staffList = gson.fromJson(reader, listType);

            // If staffList is null or empty, log that and return false
            if (staffList == null || staffList.isEmpty()) {
                System.out.println("No staff data found or invalid JSON format.");
                return false;
            }

            // Validate the credentials by checking each staff entry
            return staffList.stream()
                    .anyMatch(s -> s.getEmail().equalsIgnoreCase(email) && s.getPassword().equals(password));
        } catch (IOException e) {
            System.err.println("Error reading staff data: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error during login validation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
