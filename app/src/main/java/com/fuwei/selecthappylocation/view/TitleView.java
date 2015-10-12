package com.fuwei.selecthappylocation.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuwei.selecthappylocation.R;

/**
 * Created by linky on 15-10-9.
 */
public class TitleView extends FrameLayout {

    private Context mContext;

    private View mReturnBack;
    private TextView mTitleText;

    private CharSequence mTitle = "默认标题";
    private boolean mHasReturn = false;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleView);

        // 从布局文件中获取标题；
        if(a.hasValue(R.styleable.TitleView_bar_title)) {
            mTitle = a.getString(R.styleable.TitleView_bar_title);
        }

        // 是否返回；
        if(a.hasValue(R.styleable.TitleView_return_back)) {
            mHasReturn = a.getBoolean(R.styleable.TitleView_return_back, false);
        }

        initView(context);
    }

    private void initView(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.title_view, this, true);
        mReturnBack = view.findViewById(R.id.return_back);
        mTitleText = (TextView) view.findViewById(R.id.title_text);
        mReturnBack = view.findViewById(R.id.return_back);

        mTitleText.setText(mTitle);

        if(mHasReturn) {
            mReturnBack.setVisibility(View.VISIBLE);
            mReturnBack.setOnClickListener(mReturnBackClickListener);
        } else {
            mReturnBack.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener mReturnBackClickListener = new OnClickListener() {
        public void onClick(View v) {
            if(mContext != null && mContext instanceof Activity) {
                ((Activity)mContext).finish();
            }
        }
    };
}
