package com.fuwei.selecthappylocation.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.almeros.android.multitouch.MoveGestureDetector;
import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.log.DebugLog;
import com.fuwei.selecthappylocation.model.SeatMo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * 高端定制布局；
 */
public class SeatLayoutView extends FrameLayout implements View.OnTouchListener {

    private Context mContext;
    private Matrix mMatrix = new Matrix();

    private float mPreScaleFactor = 1.0f;
    private float mScaleFactor = 1.0f;

    private float mPreFocusX = 0.f;
    private float mFocusX = 0.f;        // SeatTableView 与 屏幕左边界的相对位置

    private float mPreFocusY = 0.f;
    private float mFocusY = 0.f;

    private ScaleGestureDetector mScaleDetector;
    private MoveGestureDetector mMoveDetector;

    SeatTableView seatTableView;
    private SeatMo[][] seatTable;

    public List<SeatMo> selectedSeats;  // 保存选中座位

    private int screenWidth;
    private int minLeft;
    private int minTop;

    int[] oldClick = new int[2];
    int[] newClick = new int[2];
    boolean eatClick = true;    // 在缩放和移动的时候,不触发 Click 事件

    private int maxRow = 5;
    private int maxColumn = 1;

    private int mDefWidth;      // 座位 宽
    private int mDefHeight;     // 座位 高

    public SeatLayoutView(Context context) {
        super(context);
        initViews(context);
    }

    public SeatLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(Context context) {

        mContext = context;

        Resources res = getResources();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.advance_order_view, this, true);
        screenWidth = res.getDisplayMetrics().widthPixels;

//        initSeatTable();                    // 初始化 SeatTable
        selectedSeats = new ArrayList<>();

        seatTableView = (SeatTableView) view.findViewById(R.id.seatviewcont);

        mDefWidth = seatTableView.getmDefWidth();
        mDefHeight = seatTableView.getmDefHeight();

        seatTable = seatTableView.getSeatTable();

        seatTableView.setOnTouchListener(this);     // 设置 点击监听

        // Setup Gesture Detectors
        mScaleDetector = new ScaleGestureDetector(mContext, new ScaleListener());
        mMoveDetector = new MoveGestureDetector(mContext, new MoveListener());
    }

    // 左侧的座位列号
