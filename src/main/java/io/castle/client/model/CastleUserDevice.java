package io.castle.client.model;

public class CastleUserDevice {
    private String token;
    private String createdAt;
    private String lastSeenAt;
    private String approvedAt;
    private String escalatedAt;
    private String mitigatedAt;
    private CastleUserDeviceContext context;

    boolean isCurrentDevice;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(String lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public String getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(String approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getEscalatedAt() {
        return escalatedAt;
    }

    public void setEscalatedAt(String escalatedAt) {
        this.escalatedAt = escalatedAt;
    }

    public String getMitigatedAt() {
        return mitigatedAt;
    }

    public void setMitigatedAt(String mitigatedAt) {
        this.mitigatedAt = mitigatedAt;
    }

    public CastleUserDeviceContext getContext() {
        return context;
    }

    public void setContext(CastleUserDeviceContext context) {
        this.context = context;
    }

    public boolean isCurrentDevice() {
        return isCurrentDevice;
    }

    public void setCurrentDevice(boolean currentDevice) {
        isCurrentDevice = currentDevice;
    }
}
