package com.example.administrator.emapview;

import android.view.MotionEvent;

/**
 * Created by Morgan on 2016/12/13.
 */

public interface GestureListener {

    void onGesture(GestureEvent event);
    void onGestureDraw(GestureEvent event);
}
