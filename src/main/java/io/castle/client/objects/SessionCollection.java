package io.castle.client.objects;

import com.fasterxml.jackson.core.type.TypeReference;
import io.castle.client.http.HttpClient;
import io.castle.client.http.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class SessionCollection extends BaseCollection<Session> {
    private UserInfoHeader info;
    private Session session;

    public SessionCollection(List<Session> sessions, String path, UserInfoHeader info, Session session) {
        setPage(sessions);
        setPath(path);
        this.session = session;
        this.info = info;
    }

    @Override
    List<Session> getPage(String path, int page, int itemsPerPage) {
        Map<String, String> queryParams = buildPageQuery(page, itemsPerPage);
        URI sessionUri = UriBuilder.newBuilder().path(path, false).query(queryParams).build();
        HttpClient client = new HttpClient(sessionUri, info, session);
        return client.get(new TypeReference<List<Session>>() {});
    }

    @Override
    public String toString() {
	return "SessionCollection{" +
		"page=" + this.getPage() +
		'}';
    }
}
