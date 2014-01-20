package cn.sh.mhedu.mhzx.mobiledesktop;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.InputSource;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sh.mhedu.mhzx.mobiledesktop.bean.JsonAppInfo;
import cn.sh.mhedu.mhzx.mobiledesktop.bean.JsonAppPackageResponse;
import cn.sh.mhedu.mhzx.mobiledesktop.bean.JsonContent;
import cn.sh.mhedu.mhzx.mobiledesktop.parser.JsonParser;
import cn.sh.mhedu.mhzx.mobiledesktop.parser.ParserBySAX;
import cn.sh.mhedu.mhzx.mobiledesktop.util.Constant;
import cn.sh.mhedu.mhzx.mobiledesktop.utility.Constants;
import cn.sh.mhedu.mhzx.mobiledesktop.view.CategoryTag;

import com.justsy.zeus.api.DefaultZeusClient;
import com.justsy.zeus.api.request.AppPackageByDepartCodeGetRequest;
import com.justsy.zeus.api.response.AppPackageByDepartCodeGetResponse;

public class ApplicationActivity extends Activity implements OnItemClickListener {
	private static final String TAG = "ApplicationActivity";

	private static final String FILE_PATH = "mobiledesktop";
	
	private static enum PACKAGE_TYPE {iPhoneEnterprisePkg, iPhoneAppStorePkg, AndroidEnterprisePkg, MediaPkg};

	private GridView mApplicationGridView;
	private ApplicationAdapter mApplicationAdapter;
	private DownloadManager mDownloadManager;
	private TextView mApplicationTitle;
	private List<JsonAppInfo> mAppList = new ArrayList<JsonAppInfo>();
	private List<JsonAppInfo> mAppToRemoveList = new ArrayList<JsonAppInfo>();
	
	private long mCategoryId;
	
	private long mEnqueue;
	
	private GetAllAppTask mGetAllAppTask;
	
