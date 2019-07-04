package model;

import java.time.LocalDateTime;

public class CurrentPoint implements Comparable<CurrentPoint>{
    private int id;
    private double longitude = 0;
    private double latitude = 0;
    private LocalDateTime forecastDate;
    private String address = "";
    private Weather weather=null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString()
    {
        return "CurrentPoint{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", forecastDate=" + forecastDate +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public int compareTo(CurrentPoint pointToCompare)
    {
        return this.forecastDate.compareTo(pointToCompare.getForecastDate());
    }

    public CurrentPoint(double longitude, double latitude, String addressString, LocalDateTime forecastDate) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = addressString;
        this.forecastDate = forecastDate;
    }

    public CurrentPoint(){}

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setForecastDate(LocalDateTime forecastDate) {
        this.forecastDate = forecastDate;
    }
    public LocalDateTime getForecastDate() {
        return forecastDate;
    }

    public void setAddress(String adressString) {
        this.address = adressString;
    }
    public String getAddress() {
        return address;
    }

    public void initializeWeather()
    {
        this.weather=new Weather(this);
    }
    public Weather getWeather(){ return weather;}

}

