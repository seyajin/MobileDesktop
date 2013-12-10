package com.easymorse;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connectivity {
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			if (networkInfo != null) {
				return networkInfo.isAvailable();
			}
		}
		return false;
	}
}
