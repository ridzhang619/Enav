package com.example.administrator.emapview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.emapview.GestureEvent;
import com.example.administrator.emapview.GestureListener;
import com.example.administrator.emapview.GestureHandleOnTouch;
import com.example.administrator.emapview.R;
import com.example.administrator.emapview.UpdatePersistableListener;
import com.example.administrator.emapview.UpdateVesselLayerListener;
import com.ndk.echart.EChartOperation;
import com.ndk.echart.Echart;
import com.ndk.echart.EchartMapview;
import com.ndk.echart.EchartPersistablelayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Morgan on 2016/11/29.
 */

public class MatrixNavigateMapView extends ImageView implements View.OnTouchListener,GestureListener,UpdatePersistableListener,UpdateVesselLayerListener {

    private Activity mActivity;
    private EChartOperation op;
    /**
     * 海图id
     */
    private int mMapviewId = 0;
    /**
     * 初始化海图显示经度
     */
    public final static double INIT_LONGITUDE = -76.4;
    /**
     * 初始化海图显示纬度
     */
    public final static double INIT_LATITUDE = 39.2;
    private int mPersistableLayerId = 0;
    private int mSubLayerId = 0;
    private Timer mTimer = null;
    /**
     * 初始状态
     */
    private int mode = 0;
    /**
     * 绘制线段模式
     */
    public static final int MODE_DRAW_LINE = 1;
    /**
     * 绘制多边形模式
     */
    public static final int MODE_DRAW_POLYGON = 2;
    /**
     * 绘制圆形模式
     */
    public static final int MODE_DRAW_CIRCLE = 3;
    /**
     * 操作海图模式
     */
    public static final int MODE_HANDLE = 4;

    final static String PNG_FILENAME = "gur.png";

    /**
     * 控制的matrix
     */
    private Matrix matrix = new Matrix();
    private Matrix matrix1 = new Matrix();

    /**
     * 当前位置的matrix
     */
    private Matrix currentMatrix = new Matrix();
    /**
     * 用于记录开始时候的坐标位置
     * */
    private PointF startPoint = new PointF();
    /**
     * 两个手指的开始距离
     * */
    private float startDis;
    /**
     *  bitmap中心点
     *  */
    private PointF midPoint;

    /**
     * 控件的宽度
     */
    private int mViewWidth;
    /**
     * 控件的高度
     */
    private int mViewHeight;

    private boolean isDragRefresh;
    private boolean isZoomRefresh;
    private Bitmap mBitmap;

    /**
     * 手指抬起的时间
     */
    private long upTime = 2000000000;
    private long moveTime;
    /**
     * 上一次手机移动的时间
     */
    private long lastMoveTime;
    /**
     * 上一次手指按下的时间
     */
    private long lastDownTime;
    private int count;
    private TimerTask timeTasker;
    private Timer timer;
    private TaskDrag taskDrag;
    //    private TaskZoom taskZoom;
    private boolean isSet;
    /**
     * map操作对象
     */
    private GestureHandleOnTouch ghot = new GestureHandleOnTouch(this);
    /**
     * 绘制图形操作对象
     */
    private GestureOnTouch got = new GestureOnTouch(this);

    private float dx;
    private float dy;
    private float zoomScale;
    private boolean mDrawing;
    private float currentX;
    private float currentY;
    private SubLayer mSubLayer;


    public MatrixNavigateMapView(Context context) {
        super(context);
    }

    public MatrixNavigateMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MatrixNavigateMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(changed){
            mViewWidth = getWidth();
            mViewHeight = getHeight();
        }
    }

    public Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mBitmap = (Bitmap)msg.obj;
            invalidate();
            isDragRefresh = true;
            long setTime = System.currentTimeMillis();
