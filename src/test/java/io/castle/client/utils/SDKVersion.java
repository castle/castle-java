package io.castle.client.utils;

import io.castle.client.Castle;
import io.castle.client.internal.config.PropertiesReader;

import java.io.InputStream;
import java.util.Properties;

public class SDKVersion {

    private static final String libraryString = "\"library\":{\"name\":\"castle-java\",\"version\":\"" + getSDKVersion() + "\",\"platform\":\"" + getJavaPlatform() + "\",\"platform_version\":\"" + getJavaVersion() + "\"}";

    private static final String version = getSDKVersion();

    public static String getLibraryString() {
        return libraryString;
    }

    public static String getVersion() {
        return version;
    }

    private static String getSDKVersion() {
        Properties versionProperties = new Properties();
        PropertiesReader reader = new PropertiesReader();
        InputStream resourceAsStream = Castle.class.getClassLoader().getResourceAsStream("version.properties");
        reader.loadPropertiesFromStream(versionProperties, resourceAsStream);
        return versionProperties.getProperty("sdk.version");
    }

    public static String getJavaVersion() {
        return System.getProperty("java.vm.version");
    }

    public static String getJavaPlatform() {
        return System.getProperty("java.vm.name");
    }
}
