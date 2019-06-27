package model;

import java.sql.Timestamp;

public class CurrentPoint {
    public double pointDolgota;
    public double pointShirota;
    public Timestamp currentDate;
    public String adressString;

    public CurrentPoint(double pointDolgota, double pointShirota, Timestamp currentDate, String adressString) {
        this.pointDolgota = pointDolgota;
        this.pointShirota = pointShirota;
        this.currentDate = currentDate;
        this.adressString = adressString;
    }
}

