

package controllers.googlemapsclasses;

import com.google.common.collect.Maps;
import model.CurrentPoint;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class GeocodingAdressGoogleMapsAPI {

    private Map<String, String> params;
    private String baseUrl = "https://maps.googleapis.com/maps/api/geocode/json";// путь к Geocoding API по HTTP
    private String KEY_GOOGLE_API = "";

    private double longitude = 0; //долгота
    private double latitude = 0;//широта
    private String adress = "";//адресс

    private enum selectionTypeAdress {address, latlng}

    public GeocodingAdressGoogleMapsAPI(String KEY_GOOGLE_API) throws JSONException, IOException {
        this.KEY_GOOGLE_API = KEY_GOOGLE_API;
    }

    public void calculateLongitudeAndLatitude (String adress) throws JSONException, IOException {

        String url = calculateJSONRequest(selectionTypeAdress.address.toString(), adress);

        JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
        // как правило наиболее подходящий ответ первый и данные о координатах можно получить по пути
        // //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
        JSONObject location = response.getJSONArray("results").getJSONObject(0);
        location = location.getJSONObject("geometry");
        location = location.getJSONObject("location");
        longitude = location.getDouble("lng");// долгота
        latitude = location.getDouble("lat");// широта
        //System.out.println(String.format("%f,%f", latitude, longitude));// итоговая широта и долгота
    }

    public void calculateAdress(String coordinates) throws JSONException, IOException {

        String url = calculateJSONRequest(selectionTypeAdress.latlng.toString(), coordinates);

        JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
        final JSONObject location = response.getJSONArray("results").getJSONObject(0);
        final String formattedAddress = location.getString("formatted_address");
        System.out.println(formattedAddress);// итоговый адрес
        adress = formattedAddress;
        //System.out.println(String.format("%f,%f", latitude, longitude));// итоговая широта и долгота
    }


    private String calculateJSONRequest(String enumTypeCoordinates, String сoordinates) throws IOException, JSONException {
       
        params = Maps.newHashMap();
        params.put("language", "ru");// язык данных, на котором мы хотим получить
        params.put("key", KEY_GOOGLE_API);
        params.put("sensor", "false");// исходит ли запрос на геокодирование от устройства с датчиком местоположения
        // текстовое значение широты/долготы, для которого следует получить ближайший понятный человеку адрес, долгота и
        // широта разделяется запятой, берем из предыдущего примера
        params.put(enumTypeCoordinates, сoordinates);
        final String url = baseUrl + '?' + JsonReader.encodeParams(params);// генерируем путь с параметрами
        System.out.println(url);// Путь, что бы можно было посмотреть в браузере ответ службы
        return url;

    }


    public CurrentPoint getCurrentPointViaAdress(String adress) throws IOException, JSONException {
        calculateLongitudeAndLatitude(adress);

        CurrentPoint currentPoint = new CurrentPoint(getlongitude(),
                getlatitude(),
                adress,
                null);

        return currentPoint;
    }

    public CurrentPoint getCurrentPointViaCoordinates(String сoordinates) throws IOException, JSONException {
        calculateAdress(сoordinates);

        CurrentPoint currentPoint = new CurrentPoint(getlongitude(),
                getlatitude(),
                adress,
                null);

        return currentPoint;
    }



    public double getlongitude(){
        return longitude;
    }

    public double getlatitude(){
        return latitude;
    }

    public String getCoordinates(){
        return latitude + "," + latitude;
    }

    public String getAdress(){
        return adress;
    }
}
