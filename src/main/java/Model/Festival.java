package Model;

import com.google.gson.annotations.Expose;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Festival {

    // Private fields for JSON serialization
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String dateFrom;
    @Expose
    private String dateTo;
    @Expose
    private double discount;

    // JavaFX properties for UI binding
    private StringProperty idProperty;
    private StringProperty nameProperty;
    private StringProperty dateFromProperty;
    private StringProperty dateToProperty;
    private DoubleProperty discountProperty;

    // Default constructor for Gson
    public Festival() {
        this.id = "";
        this.name = "";
        this.dateFrom = "";
        this.dateTo = "";
        this.discount = 0.0;
        initializeProperties();
    }

    // Constructor
    public Festival(String id, String name, String dateFrom, String dateTo, double discount) {
        this.id = id;
        this.name = name;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.discount = discount;
        initializeProperties();
    }

    private void initializeProperties() {
        this.idProperty = new SimpleStringProperty(id);
        this.nameProperty = new SimpleStringProperty(name);
        this.dateFromProperty = new SimpleStringProperty(dateFrom);
        this.dateToProperty = new SimpleStringProperty(dateTo);
        this.discountProperty = new SimpleDoubleProperty(discount);
    }

    // Getters and setters for String fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        if (idProperty != null) {
            idProperty.set(id);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (nameProperty != null) {
            nameProperty.set(name);
        }
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
        if (dateFromProperty != null) {
            dateFromProperty.set(dateFrom);
        }
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
        if (dateToProperty != null) {
            dateToProperty.set(dateTo);
        }
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
        if (discountProperty != null) {
            discountProperty.set(discount);
        }
    }

    // JavaFX property getters
    public StringProperty idProperty() {
        return idProperty;
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public StringProperty dateFromProperty() {
        return dateFromProperty;
    }

    public StringProperty dateToProperty() {
        return dateToProperty;
    }

    public DoubleProperty discountProperty() {
        return discountProperty;
    }

    @Override
    public String toString() {
        return name;
    }
} 