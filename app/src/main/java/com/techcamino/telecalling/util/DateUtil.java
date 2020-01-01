package com.techcamino.telecalling.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Thinkpad on 20-10-2016.
 */

public class DateUtil {

    public static String getCurrentTimeStamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        return dateFormat.format(calendar.getTime());
    }
}
