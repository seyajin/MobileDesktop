package cn.sh.mhedu.mhzx.mobiledesktop.parser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;
import cn.sh.mhedu.mhzx.mobiledesktop.bean.Category;
import cn.sh.mhedu.mhzx.mobiledesktop.bean.CompAppInfo;

public class ExampleHandler extends DefaultHandler {
	private static final String TAG = "ExampleHandler";

	private static enum PACKAGE_TYPE {iPhoneEnterprisePkg, iPhoneAppStorePkg, AndroidEnterprisePkg, MediaPkg};

	private List<Category> mCategoriesList;
	private List<CompAppInfo> mCompAppInfosList;
	private String mElementName;// ±£´æÔªËØÃû³Æ
	private Category mCategory;
	private CompAppInfo mCompAppInfo;
	private String mName;
	private String mDeviceStatus;
	

	public void startDocument() throws SAXException {
		mCategoriesList = new ArrayList<Category>();
		mCompAppInfosList = new ArrayList<CompAppInfo>();
		mName = "";
	}

	public void endDocument() throws SAXException {
		
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (localName.equals("Category")) {
			mCategory = new Category();
			mCategory.setId(Integer.parseInt(atts.getValue(0)));
			mCategory.setName(atts.getValue(1));
			mName = atts.getValue(1);
		} else if (localName.equals("row")) {
			mCompAppInfo = new CompAppInfo();
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("Category")) {
			if (hasChildren(mName)) {
				Log.d(TAG, "not empty category : " + mName);
				mCategoriesList.add(mCategory);
			} else {
				Log.d(TAG, "empty category : " + mName);
			}
		} else if (localName.equals("row")) {
			Log.d(TAG, "PackageType = " + mCompAppInfo.getPackageType());
			if (PACKAGE_TYPE.iPhoneAppStorePkg.name().equalsIgnoreCase(mCompAppInfo.getPackageType())
					|| PACKAGE_TYPE.iPhoneEnterprisePkg.name().equalsIgnoreCase(mCompAppInfo.getPackageType())) {
				Log.d(TAG, "ios app.");
			} else {
				mCompAppInfo.setParentCategoryName(mName);
				mCompAppInfosList.add(mCompAppInfo);
			}
		} else if (localName.equals("PackageName")) {
			mCompAppInfo.setPackageName(mElementName);
		} else if ("PackageId".equals(localName)) {
			mCompAppInfo.setPackageId(mElementName);
		} else if ("PackageType".equals(localName)) {
			mCompAppInfo.setPackageType(mElementName);
		} else if ("PackageVersion".equals(localName)) {
			mCompAppInfo.setPackageVersion(mElementName);
		} else if ("DownloadCount".equals(localName)) {
			mCompAppInfo.setDownloadCount(mElementName);
		} else if ("PackageDescription".equals(localName)) {
			mCompAppInfo.setPackageDescription(mElementName);
		} else if ("StartDate".equals(localName)) {
			mCompAppInfo.setStartDate(mElementName);
		} else if ("EndDate".equals(localName)) {
			mCompAppInfo.setEndDate(mElementName);
		} else if ("CreatedTime".equals(localName)) {
			mCompAppInfo.setCreatedTime(mElementName);
		} else if ("LastModified".equals(localName)) {
			mCompAppInfo.setLastModified(mElementName);
		} else if ("PackageUrl".equals(localName)) {
			mCompAppInfo.setPackageUrl(mElementName);
		} else if ("PackagePeriod".equalsIgnoreCase(localName)) {
			mCompAppInfo.setPackagePeriod(mElementName);
		} else if ("PackageCode".equalsIgnoreCase(localName)) {
			mCompAppInfo.setPackageCode(mElementName);
		} else if ("CFBundleIdentifier".equalsIgnoreCase(localName)) {
			mCompAppInfo.setCFBundleIdentifier(mElementName);
		}
		
		if ("StatusInfo".endsWith(localName)) {
			Log.d(TAG, "StatusInfo = " + mElementName);
			mDeviceStatus = mElementName;
		}
		mElementName = "";
	}

	public void characters(char ch[], int start, int length) {
		mElementName = new String(ch, start, length);
	}

	public List<Category> getCategories() {
		return mCategoriesList;
	}

	public List<CompAppInfo> getCompAppInfos() {
		return mCompAppInfosList;
	}
	
	public String getDeviceStatus() {
		return mDeviceStatus;
	}
	
	private boolean hasChildren(String parentCategoryName) {
		for (CompAppInfo appInfo : mCompAppInfosList) {
			if (appInfo.getParentCategoryName().equalsIgnoreCase(parentCategoryName)) {
				return true;
			}
		}
		return false;
	}

}
