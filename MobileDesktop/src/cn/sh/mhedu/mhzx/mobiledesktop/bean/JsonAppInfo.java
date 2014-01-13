package cn.sh.mhedu.mhzx.mobiledesktop.bean;

public class JsonAppInfo {
	
	public String pkgType;
	
	public String pkgName;
	
	public String pkgIdentifier;
	
	public String pkgVersion;
	
	public long pkgSize;
	
	public String pkgDisplayName;
	
	public String pkgDesc;
	
	public String pkgFilePath;
	
	public String pkgHeadpicPath;
	
	public String pkgPicPath;
	
	public String pkgMd5;
	
	@Override
	public String toString() {
		return "pkgType : " + pkgType + ", "
				+  "pkgName : " + pkgName + ", "
				+  "pkgIdentifier : " + pkgIdentifier + ", "
				+  "pkgVersion : " + pkgVersion + ", "
				+  "pkgSize : " + pkgSize + ", "
				+  "pkgDisplayName : " + pkgDisplayName + ", "
				+  "pkgDesc : " + pkgDesc + ", "
				+  "pkgFilePath : " + pkgFilePath + ", "
				+  "pkgHeadpicPath : " + pkgHeadpicPath + ", "
				+  "pkgPicPath : " + pkgPicPath + ", "
				+  "pkgMd5 : " + pkgMd5;
	}

}
