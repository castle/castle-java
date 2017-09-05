package io.castle.client.model;

/**
 * The reference to the current version of the SDK.
 */
public class CastleSdkRef {
    private String name = "Castle";
    private String version = "0.6.0-SNAPSHOT";

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
}
