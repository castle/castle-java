# Java SDK for Castle

[![CircleCI](https://circleci.com/gh/castle/castle-java.svg?style=svg)](https://circleci.com/gh/castle/castle-java) [![Maintainability](https://api.codeclimate.com/v1/badges/ef9e24a1fb8ebf7b4218/maintainability)](https://codeclimate.com/github/castle/castle-java/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/ef9e24a1fb8ebf7b4218/test_coverage)](https://codeclimate.com/github/castle/castle-java/test_coverage)

**[Castle](https://castle.io) analyzes device, location, and interaction patterns in your web and mobile apps and lets you stop account takeover attacks in real-time.**

# Usage
See the [documentation](https://docs.castle.io) for how to use this SDK with the Castle APIs

# Quickstart

When using Maven, add the following dependency to your `pom.xml` file:
```xml
        <dependency>
            <groupId>io.castle</groupId>
            <artifactId>castle-java</artifactId>
            <version>2.0.0</version>
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
Castle.instance().client().filter(...);
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

 * **Denylisted Headers**: a comma-separated list of strings representing HTTP headers that will
 never get passed to the context object. See [The Context Object](#the-context-object).
 * **Allowlisted Headers**: this is a comma-separated list of strings representing HTTP headers
 that will get passed to the context object with each call to the Castle API,
 unless they are denylisted. If not set or empty all headers will be sent. See [The Context Object](#the-context-object).
 * **Authenticate Failover Strategy**: it can be set to `ALLOW`, `DENY`, `CHALLENGE` or `THROW`.
 See also [Authenticate](#authenticate)
 * **Timeout**: an integer that represents the time in milliseconds after which a request fails.
 * **Backend Provider**: The HTTP layer that will be used to make requests to the Castle API.
 Currently there is only one available and it uses [OkHttp](https://square.github.io/okhttp/).
 * **Base URL**: The base endpoint of the Castle API without any relative path.
 * **IP Headers**: The headers checked (in order) to use for the context IP.

Allowlist and Denylist are case-insensitive.

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
Allowlisted Headers |   | `allow_list` | `CASTLE_SDK_ALLOWLIST_HEADERS` |
Denylisted Headers | `Cookie` | `deny_list` | `CASTLE_SDK_DENYLIST_HEADERS` |
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
allow_list=User-Agent,Accept-Language,Accept-Encoding,Accept-Charset,Accept,Accept-Datetime,X-Forwarded-For,Forwarded,X-Forwarded,X-Real-IP,REMOTE_ADDR
deny_list=Cookie
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
    .withAllowListHeaders("User-Agent", "Accept-Language", "Accept-Encoding")
    .withDenyListHeaders("Cookie")
    .withTimeout(500)
    .withBackendProvider(CastleBackendProvider.OKHTTP)
    .withAuthenticateFailoverStrategy(new AuthenticateFailoverStrategy(AuthenticateAction.ALLOW))
    .withApiBaseUrl("https://api.castle.io/")
    .withLogHttpRequests(true)
    .ipHeaders(Arrays.asList("X-Forwarded-For", "CF-Connecting-IP"))
    .build());
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


# Development branch

Branch for development process. The castle-java-example application have a parallel dev branch for test proposes.

To use on example application dev branch, first install locally:

    mvn clean install
