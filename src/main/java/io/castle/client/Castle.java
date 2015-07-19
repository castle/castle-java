package io.castle.client;

import io.castle.client.http.HttpConnectorSupplier;

import java.net.URI;

public class Castle {

    private static final URI API_BASE_URI = URI.create("https://api.castle.io/");

    private static volatile URI apiBaseURI = API_BASE_URI;

    private static final String VERSION = "0.5.2-SNAPSHOT";

    public static final String USER_AGENT = "castle-java/" + Castle.VERSION;

    private static volatile String secret;

    private static volatile int connectionTimeout = 3 * 1000;

    private static volatile int requestTimeout = 60 * 1000;

    private static volatile boolean disableTracking = false;

    private static volatile HttpConnectorSupplier httpConnectorSupplier = HttpConnectorSupplier.defaultSupplier;

    public static int getConnectionTimeout() {
	return connectionTimeout;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void setConnectionTimeout(int connectionTimeout) {
	Castle.connectionTimeout = connectionTimeout;
    }

    public static int getRequestTimeout() {
	return requestTimeout;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void setRequestTimeout(int requestTimeout) {
	Castle.requestTimeout = requestTimeout;
    }

    public static HttpConnectorSupplier getHttpConnectorSupplier() {
	return httpConnectorSupplier;
    }

    public static void setHttpConnectorSupplier(HttpConnectorSupplier supplier) {
	Castle.httpConnectorSupplier = supplier;
    }

    public static String getSecret() {
	return Castle.secret;
    }

    public static void setSecret(String secret) {
	Castle.secret = secret;
    }

    public static URI getApiBaseURI() {
	return Castle.apiBaseURI;
    }

    public static void setApiBaseURI(URI apiBaseURI) {
	Castle.apiBaseURI = apiBaseURI;
    }

    public static void setDisableTracking(boolean disableTracking) {
        Castle.disableTracking = disableTracking;
    }

    public static boolean isDisableTracking() {
        return Castle.disableTracking;
    }

}
