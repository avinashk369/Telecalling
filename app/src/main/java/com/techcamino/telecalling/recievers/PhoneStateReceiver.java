package com.techcamino.telecalling.recievers;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techcamino.telecalling.details.MessageDetails;
import com.techcamino.telecalling.rest.APIClient;
import com.techcamino.telecalling.rest.APIInterFace;
import com.techcamino.telecalling.services.RecorderService;
import com.techcamino.telecalling.util.CommonMethods;
import com.techcamino.telecalling.util.Constants;
import com.techcamino.telecalling.util.Security;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by VS00481543 on 25-10-2017.
 */

public class PhoneStateReceiver extends BroadcastReceiver {

    static final String TAG="State";
    public static String phoneNumber;
    public static String name,flags;
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming,incoming=false,outgoing=false,all=false,recording = false;

    private APIInterFace apiService;
    private ProgressDialog dialog;
    private int state;
    private String userId;
    private String folderName;
    private String outputFile = null;
    private static MediaRecorder recorder;

    @Override
    public void onReceive(Context context, Intent intent) {

        dialog = Security.getProgressDialog(context, "Please Wait");
        apiService =
                APIClient.getClient().create(APIInterFace.class);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        try {

            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                phoneNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            }
            else{
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                phoneNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                int state = 0;
                if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                    state = TelephonyManager.CALL_STATE_IDLE;
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                    state = TelephonyManager.CALL_STATE_RINGING;
                }
                Security.savePrefrences(Constants.MOBILE_NUMBER,phoneNumber,context);
                onCallStateChanged(context, state, phoneNumber);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void onCallStateChanged(Context context, int state, String number) {
        if(lastState == state){
            //No change, debounce extras
            return;
        }

        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                Security.savePrefrences(Constants.RECORDING_FLAG,Constants.INCOMING,context);
                startRecording(context, number, state);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    callStartTime = new Date();
                    Security.savePrefrences(Constants.RECORDING_FLAG,Constants.OUTGOING,context);
                    startRecording(context, number, state);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    //Ring but no pickup-  a miss
                    //onMissedCall(context, number, callStartTime);
                    Security.savePrefrences(Constants.RECORDING_FLAG,Constants.MISSED,context);
                    endRecording(context);
                }
                else if(isIncoming){
                    endRecording(context);
                }
                else{
                    endRecording(context);
                }
                break;
        }
        lastState = state;
    }

    private void uploadMediaFiles(Context context,String userId, String folderName) {
        // Map is used to multipart the file using okhttp3.RequestBody
        // Parsing any Media type file
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart(Constants.OWNER_ID, userId);
        builder.addFormDataPart(Constants.MOBILE_NUMBER, Security.getPreference(Constants.MOBILE_NUMBER,context).trim());
        builder.addFormDataPart(Constants.FOLDER_NAME, folderName);
        builder.addFormDataPart(Constants.RECORDING_FLAG, Security.getPreference(Constants.RECORDING_FLAG,context));

        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images
        final File file = new File(Security.getPreference(Constants.RECORDING,context));
        builder.addFormDataPart("file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));

        builder.addFormDataPart(Constants.CALL_DURATION, Security.getDuration(file));

        MultipartBody requestBody = builder.build();

        Call<ResponseBody> call = apiService.uploadMediaFile(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try{
                        file.deleteOnExit();
                        Log.d("DELETED", "Successfully ");
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else  {
                    try {
                        MessageDetails messageDetails = new MessageDetails();
                        Gson gson = new Gson();
                        messageDetails=gson.fromJson(response.errorBody().charStream(), MessageDetails.class);
                        //Security.showError("Error",messageDetails.getMessage(),context);
                        Log.d("ERROR", "Error " + messageDetails.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (dialog.isShowing())
                    dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Log.d("TESTING", "Error " + t.getMessage());
            }
        });
    }

    public void startRecording(Context context, String number,int state) {
        if(!recording)
        {
            recorder = new MediaRecorder();

            String time=new CommonMethods().getTIme();
            String path=new CommonMethods().getPath();
            outputFile =path+"/"+number+"_"+time+".mp3";
            Security.savePrefrences(Constants.RECORDING,outputFile,context);

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(outputFile);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                recorder.prepare();
                recorder.start();
                recording = true;
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void endRecording(Context context) {
        Log.d("ACTION"," stopping recording");
        try {
            if(recorder != null) {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
                recording = false;
                uploadMediaFiles(context,Security.getPreference(Constants.USER_LOGIN_ID,context),Constants.RECORDING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
