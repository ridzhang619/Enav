package com.example.administrator.emapview.view;

import com.example.administrator.emapview.UpdateVesselLayerListener;
import com.ndk.echart.EChartOperation;
import com.ndk.echart.Echart;
import com.ndk.echart.EchartMapview;
import com.ndk.echart.EchartVessellayer;

/**
 * Created by Morgan on 2016/12/28.
 */

public class VesselLayer {

    private int mVesselLayerId;
    private EChartOperation op;
    private int mMapViewId;
    /**
     * 更新VesselLayer的回调接口
     */
    private UpdateVesselLayerListener listener;

    public void setVesselLayerData(int mapViewId,EChartOperation op,UpdateVesselLayerListener listener){
        mMapViewId = mapViewId;
        this.op = op;
        this.listener = listener;
        initVesselLayer();
    }

    /**
     * 初始化vesselLayer
     */
    public void  initVesselLayer(){
        EchartMapview.EChart_InvokeMapViewGetVesselLayer.Builder ivk = EchartMapview.EChart_InvokeMapViewGetVesselLayer.newBuilder();
        Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
        hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_GET_VESSEL_LAYER);
        ivk.setHdr(hdr);

        ivk.setMapviewId(mMapViewId);
        byte[] b = op.invoke(hdr.getMsg(), ivk);
        try
        {
            EchartMapview.EChart_ResultMapViewGetVesselLayer r = EchartMapview.EChart_ResultMapViewGetVesselLayer.parseFrom(b);
            assert(mVesselLayerId == 0);
            mVesselLayerId = r.getLayerId();
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

    public void setVesselStaticInfo(){

    }

    /**
     * 更新vesselLayer
     */
    public void updateVesselLayer(int mmsi,int trueHead,int timeStamp,float longtitude,float latitude)
    {
        {
            EchartVessellayer.EChart_InvokeVLayerUpdateVesselsPostionReport.Builder ivk = EchartVessellayer.EChart_InvokeVLayerUpdateVesselsPostionReport.newBuilder();
            Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
            hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_VLAYER_UPDATE_VESSELS_POSITION_REPORT);
            ivk.setHdr(hdr);
            ivk.setLayerId(mVesselLayerId);

            EchartVessellayer.EChart_VesselPostionReport.Builder report = EchartVessellayer.EChart_VesselPostionReport.newBuilder();
            report.setMmsi(mmsi);  //船舶的MMSI号
            report.setTrueHeading(trueHead);  //船头的方向
            report.setTimestamp(timeStamp); //从1970年1月1日零点到现在经过的秒数
            report.setLongitude(longtitude);
            report.setLatitude(latitude);
            ivk.addReports(report);
            byte[] b = op.invoke(hdr.getMsg(), ivk);
        }

        {
            EchartMapview.EChart_InvokeMapViewUpdateVesselLayer.Builder ivk = EchartMapview.EChart_InvokeMapViewUpdateVesselLayer.newBuilder();
            Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
            hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_UPDATE_VESSEL_LAYER);
            ivk.setHdr(hdr);
            ivk.setMapviewId(mMapViewId);

            byte[] b = op.invoke(hdr.getMsg(), ivk);
            try
            {
                EchartMapview.EChart_ResultMapViewUpdateVesselLayer r = EchartMapview.EChart_ResultMapViewUpdateVesselLayer.parseFrom(b);
                listener.updateVesselLayer(r.getContextId());
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
    }


    public void destroyVesselLayer(){

    }


}
