package io.castle.client;

import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleResponse;
import io.castle.client.model.generated.*;
import jakarta.servlet.http.HttpServletRequest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;

public class CastleListItemsTest extends AbstractCastleHttpLayerTest {

    public CastleListItemsTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test
    public void createListItem() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "  \"primary_value\": \"A04t7AcfSA69cBTTusx0RQ\",\n" +
                "  \"secondary_value\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"comment\": \"Fradulent user found through manual inspection\",\n" +
                "  \"author\": {\n" +
                "    \"type\": \"$analyst_email\",\n" +
                "    \"identifier\": \"string\"\n" +
                "  },\n" +
                "  \"auto_archives_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "  \"id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"list_id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"archived\": true,\n" +
                "  \"created_at\": \"2021-09-27T16:46:38.313Z\"\n" +
                "}");
        mockResponse.setResponseCode(201);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        ListItemRequest listItem = createListItemRequest();

        ListItem response = sdk.onRequest(request).createListItem("2ee938c8-24c2-4c26-9d25-19511dd75029", listItem);

        // Check response object
        Assert.assertEquals("A04t7AcfSA69cBTTusx0RQ", response.getPrimaryValue());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getSecondaryValue());
        Assert.assertEquals("Fradulent user found through manual inspection", response.getComment());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getId());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getListId());
        Assert.assertTrue(response.isArchived());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getCreatedAt());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getAutoArchivesAt());
        Assert.assertEquals(AuthorType.ANALYST_EMAIL, response.getAuthor().getType());
        Assert.assertEquals("string", response.getAuthor().getIdentifier());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve(String.format("v1/lists/%s/items", "2ee938c8-24c2-4c26-9d25-19511dd75029")), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    public void createOrUpdateListItems() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "  \"total_received\": 135,\n" +
                "  \"total_processed\": 130,\n" +
                "  \"errored\": 5,\n" +
                "  \"replaced\": 10,\n" +
                "  \"updated\": 20,\n" +
                "  \"created\": 100\n" +
                "}");
        mockResponse.setResponseCode(201);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        ListItemsBatchRequest items = createListItemBulkRequest();

        ListItemsBatchResponse response = sdk.onRequest(request).createOrUpdateListItems("2ee938c8-24c2-4c26-9d25-19511dd75029", items);

        // Check response object
        Assert.assertEquals(135, response.getTotalReceived());
        Assert.assertEquals(130, response.getTotalProcessed());
        Assert.assertEquals(5, response.getErrored());
        Assert.assertEquals(10, response.getReplaced());
        Assert.assertEquals(20, response.getUpdated());
        Assert.assertEquals(100, response.getCreated());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve(String.format("v1/lists/%s/items/batch", "2ee938c8-24c2-4c26-9d25-19511dd75029")), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    public void searchListItems() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"primary_value\": \"A04t7AcfSA69cBTTusx0RQ\",\n" +
                "      \"secondary_value\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "      \"comment\": \"Fradulent user found through manual inspection\",\n" +
                "      \"author\": {\n" +
                "        \"type\": \"$analyst_email\",\n" +
                "        \"identifier\": \"string\"\n" +
                "      },\n" +
                "      \"auto_archives_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "      \"id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "      \"list_id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "      \"archived\": true,\n" +
                "      \"created_at\": \"2021-09-27T16:46:38.313Z\"\n" +
                "    }\n" +
                "  ]\n" +
                "}");
        mockResponse.setResponseCode(200);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        ListItemQuery listItemQuery = new ListItemQuery();
        listItemQuery.page(1);
        listItemQuery.resultsSize(50);

        List<BaseListItemQueryFilter> filters = new ArrayList<>();
        filters.add(new ListItemQueryFilter().field(ListItemQueryFilter.FieldEnum.PRIMARY_VALUE).op(ListItemQueryFilter.OpEnum._EQ).value("Uc80JFKRRvm"));
        filters.add(new ListItemQueryFilter().field(ListItemQueryFilter.FieldEnum.ARCHIVED).op(ListItemQueryFilter.OpEnum._EQ).value(false));
        filters.add(new ListItemOrQueryFilter().op(ListItemOrQueryFilter.OpEnum._OR).value(List.of(new ListItemQueryFilter().field(ListItemQueryFilter.FieldEnum.SECONDARY_VALUE).op(ListItemQueryFilter.OpEnum._EQ).value("1"))));
        listItemQuery.filters(filters);

        ListItemList response = sdk.onRequest(request).searchListItems("2ee938c8-24c2-4c26-9d25-19511dd75029", listItemQuery);

        // Check response object
        Assert.assertEquals(1, response.getData().size());
        ListItem listItem = response.getData().get(0);
        Assert.assertEquals("A04t7AcfSA69cBTTusx0RQ", listItem.getPrimaryValue());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", listItem.getSecondaryValue());
        Assert.assertEquals("Fradulent user found through manual inspection", listItem.getComment());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", listItem.getId());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", listItem.getListId());
        Assert.assertTrue(listItem.isArchived());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), listItem.getCreatedAt());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), listItem.getAutoArchivesAt());
        Assert.assertEquals(AuthorType.ANALYST_EMAIL, listItem.getAuthor().getType());
        Assert.assertEquals("string", listItem.getAuthor().getIdentifier());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve(String.format("v1/lists/%s/items/query", "2ee938c8-24c2-4c26-9d25-19511dd75029")), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    public void countListItems() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "  \"total_count\": 1\n" +
                "}");
        mockResponse.setResponseCode(200);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        ListItemQuery listItemQuery = new ListItemQuery();
        List<BaseListItemQueryFilter> filters = new ArrayList<>();
        filters.add(new ListItemQueryFilter().field(ListItemQueryFilter.FieldEnum.PRIMARY_VALUE).op(ListItemQueryFilter.OpEnum._EQ).value("Uc80JFKRRvm"));
        filters.add(new ListItemQueryFilter().field(ListItemQueryFilter.FieldEnum.ARCHIVED).op(ListItemQueryFilter.OpEnum._EQ).value(false));
        filters.add(new ListItemOrQueryFilter().op(ListItemOrQueryFilter.OpEnum._OR).value(List.of(new ListItemQueryFilter().field(ListItemQueryFilter.FieldEnum.SECONDARY_VALUE).op(ListItemQueryFilter.OpEnum._EQ).value("1"))));
        filters.add(new ListItemOrQueryFilter().op(ListItemOrQueryFilter.OpEnum._OR).value(List.of(new ListItemQueryFilter().field(ListItemQueryFilter.FieldEnum.SECONDARY_VALUE).op(ListItemQueryFilter.OpEnum._EQ).value("2"))));
        listItemQuery.filters(filters);

        ListItemListCount response = sdk.onRequest(request).countListItems("2ee938c8-24c2-4c26-9d25-19511dd75029", listItemQuery);

        // Check response object
        Assert.assertEquals(1, response.getTotalCount());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve(String.format("v1/lists/%s/items/count", "2ee938c8-24c2-4c26-9d25-19511dd75029")), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    public void updateListItem() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "  \"primary_value\": \"A04t7AcfSA69cBTTusx0RQ\",\n" +
                "  \"secondary_value\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"comment\": \"Listed  due to manual investigation\",\n" +
                "  \"author\": {\n" +
                "    \"type\": \"$analyst_email\",\n" +
                "    \"identifier\": \"string\"\n" +
                "  },\n" +
                "  \"auto_archives_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "  \"id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"list_id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"archived\": true,\n" +
                "  \"created_at\": \"2021-09-27T16:46:38.313Z\"\n" +
                "}");
        mockResponse.setResponseCode(201);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        ListItemRequest listItem = new ListItemRequest();
        listItem.comment("Listed  due to manual investigation");

        ListItem response = sdk.onRequest(request).updateListItem("2ee938c8-24c2-4c26-9d25-19511dd75029", "2ee938c8-24c2-4c26-9d25-19511dd75029", listItem);

        // Check response object
        Assert.assertEquals("A04t7AcfSA69cBTTusx0RQ", response.getPrimaryValue());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getSecondaryValue());
        Assert.assertEquals("Listed  due to manual investigation", response.getComment());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getId());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getListId());
        Assert.assertTrue(response.isArchived());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getCreatedAt());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getAutoArchivesAt());
        Assert.assertEquals(AuthorType.ANALYST_EMAIL, response.getAuthor().getType());
        Assert.assertEquals("string", response.getAuthor().getIdentifier());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve(String.format("v1/lists/%s/items/%s", "2ee938c8-24c2-4c26-9d25-19511dd75029", "2ee938c8-24c2-4c26-9d25-19511dd75029")), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    public void getListItem() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "  \"primary_value\": \"A04t7AcfSA69cBTTusx0RQ\",\n" +
                "  \"secondary_value\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"comment\": \"Fradulent user found through manual inspection\",\n" +
                "  \"author\": {\n" +
                "    \"type\": \"$analyst_email\",\n" +
                "    \"identifier\": \"string\"\n" +
                "  },\n" +
                "  \"auto_archives_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "  \"id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"list_id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"archived\": true,\n" +
                "  \"created_at\": \"2021-09-27T16:46:38.313Z\"\n" +
                "}");
        mockResponse.setResponseCode(200);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        ListItem response = sdk.onRequest(request).getListItem("2ee938c8-24c2-4c26-9d25-19511dd75029", "2ee938c8-24c2-4c26-9d25-19511dd75029");

        // Check response object
        Assert.assertEquals("A04t7AcfSA69cBTTusx0RQ", response.getPrimaryValue());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getSecondaryValue());
        Assert.assertEquals("Fradulent user found through manual inspection", response.getComment());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getId());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getListId());
        Assert.assertTrue(response.isArchived());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getCreatedAt());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getAutoArchivesAt());
        Assert.assertEquals(AuthorType.ANALYST_EMAIL, response.getAuthor().getType());
        Assert.assertEquals("string", response.getAuthor().getIdentifier());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve(String.format("v1/lists/%s/items/%s", "2ee938c8-24c2-4c26-9d25-19511dd75029", "2ee938c8-24c2-4c26-9d25-19511dd75029")), recordedRequest.getRequestUrl());
        Assert.assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    public void achiveListItem() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        CastleResponse response = sdk.onRequest(request).archiveListItem("2ee938c8-24c2-4c26-9d25-19511dd75029", "2ee938c8-24c2-4c26-9d25-19511dd75029");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve(String.format("v1/lists/%s/items/%s/archive", "2ee938c8-24c2-4c26-9d25-19511dd75029", "2ee938c8-24c2-4c26-9d25-19511dd75029")), recordedRequest.getRequestUrl());
        Assert.assertEquals("DELETE", recordedRequest.getMethod());
    }

    @Test
    public void unachiveListItem() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        CastleResponse response = sdk.onRequest(request).unarchiveListItem("2ee938c8-24c2-4c26-9d25-19511dd75029", "2ee938c8-24c2-4c26-9d25-19511dd75029");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve(String.format("v1/lists/%s/items/%s/unarchive", "2ee938c8-24c2-4c26-9d25-19511dd75029", "2ee938c8-24c2-4c26-9d25-19511dd75029")), recordedRequest.getRequestUrl());
        Assert.assertEquals("PUT", recordedRequest.getMethod());
    }

    private ListItemsBatchRequest createListItemBulkRequest() {
        ListItemsBatchRequest listItemsBatchRequest = new ListItemsBatchRequest();
        listItemsBatchRequest.setItems(List.of(createListItemRequest()));
        return listItemsBatchRequest;
    }

    private ListItemRequest createListItemRequest() {
        ListItemRequest listItem = new ListItemRequest();
        listItem.primaryValue("A04t7AcfSA69cBTTusx0RQ");
        listItem.secondaryValue("2ee938c8-24c2-4c26-9d25-19511dd75029");
        listItem.comment("Fradulent user found through manual inspection");
        listItem.author(new ListItemAuthor().type(AuthorType.ANALYST_EMAIL).identifier("string"));
        listItem.autoArchivesAt(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"));
        listItem.mode(ListItemRequest.ModeEnum.ERROR);
        return listItem;
    }


}
