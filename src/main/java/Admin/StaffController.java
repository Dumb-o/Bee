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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    // ObservableList to hold the staff data
    private ObservableList<Staff> staffList = FXCollections.observableArrayList();

    // File path to load staff data from
    private static final String STAFF_FILE_PATH = "src/main/resources/Data/staff.json";

    // Initialize the StaffTableView with the data
    @FXML
    public void initialize() {
        // Initialize the columns with proper property bindings
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        contactColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        // Load and display staff data
        loadStaffData();
    }

    /**
     * Loads staff data from the JSON file.
     * It uses a GsonBuilder to register custom adapters for LocalDate and StringProperty,
     * which are required to correctly deserialize the data.
     */
    private void loadStaffData() {
        try (FileReader reader = new FileReader(STAFF_FILE_PATH)) {
            // Use GsonBuilder to register ALL custom adapters
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(StringProperty.class, new StringPropertyAdapter())
                    .create();

            Type staffListType = new TypeToken<List<Staff>>(){}.getType();
            List<Staff> staffData = gson.fromJson(reader, staffListType);

            // Clear the existing list
            staffList.clear();

            // Filter out staff with email containing "@admin" and add to the ObservableList
            if (staffData != null) {
                for (Staff staff : staffData) {
                    if (staff.getEmail() != null && !staff.getEmail().contains("@admin")) {
                        staffList.add(staff);
                    }
                }
            }

            // Set the table items to the filtered staff list
            staffTable.setItems(staffList);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load staff data from the file.");
        }
    }

    /**
     * Handles adding a new staff member by opening a dialog box.
     */
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
            dialogController.setStaffController(this); // Pass a reference to this controller

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the staff dialog box.");
        }
    }

    /**
     * This method is called by the StaffDialogBoxController when a new staff member is created.
     * @param newStaff The new staff object to add to the table.
     */
    public void addNewStaff(Staff newStaff) {
        staffList.add(newStaff);
        saveStaffData(); // Save the updated list to the file
    }

    @FXML
    private void handleUpdateStaff(ActionEvent event) {
        Staff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
        if (selectedStaff != null) {
            System.out.println("Update Staff for: " + selectedStaff.getName());
            // Implement update logic here
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
            // After deleting, save the updated staff data back to the file
            saveStaffData();
        } else {
            showAlert("No Selection", "Please select a staff member to delete.");
        }
    }

    /**
     * Saves the staff data back to the JSON file.
     * This method first reads the admin user(s) from the file and then combines them
     * with the current state of the `staffList` (which reflects UI changes) before saving.
     */
    private void saveStaffData() {
        try (FileWriter writer = new FileWriter(STAFF_FILE_PATH)) {
            // Create a Gson instance with custom adapters
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(StringProperty.class, new StringPropertyAdapter())
                    .setPrettyPrinting()
                    .create();

            // 1. Create a temporary list to hold the complete data for saving
            ObservableList<Staff> staffToSave = FXCollections.observableArrayList();

            // 2. Read the admin user(s) from the existing file
            try (FileReader reader = new FileReader(STAFF_FILE_PATH)) {
                Type staffListType = new TypeToken<List<Staff>>(){}.getType();
                List<Staff> allStaffFromFile = gson.fromJson(reader, staffListType);

                if (allStaffFromFile != null) {
                    // Add only the admin user(s) to the saving list
                    for (Staff staff : allStaffFromFile) {
                        if (staff.getEmail() != null && staff.getEmail().contains("@admin")) {
                            staffToSave.add(staff);
                        }
                    }
                }
            } catch (IOException e) {
                // If the file doesn't exist, we don't have an admin to save.
                // An alert is already shown by loadStaffData() in this case.
                System.err.println("No existing file to read admin user from.");
            }

            // 3. Add all the non-admin staff from the current `staffList`
            // This list already has the deleted staff member removed.
            staffToSave.addAll(staffList);

            // 4. Write the complete, merged list to the file
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