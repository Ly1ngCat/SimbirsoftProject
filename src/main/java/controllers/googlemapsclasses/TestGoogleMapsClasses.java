package controllers.googlemapsclasses;

import model.PlaceModel;
import org.json.JSONException;
import org.junit.Assert;

import java.io.IOException;
import java.util.ArrayList;

public class TestGoogleMapsClasses {

    public static void main(String[] args) throws IOException, JSONException {

        GeocodingAdressGoogleMapsAPI geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();
        geocodingAdressGoogleMapsAPI.calculateLongitudeAndLatitude("Россия, Димитровград, улица Ленина, 10");


        /*CurrentPoint currentPoint = geocodingAdressGoogleMapsAPI.getCurrentPointViaAdress("Россия, Димитровград, улица Ленина, 10");

        CurrentPoint currentPoint2 = geocodingAdressGoogleMapsAPI.getCurrentPointViaCoordinates("55.735893,37.527420");*/

        System.out.println(String.format("%f,%f",
                geocodingAdressGoogleMapsAPI.getlatitude(),
                geocodingAdressGoogleMapsAPI.getlongitude()));// итоговая широта и долгота*/


        /*DirectionGoogleMapsAPI directionSample = new DirectionGoogleMapsAPI();
        directionSample.calculateRoute("55.735893,37.527420",
                "Россия, Димитровград, улица Театральная, улица Ленина 10", DirectionGoogleMapsAPI.travelModes.bicycling);*/



    }
}
