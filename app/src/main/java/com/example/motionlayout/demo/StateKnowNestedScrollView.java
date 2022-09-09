package com.example.motionlayout.demo;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class StateKnowNestedScrollView extends NestedScrollView {
    private static final int DELAY_MILLIS  = 100;
    private long lastScrollUpdate = -1;

    private ScrollStateListener mScrollStateListener;

    public void setScrollStateListener(ScrollStateListener scrollStateListener) {
        this.mScrollStateListener = scrollStateListener;
    }

    private final Runnable scrollerTask = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > 100) {
                lastScrollUpdate = -1;
                if (mScrollStateListener != null) {
                    mScrollStateListener.onStopScroll();
                }
            } else {
                postDelayed(this, DELAY_MILLIS);
            }
        }
    };

    public StateKnowNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public StateKnowNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StateKnowNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollStateListener != null) {
            mScrollStateListener.onStartScroll(getScrollY());
        }
        if (lastScrollUpdate == -1) {
            postDelayed(scrollerTask, DELAY_MILLIS);
        }
        lastScrollUpdate = System.currentTimeMillis();
    }

   public interface ScrollStateListener {
        void onStartScroll(float dy);
        void onStopScroll();
    }
}
