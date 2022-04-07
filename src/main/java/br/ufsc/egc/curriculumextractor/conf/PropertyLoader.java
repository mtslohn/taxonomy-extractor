package br.ufsc.egc.curriculumextractor.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    private static final String PROPERTIES_FILE = "/app.properties";

    private final Properties properties;

    public PropertyLoader() {
        this.properties = loadPropertiesFile();
    }

    public PropertyLoader(Properties properties) {
        this.properties = properties;
    }

    public String getProperty(ServiceProperty serviceProperty) {
        return properties.getProperty(serviceProperty.getPropertyName());
    }

    private Properties loadPropertiesFile() {
        Properties properties = new Properties();
        try (InputStream is = this.getClass().getResourceAsStream(PROPERTIES_FILE)) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
