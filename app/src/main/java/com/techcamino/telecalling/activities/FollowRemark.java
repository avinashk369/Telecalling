package com.techcamino.telecalling.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.techcamino.telecalling.R;
import com.techcamino.telecalling.adapter.InterestAdapter;
import com.techcamino.telecalling.adapter.SpinnerAdapter;
import com.techcamino.telecalling.adapter.StaffAdapter;
import com.techcamino.telecalling.details.CommentsDetails;
import com.techcamino.telecalling.details.InquiryDetails;
import com.techcamino.telecalling.details.InterestDetails;
import com.techcamino.telecalling.details.MessageDetails;
import com.techcamino.telecalling.details.StaffDetails;
import com.techcamino.telecalling.rest.APIClient;
import com.techcamino.telecalling.rest.APIInterFace;
import com.techcamino.telecalling.util.Constants;
import com.techcamino.telecalling.util.Security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowRemark extends AppCompatActivity {

    private Context context = this;
    private APIInterFace apiService;
    private ProgressDialog dialog;
    private InquiryDetails inquiryDetails;
    private EditText time,nextDate,remarks;
    private Spinner inquiryUpdate,phoneCallOption, interest,staff;
    private ArrayList<InterestDetails> interestDetails;
    private String loginId;
    private HashMap<String,String> queryData;
    private ArrayList<StaffDetails> staffDetails;
    private CommentsDetails commentsDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_remark);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.add_remark);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);

        dialog = Security.getProgressDialog(this, "Please Wait");
        apiService =
                APIClient.getClient().create(APIInterFace.class);

        interestDetails = new ArrayList<>();
        inquiryDetails = new InquiryDetails();
        staffDetails = new ArrayList<>();
        queryData = new HashMap<>();
        commentsDetails = new CommentsDetails();

        time = findViewById(R.id.next_time);
        nextDate = findViewById(R.id.next_date);
        remarks = findViewById(R.id.remark);

        time.setInputType(InputType.TYPE_NULL);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(FollowRemark.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( ((selectedHour< 10) ? "0"+selectedHour : selectedHour) + ":" + ((selectedMinute< 10) ? "0"+selectedMinute : selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        final DatePickerDialog dpDialog = new DatePickerDialog(context, myDateListener, year, month, day);
        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        nextDate.setInputType(InputType.TYPE_NULL);
        nextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpDialog.show();
            }
        });

        interest = findViewById(R.id.interest);
        interest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                commentsDetails.setInterestLevel(interestDetails.get(position).getId().toString().trim());
                /*if(interestDetails.get(position)!=null)
                    inquiryDetails.setInterestLevel(interestDetails.get(position).getId().toString().trim());*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        staff = findViewById(R.id.staff_spinner);
        staff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                commentsDetails.setStaffId(staffDetails.get(position).getStaffId().toString().trim());
                commentsDetails.setStaffName(staffDetails.get(position).getFirstName()+" "+ staffDetails.get(position).getLastName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        phoneCallOption = findViewById(R.id.phone_call);
        inquiryUpdate = findViewById(R.id.inquiry_spinner);
        String[] roleList = getResources().getStringArray(R.array.phone_call_option);
        String[] inquiryUpdateList = getResources().getStringArray(R.array.inquiry_updates);


        SpinnerAdapter roleAdpater = new SpinnerAdapter(context, Arrays.asList(roleList));
        //roleAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneCallOption.setAdapter(roleAdpater);
        phoneCallOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //role = parent.getSelectedItem().toString();
                commentsDetails.setPhoneOption(parent.getSelectedItem().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SpinnerAdapter inquiryAdapter = new SpinnerAdapter(context, Arrays.asList(inquiryUpdateList));
        inquiryUpdate.setAdapter(inquiryAdapter);
        inquiryUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //role = parent.getSelectedItem().toString();
                commentsDetails.setInquiryUpdate(parent.getSelectedItem().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commentsDetails.setTeleId(inquiryDetails.getTeleId());
                commentsDetails.setComment(remarks.getText().toString().trim());
                commentsDetails.setFollowupDate(nextDate.getText().toString().trim());
                commentsDetails.setFollowupTime(time.getText().toString().trim());
                saveFollowup(commentsDetails);
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

                } else {

                    fmonth = "0" + monthOfYear;
                    month = Integer.parseInt(fmonth) + 1;
                    String paddedMonth = String.format("%02d", month);
                    nextDate.setText( year+ "-" + paddedMonth + "-" + dayOfMonth);
                }

                //getUserLocation(user.getId(),searchDate.getText().toString().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };

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
        //queryData.put(Constants.INQUIRY_ID,inquiryDetails.getTeleId());
        getInterestList();
        getStaffList();
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

    public void getStaffList(){
        dialog.show();
        Call<ArrayList<StaffDetails>> call = apiService.getStaffList();
        call.enqueue(new Callback<ArrayList<StaffDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<StaffDetails>> call, Response<ArrayList<StaffDetails>> response) {
                if (response.isSuccessful()) {
                    staffDetails = response.body();
                    StaffAdapter customAdapter=new StaffAdapter(context,staffDetails);
                    staff.setAdapter(customAdapter);
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
            public void onFailure(Call<ArrayList<StaffDetails>> call, Throwable t) {
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
                        Intent intent = new Intent(FollowRemark.this, ViewInquiry.class);
                        intent.putExtra(Constants.INQUIRY_DETAIL, inquiryDetails);
                        intent.putExtra(Constants.USER_LOGIN_ID, loginId);
                        startActivity(intent);
                        finish();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                Intent intent = new Intent(FollowRemark.this, ViewInquiry.class);
                intent.putExtra(Constants.INQUIRY_DETAIL, inquiryDetails);
                intent.putExtra(Constants.USER_LOGIN_ID, loginId);
                startActivity(intent);
                finish();
            }
        });

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void saveFollowup(CommentsDetails comments){
        dialog.show();
        Call<CommentsDetails> call = apiService.saveInquiryComments(comments);
        call.enqueue(new Callback<CommentsDetails>() {
            @Override
            public void onResponse(Call<CommentsDetails> call, Response<CommentsDetails> response) {
                if (response.isSuccessful()) {
                    commentsDetails = response.body();
                    showSnackbar(Constants.SUCCESS);
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
            public void onFailure(Call<CommentsDetails> call, Throwable t) {
                Log.d("Failure", t.getMessage().toString());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("Failure",t.getMessage().toString());
            }
        });
    }
}
