package io.castle.client.deprecated.objects;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
    private String country;
    @JsonProperty("country_code")
    private String countryCode;
    private String region;
    @JsonProperty("region_code")
    private String regionCode;
    private String city;
    @JsonProperty("lon")
    private String longitude;
    @JsonProperty("lat")
    private String latitude;

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String getCountryCode() {
	return countryCode;
    }

    public void setCountryCode(String countryCode) {
	this.countryCode = countryCode;
    }

    public String getRegion() {
	return region;
    }

    public void setRegion(String region) {
	this.region = region;
    }

    public String getRegionCode() {
	return regionCode;
    }

    public void setRegionCode(String regionCode) {
	this.regionCode = regionCode;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getLongitude() {
	return longitude;
    }

    public void setLongitude(String longitude) {
	this.longitude = longitude;
    }

    public String getLatitude() {
	return latitude;
    }

    public void setLatitude(String latitude) {
	this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Location)) return false;

	Location location = (Location) o;

	if (city != null ? !city.equals(location.city) : location.city != null) return false;
	if (country != null ? !country.equals(location.country) : location.country != null) return false;
	if (countryCode != null ? !countryCode.equals(location.countryCode) : location.countryCode != null)
	    return false;
	if (latitude != null ? !latitude.equals(location.latitude) : location.latitude != null) return false;
	if (longitude != null ? !longitude.equals(location.longitude) : location.longitude != null) return false;
	if (region != null ? !region.equals(location.region) : location.region != null) return false;
	if (regionCode != null ? !regionCode.equals(location.regionCode) : location.regionCode != null) return false;

	return true;
    }

    @Override
    public int hashCode() {
	int result = country != null ? country.hashCode() : 0;
	result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
	result = 31 * result + (region != null ? region.hashCode() : 0);
	result = 31 * result + (regionCode != null ? regionCode.hashCode() : 0);
	result = 31 * result + (city != null ? city.hashCode() : 0);
	result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
	result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
	return result;
    }

    @Override
    public String toString() {
	return "Location{" +
		"country='" + country + '\'' +
		", countryCode='" + countryCode + '\'' +
		", region='" + region + '\'' +
		", regionCode='" + regionCode + '\'' +
		", city='" + city + '\'' +
		", longitude='" + longitude + '\'' +
		", latitude='" + latitude + '\'' +
		'}';
    }
}
