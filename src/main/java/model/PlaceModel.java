package model;

import java.util.ArrayList;

public class PlaceModel {
    double latitude = 0;
    double longitude = 0;
    String name = "";
    String vicinity = "";
    double rating;

    public PlaceModel(double latitude, double longitude, String name, String vicinity, double rating){
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.vicinity = vicinity;
        this.rating = rating;
    }
}
