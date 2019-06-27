package controllers.googlemapsclasses;

import constants.Constant;
import model.CurrentPoint;
import org.json.JSONException;

import java.io.IOException;

public class TestGoogleMapsClasses {

    public static void main(String[] args) throws IOException, JSONException {

        GeocodingAdressGoogleMapsAPI geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI(Constant.KEY_GOOGLE_API);
       // geocodingAdressGoogleMapsAPI.calculateLongitudeAndLatitude("Россия, Димитровград, улица Ленина, 10");


        CurrentPoint currentPoint = geocodingAdressGoogleMapsAPI.getCurrentPointViaAdress("Россия, Димитровград, улица Ленина, 10");

        System.out.println(String.format("%f,%f",
                geocodingAdressGoogleMapsAPI.getlatitude(),
                geocodingAdressGoogleMapsAPI.getlongitude()));// итоговая широта и долгота


    }
}
