package Model;

import com.google.gson.annotations.Expose;
import javafx.beans.property.*;

import java.time.LocalDate;

public class Booking {

    // Private fields for JSON serialization
    @Expose
    private int id;
    @Expose
    private String tourist;
    @Expose
    private String packageName;
    @Expose
    private String guide;
    @Expose
    private String travelDate;
    @Expose
    private double amount;

    // JavaFX properties for UI binding
    private IntegerProperty idProperty;
    private StringProperty touristProperty;
    private StringProperty packageNameProperty;
    private StringProperty guideProperty;
    private ObjectProperty<LocalDate> travelDateProperty;
    private DoubleProperty amountProperty;

    // Default constructor for Gson
    public Booking() {
        this.id = 0;
        this.tourist = "";
        this.packageName = "";
        this.guide = "";
        this.travelDate = "";
        this.amount = 0.0;
        initializeProperties();
    }

    // Constructor
    public Booking(int id, String tourist, String packageName, String guide, LocalDate travelDate, double amount) {
        this.id = id;
        this.tourist = tourist;
        this.packageName = packageName;
        this.guide = guide;
        this.travelDate = travelDate.toString();
        this.amount = amount;
        initializeProperties();
    }

    private void initializeProperties() {
        this.idProperty = new SimpleIntegerProperty(id);
        this.touristProperty = new SimpleStringProperty(tourist);
        this.packageNameProperty = new SimpleStringProperty(packageName);
        this.guideProperty = new SimpleStringProperty(guide);
        
        // Handle empty travel date
        LocalDate parsedDate = null;
        if (travelDate != null && !travelDate.isEmpty()) {
            try {
                parsedDate = LocalDate.parse(travelDate);
            } catch (Exception e) {
                parsedDate = LocalDate.now(); // Default to today if parsing fails
            }
        } else {
            parsedDate = LocalDate.now(); // Default to today if empty
        }
        this.travelDateProperty = new SimpleObjectProperty<>(parsedDate);
        
        this.amountProperty = new SimpleDoubleProperty(amount);
    }

    // Getters and setters for String fields
    public int getId() {
        return id;
    }

    public IntegerProperty idProperty() {
        return idProperty;
    }

    public void setId(int id) {
        this.id = id;
        if (idProperty != null) {
            idProperty.set(id);
        }
    }

    public String getTourist() {
        return tourist;
    }

    public StringProperty touristProperty() {
        return touristProperty;
    }

    public void setTourist(String tourist) {
        this.tourist = tourist;
        if (touristProperty != null) {
            touristProperty.set(tourist);
        }
    }

    public String getPackage() {
        return packageName;
    }

    public StringProperty packageProperty() {
        return packageNameProperty;
    }

    public void setPackage(String packageName) {
        this.packageName = packageName;
        if (packageNameProperty != null) {
            packageNameProperty.set(packageName);
        }
    }

    public String getGuide() {
        return guide;
    }

    public StringProperty guideProperty() {
        return guideProperty;
    }

    public void setGuide(String guide) {
        this.guide = guide;
        if (guideProperty != null) {
            guideProperty.set(guide);
        }
    }

    public LocalDate getTravelDate() {
        return LocalDate.parse(travelDate);
    }

    public ObjectProperty<LocalDate> travelDateProperty() {
        return travelDateProperty;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate.toString();
        if (travelDateProperty != null) {
            travelDateProperty.set(travelDate);
        }
    }

    public double getAmount() {
        return amount;
    }

    public DoubleProperty amountProperty() {
        return amountProperty;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        if (amountProperty != null) {
            amountProperty.set(amount);
        }
    }
}
