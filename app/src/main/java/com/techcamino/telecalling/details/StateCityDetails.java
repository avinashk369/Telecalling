package com.techcamino.telecalling.details;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class StateCityDetails {

    @SerializedName("id")
    private @Getter@Setter String id;
    @SerializedName("state")
    private @Getter@Setter String state;
    @SerializedName("country_id")
    private @Getter@Setter String countryId;
    @SerializedName("parent")
    private @Getter@Setter String parent;
    @SerializedName("status")
    private @Getter@Setter String status;

}
