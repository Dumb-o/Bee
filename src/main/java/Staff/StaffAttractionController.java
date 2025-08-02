package Staff;

import Model.Attraction;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StaffAttractionController implements Initializable {

    @FXML
    private BorderPane mainBorderPane; // This fx:id is not needed here
    @FXML
    private ComboBox<String> languageBox;
    @FXML
    private Button logoutButton;

    @FXML
    private TableView<Attraction> attractionTable;
    @FXML
    private TableColumn<Attraction, String> attractionNameColumn;
    @FXML
    private TableColumn<Attraction, String> attractionLocationColumn;
    @FXML
    private TableColumn<Attraction, Double> altitudeColumn;

    // Reference to the main dashboard controller
    private StaffDashboardController mainController;
    private ResourceBundle resourceBundle;

    // Public method to set the main controller reference
    public void setMainController(StaffDashboardController mainController) {
        this.mainController = mainController;
    }

    // It's good practice to also have a way to set the resource bundle
    public void setResourceBundle(ResourceBundle rb) {
        this.resourceBundle = rb;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.resourceBundle = rb;
        // Set up the cell value factories to populate the table columns
        attractionNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        attractionLocationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        altitudeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAltitude()));

        // Load attraction data from the JSON file and populate the TableView
        attractionTable.setItems(Attraction.loadAttractions());
    }

    // Inside StaffAttractionController.java
    @FXML
    private void setBooking() {
        if (mainController != null) {
            mainController.setBooking(); // Delegate to main controller
        }
    }

    @FXML
    private void viewBookings() {
        if (mainController != null) {
            mainController.viewBookings(); // Delegate to main controller
        }
    }

    @FXML
    private void viewAttractions() {
        // We are already here, do nothing
    }

    @FXML
    private void setLogin() {
        if (mainController != null) {
            mainController.setLogin(); // Delegate to main controller
        }
    }
}