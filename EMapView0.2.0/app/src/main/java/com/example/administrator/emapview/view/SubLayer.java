package com.example.administrator.emapview.view;

import android.util.Log;

import com.example.administrator.emapview.UpdatePersistableListener;
import com.ndk.echart.EChartOperation;
import com.ndk.echart.Echart;
import com.ndk.echart.EchartMapview;
import com.ndk.echart.EchartPersistablelayer;

/**
 * Created by Morgan on 2016/12/26.
 */

public class SubLayer {
    /**
     * sublayerId
     */
    private int mSubLayerId;
    /**
     * persistableLayerId
     */
    private int mPersistableLayerId;
    private EChartOperation op;
    private int mMapViewId;
    /**
     * 更新persistableLayer层的接口
     */
    private UpdatePersistableListener listener;

    public void setSubLayerData(int persistableLayerId,EChartOperation op,int mapViewId,UpdatePersistableListener listener){
        mPersistableLayerId  = persistableLayerId;
        this.op = op;
        mMapViewId = mapViewId;
        this.listener = listener;
        CreateSubLayer();
    }

    /**
     * 创建sublayer层
     */
    public void CreateSubLayer(){
        EchartPersistablelayer.EChart_InvokePLayerCreateSubLayer.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerCreateSubLayer.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_CREATE_SUBLAYER);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        byte[] b = op.invoke(hdr.getMsg(), ivk);
        try{
            EchartPersistablelayer.EChart_ResultPLayerCreateSubLayer r = EchartPersistablelayer.EChart_ResultPLayerCreateSubLayer.parseFrom(b);
            assert(mSubLayerId == 0);
            mSubLayerId = r.getSublayerId();
            Log.i("TAG",mSubLayerId+"");
        }
        catch (Exception e){
            assert(false);
            e.printStackTrace();
        }
        finally {
        }


    }

    /**
     * 销毁sublayer
     */
    public void destroySubLayer(){
        EchartPersistablelayer.EChart_InvokePLayerDestroySubLayer.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerDestroySubLayer.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_DESTROY_SUBLAYER);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);
        byte[] b = op.invoke(hdr.getMsg(), ivk);
        mSubLayerId = 0;
//        listener.updatePersistable(mSubLayerId);
//        try{
//            EchartPersistablelayer.EChart_ResultPLayerDestroySubLayer r = EchartPersistablelayer.EChart_ResultPLayerDestroySubLayer.parseFrom(b);
//            assert(mSubLayerId == 0);
////            mSubLayerId = r.getSublayerId();
//            mSubLayerId = 0;
//        }
//        catch (Exception e){
//            assert(false);
//            e.printStackTrace();
//        }
//        finally{
//
//        }
    }

    public void beginDraw(){
        EchartPersistablelayer.EChart_InvokePLayerBeginDraw.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerBeginDraw.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_BEGIN_DRAW);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);

        op.invoke(hdr.getMsg(), ivk);
    }

    public void endDraw(){
        EchartPersistablelayer.EChart_InvokePLayerEndDraw.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerEndDraw.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_END_DRAW);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);

        op.invoke(hdr.getMsg(), ivk);
    }

    /**
     * 多边形
     */
    public void beginPolygon(){
        EchartPersistablelayer.EChart_InvokePLayerBeginPolygon.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerBeginPolygon.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_BEGIN_POLYGON);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);

        op.invoke(hdr.getMsg(), ivk);
    }

    public void endPolygon(){
        EchartPersistablelayer.EChart_InvokePLayerEndPolygon.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerEndPolygon.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_END_POLYGON);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);

        Echart.EChart_Color.Builder color = Echart.EChart_Color.newBuilder();
        color.setG(100);
        color.setA(100);

        //如果不需要填充区域，则可不设置画刷
        Echart.EChart_Brush.Builder brush = Echart.EChart_Brush.newBuilder();
        brush.setColor(color);

        ivk.setBrush(brush);

        op.invoke(hdr.getMsg(), ivk);
    }

    /**
     * 线段
     */
    public void beginPolyLine(){
        EchartPersistablelayer.EChart_InvokePLayerBeginPolyline.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerBeginPolyline.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_BEGIN_POLYLINE);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);

        op.invoke(hdr.getMsg(), ivk);
    }
    public void endPolyLine(){
        EchartPersistablelayer.EChart_InvokePLayerEndPolyline.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerEndPolyline.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_END_POLYLINE);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);
