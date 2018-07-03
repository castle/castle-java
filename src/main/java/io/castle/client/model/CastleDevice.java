package io.castle.client.model;

public class CastleDevice {
    private String id;
    private String manufacturer;
    private String model;
    private String name;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static Builder builder() {
        return new Builder(new CastleDevice());
    }

    public static class Builder {
        private CastleDevice device;

        public Builder(CastleDevice device) {
            this.device = device;
        }

        public Builder id(String id) {
            device.setId(id);
            return this;
        }

        public Builder manufacturer(String manufacturer) {
            device.setManufacturer(manufacturer);
            return this;
        }

        public Builder model(String model) {
            device.setModel(model);
            return this;
        }

        public Builder name(String name) {
            device.setName(name);
            return this;
        }

        public Builder type(String type) {
            device.setType(type);
            return this;
        }
    }

    @Override
    public String toString() {
        return "CastleDevice{" +
                "id='" + id + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
