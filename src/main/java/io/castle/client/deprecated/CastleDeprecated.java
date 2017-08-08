package io.castle.client.deprecated;

import io.castle.client.deprecated.http.HttpConnectorSupplier;

import java.net.URI;

public class CastleDeprecated {

    private static final URI API_BASE_URI = URI.create("https://api.castle.io/v1/");

    private static volatile URI apiBaseURI = API_BASE_URI;

    private static final String VERSION = "0.5.2-SNAPSHOT";

    public static final String USER_AGENT = "castle-java/" + CastleDeprecated.VERSION;

    private static volatile String secret;

    private static volatile int connectionTimeout = 3 * 1000;

    private static volatile int requestTimeout = 30 * 1000;

    private static volatile boolean disableTracking = false;

    private static volatile HttpConnectorSupplier httpConnectorSupplier = HttpConnectorSupplier.defaultSupplier;

    public static int getConnectionTimeout() {
	return connectionTimeout;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void setConnectionTimeout(int connectionTimeout) {
	CastleDeprecated.connectionTimeout = connectionTimeout;
    }

    public static int getRequestTimeout() {
	return requestTimeout;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void setRequestTimeout(int requestTimeout) {
	CastleDeprecated.requestTimeout = requestTimeout;
    }

    public static HttpConnectorSupplier getHttpConnectorSupplier() {
	return httpConnectorSupplier;
    }

    public static void setHttpConnectorSupplier(HttpConnectorSupplier supplier) {
	CastleDeprecated.httpConnectorSupplier = supplier;
    }

    public static String getAPISecret() {
	return CastleDeprecated.secret;
    }

    public static void setAPISecret(String secret) {
	CastleDeprecated.secret = secret;
    }

    public static URI getApiBaseURI() {
	return CastleDeprecated.apiBaseURI;
    }

    public static void setApiBaseURI(URI apiBaseURI) {
	CastleDeprecated.apiBaseURI = apiBaseURI;
    }

    public static void setDisableTracking(boolean disableTracking) {
        CastleDeprecated.disableTracking = disableTracking;
    }

    public static boolean isDisableTracking() {
        return CastleDeprecated.disableTracking;
    }

}
