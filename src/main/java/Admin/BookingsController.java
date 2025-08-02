package Admin;

import Model.Booking;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingsController {

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, Integer> idColumn;

    @FXML
    private TableColumn<Booking, String> touristColumn;

    @FXML
    private TableColumn<Booking, String> packageColumn;

    @FXML
    private TableColumn<Booking, String> guideColumn;

    @FXML
    private TableColumn<Booking, String> travelDateColumn;

    @FXML
    private TableColumn<Booking, String> statusColumn;

    @FXML
    private TableColumn<Booking, Double> amountColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    private ObservableList<Booking> bookingsList = FXCollections.observableArrayList();
    private static final String BOOKING_FILE_PATH = "src/main/resources/Data/bookings.json";

    @FXML
    public void initialize() {
        // Initialize the columns with correct property bindings
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        touristColumn.setCellValueFactory(cellData -> cellData.getValue().touristProperty());
        packageColumn.setCellValueFactory(cellData -> cellData.getValue().packageProperty());
        guideColumn.setCellValueFactory(cellData -> cellData.getValue().guideProperty());

        // Format LocalDate into String for TableColumn
        travelDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTravelDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());

        bookingsTable.setItems(bookingsList);
        
        // Load existing bookings
        loadBookings();
    }

    private void loadBookings() {
        try (FileReader reader = new FileReader(BOOKING_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
            Type bookingListType = new TypeToken<List<Booking>>(){}.getType();
            List<Booking> bookingData = gson.fromJson(reader, bookingListType);

            bookingsList.clear();
            if (bookingData != null) {
                bookingsList.addAll(bookingData);
            }
        } catch (IOException e) {
            System.out.println("No existing bookings found or error loading bookings: " + e.getMessage());
        }
    }

    private void saveBookings() {
        try (FileWriter writer = new FileWriter(BOOKING_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
            gson.toJson(bookingsList, writer);
            System.out.println("Bookings saved to " + BOOKING_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save bookings to file.");
        }
    }

    @FXML
    private void handleAddBooking(ActionEvent event) {
        try {
            // Load the Booking Dialog Box FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Admin/BookingDialogBox.fxml"));
            VBox dialogBox = loader.load();

            // Get the controller and set it to add mode
            BookingDialogBoxController controller = loader.getController();
            controller.setAddMode();

            // Create a new Scene and set it into the stage
            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(dialogBox));
            dialogStage.setTitle("Add New Booking");
            dialogStage.showAndWait();

            // Get the saved booking and add it to the table
            Booking newBooking = controller.getBooking();
            if (newBooking != null) {
                bookingsList.add(newBooking);
                saveBookings();
                System.out.println("Added new booking: " + newBooking.getTourist());
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Add Booking dialog.");
        }
    }

    @FXML
    private void handleUpdateBooking(ActionEvent event) {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Load the Booking Dialog Box FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Admin/BookingDialogBox.fxml"));
                VBox dialogBox = loader.load();

                // Get the controller and set it to update mode
                BookingDialogBoxController controller = loader.getController();
                controller.setUpdateMode(selected);

                // Create a new Scene and set it into the stage
                Stage dialogStage = new Stage();
                dialogStage.setScene(new Scene(dialogBox));
                dialogStage.setTitle("Update Booking");
                dialogStage.showAndWait();

                // Get the updated booking and update it in the table
                Booking updatedBooking = controller.getBooking();
                if (updatedBooking != null) {
                    int index = bookingsList.indexOf(selected);
                    if (index != -1) {
                        bookingsList.set(index, updatedBooking);
                        saveBookings();
                        System.out.println("Updated booking: " + updatedBooking.getTourist());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open Update Booking dialog.");
            }
        } else {
            showAlert("No Selection", "Please select a booking to update.");
        }
    }

    @FXML
    private void handleDeleteBooking(ActionEvent event) {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Booking");
            alert.setContentText("Are you sure you want to delete the booking for: " + selected.getTourist() + "?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    bookingsList.remove(selected);
                    saveBookings();
                    System.out.println("Deleted booking with ID: " + selected.getId());
                }
            });
        } else {
            showAlert("No Selection", "Please select a booking to delete.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
