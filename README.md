# Java SDK for Castle

**[Castle](https://castle.io) adds real-time monitoring of your authentication stack,
instantly notifying you and your users on potential account hijacks.**

To generate the javadoc documentation run:

    mvn clean compile javadoc:javadoc

To check test coverage run:
    
    mvn clean test jacoco:report

Coverage report will be on the page "target/jacoco-ut/index.html"

# Quickstart

TODO

TODO: Create internal references to other sections in the README.md

# Where to Find Documentation

This file contains a guide that should get you started as quickly as possible.
There is also available a Javadoc.
TODO: point to the right URL with the javadoc

There is also an [sample application](https://github.com/castle/castle-java-example)
using Java Servlets and this SDK.

# Configuring the SDK

## Settings

Before running an application that uses the Castle.io Java SDK,
there are two settings that must be configured.
These are:

 * AppID: an identifier for the application associated with a valid Castle.io account; 
 * APISecret: an identifier that will be used for authentication purposes.
 
Both of them can be found in the settings page of a Castle.io account.
If they are are not provided, the client's initialization process will fail.

Besides the aforementioned settings, the following are other application-level setting
that can be optionally configured:

 * Blacklisted Headers: a comma-separated list of strings representing HTTP headers that will
 never get passed to the context object.
 * Whitelisted Headers: this is a comma-separated list of strings representing HTTP headers
 that will get passed to the context object with each call to the Castle.io API,
 unless they are blacklisted.
 * Authenticate Failover Strategy: it can be set to `ALLOW`, `DENY`, `CHALLENGE` or `THROW`.
 This sets the action that will be taken when a synchronous request to the authenticate endpoint
 of the Castle.io API fails.
 * Timeout: an integer that represents the time in milliseconds after which a request fails. 
 * Backend Provider: The HTTP layer that will be used to make requests to the Castle.io API.
 Currently there is only one available and it uses [OkHttp](https://square.github.io/okhttp/).
 * Base URL: The base endpoint of the Castle.io API without any relative path.
 
If the value of any of this is left unspecified, the client will be configured with their default values.

## Where to Configure settings

Settings can be provided as a Java Properties file in the classpath or through
environmental variables.
When both of these options are used, environmental variables take precedence over Java
properties.
When neither option is used, the client's initialization will provide a default value or fail,
in case there is no such value.

The following table shows the default value for each setting,
as well as the key that can be used to set its value in a properties
file or the environmental variable that can be used instead:

Setting | Default values, when they exist | Properties file key | Environmental variable |
--- | --- | --- | --- |
AppID |   | `app_id` | `CASTLE_SDK_APP_ID` |
APISecret |   | `api_secret` | `CASTLE_SDK_API_SECRET` |
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

The Castle.io Java SDK must be initialized once in the life cycle of an application using it.
There is a static method in the **io.castle.client.Castle** class to do so.
Before trying to call any API method, the `Castle.verifySdkConfigurationAndInitialize()`
should be called, preferably during the initialization process of the application using the SDK,
so the most basic errors could be discovered as early as possible.

# API calls

The `io.castle.client.api.CastleApi` interface has all methods needed to perform calls to
the Castle.io API.
Once the SDK has been initialized as described above, it is a matter of calling the
`io.castle.client.Castle#onRequest` method in the context of a Java Servlet in order
to get an instance. of `CastleApi`.

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
    }
}
```

Notice that the `io.castle.client.Castle#sdk` method is called first in order to get the singleton
instance of the `io.castle.client.Castle` class.
With this instance it is possible to use its non-static methods.

## The Context Object

Read more about the [request context](https://castle.io/docs/context) in the official documentation.

An `io.castle.client.api.CastleApi` instance obtained from a call to `io.castle.client.Castle#onRequest`
contains has a context object in JSON format in a private filed.
Its data consists of metadata taken from the `HttpServletRequest` passed to `onRequest`.
In particular, TODO: specify exactly what is passed.

It is possible to provide additional metadata to the context using the fluent api on `io.castle.client.api.CastleApi` as follows:
```java
        Castle.sdk().onRequest(req).mergeContext(additionalContextObject);
```

Any POJO class can be used as an argument of the mergeContext class. To get a complete override of the default castle, use a instance of `io.castle.client.model.CastleContext`.

TODO merge process definition and possibility to merge multiple context objects.

## Authenticate

Additional information on when to use this method can be found in the section
on [adaptive authentication](https://castle.io/docs/authentication) of the official documentation.

 

## Track

For in formation on the use cases of this method, go to the sections on
[adaptive authentication](https://castle.io/docs/authentication) and 
[security events](https://castle.io/docs/events) of the official documentation.

## Identify

## Review

Go to the section on [webhooks](https://castle.io/docs/webhooks) of the official documentation in order
to lean how to use the review method.

