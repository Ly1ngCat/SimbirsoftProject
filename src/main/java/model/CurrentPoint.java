package model;

import java.time.LocalDateTime;

public class CurrentPoint {
    public double longitude = 0;
    public double latitude = 0;
    private LocalDateTime forecastDate;
    public String adressString = "";

    public CurrentPoint(double longitude, double latitude,  String addressString, LocalDateTime forecastDate) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.adressString = addressString;
        this.forecastDate = forecastDate;
    }

    public CurrentPoint(){}

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LocalDateTime getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(LocalDateTime forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getAdressString() {
        return adressString;
    }

    public void setAdressString(String adressString) {
        this.adressString = adressString;
    }
}

