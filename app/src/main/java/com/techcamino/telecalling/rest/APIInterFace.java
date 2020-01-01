package com.techcamino.telecalling.rest;


import com.techcamino.telecalling.details.CommentsDetails;
import com.techcamino.telecalling.details.FollowupDetails;
import com.techcamino.telecalling.details.InquiryDetails;
import com.techcamino.telecalling.details.InterestDetails;
import com.techcamino.telecalling.details.LoginDetails;
import com.techcamino.telecalling.details.RecordDetails;
import com.techcamino.telecalling.details.StaffDetails;
import com.techcamino.telecalling.details.StateCityDetails;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * Created by Thinkpad on 12-10-2016.
 */

public interface APIInterFace {

    @POST("user/login")
    @FormUrlEncoded
    Call<LoginDetails> loginUser(@FieldMap Map<String, String> loginMap);

    @GET("user/show")
    Call<LoginDetails> getUser(@Header("userId") String id);

    @GET("country/state")
    Call<ArrayList<StateCityDetails>> getStateList(@Header("stateId") String stateId);

    @GET("follow/view")
    Call<ArrayList<FollowupDetails>> getFollowupList(@Header("stateId") String stateId);

    @GET("follow/list")
    Call<ArrayList<InquiryDetails>> getFollowupList(@HeaderMap Map<String,String> queryData);

    @GET("inquiry/view")
    Call<ArrayList<InquiryDetails>> getInquiryList(@HeaderMap Map<String,String> queryData);

    @GET("inquiry/comments")
    Call<ArrayList<CommentsDetails>> getInquiryComments(@HeaderMap Map<String,String> queryData);

    @GET("inquiry/top")
    Call<ArrayList<InquiryDetails>> getTopInquiryList(@HeaderMap Map<String,String> queryData);


    @GET("follow/top")
    Call<ArrayList<InquiryDetails>> getTopFollowupList(@HeaderMap Map<String,String> queryData);


    @GET("country/city")
    Call<ArrayList<StateCityDetails>> getCityList(@Header("stateId") String stateId);

    @GET("user/getInterest")
    Call<ArrayList<InterestDetails>> getInterestList();

    @GET("user/staff")
    Call<ArrayList<StaffDetails>> getStaffList();

    @POST("inquiry/add")
    Call<InquiryDetails> saveInquiryDetail(@Body InquiryDetails inquiryDetails);

    @POST("inquiry/addComments")
    Call<CommentsDetails> saveInquiryComments(@Body CommentsDetails commentsDetails);


    @POST("follow/upload")
    Call<ResponseBody> uploadMediaFile(@Body RequestBody file);


    @GET("follow/getRecords")
    Call<ArrayList<RecordDetails>> getRecordLog(@HeaderMap Map<String,String> queryData);

}
