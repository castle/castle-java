package io.castle.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastleUserDevices {
    private int totalCount;
    @SerializedName("data")
    private List<CastleUserDevice> devices;

    public int getTotalCount() {
        return totalCount;
    }

    public List<CastleUserDevice> getDevices() {
        return devices;
    }

    public void setDevices(List<CastleUserDevice> devices) {
        this.devices = devices;
        this.totalCount = devices != null ? devices.size() : 0;
    }
}
