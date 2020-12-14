package com.example.copwatch.utils;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class CustomScroller extends Scroller {

    private int mDuration;

    public CustomScroller(Context context, int mDuration) {
        super(context);
        this.mDuration = mDuration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
