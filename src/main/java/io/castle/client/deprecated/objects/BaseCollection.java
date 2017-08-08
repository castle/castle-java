package io.castle.client.deprecated.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseCollection<T> {

    private static final int pageSize = 30;

    private int currentPage = 1;

    private Map<String, String> additionalQueries;

    private List<T> page;
    private String path;

    public boolean fetchNextPage() {
	if(hasMorePages()) {
	    incrementCurrentPage();
	    setPage(getPage(this.getPath(), getCurrentPage(), getPageSize()));
	    return true;
	}
	return false;
    }

    abstract List<T> getPage(String path, int currentPage, int pageSize);

    protected boolean hasMorePages() {
	if(page.size() >= pageSize) {
	    return true;
	}
	return false;
    }

    public List<T> getPage() {
	return this.page;
    }

    protected void setPage(List<T> page) {
	this.page = page;
    }

    protected int getCurrentPage() {
	return currentPage;
    }

    protected void incrementCurrentPage() {
	currentPage++;
    }

    protected void setAdditionalQueries(Map<String, String> query) {
        this.additionalQueries = query;
    }

    protected Map<String, String> getAdditionalQueries() {
        return this.additionalQueries;
    }

    public static int getPageSize() {
	return pageSize;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public String getPath() {
	return this.path;
    }

    static Map<String,String> buildPageQuery(int page, int itemsPerPage) {
	Map<String,String> query = new HashMap<>();
	query.put("page", Integer.toString(page));
	query.put("per_page", Integer.toString(itemsPerPage));
	return query;
    }
}
