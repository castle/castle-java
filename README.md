# Java SDK for Castle

**[Castle](https://castle.io) adds real-time monitoring of your authentication stack, instantly notifying you and your users on potential account hijacks.**

## Installation

Add the Maven repository to your repositories in `pom.xml` or `settings.xml`:

```xml
<repository>
  <id>youcruit-cloudbees-snapshot</id>
  <name>youcruit-cloudbees-snapshot</name>
  <url>http://repository-youcruit.forge.cloudbees.com/snapshot/</url>
  <releases>
    <enabled>false</enabled>
  </releases>
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
</repository>
```  
 
Add the project declaration to your `pom.xml`:
  
```xml
<dependency>
  <groupId>io.castle</groupId>
  <artifactId>castle-java</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

Load and configure the library with your Castle API secret:

```java
Castle.setSecret("YOUR_API_SECRET");
```

## Tracking security events

`trackEvent` lets you record the security-related actions your users perform. The more actions you track, the more accurate Castle is in identifying fraudsters.

When you have access to a **logged in user**, set `setUserId` to the same user identifier as when you initiated Castle.js.

```java
Event event = new Event();
event.setName(Event.EventName.LOGIN_SUCCEEDED);
event.setUserId("1234");

UserInfoHeader userInfoHeader = UserInfoHeader.fromRequest(req);
Event.setUserInfoHeader(userInfoHeader).trackEvent(event);
```

When you **don't** have access to a logged in user just omit `setUserId`, typically when tracking `LOGIN_FAILED` and `PASSWORD_RESET_REQUESTED`.

### Supported events

- `LOGIN_SUCCEEDED`: Record when a user attempts to log in.
- `LOGIN_FAILED`: Record when a user logs out.
- `LOGOUT_SUCCEEDED`:  Record when a user logs out.
- `REGISTRATION_SUCCEEDED`: Capture account creation, both when a user signs up as well as when created manually by an administrator.
- `REGISTRATION_FAILED`: Record when an account failed to be created.
- `CHALLENGE_REQUESTED`: Record when a user is prompted with additional verification, such as two-factor authentication or a captcha.
- `CHALLENGE_SUCCEEDED`: Record when additional verification was successful.
- `CHALLENGE_FAILED`: Record when additional verification failed.
- `PASSWORD_RESET_REQUESTED`: An attempt was made to reset a user’s password.
- `PASSWORD_RESET_SUCCEEDED`: The user completed all of the steps in the password reset process and the password was successfully reset. Password resets **do not** required knowledge of the current password.
- `PASSWORD_RESET_FAILED`: Use to record when a user failed to reset their password.
- `PASSWORD_CHANGE_SUCCEEDED`: Use to record when a user changed their password. This event is only logged when users change their **own** password.
- `PASSWORD_CHANGE_FAILED`:  Use to record when a user failed to change their password.

## Exceptions

`CastleException` will be thrown if the Castle API returns a 400 or a 500 level HTTP response.

```java
try {
    Event.setUserInfoHeader(InfoHeader).trackEvent(incorrectEvent);
} catch(CastleException u) {
    int code = u.getResponseCode();
    Error error = u.getError();
}
```

The exception contains error details and the response code returned from the API. For details about the different error codes look here https://api.castle.io/#errors

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

And hand a supplier to the Castle object:

```
final OkHttpClient client = new OkHttpClient();
final OkUrlFactory factory = new OkUrlFactory(client);
final OkHttpSupplier supplier = new OkHttpSupplier(factory);
Castle.setHttpConnectorSupplier(supplier);
```            

### Timeouts

The default connection and request timeouts can be set in milliseconds using the 
`Castle.setConnectionTimeout` and `Castle.setRequestTimeout` methods.
