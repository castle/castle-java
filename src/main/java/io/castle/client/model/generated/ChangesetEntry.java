/*
 * Castle API
 * ## Introduction  **Just getting started? Check out our [quick start guide](https://docs.castle.io/docs/quickstart)**  Castle APIs uses standard HTTP response codes, authentication and verbs. JSON is used as data exchange format, both for parsing incoming request bodies, and in the returned response. This means that the `Content-Type` header should to be set to `application/json` in requests with a body, such as `POST` or `PUT`.  All API requests must be made over [HTTPS](http://en.wikipedia.org/wiki/HTTP_Secure). Non-HTTPS calls will fail and the **TLS version needs to be 1.1 or higher**.  ## Supported types  For a list of supported types, see our [Types Reference](https://docs.castle.io/docs/events).  ## Rate limits  Our APIs implement rate-limiting based on the number of requests made to them. Each request will return the following headers:  - `X-RateLimit-Limit` - The maximum number of requests you're permitted to make in the current time window. - `X-RateLimit-Remaining` - The number of requests remaining in the current time window. - `X-RateLimit-Reset` - The remaining time in seconds until the current time window resets.  Additionally, Our Risk, Log (and the legacy Authenticate) APIs have a per-user-id rate limit of 6 requests per second and 10 requests per 5 seconds. 
 *
 * The version of the OpenAPI document: 1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package io.castle.client.model.generated;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * Information that the attribute changed along with the changed values. Examples: &#x60;{ \&quot;email\&quot;: { \&quot;from\&quot;: \&quot;a@example.com\&quot;, \&quot;to\&quot;: \&quot;b@example.com\&quot; }&#x60;, &#x60;{ \&quot;authentication_method.type\&quot;: { \&quot;from\&quot;: null, \&quot;to\&quot;: \&quot;$push\&quot; }&#x60;
 */
@ApiModel(description = "Information that the attribute changed along with the changed values. Examples: `{ \"email\": { \"from\": \"a@example.com\", \"to\": \"b@example.com\" }`, `{ \"authentication_method.type\": { \"from\": null, \"to\": \"$push\" }`")
public class ChangesetEntry extends BaseChangesetEntry {
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-06-05T10:18:20.077062+02:00[Europe/Stockholm]")
  public static final String SERIALIZED_NAME_FROM = "from";
  @SerializedName(SERIALIZED_NAME_FROM)
  private String from;

  public static final String SERIALIZED_NAME_TO = "to";
  @SerializedName(SERIALIZED_NAME_TO)
  private String to;


  public ChangesetEntry from(String from) {
    
    this.from = from;
    return this;
  }

   /**
   * Attribute value before the event
   * @return from
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(required = true, value = "Attribute value before the event")

  public String getFrom() {
    return from;
  }


  public void setFrom(String from) {
    this.from = from;
  }


  public ChangesetEntry to(String to) {
    
    this.to = to;
    return this;
  }

   /**
   * Attribute value after the event
   * @return to
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(required = true, value = "Attribute value after the event")

  public String getTo() {
    return to;
  }


  public void setTo(String to) {
    this.to = to;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChangesetEntry changesetEntry = (ChangesetEntry) o;
    return Objects.equals(this.from, changesetEntry.from) &&
        Objects.equals(this.to, changesetEntry.to);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to);
  }

  @Override
  public String toString() {
      String sb = "class ChangesetEntry {\n" +
              "    from: " + toIndentedString(from) + "\n" +
              "    to: " + toIndentedString(to) + "\n" +
              "}";
    return sb;
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

