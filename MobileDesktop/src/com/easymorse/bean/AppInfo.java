package com.easymorse.bean;

import android.graphics.drawable.Drawable;

public class AppInfo {
	public String appName;
	public String packageName;
	public String versionName;
	public int versionCode = 0;
	public Drawable appIcon;
	
	public String appDownloadUrl;
	
	@Override
	public String toString() {
		return "AppInfo : appName = " + appName 
				+ ", packageName = " + packageName 
				+ ", versionName = " + versionName 
				+ ", versionCode = " + versionCode
				+ ", appIcon = " + appIcon
				+ ", appDownloadUrl = " + appDownloadUrl;
	}

}
