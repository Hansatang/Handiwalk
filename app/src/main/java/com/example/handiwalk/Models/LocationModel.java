package com.example.handiwalk.Models;



import com.google.firebase.firestore.GeoPoint;

public class LocationModel {
    private String name;
    private GeoPoint coordinates;
    private String description;
    private long id;

    public LocationModel(String name, GeoPoint coordinates, String description, long id) {
        this.name = name;
        this.coordinates = coordinates;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoPoint coordinates) {
        this.coordinates = coordinates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId(){
        return (int) id;
    }
}
