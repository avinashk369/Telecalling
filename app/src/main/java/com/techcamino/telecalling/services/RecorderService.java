package com.techcamino.telecalling.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by VS00481543 on 30-10-2017.
 */

public class RecorderService extends Service {

    static final String TAGS=" Inside Service";
    public static String name;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent,int flags,int startId) {
        return START_NOT_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAGS, "onDestroy: "+"Recording stopped");

    }
}
