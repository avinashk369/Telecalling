package com.techcamino.telecalling.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.techcamino.telecalling.R;
import com.techcamino.telecalling.adapter.InterestAdapter;
import com.techcamino.telecalling.adapter.StateCityAdapter;
import com.techcamino.telecalling.details.InquiryDetails;
import com.techcamino.telecalling.details.InterestDetails;
import com.techcamino.telecalling.details.MessageDetails;
import com.techcamino.telecalling.details.StateCityDetails;
import com.techcamino.telecalling.rest.APIClient;
import com.techcamino.telecalling.rest.APIInterFace;
import com.techcamino.telecalling.util.Constants;
import com.techcamino.telecalling.util.Security;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity {

    private Context context = this;
    private APIInterFace apiService;
    private ProgressDialog dialog;
    private EditText nextDate;
    private Spinner interest,city,state;
    private ArrayList<InterestDetails> interestDetails;
    private ArrayList<StateCityDetails> stateDetails;
    private ArrayList<StateCityDetails> cityDetails;
    private HashMap<String,String> queryData;
    private boolean inquiry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);

        dialog = Security.getProgressDialog(this, "Please Wait");
        apiService =
                APIClient.getClient().create(APIInterFace.class);

        queryData = new HashMap<>();

        interestDetails = new ArrayList<>();
        stateDetails = new ArrayList<>();
        cityDetails =  new ArrayList<>();


        nextDate = findViewById(R.id.next_date);
        state = findViewById(R.id.state_spinner);
        city = findViewById(R.id.city_spinner);
        interest = findViewById(R.id.interest_spinner);


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        final DatePickerDialog dpDialog = new DatePickerDialog(context, myDateListener, year, month, day);
        //dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // block previous days
        //dpDialog.getDatePicker().setMaxDate(cldr.getTimeInMillis()); // block future days

        nextDate.setInputType(InputType.TYPE_NULL);
        nextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpDialog.show();
            }
        });

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getStateList(stateDetails.get(position).getParent(),false);

                if(stateDetails.get(position)!=null
                        && !stateDetails.get(position).getParent().equalsIgnoreCase(Constants.SPINNER_STATE)) {
                    queryData.put(Constants.STATE,stateDetails.get(position).getParent().toString().trim());
                }

                /*if(stateDetails.get(position)!=null)
                    queryData.put(Constants.STATE,stateDetails.get(position).getParent().toString().trim());*/
                /*if(stateDetails.get(position)!=null)
                    inquiryDetails.setState(stateDetails.get(position).getParent().toString().trim());*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(cityDetails.get(position)!=null
                        && !cityDetails.get(position).getState().equalsIgnoreCase(Constants.SPINNER_CITY)) {
                    queryData.put(Constants.CITY,cityDetails.get(position).getState().toString().trim());
                }
                /*if(stateDetails.get(position)!=null)
                    inquiryDetails.setCity(stateDetails.get(position).getState().toString().trim());*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        interest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(interestDetails.get(position)!=null
                        && !interestDetails.get(position).getIntsLevel().equalsIgnoreCase(Constants.SPINNER_INTEREST)) {
                    queryData.put(Constants.INTEREST_LEVEL, interestDetails.get(position).getId().toString().trim());
                }
                /*if(interestDetails.get(position)!=null)
                    inquiryDetails.setInterestLevel(interestDetails.get(position).getId().toString().trim());*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inquiry) {
                    Intent inquiryList = new Intent(FilterActivity.this, InquiryList
                            .class);
                    inquiryList.putExtra(Constants.INQUIRY_DETAIL,queryData);
                    startActivity(inquiryList);
                    finish();
                }
                else{
                    Intent inquiryList = new Intent(FilterActivity.this, FollowupList
                            .class);
                    inquiryList.putExtra(Constants.INQUIRY_DETAIL,queryData);
                    startActivity(inquiryList);
                    finish();
                }


            }
        });

    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth) {
            Log.e("onDateSet()", "arg0 = [" + arg0 + "], year = [" + year + "], monthOfYear = [" + monthOfYear + "], dayOfMonth = [" + dayOfMonth + "]");
            String fmonth, fDate;
            int month;
            try {
                if (monthOfYear < 10 && dayOfMonth < 10) {

                    fmonth = "0" + monthOfYear;
                    month = Integer.parseInt(fmonth) + 1;
                    fDate = "0" + dayOfMonth;
                    String paddedMonth = String.format("%02d", month);
                    nextDate.setText(year + "-" + paddedMonth + "-" + fDate);
                    queryData.put(Constants.CREATE_DATE,nextDate.getText().toString().trim());
                } else {

                    fmonth = "0" + monthOfYear;
                    month = Integer.parseInt(fmonth) + 1;
                    String paddedMonth = String.format("%02d", month);
                    nextDate.setText( year+ "-" + paddedMonth + "-" + dayOfMonth);
                    queryData.put(Constants.CREATE_DATE,nextDate.getText().toString().trim());
                }

                //getUserLocation(user.getId(),searchDate.getText().toString().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };


    public void getInterestList(){
        dialog.show();
        Call<ArrayList<InterestDetails>> call = apiService.getInterestList();
        call.enqueue(new Callback<ArrayList<InterestDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<InterestDetails>> call, Response<ArrayList<InterestDetails>> response) {
                if (response.isSuccessful()) {
                    interestDetails = response.body();
                    InterestDetails intr = new InterestDetails();
                    intr.setIntsLevel(Constants.SPINNER_INTEREST);
                    interestDetails.add(0,intr);
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


    public void getStateList(String stateId,final boolean stateList){
        dialog.show();
        Call<ArrayList<StateCityDetails>> call = apiService.getStateList(stateId);
        call.enqueue(new Callback<ArrayList<StateCityDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<StateCityDetails>> call, Response<ArrayList<StateCityDetails>> response) {
                if (response.isSuccessful()) {


                    if(stateList) {
                        stateDetails = response.body();

                        StateCityDetails stateCity = new StateCityDetails();
                        stateCity.setParent(Constants.SPINNER_STATE);
                        stateDetails.add(0,stateCity);

                        StateCityAdapter customAdapter=new StateCityAdapter(context,stateDetails,true);
                        state.setAdapter(customAdapter);
                    }
                    else {
                        cityDetails = response.body();

                        StateCityDetails stateCity = new StateCityDetails();
                        stateCity.setState(Constants.SPINNER_CITY);
                        cityDetails.add(0,stateCity);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (inquiry) {

                    Intent inquiryList = new Intent(FilterActivity.this, InquiryList
                            .class);
                    inquiryList.putExtra(Constants.INQUIRY_DETAIL,queryData);
                    startActivity(inquiryList);
                    finish();
                }
                else{
                    Intent inquiryList = new Intent(FilterActivity.this, FollowupList
                            .class);
                    inquiryList.putExtra(Constants.INQUIRY_DETAIL,queryData);
                    startActivity(inquiryList);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        inquiry = getIntent().getBooleanExtra(Constants.INQUIRY_DETAIL,false);
        getInterestList();
        getStateList(null,true);
    }
}
