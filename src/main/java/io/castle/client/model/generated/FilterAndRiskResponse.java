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
import java.util.Map;
import java.util.Objects;
/**
 * FilterAndRiskResponse
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2025-01-20T18:58:06.855017776Z[GMT]")

public class FilterAndRiskResponse {
  @SerializedName("risk")
  private Double risk;

  @SerializedName("scores")
  private FilterAndRiskResponseScores scores = null;

  @SerializedName("policy")
  private Policy policy = null;

  @SerializedName("signals")
  private Map<String, Object> signals = new HashMap<String, Object>();

  @SerializedName("metrics")
  private Map<String, Object> metrics = null;

  @SerializedName("device")
  private Device device = null;

  @SerializedName("id")
  private String id = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("type")
  private String type = null;

  /**
   * Gets or Sets status
   */
  @SerializedName("status")
  private String status = "$attempted";

  @SerializedName("created_at")
  private OffsetDateTime createdAt = null;

  @SerializedName("authenticated")
  private Boolean authenticated = null;

  @SerializedName("authentication_method")
  private AuthenticationMethod authenticationMethod = null;

  @SerializedName("email")
  private FilterAndRiskResponseEmail email = null;

  @SerializedName("endpoint")
  private String endpoint = null;

  @SerializedName("ip")
  private IP ip = null;

  @SerializedName("params")
  private EventParams params = null;

  @SerializedName("product")
  private Product product = null;

  @SerializedName("sdks")
  private FilterAndRiskResponseSdks sdks = null;

  @SerializedName("session")
  private Session session = null;

  @SerializedName("user")
  private User user = null;

  @SerializedName("behavior")
  private Behavior behavior = null;

  @SerializedName("properties")
  private Map<String, Object> properties = null;

   /**
   * Deprecated Risk Score - use &#x60;scores&#x60; instead. Calculated only when &#x60;request_token&#x60; and &#x60;context&#x60; are provided.
   * minimum: 0
   * maximum: 1
   * @return risk
  **/
  @ApiModelProperty(example = "0.65", value = "Deprecated Risk Score - use `scores` instead. Calculated only when `request_token` and `context` are provided.")
  public Double getRisk() {
    return risk;
  }

  public FilterAndRiskResponse scores(FilterAndRiskResponseScores scores) {
    this.scores = scores;
    return this;
  }

   /**
   * Get scores
   * @return scores
  **/
  @ApiModelProperty(value = "")
  public FilterAndRiskResponseScores getScores() {
    return scores;
  }

  public void setScores(FilterAndRiskResponseScores scores) {
    this.scores = scores;
  }

  public FilterAndRiskResponse policy(Policy policy) {
    this.policy = policy;
    return this;
  }

   /**
   * Get policy
   * @return policy
  **/
  @ApiModelProperty(required = true, value = "")
  public Policy getPolicy() {
    return policy;
  }

  public void setPolicy(Policy policy) {
    this.policy = policy;
  }

   /**
   * Signals triggered for this event/context
   * @return signals
  **/
  @ApiModelProperty(example = "{\"bot_behavior\":{},\"proxy_ip\":{},\"disposable_email\":{},\"spoofed_device\":{},\"multiple_accounts_per_device\":{}}", required = true, value = "Signals triggered for this event/context")
  public Map<String, Object> getSignals() {
    return signals;
  }

   /**
   * Metrics triggered for this event/context
   * @return metrics
  **/
  @ApiModelProperty(example = "{\"1\":{},\"2\":{},\"3\":{},\"4\":{},\"5\":{}}", value = "Metrics triggered for this event/context")
  public Map<String, Object> getMetrics() {
    return metrics;
  }

  public FilterAndRiskResponse device(Device device) {
    this.device = device;
    return this;
  }

   /**
   * Get device
   * @return device
  **/
  @ApiModelProperty(required = true, value = "")
  public Device getDevice() {
    return device;
  }

  public void setDevice(Device device) {
    this.device = device;
  }

   /**
   * Castle Event ID
   * @return id
  **/
  @ApiModelProperty(example = "ASZoelALT5-PaVw2pAVMXg", value = "Castle Event ID")
  public String getId() {
    return id;
  }

  public FilterAndRiskResponse name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Event Full Name
   * @return name
  **/
  @ApiModelProperty(example = "Login Succeeded", value = "Event Full Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FilterAndRiskResponse type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Castle supported events available for this endpoint
   * @return type
  **/
  @ApiModelProperty(required = true, value = "Castle supported events available for this endpoint")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public FilterAndRiskResponse status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

   /**
   * Event timestamp
   * @return createdAt
  **/
  @ApiModelProperty(example = "2021-09-27T16:46:38.313Z", value = "Event timestamp")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

   /**
   * Authenticated or Anonymous visitor
   * @return authenticated
  **/
  @ApiModelProperty(example = "true", value = "Authenticated or Anonymous visitor")
  public Boolean isAuthenticated() {
    return authenticated;
  }

  public FilterAndRiskResponse authenticationMethod(AuthenticationMethod authenticationMethod) {
    this.authenticationMethod = authenticationMethod;
    return this;
  }

   /**
   * Get authenticationMethod
   * @return authenticationMethod
  **/
  @ApiModelProperty(value = "")
  public AuthenticationMethod getAuthenticationMethod() {
    return authenticationMethod;
  }

  public void setAuthenticationMethod(AuthenticationMethod authenticationMethod) {
    this.authenticationMethod = authenticationMethod;
  }

  public FilterAndRiskResponse email(FilterAndRiskResponseEmail email) {
    this.email = email;
    return this;
  }

   /**
   * Get email
   * @return email
  **/
  @ApiModelProperty(value = "")
  public FilterAndRiskResponseEmail getEmail() {
    return email;
  }

  public void setEmail(FilterAndRiskResponseEmail email) {
    this.email = email;
  }

   /**
   * Castle Request Endpoint
   * @return endpoint
  **/
  @ApiModelProperty(example = "/v1/risk", value = "Castle Request Endpoint")
  public String getEndpoint() {
    return endpoint;
  }

  public FilterAndRiskResponse ip(IP ip) {
    this.ip = ip;
    return this;
  }

   /**
   * Get ip
   * @return ip
  **/
  @ApiModelProperty(value = "")
  public IP getIp() {
    return ip;
  }

  public void setIp(IP ip) {
    this.ip = ip;
  }

  public FilterAndRiskResponse params(EventParams params) {
    this.params = params;
    return this;
  }

   /**
   * Get params
   * @return params
  **/
  @ApiModelProperty(value = "")
  public EventParams getParams() {
    return params;
  }

  public void setParams(EventParams params) {
    this.params = params;
  }

  public FilterAndRiskResponse product(Product product) {
    this.product = product;
    return this;
  }

   /**
   * Get product
   * @return product
  **/
  @ApiModelProperty(value = "")
  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public FilterAndRiskResponse sdks(FilterAndRiskResponseSdks sdks) {
    this.sdks = sdks;
    return this;
  }

   /**
   * Get sdks
   * @return sdks
  **/
  @ApiModelProperty(value = "")
  public FilterAndRiskResponseSdks getSdks() {
    return sdks;
  }

  public void setSdks(FilterAndRiskResponseSdks sdks) {
    this.sdks = sdks;
  }

  public FilterAndRiskResponse session(Session session) {
    this.session = session;
    return this;
  }

   /**
   * Get session
   * @return session
  **/
  @ApiModelProperty(value = "")
  public Session getSession() {
    return session;
  }

  public void setSession(Session session) {
    this.session = session;
  }

  public FilterAndRiskResponse user(User user) {
    this.user = user;
    return this;
  }

   /**
   * Get user
   * @return user
  **/
  @ApiModelProperty(value = "")
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public FilterAndRiskResponse behavior(Behavior behavior) {
    this.behavior = behavior;
    return this;
  }

   /**
   * Get behavior
   * @return behavior
  **/
  @ApiModelProperty(value = "")
  public Behavior getBehavior() {
    return behavior;
  }

  public void setBehavior(Behavior behavior) {
    this.behavior = behavior;
  }

  public FilterAndRiskResponse properties(Map<String, Object>  properties) {
    this.properties = properties;
    return this;
  }

   /**
   * Get properties
   * @return properties
  **/
  @ApiModelProperty(value = "")
  public Map<String, Object>  getProperties() {
    return properties;
  }

  public void setProperties(Map<String, Object> properties) {
    this.properties = properties;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilterAndRiskResponse loginResponse = (FilterAndRiskResponse) o;
    return Objects.equals(this.risk, loginResponse.risk) &&
        Objects.equals(this.scores, loginResponse.scores) &&
        Objects.equals(this.policy, loginResponse.policy) &&
        Objects.equals(this.signals, loginResponse.signals) &&
        Objects.equals(this.metrics, loginResponse.metrics) &&
        Objects.equals(this.device, loginResponse.device) &&
        Objects.equals(this.id, loginResponse.id) &&
        Objects.equals(this.name, loginResponse.name) &&
        Objects.equals(this.type, loginResponse.type) &&
        Objects.equals(this.status, loginResponse.status) &&
        Objects.equals(this.createdAt, loginResponse.createdAt) &&
        Objects.equals(this.authenticated, loginResponse.authenticated) &&
        Objects.equals(this.authenticationMethod, loginResponse.authenticationMethod) &&
        Objects.equals(this.email, loginResponse.email) &&
        Objects.equals(this.endpoint, loginResponse.endpoint) &&
        Objects.equals(this.ip, loginResponse.ip) &&
        Objects.equals(this.params, loginResponse.params) &&
        Objects.equals(this.product, loginResponse.product) &&
        Objects.equals(this.sdks, loginResponse.sdks) &&
        Objects.equals(this.session, loginResponse.session) &&
        Objects.equals(this.user, loginResponse.user) &&
        Objects.equals(this.behavior, loginResponse.behavior) &&
        Objects.equals(this.properties, loginResponse.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(risk, scores, policy, signals, metrics, device, id, name, type, status, createdAt, authenticated, authenticationMethod, email, endpoint, ip, params, product, sdks, session, user, behavior, properties);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterAndRiskResponse {\n");
    
    sb.append("    risk: ").append(toIndentedString(risk)).append("\n");
    sb.append("    scores: ").append(toIndentedString(scores)).append("\n");
    sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
    sb.append("    signals: ").append(toIndentedString(signals)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    device: ").append(toIndentedString(device)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    authenticated: ").append(toIndentedString(authenticated)).append("\n");
    sb.append("    authenticationMethod: ").append(toIndentedString(authenticationMethod)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
    sb.append("    ip: ").append(toIndentedString(ip)).append("\n");
    sb.append("    params: ").append(toIndentedString(params)).append("\n");
    sb.append("    product: ").append(toIndentedString(product)).append("\n");
    sb.append("    sdks: ").append(toIndentedString(sdks)).append("\n");
    sb.append("    session: ").append(toIndentedString(session)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    behavior: ").append(toIndentedString(behavior)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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
