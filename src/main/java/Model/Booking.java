package Model;

import com.google.gson.annotations.Expose;
import javafx.beans.property.*;
import java.time.LocalDate;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Booking {

    @Expose private int id;
    @Expose private String tourist;
    @Expose private String packageName;
    @Expose private String guide;
    @Expose private String travelDate;
    @Expose private double amount;

    private final IntegerProperty idProperty = new SimpleIntegerProperty();
    private final StringProperty touristProperty = new SimpleStringProperty();
    private final StringProperty packageNameProperty = new SimpleStringProperty();
    private final StringProperty guideProperty = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> travelDateProperty = new SimpleObjectProperty<>();
    private final DoubleProperty amountProperty = new SimpleDoubleProperty();

    // Default constructor for Gson
    public Booking() {
        setupProperties();
    }

    // Main constructor
    public Booking(int id, String tourist, String packageName, String guide, LocalDate travelDate, double amount) {
        this.id = id;
        this.tourist = tourist;
        this.packageName = packageName;
        this.guide = guide;
        this.travelDate = travelDate.toString();
        this.amount = amount;
        setupProperties();
    }

    private void setupProperties() {
        this.idProperty.set(this.id);
        this.touristProperty.set(this.tourist);
        this.packageNameProperty.set(this.packageName);
        this.guideProperty.set(this.guide);

        LocalDate parsedDate = null;
        if (this.travelDate != null && !this.travelDate.isEmpty()) {
            try {
                parsedDate = LocalDate.parse(this.travelDate);
            } catch (Exception e) {
            }
        }
        this.travelDateProperty.set(parsedDate);
        this.amountProperty.set(this.amount);
    }

    public static ObservableList<Booking> loadBookings() {
        try (InputStreamReader reader = new InputStreamReader(Booking.class.getResourceAsStream("/Data/bookings.json"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type listType = new TypeToken<List<Booking>>() {}.getType();
            List<Booking> list = gson.fromJson(reader, listType);

            if (list != null) {
                list.forEach(Booking::setupProperties);
                return FXCollections.observableArrayList(list);
            }
            return FXCollections.observableArrayList();
        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    // Getters for properties
    public IntegerProperty idProperty() { return idProperty; }
    public StringProperty touristProperty() { return touristProperty; }
    public StringProperty packageNameProperty() { return packageNameProperty; }
    public StringProperty guideProperty() { return guideProperty; }
    public ObjectProperty<LocalDate> travelDateProperty() { return travelDateProperty; }
    public DoubleProperty amountProperty() { return amountProperty; }

    // Getters for TableView, which will use the properties
    public int getId() { return idProperty.get(); }
    public String getTourist() { return touristProperty.get(); }
    public String getPackageName() { return packageNameProperty.get(); }
    public String getGuide() { return guideProperty.get(); }
    public LocalDate getTravelDate() { return travelDateProperty.get(); }
    public double getAmount() { return amountProperty.get(); }
}
