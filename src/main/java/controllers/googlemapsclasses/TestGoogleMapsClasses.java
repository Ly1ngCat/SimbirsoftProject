package controllers.googlemapsclasses;

import model.PlaceModel;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class TestGoogleMapsClasses {

    public static void main(String[] args) throws IOException, JSONException {

       /* GeocodingAdressGoogleMapsAPI geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI(Constant.KEY_GOOGLE_API);
       // geocodingAdressGoogleMapsAPI.calculateLongitudeAndLatitude("Россия, Димитровград, улица Ленина, 10");


        CurrentPoint currentPoint = geocodingAdressGoogleMapsAPI.getCurrentPointViaAdress("Россия, Димитровград, улица Ленина, 10");

        CurrentPoint currentPoint2 = geocodingAdressGoogleMapsAPI.getCurrentPointViaCoordinates("55.735893,37.527420");

        System.out.println(String.format("%f,%f",
                geocodingAdressGoogleMapsAPI.getlatitude(),
                geocodingAdressGoogleMapsAPI.getlongitude()));// итоговая широта и долгота*/


       PlaceSearchGoogleMapsAPI placeSearchGoogleMapsAPI = new PlaceSearchGoogleMapsAPI();
        ArrayList<PlaceModel> ggg = placeSearchGoogleMapsAPI.generateListPlaceModel("-33.8599358","151.2090295",
                PlaceSearchGoogleMapsAPI.typePlace.lodging,"2500");
    }
}
