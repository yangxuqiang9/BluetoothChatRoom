package com.example.a328789.bluetoothcharroom.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by 328789 on 2016/11/4.
 */

public class TopScrollListview extends RecyclerView {
    public TopScrollListview(Context context) {
        super(context);
    }

    public TopScrollListview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TopScrollListview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, 400, isTouchEvent);
    }
}
