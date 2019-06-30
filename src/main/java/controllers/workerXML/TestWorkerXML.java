package controllers.workerXML;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TestWorkerXML {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        ArrayList<String> ggg = RecommendationParser.getRecomendation("ash", 20);
        for ( String rules : ggg)
            //System.out.println(String.format("Имя сотрудника: %s, его должность: %s", employee.getName(), employee.getJob()));
            System.out.println(rules);
    }
}
