package Model;

import com.google.gson.annotations.Expose;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Guide {

    // Private fields for JSON serialization
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String contact;
    @Expose
    private String email;
    @Expose
    private String nationality;

    // JavaFX properties for UI binding (excluded from JSON serialization)
    @Expose(serialize = false, deserialize = false)
    private StringProperty idProperty;
    @Expose(serialize = false, deserialize = false)
    private StringProperty nameProperty;
    @Expose(serialize = false, deserialize = false)
    private StringProperty contactProperty;
    @Expose(serialize = false, deserialize = false)
    private StringProperty emailProperty;
    @Expose(serialize = false, deserialize = false)
    private StringProperty nationalityProperty;

    // Default constructor for Gson
    public Guide() {
        this.id = "";
        this.name = "";
        this.contact = "";
        this.email = "";
        this.nationality = "";
        initializeProperties();
    }

    // Constructor
    public Guide(String id, String name, String contact, String email, String nationality) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.nationality = nationality;
        initializeProperties();
    }

    private void initializeProperties() {
        this.idProperty = new SimpleStringProperty(id);
        this.nameProperty = new SimpleStringProperty(name);
        this.contactProperty = new SimpleStringProperty(contact);
        this.emailProperty = new SimpleStringProperty(email);
        this.nationalityProperty = new SimpleStringProperty(nationality);
    }

    // Getters and setters for JSON serialization
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
        if (contactProperty != null) {
            contactProperty.set(contact);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        if (emailProperty != null) {
            emailProperty.set(email);
        }
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
        if (nationalityProperty != null) {
            nationalityProperty.set(nationality);
        }
    }

    // JavaFX property getters for UI binding
    public StringProperty idProperty() {
        return idProperty;
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public StringProperty contactProperty() {
        return contactProperty;
    }

    public StringProperty emailProperty() {
        return emailProperty;
    }

    public StringProperty nationalityProperty() {
        return nationalityProperty;
    }

    @Override
    public String toString() {
        return "Guide [id=" + id + ", name=" + name + ", contact=" + contact + 
               ", email=" + email + ", nationality=" + nationality + "]";
    }
} 