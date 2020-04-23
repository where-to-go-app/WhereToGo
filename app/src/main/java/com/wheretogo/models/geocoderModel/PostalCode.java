
package com.wheretogo.models.geocoderModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostalCode {

    @SerializedName("PostalCodeNumber")
    @Expose
    private String postalCodeNumber;

    public String getPostalCodeNumber() {
        return postalCodeNumber;
    }

    public void setPostalCodeNumber(String postalCodeNumber) {
        this.postalCodeNumber = postalCodeNumber;
    }

}
