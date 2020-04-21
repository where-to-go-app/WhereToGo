package com.wheretogo.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yandex.mapkit.mapview.MapView;

public class CustomMapView extends MapView {
    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /**
         * Request all parents to relinquish the touch events
         */
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
