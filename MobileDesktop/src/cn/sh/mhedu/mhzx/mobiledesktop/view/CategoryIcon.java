package cn.sh.mhedu.mhzx.mobiledesktop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sh.mhedu.mhzx.mobiledesktop.R;

public class CategoryIcon extends LinearLayout {
	
	private ImageView mIcon;
	private TextView mName;
	private float mDensity;

	public CategoryIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDensity = context.getResources().getDisplayMetrics().density;
		
		setPadding((int) (30*mDensity), (int) (20*mDensity), (int) (10*mDensity), (int) (10*mDensity));
		
		mIcon = new ImageView(context);
		mName = new TextView(context);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CategoryIcon);
		mIcon.setImageResource(a.getResourceId(R.styleable.CategoryIcon_icon, R.drawable.icon_school));
		mName.setText(context.getString(a.getResourceId(R.styleable.CategoryIcon_name, R.string.app_name)));
		a.recycle();
		
		setOrientation(VERTICAL);
		LayoutParams iconParams = new LayoutParams(LayoutParams.WRAP_CONTENT, 0, 1);
		iconParams.gravity = Gravity.LEFT;
		addView(mIcon, iconParams);
		
		LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0);
		textParams.gravity = Gravity.RIGHT;
		mName.setTextColor(0xFFFFFFFF);
		addView(mName, textParams);
	}

	public CategoryIcon(Context context) {
		this(context, null);
	}
	
	public void setCategoryName(String name) {
		mName.setText(name);
	}
	
	public void setCategoryIcon(int resId) {
		mIcon.setImageResource(resId);
	}

}