	private LruCache<String, Bitmap> mMemoryCache;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate start");
		super.onCreate(savedInstanceState);
		
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() /1024);
		
		final int cacheSize = maxMemory / 8;
		
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount() / 1024;
			}
		};
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gridview_layout);
		Bundle bundle = getIntent().getBundleExtra("ListString");
		CategoryTag tag = (CategoryTag) bundle.getSerializable("tag");
		mCategoryId = tag.id;
		
		mApplicationTitle = (TextView) findViewById(R.id.category_title);
		mApplicationTitle.setText(tag.name);
		
		mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//		registerReceiver(new DownloadCompletedReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		
		mGetAllAppTask = new GetAllAppTask();
		mGetAllAppTask.execute();
		Log.d(TAG, "onCreate end");
	}
	
	private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}
	
	private Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mGetAllAppTask != null) {
			mGetAllAppTask.cancel(true);
		}
	}
	
	private class GetAllAppTask extends AsyncTask<Void, Void, Object> {
		
		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected Object doInBackground(Void... params) {
			try {
				return getAllApps();
			} catch (Throwable e) {
				e.printStackTrace();
				return e;
			}
		}
		
		@Override
		protected void onPostExecute(Object result) {
			if (isCancelled()) {
				return;
			}
			
			mApplicationGridView = (GridView) findViewById(R.id.category_gridview);
			mApplicationGridView.setOnItemClickListener(ApplicationActivity.this);
			mApplicationAdapter = new ApplicationAdapter(ApplicationActivity.this, mAppList);
			mApplicationGridView.setAdapter(mApplicationAdapter);
			
			if(mAppList.size() == 0) {
				Toast.makeText(ApplicationActivity.this, "该目录为空！", Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	private class DownloadCompletedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
				// install apk
				Query q = new Query();
				q.setFilterById(mEnqueue);
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
							// "application/vnd.android.package-archive"
							i.setDataAndType(Uri.parse(localUrl), MIMEType); 
							context.startActivity(i);
						} catch (Exception e) {
							// handle exception
							Toast.makeText(ApplicationActivity.this, "对不起，无法打开该应用", Toast.LENGTH_LONG).show();
						}
						
					}
				}
				
			}
		}
		
	}

	public List<JsonAppInfo> getAllApps() throws Throwable {
		String xml = "";
		if (Connectivity.isNetworkConnected(this)) {
			try {
				DefaultZeusClient dzc = new DefaultZeusClient(Constants.URL, Constants.APK_KEY, Constants.SECRET);
				
				AppPackageByDepartCodeGetRequest request = new AppPackageByDepartCodeGetRequest();
				request.setDepartcode("1");
				request.setTimestamp(System.currentTimeMillis());
				
				AppPackageByDepartCodeGetResponse response = dzc.execute(request);
				
				if (response.isSuccess()) {
					
				} else {
					
				}
				
				xml = response.getBody();
				
				SharedPreferences mSharedPreferences = getSharedPreferences(Constant.FILE_XML_SAVING, 0);
				SharedPreferences.Editor mEditor = mSharedPreferences.edit();
				mEditor.putString(Constant.KEY_XML_APPLICATION, xml);
				mEditor.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			SharedPreferences preferences = getSharedPreferences(Constant.FILE_XML_SAVING, 0);
			xml = preferences.getString(Constant.KEY_XML_APPLICATION, "");
		}
		
		Log.d(TAG, "xml applicatoin = " + xml);
		
		JsonAppPackageResponse apr = JsonParser.jsonToObject(xml, JsonAppPackageResponse.class);
		
		for (JsonContent content : apr.content) {
			if (content.appCategory.id == mCategoryId) {
				mAppList = content.appList;
				break;
			}
		}
		
		for (JsonAppInfo appInfo : mAppList) {
			if (appInfo.pkgType != null && appInfo.pkgType.toUpperCase().contains("IOS")) {
				mAppToRemoveList.add(appInfo);
			}
		}
		
		for (JsonAppInfo appInfo : mAppToRemoveList) {
			mAppList.remove(appInfo);
		}
		
		return mAppList;
	}

	private void launchApp(JsonAppInfo appInfo) {
		if (!checkTime(appInfo)) {
			Toast.makeText(this, "对不起，该应用已过期", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (appInfo.pkgFilePath.indexOf("apk") != -1) {
			PackageManager packageManager = getPackageManager();
			Intent mainIntent = new Intent(Intent.ACTION_MAIN);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			List<ResolveInfo> activityList = packageManager.queryIntentActivities(mainIntent, 0);
			
			Intent intent = getLaunchIntent(appInfo.pkgIdentifier, activityList);
			
			Log.d(TAG, "intent = " + intent);
			if (intent == null) {
				if (isFileExist(appInfo)) {
					openFile(appInfo);
				} else {
					String url = appInfo.pkgFilePath;
					if (TextUtils.isEmpty(url)) {
						Toast.makeText(this, "下载地址为空，暂无法下载。", Toast.LENGTH_LONG).show();
					} else {
						String fileName = url.substring(url.lastIndexOf("/"));
						Log.d(TAG, "fileName = " + fileName);
						Request request = new Request(Uri.parse(url));
						request.setTitle(appInfo.pkgName);
						request.setDestinationInExternalPublicDir(FILE_PATH, fileName);
						mEnqueue = mDownloadManager.enqueue(request);
					}
				}
				
			} else {
				startActivity(intent);
			}
		} else {
			if (isFileExist(appInfo)) {
				openFile(appInfo);
			} else {
				String url = appInfo.pkgFilePath;
				if (TextUtils.isEmpty(url)) {
					Toast.makeText(this, "下载地址为空，暂无法下载。", Toast.LENGTH_LONG).show();
				} else {
					String fileName = url.substring(url.lastIndexOf("/"));
					Log.d(TAG, "fileName = " + fileName);
					Request request = new Request(Uri.parse(url));
					request.setTitle(appInfo.pkgName);
					request.setDestinationInExternalPublicDir(FILE_PATH, fileName);
					mEnqueue = mDownloadManager.enqueue(request);
				}
			}
		}
		
/*		if (PACKAGE_TYPE.AndroidEnterprisePkg.name().equalsIgnoreCase(appInfo.pkgType)) {
			PackageManager packageManager = getPackageManager();
			Intent mainIntent = new Intent(Intent.ACTION_MAIN);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			List<ResolveInfo> activityList = packageManager.queryIntentActivities(mainIntent, 0);
			
			Intent intent = getLaunchIntent(appInfo.pkgIdentifier, activityList);
			
			Log.d(TAG, "intent = " + intent);
			if (intent == null) {
				if (isFileExist(appInfo)) {
					openFile(appInfo);
				} else {
					String url = appInfo.pkgFilePath;
					if (TextUtils.isEmpty(url)) {
						Toast.makeText(this, "下载地址为空，暂无法下载。", Toast.LENGTH_LONG).show();
					} else {
						String fileName = url.substring(url.lastIndexOf("/"));
						Log.d(TAG, "fileName = " + fileName);
						Request request = new Request(Uri.parse(url));
						request.setTitle(appInfo.pkgName);
						request.setDestinationInExternalPublicDir(FILE_PATH, fileName);
						mEnqueue = mDownloadManager.enqueue(request);
					}
				}
				
			} else {
				startActivity(intent);
			}
		} else if (PACKAGE_TYPE.MediaPkg.name().equalsIgnoreCase(appInfo.pkgType)) {
			if (isFileExist(appInfo)) {
				openFile(appInfo);
			} else {
				String url = appInfo.pkgFilePath;
				if (TextUtils.isEmpty(url)) {
					Toast.makeText(this, "下载地址为空，暂无法下载。", Toast.LENGTH_LONG).show();
				} else {
					String fileName = url.substring(url.lastIndexOf("/"));
					Log.d(TAG, "fileName = " + fileName);
					Request request = new Request(Uri.parse(url));
					request.setTitle(appInfo.pkgName);
					request.setDestinationInExternalPublicDir(FILE_PATH, fileName);
					mEnqueue = mDownloadManager.enqueue(request);
				}
			}
		} else {
			// DO nothing
			Toast.makeText(this, "对不起，该类型应用不支持", Toast.LENGTH_LONG).show();
		}*/
	}
	
	private Intent getLaunchIntent(String packageName, List<ResolveInfo> activityList) {
		for (ResolveInfo info : activityList) {
			if (info.activityInfo.packageName.endsWith(packageName)) {
				Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
				return intent;
			}
		}
		return null;
	}
	
	private void openFile(JsonAppInfo appInfo) {
		// open file directory
		String url = appInfo.pkgFilePath;
		String fileName = url.substring(url.lastIndexOf("/"));
		File dir = Environment.getExternalStoragePublicDirectory(FILE_PATH);
		File file = new File(dir, fileName);
		String localUrl = file.getAbsolutePath();
		if (file.exists()) {
			String extension = MimeTypeMap.getFileExtensionFromUrl(localUrl);
			String MIMEType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
			Log.d(TAG, "MIMEType = " + MIMEType);
			
			try {
				Intent i = new Intent(Intent.ACTION_VIEW); 
				i.setDataAndType(Uri.parse("file:///" + localUrl), MIMEType); 
				startActivity(i);
			} catch (Exception e) {
				Toast.makeText(ApplicationActivity.this, "对不起，无法打开该应用", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private boolean isFileExist(JsonAppInfo appInfo) {
		String url = appInfo.pkgFilePath;
		String fileName = url.substring(url.lastIndexOf("/"));
		File dir = Environment.getExternalStoragePublicDirectory(FILE_PATH);
		File file = new File(dir, fileName);
		if (file.exists()) {
			Log.d(TAG, "File exists!!! - " + file.getAbsolutePath());
			return true;
		}
		return false;
	}
	
	private boolean checkTime(JsonAppInfo appInfo) {
/*		Log.d(TAG, "appInfo.getStartDate() = " + appInfo.getStartDate());
		Log.d(TAG, "appInfo.getEndDate() = " + appInfo.getEndDate());
		if (TextUtils.isEmpty(appInfo.getStartDate()) || TextUtils.isEmpty(appInfo.getEndDate())) {
			// For test!
			return true;
		}
		
		long startTime = new Date(appInfo.getStartDate()).getTime();
		long endTime = new Date(appInfo.getEndDate()).getTime();
		long currentTime = System.currentTimeMillis();
		Log.d(TAG, "startTime = " + startTime);
		Log.d(TAG, "endTime = " + endTime);
		Log.d(TAG, "currentTime = " + currentTime);
		if (currentTime >= startTime && currentTime <= endTime) {
			return true;
		}
		return false;*/
		return true;
	}

	private class ApplicationAdapter extends BaseAdapter {

		private Context mContext;
		private List<JsonAppInfo> mAppInfoList;
		
		private Integer[] mIconsArray = { 
				R.drawable.icon_car, 
				R.drawable.icon_control,
				R.drawable.icon_prepare, 
				R.drawable.icon_school, 
				R.drawable.icon_system, 
				R.drawable.icon_car
				};

		public ApplicationAdapter(Context context, List<JsonAppInfo> inputDataList) {
			mContext = context;
			mAppInfoList = inputDataList;
		}

		@Override
		public int getCount() {
			return mAppInfoList == null ? 0 : mAppInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return mAppInfoList == null ?  null : mAppInfoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			final JsonAppInfo appUnit = mAppInfoList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.application_item, null);
				holder.applicationName = (TextView) convertView.findViewById(R.id.application_name);
				holder.applicationIcon = (ImageView) convertView.findViewById(R.id.application_icon);
				convertView.setTag(holder);
				convertView.setBackgroundResource(R.drawable.bg_light_center_category_item_selector);
			} 
			holder = (ViewHolder) convertView.getTag();
			holder.applicationName.setText(appUnit.pkgName);
			holder.applicationIcon.setImageResource(mIconsArray[position % 6]);
			loadBitmap(appUnit.pkgHeadpicPath, holder.applicationIcon);
			return convertView;
		}
	}

	private class ViewHolder {
		private ImageView applicationIcon;
		private TextView applicationName;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(isDownloading(mAppList.get(position).pkgFilePath)) {
			Toast.makeText(ApplicationActivity.this, "该应用已在下载列表或正在下载中...", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (Connectivity.isNetworkConnected(this)) {
			new CheckDeviceTask(position).execute();
		} else {
			checkLocal(position);
		}
	}
	
	private boolean isDownloading(String downloadUrl) {
		Query query = new Query();
		query.setFilterByStatus(DownloadManager.STATUS_RUNNING|DownloadManager.STATUS_PENDING);
		
		Cursor cursor = mDownloadManager.query(query);
		if (cursor.moveToFirst()) {
			do {
				String url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
				if (url.equalsIgnoreCase(downloadUrl)) {
					return true;
				}
				
			} while (cursor.moveToNext());
		}
		return false;
	}
	
	private class CheckDeviceTask extends AsyncTask<Void, Void, Object> {
		int mPosition;
		public CheckDeviceTask(int position) {
			mPosition = position;
		}

		@Override
		protected Object doInBackground(Void... params) {
			try {
				return checkDevice();
			} catch (Throwable e) {
				e.printStackTrace();
				return e;
			}
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO for test 
			result = "On";
			if (result instanceof String) {
				try {
					launchApp(mAppList.get(mPosition));
				} catch (Exception e) {
					Log.d(TAG, "exception = " + e.toString());
				}
			} else {
				// error
				Toast.makeText(ApplicationActivity.this, "对不起，设备状态异常，无法使用该应用", Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	private String checkDevice() throws Throwable {
		String xml = "";
		if (Connectivity.isNetworkConnected(this)) {
			String deviceId = Settings.System.getString(getContentResolver(),Settings.System.ANDROID_ID);
			Log.d(TAG, "deviceId = " + deviceId);

			try {
//				xml = soapPrimitive.toString();
				SharedPreferences mSharedPreferences = getSharedPreferences(Constant.FILE_DEVICE_STATUS, Context.MODE_PRIVATE);
				SharedPreferences.Editor mEditor = mSharedPreferences.edit();
				mEditor.putString(Constant.KEY_XML_STATUS_CHECK, xml);
				
				mEditor.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			SharedPreferences preferences = getSharedPreferences(Constant.FILE_DEVICE_STATUS, Context.MODE_PRIVATE);
			xml = preferences.getString(Constant.KEY_XML_STATUS_CHECK, "");
		}
		
		Log.d(TAG, "device xml = " + xml);
		
		ParserBySAX parserBySAX = new ParserBySAX();
		StringReader read = new StringReader(xml);
		InputSource source = new InputSource(read);
		String deviceStatus = parserBySAX.getDeviceStatus(source);
		
		saveDeviceStatus(deviceStatus);
		
		return deviceStatus;
	}
	
	private void saveDeviceStatus(String deviceStatus) {
		if(!TextUtils.isEmpty(deviceStatus)) {
			SharedPreferences preferences = getSharedPreferences(Constant.FILE_DEVICE_STATUS, Context.MODE_PRIVATE);
			Editor editor = preferences.edit();
			editor.putString(Constant.KEY_STATUS, deviceStatus);
			editor.putLong(Constant.KEY_STATUS_CHECK_TIME, System.currentTimeMillis());
			editor.commit();
		}
	}
	
	private void checkLocal(int position) {
		SharedPreferences preferences = getSharedPreferences(Constant.FILE_DEVICE_STATUS, Context.MODE_PRIVATE);
		long checkTime = preferences.getLong(Constant.KEY_STATUS_CHECK_TIME, -1);
		if (checkTime == -1) {
			Log.i(TAG, "Can't check time for local!");
			Toast.makeText(this, "当前设备未通过认证", Toast.LENGTH_LONG).show();
			// TODO
			// return;
		}
		
		if (checkTime + 7*24*3600 < System.currentTimeMillis()) {
			Log.i(TAG, "Chekc time out of date!");
			Toast.makeText(this, "设备认证时间过期", Toast.LENGTH_LONG).show();
			// TODO
			// return;
		}
		
		launchApp(mAppList.get(position));
	}
	
	private class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		private String imageUrl;
		
		public BitmapWorkerTask(ImageView imageView) {
			imageViewReference  = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			try {
				URL url = new URL(imageUrl);
				return BitmapFactory.decodeStream(url.openConnection().getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}
			
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
				if (this == bitmapWorkerTask && imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
		
	}
	
	private void loadBitmap(String url, ImageView imageView) {
		
		final Bitmap bitmap = getBitmapFromMemoryCache(url);
		
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			if (cancelPotentialWork(url, imageView)) {
				// BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_line_one_1);
				final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
				final AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(), null, task);
				imageView.setImageDrawable(asyncDrawable);
				task.execute(url);
			}
		}
		
	}
	
	private boolean cancelPotentialWork(String url, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		if (bitmapWorkerTask != null && url != null) {
			if (!url.equalsIgnoreCase(bitmapWorkerTask.imageUrl)) {
				return bitmapWorkerTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}
	
	private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}
	
	static class AsyncDrawable extends BitmapDrawable {
		final WeakReference<BitmapWorkerTask> taskReference;
		
		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			taskReference = new WeakReference<ApplicationActivity.BitmapWorkerTask>(bitmapWorkerTask);
		}
		
		public BitmapWorkerTask getBitmapWorkerTask() {
			return taskReference.get();
		}
	}
	
}
