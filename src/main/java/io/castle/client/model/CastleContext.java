package io.castle.client.model;

/**
 * Complete model of a context object with all the fields the Castle API understands.
 */
public class CastleContext {
    private boolean active = true;
    private CastleDevice device;
    private String timezone;
    private CastlePage page;
    private CastleReferrer referrer;
    private CastleSdkRef library = new CastleSdkRef();
    private CastleLocation location;
    private CastleNetwork network;
    private CastleOS os;
    private CastleScreen screen;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public CastleDevice getDevice() {
        return device;
    }

    public void setDevice(CastleDevice device) {
        this.device = device;
    }

    public CastlePage getPage() {
        return page;
    }

    public void setPage(CastlePage page) {
        this.page = page;
    }

    public CastleReferrer getReferrer() {
        return referrer;
    }

    public void setReferrer(CastleReferrer referrer) {
        this.referrer = referrer;
    }

    public CastleSdkRef getLibrary() {
        return library;
    }

    public CastleLocation getLocation() {
        return location;
    }

    public void setLocation(CastleLocation location) {
        this.location = location;
    }

    public CastleNetwork getNetwork() {
        return network;
    }

    public void setNetwork(CastleNetwork network) {
        this.network = network;
    }

    public CastleOS getOs() {
        return os;
    }

    public void setOs(CastleOS os) {
        this.os = os;
    }

    public CastleScreen getScreen() {
        return screen;
    }

    public void setScreen(CastleScreen screen) {
        this.screen = screen;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * Produces a string representation of the context object.
     *
     * @return the string representation of this {@code castleContext}
     */
    @Override
    public String toString() {
        return "CastleContext{" +
                "active=" + active +
                ", device=" + device +
                ", timezone='" + timezone + '\'' +
                ", page=" + page +
                ", referrer=" + referrer +
                ", library=" + library +
                ", location=" + location +
                ", network=" + network +
                ", os=" + os +
                ", screen=" + screen + '\'' +
                '}';
    }
}
