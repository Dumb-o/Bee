package Model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Staff {

    private StringProperty email;
    private StringProperty name;
    private StringProperty address;
    private StringProperty nationality;
    private StringProperty phone;
    private String password;
    private LocalDate dob; // Date of Birth is represented as LocalDate

    // Constructor
    public Staff(String email, String name, String address, String nationality, String phone, String password, String dob) {
        this.email = new SimpleStringProperty(email);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.nationality = new SimpleStringProperty(nationality);
        this.phone = new SimpleStringProperty(phone);
        this.password = password;
        this.dob = dob.isEmpty() ? null : LocalDate.parse(dob);
    }

    // Getters and setters for StringProperty
    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    // Other getters and setters for address, nationality, etc.

    @Override
    public String toString() {
        return "Staff [email=" + email.get() + ", name=" + name.get() + ", address=" + address.get() + ", nationality=" + nationality.get()
                + ", phone=" + phone.get() + ", password=" + password + ", dob=" + dob + "]";
    }

    public Object getPassword() {
        return this.password;
    }
}
