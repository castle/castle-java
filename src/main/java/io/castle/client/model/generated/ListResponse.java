package io.castle.client.model.generated;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import org.threeten.bp.OffsetDateTime;

import java.util.Objects;

public class ListResponse extends ListRequest {
    @SerializedName("created_at")
    private OffsetDateTime createdAt = null;

    @SerializedName("archived_at")
    private OffsetDateTime archivedAt = null;

    @SerializedName("id")
    private String id = null;

    @SerializedName("size_label")
    private String sizeLabel = null;

    @ApiModelProperty(example = "9852", required = false, value = "Size label of the List")
    public String getSizeLabel() {
        return sizeLabel;
    }

    public void setSizeLabel(String sizeLabel) {
        this.sizeLabel = sizeLabel;
    }

    public ListResponse id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     * @return id
     */
    @ApiModelProperty(example = "2ee938c8-24c2-4c26-9d25-19511dd75029", required = true, value = "")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Timestamp of when Aggregation was created.
     * @return createdAt
     **/
    @ApiModelProperty(example = "2022-10-23T17:21:39.213Z", value = "Timestamp of when List was created.")
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ListResponse createdAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Timestamp of when Aggregation was archived.
     * @return archivedAt
     **/
    @ApiModelProperty(example = "2022-10-23T17:21:39.213Z", value = "Timestamp of when List was archived.")
    public OffsetDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(OffsetDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    public ListResponse archivedAt(OffsetDateTime archivedAt) {
        this.archivedAt = createdAt;
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListResponse listResponse = (ListResponse) o;
        return Objects.equals(this.createdAt, listResponse.createdAt) &&
                Objects.equals(this.archivedAt, listResponse.archivedAt) &&
                Objects.equals(this.sizeLabel, listResponse.sizeLabel) &&
                Objects.equals(this.id, listResponse.id) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, archivedAt, sizeLabel, id, super.hashCode());
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ListResponse {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    sizeLabel: ").append(toIndentedString(sizeLabel)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    archivedAt: ").append(toIndentedString(archivedAt)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
