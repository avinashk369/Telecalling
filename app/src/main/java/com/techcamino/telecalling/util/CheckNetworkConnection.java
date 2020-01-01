package com.techcamino.telecalling.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class will check if the device has network connection or not 
 * @author Avinash.k
 *
 */
public class CheckNetworkConnection {
	public static boolean isConnectionAvailable(Context context)
	{
		ConnectivityManager connectivitymanger = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivitymanger != null) {
			NetworkInfo netInfo = connectivitymanger.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()
					&& netInfo.isConnectedOrConnecting() && netInfo.isAvailable())
				return true;
		}
		return false;
	}

}
