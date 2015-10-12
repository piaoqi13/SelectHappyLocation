package com.fuwei.selecthappylocation.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.fuwei.selecthappylocation.FuWeiApplication;
import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.util.Utils;
import com.touna.evaluate.spinnerwheel.AbstractWheel;
import com.touna.evaluate.spinnerwheel.OnWheelScrollListener;
import com.touna.evaluate.spinnerwheel.adapters.AbstractWheelTextAdapter;

/**
 * Created by collin on 2015-10-02.
 */
@SuppressLint({ "InflateParams", "InlinedApi" })
public class SpinnerWheelThreeDialog extends Dialog {
	private static int mVisibleItem = 3;
	private Context mContext = null;
	private View mView = null;
	private Animation mAnimation = null;
	
	private int mIndex = 0;// 等于 0 是两个参数；等于 1 是三个参数；等于2是五个参数;等于3是一个参数
	
	private AbstractWheel mAwYear = null;
	private AbstractWheel mAwMonth = null;
	private AbstractWheel mAwDay = null;
	private AbstractWheel mAwHour = null;
	private AbstractWheel mAwMinute = null;
	
	private Button mBtnConfirm = null;
	
	private String[] mYear = null;
	private String[] mMonth = null;
	private String[] mDay = null;
	
	private String[] mHour = null;
	private String[] mMinute = null;
	
	private int mDefaultYear = 0;
	private int mDefaultMonth = 0;
	private int mDefaultDay = 0;
	private int mDefaultHour = 0;
	private int mDefaultMinute = 0;
	
	public SpinnerWheelThreeDialog(Context context, String[] year) {
		super(context, R.style.SpinnerWheelDialog);
		this.mContext = context;
		this.mYear = year;
		mIndex=3;
	}
	
	public SpinnerWheelThreeDialog(Context context, String[] year, String[] month) {
		super(context, R.style.SpinnerWheelDialog);
		this.mContext = context;
		this.mYear = year;
		this.mMonth = month;
		mIndex = 0;
	}
	
	public SpinnerWheelThreeDialog(Context context, String[] province, String[] city, String[] region) {
		super(context, R.style.SpinnerWheelDialog);
		this.mContext = context;
		this.mYear = province;
		this.mMonth = city;
		this.mDay = region;
		mIndex = 1;
	}
	
