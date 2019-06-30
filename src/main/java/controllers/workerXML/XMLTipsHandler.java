package controllers.workerXML;

import model.Weather;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class XMLTipsHandler extends DefaultHandler {

    private final String WEATHER = "weather";
    private final String STREET_TEMPERATURE = "streetTemperature";
    private final String RULE = "rule";
    private final String TEXT = "text";


    private String typeWeather = "";
    private double temperature = 0;

    private ArrayList<String> listTagTurn = new ArrayList<String>();
    private ArrayList<String> rules = new ArrayList<String>();

    public XMLTipsHandler(String typeWeather, double temperature) {
        this.typeWeather = typeWeather;
        this.temperature = temperature;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        addTagFromTurn(qName);

        if (listTagTurn.contains(WEATHER)) {
            rulesByWeatherType(typeWeather, attributes);
        }

        if (listTagTurn.contains(STREET_TEMPERATURE)) {
            rulesByTemperature(attributes);
        }

    }

    private void rulesByWeatherType(String typeWeather, Attributes attributes) {

        if (isChildTag(WEATHER, typeWeather) && isChildTag(typeWeather, RULE)) {

            addRule(attributes);
        }
    }

    private void rulesByTemperature(Attributes attributes) {

        String tagTemperature = returnTagTemperature(temperature);

        if (isChildTag(STREET_TEMPERATURE, tagTemperature) && isChildTag(tagTemperature, RULE)) {
            addRule(attributes);
        }
    }

    private String returnTagTemperature(double temperature) {
        if ( temperature < 5) { //TODO: Поправить кучу иф, потому что кал
            return "negative";
        } else if (temperature >= 5 && temperature <= 20) {
            return "neutral";
        } else {
            return "positive";
        }
    }

    private void addRule(Attributes attributes) {
        String text = attributes.getValue(TEXT);
        rules.add(text);
    }

    private boolean isChildTag(String tagParent, String tagChild) {
        return (listTagTurn.lastIndexOf(tagChild) > listTagTurn.lastIndexOf(tagParent));
    }

    private void addTagFromTurn(String nameTag) {
        listTagTurn.add(nameTag);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        deleteTagFromTurn(qName);
    }

    private void deleteTagFromTurn(String nameTag) {
        listTagTurn.remove(listTagTurn.lastIndexOf(nameTag));
    }

    @Override
    public void endDocument() {
        //ArrayList<String> ggg = new ArrayList<String>(rules);
    }

    public ArrayList<String> getRecommendations() {
        return rules;
    }
}