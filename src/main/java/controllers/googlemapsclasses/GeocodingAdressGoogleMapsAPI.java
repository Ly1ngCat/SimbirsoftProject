

package controllers.googlemapsclasses;

import com.google.common.collect.Maps;
import model.CurrentPoint;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class GeocodingAdressGoogleMapsAPI implements RequestResponseLanguage{

    private Map<String, String> params;
    private String baseUrl = "https://maps.googleapis.com/maps/api/geocode/json";// путь к Geocoding API по HTTP
    private String KEY_GOOGLE_API = "";

    private double longitude = 0; //долгота
    private double latitude = 0;//широта
    private String adress = "";//адресс

    private enum selectionTypeAdress {address, latlng}

    public GeocodingAdressGoogleMapsAPI(String KEY_GOOGLE_API) {
        this.KEY_GOOGLE_API = KEY_GOOGLE_API;
    }

    public void calculateLongitudeAndLatitude (String adress)  {

        try {
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
        } catch (IOException | JSONException e) {
            longitude = 0;
            latitude = 0;
            e.printStackTrace();
        }
    }

    public void calculateAdress(String coordinates) {

        try {
            String url = calculateJSONRequest(selectionTypeAdress.latlng.toString(), coordinates);

            JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
            final JSONObject location = response.getJSONArray("results").getJSONObject(0);
            final String formattedAddress = location.getString("formatted_address");
            System.out.println(formattedAddress);// итоговый адрес
            adress = formattedAddress;
            //System.out.println(String.format("%f,%f", latitude, longitude));// итоговая широта и долгота
        }catch (JSONException | IOException e) {
            adress = "";
            e.printStackTrace();
        }
    }


    private String calculateJSONRequest(String enumTypeCoordinates, String coordinates)  {
       
        params = Maps.newHashMap();
        params.put("language", answerLanguage);// язык данных, на котором мы хотим получить
        params.put("key", KEY_GOOGLE_API);
        params.put("sensor", "false");// исходит ли запрос на геокодирование от устройства с датчиком местоположения
        // текстовое значение широты/долготы, для которого следует получить ближайший понятный человеку адрес, долгота и
        // широта разделяется запятой, берем из предыдущего примера
        params.put(enumTypeCoordinates, coordinates);
        final String url = baseUrl + '?' + JsonReader.encodeParams(params);// генерируем путь с параметрами
        System.out.println(url);// Путь, что бы можно было посмотреть в браузере ответ службы
        return url;

    }


    public CurrentPoint getCurrentPointViaAdress(String adress)  {
        calculateLongitudeAndLatitude(adress);

        return new CurrentPoint(getlongitude(),
                getlatitude(),
                adress,
                null);
    }

    public CurrentPoint getCurrentPointViaCoordinates(String сoordinates) {
        calculateAdress(сoordinates);

        return new CurrentPoint(getlongitude(),
                getlatitude(),
                adress,
                null);
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
