package Admin;

import Model.Staff;
import ap.assignment.indira.LocalDateAdapter;
import ap.assignment.indira.StringPropertyAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

public class StaffController {

    @FXML
    private TableView<Staff> staffTable;

    @FXML
    private TableColumn<Staff, String> nameColumn;

    @FXML
    private TableColumn<Staff, String> contactColumn;

    @FXML
    private TableColumn<Staff, String> emailColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    private ObservableList<Staff> staffList = FXCollections.observableArrayList();

    private static final String DATA_DIR = "Data";
    private static final String STAFF_FILE_NAME = "staff.json";
    private static final String STAFF_FILE_PATH = DATA_DIR + File.separator + STAFF_FILE_NAME;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        contactColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        loadStaffData();
    }

    private void loadStaffData() {
        try (InputStream is = getClass().getResourceAsStream("/Data/staff.json");
             InputStreamReader reader = new InputStreamReader(is)) {

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(StringProperty.class, new StringPropertyAdapter())
                    .create();

            Type staffListType = new TypeToken<List<Staff>>(){}.getType();
            List<Staff> staffData = gson.fromJson(reader, staffListType);

            staffList.clear();

            if (staffData != null) {
                for (Staff staff : staffData) {
                    if (staff.getEmail() != null && !staff.getEmail().contains("@admin")) {
                        staffList.add(staff);
                    }
                }
            }
            staffTable.setItems(staffList);

        } catch (Exception e) {
            System.err.println("Failed to load staff data from the file.");
        }
    }

    @FXML
    private void handleAddStaff(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ap/assignment/View/Admin/StaffDialogBox.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Staff");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(addButton.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            StaffDialogBoxController dialogController = loader.getController();
            dialogController.setStaffController(this);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the staff dialog box.");
        }
    }

    public void addNewStaff(Staff newStaff) {
        staffList.add(newStaff);
        saveStaffData();
    }

    @FXML
    private void handleUpdateStaff(ActionEvent event) {
        Staff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
        if (selectedStaff != null) {
            System.out.println("Update Staff for: " + selectedStaff.getName());
        } else {
            showAlert("No Selection", "Please select a staff member to update.");
        }
    }

    @FXML
    private void handleDeleteStaff(ActionEvent event) {
        Staff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
        if (selectedStaff != null) {
            staffList.remove(selectedStaff);
            System.out.println("Deleted Staff with Email: " + selectedStaff.getEmail());
            saveStaffData();
        } else {
            showAlert("No Selection", "Please select a staff member to delete.");
        }
    }

    private void saveStaffData() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        try (FileWriter writer = new FileWriter(STAFF_FILE_PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(StringProperty.class, new StringPropertyAdapter())
                    .setPrettyPrinting()
                    .create();

            ObservableList<Staff> staffToSave = FXCollections.observableArrayList();

            try (InputStream is = getClass().getResourceAsStream("/Data/staff.json");
                 InputStreamReader reader = new InputStreamReader(is)) {
                Type staffListType = new TypeToken<List<Staff>>(){}.getType();
                List<Staff> allStaffFromFile = gson.fromJson(reader, staffListType);

                if (allStaffFromFile != null) {
                    for (Staff staff : allStaffFromFile) {
                        if (staff.getEmail() != null && staff.getEmail().contains("@admin")) {
                            staffToSave.add(staff);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("No existing file to read admin user from.");
            }

            staffToSave.addAll(staffList);

            gson.toJson(staffToSave, writer);
            System.out.println("Staff data saved to " + STAFF_FILE_PATH);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save staff data to the file.");
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
