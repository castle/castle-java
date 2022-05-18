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

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModelProperty;
import org.threeten.bp.OffsetDateTime;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Risk
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-05-16T17:44:30.591898+02:00[Europe/Stockholm]")
public class Risk {
  public static final String SERIALIZED_NAME_CONTEXT = "context";
  @SerializedName(SERIALIZED_NAME_CONTEXT)
  private Context context;

  public static final String SERIALIZED_NAME_PROPERTIES = "properties";
  @SerializedName(SERIALIZED_NAME_PROPERTIES)
  private Map<String, Map<String, Object>> properties = null;

  public static final String SERIALIZED_NAME_PRODUCT = "product";
  @SerializedName(SERIALIZED_NAME_PRODUCT)
  private Product product;

  public static final String SERIALIZED_NAME_CREATED_AT = "created_at";
  @SerializedName(SERIALIZED_NAME_CREATED_AT)
  private OffsetDateTime createdAt;

  public static final String SERIALIZED_NAME_REQUEST_TOKEN = "request_token";
  @SerializedName(SERIALIZED_NAME_REQUEST_TOKEN)
  private String requestToken;

  public static final String SERIALIZED_NAME_USER = "user";
  @SerializedName(SERIALIZED_NAME_USER)
  private User user;

  /**
   * Gets or Sets type
   */
  @JsonAdapter(TypeEnum.Adapter.class)
  public enum TypeEnum {
    CHALLENGE("$challenge"),
    REGISTRATION("$registration"),
    LOGIN("$login"),
    PROFILE_UPDATE("$profile_update"),
    PROFILE_RESET("$profile_reset"),
    TRANSACTION("$transaction"),
    LOGOUT("$logout"),
    CUSTOM("$custom");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<TypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public TypeEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return TypeEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_TYPE = "type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  private TypeEnum type = TypeEnum.CUSTOM;

