/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.viewer;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Overlay layout to intercept common touch events
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jan - 2017
 */
class ViewerOverlayLayout extends RelativeLayout {
    private GestureDetectorCompat gestureDetector;
    private SingleTapListener     singleTapListener;
    private OnTouchStartListener  touchStartListener;

    interface SingleTapListener {
        boolean onSingleTap(final MotionEvent e);
    }

    interface OnTouchStartListener {
        void onTouchStart(final MotionEvent ev);
    }

    public ViewerOverlayLayout(final Context context) {
        this(context, null);
    }

    public ViewerOverlayLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewerOverlayLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.gestureDetector = new GestureDetectorCompat(getContext(), new A());
    }

    void setOnSingleTapListener(final SingleTapListener listener) {
        this.singleTapListener = listener;
    }

    public void setOnScrollListener(final OnTouchStartListener listener) {
        this.touchStartListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent ev) {
        touchStartListener.onTouchStart(ev);
        gestureDetector.onTouchEvent(ev);
        return false;
    }

    private class A extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(final MotionEvent e) {
            if (null != singleTapListener) {
                return singleTapListener.onSingleTap(e);
            } else {
                return false;
            }
        }
    }
}
