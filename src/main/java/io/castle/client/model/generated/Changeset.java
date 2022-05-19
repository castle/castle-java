/*
 * Castle API
 * ## Introduction  **Just getting started? Check out our [quick start guide](https://docs.castle.io/docs/quickstart)**  Castle APIs uses standard HTTP response codes, authentication and verbs. JSON is used as data exchange format, both for parsing incoming request bodies, and in the returned response. This means that the `Content-Type` header should to be set to `application/json` in requests with a body, such as `POST` or `PUT`.  All API requests must be made over [HTTPS](http://en.wikipedia.org/wiki/HTTP_Secure). Non-HTTPS calls will fail and the **TLS version needs to be 1.1 or higher**.  ## Supported types  For a list of supported types, see our [Types Reference](https://docs.castle.io/docs/events).   ## Rate limits  Our Risk, Log (and the legacy Authenticate) APIs have a per-user-id rate limit of 6 requests per second and 10 requests per 5 seconds. 
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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Objects;

/**
 * An object containing information about attributes that changed due to the event. You can send either anonymous attributes (eg. to track password changes) or full attributes (eg. email changes). To simplify your implementation, Castle *automatically* tracks changes to name, email, and phone, however, if you have the &#x60;from&#x60; and &#x60;to&#x60; values at hand, you can also send the changeset yourself, which also allows you to specify changes a user’s password as well as any other custom attributes. Changes to custom attributes won’t be searchable in the dashboard, but they will appear in the event stream.
 */
@ApiModel(description = "An object containing information about attributes that changed due to the event. You can send either anonymous attributes (eg. to track password changes) or full attributes (eg. email changes). To simplify your implementation, Castle *automatically* tracks changes to name, email, and phone, however, if you have the `from` and `to` values at hand, you can also send the changeset yourself, which also allows you to specify changes a user’s password as well as any other custom attributes. Changes to custom attributes won’t be searchable in the dashboard, but they will appear in the event stream.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-05-16T17:44:30.591898+02:00[Europe/Stockholm]")
public class Changeset {
  public static final String SERIALIZED_NAME_PASSWORD = "password";
  @SerializedName(SERIALIZED_NAME_PASSWORD)
  private BaseChangesetEntry password;

  public static final String SERIALIZED_NAME_EMAIL = "email";
  @SerializedName(SERIALIZED_NAME_EMAIL)
  private BaseChangesetEntry email;

  public static final String SERIALIZED_NAME_PHONE = "phone";
  @SerializedName(SERIALIZED_NAME_PHONE)
  private BaseChangesetEntry phone;

  public static final String SERIALIZED_NAME_AUTHENTICATION_METHOD_TYPE = "authentication_method.type";
  @SerializedName(SERIALIZED_NAME_AUTHENTICATION_METHOD_TYPE)
  private BaseChangesetEntry authenticationMethodType;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private BaseChangesetEntry name;


  public Changeset password(BaseChangesetEntry password) {
    
    this.password = password;
    return this;
  }

   /**
   * Get password
   * @return password
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public BaseChangesetEntry getPassword() {
    return password;
  }


  public void setPassword(BaseChangesetEntry password) {
    this.password = password;
  }


  public Changeset email(BaseChangesetEntry email) {
    
    this.email = email;
    return this;
  }

   /**
   * Email address change. Both from and to must be valid emails if provided. You can also inform Castle that the email changed without sending the values explicitly: &#x60;{ \&quot;email\&quot;: { \&quot;changed\&quot;: true } }&#x60;
   * @return email
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Email address change. Both from and to must be valid emails if provided. You can also inform Castle that the email changed without sending the values explicitly: `{ \"email\": { \"changed\": true } }`")

  public BaseChangesetEntry getEmail() {
    return email;
  }


  public void setEmail(BaseChangesetEntry email) {
    this.email = email;
  }


  public Changeset phone(BaseChangesetEntry phone) {
    
    this.phone = phone;
    return this;
  }

   /**
   * Phone number change. Both from and to must be valid phone numbers if provided. You can also inform Castle that the phone changed without sending the values explicitly: &#x60;{ \&quot;phone\&quot;: { \&quot;changed\&quot;: true } }&#x60;
   * @return phone
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Phone number change. Both from and to must be valid phone numbers if provided. You can also inform Castle that the phone changed without sending the values explicitly: `{ \"phone\": { \"changed\": true } }`")

  public BaseChangesetEntry getPhone() {
    return phone;
  }


  public void setPhone(BaseChangesetEntry phone) {
    this.phone = phone;
  }


  public Changeset authenticationMethodType(BaseChangesetEntry authenticationMethodType) {
    
    this.authenticationMethodType = authenticationMethodType;
    return this;
  }

   /**
   * Get authenticationMethodType
   * @return authenticationMethodType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public BaseChangesetEntry getAuthenticationMethodType() {
    return authenticationMethodType;
  }


  public void setAuthenticationMethodType(BaseChangesetEntry authenticationMethodType) {
    this.authenticationMethodType = authenticationMethodType;
  }


  public Changeset name(BaseChangesetEntry name) {
    
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public BaseChangesetEntry getName() {
    return name;
  }


  public void setName(BaseChangesetEntry name) {
    this.name = name;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Changeset changeset = (Changeset) o;
    return Objects.equals(this.password, changeset.password) &&
        Objects.equals(this.email, changeset.email) &&
        Objects.equals(this.phone, changeset.phone) &&
        Objects.equals(this.authenticationMethodType, changeset.authenticationMethodType) &&
        Objects.equals(this.name, changeset.name) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(password, email, phone, authenticationMethodType, name, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Changeset {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    authenticationMethodType: ").append(toIndentedString(authenticationMethodType)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

