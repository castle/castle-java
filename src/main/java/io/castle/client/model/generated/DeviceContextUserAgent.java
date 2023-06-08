/*
 * Castle API
 * ## Introduction  **Just getting started? Check out our [quick start guide](https://docs.castle.io/docs/quickstart)**  Castle APIs uses standard HTTP response codes, authentication and verbs. JSON is used as data exchange format, both for parsing incoming request bodies, and in the returned response. This means that the `Content-Type` header should to be set to `application/json` in requests with a body, such as `POST` or `PUT`.  All API requests must be made over [HTTPS](http://en.wikipedia.org/wiki/HTTP_Secure). Non-HTTPS calls will fail and the **TLS version needs to be 1.1 or higher**.  ## Supported types  For a list of supported types, see our [Types Reference](https://docs.castle.io/docs/events).  ## Rate limits  Our APIs implement rate-limiting based on the number of requests made to them. Each request will return the following headers:  - `X-RateLimit-Limit` - The maximum number of requests you're permitted to make in the current time window. - `X-RateLimit-Remaining` - The number of requests remaining in the current time window. - `X-RateLimit-Reset` - The remaining time in seconds until the current time window resets.  Additionally, Our Risk, Log (and the legacy Authenticate) APIs have a per-user-id rate limit of 6 requests per second and 10 requests per 5 seconds. 
 *
 * The version of the OpenAPI document: 1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package io.castle.client.model.generated;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * DeviceContextUserAgent
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-06-05T10:18:20.077062+02:00[Europe/Stockholm]")
public class DeviceContextUserAgent {
  public static final String SERIALIZED_NAME_RAW = "raw";
  @SerializedName(SERIALIZED_NAME_RAW)
  private String raw;

  public static final String SERIALIZED_NAME_BROWSER = "browser";
  @SerializedName(SERIALIZED_NAME_BROWSER)
  private String browser;

  public static final String SERIALIZED_NAME_VERSION = "version";
  @SerializedName(SERIALIZED_NAME_VERSION)
  private String version;

  public static final String SERIALIZED_NAME_OS = "os";
  @SerializedName(SERIALIZED_NAME_OS)
  private String os;

  public static final String SERIALIZED_NAME_MOBILE = "mobile";
  @SerializedName(SERIALIZED_NAME_MOBILE)
  private Boolean mobile;

  public static final String SERIALIZED_NAME_PLATFORM = "platform";
  @SerializedName(SERIALIZED_NAME_PLATFORM)
  private String platform;

  public static final String SERIALIZED_NAME_DEVICE = "device";
  @SerializedName(SERIALIZED_NAME_DEVICE)
  private String device;

  public static final String SERIALIZED_NAME_FAMILY = "family";
  @SerializedName(SERIALIZED_NAME_FAMILY)
  private String family;


   /**
   * Get raw
   * @return raw
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36", required = true, value = "")

  public String getRaw() {
    return raw;
  }




   /**
   * Get browser
   * @return browser
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "Chrome", required = true, value = "")

  public String getBrowser() {
    return browser;
  }




   /**
   * Get version
   * @return version
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "92.0.4515", required = true, value = "")

  public String getVersion() {
    return version;
  }




   /**
   * Get os
   * @return os
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "Mac OS X 10.15.7", required = true, value = "")

  public String getOs() {
    return os;
  }




   /**
   * Get mobile
   * @return mobile
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "false", required = true, value = "")

  public Boolean getMobile() {
    return mobile;
  }




   /**
   * Get platform
   * @return platform
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "Mac OS X", required = true, value = "")

  public String getPlatform() {
    return platform;
  }




   /**
   * Get device
   * @return device
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "Mac", required = true, value = "")

  public String getDevice() {
    return device;
  }




   /**
   * Get family
   * @return family
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "Chrome", required = true, value = "")

  public String getFamily() {
    return family;
  }




  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceContextUserAgent deviceContextUserAgent = (DeviceContextUserAgent) o;
    return Objects.equals(this.raw, deviceContextUserAgent.raw) &&
        Objects.equals(this.browser, deviceContextUserAgent.browser) &&
        Objects.equals(this.version, deviceContextUserAgent.version) &&
        Objects.equals(this.os, deviceContextUserAgent.os) &&
        Objects.equals(this.mobile, deviceContextUserAgent.mobile) &&
        Objects.equals(this.platform, deviceContextUserAgent.platform) &&
        Objects.equals(this.device, deviceContextUserAgent.device) &&
        Objects.equals(this.family, deviceContextUserAgent.family);
  }

  @Override
  public int hashCode() {
    return Objects.hash(raw, browser, version, os, mobile, platform, device, family);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceContextUserAgent {\n");
    sb.append("    raw: ").append(toIndentedString(raw)).append("\n");
    sb.append("    browser: ").append(toIndentedString(browser)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    os: ").append(toIndentedString(os)).append("\n");
    sb.append("    mobile: ").append(toIndentedString(mobile)).append("\n");
    sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
    sb.append("    device: ").append(toIndentedString(device)).append("\n");
    sb.append("    family: ").append(toIndentedString(family)).append("\n");
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
