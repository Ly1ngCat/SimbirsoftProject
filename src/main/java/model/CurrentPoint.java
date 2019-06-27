package model;

import java.sql.Timestamp;

public class CurrentPoint {
    public double longitude = 0;
    public double latitude = 0;
    public Timestamp currentDate = new Timestamp(0);
    public String adressString = "";

    public CurrentPoint(double longitude, double latitude,  String adressString, Timestamp currentDate) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.adressString = adressString;
        this.currentDate = currentDate;
    }
}

