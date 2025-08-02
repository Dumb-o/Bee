package Admin;

import Model.Guide;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GuideDialogBoxController {

    @FXML
    private Label titleLabel;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField contactField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nationalityField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private GuideController guideController;
    private Guide guideForUpdate;
    private boolean isUpdateMode = false;

    /**
     * Sets the reference to the main GuideController.
     */
    public void setGuideController(GuideController guideController) {
        this.guideController = guideController;
    }

    /**
     * Sets the guide for update mode and populates the fields.
     */
    public void setGuideForUpdate(Guide guide) {
        this.guideForUpdate = guide;
        this.isUpdateMode = true;
        
        // Update the title
        titleLabel.setText("Update Guide");
        
        // Populate the fields with existing data
        idField.setText(guide.getId());
        nameField.setText(guide.getName());
        contactField.setText(guide.getContact());
        emailField.setText(guide.getEmail());
        nationalityField.setText(guide.getNationality());
        
        // Disable ID field for updates (ID should not be changed)
        idField.setDisable(true);
    }

    @FXML
    private void handleSave() {
        // Validate input
        if (!validateInput()) {
            return;
        }

        if (isUpdateMode) {
            // Update existing guide
            guideForUpdate.setName(nameField.getText().trim());
            guideForUpdate.setContact(contactField.getText().trim());
            guideForUpdate.setEmail(emailField.getText().trim());
            guideForUpdate.setNationality(nationalityField.getText().trim());
            
            guideController.updateGuide(guideForUpdate);
        } else {
            // Create new guide
            Guide newGuide = new Guide(
                idField.getText().trim(),
                nameField.getText().trim(),
                contactField.getText().trim(),
                emailField.getText().trim(),
                nationalityField.getText().trim()
            );
            
            guideController.addNewGuide(newGuide);
        }

        // Close the dialog
        closeDialog();
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private boolean validateInput() {
        // Check if all fields are filled
        if (idField.getText().trim().isEmpty() && !isUpdateMode) {
            showAlert("Validation Error", "Guide ID is required.");
            return false;
        }
        
        if (nameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Name is required.");
            return false;
        }
        
        if (contactField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Contact is required.");
            return false;
        }
        
        if (emailField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Email is required.");
            return false;
        }
        
        if (nationalityField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Nationality is required.");
            return false;
        }

        // Validate email format (simple validation)
        String email = emailField.getText().trim();
        if (!email.contains("@") || !email.contains(".")) {
            showAlert("Validation Error", "Please enter a valid email address.");
            return false;
        }

        return true;
    }

    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 