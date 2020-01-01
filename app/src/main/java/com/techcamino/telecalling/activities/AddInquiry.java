package com.techcamino.telecalling.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.techcamino.telecalling.R;
import com.techcamino.telecalling.adapter.InterestAdapter;
import com.techcamino.telecalling.adapter.StateCityAdapter;
import com.techcamino.telecalling.details.InquiryDetails;
import com.techcamino.telecalling.details.InterestDetails;
import com.techcamino.telecalling.details.LoginDetails;
import com.techcamino.telecalling.details.MessageDetails;
import com.techcamino.telecalling.details.StateCityDetails;
import com.techcamino.telecalling.rest.APIClient;
import com.techcamino.telecalling.rest.APIInterFace;
import com.techcamino.telecalling.util.Constants;
import com.techcamino.telecalling.util.ErrorConstants;
import com.techcamino.telecalling.util.Role;
import com.techcamino.telecalling.util.Security;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInquiry extends AppCompatActivity {

    private Context context = this;
    private APIInterFace apiService;
    private ProgressDialog dialog;
    private RecyclerView myStoreView;
    private ArrayList<StateCityDetails> stateDetails;
    private ArrayList<StateCityDetails> cityDetails;
    private ArrayList<InterestDetails> interestDetails;
    private Spinner city,state, interest;
    private EditText name,companyName,mobile,remarks,country,data_status,email;
    private InquiryDetails inquiryDetails;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
    String emailPattern_two = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inquiry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.add_inquiry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);

        dialog = Security.getProgressDialog(this, "Please Wait");
        apiService =
                APIClient.getClient().create(APIInterFace.class);

        stateDetails = new ArrayList<>();
        cityDetails =  new ArrayList<>();
        interestDetails = new ArrayList<>();
        inquiryDetails = new InquiryDetails();

        state = findViewById(R.id.state_spinner);
        city = findViewById(R.id.city_spinner);
        interest = findViewById(R.id.interest_spinner);
        name = findViewById(R.id.name);
        companyName = findViewById(R.id.company_name);
        mobile = findViewById(R.id.mobile_number);
        remarks = findViewById(R.id.remark);
        country = findViewById(R.id.country);
        email = findViewById(R.id.email);
        data_status = findViewById(R.id.data_status);

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getStateList(stateDetails.get(position).getParent(),false);
                if(stateDetails.get(position)!=null)
                    inquiryDetails.setState(stateDetails.get(position).getParent().toString().trim());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(cityDetails.get(position)!=null)
                    inquiryDetails.setCity(cityDetails.get(position).getState().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        interest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(interestDetails.get(position)!=null)
                    inquiryDetails.setInterestLevel(interestDetails.get(position).getId().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateField()) {
                    return;
                }
                inquiryDetails.setName(name.getText().toString().trim());
                inquiryDetails.setCompName(companyName.getText().toString().trim());
                inquiryDetails.setMobileNo(mobile.getText().toString().trim());
                inquiryDetails.setComment(remarks.getText().toString().trim());
                inquiryDetails.setEmail(email.getText().toString().trim());
                inquiryDetails.setDataStatus(data_status.getText().toString().trim());
                inquiryDetails.setStaffId(userId);
                saveInquiry(inquiryDetails);
            }
        });

    }

    private boolean validateField(){

        if(name.getText().toString().isEmpty() || name.getText().toString().length() < 1){
            name.setError(ErrorConstants.NAME_ERROR);
            name.requestFocus();
            return false;
        }

        if(companyName.getText().toString().trim().isEmpty() || companyName.getText().toString().trim().length() < 1 ){
            companyName.setError(ErrorConstants.COMPANY_NAME_ERROR);
            companyName.requestFocus();
            return false;
        }

        if(email.getText().toString().trim().isEmpty() || email.getText().toString().trim().length() < 1 ){
            email.setError(ErrorConstants.EMAIL_ERROR);
            email.requestFocus();
            return false;
        }

        if (!email.getText().toString().trim().matches(emailPattern) && !email.getText().toString().trim().matches(emailPattern_two)) {
            email.setError(ErrorConstants.EMAIL_ERROR);
            email.requestFocus();
            return false;
        }

        if(mobile.getText().toString().trim().isEmpty() || mobile.getText().toString().trim().length() < 10 ){
            mobile.setError(ErrorConstants.MOBILE_ERROR);
            mobile.requestFocus();
            return false;
        }

        if(data_status.getText().toString().trim().isEmpty() || data_status.getText().toString().trim().length() < 1 ){
            data_status.setError(ErrorConstants.REMARK_ERROR);
            data_status.requestFocus();
            return false;
        }

        if(remarks.getText().toString().trim().isEmpty() || remarks.getText().toString().trim().length() < 1 ){
            remarks.setError(ErrorConstants.REMARK_ERROR);
            remarks.requestFocus();
            return false;
        }

        return true;
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
        userId = getIntent().getStringExtra(Constants.USER_LOGIN_ID);
        getStateList(null,true);
        getInterestList();
    }


    public void saveInquiry(InquiryDetails inquiry){
        dialog.show();
        Call<InquiryDetails> call = apiService.saveInquiryDetail(inquiry);
        call.enqueue(new Callback<InquiryDetails>() {
            @Override
            public void onResponse(Call<InquiryDetails> call, Response<InquiryDetails> response) {
                if (response.isSuccessful()) {

                    showSnackbar(Constants.INQUIRY_SUCCESS);
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
            public void onFailure(Call<InquiryDetails> call, Throwable t) {
                Log.d("Failure", t.getMessage().toString());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("Failure",t.getMessage().toString());
            }
        });
    }

    public void showSnackbar(String message){
        View contextView = findViewById(R.id.context_view);

        Snackbar snackbar = Snackbar
                .make(contextView, message, Snackbar.LENGTH_LONG)
                .setAction(Constants.CLOSE, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(AddInquiry.this, MainActivity.class);
                        //i.putExtra(Constants.USER_DETAIL, user);
                        i.putExtra(Constants.USER_LOGIN_ID, Security.getPreference(Constants.USER_LOGIN_ID,AddInquiry.this));
                        startActivity(i);
                        finish();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                Intent i = new Intent(AddInquiry.this, MainActivity.class);
                //i.putExtra(Constants.USER_DETAIL, user);
                i.putExtra(Constants.USER_LOGIN_ID, Security.getPreference(Constants.USER_LOGIN_ID,AddInquiry.this));
                startActivity(i);
                finish();
            }
        });

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void getStateList(String stateId,final boolean stateList){
        dialog.show();
        Call<ArrayList<StateCityDetails>> call = apiService.getStateList(stateId);
        call.enqueue(new Callback<ArrayList<StateCityDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<StateCityDetails>> call, Response<ArrayList<StateCityDetails>> response) {
                if (response.isSuccessful()) {


                    if(stateList) {
                        stateDetails = response.body();
                        StateCityAdapter customAdapter=new StateCityAdapter(context,stateDetails,true);
                        state.setAdapter(customAdapter);
                    }
                    else {
                        cityDetails = response.body();
                        StateCityAdapter customAdapter=new StateCityAdapter(context,cityDetails,false);
                        city.setAdapter(customAdapter);
                    }
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
            public void onFailure(Call<ArrayList<StateCityDetails>> call, Throwable t) {
                Log.d("Failure", t.getMessage().toString());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("Failure",t.getMessage().toString());
            }
        });
    }

    public void getInterestList(){
        dialog.show();
        Call<ArrayList<InterestDetails>> call = apiService.getInterestList();
        call.enqueue(new Callback<ArrayList<InterestDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<InterestDetails>> call, Response<ArrayList<InterestDetails>> response) {
                if (response.isSuccessful()) {
                    interestDetails = response.body();
                    InterestAdapter customAdapter=new InterestAdapter(context,interestDetails);
                    interest.setAdapter(customAdapter);
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
            public void onFailure(Call<ArrayList<InterestDetails>> call, Throwable t) {
                Log.d("Failure", t.getMessage().toString());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("Failure",t.getMessage().toString());
            }
        });
    }
}
