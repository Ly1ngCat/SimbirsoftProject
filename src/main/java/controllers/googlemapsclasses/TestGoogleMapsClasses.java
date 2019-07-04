package controllers.googlemapsclasses;

import model.PlaceModel;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestGoogleMapsClasses {

    public static void main(String[] args) throws IOException, JSONException {

       /* GeocodingAdressGoogleMapsAPI geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI(Constant.KEY_GOOGLE_API);
       // geocodingAdressGoogleMapsAPI.calculateLongitudeAndLatitude("Россия, Димитровград, улица Ленина, 10");


        CurrentPoint currentPoint = geocodingAdressGoogleMapsAPI.getCurrentPointViaAdress("Россия, Димитровград, улица Ленина, 10");

        CurrentPoint currentPoint2 = geocodingAdressGoogleMapsAPI.getCurrentPointViaCoordinates("55.735893,37.527420");

        System.out.println(String.format("%f,%f",
                geocodingAdressGoogleMapsAPI.getlatitude(),
                geocodingAdressGoogleMapsAPI.getlongitude()));// итоговая широта и долгота*/


        /*DirectionGoogleMapsAPI directionSample = new DirectionGoogleMapsAPI();
        directionSample.calculateRoute("55.735893,37.527420",
                "Россия, Димитровград, улица Театральная, улица Ленина 10", DirectionGoogleMapsAPI.travelModes.bicycling);*/
        //gdfhdfhdhghdgh
        int g = 4;
        int t = 67;
        String hhh = "ddgdg";
        String hhhh = "dgdg";
       PlaceSearchGoogleMapsAPI placeSearchGoogleMapsAPI = new PlaceSearchGoogleMapsAPI();
       List<PlaceModel> ggg = placeSearchGoogleMapsAPI.generateListPlaceModel("-33.8599358","151.2090295",
                PlaceSearchGoogleMapsAPI.typePlace.lodging,"2500");
    }
}
