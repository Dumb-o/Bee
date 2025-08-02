package Admin;

import Model.Attraction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class AttractionsController {

    @FXML private TableView<Attraction> attractionsTable;
    @FXML private TableColumn<Attraction, String> idColumn;
    @FXML private TableColumn<Attraction, String> nameColumn;
    @FXML private TableColumn<Attraction, String> locationColumn;
    @FXML private TableColumn<Attraction, Double> altitudeColumn;

    private final ObservableList<Attraction> attractionList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        altitudeColumn.setCellValueFactory(new PropertyValueFactory<>("altitude"));

        attractionList.setAll(Attraction.loadAttractions());
        attractionsTable.setItems(attractionList);
    }

    @FXML
    private void handleAddAttraction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Admin/AttractionsDialogBox.fxml"));
            VBox dialogRoot = loader.load();

            AttractionDialogBoxController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Attraction");
            dialogStage.setScene(new Scene(dialogRoot));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();

            if (controller.isSubmitted()) {
                Attraction newAttraction = new Attraction(
                        controller.getId(), controller.getName(), controller.getLocation(), controller.getAltitude()
                );
                attractionList.add(newAttraction);
                Attraction.saveAttractions(attractionList);  // Save the updated observable list
            }

            attractionsTable.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateAttraction() {
        Attraction selected = attractionsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarning("No Selection", "Please select an attraction to update.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Admin/AttractionsDialogBox.fxml"));
            VBox dialogRoot = loader.load();

            AttractionDialogBoxController controller = loader.getController();
            controller.setAttractionToEdit(selected);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Attraction");
            dialogStage.setScene(new Scene(dialogRoot));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();

            if (controller.isSubmitted()) {
                // Update selected attraction fields
                selected.setId(controller.getId());
                selected.setName(controller.getName());
                selected.setLocation(controller.getLocation());
                selected.setAltitude(controller.getAltitude());

                attractionsTable.refresh();
                Attraction.saveAttractions(attractionList);  // Save updated list
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteAttraction() {
        Attraction selected = attractionsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            attractionList.remove(selected);
            Attraction.saveAttractions(attractionList);
        } else {
            showWarning("No Selection", "Please select an attraction to delete.");
        }
    }

    private void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
