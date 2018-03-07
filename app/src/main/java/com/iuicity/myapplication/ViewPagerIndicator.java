package com.iuicity.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

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
    private float mScrollPivotX = 0.25f;

    private List<String> mTitles;
    private Paint mTextPaint;
    private Context mContext;
    private List<RectF> mRectFs = new ArrayList<>();
    private float mCurrentLeft = 0f;
    private float mCurrentRight = 0f;
    private int mHeight;
    private LinearInterpolator mStartInterpolator;
    private int mWidth;
    private float mTotalWidth = 0f;

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
        mStartInterpolator = new LinearInterpolator();
    }


    public void setupViewpager(ViewPager viewPager) {
        Log.d("ViewPagerIndicator", "setupViewpager");

        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            return;
        }
        mTitles = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            mTitles.add(String.valueOf(adapter.getPageTitle(i)));
        }
        invalidate();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mRectFs == null || mRectFs.size() == 0) {
                    return;
                }

                int currentPosition = Math.min(mRectFs.size() - 1, position);
                int nextPosition = Math.min(mRectFs.size() - 1, position + 1);
                RectF current = mRectFs.get(currentPosition);
                RectF next = mRectFs.get(nextPosition);

                float interpolation = mStartInterpolator.getInterpolation(positionOffset);
                mCurrentLeft = current.left + (next.left - current.left) * interpolation;
                mCurrentRight = current.right + (next.right - current.right) * interpolation;
                float scrollTo = (current.right + current.left) / 2 - mWidth * mScrollPivotX;
                float nextScrollTo = (next.right + next.left) / 2 - mWidth * mScrollPivotX;

                int scrollX = (int) (scrollTo + (nextScrollTo - scrollTo) * interpolation);
                if ((mCurrentLeft + mCurrentRight) / 2 > mWidth * mScrollPivotX && !isMoveLast(scrollX)) {
                    scrollTo(scrollX, 0);
                }
                invalidate();
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private boolean isMoveLast(int scrollX) {
        return scrollX > mTotalWidth - mWidth;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mHeight = h;
        mWidth = w;
        calculatePosition();
    }

    private void calculatePosition() {
        Rect bounds = new Rect();
        float lastRight = mSpace;
        for (int i = 0; i < mTitles.size(); i++) {
            mTextPaint.getTextBounds(mTitles.get(i), 0, mTitles.get(i).length(), bounds);
            RectF rectF = new RectF();
            rectF.top = mHeight / 2 - (bounds.bottom - bounds.top) / 2;
            rectF.bottom = rectF.top + (bounds.bottom - bounds.top);
            rectF.left = lastRight + mSpace;
            rectF.right = rectF.left + (bounds.right - bounds.left);
            lastRight = rectF.right;

            mRectFs.add(rectF);

            if (i == 0) {
                mCurrentLeft = 0f;
                mCurrentRight = mCurrentLeft + (bounds.right - bounds.left) + mSpace * 2;
            }

            mTotalWidth = rectF.right;
        }
        mTotalWidth += mSpace;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mTitles == null) {
            return;
        }

        //绘制底色
        mTextPaint.setColor(mNormalColor);
        drawTexts(canvas);

        //绘制选中部分
        canvas.save();
        canvas.clipRect(mCurrentLeft, 0, mCurrentRight, mHeight);
        mTextPaint.setColor(mSelectColor);
        drawTexts(canvas);
        canvas.restore();
    }

    private void drawTexts(Canvas canvas) {
        for (int i = 0; i < mTitles.size(); i++) {
            canvas.drawText(mTitles.get(i), mRectFs.get(i).left, mRectFs.get(i).bottom, mTextPaint);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private float mLastX = 0f;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = (int) (mLastX - event.getX());
                scrollTo(offsetX, 0);
                break;
            case MotionEvent.ACTION_UP:
                performClick();
                mLastX = 0f;
                break;

        }
        return true;
    }


}
