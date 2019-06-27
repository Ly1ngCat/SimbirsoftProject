package controllers.googlemapsclasses;

import com.google.common.collect.Maps;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class GeocodingAdressGoogleMapsAPI {

    private Map<String, String> params;
    private String baseUrl = "https://maps.googleapis.com/maps/api/geocode/json";// путь к Geocoding API по HTTP
    private String KEY_GOOGLE_API;

     private double longitude;
    private double latitude;

    public GeocodingAdressGoogleMapsAPI(String KEY_GOOGLE_API) throws JSONException, IOException {
        this.KEY_GOOGLE_API = KEY_GOOGLE_API;
    }

    public void calculateLongitudeAndLatitude (String adress) throws JSONException, IOException {

        params = Maps.newHashMap();
        params.put("sensor", "false");// исходит ли запрос на геокодирование от устройства с датчиком местоположения
        params.put("key", KEY_GOOGLE_API);
        params.put("address", adress);// адрес, который нужно геокодировать
        final String url = baseUrl + '?' + JsonReader.encodeParams(params);// генерируем путь с параметрами
        System.out.println(url);// Путь, что бы можно было посмотреть в браузере ответ службы
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

    public double getlongitude(){
        return longitude;
    }

    public double getlatitude(){
        return latitude;
    }
}
