package model;

import java.sql.Timestamp;

public class CurrentPoint {
    public double pointDolgota;
    public double pointShirota;
    public Timestamp currentDate;
    public CurrentPoint(double pointDolgota, double pointShirota, Timestamp currentDate) {
        this.pointDolgota = pointDolgota;
        this.pointShirota = pointShirota;
        this.currentDate = currentDate;
    }
}

