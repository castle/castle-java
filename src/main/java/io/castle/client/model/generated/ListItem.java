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
import org.threeten.bp.OffsetDateTime;

import java.util.Objects;
/**
 * ListItem
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2025-01-20T18:58:06.855017776Z[GMT]")

public class ListItem extends BaseItem {
  @SerializedName("id")
  private String id = null;

  @SerializedName("list_id")
  private String listId = null;

  @SerializedName("archived")
  private Boolean archived = null;

  @SerializedName("created_at")
  private OffsetDateTime createdAt = null;

  public ListItem id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(example = "2ee938c8-24c2-4c26-9d25-19511dd75029", required = true, value = "")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ListItem listId(String listId) {
    this.listId = listId;
    return this;
  }

   /**
   * ID of the List to which this List Item belongs to
   * @return listId
  **/
  @ApiModelProperty(example = "2ee938c8-24c2-4c26-9d25-19511dd75029", required = true, value = "ID of the List to which this List Item belongs to")
  public String getListId() {
    return listId;
  }

  public void setListId(String listId) {
    this.listId = listId;
  }

  public ListItem archived(Boolean archived) {
    this.archived = archived;
    return this;
  }

   /**
   * Get archived
   * @return archived
  **/
  @ApiModelProperty(value = "")
  public Boolean isArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public ListItem createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

   /**
   * Get createdAt
   * @return createdAt
  **/
  @ApiModelProperty(example = "2021-09-27T16:46:38.313Z", value = "")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListItem listItem = (ListItem) o;
    return Objects.equals(this.id, listItem.id) &&
        Objects.equals(this.listId, listItem.listId) &&
        Objects.equals(this.archived, listItem.archived) &&
        Objects.equals(this.createdAt, listItem.createdAt) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, listId, archived, createdAt, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListItem {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    listId: ").append(toIndentedString(listId)).append("\n");
    sb.append("    archived: ").append(toIndentedString(archived)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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
