package com.techcamino.telecalling.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.techcamino.telecalling.R;
import com.techcamino.telecalling.rest.APIClient;
import com.techcamino.telecalling.rest.APIInterFace;
import com.techcamino.telecalling.util.Security;

import java.util.HashMap;

public class ViewFollowUp extends AppCompatActivity {

    private Context context = this;
    private APIInterFace apiService;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_follow_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.inquiry_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);

        dialog = Security.getProgressDialog(this, "Please Wait");
        apiService =
                APIClient.getClient().create(APIInterFace.class);


    }
}
