package com.example.administrator.emapview.view;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.administrator.emapview.GestureEvent;
import com.example.administrator.emapview.GestureListener;

/**
 * Created by Morgan on 2016/12/21.
 */

public class GestureOnTouch {

    private GestureListener mListener;
    private GestureEvent mEvent =  new GestureEvent();
    private long firstClick;
    private int count;
    private long lastClick;

    public GestureOnTouch(GestureListener listener) {
        mListener = listener;
    }

    public boolean onTouch(MotionEvent event){
        switch(event.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                Log.i("TAG","MODE_DOWN");
                // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
                if (firstClick != 0 && System.currentTimeMillis() - firstClick > 300)
                {
                    count = 0;
                }
                count++;
                if (count == 1) {
                    firstClick = System.currentTimeMillis();
                    mEvent.setX(event.getX());
                    mEvent.setY(event.getY());
                    Log.i("TAG","MODE_DOWN");
                    mEvent.setType(GestureEvent.MODE_DOWN);
                    mListener.onGestureDraw(mEvent);
                }
                else if (count == 2)
                {
                    lastClick = System.currentTimeMillis();
                    // 两次点击小于300ms 也就是连续点击
                    if (lastClick - firstClick < 300)
                    {// 判断是否是执行了双击事件
                        mEvent.setType(GestureEvent.MODE_DOUBLE_CLICK);
                        mListener.onGestureDraw(mEvent);
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                    mEvent.setType(GestureEvent.MODE_UP);
                    mListener.onGestureDraw(mEvent);
                break;
        }
        return true;
    }

}
