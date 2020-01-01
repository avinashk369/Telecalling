package com.techcamino.telecalling.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.techcamino.telecalling.R;
import com.techcamino.telecalling.details.LoginDetails;
import com.techcamino.telecalling.details.MessageDetails;
import com.techcamino.telecalling.rest.APIClient;
import com.techcamino.telecalling.rest.APIInterFace;
import com.techcamino.telecalling.util.Constants;
import com.techcamino.telecalling.util.ErrorConstants;
import com.techcamino.telecalling.util.Security;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextMobile,editTextPassword;
    private APIInterFace apiService;
    private ProgressDialog dialog;
    private Context context = this;
    private TextView accessMsg;
    private HashMap<String,String> loginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginData = new HashMap<>();
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextPassword = findViewById(R.id.edittextpassword);

        dialog = Security.getProgressDialog(this, "Please Wait");
        //dialog = Security.loadDialog(LoginActivity.this);
        apiService =
                APIClient.getClient().create(APIInterFace.class);


        accessMsg = findViewById(R.id.verify);

        TextInputLayout input_layout_password = (TextInputLayout) findViewById(R.id.password_wdgt);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.montserrat_regular);
        input_layout_password.setTypeface(typeface);

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //test();
                String mobile = editTextMobile.getText().toString().trim();
                String password = editTextPassword
                        .getText().toString().trim();
                loginData.put(Constants.LOGIN_ID,mobile);
                loginData.put(Constants.LOGIN_PASS,password);
                test();
                /*if (!validateField()) {
                    return;
                }

                loginUser(loginData);*/
            }
        });
    }

    public void test(){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private boolean validateField(){

        if(editTextMobile.getText().toString().isEmpty() || editTextMobile.getText().toString().length() < 1){
            editTextMobile.setError(ErrorConstants.LOGIN_ID_ERROR);
            editTextMobile.requestFocus();
            return false;
        }

        if(editTextPassword.getText().toString().trim().isEmpty() || editTextPassword.getText().toString().trim().length() < 1 ){
            editTextPassword.setError(ErrorConstants.LOGIN_PWD_ERROR);
            editTextPassword.requestFocus();
            return false;
        }

        return true;
    }

    public void loginUser(HashMap<String,String> user){
        dialog.show();
        Call<LoginDetails> call = apiService.loginUser(user);
        call.enqueue(new Callback<LoginDetails>() {
            @Override
            public void onResponse(Call<LoginDetails> call, Response<LoginDetails> response) {
                if (response.isSuccessful()) {

                    LoginDetails loginDetails = new LoginDetails();
                    loginDetails = response.body();

                    accessMsg.setVisibility(View.GONE);

                    CheckBox remember = findViewById(R.id.remember);
                    if(remember.isChecked()) {
                        Security.saveBooleanPrefrences(Constants.LOGIN_ID, true, context);
                    }

                    Security.savePrefrences(Constants.USER_LOGIN_ID,loginDetails.getUserIdFk(),context);

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    //i.putExtra(Constants.USER_DETAIL, user);
                    i.putExtra(Constants.USER_LOGIN_ID, Security.getPreference(Constants.USER_LOGIN_ID,LoginActivity.this));
                    startActivity(i);
                    finish();

                } else {
                    try {
                        MessageDetails messageDetails = new MessageDetails();
                        Gson gson = new Gson();
                        messageDetails=gson.fromJson(response.errorBody().charStream(),MessageDetails.class);
                        //Security.showError("Error",messageDetails.getMessage(),context);
                        accessMsg.setVisibility(View.VISIBLE);
                        accessMsg.setText(messageDetails.getMessage());

                        /*if(messageDetails.getStatus().equalsIgnoreCase(Constants.INVALID_REQUEST+""))
                            accessMsg.setText(R.string.unauthorised_access);
                        if(messageDetails.getStatus().equalsIgnoreCase(Constants.UNAUTHORISED_ACCESS+""))
                            accessMsg.setVisibility(View.VISIBLE);*/

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
                Security.showError("Error",t.getMessage().toString(),context);
            }
        });
    }
}