//            Toast.makeText(mActivity, setTime-upTime+"", Toast.LENGTH_SHORT).show();
//            Toast.makeText(mActivity, "绘制完成", Toast.LENGTH_SHORT).show();

        }
    };

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension(2160,3840);
//    }
//
//    private int measureHeight(int heightMeasureSpec) {
//        int result = 0;
//        int specMode = MeasureSpec.getMode(heightMeasureSpec);
//        int specSize = MeasureSpec.getSize(heightMeasureSpec);
//        if(specMode == MeasureSpec.EXACTLY){
//            result = specSize;
//        }else{
//            result = 200;//默认大小
//            if(specMode == MeasureSpec.AT_MOST){
//                result = Math.min(result,specSize);
//            }
//        }
//        return result;
//    }
//
//    private int measureWidth(int widthMeasureSpec) {
//        int result = 0;
//        int specMode = MeasureSpec.getMode(widthMeasureSpec);
//        int specSize = MeasureSpec.getSize(widthMeasureSpec);
//        if(specMode == MeasureSpec.EXACTLY){
//            result = specSize;
//        }else{
//            result = 200;//默认大小
//            if(specMode == MeasureSpec.AT_MOST){
//                result = Math.min(result,specSize);
//            }
//        }
//        return result;
//    }



    public void  initNavigateMapView(Activity activity,EChartOperation op,int mapViewId){
        mode = MODE_HANDLE;
        this.op = op;
        mActivity = activity;
        mMapviewId = mapViewId;
//        init();
//        initPersistableLayer();
        restartTimer();
        setOnTouchListener(this);
    }



    //保证短时间内不会多次调用update()
    private void restartTimer()
    {
        try
        {
            if (mTimer != null)
            {
                mTimer.cancel();
            }

            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    update();
                }
            }, 500);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {

        }

    }
    private void update()
    {
        EchartMapview.EChart_InvokeMapViewUpdate.Builder ivk = EchartMapview.EChart_InvokeMapViewUpdate.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_UPDATE);
        ivk.setHdr(hdr);
        ivk.setMapviewId(mMapviewId);

        byte[] b = op.invoke(hdr.getMsg(), ivk);

        try
        {
            final EchartMapview.EChart_ResultMapViewUpdate r = EchartMapview.EChart_ResultMapViewUpdate.parseFrom(b);
//            mUpdateQueue.put(r.getContextId());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    draw(r.getContextId());
                }
            }).start();
        }
        catch (Exception e)
        {
            assert(false);
            e.printStackTrace();
        }
        finally
        {

        }

    }

    public void draw(int context){
        {
            EchartMapview.EChart_InvokeMapViewDraw.Builder ivk = EchartMapview.EChart_InvokeMapViewDraw.newBuilder();
            Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
            hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_DRAW);
            ivk.setHdr(hdr);
            ivk.setMapviewId(mMapviewId);
            ivk.setContextId(context);
            op.invoke(hdr.getMsg(), ivk);
        }
        //保证在ui线程执行
        mBitmap = op.getBitmap(mMapviewId);
        sendBitmapMessage();
    }

    private void sendBitmapMessage() {
        Message msg = new Message();
        msg.obj = mBitmap;
        handler.sendMessage(msg);
    }

    public void refreshDrag(float dx, float dy){
        if (mMapviewId != 0){
            EchartMapview.EChart_InvokeMapViewMove.Builder ivk = EchartMapview.EChart_InvokeMapViewMove.newBuilder();
            Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
            hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_MOVE);
            ivk.setHdr(hdr);
            ivk.setMapviewId(mMapviewId);
            ivk.setCx((int)-dx);
            ivk.setCy((int)-dy);
            op.invoke(hdr.getMsg(), ivk);
//                if(isDragRefresh){
            restartTimer();
//                    isDragRefresh = false;
//                }
        }
    }
    private void refreshZoom(float zoomScale) {
        if (mMapviewId != 0)
        {
            EchartMapview.EChart_InvokeMapViewSetScaleFactor.Builder ivk = EchartMapview.EChart_InvokeMapViewSetScaleFactor.newBuilder();
            Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
            hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_SET_SCALE_FACTOR);
            ivk.setHdr(hdr);
            ivk.setMapviewId(mMapviewId);
            ivk.setFactor(zoomScale);
            op.invoke(hdr.getMsg(), ivk);
//            if(isZoomRefresh){
            restartTimer();
//                isZoomRefresh = false;
//            }
        }
    }

    public void setSubLayer(SubLayer layer){
        mSubLayer = layer;
    }

    /**
     * 回调更新persistableLayer层
     * @param contextId
     */
    @Override
    public void updatePersistable(int contextId) {
            draw(contextId);
    }

    /**
     * 回调更新vesselLayer层
     * @param contextId
     */
    @Override
    public void updateVesselLayer(int contextId) {
        draw(contextId);
    }


    class TaskDrag implements  Runnable{
        private  float mDx;
        private  float mDy;
        public TaskDrag(float dx,float dy) {
            mDx = dx;
            mDy = dy;
        }
        @Override
        public void run() {
            refreshDrag(mDx,mDy);//拖动之后刷新
        }
    }
