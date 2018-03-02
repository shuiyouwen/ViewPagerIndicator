package com.iuicity.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shui on 2018/2/27.
 */

public class ViewPagerIndicator extends View {
    private float mTextSize;
    private int mNormalColor = Color.BLACK;
    private int mSelectColor = Color.RED;
    private int mSpace;//间隙

    private List<String> mTitles;
    private Paint mTextPaint;
    private int[] mPositions;//文字position
    private int[] mTextWidths;//每个标题的宽度存储
    private Context mContext;
    private int mTextY;//文字的y坐标
    private int mHeight;
    private float mCurrentLeft = 0;//显示红色区域的起始位置
    private float mCurrentRight = 0;//当前选中的position
    private int mCurrentPosition;//当前选中条目

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mTextSize = dip2px(mContext, 15);
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);

        mSpace = dip2px(mContext, 10);
    }

    public void setupViewpager(ViewPager viewPager) {
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            return;
        }
        mTitles = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            mTitles.add(String.valueOf(adapter.getPageTitle(i)));
        }
        calculatePosition();

        invalidate();

        final float[] last = {0};

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0 && positionOffset > last[0]) {
                    //向左滑动
                    Log.d("ViewPagerIndicator", "向左滑动");
                    float leftStart = mPositions[mCurrentPosition];
                    float leftEnd = mPositions[mCurrentPosition + 1];
                    float rightStart = leftStart + mTextWidths[mCurrentPosition];
                    float rightEnd = leftEnd + mTextWidths[mCurrentPosition + 1];
                    mCurrentLeft = leftStart + (leftEnd - leftStart) * positionOffset;
                    mCurrentRight = rightStart + (rightEnd - rightStart) * positionOffset;
                } else if (positionOffset > 0 && positionOffset < last[0]) {
                    //向右滑动
                    Log.d("ViewPagerIndicator", "向右滑动");
                    float leftStart = mPositions[mCurrentPosition];
                    float leftEnd = mPositions[mCurrentPosition - 1];
                    float rightStart = leftStart + mTextWidths[mCurrentPosition];
                    float rightEnd = leftEnd + mTextWidths[mCurrentPosition - 1];
                    mCurrentLeft = leftStart + (leftEnd - leftStart) * (1 - positionOffset);
                    mCurrentRight = rightStart + (rightEnd - rightStart) * (1 - positionOffset);
                }
                last[0] = positionOffset;
                invalidate();
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mHeight = h;
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(mTitles.get(0), 0, mTitles.get(0).length(), bounds);
        mTextY = h / 2 - (bounds.top - bounds.bottom) / 2;
    }

    private void calculatePosition() {
        mTextWidths = new int[mTitles.size()];
        mPositions = new int[mTitles.size()];
        float end = 0;
        for (int i = 0; i < mTitles.size(); i++) {
            float measureText = mTextPaint.measureText(mTitles.get(i));
            mTextWidths[i] = (int) measureText;
            if (i == 0) {
                mPositions[i] = mSpace;
            } else {
                mPositions[i] = (int) (end + mSpace);
            }
            end = mPositions[i] + measureText;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mTitles == null) {
            return;
        }

        //绘制底色
        mTextPaint.setColor(mNormalColor);
        for (int i = 0; i < mTitles.size(); i++) {
            canvas.drawText(mTitles.get(i), mPositions[i], mTextY, mTextPaint);
        }

        //绘制选中部分
        canvas.save();
        canvas.clipRect(mCurrentLeft, 0, mCurrentRight, mHeight);
        mTextPaint.setColor(mSelectColor);
        for (int i = 0; i < mTitles.size(); i++) {
            canvas.drawText(mTitles.get(i), mPositions[i], mTextY, mTextPaint);
        }
        canvas.restore();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }
}
