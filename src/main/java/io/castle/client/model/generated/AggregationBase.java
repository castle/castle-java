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

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Aggregation
 */
@ApiModel(description = "Aggregation")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2025-01-20T18:58:06.855017776Z[GMT]")

public class AggregationBase {
  @SerializedName("name")
  private String name = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("interval")
  private Integer interval = null;

  @SerializedName("group_by")
  private AggregationBaseGroupBy groupBy = null;

  @SerializedName("filters")
  private List<BaseQueryFilter> filters = null;

  @SerializedName("value")
  private AggregationBaseValue value = null;

  public AggregationBase name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Human-readable name of the aggregation
   * @return name
  **/
  @ApiModelProperty(example = "Critical Events per IP in 1h", value = "Human-readable name of the aggregation")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AggregationBase description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Description of the aggregation
   * @return description
  **/
  @ApiModelProperty(example = "Maximum Account TakeOver score seen per IP in the last day.", value = "Description of the aggregation")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AggregationBase interval(Integer interval) {
    this.interval = interval;
    return this;
  }

   /**
   * Time interval in seconds over which aggregation should be observed. Maximum is 180 days.
   * minimum: 0
   * maximum: 15552000
   * @return interval
  **/
  @ApiModelProperty(example = "86400", value = "Time interval in seconds over which aggregation should be observed. Maximum is 180 days.")
  public Integer getInterval() {
    return interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
  }

  public AggregationBase groupBy(AggregationBaseGroupBy groupBy) {
    this.groupBy = groupBy;
    return this;
  }

   /**
   * Get groupBy
   * @return groupBy
  **/
  @ApiModelProperty(value = "")
  public AggregationBaseGroupBy getGroupBy() {
    return groupBy;
  }

  public void setGroupBy(AggregationBaseGroupBy groupBy) {
    this.groupBy = groupBy;
  }

  public AggregationBase filters(List<BaseQueryFilter> filters) {
    this.filters = filters;
    return this;
  }

  public AggregationBase addFiltersItem(BaseQueryFilter filtersItem) {
    if (this.filters == null) {
      this.filters = new ArrayList<BaseQueryFilter>();
    }
    this.filters.add(filtersItem);
    return this;
  }

   /**
   * Event filters to define when aggregation should be triggered.
   * @return filters
  **/
  @ApiModelProperty(value = "Event filters to define when aggregation should be triggered.")
  public List<BaseQueryFilter> getFilters() {
    return filters;
  }

  public void setFilters(List<BaseQueryFilter> filters) {
    this.filters = filters;
  }

  public AggregationBase value(AggregationBaseValue value) {
    this.value = value;
    return this;
  }

   /**
   * Get value
   * @return value
  **/
  @ApiModelProperty(value = "")
  public AggregationBaseValue getValue() {
    return value;
  }

  public void setValue(AggregationBaseValue value) {
    this.value = value;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggregationBase aggregationBase = (AggregationBase) o;
    return Objects.equals(this.name, aggregationBase.name) &&
        Objects.equals(this.description, aggregationBase.description) &&
        Objects.equals(this.interval, aggregationBase.interval) &&
        Objects.equals(this.groupBy, aggregationBase.groupBy) &&
        Objects.equals(this.filters, aggregationBase.filters) &&
        Objects.equals(this.value, aggregationBase.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, interval, groupBy, filters, value);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggregationBase {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    interval: ").append(toIndentedString(interval)).append("\n");
    sb.append("    groupBy: ").append(toIndentedString(groupBy)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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
