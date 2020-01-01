package com.techcamino.telecalling.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.techcamino.telecalling.R;
import com.techcamino.telecalling.adapter.FollowupAdapter;
import com.techcamino.telecalling.adapter.InquiryAdapter;
import com.techcamino.telecalling.adapter.TopFollowupAdapter;
import com.techcamino.telecalling.adapter.TopInquiryAdapter;
import com.techcamino.telecalling.details.FollowupDetails;
import com.techcamino.telecalling.details.InquiryDetails;
import com.techcamino.telecalling.details.LoginDetails;
import com.techcamino.telecalling.details.MessageDetails;
import com.techcamino.telecalling.rest.APIClient;
import com.techcamino.telecalling.rest.APIInterFace;
import com.techcamino.telecalling.util.CircularImageView;
import com.techcamino.telecalling.util.Constants;
import com.techcamino.telecalling.util.Role;
import com.techcamino.telecalling.util.Security;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TopInquiryAdapter.CustomDialogListener {

    private Context context = this;
    private RecyclerView folloupItems,inquiryItem;
    private CircularImageView userImage;
    private TextView userName,userRole,logOut;
    private String userId;
    private APIInterFace apiService;
    private ProgressDialog dialog;
    private Dialog loading;

    private ArrayList<InquiryDetails> inquiryDetailsArrayList;
    private RecyclerView inquiryList;
    private HashMap<String,String> queryData;
    private HashMap<String,String> headerInquiry;
    private HashMap<String,String> headerFollowup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dialog = Security.getProgressDialog(this, "Please Wait");
        //loading = Security.loadDialog(MainActivity.this);
        apiService =
                APIClient.getClient().create(APIInterFace.class);

        queryData = new HashMap<>();
        headerInquiry = new HashMap<>();
        headerFollowup = new HashMap<>();

        folloupItems = (RecyclerView) findViewById(R.id.followup_list);
        inquiryItem = (RecyclerView) findViewById(R.id.inquiry_list);
        logOut = findViewById(R.id.logout);
        userImage = findViewById(R.id.rateruserprofileImage);

        inquiryDetailsArrayList = new ArrayList<>();




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        // get user name and email textViews
        userName = headerView.findViewById(R.id.user_name);
        userRole = headerView.findViewById(R.id.user_role);
        userImage = headerView.findViewById(R.id.rateruserprofileImage);


        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Security.deletePrefrences(context);
                Security.saveBooleanPrefrences(Constants.LOGIN_ID, false, context);
                Intent logout = new Intent(MainActivity.this, LoginActivity
                        .class);
                startActivity(logout);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userId = getIntent().getStringExtra(Constants.USER_LOGIN_ID);
        queryData.put(Constants.USER_LOGIN_ID,userId);
        getUserDetail(userId);
        getTopInquiry(headerInquiry);
        getTopFollowup(headerFollowup);
    }


    public void getUserDetail(String userId){
        dialog.show();
        Call<LoginDetails> call = apiService.getUser(userId);
        call.enqueue(new Callback<LoginDetails>() {
            @Override
            public void onResponse(Call<LoginDetails> call, Response<LoginDetails> response) {
                if (response.isSuccessful()) {

                    LoginDetails loginDetails = new LoginDetails();
                    loginDetails = response.body();

                    try {
                        userName.setText(loginDetails.getName());
                        userRole.setText(Role.getRole(Integer.valueOf(loginDetails.getUserType())));
                    } catch (Exception e) {
                        e.printStackTrace();
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
            public void onFailure(Call<LoginDetails> call, Throwable t) {
                Log.d("Failure", t.getMessage().toString());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                //Security.showError("Error",t.getMessage().toString(),context);
                showSnackbar(Constants.NETWORK_LOST);
            }
        });
    }

    public void showSnackbar(String message){
        /*View contextView = findViewById(R.id.context_view);

        Snackbar snackbar = Snackbar
                .make(contextView, message, Snackbar.LENGTH_LONG)
                .setAction(Constants.CLOSE, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();*/
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_followup_list:
                    Intent followup = new Intent(MainActivity.this, FollowupList
                            .class);
                    followup.putExtra(Constants.INQUIRY_DETAIL,queryData);
                    startActivity(followup);
                    return true;
                case R.id.navigation_add_inquiry:
                    Intent inquiry = new Intent(MainActivity.this, AddInquiry
                            .class);
                    inquiry.putExtra(Constants.USER_LOGIN_ID,userId);
                    startActivity(inquiry);
                    return true;
                case R.id.navigation_inquiry_list:
                    Intent inquiryList = new Intent(MainActivity.this, InquiryList
                            .class);
                    inquiryList.putExtra(Constants.INQUIRY_DETAIL,queryData);
                    startActivity(inquiryList);
                    return true;
            }
            return false;
        }
    };


    public void getTopInquiry(HashMap<String,String> headerMap){
        dialog.show();
        Call<ArrayList<InquiryDetails>> call = apiService.getTopInquiryList(headerMap);
        call.enqueue(new Callback<ArrayList<InquiryDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<InquiryDetails>> call, Response<ArrayList<InquiryDetails>> response) {
                if (response.isSuccessful()) {

                    inquiryDetailsArrayList = response.body();

                    TopInquiryAdapter walletAdapter = new TopInquiryAdapter(context,inquiryDetailsArrayList);
                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
                    inquiryItem.setLayoutManager(mLayoutManager1);
                    inquiryItem.setAdapter(walletAdapter);


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
            public void onFailure(Call<ArrayList<InquiryDetails>> call, Throwable t) {
                Log.d("Failure", t.getMessage().toString());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("Failure",t.getMessage().toString());
            }
        });
    }

    public void getTopFollowup(HashMap<String,String> headerMap){
        dialog.show();
        Call<ArrayList<InquiryDetails>> call = apiService.getTopFollowupList(headerMap);
        call.enqueue(new Callback<ArrayList<InquiryDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<InquiryDetails>> call, Response<ArrayList<InquiryDetails>> response) {
                if (response.isSuccessful()) {

                    inquiryDetailsArrayList = response.body();

                    TopInquiryAdapter walletAdapter = new TopInquiryAdapter(context,inquiryDetailsArrayList);
                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
                    folloupItems.setLayoutManager(mLayoutManager1);
                    folloupItems.setAdapter(walletAdapter);


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
            public void onFailure(Call<ArrayList<InquiryDetails>> call, Throwable t) {
                Log.d("Failure", t.getMessage().toString());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("Failure",t.getMessage().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_message) {
            // Handle the camera action
            whatsapp(MainActivity.this,Constants.WHATSAPP_NUMBER);
        } else if (id == R.id.nav_call_records) {
            Intent records = new Intent(MainActivity.this, CallRecords.class);
            startActivity(records);
        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void whatsapp(Activity activity, String phone) {
        //String formattedNumber = Util.formatPhone(phone);
        try{
            /*Intent sendIntent =new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,"");
            sendIntent.setType("text/plain");
            sendIntent.putExtra("jid", phone +"@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(sendIntent);*/

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+phone +"&text="+Constants.WHATSAPP_MESSAGE));
            startActivity(intent);
        }
        catch(Exception e)
        {
            Toast.makeText(activity,"Error/n"+ e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onImageClicked(InquiryDetails inquiry) {
        Toast.makeText(context,inquiry.getInterestDetails().getIntsLevel(),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, ViewInquiry
                .class);
        intent.putExtra(Constants.INQUIRY_DETAIL, inquiry);
        intent.putExtra(Constants.USER_LOGIN_ID, userId);
        startActivity(intent);
    }
}
