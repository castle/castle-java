/*
 * Castle API
 * ## Introduction  **Just getting started? Check out our [quick start guide](https://docs.castle.io/docs/quickstart)**  Castle APIs uses standard HTTP response codes, authentication and verbs. JSON is used as data exchange format, both for parsing incoming request bodies, and in the returned response. This means that the `Content-Type` header should to be set to `application/json` in requests with a body, such as `POST` or `PUT`.  All API requests must be made over [HTTPS](http://en.wikipedia.org/wiki/HTTP_Secure). Non-HTTPS calls will fail and the **TLS version needs to be 1.1 or higher**.  ## Supported types  For a list of supported types, see our [Types Reference](https://docs.castle.io/docs/events).  ## Rate limits  Our APIs implement rate-limiting based on the number of requests made to them. Each request will return the following headers:  - `X-RateLimit-Limit` - The maximum number of requests you're permitted to make in the current time window. - `X-RateLimit-Remaining` - The number of requests remaining in the current time window. - `X-RateLimit-Reset` - The remaining time in seconds until the current time window resets.  Additionally, Our Risk, Filter (and the legacy Authenticate) APIs have a per-user-id rate limit of 6 requests per second and 10 requests per 5 seconds. 
 *
 * The version of the OpenAPI document: 1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package io.castle.client.model.generated;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * FilterAndRiskResponseScoresAccountAbuse
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-05T19:53:01.452316+01:00[Europe/Stockholm]")
public class FilterAndRiskResponseScoresAccountAbuse {
  public static final String SERIALIZED_NAME_SCORE = "score";
  @SerializedName(SERIALIZED_NAME_SCORE)
  private double score;


   /**
   * Calculated Account Abuse Risk Score.
   * minimum: 0
   * maximum: 1
   * @return score
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "0.65", required = true, value = "Calculated Account Abuse Risk Score.")

  public double getScore() {
    return score;
  }




  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilterAndRiskResponseScoresAccountAbuse filterAndRiskResponseScoresAccountAbuse = (FilterAndRiskResponseScoresAccountAbuse) o;
    return Objects.equals(this.score, filterAndRiskResponseScoresAccountAbuse.score);
  }

  @Override
  public int hashCode() {
    return Objects.hash(score);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterAndRiskResponseScoresAccountAbuse {\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
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

