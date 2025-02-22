/*
 * Castle API
 * ## Introduction  **Just getting started? Check out our [quick start guide](https://docs.castle.io/docs/quickstart)**  Castle APIs uses standard HTTP response codes, authentication and verbs. JSON is used as data exchange format, both for parsing incoming request bodies, and in the returned response. This means that the `Content-Type` header should to be set to `application/json` in requests with a body, such as `POST` or `PUT`.  All API requests must be made over [HTTPS](http://en.wikipedia.org/wiki/HTTP_Secure). Non-HTTPS calls will fail and the **TLS version needs to be 1.1 or higher**.  ## Supported types  For a list of supported types, see our [Types Reference](https://docs.castle.io/docs/events).  ## Rate limits  Our APIs implement rate-limiting based on the number of requests made to them. Each request will return the following headers:  - `X-RateLimit-Limit` - The maximum number of requests you're permitted to make in the current time window. - `X-RateLimit-Remaining` - The number of requests remaining in the current time window. - `X-RateLimit-Reset` - The remaining time in seconds until the current time window resets.  Additionally, Our Risk, Filter (and the legacy Authenticate) APIs have a per-user-id rate limit of 6 requests per second and 10 requests per 5 seconds. 
 *
 * OpenAPI spec version: 1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.castle.client.model.generated;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Gets or Sets Op
 */
@JsonAdapter(Op.Adapter.class)
public enum Op {
  @SerializedName("$eq")
  EQ("$eq"),
  @SerializedName("$neq")
  NEQ("$neq"),
  @SerializedName("$in")
  IN("$in"),
  @SerializedName("$nin")
  NIN("$nin"),
  @SerializedName("$range")
  RANGE("$range"),
  @SerializedName("$exists")
  EXISTS("$exists"),
  @SerializedName("$nexists")
  NEXISTS("$nexists"),
  @SerializedName("$geo")
  GEO("$geo"),
  @SerializedName("$like")
  LIKE("$like"),
  @SerializedName("$nlike")
  NLIKE("$nlike"),
  @SerializedName("$contains")
  CONTAINS("$contains"),
  @SerializedName("$ncontains")
  NCONTAINS("$ncontains"),
  @SerializedName("$starts_with")
  STARTS_WITH("$starts_with"),
  @SerializedName("$nstarts_with")
  NSTARTS_WITH("$nstarts_with"),
  @SerializedName("$ends_with")
  ENDS_WITH("$ends_with"),
  @SerializedName("$nends_with")
  NENDS_WITH("$nends_with"),
  @SerializedName("$matches")
  MATCHES("$matches"),
  @SerializedName("$nmatches")
  NMATCHES("$nmatches"),
  @SerializedName("$ip_range")
  IP_RANGE("$ip_range"),
  @SerializedName("$nip_range")
  NIP_RANGE("$nip_range"),
  @SerializedName("$or")
  OR("$or");

  private String value;

  Op(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static Op fromValue(String input) {
    for (Op b : Op.values()) {
      if (b.value.equals(input)) {
        return b;
      }
    }
    return null;
  }

  public static class Adapter extends TypeAdapter<Op> {
    @Override
    public void write(final JsonWriter jsonWriter, final Op enumeration) throws IOException {
      jsonWriter.value(String.valueOf(enumeration.getValue()));
    }

    @Override
    public Op read(final JsonReader jsonReader) throws IOException {
      Object value = jsonReader.nextString();
      return Op.fromValue((String)(value));
    }
  }
}
