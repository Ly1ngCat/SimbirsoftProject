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
    private static long dateToEpochSec(LocalDateTime dateTime)
    {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()/1000;
    }

    public static HashMap<String, String> parser(CurrentPoint point) throws IOException {
        Map<String, String> params= Maps.newHashMap();
        String urlForecast="https://api.openweathermap.org/data/2.5/forecast?";

        FileInputStream fisConfig = new FileInputStream("src/main/resources/config.properties");
        Properties propertyConfig = new Properties();
        propertyConfig.load(fisConfig);
        params.put("appid",propertyConfig.getProperty("OWMKey"));
        params.put("units","metric");
        params.put("lat",""+point.getLatitude());
        params.put("lon",""+point.getLongitude());

        final String url=urlForecast+ JsonReader.encodeParams(params);

        try
        {
            JSONObject response = JsonReader.read(url); // делаем запрос к вебсервису и получаем от него ответ JSON
            HashMap<String, String> conditions=new HashMap<>();
            long currentDate = dateToEpochSec(LocalDateTime.now());
            long forecastDate=dateToEpochSec(point.getForecastDate());
            double di=(forecastDate-currentDate)/(3600*3); //получаем нужный индекс для массива предсказаний погоды
            if (di<0) //если выбрали дату раньше, чем текущая, то берем текущий прогноз погоды
            {
                di=0.0;
            }
            else
            {
                if (di>40) //если выбрали дату позже, чем 5 суток, то берем самый последний доступный прогноз
                {
                    di=39.0;
                }
            }
            try
            {
                JSONObject locationForecast = response.getJSONArray("list").getJSONObject((int)di);
                try {
                    conditions.put("cityID", response.getJSONObject("city").getString("id"));

                } catch (JSONException e) {
                    conditions.put("cityID", "-1");
                }
                try {
                    conditions.put("cityName", response.getJSONObject("city").getString("name"));
                } catch (JSONException e) {
                    conditions.put("cityName", "Unknown");
                }
                try {
                    conditions.put("temp", locationForecast.getJSONObject("main").getString("temp"));
                    conditions.put("tempMin", locationForecast.getJSONObject("main").getString("temp_min"));
                    conditions.put("tempMax", locationForecast.getJSONObject("main").getString("temp_max"));
                    conditions.put("weatherType", locationForecast.getJSONArray("weather").getJSONObject(0).getString("main"));
                    conditions.put("weatherDesc", locationForecast.getJSONArray("weather").getJSONObject(0).getString("description"));
                    conditions.put("windSpeed", locationForecast.getJSONObject("wind").getString("speed"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            System.out.println(conditions);
            return conditions;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return new HashMap<String, String>();

    }
}
