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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private ObservableList<Guide> guideList = FXCollections.observableArrayList();

    private static final String DATA_DIR = "Data";
    private static final String GUIDE_FILE_NAME = "guides.json";
    private static final String GUIDE_FILE_PATH = DATA_DIR + File.separator + GUIDE_FILE_NAME;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        contactColumn.setCellValueFactory(cellData -> cellData.getValue().contactProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        nationalityColumn.setCellValueFactory(cellData -> cellData.getValue().nationalityProperty());

        loadGuideData();
    }

    private void loadGuideData() {
        try (InputStream is = getClass().getResourceAsStream("/Data/guides.json");
             InputStreamReader reader = new InputStreamReader(is)) {

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            Type guideListType = new TypeToken<List<Guide>>() {}.getType();
            List<Guide> guideData = gson.fromJson(reader, guideListType);

            guideList.clear();

            if (guideData != null) {
                guideList.addAll(guideData);
            }

            guideTable.setItems(guideList);

        } catch (Exception e) {
            System.err.println("Failed to load guide data from the file.");
        }
    }

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
            dialogController.setGuideController(this);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the guide dialog box.");
        }
    }

    public void addNewGuide(Guide newGuide) {
        guideList.add(newGuide);
        saveGuideData();
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
                dialogController.setGuideForUpdate(selectedGuide);

                dialogStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open the guide dialog box.");
            }
        } else {
            showAlert("No Selection", "Please select a guide to update.");
        }
    }

    public void updateGuide(Guide updatedGuide) {
        for (int i = 0; i < guideList.size(); i++) {
            if (guideList.get(i).getId().equals(updatedGuide.getId())) {
                guideList.set(i, updatedGuide);
                break;
            }
        }
        saveGuideData();
    }

    @FXML
    private void handleDeleteGuide(ActionEvent event) {
        Guide selectedGuide = guideTable.getSelectionModel().getSelectedItem();
        if (selectedGuide != null) {
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

    private void saveGuideData() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        File guidesFile = new File(DATA_DIR, GUIDE_FILE_NAME);

        try (FileWriter writer = new FileWriter(guidesFile)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            gson.toJson(guideList, writer);
            System.out.println("Guide data saved to " + guidesFile.getAbsolutePath());

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
