package model;

import java.io.IOException;
import java.util.Map;
import controllers.openweathermapclasses.WeatherParser;
import org.json.JSONException;

public class Weather
{
    public double predictedTempMin;
    public double predictedTempMax;
    public double predictedTemp;

    public String OWMcityName;
    public int OWMcityID;

    public String weatherType;
    public String shortWeatherDescription;
    public double windSpeed;

    public Weather(CurrentPoint point)
    {
        try
        {
            Map<String, String> params=WeatherParser.parser(point);
            this.OWMcityID=Integer.parseInt(params.get("cityID"));
            this.OWMcityName=params.get("cityName");
            this.predictedTemp=Double.parseDouble(params.get("temp"));
            this.predictedTempMin=Double.parseDouble(params.get("tempMin"));
            this.predictedTempMax=Double.parseDouble(params.get("tempMax"));
            this.weatherType=params.get("weatherType");
            this.shortWeatherDescription=params.get("weatherDesc");
            this.windSpeed=Double.parseDouble(params.get("windSpeed"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
