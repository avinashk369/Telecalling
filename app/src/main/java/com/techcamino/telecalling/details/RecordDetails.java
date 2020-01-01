package com.techcamino.telecalling.details;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class RecordDetails {

    @SerializedName("id")
    private @Getter@Setter String id;
    @SerializedName("staff_id")
    private @Getter@Setter String staffId;
    @SerializedName("call_duration")
    private @Getter@Setter String callDuration;
    @SerializedName("mobile_number")
    private @Getter@Setter String mobileNumber;
    @SerializedName("created_on")
    private @Getter@Setter String createdOn;
    @SerializedName("flags")
    private @Getter@Setter String flags;
}
