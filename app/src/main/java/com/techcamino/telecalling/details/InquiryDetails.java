package com.techcamino.telecalling.details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class InquiryDetails implements Serializable {

    /**
     */
    @SerializedName("tele_id")
    private @Getter@Setter String teleId;
    @SerializedName("name")
    private @Getter@Setter String name;
    @SerializedName("comp_name")
    private @Getter@Setter String compName;
    @SerializedName("email")
    private @Getter@Setter String email;
    @SerializedName("city")
    private @Getter@Setter String city;
    @SerializedName("state")
    private @Getter@Setter String state;
    @SerializedName("country")
    private @Getter@Setter String country;
    @SerializedName("mobile_no")
    private @Getter@Setter String mobileNo;
    @SerializedName("alternate_mobile")
    private @Getter@Setter String alternateMobile;
    @SerializedName("landline_no")
    private @Getter@Setter String landlineNo;
    @SerializedName("alternate_landline")
    private @Getter@Setter String alternateLandline;
    @SerializedName("address")
    private @Getter@Setter String address;
    @SerializedName("data_name")
    private @Getter@Setter String dataName;
    @SerializedName("tag")
    private @Getter@Setter String tag;
    @SerializedName("interest_level")
    private @Getter@Setter String interestLevel;
    @SerializedName("website")
    private @Getter@Setter String website;
    @SerializedName("data_source")
    private @Getter@Setter String dataSource;
    @SerializedName("data_status")
    private @Getter@Setter String dataStatus;
    @SerializedName("phone_option")
    private @Getter@Setter String phoneOption;
    @SerializedName("inquiry_status")
    private @Getter@Setter String inquiryStatus;
    @SerializedName("inquiry_update")
    private @Getter@Setter String inquiryUpdate;
    @SerializedName("duplicate_flag")
    private @Getter@Setter String duplicateFlag;
    @SerializedName("comment")
    private @Getter@Setter String comment;
    @SerializedName("staff_id")
    private @Getter@Setter String staffId;
    @SerializedName("staff_name")
    private @Getter@Setter String staffName;
    @SerializedName("status")
    private @Getter@Setter String status;
    @SerializedName("create_date")
    private @Getter@Setter String createDate;
    @SerializedName("interestLevel")
    private @Getter@Setter InterestDetails interestDetails;

}
