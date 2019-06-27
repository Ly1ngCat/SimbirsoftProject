package model;

import java.sql.Timestamp;

public class CurrentPoint {
    public double longitude;
    public double latitude;
    public Timestamp currentDate;
    public String adressString;

    public CurrentPoint(double longitude, double latitude,  String adressString, Timestamp currentDate) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.adressString = adressString;
        this.currentDate = currentDate;
    }
}

