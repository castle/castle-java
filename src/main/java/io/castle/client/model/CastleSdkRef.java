package io.castle.client.model;

import java.util.Properties;
import io.castle.client.Castle;
import io.castle.client.internal.config.PropertiesReader;

import java.io.InputStream;

/**
 * The reference to the current version of the SDK.
 */
public class CastleSdkRef {
    private String name = "Castle";

    private String version;

    CastleSdkRef() {
        this.version = loadSdkVersion().getProperty("sdk.version");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "CastleSdkRef{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    private Properties loadSdkVersion() {
        Properties versionProperties = new Properties();
        PropertiesReader reader = new PropertiesReader();
        InputStream resourceAsStream = Castle.class.getClassLoader().getResourceAsStream("version.properties");
        return reader.loadPropertiesFromStream(versionProperties, resourceAsStream);
    }
}
