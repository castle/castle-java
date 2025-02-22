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
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * GroupBy
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2025-01-20T18:58:06.855017776Z[GMT]")

public class GroupBy {
  @SerializedName("fields")
  private List<GroupingField> fields = new ArrayList<GroupingField>();

  @SerializedName("filters")
  private List<BaseQueryFilter> filters = null;

  public GroupBy fields(List<GroupingField> fields) {
    this.fields = fields;
    return this;
  }

  public GroupBy addFieldsItem(GroupingField fieldsItem) {
    this.fields.add(fieldsItem);
    return this;
  }

   /**
   * the field names to group by. Consult the [Events Schema API](/#operation/getEventSchema) for the list of available fields.
   * @return fields
  **/
  @ApiModelProperty(required = true, value = "the field names to group by. Consult the [Events Schema API](/#operation/getEventSchema) for the list of available fields.")
  public List<GroupingField> getFields() {
    return fields;
  }

  public void setFields(List<GroupingField> fields) {
    this.fields = fields;
  }

  public GroupBy filters(List<BaseQueryFilter> filters) {
    this.filters = filters;
    return this;
  }

  public GroupBy addFiltersItem(BaseQueryFilter filtersItem) {
    if (this.filters == null) {
      this.filters = new ArrayList<BaseQueryFilter>();
    }
    this.filters.add(filtersItem);
    return this;
  }

   /**
   * The filters to apply to the grouping level. This let&#x27;s you have separate filters on the grouping level compared to the top-level query. For example, you can query events in the last 24 hours with risk score &gt; 90, group by user id and then compute aggregate columns with a 30-day time-range.
   * @return filters
  **/
  @ApiModelProperty(value = "The filters to apply to the grouping level. This let's you have separate filters on the grouping level compared to the top-level query. For example, you can query events in the last 24 hours with risk score > 90, group by user id and then compute aggregate columns with a 30-day time-range.")
  public List<BaseQueryFilter> getFilters() {
    return filters;
  }

  public void setFilters(List<BaseQueryFilter> filters) {
    this.filters = filters;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupBy groupBy = (GroupBy) o;
    return Objects.equals(this.fields, groupBy.fields) &&
        Objects.equals(this.filters, groupBy.filters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fields, filters);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GroupBy {\n");
    
    sb.append("    fields: ").append(toIndentedString(fields)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
