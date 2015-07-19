package io.castle.client;

import io.castle.client.objects.Monitoring;
import io.castle.client.objects.Session;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MonitorINTTest extends GenericINTTest{

    @Test
    public void testMonitor() {
	Session session = Session.setUserInfoHeaders(userHeader).create(testUser);
	Monitoring.setUserInfoHeader(userHeader).heartBeat(session);
    }
}
