package Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Attraction {
    private String id;
    private String name;
    private String location;
    private double altitude;

    // Constructor
    public Attraction(String id, String name, String location, double altitude) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.altitude = altitude;
    }

    private static final Path FILE_PATH = Path.of("src/main/resources/Data/attractions.json");
    private static final Gson gson = new Gson();

    public static ObservableList<Attraction> loadAttractions() {
        if (!Files.exists(FILE_PATH)) {
            return FXCollections.observableArrayList();
        }

        try (Reader reader = new FileReader(FILE_PATH.toFile())) {
            Type listType = new TypeToken<List<Attraction>>() {}.getType();
            List<Attraction> list = gson.fromJson(reader, listType);
            return FXCollections.observableArrayList(list != null ? list : new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    public static void saveAttractions(List<Attraction> attractions) {
        try (Writer writer = new FileWriter(FILE_PATH.toFile())) {
            gson.toJson(attractions, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public double getAltitude() {
        return altitude;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", altitude=" + altitude +
                '}';
    }
}
