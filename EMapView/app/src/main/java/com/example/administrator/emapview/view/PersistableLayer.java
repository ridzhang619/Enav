package com.example.administrator.emapview.view;

import com.ndk.echart.EChartOperation;
import com.ndk.echart.Echart;
import com.ndk.echart.EchartMapview;

/**
 * Created by Morgan on 2017/1/3.
 */

public class PersistableLayer {

    private int mMapviewId;
    private int mPersistableLayerId;
    private EChartOperation op;

    public PersistableLayer(int mMapviewId, EChartOperation op) {
        this.mMapviewId = mMapviewId;
        this.op = op;
        createPersistableLayer();
    }

    private void createPersistableLayer(){
        {
            EchartMapview.EChart_InvokeMapViewGetPersistableLayer.Builder ivk = EchartMapview.EChart_InvokeMapViewGetPersistableLayer.newBuilder();
            Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
            hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_GET_PERSISTABLE_LAYER);
            ivk.setHdr(hdr);

            ivk.setMapviewId(mMapviewId);
            byte[] b = op.invoke(hdr.getMsg(), ivk);
            try
            {
                EchartMapview.EChart_ResultMapViewGetPersistableLayer r = EchartMapview.EChart_ResultMapViewGetPersistableLayer.parseFrom(b);
                assert(mPersistableLayerId == 0);
                mPersistableLayerId = r.getLayerId();
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
    public int getPersistableId(){
        return mPersistableLayerId;
    }
}
