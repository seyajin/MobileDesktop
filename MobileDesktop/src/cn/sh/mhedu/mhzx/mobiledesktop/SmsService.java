package cn.sh.mhedu.mhzx.mobiledesktop;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class SmsService extends Service {

	private Timer timer;
	private boolean started;

	private ISmsService.Stub serviceBinder = new ISmsService.Stub() {
		public void stop() throws RemoteException {
			started = false;
			Log.e("sms.service", "sms service stopped.");
		}

		public void start() throws RemoteException {
			started = true;
			Log.e("sms.service", "sms service started.");
		}

		public boolean isStarted() throws RemoteException {
			return started;
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return serviceBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (started) {
					Log.e("sms.service", "send a sms message.");
				}
			}
		}, 0, 1000 * 5);
		Log.e("sms.service", "sms service created.");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
		Log.e("sms.service", "sms service shutdown.");
	}
}