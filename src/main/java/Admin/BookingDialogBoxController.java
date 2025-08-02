package Admin;

import Model.Booking;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDialogBoxController {

    @FXML private TextField idField;  // Changed to TextField instead of Text
    @FXML private TextField touristField;
    @FXML private TextField packageField;
    @FXML private TextField guideField;
    @FXML private DatePicker travelDateField;
    @FXML private TextField amountField;

    private static final String JSON_FILE = "bookings.json";  // Path to save the JSON file

    private Booking savedBooking;

    // Called when the "Save" button is pressed
    @FXML
    private void handleSaveBooking() {
        // Validate all the fields are filled
        if (validateFields()) {
            String touristName = touristField.getText();
            String packageName = packageField.getText();
            String guide = guideField.getText();
            LocalDate travelDate = travelDateField.getValue();
            String amount = amountField.getText();

            // Create a booking object
            savedBooking = new Booking(
                    Integer.parseInt(idField.getText()),  // Parse ID from TextField
                    touristName,
                    packageName,
                    guide,
                    travelDate,
                    Double.parseDouble(amount)
            );

            // Save the booking data to a JSON file
            saveBookingToJson(savedBooking);

            // Optionally, close the dialog box
            Stage stage = (Stage) touristField.getScene().getWindow();
            stage.close();
        }
    }

    // Called when the "Cancel" button is pressed
    @FXML
    private void handleCancel() {
        // Optionally reset fields or close the dialog box
        resetFields();
        Stage stage = (Stage) touristField.getScene().getWindow();
        stage.close();
    }

    // Validate that all the required fields are filled
    private boolean validateFields() {
        if (touristField.getText().isEmpty() || packageField.getText().isEmpty() || guideField.getText().isEmpty() ||
                travelDateField.getValue() == null || amountField.getText().isEmpty()) {

            // Show an alert if validation fails
            showAlert("Validation Error", "All fields must be filled out!");
            return false;
        }
        return true;
    }

    // Reset all fields after cancellation
    private void resetFields() {
        touristField.clear();
        packageField.clear();
        guideField.clear();
        travelDateField.setValue(null);
        amountField.clear();
    }

    // Show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Save the booking details to a JSON file
    private void saveBookingToJson(Booking booking) {
        try {
            // Read existing bookings from the JSON file if it exists
            List<Booking> bookings = loadBookingsFromJson();

            // Add the new booking to the list
            bookings.add(booking);

            // Create a Gson object to convert the list into JSON
            Gson gson = new Gson();
            String json = gson.toJson(bookings);

            // Write the JSON to a file
            try (FileWriter writer = new FileWriter(JSON_FILE)) {
                writer.write(json);
                System.out.println("Booking saved to JSON file!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load existing bookings from the JSON file
    private List<Booking> loadBookingsFromJson() {
        List<Booking> bookings = new ArrayList<>();

        try {
            File file = new File(JSON_FILE);
            if (file.exists()) {
                Gson gson = new Gson();
                bookings = gson.fromJson(new java.io.FileReader(file), new TypeToken<List<Booking>>(){}.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    // Return the saved booking
    public Booking getBooking() {
        return savedBooking;
    }
}
