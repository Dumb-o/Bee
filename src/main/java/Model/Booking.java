package Model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Booking {

    private IntegerProperty id;
    private StringProperty tourist;
    private StringProperty packageName;
    private StringProperty guide;
    private ObjectProperty<LocalDate> travelDate;
    private DoubleProperty amount;

    // Constructor
    public Booking(int id, String tourist, String packageName, String guide, LocalDate travelDate, double amount) {
        this.id = new SimpleIntegerProperty(id);
        this.tourist = new SimpleStringProperty(tourist);
        this.packageName = new SimpleStringProperty(packageName);
        this.guide = new SimpleStringProperty(guide);
        this.travelDate = new SimpleObjectProperty<>(travelDate);
        this.amount = new SimpleDoubleProperty(amount);
    }

    // Getters/setters for properties

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTourist() {
        return tourist.get();
    }

    public StringProperty touristProperty() {
        return tourist;
    }

    public void setTourist(String tourist) {
        this.tourist.set(tourist);
    }

    public String getPackage() {
        return packageName.get();
    }

    public StringProperty packageProperty() {
        return packageName;
    }

    public void setPackage(String packageName) {
        this.packageName.set(packageName);
    }

    public String getGuide() {
        return guide.get();
    }

    public StringProperty guideProperty() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide.set(guide);
    }

    public LocalDate getTravelDate() {
        return travelDate.get();
    }

    public ObjectProperty<LocalDate> travelDateProperty() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate.set(travelDate);
    }

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }
}
