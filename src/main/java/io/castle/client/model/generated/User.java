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

import java.util.HashMap;
import java.util.Objects;
/**
 * User
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2025-01-20T18:58:06.855017776Z[GMT]")

public class User {
  @SerializedName("id")
  private String id = null;

  @SerializedName("email")
  private String email = null;

  @SerializedName("phone")
  private String phone = null;

  @SerializedName("registered_at")
  private OffsetDateTime registeredAt = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("traits")
  private HashMap<String, Object> traits = null;

  @SerializedName("address")
  private Address address = null;

  public User id(String id) {
    this.id = id;
    return this;
  }

   /**
   * A unique user identifier
   * @return id
  **/
  @ApiModelProperty(required = true, value = "A unique user identifier")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public User email(String email) {
    this.email = email;
    return this;
  }

   /**
   * The identified user&#x27;s email address
   * @return email
  **/
  @ApiModelProperty(value = "The identified user's email address")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public User phone(String phone) {
    this.phone = phone;
    return this;
  }

   /**
   * The identified user&#x27;s phone number E.164 formatted
   * @return phone
  **/
  @ApiModelProperty(value = "The identified user's phone number E.164 formatted")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public User registeredAt(OffsetDateTime registeredAt) {
    this.registeredAt = registeredAt;
    return this;
  }

   /**
   * The ISO8601 timestamp of the user&#x27;s account creation
   * @return registeredAt
  **/
  @ApiModelProperty(value = "The ISO8601 timestamp of the user's account creation")
  public OffsetDateTime getRegisteredAt() {
    return registeredAt;
  }

  public void setRegisteredAt(OffsetDateTime registeredAt) {
    this.registeredAt = registeredAt;
  }

  public User name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The full name of the user
   * @return name
  **/
  @ApiModelProperty(value = "The full name of the user")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User traits(HashMap<String, Object> traits) {
    this.traits = traits;
    return this;
  }

   /**
   * Known traits of the identified user
   * @return traits
  **/
  @ApiModelProperty(value = "Known traits of the identified user")
  public HashMap<String, Object> getTraits() {
    return traits;
  }

  public void setTraits(HashMap<String, Object> traits) {
    this.traits = traits;
  }

  public User address(Address address) {
    this.address = address;
    return this;
  }

   /**
   * Get address
   * @return address
  **/
  @ApiModelProperty(value = "")
  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.phone, user.phone) &&
        Objects.equals(this.registeredAt, user.registeredAt) &&
        Objects.equals(this.name, user.name) &&
        Objects.equals(this.traits, user.traits) &&
        Objects.equals(this.address, user.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, phone, registeredAt, name, traits, address);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    registeredAt: ").append(toIndentedString(registeredAt)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    traits: ").append(toIndentedString(traits)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
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
