package Staff;

import Model.Booking;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffBookingsController implements Initializable {

    @FXML
    private TableView<Booking> bookingTable;
    @FXML
    private TableColumn<Booking, String> customerNameColumn;
    @FXML
    private TableColumn<Booking, String> attractionNameColumn;
    @FXML
    private TableColumn<Booking, String> dateColumn;
    @FXML
    private TableColumn<Booking, String> timeColumn;
    @FXML
    private TableColumn<Booking, String> statusColumn;

    private StaffDashboardController mainController;
    private ResourceBundle resourceBundle;

    public void setMainController(StaffDashboardController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.resourceBundle = rb;

        // Corrected: The property name "tourist" now correctly points to the getTourist() method.
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("tourist"));
        // Corrected: The property name "packageName" now correctly points to the getPackageName() method.
        attractionNameColumn.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("travelDate"));
        // Assuming there is a time property in Booking model
        // timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        // Assuming there is a status property in Booking model
        // statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load bookings from the model and set them in the table
        bookingTable.setItems(Booking.loadBookings());
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
