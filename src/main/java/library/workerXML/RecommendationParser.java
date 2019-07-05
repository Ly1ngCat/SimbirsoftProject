package library.workerXML;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public  class RecommendationParser {

     public RecommendationParser() { }

   static public ArrayList<String> getRecommendation(String typeWeather, double temperature) {

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            XMLTipsHandler handler = new XMLTipsHandler(typeWeather, temperature);

            parser.parse(new File("src/main/resources/tips_xml.xml"), handler);

            return handler.getRecommendations();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }
}

