package Staff;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StaffDashboardController implements Initializable {

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ComboBox<String> languageBox;
    @FXML
    private Button logoutButton;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.resourceBundle = rb;
        System.out.println("mainBorderPane: " + mainBorderPane);
    }

    @FXML
    void setBooking() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Staff/BookingForm.fxml"), resourceBundle);
            Parent root = loader.load();

            Stage bookingStage = new Stage();
            bookingStage.setTitle(resourceBundle.getString("title.create.booking"));
            bookingStage.setScene(new Scene(root));
            bookingStage.initModality(Modality.APPLICATION_MODAL);
            bookingStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error loading BookingForm FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void viewBookings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Staff/StaffBookings.fxml"), resourceBundle);
            Parent bookingsView = loader.load();

            StaffBookingsController bookingController = loader.getController();
            bookingController.setMainController(this);

            mainBorderPane.setCenter(bookingsView);
        } catch (IOException e) {
            System.err.println("Error loading StaffBookings FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void viewAttractions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Staff/StaffAttractions.fxml"), resourceBundle);
            Parent attractionsView = loader.load();

            StaffAttractionController attractionsController = loader.getController();
            attractionsController.setMainController(this);

            mainBorderPane.setCenter(attractionsView);
        } catch (IOException e) {
            System.err.println("Error loading StaffAttractions FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void setLogin() {
        // TODO: Implement logic to return to the login screen, also passing the resourceBundle.
    }
}