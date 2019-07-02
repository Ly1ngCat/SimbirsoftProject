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

    /*public PlaceModel(ArrayList<String> placeParams){ //TODO: Поправить привязку
        this.latitude = Double.parseDouble(placeParams.get(0));
        this.longitude = Double.parseDouble(placeParams.get(1));
        this.name = placeParams.get(2);
        this.vicinity = placeParams.get(3);
        this.rating = Double.parseDouble(placeParams.get(4));
    }*/
}
