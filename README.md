# Java SDK for Castle

**[Castle](https://castle.io) analyzes user behavior in web and mobile apps to stop fraud before it happens.**

# Requirements

- **Java Version:** This SDK requires **Java 17** or higher, and is built and tested against Java 17, 21 and 25.

# Supported APIs

The SDK exposes the Castle API surface:

| Group | Methods |
| --- | --- |
| Scoring | `risk`, `filter`, `log` |
| Lists | `createList`, `list`, `listAllLists`, `searchLists`, `updateList`, `deleteList` |
| List items | `createListItem`, `createOrUpdateListItems`, `searchListItems`, `countListItems`, `getListItem`, `updateListItem`, `archiveListItem`, `unarchiveListItem` |
| Privacy | `requestUserData`, `deleteUserData` |
| Events | `eventsSchema`, `queryEvents`, `groupEvents` |
| Webhooks | `verifyWebhookSignature` |
| Secure mode | `secureUserID` |
| Generic | `get`, `post`, `put`, `delete` |

# Usage
See the [documentation](https://docs.castle.io) for how to use this SDK with the Castle APIs.

# Quickstart

When using Maven, add the following dependency to your `pom.xml` file:
```xml
        <dependency>
            <groupId>io.castle</groupId>
            <artifactId>castle-java</artifactId>
            <version>3.0.0</version>
        </dependency>
```

## Initialize the SDK

Go to the settings page of your Castle account and find your **API Secret**.

**Alt 1. Initialize using ENV variables**

On initialization the Castle SDK will look for the secret in the `CASTLE_API_SECRET` environment variable. If it is set, no options need to be passed to the initializer.

```java
Castle castle = Castle.initialize();
```

**Alt 2. Initialize using API secret only**

```java
Castle castle = Castle.initialize("abcd");
```

**Alt 3. Initialize using configuration builder**

If you don't use ENV variables, you can set the secret programmatically together
with other options by using `CastleConfigurationBuilder`. `Castle.configurationBuilder()`
returns a configuration builder initialized with default settings.

```java
Castle castle = Castle.initialize(
  Castle.configurationBuilder()
    .apiSecret("abcd")
    .enableHttpLogging(true)
    .withTimeout(java.time.Duration.ofMillis(1000))
    .build()
);
```
All other settings will be set to their default values.

`Castle` implements `AutoCloseable`. Call `close()` when the process shuts down, or use try-with-resources:

```java
try (Castle castle = Castle.initialize("abcd")) {
    castle.client().filter(...);
}
```

A Spring bean can release the HTTP client from a destroy callback:

```java
@PreDestroy
public void shutdown() {
    castle.close();
}
```

A global instance can be registered with `Castle.setSingletonInstance(castle)` and read with `Castle.instance()`.

# Scoring

`risk`, `filter`, and `log` send the payload you pass. Put `context` (IP and headers) on that payload. `onRequest` and `mergeContext` do not attach fields to these requests.

```java
CastleResponse response = castle.client().risk(ImmutableMap.builder()
    .put("type", "$login")
    .put("status", "$succeeded")
    .put("request_token", requestToken)
    .put("user", ImmutableMap.of("id", userId))
    .put("context", ImmutableMap.of(
        "ip", ipAddress,
        "headers", headers
    ))
    .build());
```

Typed payloads work the same way:

```java
Risk payload = new Risk()
    .type(Risk.TypeEnum.LOGIN)
    .status(Risk.StatusEnum.SUCCEEDED)
    .requestToken(requestToken)
    .user(new RiskUser().id(userId))
    .context(new Context()
        .ip(ipAddress)
        .addHeadersItem("User-Agent", userAgent));

FilterAndRiskResponse result = castle.client().risk(payload);
```

`CastleContextBuilder` can extract IP and headers from an `HttpServletRequest` for you to place on the payload:

```java
CastleContext extracted = castle.contextBuilder()
    .fromHttpServletRequest(request)
    .build();
```

Allowlist and denylist settings apply to that extraction. The default denylist is `Cookie` and `Authorization`.

`doNotTrack(true)`, `client(true)`, and `onRequest(request, true)` store a flag on the client. Scoring and other API methods still send the HTTP request.

# Webhooks

Castle signs every webhook with HMAC-SHA256 of the raw request body. Verify the `X-Castle-Signature` header against those bytes:

```java
boolean valid = castle.verifyWebhookSignature(request, rawBody);
```

# Configuring the SDK

## Settings

Before running an application that uses the Castle Java SDK,
there is one that must be configured:

 * **API Secret**: a secret that will be used for authentication purposes.

If the API Secret is not provided, the client's initialization process will fail. It can be found in the settings page of the Castle dashboard.

Besides the aforementioned settings, the following are other application-level settings
that can be optionally configured:

 * **Denylisted Headers**: a comma-separated list of strings representing HTTP headers that will
 never get passed to the context object extracted from an `HttpServletRequest`.
 * **Allowlisted Headers**: this is a comma-separated list of strings representing HTTP headers
 that will get passed to the extracted context object,
 unless they are denylisted. If not set or empty all headers will be sent.
 * **Timeout**: an integer that represents the time in milliseconds applied to connect, read, and write.
 `CastleConfigurationBuilder#withTimeout(Duration)` accepts the same value as a `java.time.Duration`.
 * **Backend Provider**: The HTTP layer that will be used to make requests to the Castle API.
 Currently there is only one available and it uses [OkHttp](https://square.github.io/okhttp/).
 * **Base URL**: The base endpoint of the Castle API without any relative path.
 * **IP Headers**: The headers checked (in order) to use for the context IP.
 * **Log HTTP**: when true, OkHttp logs request and response bodies. The `Authorization` header is redacted.

Allowlist and Denylist are case-insensitive.

If the value of any of these keys is left unspecified, the client will be configured with their default values.
See *[Where to Configure Settings](#where-to-configure-settings)* for a list of the default values.

## Where to Configure Settings

Settings can be provided as a Java Properties file in the classpath, through
environmental variables or through methods calls on `CastleConfigurationBuilder`.
When two of these options are used, environmental variables take precedence over the Java
Properties file.

The following table shows the default value for each setting.
It also shows the key that can be used to set its value in a Properties file.
Finally, it also contains the environmental variable that can be used instead of the key in the Java Properties file:

Setting | Default values, when they exist | Properties file key | Environment variable |
--- | --- |---------------------| --- |
API Secret |   | `api_secret`        | `CASTLE_API_SECRET` |
Allowlisted Headers |   | `allow_list`        | `CASTLE_SDK_ALLOWLIST_HEADERS` |
Denylisted Headers | `Cookie`, `Authorization` | `deny_list`         | `CASTLE_SDK_DENYLIST_HEADERS` |
Timeout | `1000` | `timeout`           | `CASTLE_SDK_TIMEOUT` |
Backend Provider | `OKHTTP` | `backend_provider`  | `CASTLE_SDK_BACKEND_PROVIDER` |
Base URL | `https://api.castle.io/` | `base_url`          | `CASTLE_SDK_BASE_URL` |
Log HTTP | false | `log_http`          | `CASTLE_SDK_LOG_HTTP` |
IP Headers |  | `ip_headers`        | `CASTLE_SDK_IP_HEADERS` |

By default, the SDK will look in the classpath for the Java Properties file named `castle_sdk.properties`.
An alternative file can be chosen by setting the `CASTLE_PROPERTIES_FILE` environment variable to a different value.

The following is a sample Java Properties file containing all of the settings that can be
modified:

```properties
api_secret=
allow_list=User-Agent,Accept-Language,Accept-Encoding,Accept-Charset,Accept,Accept-Datetime,X-Forwarded-For,Forwarded,X-Forwarded,X-Real-IP,REMOTE_ADDR
deny_list=Cookie,Authorization
timeout=1000
backend_provider=OKHTTP
base_url=https://api.castle.io/
log_http=false
ip_headers=
```

To configure using the `CastleConfigurationBuilder` use the corresponding method to set the values

```java
Castle castle = Castle.initialize(Castle.configurationBuilder()
    .apiSecret("abcd")
    .withAllowListHeaders("User-Agent", "Accept-Language", "Accept-Encoding")
    .withDenyListHeaders("Cookie", "Authorization")
    .withTimeout(1000)
    .withBackendProvider(CastleBackendProvider.OKHTTP)
    .withApiBaseUrl("https://api.castle.io/")
    .withLogHttpRequests(true)
    .ipHeaders(Arrays.asList("X-Forwarded-For", "CF-Connecting-IP"))
    .build());
```

# Development branch

Branch for development process. The castle-java-example application have a parallel dev branch for test proposes.

To use on example application dev branch, first install locally:

    mvn clean install
