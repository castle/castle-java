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
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;
import java.util.Objects;
/**
 * ListQueryFilter
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2025-01-20T18:58:06.855017776Z[GMT]")

public class ListQueryFilter extends BaseListQueryFilter{
  /**
   * List field name
   */
  @JsonAdapter(FieldEnum.Adapter.class)
  public enum FieldEnum {
    @SerializedName("primary_field")
    PRIMARY_FIELD("primary_field"),
    @SerializedName("secondary_field")
    SECONDARY_FIELD("secondary_field"),
    @SerializedName("archived")
    ARCHIVED("archived");

    private String value;

    FieldEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static FieldEnum fromValue(String input) {
      for (FieldEnum b : FieldEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<FieldEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final FieldEnum enumeration) throws IOException {
        jsonWriter.value(String.valueOf(enumeration.getValue()));
      }

      @Override
      public FieldEnum read(final JsonReader jsonReader) throws IOException {
        Object value = jsonReader.nextString();
        return FieldEnum.fromValue((String)(value));
      }
    }
  }  @SerializedName("field")
  private FieldEnum field = null;

  /**
   * Gets or Sets op
   */
  @SerializedName("op")
  private Op op = null;

  @SerializedName("value")
  private Object value = null;

  public ListQueryFilter field(FieldEnum field) {
    this.field = field;
    return this;
  }

   /**
   * List field name
   * @return field
  **/
  @ApiModelProperty(example = "primary_field", required = true, value = "List field name")
  public FieldEnum getField() {
    return field;
  }

  public void setField(FieldEnum field) {
    this.field = field;
  }

  public ListQueryFilter op(Op op) {
    this.op = op;
    return this;
  }

   /**
   * Get op
   * @return op
  **/
  @ApiModelProperty(example = "$eq", required = true, value = "")
  public Op getOp() {
    return op;
  }

  public void setOp(Op op) {
    this.op = op;
  }

  public ListQueryFilter value(Object value) {
    this.value = value;
    return this;
  }

   /**
   * Can be string or bool
   * @return value
  **/
  @ApiModelProperty(example = "effb8e3c-084c-4d3e-b7ee-f8741b79c8d2", required = true, value = "Can be string or bool")
  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListQueryFilter listQueryFilter = (ListQueryFilter) o;
    return Objects.equals(this.field, listQueryFilter.field) &&
        Objects.equals(this.op, listQueryFilter.op) &&
        Objects.equals(this.value, listQueryFilter.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(field, op, value);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListQueryFilter {\n");
    
    sb.append("    field: ").append(toIndentedString(field)).append("\n");
    sb.append("    op: ").append(toIndentedString(op)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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
