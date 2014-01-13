package cn.sh.mhedu.mhzx.mobiledesktop.bean;

import java.util.List;

public class JsonAppPackageResponse {
	
	public int totalCount;
	
	public List<JsonContent> content;
	
	public boolean success;
	
	public int status;
	
	public JsonMsgs msgs;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (JsonContent c : content) {
			sb.append(c.toString() + " ");
		}
		
		return "AppPackageResponse, totalCount : " + totalCount + "content" + sb.toString() + ", success : " + success + ", status : " + status;
	}

}
