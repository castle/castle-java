package io.castle.client.model;

import java.util.Objects;
import java.util.Properties;
import io.castle.client.Castle;
import io.castle.client.internal.config.PropertiesReader;

import java.io.InputStream;

/**
 * The reference to the current version of the SDK.
 */
public class CastleSdkRef {
    private String name = "castle-java";
    private String version;
    private String platform;
    private String platformVersion;

    CastleSdkRef() {
        this.version = loadSdkVersion().getProperty("sdk.version");
        this.platformVersion = getJavaVersion();
        this.platform = getJavaPlatform();
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastleSdkRef that = (CastleSdkRef) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }


    @Override
    public String toString() {
        return "CastleSdkRef{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", platform='" + platform + '\'' +
                ", platformVersion='" + platformVersion + '\'' +
                '}';
    }

    private Properties loadSdkVersion() {
        Properties versionProperties = new Properties();
        PropertiesReader reader = new PropertiesReader();
        InputStream resourceAsStream = Castle.class.getClassLoader().getResourceAsStream("version.properties");
        return reader.loadPropertiesFromStream(versionProperties, resourceAsStream);
    }

    public static String getJavaVersion() {
        return System.getProperty("java.vm.version");
    }

    public static String getJavaPlatform() {
        return System.getProperty("java.vm.name");
    }
}