	public SpinnerWheelThreeDialog(Context context, String[] year, String[] month, String[] day, String[] hour, String[] minute) {
		super(context, R.style.SpinnerWheelDialog);
		this.mContext = context;
		this.mYear = year;
		this.mMonth = month;
		this.mDay = day;
		this.mHour = hour;
		this.mMinute = minute;
		mIndex = 2;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView=LayoutInflater.from(getContext()).inflate(R.layout.spinnerwheel_three_dialog_layout, null);
		
		// 设置属性宽度全屏
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		window.getDecorView().setPadding(0, 0, 0, 0);
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		window.setWindowAnimations(R.style.AnimationFade);
		window.setAttributes(lp);
		
		mAwYear = (AbstractWheel) mView.findViewById(R.id.sw_year);
		mAwYear.setCyclic(false);
		mAwYear.setVisibleItems(mVisibleItem);
		mAwMonth = (AbstractWheel) mView.findViewById(R.id.sw_month);
		mAwMonth.setVisibleItems(mVisibleItem);
		mAwMonth.setCyclic(true);
		mAwDay = (AbstractWheel) mView.findViewById(R.id.sw_day);
		mAwDay.setVisibleItems(mVisibleItem);
		mAwDay.setCyclic(true);
		mAwHour = (AbstractWheel) mView.findViewById(R.id.sw_hour);
		mAwHour.setVisibleItems(mVisibleItem);
		mAwHour.setCyclic(true);
		mAwMinute = (AbstractWheel) mView.findViewById(R.id.sw_minute);
		mAwMinute.setVisibleItems(mVisibleItem);
		mAwMinute.setCyclic(true);
		mBtnConfirm = (Button) mView.findViewById(R.id.btn_confirm);
		
		// 控制滚轮显示个数
		if (mIndex == 0) {
			mAwYear.setViewAdapter(new SpinnerWheelAdapter(mContext, mYear));
			mAwYear.setCurrentItem(mDefaultYear);
			mAwMonth.setViewAdapter(new SpinnerWheelAdapter(mContext, mMonth));
			mAwMonth.setCurrentItem(mDefaultMonth);
			mAwDay.setVisibility(View.GONE);
			mAwHour.setVisibility(View.GONE);
			mAwMinute.setVisibility(View.GONE);
		} else if (mIndex == 1) {
			mAwYear.setViewAdapter(new SpinnerWheelAdapter(mContext, mYear));
			mAwYear.setCurrentItem(mDefaultYear);
			mAwMonth.setViewAdapter(new SpinnerWheelAdapter(mContext, mMonth));
			mAwMonth.setCurrentItem(mDefaultMonth);
			mAwDay.setViewAdapter(new SpinnerWheelAdapter(mContext, mDay));
			mAwDay.setCurrentItem(mDefaultDay);
			mAwHour.setVisibility(View.GONE);
			mAwMinute.setVisibility(View.GONE);
		} else if(mIndex==2){
			mAwYear.setViewAdapter(new SpinnerWheelAdapter(mContext, mYear));
			mAwYear.setCurrentItem(mDefaultYear);
			mAwMonth.setViewAdapter(new SpinnerWheelAdapter(mContext, mMonth));
			mAwMonth.setCurrentItem(mDefaultMonth);
			mAwDay.setViewAdapter(new SpinnerWheelAdapter(mContext, mDay));
			mAwDay.setCurrentItem(mDefaultDay);
			mAwHour.setViewAdapter(new SpinnerWheelAdapter(mContext, mHour));
			mAwHour.setCurrentItem(mDefaultHour);
			mAwMinute.setViewAdapter(new SpinnerWheelAdapter(mContext, mMinute));
			mAwMinute.setCurrentItem(mDefaultMinute);
		}else{
			mAwYear.setViewAdapter(new SpinnerWheelAdapter(mContext, mYear));
			mAwYear.setCurrentItem(mDefaultYear);
			mAwMonth.setVisibility(View.GONE);
			mAwDay.setVisibility(View.GONE);
			mAwHour.setVisibility(View.GONE);
			mAwMinute.setVisibility(View.GONE);
		}
		
		mAwMonth.addScrollingListener( new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
            	// ToDo
            }
            public void onScrollingFinished(AbstractWheel wheel) {
                if (mIndex == 1) {// 等于1时三个滚轮
                	int monthIndex = wheel.getCurrentItem();
                    // 重置日数据
                	mAwDay.setViewAdapter(new SpinnerWheelAdapter(mContext, mDay));
        			mAwDay.setCurrentItem(mDefaultDay);
        			if (monthIndex == 0 || monthIndex == 2 || monthIndex == 4 || monthIndex == 6 || monthIndex == 7 || monthIndex == 9 || monthIndex == 11) {// 一个月31天的月份
        				mAwDay.setViewAdapter(new SpinnerWheelAdapter(mContext, FuWeiApplication.getInstance().mDay31Array));
                	} else if (monthIndex == 3 || monthIndex == 5 || monthIndex == 8 || monthIndex == 10) {// 一个月30天的月份
                		mAwDay.setViewAdapter(new SpinnerWheelAdapter(mContext, FuWeiApplication.getInstance().mDay30Array));
                	} else if (monthIndex == 1 && Utils.isLudgeYear(Long.parseLong(mYear[mAwYear.getCurrentItem()].replace("年", "")))) {// 闰年2月29天
                		mAwDay.setViewAdapter(new SpinnerWheelAdapter(mContext, FuWeiApplication.getInstance().mDay29Array));
                	} else if (monthIndex == 1 && !Utils.isLudgeYear(Long.parseLong(mYear[mAwYear.getCurrentItem()].replace("年", "")))) {// 平年2月28天
                		mAwDay.setViewAdapter(new SpinnerWheelAdapter(mContext, FuWeiApplication.getInstance().mDay28Array));
                	}
        			// 重选日时分通通显示1
        			mAwDay.setCurrentItem(0);
        			mAwHour.setCurrentItem(0);
        			mAwMinute.setCurrentItem(0);
                }
            }
        });
		
        setContentView(mView);
	}
	//Index=3 一个参数
	public void setDefault(int year){
		String yearStr = String.valueOf(year) + "年";
		for (int i = 0; i < mYear.length; i ++) {
			if (yearStr.equals(mYear[i])) {
				mDefaultYear = i;
				break;
			}
		}
	}
	// Index=0两个参数
	public void setDefault(int year, int month) {
		String yearStr = String.valueOf(year) + "年";
		for (int i = 0; i < mYear.length; i ++) {
			if (yearStr.equals(mYear[i])) {
				mDefaultYear = i;
				break;
			}
		}
		
		for (int i = 0; i < mMonth.length; i ++) {
			if (((String.valueOf(month).length() == 1 ? "0" + String.valueOf(month) : String.valueOf(month)) + "月").equals(mMonth[i])) {
				this.mDefaultMonth = i;
				break;
			}
		}
	}
	
	// Index=1三个参数
	public void setDefault(int year, int month, int day) {
		String yearStr = String.valueOf(year) + "年";
		for (int i = 0; i < mYear.length; i ++) {
			if (yearStr.equals(mYear[i])) {
				this.mDefaultYear = i;
				break;
			}
		}
		
		for (int i = 0; i < mMonth.length; i ++) {
			if (((String.valueOf(month).length() == 1 ? "0" + String.valueOf(month) : String.valueOf(month)) + "月").equals(mMonth[i])) {
				this.mDefaultMonth = i;
				break;
			}
		}
		
		for (int i = 0; i < mDay.length; i ++) {
			if (((String.valueOf(day).length() == 1 ? "0" + String.valueOf(day) : String.valueOf(day)) + "日").equals(mDay[i])) {
				this.mDefaultDay = i;
				break;
			}
		}
	}
	
	// Index=2五个参数
	public void setDefault(int year, int month, int day, int hour, int minute) {
		String yearStr = String.valueOf(year) + "年";
		for (int i = 0; i < mYear.length; i ++) {
			if (yearStr.equals(mYear[i])) {
				this.mDefaultYear = i;
				break;
			}
		}
		
		for (int i = 0; i < mMonth.length; i ++) {
			if (((String.valueOf(month).length() == 1 ? "0" + String.valueOf(month) : String.valueOf(month)) + "月").equals(mMonth[i])) {
				this.mDefaultMonth = i;
				break;
			}
		}
		
		for (int i = 0; i < mDay.length; i ++) {
			if (((String.valueOf(day).length() == 1 ? "0" + String.valueOf(day) : String.valueOf(day)) + "日").equals(mDay[i])) {
				this.mDefaultDay = i;
				break;
			}
		}
		
		for (int i = 0; i < mHour.length; i ++) {
			if (((String.valueOf(hour).length() == 1 ? "0" + String.valueOf(hour) : String.valueOf(hour)) + "时").equals(mHour[i])) {
				this.mDefaultHour = i;
				break;
			}
		}
		
		for (int i = 0; i < mMinute.length; i ++) {
			if (((String.valueOf(minute).length() == 1 ? "0" + String.valueOf(minute) : String.valueOf(minute)) + "分").equals(mMinute[i])) {
				this.mDefaultMinute = i;
				break;
			}
		}
	}
	
	public void setConfirmListener(View.OnClickListener listener) {
		mBtnConfirm.setOnClickListener(listener);
	}
	
	public String getResult() {
		StringBuffer finalString = new StringBuffer();
		if (mIndex==0) {// 年月
			finalString.append(mYear[mAwYear.getCurrentItem()]);
			finalString.append("-");
			finalString.append(mMonth[mAwMonth.getCurrentItem()]);
		} else if (mIndex==1) {// 年月日
			finalString.append(mYear[mAwYear.getCurrentItem()]);
			finalString.append("-");// 2个空格
			finalString.append(mMonth[mAwMonth.getCurrentItem()]);
			finalString.append("-");// 2个空格
			finalString.append(FuWeiApplication.getInstance().mDay31Array[mAwDay.getCurrentItem()]);
		} else if(mIndex==2){// 年月日时分
			finalString.append(mYear[mAwYear.getCurrentItem()]);
			finalString.append("-");
			finalString.append(mMonth[mAwMonth.getCurrentItem()]);
			finalString.append("-");
			finalString.append(mDay[mAwDay.getCurrentItem()]);
			finalString.append("    ");// 4个空格
			finalString.append(mHour[mAwHour.getCurrentItem()]);
			finalString.append(":");
			finalString.append(mMinute[mAwMinute.getCurrentItem()]);
		}else{
			finalString.append(mYear[mAwYear.getCurrentItem()]);
		}
		return finalString.toString().replace("年", "").replace("月", "").replace("日", "").replace("时", "").replace("分", "");
	}
	
	@Override
	public void show() {
		super.show();
		mAnimation=AnimationUtils.loadAnimation(mContext, R.anim.spinner_wheel_enter);
        mView.startAnimation(mAnimation);
	}
	
	private class SpinnerWheelAdapter extends AbstractWheelTextAdapter {
		private String data[] = null;

		protected SpinnerWheelAdapter(Context context, String[] data) {
			super(context, R.layout.spinnerwheel_item, NO_RESOURCE);
			this.data = data;
			setItemTextResource(R.id.item_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return data.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return data[index];
		}
	}
	
}
