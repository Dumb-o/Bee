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
import java.io.InputStreamReader; // Added import
import java.io.File;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDialogBoxController {

    @FXML private TextField idField;
    @FXML private TextField touristField;
    @FXML private TextField packageField;
    @FXML private ComboBox<Guide> guideComboBox;
    @FXML private DatePicker travelDateField;
    @FXML private TextField amountField;

    private static final String JSON_FILE = "bookings.json";
    private static final String GUIDE_FILE_PATH = "/Data/guides.json"; // Corrected file path

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
        idField.setText(String.valueOf(getNextBookingId()));
    }

    public void setUpdateMode(Booking booking) {
        isUpdateMode = true;
        bookingForUpdate = booking;

        idField.setText(String.valueOf(booking.getId()));
        touristField.setText(booking.getTourist());
        // Corrected: Uses getPackageName() to match the Booking model
        packageField.setText(booking.getPackageName());

        if (booking.getGuide() != null && !booking.getGuide().isEmpty()) {
            for (Guide guide : guideList) {
                if (guide.getName().equals(booking.getGuide())) {
                    guideComboBox.setValue(guide);
                    break;
                }
            }
        }

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
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(GUIDE_FILE_PATH))) {
            Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
            Type guideListType = new TypeToken<List<Guide>>(){}.getType();
            List<Guide> guideData = gson.fromJson(reader, guideListType);

            guideList.clear();
            if (guideData != null) {
                guideList.addAll(guideData);
            }
        } catch (Exception e) {
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

    @FXML
    private void handleSaveBooking() {
        if (validateFields()) {
            String touristName = touristField.getText();
            String packageName = packageField.getText();
            String guide = guideComboBox.getValue() != null ? guideComboBox.getValue().getName() : "";
            LocalDate travelDate = travelDateField.getValue();
            String amount = amountField.getText();

            savedBooking = new Booking(
                    Integer.parseInt(idField.getText()),
                    touristName,
                    packageName,
                    guide,
                    travelDate,
                    Double.parseDouble(amount)
            );

            Stage stage = (Stage) touristField.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleCancel() {
        resetFields();
        Stage stage = (Stage) touristField.getScene().getWindow();
        stage.close();
    }

    private boolean validateFields() {
        if (touristField.getText().isEmpty() || packageField.getText().isEmpty() ||
                travelDateField.getValue() == null || amountField.getText().isEmpty()) {

            showAlert("Validation Error", "Tourist name, package, travel date, and amount are required!");
            return false;
        }
        return true;
    }

    private void resetFields() {
        touristField.clear();
        packageField.clear();
        guideComboBox.setValue(null);
        travelDateField.setValue(null);
        amountField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveBookingToJson(Booking booking) {
        try {
            List<Booking> bookings = loadBookingsFromJson();
            bookings.add(booking);
            Gson gson = new Gson();
            String json = gson.toJson(bookings);

            try (FileWriter writer = new FileWriter(JSON_FILE)) {
                writer.write(json);
                System.out.println("Booking saved to JSON file!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public Booking getBooking() {
        return savedBooking;
    }
}
