package com.example.administrator.emapview;

import android.gesture.Gesture;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by Morgan on 2016/12/13.
 */

public class GestureHandleOnTouch {

    private GestureListener mGestureListener;
    private int mode = 0;
    /**
     * 记录手指起点
     */
    private PointF startPoint = new PointF();
    /**
     * 记录开始手指间距离
     */
    private float startDis;
    private GestureEvent gestureEvent =  new GestureEvent();
    /**
     * 手指缩放的倍数
     */
    private float scale;
    /**
     * 手指在x轴方向上的偏移量
     */
    private float dx;
    /**
     * 手指在y轴方向上的偏移量
     */
    private float dy;


    public GestureHandleOnTouch(GestureListener listener){
        mGestureListener = listener;
    }

    public boolean doTouch(MotionEvent event){
        switch(event.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                mode = GestureEvent.MODE_DOWN;
                Log.i("TAG","ACTION_DOWN");
                startPoint.set(event.getX(),event.getY());
                gestureEvent.setType(GestureEvent.MODE_DOWN);
                mGestureListener.onGesture(gestureEvent);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                startDis = distance(event);//记录开始两指之间的距离
//                Log.i("TAG",startDis+"startDis");
//                gestureEvent.setType(GestureEvent.MODE_BEGIN_ZOOM);
//                mGestureListener.onGesture(gestureEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                //判断如果有一个触点则为drag模式
                if(event.getPointerCount() == 1){
                    if(mode==GestureEvent.MODE_DOWN
                            || mode==GestureEvent.MODE_BEGIN_DRAG
                            || mode==GestureEvent.MODE_DRAGING){
                        dx = event.getX() - startPoint.x;
                        dy = event.getY() - startPoint.y;
                        //当有一个触点时候,给一个活动范围,在范围内视为begin_drag,大于这个范围则视为draging状态
                        if(Math.abs(dx)<1&&Math.abs(dy)<1){
                            mode = GestureEvent.MODE_BEGIN_DRAG;
                            gestureEvent.setType(GestureEvent.MODE_BEGIN_DRAG);
                            mGestureListener.onGesture(gestureEvent);
                        }else{
                            mode = GestureEvent.MODE_DRAGING;
                            gestureEvent.setX(dx);
                            gestureEvent.setY(dy);
                            gestureEvent.setType(GestureEvent.MODE_DRAGING);
                            mGestureListener.onGesture(gestureEvent);
                        }
                    }
                }else if(event.getPointerCount() == 2){//如果有两个触点则为zoom模式
                    if(mode != GestureEvent.MODE_DRAGING){
                        float endDis = distance(event);
                        if(endDis>10f){
                            scale = endDis/startDis;//计算zoom比例
                            Log.i("TAG",scale+"");
                            //给定一个缩放的范围,scale在0.99~1.01之间被视为begin_zoom模式
                            if(Math.abs(scale)<1.01&&Math.abs(scale)>0.99){
                                gestureEvent.setType(GestureEvent.MODE_BEGIN_ZOOM);
                                mGestureListener.onGesture(gestureEvent);
                            }else{
                                if(scale<0.6){
                                    scale = 0.6f;
                                }else if(scale > 2){
                                    scale = 2;
                                }
                                gestureEvent.setScale(scale);
                                gestureEvent.setType(GestureEvent.MODE_ZOOMING);
                                mGestureListener.onGesture(gestureEvent);
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.i("TAG",mode+"");
                if(event.getPointerCount() == 2&&mode!=GestureEvent.MODE_DRAGING){
                    gestureEvent.setType(GestureEvent.MODE_END_ZOOM);
                    mGestureListener.onGesture(gestureEvent);
                }
                mode = 0;
                break;
            case MotionEvent.ACTION_UP:
                /**
                 * 此时要判断一下当前模式,如果在drag模式再进行回调,如果不判断,zoom模式下也会回调up,则每次zoom之后抬手
                 * 都会执行MODE_END_DRAG,造成zoom之后又会执行一次drag,导致zoom之后的海图位置衔接不上
                 */
                if(mode == GestureEvent.MODE_DRAGING){
                    gestureEvent.setType(GestureEvent.MODE_END_DRAG);
                    mGestureListener.onGesture(gestureEvent);
                }
                break;
        }

        return false;
    }

    /** 计算两个手指间的距离 */
    private float distance(MotionEvent event) {
        float dx = 0f;
        float dy = 0f;
        try {
            dx = event.getX(1) - event.getX(0);
            dy = event.getY(1) - event.getY(0);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        /** 使用勾股定理返回两点之间的距离 */
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
    /** 计算两个手指间的距离 */
    private float distance1(MotionEvent event) {
        float dx = 0f;
        float dy = 0f;
        try {
            dx = event.getX(1) - event.getX(0);
            dy = event.getY(1) - event.getY(0);
            dx = dx * 1.5f;
            dy = dy * 1.5f;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        /** 使用勾股定理返回两点之间的距离 */
        return (float) Math.sqrt(dx * dx + dy * dy);
    }


}
