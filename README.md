# Java SDK for Castle

[![CircleCI](https://circleci.com/gh/castle/castle-java.svg?style=svg)](https://circleci.com/gh/castle/castle-java) [![Maintainability](https://api.codeclimate.com/v1/badges/ef9e24a1fb8ebf7b4218/maintainability)](https://codeclimate.com/github/castle/castle-java/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/ef9e24a1fb8ebf7b4218/test_coverage)](https://codeclimate.com/github/castle/castle-java/test_coverage)

**[Castle](https://castle.io) analyzes device, location, and interaction patterns in your web and mobile apps and lets you stop account takeover attacks in real-time.**

# Quickstart

When using Maven, add the following dependency to your `pom.xml` file:
```xml
        <dependency>
            <groupId>io.castle</groupId>
            <artifactId>castle-java</artifactId>
            <version>1.6.0</version>
        </dependency>
```

## Initialize the SDK

Go to the settings page of your Castle account and find your **API Secret**

**Alt 1. Initialize using ENV variables**

On initialization the Castle SDK will look for the secret in the `CASTLE_API_SECRET` environment variable. If it is set, no options needs to be passed to the initializer.

```java
Castle castle = Castle.initialize();
```

**Alt 2. Initialize using API secret only**

```java
Castle castle = Castle.initialize("abcd");
```


**Alt 3. Initialize using configuration builder**

If you don't use ENV variables, you can set the secret programatically together
with other options by using `CastleConfigurationBuilder`. `Castle.configurationBuilder()`
returns a configuration builder initialized with default settings.

```java
Castle castle = Castle.initialize(
  Castle.configurationBuilder()
    .apiSecret("abcd")
    .enableHttpLogging(true) // Log all outgoing requests sent to Castle
    .build()
);
```
All other settings will be set to their default values.

We can also maintain a global instance wich can be set the following way

```java
Castle.setSingletonInstance(castle);

// Use the singleton
Castle.instance().client().track(...);
```

## Tracking events

Once the SDK has been initialized, tracking requests are sent through the SDK
instance.

**Example**

```java
// Extract the request context, containing eg. the IP and UserAgent of the end-user
// `req` is an instance of `HttpServletRequest`.
Castle castle = Castle.initialize();
CastleContext context = castle.contextBuilder()
    .fromHttpServletRequest(req)
    .build();

castle.client().track(CastleMessage.builder("$login.succeeded")
    .context(context)
    .userId("1234")
    .userTraits(ImmutableMap.builder()
        .put("name", "Winston Smith")
        .put("email", "wsmith@theparty.com")
        .put("timestamp", "1984-01-01T11:22:33Z")
        .build())
    .properties(ImmutableMap.builder()
        .put("quota", "12")
        .build())
    .build()
);
```
By default `REMOTE_ADDR` is used for IP. To use another header or value use the `CastleContextBuilder` method `ip`.

**Example**

```java
Castle castle = Castle.initialize();
CastleContext context = castle.contextBuilder()
    .fromHttpServletRequest(req)
    .ip(req.getHeader("X-Forwarded-For"))
    .build();
```

## Authenticating events

The method signature for the authenticate call is identical to track. The difference
is that a `Verdict` will be returned, indicating which action to take based on the risk.

**Example**

```java
// Extract the request context, containing eg. the IP and UserAgent of the end-user
// `req` is an instance of `HttpServletRequest`.
Castle castle = Castle.initialize();
CastleContext context = castle.contextBuilder()
    .fromHttpServletRequest(req)
    .build();

Verdict verdict = castle.client().authenticate(CastleMessage.builder("$login.succeeded")
    .context(context)
    .userId("1234")
    .build()
);

if (verdict.getAction() == AuthenticateAction.DENY) {
  // IMPLEMENT: Deny user
}
```

Note that the `req` instance should be bound to the underlying request in order to extract the necessary information.
It means that a safe place to create the `CastleApi` instance is the request handling thread. After creation the
`CastleApi` instance can be passed to any thread independently of the original thread life cycle.

## The Context Object

The context object contains information about the request sent by the end-user,
such as IP and UserAgent

```java

// Quick way of building context through the incoming HttpServletRequest
CastleContext context = castle.contextBuilder()
    .fromHttpServletRequest(request)
    .build()

// or build context manually
CastleContext context = castle.contextBuilder()
    .ip("1.1.1.1")
    .userAgent("Mozilla/5.0")
    .headers(CastleHeaders.builder()
        .add("User-Agent", "Mozilla/5.0")
        .add("Accept-Language", "sv-se")
        .build())
    .build();

// Use Castle insteance and track request
Castle castle = Castle.initialize();
castle.client().track(CastleMessage.builder("$login.failed")
    .context(context)
    .userId("1234")
    .build()
);

```

### Default Context

When the context gets constructed from a `HttpServletRequest` the following parts
gets extracted and set automatically:

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
        "user_agent": "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1"
      }
```

### Tracking in an async environment

When tracking data to Castle in an async environment the original request data
first needs to be extracted and serialized so it can be restored later when the
tracking request is sent.

**Example**

```java
Castle castle = Castle.initialize();

// Serialize the incoming request before sending off the data to a worker
String jsonContext = castle.contextBuilder()
    .fromHttpServletRequest(request)
    .toJson();

// Convert json back to a CastleContext
CastleContext context = castle.contextBuilder()
    .fromJson(jsonContext)
    .build()

// Send the tracking request
castle.client().track(CastleMessage.builder("$login.failed")
    .context(context)
    .userId("1234")
    .build()
);

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
there is one that must be configured:

 * **API Secret**: a secret that will be used for authentication purposes.

If the API Secret is not provided, the client's initialization process will fail. It can be found in the settings page of the Castle dashboard.

Besides the aforementioned settings, the following are other application-level setting
that can be optionally configured:

 * **Blacklisted Headers**: a comma-separated list of strings representing HTTP headers that will
 never get passed to the context object. See [The Context Object](#the-context-object).
 * **Whitelisted Headers**: this is a comma-separated list of strings representing HTTP headers
 that will get passed to the context object with each call to the Castle API,
 unless they are blacklisted. If not set or empty all headers will be sent. See [The Context Object](#the-context-object).
 * **Authenticate Failover Strategy**: it can be set to `ALLOW`, `DENY`, `CHALLENGE` or `THROW`.
 See also [Authenticate](#authenticate)
 * **Timeout**: an integer that represents the time in milliseconds after which a request fails.
 * **Backend Provider**: The HTTP layer that will be used to make requests to the Castle API.
 Currently there is only one available and it uses [OkHttp](https://square.github.io/okhttp/).
 * **Base URL**: The base endpoint of the Castle API without any relative path.
 * **IP Headers**: The headers checked (in order) to use for the context IP.

Whitelist and Blacklist are case-insensitive.

If the value of any of these keys is left unspecified, the client will be configured with their default values.
See *[Where to Configure Settings](#where-to-configure-settings)* for a list of the default values.

## Where to Configure Settings

Settings can be provided as a Java Properties file in the classpath, through
environmental variables or through methods calls on `CastleConfigurationBuilder`
When two of these options are used, environmental variables take precedence over the Java
Properties file.

The following table shows the default value for each setting.
It also shows the key that can be used to set its value in a Properties file.
Finally, it also contains the environmental variable that can be used instead of the key in the Java Properties file:

Setting | Default values, when they exist | Properties file key | Environment variable |
--- | --- | --- | --- |
API Secret |   | `api_secret` | `CASTLE_API_SECRET` |
Whitelisted Headers |   | `white_list` | `CASTLE_SDK_WHITELIST_HEADERS` |
Blacklisted Headers | `Cookie` | `black_list` | `CASTLE_SDK_BLACKLIST_HEADERS` |
Timeout | `500` | `timeout` | `CASTLE_SDK_TIMEOUT` |
Authenticate Failover Strategy | `ALLOW` | `failover_strategy` | `CASTLE_SDK_AUTHENTICATE_FAILOVER_STRATEGY` |
Backend Provider | `OKHTTP` | `backend_provide` | `CASTLE_SDK_BACKEND_PROVIDER` |
Base URL | `https://api.castle.io/` | `base_url` | `CASTLE_SDK_BASE_URL` |
Log HTTP | false | `log_http` | `CASTLE_SDK_LOG_HTTP` |
IP Headers |  | `ip_headers` | `CASTLE_SDK_IP_HEADERS` |

By default, the SDK will look in the classpath for the Java Properties file named `castle_sdk.properties`.
An alternative file can be chosen by setting the `CASTLE_PROPERTIES_FILE` environment variable to a different value.

The following is a sample Java Properties file containing all of the settings that can be
modified:

```properties
api_secret=
white_list=User-Agent,Accept-Language,Accept-Encoding,Accept-Charset,Accept,Accept-Datetime,X-Forwarded-For,Forwarded,X-Forwarded,X-Real-IP,REMOTE_ADDR
black_list=Cookie
timeout=500
backend_provider=OKHTTP
failover_strategy=ALLOW
base_url=https://api.castle.io/
log_http=false
ip_headers=
```

To configure using the `CastleConfigurationBuilder` use the corresponding method to set the values

```builder
Castle castle = Castle.initialize(Castle.configurationBuilder()
    .apiSecret("abcd")
    .withWhiteListHeaders("User-Agent", "Accept-Language", "Accept-Encoding")
    .withBlackListHeaders("Cookie")
    .withTimeout(500)
    .withBackendProvider(CastleBackendProvider.OKHTTP)
    .withAuthenticateFailoverStrategy(new AuthenticateFailoverStrategy(AuthenticateAction.ALLOW))
    .withApiBaseUrl("https://api.castle.io/")
    .withLogHttpRequests(true)
    .ipHeaders(Arrays.asList("X-Forwarded-For", "CF-Connecting-IP"))
    .build());
```

## Secure Mode

See the documentation on [secure mode](https://castle.io/docs/securing_requests) in order to learn more.

In order to enable secure mode using Castle.js, use the
`io.castle.client.Castle#secureUserID` method whenever there is a need to make an identify call
from a jsp:

```jsp
            <script type="text/javascript">
                ...
                _castle('secure',
                    '<%= Castle.instance().secureUserID(someUserID) %>');
                ...
            </script>
```

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
        castle.client().authenticateAsync(CastleMessage.builder(event)
            .userId(userId)
            .context(context)
            .build()
        , handler);
        ...
```

## The `doNotTrack` Boolean

The `io.castle.client.api.CastleApi` instance obtained from a call to `io.castle.client.Castle#onRequest`
contains a boolean in a private field named `doNotTrack`.
Its default value is `false`, but it can be set to true by using the `doNotTrack` parameter
of the `onRequest`.

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

# Development branch

Branch for development process. The castle-java-example application have a parallel dev branch for test proposes.

To use on example application dev branch, first install locally:

    mvn clean install
