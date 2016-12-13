# Java SDK for Castle

**[Castle](https://castle.io) adds real-time monitoring of your authentication stack, instantly notifying you and your users on potential account hijacks.**

## Installation

Clone the repository:

```bash
$ git clone git@github.com:castle/castle-java.git
```

Build it:

```
$ mvn clean install
```

Load and configure the library with your Castle API secret:

```java
Castle.setAPISecret("YOUR_API_SECRET");
```

## Documentation

[Official Castle docs](https://castle.io/docs)

## Exceptions

`CastleException` will be thrown if the Castle API returns a 400 or a 500 level HTTP response. The exception contains error details and the response code returned from the API. See https://api.castle.io/#errors for details about the different error codes, .

```java
try {
    Event.setUserInfoHeader(userInfoHeader).track(event);
} catch(CastleException u) {
    int code = u.getResponseCode();
    Error error = u.getError();
}
```

## Configuration

### HTTP

The client can be configured to accept any HTTP stack that implements
`java.net.HttpURLConnection` by implementing the `HttpConnectorSupplier`
 interface.

For example, to use [OkHttp](http://square.github.io/okhttp/) as a connection
supplier, create a supplier class:

```java
public class OkHttpSupplier implements HttpConnectorSupplier {
    private final OkUrlFactory urlFactory;

    public OkHttpSupplier(OkUrlFactory urlFactory) {
        this.urlFactory = urlFactory;
    }

    @Override
    public HttpURLConnection connect(URI uri) throws IOException {
        return urlFactory.open(uri.toURL());
    }
}
```

Then send a supplier to the Castle object:

```
final OkHttpClient client = new OkHttpClient();
final OkUrlFactory factory = new OkUrlFactory(client);
final OkHttpSupplier supplier = new OkHttpSupplier(factory);
Castle.setHttpConnectorSupplier(supplier);
```

### Timeouts

The default connection and request timeouts can be set in milliseconds using the
`Castle.setConnectionTimeout` (default: 3 s) and `Castle.setRequestTimeout` (default: 30 s) methods.
