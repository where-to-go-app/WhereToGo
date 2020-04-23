
package com.wheretogo.models.geocoderModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoObject {

    @SerializedName("metaDataProperty")
    @Expose
    private MetaDataProperty_ metaDataProperty;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("boundedBy")
    @Expose
    private BoundedBy_ boundedBy;
    @SerializedName("Point")
    @Expose
    private Point_ point;

    public MetaDataProperty_ getMetaDataProperty() {
        return metaDataProperty;
    }

    public void setMetaDataProperty(MetaDataProperty_ metaDataProperty) {
        this.metaDataProperty = metaDataProperty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BoundedBy_ getBoundedBy() {
        return boundedBy;
    }

    public void setBoundedBy(BoundedBy_ boundedBy) {
        this.boundedBy = boundedBy;
    }

    public Point_ getPoint() {
        return point;
    }

    public void setPoint(Point_ point) {
        this.point = point;
    }

}
