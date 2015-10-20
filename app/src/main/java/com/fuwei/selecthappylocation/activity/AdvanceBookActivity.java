package com.fuwei.selecthappylocation.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.almeros.android.multitouch.MoveGestureDetector;
import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.model.SeatMo;
import com.fuwei.selecthappylocation.view.SeatTableView;

import java.util.Random;

/**
 * 高端定制
 */
public class AdvanceBookActivity extends BaseActivity implements View.OnTouchListener {

    private Matrix mMatrix = new Matrix();
    private float mPreScaleFactor = 1.0f;
    private float mScaleFactor = 1.0f;
    private float mPreFocusX = 0.f;
    private float mFocusX = 0.f;
    private float mPreFocusY = 0.f;
    private float mFocusY = 0.f;

    private TextView mTvSelectedNumber;
    private SeatTableView mSeatTableView;
    private SeatMo[][] mSeatTable;
    private int mDefWidth;
    private LinearLayout mRowView;

    private ScaleGestureDetector mScaleDetector;
    private MoveGestureDetector mMoveDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.advance_book_view);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        Resources resources = getResources();
        mDefWidth = resources.getDimensionPixelSize(R.dimen.padding_20dp);

        mTvSelectedNumber = (TextView) findViewById(R.id.selected_number);
        mSeatTableView = (SeatTableView) findViewById(R.id.seatviewcont);
        mRowView = (LinearLayout) findViewById(R.id.seatraw);

        initSeatTable();

        //居中线的画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2f);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        mSeatTableView.setSeatTable(mSeatTable);
        mSeatTableView.setRowSize(maxRow);
        mSeatTableView.setColumnSize(maxColumn);
        mSeatTableView.setOnTouchListener(this);
        mSeatTableView.setLinePaint(paint);
        mSeatTableView.setDefWidth(mDefWidth);

        onChanged();

        // Setup Gesture Detectors
        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
        mMoveDetector = new MoveGestureDetector(this, new MoveListener());
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();      // scale change since previous event
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 6.0f));
            return true;
        }
    }

    int[] oldClick = new int[2];
    int[] newClick = new int[2];
    boolean eatClick = true;    // 在缩放和移动的时候,不触发click事件
    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        public boolean onMove(MoveGestureDetector detector) {
            PointF d = detector.getFocusDelta();
            eatClick = d.x > 1 || d.y > 1;
            mFocusX += d.x;
            mFocusY += d.y;

            return true;
        }
    }

    // 左侧的座位列号
    public void onChanged() {
        mRowView.removeAllViews();
        mRowView.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_1dp), (int) (mFocusY), 0, 0);//上下移动

        //rowView.setBackgroundColor(getResources().getColor(R.color.black));
//        rowView.startAnimation(alpha);

        for (int i = 0; i < mSeatTableView.getRowSize(); i++) {
            TextView textView = new TextView(AdvanceBookActivity.this);
            // 座位有可能为空
            for (int j = 0; j < mSeatTableView.getColumnSize(); j++) {
                if (mSeatTable[i][j] != null) {
                    textView.setText(mSeatTable[i][j].rowName);
                    break;
                }
            }
            textView.setTextSize(8.0f * mScaleFactor);
            textView.setTextColor(Color.LTGRAY);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, (int)(mDefWidth * mScaleFactor)));
            textView.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_2dp), 0, getResources().getDimensionPixelSize(R.dimen.padding_2dp), 0);
            mRowView.addView(textView);
        }
    }

    private int maxRow = 16;
    private int maxColumn = 30;

    private void initSeatTable() {
        mSeatTable = new SeatMo[maxRow][maxColumn];// mock data
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxColumn; j++) {
                SeatMo seat = new SeatMo();
                seat.row = i;
                seat.column = j;
                seat.rowName = String.valueOf((char)('A' + i));
                seat.seatName = seat.rowName + "排" + (j + 1) + "座";
                seat.status = randInt(-2, 1);//假设-2为空座位
                mSeatTable[i][j] = seat.status == -2 ? null : seat;
            }
        }
    }

    public  int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
