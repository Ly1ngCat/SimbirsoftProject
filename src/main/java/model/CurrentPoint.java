package model;

import java.time.LocalDateTime;

public class CurrentPoint {
    public double longitude = 0;
    public double latitude = 0;
    public LocalDateTime currentDate;
    public String adressString = "";

    public CurrentPoint(double longitude, double latitude,  String adressString, LocalDateTime currentDate) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.adressString = adressString;
        this.currentDate = currentDate;
    }
}

