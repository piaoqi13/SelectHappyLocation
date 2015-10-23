package com.fuwei.selecthappylocation.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.model.SeatMo;

import java.util.Random;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * 座位显示等的 View
 * @author YanLu
 */
public class SeatTableView extends View {

    Bitmap seat_sale, seat_sold, seat_selected;
    Bitmap SeatSale, SeatSold, SeatSelected;

    private int mSeatWidth;  // 座位图 宽
    private int mSeatHeight; // 座位图 高

    private int mDefWidth;   // 初始值 宽
    private int mDefHeight;  // 初始值 高

    private int mWidthGap;     //
    private int mHeightGap;    //

    // 放大率和移动位置
    public float mScaleFactor = 1.f;
    public float mPaddingTop = 20.0f;        // View 的开始位置 X 坐标 以及左右边界；
    public float mPaddingLeft = .0f;         // View 的开始位置 Y 坐标 以及上下边界；

    private SeatMo[][] seatTable;

    private int rowSize = 5;
    private int columnSize = 5;

//    private Paint linePaint;    // 中央线的绘制

    int width;
    int height;

    public SeatTableView(Context context) {
        super(context, null);
    }

    public SeatTableView(Context context, AttributeSet attr) {
        super(context, attr);

        Resources res = getResources();
        mDefWidth = res.getDimensionPixelSize(R.dimen.def_width);
        mDefHeight = res.getDimensionPixelSize(R.dimen.def_height);

        mWidthGap = res.getDimensionPixelSize(R.dimen.width_gap);
        mHeightGap = res.getDimensionPixelSize(R.dimen.height_gap);

        initSeatTable();

        SeatSale = BitmapFactory.decodeResource(
                context.getResources(),
                R.mipmap.seat_sale);
        SeatSold = BitmapFactory.decodeResource(
                context.getResources(),
                R.mipmap.seat_sold);
        SeatSelected = BitmapFactory.decodeResource(
                context.getResources(),
                R.mipmap.seat_selected);

        setVerticalScrollBarEnabled(true);
        setHorizontalScrollBarEnabled(true);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 只绘制可见的座位图
        if(mDefWidth < 10) {
            throw new IllegalArgumentException("the width must > 10, the value is " + mDefWidth);
        }

        mSeatWidth = (int) (mDefWidth * mScaleFactor);
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
        if (seat_sold == null) {
            seat_sold = createScaledBitmap(SeatSelected, mSeatWidth, mSeatHeight, true);
        }

        // 画座位
        int m = (int)(mPaddingLeft + mSeatWidth);
        m = m >= 0 ? 0 : -m / mSeatWidth;
        int n = Math.min(rowSize - 1, m + (height / mSeatHeight) + 2);  // 两边多显示1列,避免临界的突然消失的现象

        for (int i = m; i <= n; i++) {
            // 绘制中线, 座位间隔由图片来做, 简化处理
            int k = (int)(mPaddingTop + mSeatWidth + 0.5f);
            k = k > 0 ? 0 : -k / mSeatWidth;                                 // 移动距离不可能出现移到 - rowSize
            int l = Math.min(columnSize - 1, k + (width / mSeatWidth) + 2);  // 两边多显示 1 列,避免临界的突然消失的现象

            for (int j = k; j <= l; j++) {

                if (seatTable[i][j] != null) {
                    switch (seatTable[i][j].status) {
                        case -1:
                        case 0: {
                            canvas.drawBitmap(seat_sold,
                                    j * (mSeatWidth) + mPaddingTop + (j - 1) * mWidthGap,
                                    i * (mSeatHeight) + mPaddingLeft + i * mHeightGap,
                                    null);
                            break;
                        }
                        case 1: {
                            canvas.drawBitmap(seat_sale,
                                    j * (mSeatWidth) + mPaddingTop + (j - 1) * mWidthGap,
                                    i * (mSeatHeight) + mPaddingLeft + i * mHeightGap,
                                    null);
                            break;
                        }
                        case 2: {
                            canvas.drawBitmap(seat_selected,
                                    j * (mSeatWidth) + mPaddingTop + (j - 1) * mWidthGap,
                                    i * (mSeatHeight) + mPaddingLeft + i * mHeightGap,
                                    null);
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
            }
        }
    }

    private void initSeatTable() {
        seatTable = new SeatMo[rowSize][columnSize];// mock data
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                SeatMo seat = new SeatMo();
                seat.row = i;
                seat.column = j;
                seat.rowName = String.valueOf((char)('A' + i));
                seat.seatName = seat.rowName + "排" + (j + 1) + "座";
                seat.status = randInt(-1,1);
//                seatTable[i][j] = seat.status == -2 ? null : seat;
                seatTable[i][j] = seat;
            }
        }
    }

    public  int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public SeatMo[][] getSeatTable() {
        return seatTable;
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public int getmDefWidth() {
        return mDefWidth;
    }

    public int getmDefHeight() {
        return mDefHeight;
    }

    public int getmSeatWidth() {
        return mSeatWidth;
    }

}
