package controllers.googlemapsclasses;

import com.google.common.collect.Maps;
import model.CurrentPoint;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;

public class DirectionSample extends AbstractSample {

    private String KEY_GOOGLE_API = "";
    private final String baseUrl = "https://maps.googleapis.com/maps/api/directions/json";// путь к Geocoding API по

    protected enum travelModes {driving, walking, bicycling, transit}

    private String distance;
    private String duration;

    public DirectionSample(String KEY_GOOGLE_API) throws JSONException, IOException {
        this.KEY_GOOGLE_API = KEY_GOOGLE_API;
    }

    public void calculateRoute(String origin, String destination, travelModes travelModes) throws JSONException, IOException {

        // HTTP
        final Map<String, String> params = Maps.newHashMap();
        params.put("sensor", "false");// указывает, исходит ли запрос на геокодирование от устройства с датчиком
        params.put("language", "ru");// язык данные на котором мы хочем получить
        params.put("mode", travelModes.toString());// способ перемещения, может быть driving, walking, bicycling. transit запрашивает указания по маршрутам общественного транспорта (при наличии). Если вы установите режим transit, можно дополнительно указать либо departure_timeили arrival_time. Если время не указано, по departure_timeумолчанию используется текущее время (то есть время отправления по умолчанию равно текущему времени). Вы также можете при желании включать transit_modeи / или transit_routing_preference.
        params.put("origin", origin);// адрес или текстовое значение широты и
        // отправного пункта маршрута
        params.put("destination", destination);// адрес или текстовое значение широты и долготы
        // долготы конечного пункта маршрута
        params.put("key", KEY_GOOGLE_API);//Ключ API
        final String url = baseUrl + '?' + encodeParams(params);// генерируем путь с параметрами
        System.out.println(url); // Можем проверить что вернет этот путь в браузере
        final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
        // как правило наиболее подходящий ответ первый и данные о кординатах можно получить по пути
        // //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
        JSONObject location = response.getJSONArray("routes").getJSONObject(0);
        location = location.getJSONArray("legs").getJSONObject(0);
        distance = location.getJSONObject("distance").getString("text");
        duration = location.getJSONObject("duration").getString("text");

        System.out.println(distance + "\n" + duration);
    }


    public void calculateRouteWithCurrentModel(CurrentPoint origin, CurrentPoint destination, travelModes travelModes) throws IOException, JSONException {

        String stringOrigin = assignValueAddresses(origin);
        String stringDestination = assignValueAddresses(destination);

        calculateRoute(stringOrigin,stringDestination, travelModes);

    }

    private String assignValueAddresses(CurrentPoint origin)
    {
        String adress;
        if (origin.latitude != 0 & origin.longitude != 0) {
            adress = String.valueOf(origin.latitude) + "," + String.valueOf(origin.longitude);
        }else
        {
            adress = origin.adressString;
        }
        return adress;
    }


    public String getDitanceString() {
        return distance;
    }

    public String getDurationString() {
        return duration;
    }

}

