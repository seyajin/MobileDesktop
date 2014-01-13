package cn.sh.mhedu.mhzx.mobiledesktop.bean;

import java.util.List;

import cn.sh.mhedu.mhzx.mobiledesktop.R;

public class JsonContent {
	
	public int imageResId = R.drawable.icon_line_one_1;;
	
	public JsonAppCategroy appCategory;
	
	public List<JsonAppInfo> appList;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (JsonAppInfo info : appList) {
			sb.append(info.toString() + " ");
		}
		
		return "appCategory.id : " + appCategory.id + ", appCategory.categoryName : " + appCategory.categoryName + ", appList : " + sb.toString();
	}

}
