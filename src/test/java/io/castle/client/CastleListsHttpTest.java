package io.castle.client;

import com.google.gson.JsonParser;
import io.castle.client.internal.json.CastleGsonModel;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
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

public class CastleListsHttpTest extends AbstractCastleHttpLayerTest {

    public CastleListsHttpTest() {
        super(new AuthenticateFailoverStrategy(AuthenticateAction.CHALLENGE));
    }

    @Test
    public void updateList() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "  \"name\": \"Malicious IPs\",\n" +
                "  \"description\": \"We block these IPs from withdrawing funds. Please be careful.\",\n" +
                "  \"color\": \"$red\",\n" +
                "  \"default_item_archivation_time\": 2592000,\n" +
                "  \"id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"primary_field\": \"device.fingerprint\",\n" +
                "  \"secondary_field\": \"ip.value\",\n" +
                "  \"archived_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "  \"created_at\": \"2021-09-27T16:46:38.313Z\"\n" +
                "}");
        mockResponse.setResponseCode(201);
        server.enqueue(mockResponse);

        // Add a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        ListRequest list = createListRequest();

        ListResponse response = sdk.onRequest(request).updateList("2ee938c8-24c2-4c26-9d25-19511dd75029", list);

        // Check response object
        Assert.assertEquals("Malicious IPs", response.getName());
        Assert.assertEquals("We block these IPs from withdrawing funds. Please be careful.", response.getDescription());
        Assert.assertEquals(ListColor.RED, response.getColor());
        Assert.assertEquals(2592000, response.getDefaultItemArchivationTime());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getId());
        Assert.assertEquals("device.fingerprint", response.getPrimaryField());
        Assert.assertEquals("ip.value", response.getSecondaryField());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getArchivedAt());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getCreatedAt());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve(String.format("v1/lists/%s", "2ee938c8-24c2-4c26-9d25-19511dd75029")), recordedRequest.getRequestUrl());
        Assert.assertEquals("PUT", recordedRequest.getMethod());
    }

    @Test
    public void listAllLists() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("[\n" +
                "  {\n" +
                "    \"name\": \"Malicious IPs\",\n" +
                "    \"description\": \"We block these IPs from withdrawing funds. Please be careful.\",\n" +
                "    \"color\": \"$red\",\n" +
                "    \"default_item_archivation_time\": 2592000,\n" +
                "    \"id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "    \"primary_field\": \"device.fingerprint\",\n" +
                "    \"secondary_field\": \"ip.value\",\n" +
                "    \"archived_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "    \"created_at\": \"2021-09-27T16:46:38.313Z\"\n" +
                "  }\n" +
                "]");
        mockResponse.setResponseCode(200);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        List<ListResponse> response = sdk.onRequest(request).listAllLists();

        // Check response object
        Assert.assertEquals(1, response.size());
        ListResponse list = response.get(0);
        Assert.assertEquals("Malicious IPs", list.getName());
        Assert.assertEquals("We block these IPs from withdrawing funds. Please be careful.", list.getDescription());
        Assert.assertEquals(ListColor.RED, list.getColor());
        Assert.assertEquals(2592000, list.getDefaultItemArchivationTime());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", list.getId());
        Assert.assertEquals("device.fingerprint", list.getPrimaryField());
        Assert.assertEquals("ip.value", list.getSecondaryField());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), list.getArchivedAt());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), list.getCreatedAt());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists"), recordedRequest.getRequestUrl());
        Assert.assertEquals("GET", recordedRequest.getMethod());
    }

    @Test public void getList() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "  \"name\": \"Malicious IPs\",\n" +
                "  \"description\": \"We block these IPs from withdrawing funds. Please be careful.\",\n" +
                "  \"color\": \"$red\",\n" +
                "  \"default_item_archivation_time\": 2592000,\n" +
                "  \"id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"primary_field\": \"device.fingerprint\",\n" +
                "  \"secondary_field\": \"ip.value\",\n" +
                "  \"archived_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "  \"created_at\": \"2021-09-27T16:46:38.313Z\"\n" +
                "}");
        mockResponse.setResponseCode(200);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        ListRequest list = createListRequest();

        ListResponse response = sdk.onRequest(request).list("2ee938c8-24c2-4c26-9d25-19511dd75029");

        // Check response object
        Assert.assertEquals("Malicious IPs", response.getName());
        Assert.assertEquals("We block these IPs from withdrawing funds. Please be careful.", response.getDescription());
        Assert.assertEquals(ListColor.RED, response.getColor());
        Assert.assertEquals(2592000, response.getDefaultItemArchivationTime());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getId());
        Assert.assertEquals("device.fingerprint", response.getPrimaryField());
        Assert.assertEquals("ip.value", response.getSecondaryField());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getArchivedAt());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getCreatedAt());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve(String.format("v1/lists/%s", "2ee938c8-24c2-4c26-9d25-19511dd75029")), recordedRequest.getRequestUrl());
        Assert.assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    public void deleteList() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(204);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        sdk.onRequest(request).deleteList("2ee938c8-24c2-4c26-9d25-19511dd75029");

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve(String.format("v1/lists/%s", "2ee938c8-24c2-4c26-9d25-19511dd75029")), recordedRequest.getRequestUrl());
        Assert.assertEquals("DELETE", recordedRequest.getMethod());
    }

    @Test
    public void searchList() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("[\n" +
                "  {\n" +
                "    \"name\": \"Malicious IPs\",\n" +
                "    \"description\": \"We block these IPs from withdrawing funds. Please be careful.\",\n" +
                "    \"color\": \"$red\",\n" +
                "    \"default_item_archivation_time\": 2592000,\n" +
                "    \"id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "    \"primary_field\": \"device.fingerprint\",\n" +
                "    \"secondary_field\": \"ip.value\",\n" +
                "    \"archived_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "    \"created_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "    \"size_label\": \"9852\"\n" +
                "  }\n" +
                "]");
        mockResponse.setResponseCode(200);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        ListQuery query = createListQuery();

        List<ListResponse> response = sdk.onRequest(request).searchLists(query);

        // Check response object
        Assert.assertEquals(1, response.size());
        ListResponse list = response.get(0);
        Assert.assertEquals("Malicious IPs", list.getName());
        Assert.assertEquals("We block these IPs from withdrawing funds. Please be careful.", list.getDescription());
        Assert.assertEquals(ListColor.RED, list.getColor());
        Assert.assertEquals(2592000, list.getDefaultItemArchivationTime());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", list.getId());
        Assert.assertEquals("device.fingerprint", list.getPrimaryField());
        Assert.assertEquals("ip.value", list.getSecondaryField());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), list.getArchivedAt());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), list.getCreatedAt());
        Assert.assertEquals("9852", list.getSizeLabel());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists/query"), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    public void createList() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\n" +
                "  \"name\": \"Malicious IPs\",\n" +
                "  \"description\": \"We block these IPs from withdrawing funds. Please be careful.\",\n" +
                "  \"color\": \"$red\",\n" +
                "  \"default_item_archivation_time\": 2592000,\n" +
                "  \"id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"primary_field\": \"device.fingerprint\",\n" +
                "  \"secondary_field\": \"ip.value\",\n" +
                "  \"archived_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "  \"created_at\": \"2021-09-27T16:46:38.313Z\"\n" +
                "}");
        mockResponse.setResponseCode(201);
        server.enqueue(mockResponse);

        // And a mock Request
        HttpServletRequest request = new MockHttpServletRequest();

        ListRequest list = createListRequest();

        ListResponse response = sdk.onRequest(request).createList(list);

        // Check response object
        Assert.assertEquals("Malicious IPs", response.getName());
        Assert.assertEquals("We block these IPs from withdrawing funds. Please be careful.", response.getDescription());
        Assert.assertEquals(ListColor.RED, response.getColor());
        Assert.assertEquals(2592000, response.getDefaultItemArchivationTime());
        Assert.assertEquals("2ee938c8-24c2-4c26-9d25-19511dd75029", response.getId());
        Assert.assertEquals("device.fingerprint", response.getPrimaryField());
        Assert.assertEquals("ip.value", response.getSecondaryField());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getArchivedAt());
        Assert.assertEquals(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"), response.getCreatedAt());

        // Then
        RecordedRequest recordedRequest = server.takeRequest();
        Assert.assertEquals(testServerBaseUrl.resolve("v1/lists"), recordedRequest.getRequestUrl());
        Assert.assertEquals("POST", recordedRequest.getMethod());
    }

    private ListQuery createListQuery() {
        ListQuery query = new ListQuery();
        query.page(2);
        query.resultsSize(50);
        query.includeSizeLabel(true);

        ListQuerySort sort = new ListQuerySort();
        sort.field(ListQuerySort.FieldEnum.CREATED_AT);
        sort.order(ListQuerySort.OrderEnum.ASC);
        query.sort(sort);

        List<BaseListQueryFilter> filters = new ArrayList<>();

        ListQueryFilter filter = new ListQueryFilter();
        filter.field(ListQueryFilter.FieldEnum.PRIMARY_FIELD);
        filter.op(Op.EQ);
        filter.value("device.fingerprint");
        filters.add(filter);

        filter = new ListQueryFilter();
        filter.field(ListQueryFilter.FieldEnum.ARCHIVED);
        filter.op(Op.EQ);
        filter.value(false);
        filters.add(filter);

        ListQueryOrFilter orFilter = new ListQueryOrFilter();
        orFilter.op(Op.OR);
        orFilter.value(List.of(new ListQueryFilter().field(ListQueryFilter.FieldEnum.SECONDARY_FIELD).op(Op.EQ).value("user.id")));
        filters.add(orFilter);

        orFilter = new ListQueryOrFilter();
        orFilter.op(Op.OR);
        orFilter.value(List.of(new ListQueryFilter().field(ListQueryFilter.FieldEnum.SECONDARY_FIELD).op(Op.EQ).value("user.email")));
        filters.add(orFilter);

        query.filters(filters);

        query.sort(new ListQuerySort().field(ListQuerySort.FieldEnum.CREATED_AT).order(ListQuerySort.OrderEnum.ASC));

        return query;
    }

    @Test
    public void compareListQueryJson() {
        ListQuery listQuery = createListQuery();

        // Convert list query object to JSON
        CastleGsonModel gson = new CastleGsonModel();
        String listJson = gson.getGson().toJson(listQuery);

        // Provided JSON
        String providedJson = "{\n" +
                "  \"filters\": [\n" +
                "    {\n" +
                "      \"field\": \"primary_field\",\n" +
                "      \"op\": \"$eq\",\n" +
                "      \"value\": \"device.fingerprint\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"field\": \"archived\",\n" +
                "      \"op\": \"$eq\",\n" +
                "      \"value\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"op\": \"$or\",\n" +
                "      \"value\": [\n" +
                "        {\n" +
                "          \"field\": \"secondary_field\",\n" +
                "          \"op\": \"$eq\",\n" +
                "          \"value\": \"user.id\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"op\": \"$or\",\n" +
                "      \"value\": [\n" +
                "        {\n" +
                "          \"field\": \"secondary_field\",\n" +
                "          \"op\": \"$eq\",\n" +
                "          \"value\": \"user.email\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"page\": 2,\n" +
                "  \"results_size\": 50,\n" +
                "  \"include_size_label\": true,\n" +
                "  \"sort\": {\n" +
                "    \"field\": \"created_at\",\n" +
                "    \"order\": \"asc\"\n" +
                "  }\n" +
                "}";

        // Compare the JSON strings
        Assert.assertEquals(JsonParser.parseString(providedJson), JsonParser.parseString(listJson));
    }

    private ListRequest createListRequest() {
        ListRequest list = new ListRequest();
        list.setName("Malicious IPs");
        list.setDescription("We block these IPs from withdrawing funds. Please be careful.");
        list.setColor(ListColor.RED);
        list.setDefaultItemArchivationTime(2592000);
        list.primaryField("device.fingerprint");
        list.secondaryField("ip.value");
        return list;
    }

    @Test
    public void compareListRequestJson() {
        ListRequest list = createListRequest();

        // Convert list request object to JSON
        CastleGsonModel gson = new CastleGsonModel();
        String listJson = gson.getGson().toJson(list);

        // Provided JSON
        String providedJson = "{\n" +
                "  \"name\": \"Malicious IPs\",\n" +
                "  \"description\": \"We block these IPs from withdrawing funds. Please be careful.\",\n" +
                "  \"color\": \"$red\",\n" +
                "  \"default_item_archivation_time\": 2592000,\n" +
                "  \"primary_field\": \"device.fingerprint\",\n" +
                "  \"secondary_field\": \"ip.value\"\n" +
                "}";

        // Compare the JSON strings
        Assert.assertEquals(JsonParser.parseString(providedJson), JsonParser.parseString(listJson));
    }

    private ListRequest createListResponse() {
        ListResponse list = new ListResponse();
        list.setName("Malicious IPs");
        list.setDescription("We block these IPs from withdrawing funds. Please be careful.");
        list.setColor(ListColor.RED);
        list.setDefaultItemArchivationTime(2592000);
        list.primaryField("device.fingerprint");
        list.secondaryField("ip.value");
        list.setId("2ee938c8-24c2-4c26-9d25-19511dd75029");
        list.setArchivedAt(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"));
        list.setCreatedAt(OffsetDateTime.parse("2021-09-27T16:46:38.313Z"));

        return list;
    }

    @Test
    public void compareListResponseJson() {
        ListRequest list = createListResponse();

        // Convert list response object to JSON
        CastleGsonModel gson = new CastleGsonModel();
        String listJson = gson.getGson().toJson(list);

        // Provided JSON
        String providedJson = "{\n" +
                "  \"name\": \"Malicious IPs\",\n" +
                "  \"description\": \"We block these IPs from withdrawing funds. Please be careful.\",\n" +
                "  \"color\": \"$red\",\n" +
                "  \"default_item_archivation_time\": 2592000,\n" +
                "  \"id\": \"2ee938c8-24c2-4c26-9d25-19511dd75029\",\n" +
                "  \"primary_field\": \"device.fingerprint\",\n" +
                "  \"secondary_field\": \"ip.value\",\n" +
                "  \"archived_at\": \"2021-09-27T16:46:38.313Z\",\n" +
                "  \"created_at\": \"2021-09-27T16:46:38.313Z\"\n" +
                "}";

        // Compare the JSON strings
        Assert.assertEquals(JsonParser.parseString(providedJson), JsonParser.parseString(listJson));
    }
}
