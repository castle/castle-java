# userbin-java

Java bindings for the [Castle.io API](https://api.castle.io/)

 - [Installation](#installation)
 - [Resources](#resources)
 - [Authorization](#authorization)
 - [Usage](#usage)
 - [Idioms](#idioms)
 - [Configuration](#configuration)


## Add a dependency

To use the client, you can add a depencency to our maven repo

### maven

Add our repo to your repositories in `pom.xml` or `settings.xml`:

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
 
and add the project declaration to your `pom.xml`:
  
```xml
	<dependency>
        <groupId>io.castle</groupId>
        <artifactId>castle-java</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </dependency>
```

## Resources

Resources this API supports:

- [Monitoring] (#monitoring)
- [Users](#users)
- [Sessions](#sessions)
- [Events](#events)
- [Pairings](#pairings)
- [Challenges](#challenges)
- [Backup codes](#backupcodes)
- [Trusted devices](#trusteddevices)



## Authorization

You can set the app's secret via the `Castle` object -

```java
    Castle.setSecret("<YOUR SECRET CAN BE FOUND IN THE ADMIN CONSOLE>");
```

## Usage

### General
For more indepth documentation read about it here http://api.castle.com

All objects can be created using either
User.create(user) wich will create a user, this should be used only if its a pure backend operation. If however the operation is triggered by a real user
you should call;

```java
UserInfoHeader info = new UserInfoHeader();
info.setIp("192.1.3.12"); //Should be the users REAL ip-address
info.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
User.setUserInfoHeaders(header).create(user); //now the users real ip and useragent will be stored with userbin.

```

Once you have a session you should always pass it along with the request this is done like this;

```java
EventCollection events = Event.setUserInfoHeaders(header).setSession(session).listAllUserEvents(user);
```
To test the API all the endpoints have their own INTTest. Just set the system env USERBIN_TEST with your secret and run the tests in debug to figure out how they work.

All Object collections returns a list of 30 items with the getPage() method and fetched the next page with the fetchNextPage() method.

### Exception handling
All errors returned from the api raise an CastleException.

This exception contains the error details and the response code returned from the api.
For details about the different error codes look here https://api.castle.io/#errors

```java
    try {
        User.find("missingId");
    } catch(CastleException u) {
      int code = u.getResponseCode();
      Error error = u.getError();
    }
```

### Monitoring
// Monitor a session

```java
    Session session = <YOUR SESSION>
    Monitoring.setUserInfoHeader(userHeader).heartBeat(session);
```


### Users

```java
    User user = new User();
    user.setId(testId.toString());
    user.setEmail("p@noonday.se");
    user.setFirstName("Patrick");
    user.setLastName("Gilmore");

    //Create user
    User.setUserInfoHeaders(userHeader).create(user);

    //Update user
    User.update(user);

    //Find user
    User found = User.find(user.getId());

    //List users
    UserCollection users = User.listUsers();
    do {
        List<User> uL : users.getPage();
    } while(users.fetchNextPage());

    //Delete user
    User.setUserInfoHeaders(userHeader).delete(user);



```

### Session

```java
    //Create session, call when a user logges in
    Session session = Session.setUserInfoHeaders(userHeader).create(testUser);

    //Check the token validation
    assertFalse(session.hasExpired());
    assertTrue(session.isValid());

    //Find a session
    Session found = Session.setSession(session).setUserInfoHeaders(userHeaders).find(session.getId(), testUser);

    //Delete the session, call when user logges out
    Session.setUserInfoHeaders(userHeader).delete(testUser, session);

```

### Events

```java
    //Get all events for a user
    Events.setUserInfoHeaders(userHeader).listAllUserEvents(user);

    //Get all events
    EventCollection events = Events.listAllEvents();
    do {
        List<Event> e = events.getPage();
        //Do stuff with events
    } while(events.fetchNextPage());

    //Get all events with query
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    Map<String,String> query = new HashMap<>();
    Calendar from = Calendar.getInstance();
    from.set(Calendar.MONTH, Calendar.SEPTEMBER);
    from.set(Calendar.DAY_OF_MONTH,1);
    Calendar to = Calendar.getInstance();
    query.put("start_at", sdf.format(from.getTime()));
    query.put("end_at", sdf.format(to.getTime()));
    EventCollection events = Event.listAllEvents(query);
    do {
        events.getPage();
        //Do stuff with events
    } while(events.fetchNextPage());
```

### Pairings

```java

    //Create a pairing
    PairingRequest request = PairingRequest.authenticatorParing(true);
    Pairing pairing = Pairing.setUserInfoHeader(userHeader).setSession(session).create(testUser, request);

    //Find pairing
    Pairing found = Pairing.setSession(session).setUserInfoHeader(userHeader).find(user,pairingId);

    //Verify pairing
    System.out.println(pairing.getConfig().get("qr_url"));
    String response = "CHANGE_ME";
    Pairing verified = Pairing.setUserInfoHeader(userHeader).setSession(session).verify(testUser, pairing, response);

    //List verified pairings
    List<Pairing> verifiedPairings = Pairing.setSession(session).setUserInfoHeader(userHeader).listAllPairings(user);

    //Delete a pairing
    Pairing.setSession(session).setUserInfoHeader(userHeader).delete(user, paring)

```

### Challenges

```java

    //Create default challenge
    Challenge challenge = Challenge.setSession(session).setUserInfoHeader(userHeader).create(user);

    //Create challenge for different pairing
    Challenge challenge = Challenge.setSession(session).setUserInfoHeader(userHeader).create(user, pairingId);

    //Find a challenge
    Challenge challenge = Challenge.setSession(session).setUserInfoHeader(userHeader).find(user, challengeId);

    //Verify a challenge
    String response = "e.g google authenticator token";
    Challenge.setSession(session).setUserInfoHeader(userHeader).verify(user, challenge, response);

```

### BackupCodes

Not implemented.

### Trusted device

Not implemented.

## Configuration

### HTTP

The client can be configured to accept any http stack that implements
`java.net.HttpURLConnection` by implementing the `HttpConnectorSupplier`
 interface. 
 
For example, to use [OkHttp](http://square.github.io/okhttp/) as a connection 
supplier, create a supplier class - 
 
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

and hand a supplier to the Castle object -

```
final OkHttpClient client = new OkHttpClient();
final OkUrlFactory factory = new OkUrlFactory(client);
final OkHttpSupplier supplier = new OkHttpSupplier(factory);
Castle.setHttpConnectorSupplier(supplier);
```            

#### Timeouts

The default connection and request timeouts can be set in milliseconds using the 
`Castle.setConnectionTimeout` and `Castle.setRequestTimeout` methods.