//    class TaskZoom implements Runnable{
//        @Override
//        public void run() {
//            refreshZoom();//缩放之后刷新
//        }
//    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //判断当前选择的模式,操作海图模式和绘制图形模型,实现不同的接口,回调不同的手势
        if(mode == MODE_HANDLE){
            ghot.doTouch(event);
        }else{
            got.onTouch(event);
        }
        return true;
    }
    @Override
    public void onGesture(GestureEvent event) {
        switch(event.getType()){
            case GestureEvent.MODE_DOWN:
                break;
            //开始拖动
            case GestureEvent.MODE_BEGIN_DRAG:
                break;
            //拖动中不断获取x,y偏移量
            case GestureEvent.MODE_DRAGING:
                if(isDragRefresh){
                    dx = event.getX()/3;
                    dy = event.getY()/3;
                    matrix.postTranslate(dx,dy);
                    invalidate();
                }
                break;
            //停止拖动时更新海图
            case GestureEvent.MODE_END_DRAG:
//                Toast.makeText(mActivity, "MMM", Toast.LENGTH_SHORT).show();
                if(isDragRefresh){
                    refreshDrag(dx,dy);
//                    mSubLayer.mo  veTo(event.getX(),event.getY());
//                    drawPng();
                    isDragRefresh = false;
                }
                break;
            //开始缩放
            case GestureEvent.MODE_BEGIN_ZOOM:
                midPoint = mid();
                break;
            //缩放中
            case GestureEvent.MODE_ZOOMING:
                if(isDragRefresh){
                    zoomScale = event.getScale();
                    matrix.postScale(zoomScale,zoomScale,midPoint.x,midPoint.y);
                    invalidate();
                }
                break;
            //停止缩放
            case GestureEvent.MODE_END_ZOOM:
                if(isDragRefresh){
                    refreshZoom(zoomScale);
                    isDragRefresh = false;
                }
                break;
        }
    }

    @Override
    public void onGestureDraw(GestureEvent event) {
        switch(event.getType()){
            case GestureEvent.MODE_DOWN:
                currentX = event.getX();
                currentY = event.getY();
                break;
            case GestureEvent.MODE_DOUBLE_CLICK:
                //双击开始记录坐标
                onDoubleClick(currentX,currentY);
                break;
            case GestureEvent.MODE_UP:
                if (mDrawing){
                    onSingleClick(currentX, currentY);
                    mSubLayer.updatePersistableLayer();
                }
                break;
        }
    }

    /**
     * 判断当前绘制模式,分别为线段,多边形区域,圆
     * @param x
     * @param y
     */
    private void onDoubleClick(float x, float y){
        if(mode == MODE_DRAW_LINE){
            drawLine(x,y,255,0,0,6);
        }else if(mode == MODE_DRAW_POLYGON){
            drawPolygon(x,y,0,255,0,6);
        }else if(mode == MODE_DRAW_CIRCLE){
            drawCircle(x,y,0,0,255,6);
        }
    }

    private void onSingleClick(float x, float y){
        if(mode == MODE_DRAW_LINE){
            Toast.makeText(mActivity, "singleClickL", Toast.LENGTH_SHORT).show();
            mSubLayer.polyLineTo(x,y);
        }else if(mode == MODE_DRAW_POLYGON){
            Toast.makeText(mActivity, "singleClickP", Toast.LENGTH_SHORT).show();
            mSubLayer.polygonLineTo(x,y);
        }else if(mode == MODE_DRAW_CIRCLE){
//            drawCircle();
            {
                mDrawing = false;
                mSubLayer.endDraw();
                mSubLayer.updatePersistableLayer();
            }
            Toast.makeText(mActivity, "singleClickC", Toast.LENGTH_SHORT).show();
        }
    }

    public void setDrawMode(int mode){
        this.mode  = mode;
    }

    /**
     * 设置画笔样式
     * @param red R
     * @param green G
     * @param blue B
     * @param lineWidth 线宽
     */
    public void setPenStyle(int red,int green,int blue,int lineWidth){
        mSubLayer.setPen(red,green,blue,lineWidth);
    }


    private void drawCircle(float x,float y,int r, int g, int b, int lineWidth) {
        if (!mDrawing){
            mDrawing = true;
            mSubLayer.beginDraw();
            mSubLayer.moveTo(x, y);
            mSubLayer.setPen(r,g,b,lineWidth);
            mSubLayer.beginCircle();
        }
    }

    private void drawPolygon(float x,float y,int r, int g, int b, int lineWidth) {
        if (!mDrawing){
            mDrawing = true;
            mSubLayer.beginDraw();
            mSubLayer.moveTo(x, y);
//            PolygonLineTo(x,y);
            mSubLayer.setPen(r,g,b,lineWidth);
            mSubLayer.beginPolygon();
//            BeginPolyLine();
        }else{
            mDrawing = false;
            mSubLayer.endPolygon();
//            EndPolyLine();
            mSubLayer.endDraw();
            //只需要更新持久化图层即可,可提高更新效率
            mSubLayer.updatePersistableLayer();
        }
    }

    private void drawLine(float x,float y,int r, int g, int b, int lineWidth) {
        if (!mDrawing){
            mDrawing = true;
            mSubLayer.beginDraw();
            mSubLayer.moveTo(x,y);
//            PolygonLineMoveTo(x,y); 
            mSubLayer.setPen(r,g,b,lineWidth);
            mSubLayer.beginPolyLine();
        }else{
            mDrawing = false;
            mSubLayer.endPolyLine();
            mSubLayer.endDraw();
            //只需要更新持久化图层即可,可提高更新效率
            mSubLayer.updatePersistableLayer();
        }
    }
    private void drawPng() {
        EchartPersistablelayer.EChart_InvokePLayerDrawPng.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerDrawPng.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_DRAW_PNG);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);
        ivk.setFilename(PNG_FILENAME);

        op.invoke(hdr.getMsg(), ivk);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("TAG","onDraw");
        if (mBitmap != null) {
//            matrix.postTranslate(-mBitmap.getWidth()/3,-mBitmap.getHeight()/3);
//            matrix.postScale(3,3);
            canvas.drawBitmap(mBitmap, matrix, null);
            matrix.reset();
        }
    }
    /** 计算bitmap中心点 */
    private PointF mid() {
        float midX = mBitmap.getWidth() / 2;
        float midY = mBitmap.getHeight() / 2;
        return new PointF(midX, midY);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }
}
