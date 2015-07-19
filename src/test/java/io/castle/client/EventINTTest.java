package io.castle.client;

import io.castle.client.objects.Event;
import io.castle.client.objects.EventCollection;
import io.castle.client.objects.Session;
import org.junit.Ignore;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EventINTTest extends GenericINTTest {


    @Test
    public void trackUser(){
        Event event = new Event();
        event.setUserId("1234");
        event.setName(Event.EventName.LOGIN_SUCCEEDED);
        Event returnedEvent = Event.setUserInfoHeader(userHeader).trackEvent(event);
        assertNotNull(returnedEvent.getId());
        assertNotNull(returnedEvent.getCreatedAt());
    }


    @Test
    @Ignore
    public void getUserEvents() {
        Session.setUserInfoHeaders(userHeader).create(testUser);
        EventCollection events = Event.setUserInfoHeader(userHeader).listAllUserEvents(testUser);
        assertNotNull(events);
        assertEquals(1, events.getPage().size());
        assertEquals(Event.EventName.SESSION_CREATED, events.getPage().get(0).getName());
    }

    @Test
    @Ignore
    public void getAllEvents() {
        EventCollection events = Event.setUserInfoHeader(userHeader).listAllEvents();
        assertNotNull(events);
        int noPages = 1;
        while(events.fetchNextPage()) {
            noPages++;
        }
        assertTrue(noPages > 1);
    }

    @Test
    @Ignore
    public void getAllEventsForQuery() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Map<String,String> query = new HashMap<>();
        Calendar from = Calendar.getInstance();
        from.set(Calendar.MONTH, Calendar.SEPTEMBER);
        from.set(Calendar.DAY_OF_MONTH,1);
        Calendar to = Calendar.getInstance();
        query.put("start_at", sdf.format(from.getTime()));
        query.put("end_at", sdf.format(to.getTime()));
        EventCollection events = Event.setUserInfoHeader(userHeader).listAllEvents(query);
        int noPages = 1;
        while(events.fetchNextPage()) {
            noPages++;
        }
        assertTrue(noPages > 1);
    }

}
