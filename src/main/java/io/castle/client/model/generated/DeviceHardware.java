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
 * DeviceHardware
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-05T19:53:01.452316+01:00[Europe/Stockholm]")
public class DeviceHardware {
  public static final String SERIALIZED_NAME_TYPE = "type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  private String type;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_BRAND = "brand";
  @SerializedName(SERIALIZED_NAME_BRAND)
  private String brand;

  public static final String SERIALIZED_NAME_MODEL = "model";
  @SerializedName(SERIALIZED_NAME_MODEL)
  private DeviceHardwareModel model;

  public static final String SERIALIZED_NAME_DISPLAY = "display";
  @SerializedName(SERIALIZED_NAME_DISPLAY)
  private DeviceHardwareDisplay display = null;


  public DeviceHardware type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "computer", required = true, value = "")

  public String getType() {
    return type;
  }


  public void setType(String type) {
    this.type = type;
  }


  public DeviceHardware name(String name) {
    
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(required = true, value = "")

  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public DeviceHardware brand(String brand) {
    
    this.brand = brand;
    return this;
  }

   /**
   * Get brand
   * @return brand
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(required = true, value = "")

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public DeviceHardware display(DeviceHardwareDisplay display) {
    this.display = display;
    return this;
  }

   /**
   * Get display
   * @return display
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  public DeviceHardwareDisplay getDisplay() {
    return display;
  }

  public void setDisplay(DeviceHardwareDisplay display) {
    this.display = display;
  }

  public DeviceHardware model(DeviceHardwareModel model) {
    this.model = model;
    return this;
  }

   /**
   * Get model
   * @return model
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")

  public DeviceHardwareModel getModel() {
    return model;
  }


  public void setModel(DeviceHardwareModel model) {
    this.model = model;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceHardware deviceHardware = (DeviceHardware) o;
    return Objects.equals(this.type, deviceHardware.type) &&
        Objects.equals(this.name, deviceHardware.name) &&
        Objects.equals(this.brand, deviceHardware.brand) &&
        Objects.equals(this.display, deviceHardware.display) &&
        Objects.equals(this.model, deviceHardware.model);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, name, brand, display, model);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceHardware {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
    sb.append("    display: ").append(toIndentedString(display)).append("\n");
    sb.append("    model: ").append(toIndentedString(model)).append("\n");
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

