package io.castle.client.model;

public class Review {

    private String userId;
    private ReviewContext context;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ReviewContext getContext() {
        return context;
    }

    public void setContext(ReviewContext context) {
        this.context = context;
    }
}
