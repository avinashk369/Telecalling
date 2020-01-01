package com.techcamino.telecalling.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techcamino.telecalling.R;
import com.techcamino.telecalling.adapter.CommentsAdapter;
import com.techcamino.telecalling.adapter.InquiryAdapter;
import com.techcamino.telecalling.details.CommentsDetails;
import com.techcamino.telecalling.details.InquiryDetails;
import com.techcamino.telecalling.details.MessageDetails;
import com.techcamino.telecalling.rest.APIClient;
import com.techcamino.telecalling.rest.APIInterFace;
import com.techcamino.telecalling.util.Constants;
import com.techcamino.telecalling.util.DividerItemDecorator;
import com.techcamino.telecalling.util.Security;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewInquiry extends AppCompatActivity implements CommentsAdapter.CustomDialogListener {

    private Context context = this;
    private APIInterFace apiService;
    private ProgressDialog dialog;
    private String loginId;
    private InquiryDetails inquiryDetails;
    private TextView state,city,compName,name,email,number,address;
    private HashMap<String,String> queryData;
    private ArrayList<CommentsDetails> commentsDetailsArrayList;
    private RecyclerView uncleared_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inquiry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.inquiry_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);

        dialog = Security.getProgressDialog(this, "Please Wait");
        apiService =
                APIClient.getClient().create(APIInterFace.class);

        inquiryDetails = new InquiryDetails();
        commentsDetailsArrayList = new ArrayList<>();
        queryData = new HashMap<>();

        state = findViewById(R.id.state_name);
        city = findViewById(R.id.city_name);
        address = findViewById(R.id.address);
        compName = findViewById(R.id.comp_name);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        number = findViewById(R.id.mobile_number);
        uncleared_list = findViewById(R.id.uncleared_list);
        findViewById(R.id.follow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent remark = new Intent(ViewInquiry.this, FollowRemark
                        .class);
                remark.putExtra(Constants.INQUIRY_DETAIL,inquiryDetails);
                remark.putExtra(Constants.USER_LOGIN_ID, loginId);
                startActivity(remark);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        loginId = getIntent().getStringExtra(Constants.USER_LOGIN_ID);
        inquiryDetails =  (InquiryDetails) getIntent().getSerializableExtra(Constants.INQUIRY_DETAIL);
        //Toast.makeText(context,loginId+"_"+inquiryDetails.getTeleId(),Toast.LENGTH_LONG).show();
        initView(inquiryDetails);
        queryData.put(Constants.INQUIRY_ID,inquiryDetails.getTeleId());
        getInquiryCommnets(queryData);
    }

    public void initView(InquiryDetails inquiryDetails){

        //state,city,compName,name,email,number,address
        state.setText((inquiryDetails.getState() !=null) ? inquiryDetails.getState().toString().trim()  : "");
        city.setText((inquiryDetails.getCity() !=null) ? inquiryDetails.getCity().toString().trim() : "");
        compName.setText((inquiryDetails.getCompName() !=null) ? inquiryDetails.getCompName().toString().trim() : "");
        name.setText((inquiryDetails.getName() !=null) ?inquiryDetails.getName().toString().trim() : "");
        email.setText((inquiryDetails.getEmail() !=null) ? inquiryDetails.getEmail().toString().trim() : "");
        number.setText((inquiryDetails.getMobileNo() !=null )? inquiryDetails.getMobileNo().toString().trim() : "");
        address.setText((inquiryDetails.getAddress() !=null) ? inquiryDetails.getAddress().toString().trim() : "");

    }

    public void getInquiryCommnets(HashMap<String ,String> queryData){
        dialog.show();
        Call<ArrayList<CommentsDetails>> call = apiService.getInquiryComments(queryData);
        call.enqueue(new Callback<ArrayList<CommentsDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentsDetails>> call, Response<ArrayList<CommentsDetails>> response) {
                if (response.isSuccessful()) {

                    commentsDetailsArrayList = response.body();

                    CommentsAdapter walletAdapter = new CommentsAdapter(context,commentsDetailsArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    uncleared_list.setLayoutManager(mLayoutManager);
                    uncleared_list.addItemDecoration(new DividerItemDecorator(context.getResources().getDrawable(R.drawable.divider)));
                    uncleared_list.setAdapter(walletAdapter);

                } else {
                    try {
                        MessageDetails messageDetails = new MessageDetails();
                        Gson gson = new Gson();
                        messageDetails=gson.fromJson(response.errorBody().charStream(),MessageDetails.class);
                        Log.d("Error",messageDetails.getMessage().toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommentsDetails>> call, Throwable t) {
                Log.d("Failure", t.getMessage().toString());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("Failure",t.getMessage().toString());
            }
        });
    }

    @Override
    public void onItemClicked(CommentsDetails commentsDetails) {
        //Toast.makeText(context,commentsDetails.getTeleId(),Toast.LENGTH_LONG).show();
    }
}
