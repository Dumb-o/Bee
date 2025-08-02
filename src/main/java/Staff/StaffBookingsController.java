package Staff;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffBookingsController implements Initializable {

    @FXML
    private TableView bookingTable;
    @FXML
    private TableColumn customerNameColumn;
    @FXML
    private TableColumn attractionNameColumn;
    @FXML
    private TableColumn dateColumn;
    @FXML
    private TableColumn timeColumn;
    @FXML
    private TableColumn statusColumn;

    private StaffDashboardController mainController;
    private ResourceBundle resourceBundle;

    public void setMainController(StaffDashboardController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.resourceBundle = rb;
        // Initialization logic for bookings table goes here
    }

    @FXML
    private void setBooking() {
        if (mainController != null) {
            mainController.setBooking();
        }
    }

    @FXML
    private void viewBookings() {
        // Already on this page, do nothing
        System.out.println("Already on the View Bookings page.");
    }

    @FXML
    private void viewAttractions() {
        if (mainController != null) {
            mainController.viewAttractions();
        }
    }

    @FXML
    private void setLogin() {
        if (mainController != null) {
            mainController.setLogin();
        }
    }
}