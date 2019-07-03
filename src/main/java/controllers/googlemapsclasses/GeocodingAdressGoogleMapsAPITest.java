package controllers.googlemapsclasses;

import model.CurrentPoint;
import model.PlaceModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GeocodingAdressGoogleMapsAPITest extends AbstractSampleGoogleMapsAPI {

    @Test
    public void calculateLongitudeAndLatitude() {

        //Хороший ввод
        GeocodingAdressGoogleMapsAPI geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        geocodingAdressGoogleMapsAPI.calculateLongitudeAndLatitude("Россия, Димитровград, улица Ленина, 10");


        Assert.assertEquals("54.2266303", String.valueOf(geocodingAdressGoogleMapsAPI.getlatitude()));
        Assert.assertEquals("49.5472962", String.valueOf(geocodingAdressGoogleMapsAPI.getlongitude()));

        //Кривой ввод
        geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        geocodingAdressGoogleMapsAPI.calculateLongitudeAndLatitude("aarhahah");

        Assert.assertEquals(String.valueOf(0.0), String.valueOf(geocodingAdressGoogleMapsAPI.getlongitude()));

        //Пустой ввод
        geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        geocodingAdressGoogleMapsAPI.calculateLongitudeAndLatitude(null);

        Assert.assertEquals(String.valueOf(0.0), String.valueOf(geocodingAdressGoogleMapsAPI.getlongitude()));

    }

    @Test
    public void calculateAdress() {
        //Хороший ввод
        GeocodingAdressGoogleMapsAPI geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        geocodingAdressGoogleMapsAPI.calculateAdress("54.2266303" + "," + "49.5472962");

        Assert.assertEquals("пр. Ленина, 10, Димитровград, Ульяновская обл., Россия, 433507", String.valueOf(geocodingAdressGoogleMapsAPI.getAdress()));

        //Кривой ввод
        geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        geocodingAdressGoogleMapsAPI.calculateAdress("54.2266303"  + "49.5472962");
        Assert.assertEquals("", String.valueOf(geocodingAdressGoogleMapsAPI.getAdress()));

        //Пустой ввод
        geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        geocodingAdressGoogleMapsAPI.calculateAdress(null);
        Assert.assertEquals("", String.valueOf(geocodingAdressGoogleMapsAPI.getAdress()));
    }

    @Test
    public void getCurrentPointViaAdress() {

        CurrentPoint currentPoint;
        CurrentPoint currentPointNull = new CurrentPoint();
        currentPointNull.longitude = 0.0;
        currentPointNull.latitude = 0.0;
        currentPointNull.adressString = "";

        //Хороший ввод
        GeocodingAdressGoogleMapsAPI geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        currentPoint = geocodingAdressGoogleMapsAPI.getCurrentPointViaAdress("Россия, Димитровград, улица Ленина, 10");


        Assert.assertEquals("49.5472962", String.valueOf(currentPoint.longitude));
        Assert.assertEquals("54.2266303", String.valueOf(currentPoint.latitude));
        Assert.assertEquals("Россия, Димитровград, улица Ленина, 10", String.valueOf(currentPoint.adressString));

        //Кривой ввод
        geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        currentPoint = geocodingAdressGoogleMapsAPI.getCurrentPointViaAdress("aarhahah");

        Assert.assertEquals("0.0", String.valueOf(currentPoint.longitude));
        Assert.assertEquals("0.0", String.valueOf(currentPoint.latitude));
        Assert.assertEquals("", String.valueOf(currentPoint.adressString));

        //Пустой ввод
        geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        currentPoint = geocodingAdressGoogleMapsAPI.getCurrentPointViaAdress(null);

        Assert.assertEquals("0.0", String.valueOf(currentPoint.longitude));
        Assert.assertEquals("0.0", String.valueOf(currentPoint.latitude));
        Assert.assertEquals("", String.valueOf(currentPoint.adressString));

    }

    @Test
    public void getCurrentPointViaCoordinates() {

        CurrentPoint currentPoint = new CurrentPoint();

        //Хороший ввод
        GeocodingAdressGoogleMapsAPI geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        currentPoint = geocodingAdressGoogleMapsAPI.getCurrentPointViaCoordinates(54.2266303  , 49.5472962);


        Assert.assertEquals("49.5472962", String.valueOf(currentPoint.longitude));
        Assert.assertEquals("54.2266303", String.valueOf(currentPoint.latitude));
        Assert.assertEquals("пр. Ленина, 10, Димитровград, Ульяновская обл., Россия, 433507", String.valueOf(currentPoint.adressString));

        //Кривой ввод
        geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        currentPoint = geocodingAdressGoogleMapsAPI.getCurrentPointViaCoordinates(54.2266303 ,45464646464.0);

        Assert.assertEquals("0.0", String.valueOf(currentPoint.longitude));
        Assert.assertEquals("0.0", String.valueOf(currentPoint.latitude));
        Assert.assertEquals("", String.valueOf(currentPoint.adressString));

        //Пустой ввод
        geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        currentPoint = geocodingAdressGoogleMapsAPI.getCurrentPointViaCoordinates(0, 0);

        Assert.assertEquals("0.0", String.valueOf(currentPoint.longitude));
        Assert.assertEquals("0.0", String.valueOf(currentPoint.latitude));
        Assert.assertEquals("", String.valueOf(currentPoint.adressString));
    }
}