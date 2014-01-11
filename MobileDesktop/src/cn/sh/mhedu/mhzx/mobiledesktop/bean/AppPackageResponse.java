package cn.sh.mhedu.mhzx.mobiledesktop.bean;

import java.util.List;

public class AppPackageResponse {
	
	public int totalCount;
	
	public List<Object> content;
	
	public boolean success;
	
	public int status;
	
	public Object msgs;
	
	@Override
	public String toString() {
		return "AppPackageResponse, totalCount : " + totalCount + ", success : " + success + ", status : " + status;
	}

}
