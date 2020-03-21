package bavision.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {
    public static final String DB_KEY_URL = "db.url";
    public static final String DB_KEY_USER = "db.user";
    public static final String DB_KEY_PASSWORD = "db.password";
    public static final String DB_KEY_MIN_IDLE = "db.MinIdle";
    public static final String DB_KEY_MAX_IDLE = "db.maxIdle";
    public static final String DB_KEY_MAX_OPEN_PREPARED_STATEMENTS = "db.maxOpenPrepStatemenets";

    private Properties properties;
    private final String propertiesFileName = "config";

    public PropertiesHelper() {
        try (InputStream input = PropertiesHelper.class.getClassLoader().getResourceAsStream(propertiesFileName + ".properties")) {
            properties = new Properties();
            if (input == null) {
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getValueByKeyName(String key) {
        return properties.getProperty(key);
    }

}