//    public void onChanged() {
//        rowView.removeAllViews();
//        rowView.setPadding(getResources().getDimensionPixelSize(
//                R.dimen.padding_1dp),(int) (mFocusY), 0, 0); // 上下移动
//
//        for (int i = 0; i < seatTableView.getRowSize(); i++) {
//            TextView textView = new TextView(mContext);
//
//            // 座位有可能为空；
//            for (int j = 0; j < seatTableView.getColumnSize(); j++) {
//                if (seatTable[i][j] != null) {
//                    textView.setText(seatTable[i][j].rowName);
//                    break;
//                }
//            }
//            textView.setTextSize(8.0f * mScaleFactor);
//            textView.setTextColor(Color.LTGRAY);
//            textView.setGravity(Gravity.CENTER);
//            textView.setLayoutParams(new ViewGroup.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    (int)(defWidth * mScaleFactor)));
//            textView.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_2dp), 0, getResources().getDimensionPixelSize(R.dimen.padding_2dp), 0);
//            rowView.addView(textView);
//        }
//    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        public boolean onScale(ScaleGestureDetector detector) {

            DebugLog.d(DebugLog.TAG, "ScaleListener:onScale " + "getScaleFactor : " + detector.getScaleFactor());
            DebugLog.d(DebugLog.TAG, "ScaleListener:onScale " + "mScaleFactor : " + mScaleDetector);

            mScaleFactor *= detector.getScaleFactor();      // scale change since previous event
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 1.5f));
            return true;
        }
    }

    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        public boolean onMove(MoveGestureDetector detector) {
            PointF p = detector.getFocusDelta();

            eatClick = p.x > 1 || p.y > 1;  // 移动位移
            mFocusX += p.x;
            mFocusY += p.y;

//            DebugLog.d(DebugLog.TAG, " MoveListener:onMove "
//                    + "p.x : " + p.x
//                    + " p.y : " + p.y);
//            DebugLog.d(DebugLog.TAG, " MoveListener:onMove "
//                    + "mFocusX : " + mFocusX
//                    + " mFocusY : " + mFocusY);
            return true;
        }
    }

    public  int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    int[] noSeat = {-1, -1};

    // click 的坐标
    public int[] getClickPoint(MotionEvent event) {

        float currentXPosition = event.getX() - mFocusX;
        float currentYPosition = event.getY() - mFocusY;    // 修正坐标

        float seatWidth = seatTableView.getmSeatWidth();
        for (int i = 0; i < seatTableView.getRowSize(); i++) {
            for (int j = 0; j < seatTableView.getColumnSize(); j++) {

                DebugLog.d(DebugLog.TAG, "SeatLayoutView:getClickPoint "
                        + "j : " + j
                        + " seatWidth : " + seatWidth
                        + " currentXPosition : " + currentXPosition);

                if ((j * seatWidth) < currentXPosition
                        && currentXPosition < (j + 1) * seatWidth
                        && (i * seatWidth) < currentYPosition && currentYPosition < (i + 1) * seatWidth
                        && seatTable[i][j] != null
                        && seatTable[i][j].status >= 1) {   // 1 和 2 才能被点击

                    return new int[]{i, j};
                }
            }
        }
        return noSeat;
    }

    /**
     * @param v     view
     * @param event event
     * @return
     */
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                oldClick = getClickPoint(event);
                eatClick = false;       // 触发点击；
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                eatClick = true;        // 不触发点击事件；
                break;
            case MotionEvent.ACTION_UP:
                newClick = getClickPoint(event);
                int i = newClick[0];
                int j = newClick[1];

                // 如果 触发点击 & 点击区域有效 & 点击区域同时也是 DOWN 的区域
                if (!eatClick && i != -1 && j != -1 && i == oldClick[0] && j == oldClick[1]) {
//                    座位状态:1：可售，0：已售，-1：删除(非法)
                    if (seatTable[i][j].status == 1) {  // 如果为可售状态；
                        seatTable[i][j].status = 2;
                        selectedSeats.add(seatTable[i][j]);
                        Toast.makeText(mContext, seatTable[i][j].seatName, Toast.LENGTH_SHORT).show();
                    } else {
                        seatTable[i][j].status = 1;
                        selectedSeats.remove(seatTable[i][j]);
                    }
                }
                break;
        }

        mScaleDetector.onTouchEvent(event);
        mMoveDetector.onTouchEvent(event);

        float diffScal = Math.abs(mPreScaleFactor - mScaleFactor);  // 之前的缩放倍数 - 当前的缩放倍数；
        float diffY = Math.abs(mPreFocusY - mFocusY);               // Y 轴方向的绝对位移
        float diffX = Math.abs(mPreFocusX - mFocusX);               // X 轴方向的绝对位移

        // Log.i(TAG, "diffScal = " + diffScal + ", preSeatWidth = " + preSeatWidth + ", diffY ＝ " + diffY + ", diffX = " + diffX);
        // 如果 触发点击 或者 Y 轴方向位移 大于 5 或者 X 轴方向位移大于 5 或者 缩放因子差值 大于 0.01;
        if (!eatClick || diffY > 5 || diffX > 5 || diffScal > 0.01) {   // 减少绘制次数
            mMatrix.reset();
            mPreScaleFactor = mScaleFactor;
            mPreFocusY = mFocusY;
            mPreFocusX = mFocusX;

            // 限定移动区域
            // minLeft = seatTableView 的宽度 - 屏幕宽度
            minLeft = (int) (mDefWidth * mScaleFactor * maxColumn) - screenWidth;

            DebugLog.d(DebugLog.TAG, "SeatLayoutView:onTouch " + "minLeft : " + minLeft);
            DebugLog.d(DebugLog.TAG, "SeatLayoutView:onTouch " + "screenWidth : " + screenWidth);

            mFocusX = minLeft > 0 ?
                    // -minLeft <= mFocusX <= defWidth*mScaleFactor
                    Math.max(-minLeft, Math.min(mFocusX, mDefWidth * mScaleFactor))
                    // 0 <= mFocusX <= defWidth * mScaleFactor
                    : Math.max(0, Math.min(mFocusX, mDefWidth * mScaleFactor));

            DebugLog.d(DebugLog.TAG, "SeatLayoutView:onTouch " + "mFocusX : " + mFocusX);

            //
            minTop = (int) (mDefHeight * mScaleFactor * maxRow) - seatTableView.getMeasuredHeight();

            // minTop : -292
            DebugLog.d(DebugLog.TAG, "SeatLayoutView:onTouch " + "minTop : " + minTop);

            // -minTop <= mFocusY <= 0
            mFocusY = minTop > 0 ? Math.max(-minTop, Math.min(mFocusY, 0)) : 0;

            DebugLog.d(DebugLog.TAG, "SeatLayoutView:onTouch " + "mFocusY : " + mFocusY);

            mMatrix.postScale(mScaleFactor, mScaleFactor);  // 宽高缩放相同的系数；

            seatTableView.mScaleFactor = mScaleFactor;
            seatTableView.mPaddingTop = mFocusX;
            seatTableView.mPaddingLeft = mFocusY;

            // 重新绘制
            int seatWidth = (int) (mDefWidth * mScaleFactor);    // 座位的新宽高
            int seatHeight = (int) (mDefHeight * mScaleFactor);

            // 可购买座位
            seatTableView.seat_sale = createScaledBitmap(seatTableView.SeatSale, seatWidth, seatHeight, true);
            // 红色 已售座位
            seatTableView.seat_sold = createScaledBitmap(seatTableView.SeatSold, seatWidth, seatHeight, true);
            // 绿色 我的选择
            seatTableView.seat_selected = createScaledBitmap(seatTableView.SeatSelected, seatWidth, seatHeight, true);
            seatTableView.invalidate();

        }

        return true;
    }

}
