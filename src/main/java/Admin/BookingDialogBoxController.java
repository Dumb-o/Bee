package Admin;

import Model.Booking;
import Model.Guide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDialogBoxController {

    @FXML private TextField idField;  // Changed to TextField instead of Text
    @FXML private TextField touristField;
    @FXML private TextField packageField;
    @FXML private ComboBox<Guide> guideComboBox;
    @FXML private DatePicker travelDateField;
    @FXML private TextField amountField;

    private static final String JSON_FILE = "bookings.json";  // Path to save the JSON file
    private static final String GUIDE_FILE_PATH = "src/main/resources/Data/guides.json";

    private Booking savedBooking;
    private ObservableList<Guide> guideList = FXCollections.observableArrayList();
    private boolean isUpdateMode = false;
    private Booking bookingForUpdate;

    @FXML
    public void initialize() {
        loadGuides();
        setupGuideComboBox();
    }

    public void setAddMode() {
        isUpdateMode = false;
        bookingForUpdate = null;
        resetFields();
        // Set a default ID for new booking
        idField.setText(String.valueOf(getNextBookingId()));
    }

    public void setUpdateMode(Booking booking) {
        isUpdateMode = true;
        bookingForUpdate = booking;
        
        // Populate fields with existing data
        idField.setText(String.valueOf(booking.getId()));
        touristField.setText(booking.getTourist());
        packageField.setText(booking.getPackage());
        
        // Set guide if exists
        if (booking.getGuide() != null && !booking.getGuide().isEmpty()) {
            for (Guide guide : guideList) {
                if (guide.getName().equals(booking.getGuide())) {
                    guideComboBox.setValue(guide);
                    break;
                }
            }
        }
        
        // Set travel date
        if (booking.getTravelDate() != null) {
            travelDateField.setValue(booking.getTravelDate());
        }
        
        amountField.setText(String.valueOf(booking.getAmount()));
    }

    private int getNextBookingId() {
        int maxId = 0;
        for (Booking booking : loadBookingsFromJson()) {
            if (booking.getId() > maxId) {
                maxId = booking.getId();
            }
        }
        return maxId + 1;
    }

    private void loadGuides() {
        try (FileReader reader = new FileReader(GUIDE_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
            Type guideListType = new TypeToken<List<Guide>>(){}.getType();
            List<Guide> guideData = gson.fromJson(reader, guideListType);

            guideList.clear();
            if (guideData != null) {
                guideList.addAll(guideData);
            }
        } catch (IOException e) {
            System.out.println("No guides found or error loading guides: " + e.getMessage());
        }
    }

    private void setupGuideComboBox() {
        guideComboBox.setItems(guideList);
        guideComboBox.setCellFactory(param -> new ListCell<Guide>() {
            @Override
            protected void updateItem(Guide item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " (" + item.getId() + ")");
                }
            }
        });
        guideComboBox.setButtonCell(guideComboBox.getCellFactory().call(null));
    }

    // Called when the "Save" button is pressed
    @FXML
    private void handleSaveBooking() {
        // Validate all the fields are filled
        if (validateFields()) {
            String touristName = touristField.getText();
            String packageName = packageField.getText();
            String guide = guideComboBox.getValue() != null ? guideComboBox.getValue().getName() : "";
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

            // Close the dialog box immediately
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
        if (touristField.getText().isEmpty() || packageField.getText().isEmpty() ||
                travelDateField.getValue() == null || amountField.getText().isEmpty()) {

            // Show an alert if validation fails
            showAlert("Validation Error", "Tourist name, package, travel date, and amount are required!");
            return false;
        }
        return true;
    }

    // Reset all fields after cancellation
    private void resetFields() {
        touristField.clear();
        packageField.clear();
        guideComboBox.setValue(null);
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
