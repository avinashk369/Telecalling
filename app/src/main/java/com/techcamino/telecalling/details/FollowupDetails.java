package com.techcamino.telecalling.details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class FollowupDetails implements Serializable {

    @SerializedName("com_id")
    private @Getter@Setter String comId;
    @SerializedName("inqfollow_id")
    private @Getter@Setter String inqfollowId;
    @SerializedName("comment")
    private @Getter@Setter String comment;
    @SerializedName("expected_date")
    private @Getter@Setter String expectedDate;
    @SerializedName("interest_level")
    private @Getter@Setter String interestLevel;
    @SerializedName("inquiry_status")
    private @Getter@Setter String inquiryStatus;
    @SerializedName("next_pay_date")
    private @Getter@Setter String nextPayDate;
    @SerializedName("status")
    private @Getter@Setter String status;
    @SerializedName("create_date")
    private @Getter@Setter String createDate;
}
