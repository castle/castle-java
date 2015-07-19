package io.castle.client;

import io.castle.client.mock.MockHttpRequest;
import io.castle.client.objects.User;
import io.castle.client.objects.UserInfoHeader;
import org.junit.After;
import org.junit.Before;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;

import static org.junit.Assert.assertNotNull;

public class GenericINTTest {
    protected User testUser;
    protected UserInfoHeader userHeader;

    @Before
    public void setup() {
        Castle.setApiBaseURI(URI.create("https://api.castle.io/v1"));
        final String castleTest = System.getenv("CASTLE_TEST");
        assertNotNull("CASTLE_TEST environment variable must be set", castleTest);
        Castle.setSecret(castleTest);
        HttpServletRequest request = mockRequest();
        userHeader = UserInfoHeader.fromRequest(request);
//        TODO: this is not needed for event track
//        testUser = new User();
//        testUser.setId(UUID.randomUUID().toString());
//        testUser.setFirstName("Test");
//        testUser.setLastName("Testsson");
//        testUser.setEmail("test@test.com");
//        testUser.setName("Test Testsson");
//        testUser.setUsername("ttestsson");
//        User.setUserInfoHeaders(userHeader).create(testUser);
    }

    private MockHttpRequest mockRequest() {
        return new MockHttpRequest(){

            public final Map<String,String> headers = new HashMap<String,String>(){{
                put("User-Agent","Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko");
            }};

            @Override
            public String getRemoteAddr() {
                return "1.2.3.4";
            }

            @Override
            public Cookie[] getCookies() {
                return new Cookie[]{ new Cookie("__cid","d8e84c31-c787-4789-ac9c-5c67353d4f5e")};
            }

            @Override
            public Enumeration getHeaderNames() {
                return Collections.enumeration(headers.keySet());
            }

            @Override
            public String getHeader(String header) {
                return headers.get(header);
            }
        };
    }

    @After
    public void teardown() {
//        TODO: this is not needed for event track
//        User.setUserInfoHeaders(userHeader).delete(testUser);
    }
}
