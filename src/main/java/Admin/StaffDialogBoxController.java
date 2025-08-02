package Admin;

import Model.Staff;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StaffDialogBoxController {

    // FXML components
    @FXML private TextField staffIdField;
    @FXML private TextField nameField;
    @FXML private TextField contactField;
    @FXML private TextField emailField;
    @FXML private TextField positionField;
    @FXML private TextField addressField;
    @FXML private TextField nationalityField;
    @FXML private PasswordField passwordField;
    @FXML private DatePicker dobPicker;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private StaffController staffController;

    // Method to set a reference to the main StaffController
    public void setStaffController(StaffController staffController) {
        this.staffController = staffController;
    }

    // Handle the Save button action
    @FXML
    private void handleSaveStaff() {
        // Get the data from the fields
        String email = emailField.getText();
        String name = nameField.getText();
        String address = addressField.getText();
        String nationality = nationalityField.getText();
        String contact = contactField.getText();
        String password = passwordField.getText();
        LocalDate dob = dobPicker.getValue();

        // Validate the input data
        if (email.isEmpty() || name.isEmpty() || address.isEmpty() || nationality.isEmpty() || contact.isEmpty() || password.isEmpty() || dob == null) {
            showAlert("Input Error", "Please fill in all fields.");
            return;
        }

        // Format the date to the correct "dd/MM/yyyy" pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dobString = dob.format(formatter);

        // Create a new staff object
        Staff newStaff = new Staff(email, name, address, nationality, contact, password, dobString);

        // Pass the new staff object to the main controller
        if (staffController != null) {
            staffController.addNewStaff(newStaff);
        }

        // Close the dialog after saving
        closeDialog();
    }

    // Handle the Cancel button action
    @FXML
    private void handleCancel() {
        // Just close the dialog without saving
        closeDialog();
    }

    // Method to show an alert message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to close the dialog
    private void closeDialog() {
        // Close the current window (dialog)
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}