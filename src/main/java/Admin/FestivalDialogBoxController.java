package Admin;

import Model.Festival;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FestivalDialogBoxController {

    @FXML
    private Label titleLabel;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private DatePicker dateFromPicker;

    @FXML
    private DatePicker dateToPicker;

    @FXML
    private TextField discountField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private FestivalController festivalController;
    private Festival festivalForUpdate;
    private boolean isUpdateMode = false;

    /**
     * Set the festival controller reference
     */
    public void setFestivalController(FestivalController controller) {
        this.festivalController = controller;
    }

    /**
     * Set the dialog to add mode
     */
    public void setAddMode() {
        isUpdateMode = false;
        titleLabel.setText("Add New Festival");
        clearFields();
    }

    /**
     * Set the dialog to update mode with existing festival data
     */
    public void setUpdateMode(Festival festival) {
        isUpdateMode = true;
        festivalForUpdate = festival;
        titleLabel.setText("Update Festival");
        
        // Populate fields with existing data
        idField.setText(festival.getId());
        nameField.setText(festival.getName());
        
        // Parse and set dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate dateFrom = LocalDate.parse(festival.getDateFrom(), formatter);
            dateFromPicker.setValue(dateFrom);
        } catch (Exception e) {
            dateFromPicker.setValue(null);
        }
        
        try {
            LocalDate dateTo = LocalDate.parse(festival.getDateTo(), formatter);
            dateToPicker.setValue(dateTo);
        } catch (Exception e) {
            dateToPicker.setValue(null);
        }
        
        discountField.setText(String.valueOf(festival.getDiscount()));
    }

    @FXML
    private void saveFestival() {
        // Validate input fields
        if (!validateInput()) {
            return;
        }

        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            
            // Get dates from DatePickers
            LocalDate dateFromValue = dateFromPicker.getValue();
            LocalDate dateToValue = dateToPicker.getValue();
            
            if (dateFromValue == null || dateToValue == null) {
                showAlert("Error", "Please select both start and end dates.");
                return;
            }
            
            String dateFrom = dateFromValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String dateTo = dateToValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            double discount = Double.parseDouble(discountField.getText().trim());

            Festival festival = new Festival(id, name, dateFrom, dateTo, discount);

            if (isUpdateMode) {
                festivalController.updateFestivalInList(festivalForUpdate, festival);
            } else {
                festivalController.addFestivalToList(festival);
            }

            closeDialog();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid discount percentage (number).");
        }
    }

    @FXML
    private void cancelDialog() {
        closeDialog();
    }

    private boolean validateInput() {
        if (idField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter a Festival ID.");
            return false;
        }
        if (nameField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter a Festival Name.");
            return false;
        }
        if (dateFromPicker.getValue() == null) {
            showAlert("Error", "Please select a Date From.");
            return false;
        }
        if (dateToPicker.getValue() == null) {
            showAlert("Error", "Please select a Date To.");
            return false;
        }
        if (discountField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter a Discount percentage.");
            return false;
        }

        // Validate date range
        LocalDate dateFrom = dateFromPicker.getValue();
        LocalDate dateTo = dateToPicker.getValue();
        
        if (dateFrom.isAfter(dateTo)) {
            showAlert("Error", "Date From cannot be after Date To.");
            return false;
        }

        // Validate discount range
        try {
            double discount = Double.parseDouble(discountField.getText().trim());
            if (discount < 0 || discount > 100) {
                showAlert("Error", "Discount percentage must be between 0 and 100.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid discount percentage (number).");
            return false;
        }

        return true;
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        dateFromPicker.setValue(null);
        dateToPicker.setValue(null);
        discountField.clear();
    }

    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 