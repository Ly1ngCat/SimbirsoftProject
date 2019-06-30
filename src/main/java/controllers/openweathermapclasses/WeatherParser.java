package controllers.openweathermapclasses;

import com.google.common.collect.Maps;
import controllers.googlemapsclasses.JsonReader;
import model.CurrentPoint;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class WeatherParser
{
    //private static String apiKey="1663d3f111783366b1c00872fb6ec203";

    private static long dateToEpochSec(LocalDateTime dateTime)
    {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()/1000;
    }
    public static HashMap<String, String> parser(CurrentPoint point) throws JSONException, IOException {
        Map<String, String> params= Maps.newHashMap();
        String urlForecast="https://api.openweathermap.org/data/2.5/forecast?";

        FileInputStream fisConfig = new FileInputStream("src/main/resources/config.properties");
        Properties propertyConfig = new Properties();
        propertyConfig.load(fisConfig);

        params.put("appid",propertyConfig.getProperty("OWMKey"));
        params.put("units","metric");
        params.put("lat",""+point.latitude);
        params.put("lon",""+point.longitude);

        final String url=urlForecast+ JsonReader.encodeParams(params);

        long currentDate = dateToEpochSec(LocalDateTime.now());
        long forecastDate=dateToEpochSec(point.getForecastDate());

        System.out.println(forecastDate);
        System.out.println(currentDate);
        final JSONObject response = JsonReader.read(url); // делаем запрос к вебсервису и получаем от него ответ

        HashMap<String, String> conditions=new HashMap<>();

        double di=(forecastDate-currentDate)/(3600*3); //TODO: Магические числа
        JSONObject locationForecast = response.getJSONArray("list").getJSONObject((int)di);

        conditions.put("cityID", response.getJSONObject("city").getString("id"));
        conditions.put("cityName", response.getJSONObject("city").getString("name"));
        conditions.put("temp", locationForecast.getJSONObject("main").getString("temp"));
        conditions.put("tempMin", locationForecast.getJSONObject("main").getString("temp_min"));
        conditions.put("tempMax", locationForecast.getJSONObject("main").getString("temp_max"));
        conditions.put("weatherType", locationForecast.getJSONArray("weather").getJSONObject(0).getString("main"));
        conditions.put("weatherDesc", locationForecast.getJSONArray("weather").getJSONObject(0).getString("description"));
        conditions.put("windSpeed", locationForecast.getJSONObject("wind").getString("speed"));
        System.out.println(conditions);
        return conditions;
    }
}
