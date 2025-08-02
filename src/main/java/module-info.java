module ap.assignment.indira {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;

    exports ap.assignment.indira;
    exports Admin;

    opens Staff to javafx.fxml;
    opens Admin to javafx.fxml;
    opens Model to javafx.base, com.google.gson;
    opens ap.assignment.indira to javafx.fxml;
}