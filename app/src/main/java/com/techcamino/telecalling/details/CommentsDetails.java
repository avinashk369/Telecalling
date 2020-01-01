package com.techcamino.telecalling.details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class CommentsDetails implements Serializable {

    /**
     */
    @SerializedName("com_id")
    private @Getter@Setter String comId;
    @SerializedName("tele_id")
    private @Getter@Setter String teleId;
    @SerializedName("comment")
    private @Getter@Setter String comment;
    @SerializedName("staff_id")
    private @Getter@Setter String staffId;
    @SerializedName("staff_name")
    private @Getter@Setter String staffName;
    @SerializedName("followup_date")
    private @Getter@Setter String followupDate;
    @SerializedName("followup_time")
    private @Getter@Setter String followupTime;
    @SerializedName("interest_level")
    private @Getter@Setter String interestLevel;
    @SerializedName("phone_option")
    private @Getter@Setter String phoneOption;
    @SerializedName("hour_option")
    private @Getter@Setter String hourOption;
    @SerializedName("inquiry_update")
    private @Getter@Setter String inquiryUpdate;
    @SerializedName("create_date")
    private @Getter@Setter String createDate;
    @SerializedName("status")
    private @Getter@Setter String status;
}
