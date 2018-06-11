# Java SDK for Castle

[![CircleCI](https://circleci.com/gh/castle/castle-java.svg?style=svg)](https://circleci.com/gh/castle/castle-java)

**[Castle](https://castle.io) analyzes device, location, and interaction patterns in your web and mobile apps and lets you stop account takeover attacks in real-time.**

# Quickstart

When using Maven, add the following dependency to your `pom.xml` file:
```xml
        <dependency>
            <groupId>io.castle</groupId>
            <artifactId>castle-java</artifactId>
            <version>1.0.3</version>
        </dependency>
```

## Initialize the SDK

Go to the settings page of your Castle account and find your **API Secret** and (optionally) your **APP ID**.

**Alt 1. Initialize using ENV variables**

On initialization the Castle SDK will look for the secret in `CASTLE_SDK_API_SECRET`.
If that is set, no options needs to be passed to the initialization.

```java
Castle.initialize();
```

**Alt 2. Initialize using configuration builder**

If you don't use ENV variables, you can set the secret programatically by using
a `CastleConfigurationBuilder`. `Castle.configurationBuilder()` returns a
configuration builder initialized with default settings.

```java
Castle.initialize(
  Castle.configurationBuilder()
    .apiSecret("abcd")
);
```

All other settings will be set to their default values.

## Tracking events

Once the SDK has been initialized, tracking requests are sent through the SDK
instance.

```java
// `req` is an instance of `HttpServletRequest`.
CastleApi castle = Castle.sdk().onRequest(req);

castle.track("$login.succeeded", "<user_id>");
```

Note that the `req` instance should be bound to the underlying request in order to extract the necessary information.
It means that a safe place to create the `CastleApi` instance is the request handling thread. After creation the
`CastleApi` instance can be passed to any thread independently of the original thread life cycle.

## Castle Javascript secure mode

When using Castle.js, secure mode can be enabled with the following helper:
```
_castle('secure',
    '<%= Castle.sdk().secureUserID(someUserID) %>');
```

## Java 7 configuration

To use the library on a java 7 environment, switch the guava library to the following version:
```xml
        <dependency>
            <groupId>io.castle</groupId>
            <artifactId>castle-java</artifactId>
            <version>1.0.3</version>
            <exclusions>
                <exclusion>
                    <groupId>com.google.guava</groupId>
                    <artifactId>guava</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>23.0-android</version>
        </dependency>
```

# Configuring the SDK

## Settings

Before running an application that uses the Castle Java SDK,
there is one that must be configured.
This is:

 * **API Secret**: a secret that will be used for authentication purposes.

If the API Secret is not provided, the client's initialization process will fail.

Similar to that, there is also another setting associated to a Castle account.

 * **App ID**: an application identifier associated with a valid Castle account.

Both of them can be found in the settings page of a Castle account.

Besides the aforementioned settings, the following are other application-level setting
that can be optionally configured:

 * **Blacklisted Headers**: a comma-separated list of strings representing HTTP headers that will
 never get passed to the context object. See [The Context Object](#the-context-object).
 * **Whitelisted Headers**: this is a comma-separated list of strings representing HTTP headers
 that will get passed to the context object with each call to the Castle API,
 unless they are blacklisted. See [The Context Object](#the-context-object).
 * **Authenticate Failover Strategy**: it can be set to `ALLOW`, `DENY`, `CHALLENGE` or `THROW`.
 See also [Authenticate](#authenticate)
 * **Timeout**: an integer that represents the time in milliseconds after which a request fails.
 * **Backend Provider**: The HTTP layer that will be used to make requests to the Castle API.
 Currently there is only one available and it uses [OkHttp](https://square.github.io/okhttp/).
 * **Base URL**: The base endpoint of the Castle API without any relative path.

Whitelist and Blacklist are case-insensitive.

If the value of any of these keys is left unspecified, the client will be configured with their default values.
See *[Where to Configure Settings](#where-to-configure-settings)* for a list of the default values.

## Where to Configure Settings

Settings can be provided as a Java Properties file in the classpath, or through
environmental variables.
When both of these options are used, environmental variables take precedence over the Java
Properties file.

The following table shows the default value for each setting.
It also shows the key that can be used to set its value in a Properties file.
Finally, it also contains the environmental variable that can be used instead of the key in the Java Properties file:

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
Log HTTP | false | `log_http` | `CASTLE_SDK_LOG_HTTP` |

By default, the SDK will look in the classpath for the Java Properties file named `castle_sdk.properties`.
An alternative file can be chosen by setting the `CASTLE_PROPERTIES_FILE` environment variable to a different value.

The following is a sample Java Properties file containing all of the settings that can be
modified:

```properties
app_id=
api_secret=
white_list=User-Agent,Accept-Language,Accept-Encoding,Accept-Charset,Accept,Accept-Datetime,X-Forwarded-For,Forwarded,X-Forwarded,X-Real-IP,REMOTE_ADDR
black_list=Cookie
timeout=500
backend_provider=OKHTTP
failover_strategy=ALLOW
base_url=https://api.castle.io/
log_http=false
```

## Initializing the SDK

The Castle Java SDK must be initialized once in the life cycle of an application using it.
There is a static method in the `io.castle.client.Castle` class to do so.
Before trying to call any API method, the `io.castle.client.Castle#verifySdkConfigurationAndInitialize`
should be called, preferably during the initialization process of the application using the SDK.
This method will discover the the most basic configuration errors as early as possible.

For more information on the logic of settings validation, see the section of the javadoc for the
`io.castle.client.internal.config` package.

## Secure Mode

See the documentation on [secure mode](https://castle.io/docs/secure_mode) in order to learn more.

In order to enable secure mode using Castle.js, use the
`io.castle.client.Castle#secureUserID` method whenever there is a need to make an identify call
from a jsp:

```jsp
            <script type="text/javascript">
                ...
                _castle('secure',
                    '<%= Castle.sdk().secureUserID(someUserID) %>');
                ...
            </script>
```

# API calls

The `io.castle.client.api.CastleApi` interface has all methods needed to perform requests to the Castle API.
Once the SDK has been properly initialized, as described above, getting an instance of `CastleApi` is a matter of
calling the `io.castle.client.Castle#onRequest` method in the context of a Java Servlet.

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
With this instance it is possible to use the non-static methods of `io.castle.client.Castle`.

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

Notice that the signature of the `mergeContext` method is `CastleApi`, which means that
the merge operation results in a new reference to a `CastleApi` instance whose context object is set to the merged
context object.
Also, notice that, because of this, the `io.castle.client.api.CastleApi#mergeContext` can be used multiple times.

### Context Keys

Below is a full list of all keys Castle understands:

```JSON
    {
        "active": true,
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
          },
        "ip": "8.8.8.8",
        "locale": "pl-PL",
        "location": {
          "city": "San Francisco",
          "country": "United States",
          "latitude": 40.3194747,
          "longitude": -79.0388367,
          "speed": 0
        },
        "network": {
          "bluetooth": false,
          "carrier": "T-Mobile PL",
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
        "userAgent": "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1"
      }
```

To get a complete override of the default context, use a instance of `io.castle.client.model.CastleContext`.

### Default Context

As seen above, the default context gets constructed from a `HttpServletRequest`.
The following is a sample default context created in such a manner:

```JSON
    {
        "active": true,
        "client_id": "B5682FA0-C21E-11E4-8DFC-CDF9AAEE34FF",
        "headers": {
            "Accept-Language": "en-US,en;q=0.8"
          },
        "ip": "8.8.8.8",
        "library": {
          "name": "Castle",
          "version": "1.0.1"
        },
        "userAgent": "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1"
      }
```
The following remarks explain how the values of the fields are set:
* The default value for the `active` key is `true`.
* The `client_id` key is derived from the cookie `__cid`.
* If the key `__cid` in the cookie header does not exist, the `client_id` key takes its value from the header
`X-Castle-Client-Id`.
* A JSON Object is created and set as value for the `headers` key in the context JSON.
All HTTP headers, along with the `REMOTE_ADDR` CGI header, are taken from the `HttpServletRequest` and
are used as key-values of the JSON object.
This is done in such a way that each header's name is taken as a key and its contents are used as the value.
* After it is created, the `headers` object gets filtered according to the blacklist and whitelist specified in the
[configuration](#configuring-the-sdk);
that is, headers in the blacklist are never passed to the context, while those headers that are whitelisted,
but not blacklisted, get passed to the context.
* The library key is added by default.
Its value specifies the version of the SDK used.
* The the value of the `User-Agent` HTTP header is used as the value for the `userAgent` key in the context.

### Merging Strategy

The strategy for merging contexts is a recursive one.
More specifically, when the optional context is provided, the merging strategy works as follows:
* If the optional context is `null`, the context returned is an empty JSON.
* If the optional context contains a key and a non-null value, then:
    - if there was no such key in the original context, the client appends the key and value;
    - otherwise, the client merges the value of the key in the default context with the value of the
    additional context's key in the following manner:
        + JSON objects get recursively merged using the same strategy;
        + JSON arrays get combined in such a way that elements in the additional context get appended to
        elements in the original context;
        + JSON primitives in the original context get overwritten by JSON primitives in the additional context.
* If the optional context contains a key and a null value, and if the key is present in the original context,
the key is removed from the context.

## Authenticate

Information on when to use this method can be found in the section
on [adaptive authentication](https://castle.io/docs/authentication) of the official documentation.

The `io.castle.client.api.CastleApi#authenticate` method makes a synchronous POST request to the
`/v1/authenticate` endpoint of the Castle API.
This behaviour can be disabled using the [`doNotTrack` boolean](the-donottrack-boolean).

There are two required parameters that need to be specified in order to make a request to the authenticate endpoint:

* **Event**: a string with the name of an event understood by the Castle API.
* **User ID**: a string representing a user ID.

Moreover, there are an additional optional parameters that can be specified:

* **Properties**: object for recording additional information connected to the event.
* **Traits**: object for recording additional information connected to the user (e.g. email, username, etc).

The properties and traits parameters take any POJO class.
They get sent in JSON format.

The return value of an authenticate call is a `io.castle.client.model.Verdict` instance.
An instance of `Verdict` contains the following fields:

* **Authenticate action**: Can be one of `ALLOW`, `DENY` or `CHALLENGE`.
The semantics of each action is described in the documentation referred at the beginning of this section.
* **User ID**: a string representing a user id associated to an authentication attempt.
* **Failover boolean**: `true` if the authenticate failover strategy was used, `false` otherwise.
* **Failover reason**: A string with information on the reason why the authenticate failover strategy was used.

### The Authenticate Failover Strategy

It is the strategy that will be used when a request to the `/v1/authenticate` endpoint
of the Castle API fails.
Also, see [`doNotTrack` boolean](the-donottrack-boolean) for another use case of a failover strategy.

It can be one of the following options:
* return a specific *authenticate action* inside an instance of `Verdict`;
* throw an `io.castle.client.model.CastleRuntimeException`.

See [configuration](#configuring-the-sdk) to find out how to enable a failover strategy and to
learn about its default value.



### Asynchronous requests to authenticate

The Castle Java SDK also offers support for making asynchronous POST requests to the authenticate endpoint.
The only thing that is needed is to implement an instance of the `io.castle.client.model.AsyncCallbackHandler`
interface.
Then `io.castle.client.api.CastleApi#authenticateAsync` must be called specifying the same parameters as for its sync counterpart.

The following snippet provides an example of an async call to the authenticate endpoint:
```java
        ...
        AsyncCallbackHandler<Verdict> handler = new AsyncCallbackHandler<Verdict>() {
            @Override
            public void onResponse(Verdict response) {
                // handle success
            }

            @Override
            public void onException(Exception exception) {
                // handle failure
            }
        };
        sdk.onRequest(request).authenticateAsync(event, id, handler);
        ...
```

## Track

For information on the use cases of this method, go to the sections on
[adaptive authentication](https://castle.io/docs/authentication) and
[security events](https://castle.io/docs/events) of the official documentation.

The `io.castle.client.api.CastleApi#track` method makes an asynchronous POST request to the
`/v1/track` endpoint of the Castle API.
This behaviour can be disabled using the [`doNotTrack` boolean](the-donottrack-boolean).

The only required parameter for a call to the track endpoint is **Event**.
This is a string, whose semantics is specified in the documentation featured at the beginning of this section.

Optional parameters are:

* **User ID**: a string representing the id of a user.
* **Properties**: object for recording additional information connected to the event.

Again, the properties parameter takes any POJO class and it gets sent in JSON format.

The signature of this method if `void`.

## Identify

Go to [Tracking user activity with Castle.js](https://castle.io/docs/tracking), the
[Castle.js reference](https://castle.io/docs/castlejs) and
[identify subsection of Integrating through Segment](https://castle.io/docs/segment#identify) for information on identify.

The `io.castle.client.api.CastleApi#identify` method makes an asynchronous POST request to the
`/v1/identify` endpoint of the Castle API.
This behaviour can be disabled using the [`doNotTrack` boolean](the-donottrack-boolean).

The only required parameter is **User ID**, a string representing the id of a user.

Optional parameters are:

* **Traits**: object for recording additional information connected to the user (e.g. email, username, etc).
* **Active**: boolean specifying that the identify call is associated with an active user session.

The traits parameter takes any POJO class and sends it in JSON format.

## Review

Go to the section on [webhooks](https://castle.io/docs/webhooks) of the official documentation in order
to learn how to use the review method. TODO: find out more references to the official docs.

The `io.castle.client.api.CastleApi#review` method makes a GET request to the
`/v1/reviews/{reviewId}` of the Castle API.
The `reviewId` parameter of the method specifies the endpoint of the request.

The signature of the method is `io.castle.client.model.Review`.
An instance of `Review` contains data parsed from the review JSON object sent by Castle in the body of the response.

## The `doNotTrack` Boolean

The `io.castle.client.api.CastleApi` instance obtained from a call to `io.castle.client.Castle#onRequest`
contains a boolean in a private field named `doNotTrack`.
Its default value is `false`, but it can be set to true by using the `doNotTrack` parameter
of the `onRequestMethod`.

The following list specifies the behaviour of each API call when this field is set to `true`:

* **Authenticate**: the method returns a `Veredict` with `authenticateAction` set to `Allow` and `failover` set to
`true`.
* **Track**: the method returns immediately and no request is made.
The track call enables users to specify a custom instance of `io.castle.client.model.AsyncCallbackHandler<Boolean>`.
In such case, the behaviour is to call the handler's `onResponse` method with `true` as value and then return without making any request to the Castle API.
* **Identify**: the method returns immediately and no request is made.
* **Review**: not applicable.

# Documentation

The official Castle [documentation](https://castle.io/docs) documents all possible use cases of the API.
It also contains information on support for other platforms.

This file contains a guide that should get you started as quickly as possible with the Java SDK.
There is also available a Javadoc.
Furthermore, there is a [sample application](https://github.com/castle/castle-java-example)
using Java Servlets and this SDK.

To generate the javadoc documentation run:

    mvn clean compile javadoc:javadoc

The javadoc will be located inside the `target/site` directory.

To check test coverage run:

    mvn clean test jacoco:report

The coverage report will be on the page `target/jacoco-ut/index.html`

# Development branch

Branch for development process. The castle-java-example application have a parallel dev branch for test proposes.

To use on example application dev branch, first install locally:

    mvn clean install


