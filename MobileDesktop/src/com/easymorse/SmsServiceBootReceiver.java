package com.easymorse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SmsServiceBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Intent i = new Intent(context, SmsServiceOptionsActivity.class);                  
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);                
		context.startActivity(i); 
	}
	
	public void registerScreenActionReceiverr(Context mContext){
		
	}

}