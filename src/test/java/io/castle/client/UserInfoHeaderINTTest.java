package io.castle.client;

import org.junit.*;
import static org.mockito.Mockito.*;

import io.castle.client.objects.UserInfoHeader;

import static org.junit.Assert.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class UserInfoHeaderINTTest {

    @Test
    public void itSetsClientIdFromHeader() {
        String uuid = "7e51335b-f4bc-4bc7-875d-b713fb61eb23";
        HttpServletRequest mockedRequest = mock(HttpServletRequest.class);
        when(mockedRequest.getHeader("X-Castle-Client-Id")).thenReturn(uuid);
        UserInfoHeader infoHeader = new UserInfoHeader();
        infoHeader.setClientIdFromRequest(mockedRequest);

        assertEquals(infoHeader.getClientId(), uuid);
    }

    @Test
    public void itSetsClientIdFromCookieId() {
        String uuid = "7e51335b-f4bc-4bc7-875d-b713fb61eb23";
        Cookie cookie = new Cookie("__cid", uuid);
        Cookie[] cookies = new Cookie[] { cookie };
        HttpServletRequest mockedRequest = mock(HttpServletRequest.class);
        when(mockedRequest.getCookies()).thenReturn(cookies);
        UserInfoHeader infoHeader = new UserInfoHeader();
        infoHeader.setClientIdFromRequest(mockedRequest);

        assertEquals(infoHeader.getClientId(), uuid);
    }
}
