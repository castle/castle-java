package io.castle.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastleUserDevices {
    int totalCount;
    @SerializedName("data")
    List<CastleUserDevice> devices;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<CastleUserDevice> getDevices() {
        return devices;
    }

    public void setDevices(List<CastleUserDevice> devices) {
        this.devices = devices;
    }
}
