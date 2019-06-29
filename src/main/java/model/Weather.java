package model;

import java.io.IOException;
import java.util.Map;
import controllers.openweathermapclasses.WeatherParser;
import org.json.JSONException;

public class Weather
{
    public double predictedTempMin;//TODO: нет начальнвх значений полей, в случае не заполнения модель упадёт, либо выставь начальне значений, либо обработай в catch
    public double predictedTempMax;
    public double predictedTemp;

    public String OWMcityName;
    public int OWMcityID;

    public String weatherType;
    public String shortWeatherDescription;
    public double windSpeed;

    public Weather(CurrentPoint point) throws IOException, JSONException { //TODO: Игорь. сделай внутренний try catch, так как ты всем методом наследовал обработку исключений без её реализации ( throws IOException, JSONException {), а это значит ты должен их будешь обработать в месте объявления, это не гуд
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

}
