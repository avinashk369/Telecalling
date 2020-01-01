package com.techcamino.telecalling.details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class StaffDetails implements Serializable {

    /**
     */
    @SerializedName("staff_id")
    private @Getter@Setter String staffId;
    @SerializedName("firstname")
    private @Getter@Setter String firstName;
    @SerializedName("lastname")
    private @Getter@Setter String lastName;
    @SerializedName("contact")
    private @Getter@Setter String contact;
    @SerializedName("email_id")
    private @Getter@Setter String email;
    @SerializedName("role")
    private @Getter@Setter String role;
    @SerializedName("salary")
    private @Getter@Setter String salary;
    @SerializedName("address")
    private @Getter@Setter String address;
    @SerializedName("image")
    private @Getter@Setter String image;
    @SerializedName("status")
    private @Getter@Setter String status;
    @SerializedName("join_date")
    private @Getter@Setter String join_date;
    @SerializedName("regs_no")
    private @Getter@Setter String regs_no;
    @SerializedName("del_flag")
    private @Getter@Setter String del_flag;
}
