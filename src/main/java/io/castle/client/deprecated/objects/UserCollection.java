package io.castle.client.deprecated.objects;

import com.fasterxml.jackson.core.type.TypeReference;
import io.castle.client.deprecated.http.HttpClient;
import io.castle.client.deprecated.http.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class UserCollection extends BaseCollection<User>{

    private UserInfoHeader info;
    private Session session;


    public UserCollection(List<User> users, String path, UserInfoHeader info, Session session) {
	setPath(path);
        setPage(users);
        this.info = info;
        this.session = session;
    }

    @Override
    List<User> getPage(String path, int page, int itemsPerPage) {
        Map<String, String> queryParams = buildPageQuery(page, itemsPerPage);
        URI sessionUri = UriBuilder.newBuilder().path(path, false).query(queryParams).build();
        HttpClient client = new HttpClient(sessionUri, info, session);
        return client.get(new TypeReference<List<User>>() {});
    }

    @Override
    public String toString() {
	return "UserCollection{" +
		"page=" + getPage() +
		'}';
    }
}
