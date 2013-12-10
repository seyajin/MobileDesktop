package cn.sh.mhedu.mhzx.mobiledesktop.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import cn.sh.mhedu.mhzx.mobiledesktop.ApplicationActivity;
import cn.sh.mhedu.mhzx.mobiledesktop.R;

public class CategoryItemEvenView extends LinearLayout implements OnClickListener {
	private CategoryIcon mCategoryIconOne;
	private CategoryIcon mCategoryIconTwo;
	private CategoryIcon mCategoryIconThree;
	private CategoryIcon mCategoryIconFour;
	private CategoryIcon mCategoryIconFive;
	
	private Context mContext;
	
	public CategoryItemEvenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public CategoryItemEvenView(Context context) {
		super(context);
		initView(context);
	}
	
	private void initView(Context context) {
		mContext = context;
		
		mCategoryIconOne = new CategoryIcon(context);
		mCategoryIconTwo = new CategoryIcon(context);
		mCategoryIconThree = new CategoryIcon(context);
		mCategoryIconFour = new CategoryIcon(context);
		mCategoryIconFive = new CategoryIcon(context);
		
		LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		addView(mCategoryIconOne, lp);
		lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		addView(mCategoryIconTwo, lp);
		lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 2);
		addView(mCategoryIconThree, lp);
		lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		addView(mCategoryIconFour, lp);
		lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		addView(mCategoryIconFive, lp);
		
	}
	
	private void initCategoryIconOne(int resId, String name) {
		mCategoryIconOne.setCategoryIcon(resId);
		mCategoryIconOne.setCategoryName(name);
		mCategoryIconOne.setBackgroundResource(R.drawable.bg_light_category_item_selector);
		mCategoryIconOne.setOnClickListener(this);
		mCategoryIconOne.setTag(name);
	}
	
	private void initCategoryIconTwo(int resId, String name) {
		mCategoryIconTwo.setCategoryIcon(resId);
		mCategoryIconTwo.setCategoryName(name);
		mCategoryIconTwo.setBackgroundResource(R.drawable.bg_light_category_item_selector);
		mCategoryIconTwo.setOnClickListener(this);
		mCategoryIconTwo.setTag(name);
	}
	
	private void initCategoryIconThree(int resId, String name) {
		mCategoryIconThree.setCategoryIcon(resId);
		mCategoryIconThree.setCategoryName(name);
		mCategoryIconThree.setBackgroundResource(R.drawable.bg_dark_category_item_selector);
		mCategoryIconThree.setOnClickListener(this);
		mCategoryIconThree.setTag(name);
	}
	
	private void initCategoryIconFour(int resId, String name) {
		mCategoryIconFour.setCategoryIcon(resId);
		mCategoryIconFour.setCategoryName(name);
		mCategoryIconFour.setBackgroundResource(R.drawable.bg_light_category_item_selector);
		mCategoryIconFour.setOnClickListener(this);
		mCategoryIconFour.setTag(name);
	}
	
	private void initCategoryIconFive(int resId, String name) {
		mCategoryIconFive.setCategoryIcon(resId);
		mCategoryIconFive.setCategoryName(name);
		mCategoryIconFive.setBackgroundResource(R.drawable.bg_light_category_item_selector);
		mCategoryIconFive.setOnClickListener(this);
		mCategoryIconFive.setTag(name);
	}
	
	public void initCategoryIcon(int position, int resId, String name) {
		switch (position) {
		case 0:
			initCategoryIconOne(resId, name);
			break;
			
		case 1:
			initCategoryIconTwo(resId, name);
			break;
			
		case 2:
			initCategoryIconThree(resId, name);
			break;
			
		case 3:
			initCategoryIconFour(resId, name);
			break;
			
		case 4:
			initCategoryIconFive(resId, name);
			break;

		default:
			break;
		}
	}
	
	private void hideCategoryIconOne() {
		mCategoryIconOne.setVisibility(View.INVISIBLE);
	}
	
	private void hideCategoryIconTwo() {
		mCategoryIconTwo.setVisibility(View.INVISIBLE);
	}
	
	private void hideCategoryIconThree() {
		mCategoryIconThree.setVisibility(View.INVISIBLE);
	}
	
	private void hideCategoryIconFour() {
		mCategoryIconFour.setVisibility(View.INVISIBLE);
	}
	
	private void hideCategoryIconFive() {
		mCategoryIconFive.setVisibility(View.INVISIBLE);
	}
	
	public void hideCategoryIcon(int position) {
		switch (position) {
		case 0:
			hideCategoryIconOne();
			break;
			
		case 1:
			hideCategoryIconTwo();
			break;
			
		case 2:
			hideCategoryIconThree();
			break;
			
		case 3:
			hideCategoryIconFour();
			break;
			
		case 4:
			hideCategoryIconFive();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(mContext, ApplicationActivity.class);
		intent.putExtra("ListString", (String) v.getTag());
		mContext.startActivity(intent);
	}

}
