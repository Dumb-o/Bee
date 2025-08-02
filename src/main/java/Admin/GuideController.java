package Admin;

import Model.Guide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class GuideController {

    @FXML
    private TableView<Guide> guideTable;

    @FXML
    private TableColumn<Guide, String> idColumn;

    @FXML
    private TableColumn<Guide, String> nameColumn;

    @FXML
    private TableColumn<Guide, String> contactColumn;

    @FXML
    private TableColumn<Guide, String> emailColumn;

    @FXML
    private TableColumn<Guide, String> nationalityColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    // ObservableList to hold the guide data
    private ObservableList<Guide> guideList = FXCollections.observableArrayList();

    // File path to load guide data from
    private static final String GUIDE_FILE_PATH = "src/main/resources/Data/guides.json";

    // Initialize the GuideTableView with the data
    @FXML
    public void initialize() {
        // Initialize the columns with proper property bindings
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        contactColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        nationalityColumn.setCellValueFactory(cellData -> cellData.getValue().nationalityProperty());

        // Load and display guide data
        loadGuideData();
    }

    /**
     * Loads guide data from the JSON file.
     */
    private void loadGuideData() {
        try (FileReader reader = new FileReader(GUIDE_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
            Type guideListType = new TypeToken<List<Guide>>(){}.getType();
            List<Guide> guideData = gson.fromJson(reader, guideListType);

            // Clear the existing list
            guideList.clear();

            // Add guides to the ObservableList
            if (guideData != null) {
                guideList.addAll(guideData);
            }

            // Set the table items to the guide list
            guideTable.setItems(guideList);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load guide data from the file.");
        }
    }

    /**
     * Handles adding a new guide by opening a dialog box.
     */
    @FXML
    private void handleAddGuide(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Admin/GuideDialogBox.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Guide");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(addButton.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            GuideDialogBoxController dialogController = loader.getController();
            dialogController.setGuideController(this); // Pass a reference to this controller

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the guide dialog box.");
        }
    }

    /**
     * This method is called by the GuideDialogBoxController when a new guide is created.
     * @param newGuide The new guide object to add to the table.
     */
    public void addNewGuide(Guide newGuide) {
        guideList.add(newGuide);
        saveGuideData(); // Save the updated list to the file
    }

    @FXML
    private void handleUpdateGuide(ActionEvent event) {
        Guide selectedGuide = guideTable.getSelectionModel().getSelectedItem();
        if (selectedGuide != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Admin/GuideDialogBox.fxml"));
                Parent root = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Update Guide");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(updateButton.getScene().getWindow());
                dialogStage.setScene(new Scene(root));

                GuideDialogBoxController dialogController = loader.getController();
                dialogController.setGuideController(this);
                dialogController.setGuideForUpdate(selectedGuide); // Pass the selected guide for updating

                dialogStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open the guide dialog box.");
            }
        } else {
            showAlert("No Selection", "Please select a guide to update.");
        }
    }

    /**
     * This method is called by the GuideDialogBoxController when a guide is updated.
     * @param updatedGuide The updated guide object.
     */
    public void updateGuide(Guide updatedGuide) {
        // Find and replace the guide in the list
        for (int i = 0; i < guideList.size(); i++) {
            if (guideList.get(i).getId().equals(updatedGuide.getId())) {
                guideList.set(i, updatedGuide);
                break;
            }
        }
        saveGuideData(); // Save the updated list to the file
    }

    @FXML
    private void handleDeleteGuide(ActionEvent event) {
        Guide selectedGuide = guideTable.getSelectionModel().getSelectedItem();
        if (selectedGuide != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete guide: " + selectedGuide.getName() + "?");

            if (alert.showAndWait().orElse(null) == ButtonType.OK) {
                guideList.remove(selectedGuide);
                System.out.println("Deleted Guide with ID: " + selectedGuide.getId());
                saveGuideData();
            }
        } else {
            showAlert("No Selection", "Please select a guide to delete.");
        }
    }

    /**
     * Saves the guide data back to the JSON file.
     */
    private void saveGuideData() {
        try (FileWriter writer = new FileWriter(GUIDE_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
            gson.toJson(guideList, writer);
            System.out.println("Guide data saved to " + GUIDE_FILE_PATH);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save guide data to the file.");
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