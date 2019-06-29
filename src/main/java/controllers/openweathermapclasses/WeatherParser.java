package controllers.openweathermapclasses;

import com.google.common.collect.Maps;
import controllers.googlemapsclasses.JsonReader;
import model.CurrentPoint;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WeatherParser
{
    private static String apiKey="1663d3f111783366b1c00872fb6ec203"; //TODO: Игорь. Сделвй выгрузку ключа из ресурсов, будут вопросы пиши
    private static String urlForecast="https://api.openweathermap.org/data/2.5/forecast?";

    public static HashMap<String, String> parser(CurrentPoint point) throws JSONException, IOException {
        Map<String, String> params= Maps.newHashMap();
        //OWM owm = new OWM(apiKey);
        params.put("appid",apiKey);
        params.put("units","metric");
        params.put("lat",""+point.latitude);
        params.put("lon",""+point.longitude);
        final String url=urlForecast+ JsonReader.encodeParams(params);
        long currentDate = System.currentTimeMillis()/1000;//TODO: Магические числа
      final long forecastDate=11231414L;// point.forecastDate.getTime() закоментил Ринат, чтобы не выдавал ошибку
       System.out.println(forecastDate);
        final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ

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