  /**
   * Gets or Sets status
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    ATTEMPTED("$attempted"),

    SUCCEEDED("$succeeded"),

    FAILED("$failed"),

    REQUESTED("$requested");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<StatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StatusEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public StatusEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return StatusEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_STATUS = "status";
  @SerializedName(SERIALIZED_NAME_STATUS)
  private StatusEnum status = StatusEnum.SUCCEEDED;

  public static final String SERIALIZED_NAME_AUTHENTICATION_METHOD = "authentication_method";
  @SerializedName(SERIALIZED_NAME_AUTHENTICATION_METHOD)
  private AuthenticationMethod authenticationMethod;

  public static final String SERIALIZED_NAME_CHANGESET = "changeset";
  @SerializedName(SERIALIZED_NAME_CHANGESET)
  private Changeset changeset;

  public static final String SERIALIZED_NAME_TRANSACTION = "transaction";
  @SerializedName(SERIALIZED_NAME_TRANSACTION)
  private Transaction transaction;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;


  public Risk context(Context context) {
    
    this.context = context;
    return this;
  }

   /**
   * Get context
   * @return context
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")

  public Context getContext() {
    return context;
  }


  public void setContext(Context context) {
    this.context = context;
  }


  public Risk properties(Map<String, Map<String, Object>> properties) {
    
    this.properties = properties;
    return this;
  }

  public Risk putPropertiesItem(String key, Map<String, Object> propertiesItem) {
    if (this.properties == null) {
      this.properties = new HashMap<String, Map<String, Object>>();
    }
    this.properties.put(key, propertiesItem);
    return this;
  }

   /**
   * User defined properties associated with this device
   * @return properties
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "User defined properties associated with this device")

  public Map<String, Map<String, Object>> getProperties() {
    return properties;
  }


  public void setProperties(Map<String, Map<String, Object>> properties) {
    this.properties = properties;
  }


  public Risk product(Product product) {
    
    this.product = product;
    return this;
  }

   /**
   * Get product
   * @return product
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Product getProduct() {
    return product;
  }


  public void setProduct(Product product) {
    this.product = product;
  }


  public Risk createdAt(OffsetDateTime createdAt) {
    
    this.createdAt = createdAt;
    return this;
  }

   /**
   * The default value is the time of which the event was received by Castle. If you’re passing events in a delayed fashion, such as via an event queue, you should make sure to set this field to the time of which the event was tracked and put into the queue.
   * @return createdAt
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2021-09-27T16:46:38.313Z", value = "The default value is the time of which the event was received by Castle. If you’re passing events in a delayed fashion, such as via an event queue, you should make sure to set this field to the time of which the event was tracked and put into the queue.")

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }


  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }


  public Risk requestToken(String requestToken) {
    
    this.requestToken = requestToken;
    return this;
  }

   /**
   * Token generated from a client. Check out our [quick start guide](/v1/getting-started) to generate a &#x60;request_token&#x60; 
   * @return requestToken
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "test_lZWva9rsNe3u0_EIc6R8V3t5beV38piPAQbhgREGygYCAo2FRSv1tAQ4-cb6ArKHOWK_zG18hO1uZ8K0LDbNqU9njuhscoLyaj3NyGxyiO0iS4ziIkm-oVom3LEsN9i6InSbuzo-w7ErJqrkYW2CrjA23LEyN92wIkCE82dggvktPtWvMmrl42Bj2uM7Zdn2AQGXC6qGTIECRlwaAgZcgcAGeX4", required = true, value = "Token generated from a client. Check out our [quick start guide](/v1/getting-started) to generate a `request_token` ")

  public String getRequestToken() {
    return requestToken;
  }


  public void setRequestToken(String requestToken) {
    this.requestToken = requestToken;
  }


  public Risk user(User user) {
    
    this.user = user;
    return this;
  }

   /**
   * Get user
   * @return user
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")

  public User getUser() {
    return user;
  }


  public void setUser(User user) {
    this.user = user;
  }


  public Risk type(TypeEnum type) {
    
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")

  public TypeEnum getType() {
    return type;
  }


  public void setType(TypeEnum type) {
    this.type = type;
  }


  public Risk status(StatusEnum status) {
    
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")

  public StatusEnum getStatus() {
    return status;
  }


  public void setStatus(StatusEnum status) {
    this.status = status;
  }


  public Risk authenticationMethod(AuthenticationMethod authenticationMethod) {
    
    this.authenticationMethod = authenticationMethod;
    return this;
  }

   /**
   * Get authenticationMethod
   * @return authenticationMethod
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public AuthenticationMethod getAuthenticationMethod() {
    return authenticationMethod;
  }


  public void setAuthenticationMethod(AuthenticationMethod authenticationMethod) {
    this.authenticationMethod = authenticationMethod;
  }


  public Risk changeset(Changeset changeset) {
    
    this.changeset = changeset;
    return this;
  }

   /**
   * Get changeset
   * @return changeset
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Changeset getChangeset() {
    return changeset;
  }


  public void setChangeset(Changeset changeset) {
    this.changeset = changeset;
  }


  public Risk transaction(Transaction transaction) {
    
    this.transaction = transaction;
    return this;
  }

   /**
   * Get transaction
   * @return transaction
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Transaction getTransaction() {
    return transaction;
  }


  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }


  public Risk name(String name) {
    
    this.name = name;
    return this;
  }

   /**
   * Your custom name of the event.
   * @return name
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Your custom name of the event.")

  public String getName() {
    return name;
  }


  public void setName(String name) {
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
    Risk risk = (Risk) o;
    return Objects.equals(this.context, risk.context) &&
        Objects.equals(this.properties, risk.properties) &&
        Objects.equals(this.product, risk.product) &&
        Objects.equals(this.createdAt, risk.createdAt) &&
        Objects.equals(this.requestToken, risk.requestToken) &&
        Objects.equals(this.user, risk.user) &&
        Objects.equals(this.type, risk.type) &&
        Objects.equals(this.status, risk.status) &&
        Objects.equals(this.authenticationMethod, risk.authenticationMethod) &&
        Objects.equals(this.changeset, risk.changeset) &&
        Objects.equals(this.transaction, risk.transaction) &&
        Objects.equals(this.name, risk.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(context, properties, product, createdAt, requestToken, user, type, status, authenticationMethod, changeset, transaction, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Risk {\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("    product: ").append(toIndentedString(product)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    requestToken: ").append(toIndentedString(requestToken)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    authenticationMethod: ").append(toIndentedString(authenticationMethod)).append("\n");
    sb.append("    changeset: ").append(toIndentedString(changeset)).append("\n");
    sb.append("    transaction: ").append(toIndentedString(transaction)).append("\n");
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

