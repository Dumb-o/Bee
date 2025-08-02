package Admin;

import Model.Festival;
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

public class FestivalController {

    @FXML
    private TableView<Festival> festivalTable;

    @FXML
    private TableColumn<Festival, String> idColumn;

    @FXML
    private TableColumn<Festival, String> nameColumn;

    @FXML
    private TableColumn<Festival, String> dateFromColumn;

    @FXML
    private TableColumn<Festival, String> dateToColumn;

    @FXML
    private TableColumn<Festival, Number> discountColumn;

    private ObservableList<Festival> festivalList = FXCollections.observableArrayList();
    private static final String FESTIVAL_FILE_PATH = "src/main/resources/Data/festivals.json";

    // Initialize the FestivalTableView with the data
    @FXML
    public void initialize() {
        // Initialize the columns with proper property bindings
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        dateFromColumn.setCellValueFactory(cellData -> cellData.getValue().dateFromProperty());
        dateToColumn.setCellValueFactory(cellData -> cellData.getValue().dateToProperty());
        discountColumn.setCellValueFactory(cellData -> cellData.getValue().discountProperty());

        // Load and display festival data
        loadFestivalData();
    }

    @FXML
    private void addFestival(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Admin/FestivalDialogBox.fxml"));
            Parent root = loader.load();

            FestivalDialogBoxController dialogController = loader.getController();
            dialogController.setFestivalController(this);
            dialogController.setAddMode();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Festival");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Add Festival dialog.");
        }
    }

    @FXML
    private void updateFestival(ActionEvent event) {
        Festival selectedFestival = festivalTable.getSelectionModel().getSelectedItem();
        if (selectedFestival == null) {
            showAlert("Warning", "Please select a festival to update.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Admin/FestivalDialogBox.fxml"));
            Parent root = loader.load();

            FestivalDialogBoxController dialogController = loader.getController();
            dialogController.setFestivalController(this);
            dialogController.setUpdateMode(selectedFestival);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Festival");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Update Festival dialog.");
        }
    }

    @FXML
    private void deleteFestival(ActionEvent event) {
        Festival selectedFestival = festivalTable.getSelectionModel().getSelectedItem();
        if (selectedFestival == null) {
            showAlert("Warning", "Please select a festival to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Festival");
        alert.setContentText("Are you sure you want to delete the festival: " + selectedFestival.getName() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                festivalList.remove(selectedFestival);
                saveFestivalData();
                System.out.println("Deleted Festival with ID: " + selectedFestival.getId());
            }
        });
    }

    public void addFestivalToList(Festival festival) {
        festivalList.add(festival);
        saveFestivalData();
        System.out.println("Added Festival: " + festival.getName());
    }

    public void updateFestivalInList(Festival oldFestival, Festival newFestival) {
        int index = festivalList.indexOf(oldFestival);
        if (index != -1) {
            festivalList.set(index, newFestival);
            saveFestivalData();
            System.out.println("Updated Festival: " + newFestival.getName());
        }
    }

    private void loadFestivalData() {
        try (FileReader reader = new FileReader(FESTIVAL_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
            Type festivalListType = new TypeToken<List<Festival>>(){}.getType();
            List<Festival> festivalData = gson.fromJson(reader, festivalListType);

            // Clear the existing list
            festivalList.clear();

            // Add festivals to the ObservableList
            if (festivalData != null) {
                festivalList.addAll(festivalData);
            }

            // Set the table items to the festival list
            festivalTable.setItems(festivalList);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load festival data from the file.");
        }
    }

    private void saveFestivalData() {
        try (FileWriter writer = new FileWriter(FESTIVAL_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
            gson.toJson(festivalList, writer);
            System.out.println("Festival data saved to " + FESTIVAL_FILE_PATH);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save festival data to the file.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 