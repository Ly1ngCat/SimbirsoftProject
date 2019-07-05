package library;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigParse {


    static public String getValueByKey(String pathFileConfig, String key)  {
        try {
            FileInputStream fisConfig = new FileInputStream(pathFileConfig);
            Properties propertyConfig = new Properties();
            propertyConfig.load(fisConfig);

            return propertyConfig.getProperty(key);

        } catch (IOException e) {
            e.printStackTrace();
            return ""; //TODO: Временное решение, хотя класс, который в себя берёт значение уже должен знать, что делать, если в него пришел null
        }
    }

}
