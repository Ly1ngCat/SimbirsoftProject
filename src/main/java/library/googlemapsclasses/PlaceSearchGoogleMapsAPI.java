package library.googlemapsclasses;

import com.google.common.collect.Maps;
import library.ArrayListToPlaceModelParse;
import model.CurrentPoint;
import model.PlaceModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class PlaceSearchGoogleMapsAPI extends AbstractSampleGoogleMapsAPI implements RequestResponseLanguage {

    public enum typePlace {
        accounting, airport, amusement_park, aquarium, art_gallery, atm, bakery, bank, bar, beauty_salon, bicycle_store,
        book_store, bowling_alley, bus_station, cafe, campground, car_dealer, car_rental, car_repair, car_wash, casino, cemetery, church,
        city_hall, clothing_store, convenience_store, courthouse, dentist, department_store, doctor, electrician, electronics_store, embassy,
        fire_station, florist, funeral_home, furniture_store, gas_station, gym, hair_care, hardware_store, hindu_temple, home_goods_store,
        hospital, insurance_agency, jewelry_store, laundry, lawyer, library, liquor_store, local_government_office, locksmith, lodging,
        meal_delivery, meal_takeaway, mosque, movie_rental, movie_theater, moving_company, museum, night_club, painter, park, parking, pet_store,
        pharmacy, physiotherapist, plumber, police, post_office, real_estate_agency, restaurant, roofing_contractor, rv_park, school, shoe_store,
        shopping_mall, spa, stadium, storage, store, subway_station, supermarket, synagogue, taxi_stand, train_station, transit_station,
        travel_agency, veterinary_care, zoo
    }

    private String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

    public PlaceSearchGoogleMapsAPI() {
    }

    public List<ArrayList<String>> generateListPlace(String latitude, String longitude, typePlace typePlace, String radius) {
        String url = createJSONRequest(latitude, longitude, typePlace, radius);

        return findPlacesNearby(url);
    }

    public ArrayList<PlaceModel> generateListPlaceModel(String latitude, String longitude, typePlace typePlace, String radius){
        return ArrayListToPlaceModelParse.convert(generateListPlace( latitude,  longitude,  typePlace,  radius));
    }

    private String createJSONRequest(String latitude, String longitude, typePlace typePlace, String radius) {
        Map<String, String> params = Maps.newHashMap();
        params.put("radius", radius);
        params.put("type", typePlace.toString());
        params.put("language", answerLanguage);// язык данных, на котором мы хотим получить
        params.put("key", KEY_GOOGLE_API);
        params.put("location", latitude + "," + longitude);

        final String url = baseUrl + '?' + JsonReader.encodeParams(params);// генерируем путь с параметрами
        System.out.println(url);// Путь, что бы можно было посмотреть в браузере ответ службы
        return url;
    }

    private String createJSONRequest(CurrentPoint currentPoint, typePlace typePlace, String radius) {
        return createJSONRequest(String.valueOf(currentPoint.getLatitude()), String.valueOf(currentPoint.getLongitude()), typePlace, radius);
    }


    public List<ArrayList<String>> findPlacesNearby(String url) {

        List<ArrayList<String>> listPlace = new ArrayList<>();

        try {
            JSONObject response = JsonReader.read(url);
            JSONArray results = response.getJSONArray("results");
            int jsonLength=results.length();
            for (int indexPlace = 0; indexPlace < jsonLength; indexPlace++) {
                final JSONObject location = results.getJSONObject(indexPlace);
                listPlace.add(parsePlaceNearby(location));
            }


            return listPlace;

        } catch (Exception e) {

            e.printStackTrace();
            return Collections.emptyList() ;
        }
    }


    private ArrayList<String> parsePlaceNearby(JSONObject location) {
        try {
            ArrayList<String> listLine = new ArrayList<>();

            double latitude = Double.parseDouble(hasJSONAttribute(location.getJSONObject("geometry").getJSONObject("location"),
                    "lat"));
            double longitude = Double.parseDouble(hasJSONAttribute(location.getJSONObject("geometry").getJSONObject("location"),
                    "lng"));
            String name = hasJSONAttribute(location,"name");
            String vicinity =  hasJSONAttribute(location,"vicinity");
            double rating = Double.parseDouble(hasJSONAttribute(location, "rating"));

            listLine.add(String.valueOf(latitude));
            listLine.add(String.valueOf(longitude));
            listLine.add(name);
            listLine.add(vicinity);
            listLine.add(String.valueOf(rating));

            return listLine;

        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }

    private String hasJSONAttribute(JSONObject object, String attribute) throws JSONException {
        return object.has(attribute) ? object.get(attribute).toString() : "0";
    }


}


