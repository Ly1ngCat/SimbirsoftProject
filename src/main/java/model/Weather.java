package model;

import java.io.IOException;
import java.util.Map;
import controllers.openweathermapclasses.WeatherParser;
import org.json.JSONException;

public class Weather
{
    private double predictedTempMin=0;
    private double predictedTempMax=0;
    private double predictedTemp=0;
    private String OWMcityName="Unknown place";
    private int OWMcityID=-1;
    private String weatherType="No info";
    private String shortWeatherDescription="No info";
    private double windSpeed=0;

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

    public double getPredictedTempMin() {
        return predictedTempMin;
    }

    public double getPredictedTempMax() {
        return predictedTempMax;
    }

    public double getPredictedTemp() {
        return predictedTemp;
    }

    public String getOWMcityName() {
        return OWMcityName;
    }

    public int getOWMcityID() {
        return OWMcityID;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public String getShortWeatherDescription() {
        return shortWeatherDescription;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

}
