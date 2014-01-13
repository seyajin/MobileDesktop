package cn.sh.mhedu.mhzx.mobiledesktop.view;

import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
	
	private void initCategoryIcon(CategoryIcon categoryIcon,int position, int resId, CategoryTag tag) {
		categoryIcon.setCategoryIcon(resId);
		categoryIcon.setCategoryName(tag.name);
		
		if (position == 2) {
			categoryIcon.setBackgroundResource(R.drawable.bg_dark_category_item_selector);
		} else {
			categoryIcon.setBackgroundResource(R.drawable.bg_light_category_item_selector);
		}
		
		categoryIcon.setOnClickListener(this);
		categoryIcon.setTag(tag);
	}
	
	public void initCategoryIcon(int position, int resId, CategoryTag tag) {
		switch (position) {
		case 0:
			initCategoryIcon(mCategoryIconOne, 0, resId, tag);
			break;
			
		case 1:
			initCategoryIcon(mCategoryIconTwo, 1, resId, tag);
			break;
			
		case 2:
			initCategoryIcon(mCategoryIconThree, 2, resId, tag);
			break;
			
		case 3:
			initCategoryIcon(mCategoryIconFour, 3, resId, tag);
			break;
			
		case 4:
			initCategoryIcon(mCategoryIconFive, 4, resId, tag);
			break;

		default:
			break;
		}
	}
	
	private void hideCategoryIcon(CategoryIcon categoryIcon) {
		categoryIcon.setVisibility(View.INVISIBLE);
	}
	
	public void hideCategoryIcon(int position) {
		switch (position) {
		case 0:
			hideCategoryIcon(mCategoryIconOne);
			break;
			
		case 1:
			hideCategoryIcon(mCategoryIconTwo);
			break;
			
		case 2:
			hideCategoryIcon(mCategoryIconThree);
			break;
			
		case 3:
			hideCategoryIcon(mCategoryIconFour);
			break;
			
		case 4:
			hideCategoryIcon(mCategoryIconFive);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(mContext, ApplicationActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("tag", (Serializable) v.getTag());
		intent.putExtra("ListString", bundle);
		mContext.startActivity(intent);
	}

}
