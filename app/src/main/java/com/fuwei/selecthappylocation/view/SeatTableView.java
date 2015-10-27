package com.fuwei.selecthappylocation.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.almeros.android.multitouch.MoveGestureDetector;
import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.model.SeatMo;
import com.fuwei.selecthappylocation.util.EasyLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * 座位显示等的 View
 * @author Linky
 */
public class SeatTableView extends View implements View.OnTouchListener {

    Bitmap seat_sale, seat_sold, seat_selected,seat_locked;
    Bitmap SeatSale, SeatSold, SeatSelected, SeatLocked;

    private Matrix mMatrix = new Matrix();

    private float mPreScaleFactor = 1.0f;

    private float mPreFocusX = 0.f;
    private float mFocusX = 0.f;        // SeatTableView 与 屏幕左边界的相对位置

    private float mPreFocusY = 0.f;
    private float mFocusY = 0.f;

    private int mSeatWidth;  // 座位图 宽
    private int mSeatHeight; // 座位图 高

    private int mDefWidth;   // 初始值 宽
    private int mDefHeight;  // 初始值 高

    private int mWidthGap;     //
    private int mHeightGap;    //

    private int mMarginLeft;    //

    // 放大率和移动位置
    public float mScaleFactor = 1.f;
    public float mPaddingTop = .0f;             // View 的开始位置 X 坐标 以及左右边界；
    public float mPaddingLeft = .0f;            // View 的开始位置 Y 坐标 以及上下边界；

    private SeatMo[][] seatTable;

    private int mRowSize = 10;
    private int mColumnSize = 5;

    private int screenWidth;
    private int minLeft;
    private int minTop;

    private MoveGestureDetector mMoveDetector;
    private ScaleGestureDetector mScaleDetector;

    int[] oldClick = new int[2];
    int[] newClick = new int[2];

//    private Paint linePaint;    // 中央线的绘制

    int width;
    int height;

    // 保存选中座位
    public List<SeatMo> selectedSeats;

    public SeatTableView(Context context) {
        super(context, null);
        initViews(context);
    }

    public SeatTableView(Context context, AttributeSet attr) {
        super(context, attr);
        initViews(context);
    }

    private void initViews(Context context) {

        initAttribute(context);
        setOnTouchListener(this);     // 设置 点击监听
        setVerticalScrollBarEnabled(true);
        setHorizontalScrollBarEnabled(true);

        // Setup Gesture Detectors
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mMoveDetector = new MoveGestureDetector(context, new MoveListener());
    }

