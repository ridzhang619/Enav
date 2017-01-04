package com.example.administrator.emapview;

/**
 * Created by Morgan on 2016/12/13.
 */

public class GestureEvent {
    private int mode;
    public static final int MODE_DOWN  = 1;
    public static final int MODE_BEGIN_DRAG = 2;
    public static final int MODE_DRAGING = 3;
    public static final int MODE_END_DRAG = 4;
    public static final int MODE_BEGIN_ZOOM = 5;
    public static final int MODE_ZOOMING = 6;
    public static final int MODE_END_ZOOM = 7;
    public static final int MODE_UP = 8;
    public static final int MODE_DOUBLE_CLICK = 9;




//    public static final int MODE_DRAG = 1;
//    public static final int MODE_ZOOM_OUT = 2;
//    public static final int MODE_ZOOM_IN = 3;
//    public static final int MODE_POINTER_DOWN = 4;
//    public static final int MODE_POINTER_UP = 5;
//    public static final int MODE_UP = 6;


    private float mScale;
    private float x;
    private float y;




    public void setScale(float scale){
        mScale = scale;
    }

    public float getScale(){
        return mScale;
    }

    public void setType(int mode){
        this.mode = mode;
    }

    public int getType(){
        return mode;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
