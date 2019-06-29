package model;

import java.sql.Timestamp;

public class CurrentPoint {
    public double longitude = 0;
    public double latitude = 0;
    public Timestamp currentDate; //поменять название на forecastDate, ибо мы желаемую дату, а не текущую, ищем
    public String adressString = "";

    public CurrentPoint(double longitude, double latitude,  String adressString, Timestamp currentDate) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.adressString = adressString;
        this.currentDate = currentDate;
    }
}

