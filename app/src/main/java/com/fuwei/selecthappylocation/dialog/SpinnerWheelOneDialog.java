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

import com.fuwei.selecthappylocation.R;
import com.touna.evaluate.spinnerwheel.AbstractWheel;
import com.touna.evaluate.spinnerwheel.adapters.AbstractWheelTextAdapter;

/**
 * Created by collin on 2015-10-02.
 */
@SuppressLint({ "InflateParams", "InlinedApi" })
public class SpinnerWheelOneDialog extends Dialog {
	private static int mVisibleItem = 3;
	private Context mContext = null;
	private View mView = null;
	private Animation mAnimation = null;
	
	private AbstractWheel mAwData = null;
	private Button mBtnConfirm = null;
	
	private String[] mData = null;
	private int mDefaultData = 0;
	
	public SpinnerWheelOneDialog(Context context, String[] data) {
		super(context, R.style.SpinnerWheelDialog);
		this.mContext = context;
		this.mData = data;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView=LayoutInflater.from(getContext()).inflate(R.layout.spinnerwheel_one_dialog_layout, null);
		
		// 设置属性宽度全屏
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		window.getDecorView().setPadding(0, 0, 0, 0);
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		window.setWindowAnimations(R.style.AnimationFade);
		window.setAttributes(lp);
		
		mAwData = (AbstractWheel) mView.findViewById(R.id.sw_one_wheel);
		mAwData.setCyclic(false);
		mAwData.setVisibleItems(mVisibleItem);
		mAwData.setViewAdapter(new SpinnerWheelAdapter(mContext, mData));
		mAwData.setCurrentItem(mDefaultData);
		mBtnConfirm = (Button) mView.findViewById(R.id.btn_one_wheel_confirm);
		
        setContentView(mView);
	}
	
	public void setDefault(int year) {
		this.mDefaultData = year;
	}
	
	public void setConfirmListener(View.OnClickListener listener) {
		mBtnConfirm.setOnClickListener(listener);
	}
	
	public String getResult() {
		StringBuffer finalString = new StringBuffer();
		finalString.append(mData[mAwData.getCurrentItem()]);
		return finalString.toString();
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
			return data == null ? 0 : data.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return data[index];
		}
	}
	
}
