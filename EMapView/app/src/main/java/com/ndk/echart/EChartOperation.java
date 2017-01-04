package com.ndk.echart;
import android.graphics.Bitmap;

import java.util.*;

import com.google.protobuf.*;

import com.ndk.echart.Echart.*;
import com.ndk.echart.EchartMapview.*;



public final class EChartOperation
{
    private Map<Integer, Bitmap> mBitmapMap = new HashMap<Integer, Bitmap>();

    public EChartOperation(){}

    public final Bitmap getBitmap(int mapviewId)
    {
        assert(mBitmapMap.containsKey(mapviewId));
        return mBitmapMap.get(mapviewId);
    }

    public byte[] invoke(EChart_MsgType msg, GeneratedMessageLite.Builder invoke)
    {
        byte[] result = null;
        try
        {
            byte[] ivk = invoke.build().toByteArray();
            if (msg == EChart_MsgType.MSG_INVOKE_MAPVIEW_CREATE)
            {
                EChart_InvokeMapViewCreate.Builder b = (EChart_InvokeMapViewCreate.Builder)invoke;
                Bitmap bm = Bitmap.createBitmap(b.getMaxWidth(), b.getMaxHeight(), Bitmap.Config.ARGB_8888);

                result = ndkInvoke(msg.getNumber(), ivk, bm);
                EChart_ResultMapViewCreate r = EChart_ResultMapViewCreate.parseFrom(result);
                assert(r.getHdr().getMsg() == msg);

                assert(!mBitmapMap.containsKey(r.getMapviewId()));
                mBitmapMap.put(r.getMapviewId(), bm);
            }
            else if (msg == EChart_MsgType.MSG_INVOKE_MAPVIEW_DRAW)
            {
                EChart_InvokeMapViewDraw.Builder b = (EChart_InvokeMapViewDraw.Builder)invoke;
                Bitmap bm = getBitmap(b.getMapviewId());

                result = ndkInvoke(msg.getNumber(), ivk, bm);
                EChart_ResultMapViewDraw r = EChart_ResultMapViewDraw.parseFrom(result);
                assert(r.getHdr().getMsg() == msg);

            }
            else
            {
                if (msg == EChart_MsgType.MSG_INVOKE_INIT)
                {
                    result = ndkInitialize(ivk);
                }
                else if (msg == EChart_MsgType.MSG_INVOKE_UNINIT)
                {
                    result = ndkUninitialize(ivk);
                }
                else
                {
                    result = ndkInvoke(msg.getNumber(), ivk, null);
                    if (msg == EChart_MsgType.MSG_RESULT_MAPVIEW_DESTROY)
                    {
                        EChart_InvokeMapViewDestroy.Builder b = (EChart_InvokeMapViewDestroy.Builder)invoke;
                        assert(mBitmapMap.containsKey(b.getMapviewId()));
                        mBitmapMap.remove(b.getMapviewId());
                    }

                    else
                    {
                        assert(msg != null);
                    }
                }
            }

        }
        catch (Exception e)
        {
            assert(false);
            e.printStackTrace();
        }
        finally
        {

        }

        assert(result != null);
        return result;
    }

    private native byte[] ndkInitialize(byte[] invoke);
    private native byte[] ndkUninitialize(byte[] invoke);

    private native byte[] ndkInvoke(int msg, byte[] invoke, Object param);
    static
    {
        System.loadLibrary("proj");
        System.loadLibrary("protobuf-lite");
        System.loadLibrary("png");
        System.loadLibrary("pixman");
        System.loadLibrary("freetype");
        System.loadLibrary("cairo");
        System.loadLibrary("echart");
    }


}
