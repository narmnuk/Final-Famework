package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    public static String readProperty(String filePath, String property) {

        Properties prop = new Properties();

        try {
            FileInputStream fileInput = new FileInputStream(filePath);
            prop.load(fileInput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop.getProperty(property);
    }
}
