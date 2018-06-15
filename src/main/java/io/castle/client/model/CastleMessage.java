package io.castle.client.model;

import javax.annotation.Nonnull;


public class CastleMessage {
  public String createdAt;
  public String deviceToken;
  @Nonnull public String event;
  public Object properties;
  public String reviewId;
  public String userId;
  public Object userTraits;

  /**
   * Initialize a new request payload message
   * @param  event Name of the event to send
   */
  public CastleMessage(String event) {
    this.event = event;
  }

  public static Builder builder(String event) {
    return new Builder(new CastleMessage(event));
  }

  public static class Builder {
    private CastleMessage payload;

    public Builder(CastleMessage payload) {
      this.payload = payload;
    }

    public CastleMessage build() {
      return payload;
    }

    public Builder createdAt(String createdAt) {
      payload.createdAt = createdAt;
      return self();
    }

    public Builder deviceToken(String deviceToken) {
      payload.deviceToken = deviceToken;
      return self();
    }

    public Builder reviewId(String reviewId) {
      payload.reviewId = reviewId;
      return self();
    }

    public Builder properties(Object properties) {
      if (properties == null) {
        throw new NullPointerException("Null properties");
      }
      payload.properties = properties;
      return self();
    }

    public Builder userId(String userId) {
      payload.userId = userId;
      return self();
    }

    public Builder userTraits(Object userTraits) {
      if (userTraits == null) {
        throw new NullPointerException("Null userTraits");
      }
      payload.userTraits = userTraits;
      return self();
    }

    Builder self() {
      return this;
    }
  }
}
