package model;

import java.security.Timestamp;
import java.time.LocalDateTime;

public class CurrentPoint {
    public double longitude = 0;
    public double latitude = 0;
    public LocalDateTime forecastDate;
    public String adressString = "";

    public CurrentPoint(double longitude, double latitude,  String adressString, LocalDateTime forecastDate) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.adressString = adressString;
        this.forecastDate = forecastDate;
    }
}

