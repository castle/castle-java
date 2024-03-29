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
import java.util.ArrayList;
import java.util.List;

/**
 * Context
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-06-05T10:18:20.077062+02:00[Europe/Stockholm]")
public class Context {
  public static final String SERIALIZED_NAME_HEADERS = "headers";
  @SerializedName(SERIALIZED_NAME_HEADERS)
  private List<List<String>> headers = new ArrayList<List<String>>();

  public static final String SERIALIZED_NAME_IP = "ip";
  @SerializedName(SERIALIZED_NAME_IP)
  private String ip;


  public Context headers(List<List<String>> headers) {
    
    this.headers = headers;
    return this;
  }

  public Context addHeadersItem(String key, String value) {
    this.headers.add(Arrays.asList(key, value));
    return this;
  }

   /**
   * The Headers object of the originating request.  For best results, it&#39;s recommended to forward all headers from the originating request. At minimum, the following headers should be forwarded: &#x60;Host&#x60;, &#x60;User-Agent&#x60;, &#x60;Accept&#x60;, &#x60;Accept-Encoding&#x60;, &#x60;Accept-Language&#x60;. 
   * @return headers
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "[[\"User-Agent\",\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15\"],[\"Accept-Encoding\",\"gzip, deflate, br\"],[\"Accept-Language\",\"en-us\"],[\"Accept\",\"text/html,application/xhtml+xml,application/xml;q=0.9,*_/_*;q=0.8\"],[\"Connection\",\"close\"],[\"Host\",\"castle.io\"]]", required = true, value = "The Headers object of the originating request.  For best results, it's recommended to forward all headers from the originating request. At minimum, the following headers should be forwarded: `Host`, `User-Agent`, `Accept`, `Accept-Encoding`, `Accept-Language`. ")

  public List<List<String>> getHeaders() {
    return headers;
  }


  public void setHeaders(List<List<String>> headers) {
    this.headers = headers;
  }


  public Context ip(String ip) {
    
    this.ip = ip;
    return this;
  }

   /**
   * The IPv4 or IPv6 address of the originating request. Must be a valid, public IP. Supports ipv4 and ipv6 
   * @return ip
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "211.96.77.55", required = true, value = "The IPv4 or IPv6 address of the originating request. Must be a valid, public IP. Supports ipv4 and ipv6 ")

  public String getIp() {
    return ip;
  }


  public void setIp(String ip) {
    this.ip = ip;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Context context = (Context) o;
    return Objects.equals(this.headers, context.headers) &&
        Objects.equals(this.ip, context.ip);
  }

  @Override
  public int hashCode() {
    return Objects.hash(headers, ip);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Context {\n");
    sb.append("    headers: ").append(toIndentedString(headers)).append("\n");
    sb.append("    ip: ").append(toIndentedString(ip)).append("\n");
    sb.append("}");
    return sb.toString();
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

