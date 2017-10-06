package io.castle.client.internal.config;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    public Properties loadPropertiesFromStream(Properties loaded, @Nonnull InputStream resourceAsStream) {

        try (final InputStream streamFromFile = resourceAsStream) {
            loaded.load(streamFromFile);
        } catch (IOException e) {
            //OK, no file configuration, create a empty new properties just for security against side effects of the
            // failed load operation
            return new Properties();
        }
        return loaded;
    }
}
