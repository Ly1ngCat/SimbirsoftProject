package model;

import java.util.ArrayList;

public class PlaceModel {
    public double latitude = 0;
    public double longitude = 0;
    public String name = "";
    public String vicinity = "";
    public double rating;

    public PlaceModel(double latitude, double longitude, String name, String vicinity, double rating){
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.vicinity = vicinity;
        this.rating = rating;
    }
}
