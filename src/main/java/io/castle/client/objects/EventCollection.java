package io.castle.client.objects;

import com.fasterxml.jackson.core.type.TypeReference;
import io.castle.client.http.HttpClient;
import io.castle.client.http.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class EventCollection extends BaseCollection<Event> {

    private UserInfoHeader info;
    private Session session;

    public EventCollection(List<Event> events, String path, UserInfoHeader info, Session session) {
        setPage(events);
        setPath(path);
        this.info = info;
        this.session = session;
    }

    public EventCollection(List<Event> events, String path, Map<String,String> queryParams, UserInfoHeader info, Session session) {
        setPage(events);
        setPath(path);
        setAdditionalQueries(queryParams);
        this.info = info;
        this.session = session;
    }

    List<Event> getPage(String path, int page, int itemsPerPage) {
        Map<String, String> queryParams = buildPageQuery(page, itemsPerPage);
        if(getAdditionalQueries()!=null) {
            queryParams.putAll(getAdditionalQueries());
        }
        URI sessionUri = UriBuilder.newBuilder().path(path, false).query(queryParams).build();
        HttpClient client = new HttpClient(sessionUri, info, session);
        return client.get(new TypeReference<List<Event>>() {});
    }

    @Override
    public String toString() {
        return "UserCollection{" +
                "page=" + getPage() +
                '}';
    }
}
