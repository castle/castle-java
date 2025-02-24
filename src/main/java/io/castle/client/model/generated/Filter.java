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

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModelProperty;
import org.threeten.bp.OffsetDateTime;

import java.io.IOException;
import java.util.*;

/**
 * Filter
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-05T19:53:01.452316+01:00[Europe/Stockholm]")
public class Filter {
  public static final String SERIALIZED_NAME_CONTEXT = "context";
  @SerializedName(SERIALIZED_NAME_CONTEXT)
  private Context context;

  public static final String SERIALIZED_NAME_PROPERTIES = "properties";
  @SerializedName(SERIALIZED_NAME_PROPERTIES)
  private Map<String, Map<String, Object>> properties = null;

  public static final String SERIALIZED_NAME_PRODUCT = "product";
  @SerializedName(SERIALIZED_NAME_PRODUCT)
  private Product product;

  public static final String SERIALIZED_NAME_SESSION = "session";
  @SerializedName(SERIALIZED_NAME_SESSION)
  private Session session;

  public static final String SERIALIZED_NAME_CREATED_AT = "created_at";
  @SerializedName(SERIALIZED_NAME_CREATED_AT)
  private OffsetDateTime createdAt;

  public static final String SERIALIZED_NAME_REQUEST_TOKEN = "request_token";
  @SerializedName(SERIALIZED_NAME_REQUEST_TOKEN)
  private String requestToken;

  public static final String SERIALIZED_NAME_USER = "user";
  @SerializedName(SERIALIZED_NAME_USER)
  private User user;

  public static final String SERIALIZED_NAME_PARAMS = "params";
  @SerializedName(SERIALIZED_NAME_PARAMS)
  private FilterRequestParams params;

  public static final String SERIALIZED_NAME_MATCHING_USER_ID = "matching_user_id";
  @SerializedName(SERIALIZED_NAME_MATCHING_USER_ID)
  private String matchingUserId;

  public static final String SERIALIZED_NAME_SKIP_REQUEST_TOKEN_VALIDATION = "skip_request_token_validation";
  @SerializedName(SERIALIZED_NAME_SKIP_REQUEST_TOKEN_VALIDATION)
  private Boolean skipRequestTokenValidation = false;

  public static final String SERIALIZED_NAME_SKIP_CONTEXT_VALIDATION = "skip_context_validation";
  @SerializedName(SERIALIZED_NAME_SKIP_CONTEXT_VALIDATION)
  private Boolean skipContextValidation = false;

  public static final String SERIALIZED_NAME_EXPAND = "expand";
  @SerializedName(SERIALIZED_NAME_EXPAND)
  private List<String> expand = null;

  /**
   * Castle supported events available for this endpoint
   */
  @JsonAdapter(TypeEnum.Adapter.class)
  public enum TypeEnum {

    REGISTRATION("$registration"),
    LOGIN("$login"),
    PASSWORD_RESET_REQUEST("$password_reset_request"),
    CUSTOM("$custom"),
    CHALLENGE("$challenge"),
    TRANSACTION("$transaction");

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
  private TypeEnum type = TypeEnum.CHALLENGE;

  /**
   * Gets or Sets status
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    ATTEMPTED("$attempted"),
    
    SUCCEEDED("$succeeded"),
    
    FAILED("$failed");

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
  private StatusEnum status = StatusEnum.ATTEMPTED;

  public static final String SERIALIZED_NAME_AUTHENTICATION_METHOD = "authentication_method";
  @SerializedName(SERIALIZED_NAME_AUTHENTICATION_METHOD)
  private AuthenticationMethod authenticationMethod;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_TRANSACTION = "transaction";
  @SerializedName(SERIALIZED_NAME_TRANSACTION)
  private Transaction transaction;


  public Filter context(Context context) {
    
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


  public Filter properties(Map<String, Map<String, Object>> properties) {
    
    this.properties = properties;
    return this;
  }

  public Filter putPropertiesItem(String key, Map<String, Object> propertiesItem) {
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


  public Filter product(Product product) {
    
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


  public Filter session(Session session) {
    
    this.session = session;
    return this;
  }

   /**
   * Get session
   * @return session
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Session getSession() {
    return session;
  }


  public void setSession(Session session) {
    this.session = session;
  }


  public Filter createdAt(OffsetDateTime createdAt) {
    
    this.createdAt = createdAt;
    return this;
  }

   /**
   * The default value is the time of which the event was received by Castle. If you’re passing events in a delayed fashion, such as via an event queue, you should make sure to set this field to the time of which the event was tracked and put into the queue.
   * @return createdAt
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The default value is the time of which the event was received by Castle. If you’re passing events in a delayed fashion, such as via an event queue, you should make sure to set this field to the time of which the event was tracked and put into the queue.")

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }


  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }


  public Filter requestToken(String requestToken) {
    
    this.requestToken = requestToken;
    return this;
  }

   /**
   * Token generated from a client. Check out our [quick start guide](https://docs.castle.io/docs/quickstart) to generate a &#x60;request_token&#x60; 
   * @return requestToken
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "test_lZWva9rsNe3u0_EIc6R8V3t5beV38piPAQbhgREGygYCAo2FRSv1tAQ4-cb6ArKHOWK_zG18hO1uZ8K0LDbNqU9njuhscoLyaj3NyGxyiO0iS4ziIkm-oVom3LEsN9i6InSbuzo-w7ErJqrkYW2CrjA23LEyN92wIkCE82dggvktPtWvMmrl42Bj2uM7Zdn2AQGXC6qGTIECRlwaAgZcgcAGeX4", required = true, value = "Token generated from a client. Check out our [quick start guide](https://docs.castle.io/docs/quickstart) to generate a `request_token` ")

  public String getRequestToken() {
    return requestToken;
  }


  public void setRequestToken(String requestToken) {
    this.requestToken = requestToken;
  }


  public Filter user(User user) {
    
    this.user = user;
    return this;
  }

   /**
   * Get user
   * @return user
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public User getUser() {
    return user;
  }


  public void setUser(User user) {
    this.user = user;
  }


  public Filter params(FilterRequestParams params) {
    
    this.params = params;
    return this;
  }

   /**
   * Get params
   * @return params
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public FilterRequestParams getParams() {
    return params;
  }


  public void setParams(FilterRequestParams params) {
    this.params = params;
  }


  public Filter matchingUserId(String matchingUserId) {
    
    this.matchingUserId = matchingUserId;
    return this;
  }

   /**
   * User id related connected with the request
   * @return matchingUserId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "123", value = "User id related connected with the request")

  public String getMatchingUserId() {
    return matchingUserId;
  }


  public void setMatchingUserId(String matchingUserId) {
    this.matchingUserId = matchingUserId;
  }


  public Filter skipRequestTokenValidation(Boolean skipRequestTokenValidation) {
    
    this.skipRequestTokenValidation = skipRequestTokenValidation;
    return this;
  }

   /**
   * Skip &#x60;request_token&#x60; validation. Enable when it&#39;s impossible to obtain Castle request_token (e.g., third-party API calls). When enabled, Castle risk scores are not calculated. 
   * @return skipRequestTokenValidation
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Skip `request_token` validation. Enable when it's impossible to obtain Castle request_token (e.g., third-party API calls). When enabled, Castle risk scores are not calculated. ")

  public Boolean getSkipRequestTokenValidation() {
    return skipRequestTokenValidation;
  }


  public void setSkipRequestTokenValidation(Boolean skipRequestTokenValidation) {
    this.skipRequestTokenValidation = skipRequestTokenValidation;
  }


  public Filter skipContextValidation(Boolean skipContextValidation) {
    
    this.skipContextValidation = skipContextValidation;
    return this;
  }

   /**
   * Skip &#x60;context&#x60; validation. Enable when HTTP request context is not providing useful insight (e.g., third-party API calls from Data Center IP). 
   * @return skipContextValidation
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Skip `context` validation. Enable when HTTP request context is not providing useful insight (e.g., third-party API calls from Data Center IP). ")

  public Boolean getSkipContextValidation() {
    return skipContextValidation;
  }


  public void setSkipContextValidation(Boolean skipContextValidation) {
    this.skipContextValidation = skipContextValidation;
  }


  public Filter expand(List<String> expand) {
    
    this.expand = expand;
    return this;
  }

  public Filter addExpandItem(String expandItem) {
    if (this.expand == null) {
      this.expand = new ArrayList<String>();
    }
    this.expand.add(expandItem);
    return this;
  }

   /**
   * Include additional properties into API response. *This option is currently in beta and available only to select customers.* 
   * @return expand
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "[\"all\"]", value = "Include additional properties into API response. *This option is currently in beta and available only to select customers.* ")

  public List<String> getExpand() {
    return expand;
  }


  public void setExpand(List<String> expand) {
    this.expand = expand;
  }


  public Filter type(TypeEnum type) {
    
    this.type = type;
    return this;
  }

   /**
   * Castle supported events available for this endpoint
   * @return type
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "Castle supported events available for this endpoint")

  public TypeEnum getType() {
    return type;
  }


  public void setType(TypeEnum type) {
    this.type = type;
  }


  public Filter status(StatusEnum status) {
    
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public StatusEnum getStatus() {
    return status;
  }


  public void setStatus(StatusEnum status) {
    this.status = status;
  }


  public Filter authenticationMethod(AuthenticationMethod authenticationMethod) {
    
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


  public Filter name(String name) {
    
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


  public Filter transaction(Transaction transaction) {
    
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Filter filter = (Filter) o;
    return Objects.equals(this.context, filter.context) &&
        Objects.equals(this.properties, filter.properties) &&
        Objects.equals(this.product, filter.product) &&
        Objects.equals(this.session, filter.session) &&
        Objects.equals(this.createdAt, filter.createdAt) &&
        Objects.equals(this.requestToken, filter.requestToken) &&
        Objects.equals(this.user, filter.user) &&
        Objects.equals(this.params, filter.params) &&
        Objects.equals(this.matchingUserId, filter.matchingUserId) &&
        Objects.equals(this.skipRequestTokenValidation, filter.skipRequestTokenValidation) &&
        Objects.equals(this.skipContextValidation, filter.skipContextValidation) &&
        Objects.equals(this.expand, filter.expand) &&
        Objects.equals(this.type, filter.type) &&
        Objects.equals(this.status, filter.status) &&
        Objects.equals(this.authenticationMethod, filter.authenticationMethod) &&
        Objects.equals(this.name, filter.name) &&
        Objects.equals(this.transaction, filter.transaction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(context, properties, product, session, createdAt, requestToken, user, params, matchingUserId, skipRequestTokenValidation, skipContextValidation, expand, type, status, authenticationMethod, name, transaction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Filter {\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("    product: ").append(toIndentedString(product)).append("\n");
    sb.append("    session: ").append(toIndentedString(session)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    requestToken: ").append(toIndentedString(requestToken)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    params: ").append(toIndentedString(params)).append("\n");
    sb.append("    matchingUserId: ").append(toIndentedString(matchingUserId)).append("\n");
    sb.append("    skipRequestTokenValidation: ").append(toIndentedString(skipRequestTokenValidation)).append("\n");
    sb.append("    skipContextValidation: ").append(toIndentedString(skipContextValidation)).append("\n");
    sb.append("    expand: ").append(toIndentedString(expand)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    authenticationMethod: ").append(toIndentedString(authenticationMethod)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    transaction: ").append(toIndentedString(transaction)).append("\n");
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

