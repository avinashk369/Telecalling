package com.techcamino.telecalling.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class PhonecallReceiver extends BroadcastReceiver {

	//The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations

	private static int lastState = TelephonyManager.CALL_STATE_IDLE;
	private static Date callStartTime;
	private static boolean isIncoming,incoming=false,outgoing=false,all=false,recording = false;
	private static String savedNumber;  //because the passed incoming is only valid in ringing
	private String outputFile = null;
	private static MediaRecorder myAudioRecorder = new MediaRecorder();

	@Override
	public void onReceive(Context context, Intent intent) {

		//We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
		if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
			savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
		}
		else{
			String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
			String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
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


			onCallStateChanged(context, state, number);
		}

	}

	//Derived classes should override these to respond to specific events of interest
	protected void onIncomingCallStarted(Context ctx, String number, Date start)
	{
			startRecording(ctx, number,"true");
	}
	protected void onOutgoingCallStarted(Context ctx, String number, Date start)
	{
			startRecording(ctx, number,"false");
	}



	public void startRecording(Context ctx, String number,String status)
	{
		if(!recording)
		{
			SimpleDateFormat  DATE_FORMAT = new SimpleDateFormat("dd-MM-yy  HH-mm-ss");
			SimpleDateFormat  DATEFORMAT = new SimpleDateFormat("dd-MM-yy");
			outputFile = Environment.getExternalStorageDirectory().
					getAbsolutePath() + "/telecalling/"+DATEFORMAT.format(new Date())+"@.mp3";
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CallRecorder/"+DATEFORMAT.format(new Date()));

			if(!file.exists())
				file.mkdirs();

			myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
			myAudioRecorder.setOutputFile(outputFile);

			try {
				myAudioRecorder.prepare();
				myAudioRecorder.start();
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

	public void endRecording()
	{
		try{
			myAudioRecorder.stop();
			myAudioRecorder.release();
		}catch(RuntimeException stopException){
			//handle cleanup here
		}
		myAudioRecorder.reset();
	}

	protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
	{
			endRecording();
	}
	protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
	{
			endRecording();
	}
	protected void onMissedCall(Context ctx, String number, Date start){}

	//Deals with actual events

	//Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
	//Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
	public void onCallStateChanged(Context context, int state, String number) {
		if(lastState == state){
			//No change, debounce extras
			return;
		}
		switch (state) {
		case TelephonyManager.CALL_STATE_RINGING:
			isIncoming = true;
			callStartTime = new Date();
			savedNumber = number;
			onIncomingCallStarted(context, number, callStartTime);
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			//Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
			if(lastState != TelephonyManager.CALL_STATE_RINGING){
				isIncoming = false;
				callStartTime = new Date();
				onOutgoingCallStarted(context, savedNumber, callStartTime);                     
			}
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			//Went to idle-  this is the end of a call.  What type depends on previous state(s)
			if(lastState == TelephonyManager.CALL_STATE_RINGING){
				//Ring but no pickup-  a miss
				onMissedCall(context, savedNumber, callStartTime);
			}
			else if(isIncoming){
				onIncomingCallEnded(context, savedNumber, callStartTime, new Date());                       
			}
			else{
				onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());                                               
			}
			break;
		}
		lastState = state;
	}
}