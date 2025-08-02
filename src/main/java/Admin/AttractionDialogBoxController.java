package Admin;

import Model.Attraction;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AttractionDialogBoxController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private TextField altitudeField;

    private boolean submitted = false;
    private Attraction attractionToEdit = null;

    public boolean isSubmitted() {
        return submitted;
    }

    public String getId() {
        return idField.getText();
    }

    public String getName() {
        return nameField.getText();
    }

    public String getLocation() {
        return locationField.getText();
    }

    public double getAltitude() {
        try {
            return Double.parseDouble(altitudeField.getText());
        } catch (NumberFormatException e) {
            showError("Altitude must be a valid number.");
            throw e;
        }
    }

    public void setAttractionToEdit(Attraction attraction) {
        this.attractionToEdit = attraction;
        if (attraction != null) {
            idField.setText(attraction.getId());
            nameField.setText(attraction.getName());
            locationField.setText(attraction.getLocation());
            altitudeField.setText(String.valueOf(attraction.getAltitude()));
        }
    }

    @FXML
    private void handleAdd() {
        if (idField.getText().isBlank() || nameField.getText().isBlank()
                || locationField.getText().isBlank() || altitudeField.getText().isBlank()) {
            showError("All fields are required.");
            return;
        }

        try {
            Double.parseDouble(altitudeField.getText()); // Validate only
        } catch (NumberFormatException e) {
            showError("Altitude must be a valid number.");
            return;
        }

        submitted = true;
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleCancel() {
        submitted = false;
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }
}
