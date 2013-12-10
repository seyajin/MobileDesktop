package cn.sh.mhedu.mhzx.mobiledesktop.bean;

import com.easymorse.R;
import com.easymorse.R.drawable;

public class Category {
	private Integer id;
	private String name;
	public int imageResId = R.drawable.icon_line_one_1;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Category : id = " + id + ", name = " + name;
	}

}
