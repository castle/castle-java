# Java SDK for Castle

**[Castle](https://castle.io) adds real-time monitoring of your authentication stack,
instantly notifying you and your users on potential account hijacks.**

To generate the javadoc documentation run:

    mvn clean compile javadoc:javadoc

The java will be located in the `target/site` directory.

To check test coverage run:
    
    mvn clean test jacoco:report

The coverage report will be on the page `target/jacoco-ut/index.html`

# Quickstart

TODO

TODO: Create internal references to other sections in the README.md

# Where to Find Documentation

The [offical Castle documentation](https://castle.io/docs) contains information for other platforms
and documents all possible use cases of the API.
This file contains a guide that should get you started as quickly as possible with the Java SDK.
There is also available a Javadoc.
TODO: point to the right URL with the javadoc
Furthermore, there is a [sample application](https://github.com/castle/castle-java-example)
using Java Servlets and this SDK.

# Configuring the SDK

## Settings

Before running an application that uses the Castle Java SDK,
there are two settings that must be configured.
These are:

 * **App ID**: an identifier for the application associated with a valid Castle account; 
 * **API Secret**: an identifier that will be used for authentication purposes.
 
Both of them can be found in the settings page of a Castle account.
If they are are not provided, the client's initialization process will fail.

Besides the aforementioned settings, the following are other application-level setting
that can be optionally configured:

 * **Blacklisted Headers**: a comma-separated list of strings representing HTTP headers that will
 never get passed to the context object. See [The Context Object](#the-context-object).
 * **Whitelisted Headers**: this is a comma-separated list of strings representing HTTP headers
 that will get passed to the context object with each call to the Castle API,
 unless they are blacklisted. See [The Context Object](#the-context-object).
 * **Authenticate Failover Strategy**: it can be set to `ALLOW`, `DENY`, `CHALLENGE` or `THROW`.
 This sets the action that will be taken when a request to the `/v1/authenticate` endpoint
 of the Castle API fails. See also [Authenticate](#authenticate)
 * **Timeout**: an integer that represents the time in milliseconds after which a request fails. 
 * **Backend Provider**: The HTTP layer that will be used to make requests to the Castle API.
 Currently there is only one available and it uses [OkHttp](https://square.github.io/okhttp/).
 * **Base URL**: The base endpoint of the Castle API without any relative path.
 
If the value of any of this is left unspecified, the client will be configured with their default values.

## Where to Configure settings

Settings can be provided as a Java Properties file in the classpath or through
environmental variables.
When both of these options are used, environmental variables take precedence over Java
properties.
When neither option is used, the client's initialization process will provide a default value or fail,
in case there is no such value.

The following table shows the default value for each setting,
as well as the key that can be used to set its value in a properties
file or the environmental variable that can be used instead:

Setting | Default values, when they exist | Properties file key | Environmental variable |
--- | --- | --- | --- |
App ID |   | `app_id` | `CASTLE_SDK_APP_ID` |
API Secret |   | `api_secret` | `CASTLE_SDK_API_SECRET` |
Whitelisted Headers | `User-Agent,Accept-Language,Accept-Encoding,Accept-Charset,Accept,Accept-Datetime,X-Forwarded-For,Forwarded,X-Forwarded,X-Real-IP,REMOTE_ADDR` | `white_list` | `CASTLE_SDK_WHITELIST_HEADERS` |
Blacklisted Headers | `Cookie` | `black_list` | `CASTLE_SDK_BLACKLIST_HEADERS` |
Timeout | `500` | `timeout` | `CASTLE_SDK_TIMEOUT` |
Authenticate Failover Strategy | `ALLOW` | `failover_strategy` | `CASTLE_SDK_AUTHENTICATE_FAILOVER_STRATEGY` |
Backend Provider | `OKHTTP` | `backend_provide` | `CASTLE_SDK_BACKEND_PROVIDER` |
Base URL | `https://api.castle.io/` | `base_url` | `CASTLE_SDK_BASE_URL` |

By default, the SDK will look for the Java Properties file named `castle_sdk.properties`
in the classpath.
An alternative filename can be chosen by setting the `CASTLE_PROPERTIES_FILE` environment variable to a different value.

The following is a sample Java Properties file containing all of the settings that can be
modified: 
    
    app_id=
    api_secret=
    white_list=User-Agent,Accept-Language,Accept-Encoding,Accept-Charset,Accept,Accept-Datetime,X-Forwarded-For,Forwarded,X-Forwarded,X-Real-IP,REMOTE_ADDR
    black_list=Cookie
    timeout=500
    backend_provider=OKHTTP
    failover_strategy=ALLOW
    base_url=https://api.castle.io/

## Initializing the SDK

The Castle Java SDK must be initialized once in the life cycle of an application using it.
There is a static method in the `io.castle.client.Castle` class to do so.
Before trying to call any API method, the `io.castle.client.Castle#verifySdkConfigurationAndInitialize`
should be called, preferably during the initialization process of the application using the SDK.
Then the most basic errors will be discovered as early as possible.

# API calls

The `io.castle.client.api.CastleApi` interface has all methods needed to perform calls to
the Castle API.
Once the SDK has been initialized as described above, getting an instance of `CastleApi` is a matter of calling the
`io.castle.client.Castle#onRequest` method in the context of a Java Servlet.

The following is an example of such a use case:
```java
import io.castle.client.Castle;
import io.castle.client.api.CastleApi;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/endpoint")
public class SomeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CastleApi castleApi = Castle.sdk().onRequest(req);
        ...
    }
}
```

Notice that the `io.castle.client.Castle#sdk` method is called first in order to get the singleton
instance of the `io.castle.client.Castle` class.
With this instance it is possible to use non-static methods of `io.castle.client.Castle`.

## The Context Object

Read more about the [request context](https://castle.io/docs/context) in the official documentation.

An `io.castle.client.api.CastleApi` instance obtained from a call to `io.castle.client.Castle#onRequest`
contains a context object in JSON format in a private field.
Its data consists of metadata taken from the `HttpServletRequest` passed to `onRequest`.

It is possible to provide additional metadata to the context using the fluent api on `io.castle.client.api.CastleApi` 
as follows:
```java
CastleApi newAPIRef = Castle.sdk().onRequest(req).mergeContext(additionalContextObject);

```

Any POJO class can be used as an argument of the mergeContext class.
To get a complete override of the default context,
use a instance of `io.castle.client.model.CastleContext`.

Notice that the signature of the `mergeContext` method is `CastleApi`, which means that
the merge operation results in a new reference to a `CastleApi` instance whose context object is set to the merged
context object.  
Also, notice that, because of this, the `io.castle.client.api.CastleApi#mergeContext` can be used multiple times.

### Context Keys

Below is a full list of all keys Castle understands:
 
```JSON
    {
        "active": true, // if absent default to true
        "device": {
          "id": "B5682FA0-C21E-11E4-8DFC-CDF9AAEE34FF",
          "manufacturer": "Apple",
          "model": "iPhone7,2",
          "name": "umami",
          "type": "ios",
          "token": "aa45bb0d80d4bb6cd60854ff165dd548c838g5605bbfb9571066395b8c9da449" 
        },
        "client_id": "AAAAAAAA-CCCC-DDD4-FFFF-DD00AACDAFB",
        "page": {
          "path": "/example/",
          "referrer": "",
          "search": "",
          "title": "Example INC ",
          "url": "https://example.com/sample/"
        },
        "referrer": {
          "id": "ABCD582CDEFFFF01919",
          "type": "dataxu"
        },
        "headers": {
            "Accept-Language": "en-US,en;q=0.8"
          }
        },
        "ip": "8.8.8.8",
        "library": {
          "name": "Castle",
          "version": "0.6.0-SNAPSHOT""
        },
        "locale": "nl-NL", // will default to Accept-Language header with q==1
        "location": { // will default to IP location 
          "city": "San Francisco",
          "country": "United States",
          "latitude": 40.3194747,
          "longitude": -79.0388367,
          "speed": 0
        },
        "network": {
          "bluetooth": false,
          "carrier": "T-Mobile NL",
          "cellular": true,
          "wifi": false
        },
        "os": {
          "name": "iPhone OS",
          "version": "8.1.3"
        },
        "screen": {
          "width": 320,
          "height": 568,
          "density": 2
        },
        "timezone": "Europe/WarsaW",
        "userAgent": "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1" //will default to request.headers.user_agent
      }
```

### Default Context

As seen above the default context gets constructed from a `HttpServletRequest`.
The following is a sample default context created:

```JSON
    {
        "active": true, // default to true
        "client_id": "B5682FA0-C21E-11E4-8DFC-CDF9AAEE34FF",
        "headers": {
            "Accept-Language": "en-US,en;q=0.8",
            ...
          }
        },
        "ip": "8.8.8.8",
        "library": {
          "name": "Castle",
          "version": "0.6.0-SNAPSHOT""
        },
        "userAgent": "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1" //will default to headers.user_agent
      }
```
The following remarks explain how the values of the fields are set:
* The `client_id` key is derived from the cookie `__cid`.
* If the key `__cid`  does not exist, the `client_id` key takes its value from the header `X-Castle-Client-Id`. 
* All HTTP headers along with the `REMOTE_ADDR` CGI header are taken from the `HttpServletRequest`; 
a JSON Object with the header name as key and the header value as value is created and set as value for the `headers` key
in the context JSON.
* After it is created, `headers` object gets filtered according to the blacklist and whitelist;
that is, headers in the blacklist are never passed to the context, while those headers that are whitelisted,
but not blacklisted, get passed to the context.
* The library key is added specifying the version of the SDK used.
* The the value of the `User-Agent` HTTP header is used as the value for the `userAgent` key in the context.

### Merging Strategy

The strategy for merging contexts is a recursive one.
More specifically, when the optional context is provided, the merging strategy works as follows:
* If the optional context contains a key and a non-null value, then:
    - if there was no such key in the original context, the client appends the key and value;
    - otherwise, the client merges the value of the key in the default context with the value of the
    additional context's key in the following manner:
        + JSON objects get recursively merged using the same strategy;
        + JSON arrays get combined in such a way that elements in the additional context get appended to
        elements in the original context;
        + JSON primitives in the original context get overwritten.
* If the optional context contains a key and a null value, and if the key is present in the original context,
the key is removed from the context.

## Authenticate

Information on when to use this method can be found in the section
on [adaptive authentication](https://castle.io/docs/authentication) of the official documentation.

The `io.castle.client.api.CastleApi#authenticate` method makes a synchronous call to the
`/v1/authenticate` endpoint of the Castle API.
It returns a `io.castle.client.model.Verdict` instance.
A verdict contains the following fields:

* **Authenticate action**: Can be one of `ALLOW`, `DENY` or `CHALLENGE`.
The semantics of each action is described in the documentation referred at the beginning of this section.
* **User ID**: a string representing a user id associated to an authentication attempt.
* **Failover boolean**:
* **Failover reason**:

## Track

For information on the use cases of this method, go to the sections on
[adaptive authentication](https://castle.io/docs/authentication) and 
[security events](https://castle.io/docs/events) of the official documentation.

The `io.castle.client.api.CastleApi#track` method makes an asynchronous call to the
`/v1/track` endpoint of the Castle API.

## Identify

## Review

Go to the section on [webhooks](https://castle.io/docs/webhooks) of the official documentation in order
to learn how to use the review method.

## The 'doNotTrack' Boolean

TODO: describe its behavior for all API calls.