package io.castle.client;

import com.google.common.collect.ImmutableMap;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Test;

public class CastleListsEventsPrivacyHttpTest extends AbstractCastleHttpLayerTest {

    public CastleListsEventsPrivacyHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.ALLOW));
    }

    @Test
    public void createList() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"id\":\"list-1\"}"));

        CastleResponse response = sdk.client().createList(ImmutableMap.builder()
                .put("name", "Trusted devices")
                .put("color", "$green")
                .build());

        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("POST", recordedRequest.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists"), recordedRequest.getRequestUrl());
        Assert.assertTrue(response.isSuccessful());
    }

    @Test
    public void getAllLists() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("[]"));

        sdk.client().getAllLists();

        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("GET", recordedRequest.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists"), recordedRequest.getRequestUrl());
    }

    @Test
    public void getList() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"id\":\"list-1\"}"));

        sdk.client().getList("list-1");

        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("GET", recordedRequest.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/list-1"), recordedRequest.getRequestUrl());
    }

    @Test
    public void queryLists() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("[]"));

        sdk.client().queryLists(ImmutableMap.builder().put("name", "Trusted devices").build());

        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("POST", recordedRequest.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/query"), recordedRequest.getRequestUrl());
    }

    @Test
    public void updateAndDeleteList() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"id\":\"list-1\"}"));
        sdk.client().updateList("list-1", ImmutableMap.builder().put("name", "Renamed").build());
        RecordedRequest update = server.takeRequest();
        Assert.assertEquals("PUT", update.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/list-1"), update.getRequestUrl());

        server.enqueue(new MockResponse().setBody("{}"));
        sdk.client().deleteList("list-1");
        RecordedRequest delete = server.takeRequest();
        Assert.assertEquals("DELETE", delete.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/list-1"), delete.getRequestUrl());
    }

    @Test
    public void listItemsLifecycle() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"id\":\"item-1\"}"));
        sdk.client().createListItem("list-1", ImmutableMap.builder()
                .put("primary_value", "1.2.3.4")
                .build());
        RecordedRequest create = server.takeRequest();
        Assert.assertEquals("POST", create.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/list-1/items"), create.getRequestUrl());

        server.enqueue(new MockResponse().setBody("{}"));
        sdk.client().createListItemsBatch("list-1", ImmutableMap.builder()
                .put("items", new Object[]{})
                .build());
        RecordedRequest batch = server.takeRequest();
        Assert.assertEquals("POST", batch.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/list-1/items/batch"), batch.getRequestUrl());

        server.enqueue(new MockResponse().setBody("{\"id\":\"item-1\"}"));
        sdk.client().getListItem("list-1", "item-1");
        RecordedRequest get = server.takeRequest();
        Assert.assertEquals("GET", get.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/list-1/items/item-1"), get.getRequestUrl());

        server.enqueue(new MockResponse().setBody("[]"));
        sdk.client().queryListItems("list-1", ImmutableMap.builder().put("filters", ImmutableMap.of()).build());
        RecordedRequest query = server.takeRequest();
        Assert.assertEquals("POST", query.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/list-1/items/query"), query.getRequestUrl());

        server.enqueue(new MockResponse().setBody("{\"count\":0}"));
        sdk.client().countListItems("list-1", ImmutableMap.builder().put("filters", ImmutableMap.of()).build());
        RecordedRequest count = server.takeRequest();
        Assert.assertEquals("POST", count.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/list-1/items/count"), count.getRequestUrl());

        server.enqueue(new MockResponse().setBody("{\"id\":\"item-1\"}"));
        sdk.client().updateListItem("list-1", "item-1", ImmutableMap.builder().put("comment", "x").build());
        RecordedRequest update = server.takeRequest();
        Assert.assertEquals("PUT", update.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/list-1/items/item-1"), update.getRequestUrl());

        server.enqueue(new MockResponse().setBody("{}"));
        sdk.client().archiveListItem("list-1", "item-1");
        RecordedRequest archive = server.takeRequest();
        Assert.assertEquals("DELETE", archive.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/list-1/items/item-1/archive"), archive.getRequestUrl());

        server.enqueue(new MockResponse().setBody("{}"));
        sdk.client().unarchiveListItem("list-1", "item-1");
        RecordedRequest unarchive = server.takeRequest();
        Assert.assertEquals("PUT", unarchive.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/list-1/items/item-1/unarchive"), unarchive.getRequestUrl());
    }

    @Test
    public void requestUserData() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("{}"));

        sdk.client().requestUserData(ImmutableMap.builder()
                .put("user_id", "12345")
                .build());

        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("POST", recordedRequest.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/privacy/users"), recordedRequest.getRequestUrl());
    }

    @Test
    public void deleteUserData() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("{}"));

        sdk.client().deleteUserData(ImmutableMap.builder()
                .put("user_id", "12345")
                .build());

        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals("DELETE", recordedRequest.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/privacy/users"), recordedRequest.getRequestUrl());
    }

    @Test
    public void eventsApi() throws InterruptedException {
        server.enqueue(new MockResponse().setBody("{}"));
        sdk.client().eventsSchema();
        RecordedRequest schema = server.takeRequest();
        Assert.assertEquals("GET", schema.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/events/schema"), schema.getRequestUrl());

        server.enqueue(new MockResponse().setBody("[]"));
        sdk.client().queryEvents(ImmutableMap.builder().put("filters", ImmutableMap.of()).build());
        RecordedRequest query = server.takeRequest();
        Assert.assertEquals("POST", query.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/events/query"), query.getRequestUrl());

        server.enqueue(new MockResponse().setBody("[]"));
        sdk.client().groupEvents(ImmutableMap.builder().put("filters", ImmutableMap.of()).build());
        RecordedRequest group = server.takeRequest();
        Assert.assertEquals("POST", group.getMethod());
        Assert.assertEquals(testServerBaseUrl.resolve("v1/events/group"), group.getRequestUrl());
    }
}
