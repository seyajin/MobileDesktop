package com.easymorse;

import com.easymorse.util.Constant;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class SmsServiceOptionsActivity extends Activity {
	
	private static final String FILE_FIRST_OPEN = "file_first_open";
	private static final String KEY_IS_FIRST = "key_is_first";

	private ISmsService mSmsService;

	private ServiceConnection serviceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			mSmsService = (ISmsService) service;
		}
		public void onServiceDisconnected(ComponentName name) {
			mSmsService = null;
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		bindService(new Intent("com.easymorse.SmsService"), serviceConnection, BIND_AUTO_CREATE);
		setContentView(R.layout.main_entry);
		initView();
		
		setDeviceCheckTime();
	}
	
	/**
	 * Save the first use time when login
	 */
	private void setDeviceCheckTime() {
		SharedPreferences preferences = getSharedPreferences(FILE_FIRST_OPEN, Context.MODE_PRIVATE);

		boolean isFirstLogin = preferences.getBoolean(KEY_IS_FIRST, true);
		if (isFirstLogin) {
			SharedPreferences preferencesDeviceStatus = getSharedPreferences(Constant.FILE_DEVICE_STATUS, Context.MODE_PRIVATE);
			Editor editorDeviceStatus = preferencesDeviceStatus.edit();
			editorDeviceStatus.putLong(Constant.KEY_STATUS_CHECK_TIME, System.currentTimeMillis());
			
			Editor editor = preferences.edit();
			editor.putBoolean(KEY_IS_FIRST, false);
			editor.commit();
		}
	}
	
	private void initView() {
		View view = findViewById(R.id.entry_btn);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(SmsServiceOptionsActivity.this, CategoryActivity.class);
				startActivity(intent);
			}
		});
		
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		unbindService(serviceConnection);
	}
	
	@Override
	public void onBackPressed() {
		// Do nothing!
	}
}