//
//        Echart.EChart_Color.Builder color = Echart.EChart_Color.newBuilder();
//        color.setG(100);
//        color.setA(100);
        //如果不需要填充区域，则可不设置画刷
//        Echart.EChart_Brush.Builder brush = Echart.EChart_Brush.newBuilder();
//        brush.setColor(color);
//        ivk.setBrush(brush);
        op.invoke(hdr.getMsg(), ivk);
    }

    /**
     * 圆
     */
    public void beginCircle(){
        EchartPersistablelayer.EChart_InvokePLayerDrawCircle.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerDrawCircle.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_DRAW_CIRCLE);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);
        ivk.setRadius(200);

        op.invoke(hdr.getMsg(), ivk);
    }


    /**
     * 移动到当前点开始绘制
     * @param x
     * @param y
     */
    public void moveTo(float x, float y){
        EchartPersistablelayer.EChart_InvokePLayerMoveTo.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerMoveTo.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_MOVE_TO);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);

        Echart.EChart_GeoPoint.Builder pt = Echart.EChart_GeoPoint.newBuilder();
        pt.setX(x);
        pt.setY(y);

        ivk.setPt(pt);

        op.invoke(hdr.getMsg(), ivk);
    }

    /**
     * 设置画笔
     * @param r 红色
     * @param g 绿色
     * @param b 蓝色
     * @param lineWidth 线宽
     */
    public void setPen(int r,int g,int b,int lineWidth){
        EchartPersistablelayer.EChart_InvokePLayerSetPen.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerSetPen.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_SET_PEN);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);

        Echart.EChart_Color.Builder color = Echart.EChart_Color.newBuilder();
        color.setR(r);
        color.setG(g);
        color.setB(b);

        Echart.EChart_Pen.Builder pen = Echart.EChart_Pen.newBuilder();
        pen.setWidth(lineWidth);
        pen.setColor(color);

        ivk.setPen(pen);

        op.invoke(hdr.getMsg(), ivk);
    }

    /**
     * 更新PersistableLayer
     */
    public void updatePersistableLayer(){
        EchartMapview.EChart_InvokeMapViewUpdatePersistableLayer.Builder ivk = EchartMapview.EChart_InvokeMapViewUpdatePersistableLayer.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_UPDATE_PERSISTABLE_LAYER);
        ivk.setHdr(hdr);
        ivk.setMapviewId(mMapViewId);

        byte[] b = op.invoke(hdr.getMsg(), ivk);
        try
        {
            EchartMapview.EChart_ResultMapViewUpdatePersistableLayer r = EchartMapview.EChart_ResultMapViewUpdatePersistableLayer.parseFrom(b);
            listener.updatePersistable(r.getContextId());
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

    /**
     * 绘制线段时连接的下一个点
     * @param x
     * @param y
     */
    public void polyLineTo(float x, float y){
        EchartPersistablelayer.EChart_InvokePLayerPolylineLineTo.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerPolylineLineTo.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_POLYLINE_LINE_TO);
        ivk.setHdr(hdr);

        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);

        Echart.EChart_GeoPoint.Builder pt = Echart.EChart_GeoPoint.newBuilder();
        pt.setX(x);
        pt.setY(y);
        ivk.setPt(pt);
        op.invoke(hdr.getMsg(), ivk);
    }

    /**
     * 绘制多边形时连接下一个点
     * @param x
     * @param y
     */
    public void polygonLineTo(float x, float y){
        EchartPersistablelayer.EChart_InvokePLayerPolygonLineTo.Builder ivk = EchartPersistablelayer.EChart_InvokePLayerPolygonLineTo.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_PLAYER_POLYGON_LINE_TO);
        ivk.setHdr(hdr);
        ivk.setLayerId(mPersistableLayerId);
        ivk.setSublayerId(mSubLayerId);
        Echart.EChart_GeoPoint.Builder pt = Echart.EChart_GeoPoint.newBuilder();
        pt.setX(x);
        pt.setY(y);
        ivk.setPt(pt);
        op.invoke(hdr.getMsg(), ivk);
    }

}
