package cn.sh.mhedu.mhzx.mobiledesktop;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import cn.sh.mhedu.mhzx.mobiledesktop.bean.JsonAppPackageResponse;
import cn.sh.mhedu.mhzx.mobiledesktop.bean.JsonContent;
import cn.sh.mhedu.mhzx.mobiledesktop.parser.JsonParser;
import cn.sh.mhedu.mhzx.mobiledesktop.util.Constant;
import cn.sh.mhedu.mhzx.mobiledesktop.utility.Constants;
import cn.sh.mhedu.mhzx.mobiledesktop.view.CategoryItemEvenView;
import cn.sh.mhedu.mhzx.mobiledesktop.view.CategoryItemOddView;
import cn.sh.mhedu.mhzx.mobiledesktop.view.CategoryTag;

import com.justsy.zeus.api.DefaultZeusClient;
import com.justsy.zeus.api.request.AppPackageByDepartCodeGetRequest;
import com.justsy.zeus.api.response.AppPackageByDepartCodeGetResponse;

public class CategoryActivity extends Activity {
	private static final String TAG = "CategoryActivity";
	
	private static final int INIT_MAIN_CONTENT_VIEW = 0;

	private TextView mCategoryTitle;
	private LinearLayout mContentLayout;
	private List<JsonContent> mCategoryList;
	private MyHandler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate start");
		
/*		Log.d(TAG, "Build.SERIAL = " + Build.SERIAL);
		
		String deviceId = Settings.System.getString(getContentResolver(),Settings.System.ANDROID_ID);
		Log.d(TAG, "deviceId = " + deviceId);
		
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		Log.d(TAG, "tm deviceId = " + tm.getDeviceId());*/
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.category_layout);

		mHandler = new MyHandler();
		mCategoryTitle = (TextView) findViewById(R.id.category_title);
		mContentLayout = (LinearLayout) findViewById(R.id.main_content);
		
		new GetCategoryTask().execute();
		Log.d(TAG, "onCreate end");
		
	}
	
	private void initContentView() {
		int totalSize = mCategoryList.size();
		Log.d(TAG, "CategoryList size = " + mCategoryList.size());
		int size = totalSize;
		int position = 0;
		int rows = 1;
		if (size == 0) {
			return;
		}
		
		do {
			if (rows % 2 == 0) {
				CategoryItemEvenView evenView = new CategoryItemEvenView(this);
				
				for (int i = 0; i < 5; i++) {
					if (position + i < totalSize) {
						CategoryTag tag = new CategoryTag();
						tag.id = mCategoryList.get(position + i).appCategory.id;
						tag.name = mCategoryList.get(position + i).appCategory.categoryName;
						evenView.initCategoryIcon(i, mCategoryList.get(position + i).imageResId, tag);
					} else {
						evenView.hideCategoryIcon(i);
					}
				}
				
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				mContentLayout.addView(evenView, lp);
			} else {
				CategoryItemOddView oddView = new CategoryItemOddView(this);
				
				for (int i = 0; i < 4; i++) {
					if (position + i < totalSize) {
						CategoryTag tag = new CategoryTag();
						tag.id = mCategoryList.get(position + i).appCategory.id;
						tag.name = mCategoryList.get(position + i).appCategory.categoryName;
						oddView.initCategoryIcon(i, mCategoryList.get(position + i).imageResId, tag);
					} else {
						oddView.hideCategoryIcon(i);
					}
				}
				
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				mContentLayout.addView(oddView, lp);
			}
			
			if (rows % 2 == 0) {
				size -= 5;
				position += 5;
			} else {
				size -= 4;
				position += 4;
			}
			rows++;
		} while (size > 0);
		
		mContentLayout.invalidate();
	}
	
	private class GetCategoryTask extends AsyncTask<Void, Void, Object> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Object doInBackground(Void... params) {
			try {
				return getCategories();
			} catch (Throwable e) {
				return e;
			}
		}
		
		@Override
		protected void onPostExecute(Object result) {
			if (result instanceof Throwable) {
				mCategoryTitle.setText(((Throwable)result).getMessage());
				return;
			}
			
			if (mCategoryList.size() == 0) {
				Toast.makeText(CategoryActivity.this, "对不起，获取类别为空，请退出重试。", Toast.LENGTH_LONG).show();
			}
			
			initCategoryIcon();
			
			mHandler.sendEmptyMessage(INIT_MAIN_CONTENT_VIEW);
		}
	}
	
	private void initCategoryIcon() {
		try {
			mCategoryList.get(0).imageResId = R.drawable.icon_line_one_1;
			mCategoryList.get(1).imageResId = R.drawable.icon_line_one_2;
			mCategoryList.get(2).imageResId = R.drawable.icon_line_one_3;
			mCategoryList.get(3).imageResId = R.drawable.icon_line_one_4;
			
			mCategoryList.get(4).imageResId = R.drawable.icon_line_two_1;
			mCategoryList.get(5).imageResId = R.drawable.icon_line_two_2;
			mCategoryList.get(6).imageResId = R.drawable.icon_line_two_3;
			mCategoryList.get(7).imageResId = R.drawable.icon_line_two_4;
			mCategoryList.get(8).imageResId = R.drawable.icon_line_two_5;
			
			mCategoryList.get(9).imageResId = R.drawable.icon_line_three_1;
			mCategoryList.get(10).imageResId = R.drawable.icon_line_three_2;
			mCategoryList.get(11).imageResId = R.drawable.icon_line_three_3;
			mCategoryList.get(12).imageResId = R.drawable.icon_line_three_4;
		} catch (Exception e) {
			
		}
	}

	private List<JsonContent> getCategories() throws Throwable {
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

				SharedPreferences mSharedPreferences = getSharedPreferences(Constant.FILE_XML_SAVING, Context.MODE_PRIVATE);
				SharedPreferences.Editor mEditor = mSharedPreferences.edit();
				mEditor.putString(Constant.KEY_XML_CATEGORY, xml);
				mEditor.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			SharedPreferences preferences = getSharedPreferences(Constant.FILE_XML_SAVING, Context.MODE_PRIVATE);
			xml = preferences.getString(Constant.KEY_XML_CATEGORY, "");
		}
		
		Log.d(TAG, "xml = " + xml);
		
		JsonAppPackageResponse apr = JsonParser.jsonToObject(xml, JsonAppPackageResponse.class);
		mCategoryList = apr.content;
		return mCategoryList;
		
	}

	private class MyHandler extends Handler {
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INIT_MAIN_CONTENT_VIEW:
				initContentView();
				break;

			default:
				break;
			}
		}
	}
}
