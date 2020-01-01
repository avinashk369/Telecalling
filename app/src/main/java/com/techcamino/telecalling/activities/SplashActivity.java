package com.techcamino.telecalling.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.techcamino.telecalling.R;
import com.techcamino.telecalling.rest.APIInterFace;
import com.techcamino.telecalling.util.CheckNetworkConnection;
import com.techcamino.telecalling.util.Constants;
import com.techcamino.telecalling.util.Security;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private boolean isNetwork = false;
    private Context context = this;
    private APIInterFace apiService;
    private ProgressDialog dialog;
    boolean checkResume=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(checkPermission()) {
            Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();
            if(checkResume==false) {
            }
            isNetwork = CheckNetworkConnection.isConnectionAvailable(SplashActivity.this);
            handleSplash();
        }


    }

    /* This Function will handle splash request*/
    public void handleSplash()
    {


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                boolean login = Security.getBooleanPref(Constants.LOGIN_ID,SplashActivity.this);
                String userId = Security.getPreference(Constants.USER_LOGIN_ID,SplashActivity.this);
                if(login && !userId.equalsIgnoreCase("missing")){
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.putExtra(Constants.USER_LOGIN_ID, userId);
                    startActivity(i);
                }else {
                    // Start your app main activity
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Check", "onResume: ");
        if(checkPermission()) {
            Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();
            if(checkResume==false) {
            }
        }
    }

    protected void onPause()
    {
        super.onPause();
        SharedPreferences pref3= PreferenceManager.getDefaultSharedPreferences(this);
        if(pref3.getBoolean("pauseStateVLC",false)) {
            checkResume = true;
            pref3.edit().putBoolean("pauseStateVLC",false).apply();
        }
        else
            checkResume=false;
    }
    private boolean checkPermission()
    {
        int i=0;
        String[] perm={Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_CONTACTS};
        List<String> reqPerm=new ArrayList<>();

        for(String permis:perm) {
            int resultPhone = ContextCompat.checkSelfPermission(SplashActivity.this,permis);
            if(resultPhone== PackageManager.PERMISSION_GRANTED)
                i++;
            else {
                reqPerm.add(permis);
            }
        }

        if(i==5)
            return true;
        else
            return requestPermission(reqPerm);
    }



    private boolean requestPermission(List<String> perm)
    {
        // String[] permissions={Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        String[] listReq=new String[perm.size()];
        listReq=perm.toArray(listReq);
        for(String permissions:listReq) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,permissions)) {
                Toast.makeText(getApplicationContext(), "Phone Permissions needed for " + permissions, Toast.LENGTH_LONG);
            }
        }

        ActivityCompat.requestPermissions(SplashActivity.this, listReq, 1);


        return false;
    }


    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults)
    {
        switch(requestCode)
        {
            case 1:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(),"Permission Granted to access Phone calls",Toast.LENGTH_LONG);
                else
                    Toast.makeText(getApplicationContext(),"You can't access Phone calls",Toast.LENGTH_LONG);
                break;
        }

    }
}
