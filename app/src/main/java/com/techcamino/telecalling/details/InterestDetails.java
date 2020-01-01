package com.techcamino.telecalling.details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class InterestDetails implements Serializable {

    @SerializedName("ints_id")
    private @Getter@Setter String id;
    @SerializedName("ints_level")
    private @Getter@Setter String intsLevel;
    @SerializedName("ints_order")
    private @Getter@Setter String intsOrder;
    @SerializedName("create_date")
    private @Getter@Setter String createDate;
    @SerializedName("status")
    private @Getter@Setter String status;

}
