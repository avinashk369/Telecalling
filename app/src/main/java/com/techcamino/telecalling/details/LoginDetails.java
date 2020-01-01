package com.techcamino.telecalling.details;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class LoginDetails {

    @SerializedName("log_id")
    private @Getter@Setter String logId;
    @SerializedName("login_name")
    private @Getter@Setter String name;
    @SerializedName("user_id_fk")
    private @Getter@Setter String userIdFk;
    @SerializedName("login_pass")
    private @Getter@Setter String password;
    @SerializedName("user_type")
    private @Getter@Setter String userType;
    @SerializedName("create_date")
    private @Getter@Setter String createDate;
    @SerializedName("last_login_date")
    private @Getter@Setter String lastLoginDate;
    @SerializedName("last_login_ip")
    private @Getter@Setter String lastLoginIp;
    @SerializedName("login_status")
    private @Getter@Setter String loginStatus;
}