    private void initAttribute(Context context) {

        Resources res = getResources();

        // 默认宽度
        mDefWidth = res.getDimensionPixelSize(R.dimen.def_width);

        // 默认高度
        mDefHeight = res.getDimensionPixelSize(R.dimen.def_height);

        // 宽间距
        mWidthGap = res.getDimensionPixelSize(R.dimen.width_gap);

        // 高间距
        mHeightGap = res.getDimensionPixelSize(R.dimen.height_gap);

        // 左边距
        mPaddingLeft = res.getDimensionPixelSize(R.dimen.common_padding_left);

        // 左边距
        mMarginLeft = res.getDimensionPixelSize(R.dimen.common_padding_left);
        mFocusX = mMarginLeft;

        // 虚拟数据
        initSeatTable();

        selectedSeats = new ArrayList<>();

        screenWidth = res.getDisplayMetrics().widthPixels;

        SeatSale = BitmapFactory.decodeResource(
                context.getResources(),
                R.mipmap.seat_sale);
        SeatSold = BitmapFactory.decodeResource(
                context.getResources(),
                R.mipmap.seat_sold);
        SeatSelected = BitmapFactory.decodeResource(
                context.getResources(),
                R.mipmap.seat_selected);
        SeatLocked = BitmapFactory.decodeResource(
                context.getResources(),
                R.mipmap.seat_locked);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 只绘制可见的座位图
        if(mDefWidth < 10) {
            throw new IllegalArgumentException("the width must > 10, the value is " + mDefWidth);
        }

        mSeatWidth = (int) (mDefWidth* mScaleFactor);
        mSeatHeight = (int) (mDefHeight * mScaleFactor);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        // 可购买座位
        if (seat_sale == null) {
            seat_sale = createScaledBitmap(SeatSale, mSeatWidth, mSeatHeight, true);
        }

        // 红色 已售座位
        if (seat_sold == null) {
            seat_sold = createScaledBitmap(SeatSold, mSeatWidth, mSeatHeight, true);
        }

        // 绿色 我的选择
        if (seat_selected == null) {
            seat_selected = createScaledBitmap(SeatSelected, mSeatWidth, mSeatHeight, true);
        }

        if(seat_locked == null) {
            seat_locked = createScaledBitmap(SeatLocked, mSeatWidth, mSeatHeight, true);
        }

        int m = mFocusY > 0 ? 0 : (int) (-mFocusY / (mSeatHeight + mHeightGap));
        int n = Math.min((int) ((getMeasuredHeight() - mFocusY) / (mSeatHeight + mHeightGap)) + 1, mRowSize);


        for (int i = m; i < n; i++) {
            // 绘制中线, 座位间隔由图片来做, 简化处理
//            int k = (int)(mPaddingTop + mSeatHeight + mHeightGap + 0.5f);
            int k = mFocusX > 0 ? 0 : (int) (-mFocusX / (mSeatWidth + mWidthGap));
            int l = Math.min((int) ((screenWidth - mFocusX) / (mSeatWidth + mWidthGap)) + 1, mColumnSize);

            for (int j = k; j < l; j++) {

                if (seatTable[i][j] != null) {
                    switch (seatTable[i][j].status) {
                        case -1:    // -1 表示锁住
                            canvas.drawBitmap(seat_locked,
                                    j * (mSeatWidth + mWidthGap) - mWidthGap + mPaddingLeft,
                                    i * mSeatHeight + mPaddingTop + i * mHeightGap,
                                    null);
                        case 0: {
                            canvas.drawBitmap(seat_sold,
                                    j * (mSeatWidth + mWidthGap) - mWidthGap + mPaddingLeft,
                                    i * mSeatHeight + mPaddingTop + i * mHeightGap,
                                    null);
                            break;
                        }
                        case 1: {
                            canvas.drawBitmap(seat_sale,
                                    j * (mSeatWidth + mWidthGap) - mWidthGap + mPaddingLeft,
                                    i * mSeatHeight + mPaddingTop + i * mHeightGap,
                                    null);
                            break;
                        }
                        case 2: {
                            canvas.drawBitmap(seat_selected,
                                    j * (mSeatWidth + mWidthGap) - mWidthGap + mPaddingLeft,
                                    i * mSeatHeight + mPaddingTop + i * mHeightGap,
                                    null);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void initSeatTable() {
        seatTable = new SeatMo[mRowSize][mColumnSize];    // mock data
        for (int i = 0; i < mRowSize; i++) {
            for (int j = 0; j < mColumnSize; j++) {
                SeatMo seat = new SeatMo();
                seat.row = i;
                seat.column = j;
                seat.rowName = String.valueOf((char)('A' + i));
                seat.seatName = seat.rowName + "排" + (j + 1) + "座";
                seat.status = randInt(-1,1);
                seatTable[i][j] = seat;
            }
        }
    }

    public  int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();      // scale change since previous event
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 1.5f));
            return true;
        }
    }

    boolean eatClick = true;    // 在缩放和移动的时候,不触发 Click 事件

    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        public boolean onMove(MoveGestureDetector detector) {
            PointF p = detector.getFocusDelta();

            eatClick = p.x > 1 || p.y > 1;  // 移动位移
            mFocusX += p.x;
            mFocusY += p.y;
            return true;
        }
    }

    int[] noSeat = {-1, -1};

    public int[] getClickPoint(MotionEvent event) {

        float currentXPosition = event.getX() - mFocusX;
        float currentYPosition = event.getY() - mFocusY;    // 修正坐标

        float seatWidth = mSeatWidth;
        float seatHeight = mSeatHeight;

        for (int i = 0; i < mRowSize; i++) {
            for (int j = 0; j < mColumnSize; j++) {

                if ((j * (seatWidth + mWidthGap)) < currentXPosition
                        && currentXPosition < (j + 1) * (seatWidth + mWidthGap)
                        && (i * (seatHeight + mHeightGap)) < currentYPosition
                        && currentYPosition < (i + 1) * (seatHeight + mHeightGap)
                        && seatTable[i][j] != null
                        && seatTable[i][j].status >= 1) {   // 1 和 2 才能被点击

                    return new int[]{i, j};
                }
            }
        }
        return noSeat;
    }

    @Override
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
                        seatTable[i][j].status = 2;     // 选中状态
                        selectedSeats.add(seatTable[i][j]);
//                        Toast.makeText(mContext, seatTable[i][j].seatName, Toast.LENGTH_SHORT).show();
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
            int itemWidth = (int) ((mDefWidth + mWidthGap) * mScaleFactor * mColumnSize);
            minLeft = itemWidth - screenWidth;

            /**
             * 大于 0 的意思是说，放大到 超过屏幕了
             */
            mFocusX = minLeft > 0 ?
//                    Math.max((-minLeft + mMarginLeft), Math.min(mFocusX, mMarginLeft)) : mMarginLeft;
                    Math.max(-minLeft + mWidthGap * (mScaleFactor - 1) * mColumnSize, Math.min(mFocusX, mMarginLeft)) : mMarginLeft;

            EasyLogger.d("Linky", "SeatLayoutView:onTouch " + "mFocusX : " + mFocusX);

            //
            minTop = (int) ((mDefHeight + mHeightGap) * mScaleFactor * mRowSize) - getMeasuredHeight();

            // -minTop <= mFocusY <= 0
            // 当 > 0 时，
            mFocusY = minTop > 0 ? Math.max(-minTop+(mHeightGap * (mScaleFactor - 1 ) * mRowSize), Math.min(mFocusY,0)) : 0;

            //
            mMatrix.postScale(mScaleFactor, mScaleFactor);  // 宽高缩放相同的系数；

            mPaddingTop = mFocusY;
            mPaddingLeft = mFocusX;

            // 重新绘制
            int seatWidth = (int) (mDefWidth * mScaleFactor);    // 座位的新宽高
            int seatHeight = (int) (mDefHeight * mScaleFactor);

            // 可购买座位
            seat_sale = createScaledBitmap(SeatSale, seatWidth, seatHeight, true);
            // 红色 已售座位
            seat_sold = createScaledBitmap(SeatSold, seatWidth, seatHeight, true);
            // 绿色 我的选择
            seat_selected = createScaledBitmap(SeatSelected, seatWidth, seatHeight, true);
            // 锁定
            seat_locked = createScaledBitmap(SeatLocked, seatWidth, seatHeight, true);

            invalidate();
            mOnViewChangeListener.onViewChange(mRowSize,mFocusY, mScaleFactor);
        }
        return true;
    }

    public  int getmRowSize(){
        return mRowSize;
    }

    public int getmColumnSize() {
        return mColumnSize;
    }

    private OnViewChangeListener mOnViewChangeListener;
    public void setmOnViewChangeListener(OnViewChangeListener onViewChangeListener) {
        mOnViewChangeListener = onViewChangeListener;
    }
    public interface OnViewChangeListener {
        public void onViewChange(int rowSize, float focusY, float scaleFactor);
    }
}
