package io.castle.client.deprecated.objects;

public class Error {

    private String type;
    private String message;

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Error)) return false;

        Error error = (Error) o;

        if (message != null ? !message.equals(error.message) : error.message != null) return false;
        if (type != null ? !type.equals(error.type) : error.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Error{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
