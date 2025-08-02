package Admin;

import Model.Booking;
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

import java.io.IOException;
import java.time.format.DateTimeFormatter;

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
    }

    @FXML
    private void handleAddBooking(ActionEvent event) {
        try {
            // Load the Booking Dialog Box FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Admin/BookingDialogBox.fxml"));
            VBox dialogBox = loader.load();

            // Create a new Scene and set it into the stage
            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(dialogBox));
            dialogStage.setTitle("Add New Booking");
            dialogStage.showAndWait();

            // Get the BookingDialogBoxController and add the booking to the table once it's saved
            BookingDialogBoxController controller = loader.getController();
            Booking newBooking = controller.getBooking();  // Assuming the controller has a method to return the saved booking
            if (newBooking != null) {
                bookingsList.add(newBooking);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateBooking(ActionEvent event) {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Update Booking for ID: " + selected.getId());
            // TODO: Implement update booking logic
        } else {
            showAlert("No Selection", "Please select a booking to update.");
        }
    }

    @FXML
    private void handleDeleteBooking(ActionEvent event) {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            bookingsList.remove(selected);
            System.out.println("Deleted booking with ID: " + selected.getId());
            // TODO: Implement actual delete logic (DB etc)
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
