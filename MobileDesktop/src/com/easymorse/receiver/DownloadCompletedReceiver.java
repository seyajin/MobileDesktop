package com.easymorse.receiver;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class DownloadCompletedReceiver extends BroadcastReceiver {
	private static final String TAG = "DownloadCompletedReceiver";
	
	private DownloadManager mDownloadManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "download complete!!!");
		mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		
		if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
			long enqueue = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			
			Query q = new Query();
			q.setFilterById(enqueue);
			Cursor cursor = mDownloadManager.query(q);
			
			if (cursor.moveToFirst()) {
				int downloadStatusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
				if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(downloadStatusIndex)) {
					String localUrl = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
					Log.d(TAG, "localUrl = " + localUrl);
					String extension = MimeTypeMap.getFileExtensionFromUrl(localUrl);
					String MIMEType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
					Log.d(TAG, "MIMEType = " + MIMEType);
					
					try {
						Intent i = new Intent(Intent.ACTION_VIEW); 
						i.setDataAndType(Uri.parse(localUrl), MIMEType); 
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);
					} catch (Exception e) {
						Toast.makeText(context, "对不起，无法打开该应用", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(context, "对不起，该文件下载失败，请稍候再试。", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
}
