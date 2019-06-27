package controllers.openweathermapclasses;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.model.CurrentWeather;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map.Entry;
import com.google.common.base.Function;
import org.json.JSONException;
import org.json.JSONObject;
import controllers.googlemapsclasses.JsonReader;

public class WeatherInfo
{
    public static String cityName="Ulyanovsk";
    public static Double longitude;
    public static Double latitude;
    private Date visitDate;

    public WeatherInfo(Date visitDate)
    {
        this.visitDate=visitDate;
    }

    public void getDressRecommendation() //recommends dress in accordance with weather
    {

    }

    public void getWeatherInfo() //shows weather info for provided city at provided Date
    {

    }

    public static void Start() throws APIException, IOException, JSONException {
        WeatherInfo gwi=new WeatherInfo(new Date());
        // declaring object of "OWM" class
        String apiKey="1663d3f111783366b1c00872fb6ec203";
        String urlForecast="https://api.openweathermap.org/data/2.5/forecast?";
        Map<String, String> params=Maps.newHashMap();
        OWM owm = new OWM(apiKey);

        // getting current weather data for the "Ulyanovsk" city
        CurrentWeather cwd = owm.currentWeatherByCityName(cityName);

        // checking data retrieval was successful or not
        if (cwd.hasRespCode() && cwd.getRespCode() == 200)
        {

            // checking if city name is available
            if (cwd.hasCityName())
            {
                //printing city name from the retrieved data
                System.out.println("City: " + cwd.getCityName()+" "+cwd.getCoordData().getLatitude()
                        + " "+cwd.getCoordData().getLongitude() +"\nTime: "+gwi.visitDate);
                longitude=cwd.getCoordData().getLongitude();
                latitude=cwd.getCoordData().getLatitude();
                params.put("appid",apiKey);
                params.put("units","metric");
                params.put("lat",latitude.toString());
                params.put("lon",longitude.toString());
                final String url=urlForecast+JsonReader.encodeParams(params);
                System.out.println(url);
                final long forecastDate=1561741846; //UNIX time of 28.06.2019 17:10:46 GMT(0)
                long currentDate = System.currentTimeMillis()/1000; //current time
                final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ

                double di=(forecastDate-currentDate)/(3600*3);
                JSONObject locationForecast = response.getJSONArray("list").getJSONObject((int)di);
                System.out.println("The predicted temperature for date "
                        +new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(forecastDate*1000))
                        +" in "+cwd.getCityName()+" is "+locationForecast.getJSONObject("main").getDouble("temp"));
            }

            // checking if max. temp. and min. temp. is available
            if (cwd.hasMainData() && cwd.getMainData().hasTempMax() && cwd.getMainData().hasTempMin())
            {
                // printing the max./min. temperature
                System.out.println("Temperature: " + (cwd.getMainData().getTempMax()-273.15)
                        + "/" + (cwd.getMainData().getTempMin()-273.15) + "\'C");
            }
        }
    }
}
