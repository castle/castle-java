package io.castle.client.model;

import java.util.Map;

public class CastleUser {
    String id;
    String createdAt;
    String updatedAt;
    String lastSeenAt;
    String flaggedAt;
    double risk;
    int leaksCount;
    int devicesCount;
    String email;
    String name;
    String username;
    String phone;
    CastleUserAddress address;
    Map<String, String> customAttributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(String lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public String getFlaggedAt() {
        return flaggedAt;
    }

    public void setFlaggedAt(String flaggedAt) {
        this.flaggedAt = flaggedAt;
    }

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public int getLeaksCount() {
        return leaksCount;
    }

    public void setLeaksCount(int leaksCount) {
        this.leaksCount = leaksCount;
    }

    public int getDevicesCount() {
        return devicesCount;
    }

    public void setDevicesCount(int devicesCount) {
        this.devicesCount = devicesCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CastleUserAddress getAddress() {
        return address;
    }

    public void setAddress(CastleUserAddress address) {
        this.address = address;
    }

    public Map<String, String> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(Map<String, String> customAttributes) {
        this.customAttributes = customAttributes;
    }
}
