package com.example.administrator.emapview.view;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.administrator.emapview.R;
import com.ndk.echart.EChartOperation;
import com.ndk.echart.Echart;
import com.ndk.echart.EchartMapview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Morgan on 2017/1/3.
 */

public class BaseMapLayer {

    private EChartOperation op = new EChartOperation();
    private int mMapviewId;
    private Activity mActivity;
    private ImageView mImg;
    /**
     * 初始化海图显示经度
     */
    public final static double INIT_LONGITUDE = -76.4;
    /**
     * 初始化海图显示纬度
     */
    public final static double INIT_LATITUDE = 39.2;

    public BaseMapLayer(Activity activity, ImageView imageView) {
        mImg = imageView;
        mActivity = activity;
        createBaseMapLayer();
    }

    private void createBaseMapLayer(){

        copy(R.raw.res_db, "res.db");
        copy(R.raw.us5md12m_000_ach, "US5MD12M.000.ach");
        copy(R.raw.us5md12m_000_ifo, "US5MD12M.000.ifo");
        copy(R.raw.serif_ttf, "serif.ttf");
        copy(R.raw.sans_serif_ttf, "sans-serif.ttf");
        copy(R.raw.gur_png, "gur.png");
        {
            Echart.EChart_InvokeInit.Builder ivk  = Echart.EChart_InvokeInit.newBuilder();
            Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
            hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_INIT);
            ivk.setHdr(hdr);

            ivk.setMapPath(mActivity.getFilesDir().getPath());
            ivk.setFontPath(mActivity.getFilesDir().getPath());
            ivk.setDbFilename(mActivity.getFilesDir().getPath() + "/res.db");
            ivk.setFontPath(mActivity.getFilesDir().getPath());
            ivk.setPngPath(mActivity.getFilesDir().getPath());

            op.invoke(hdr.getMsg(), ivk);
        }

        {
            EchartMapview.EChart_InvokeMapViewCreate.Builder ivk = EchartMapview.EChart_InvokeMapViewCreate.newBuilder();
            Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
            hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_CREATE);
            ivk.setHdr(hdr);

            DisplayMetrics metric = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mImg.getLayoutParams();
            params.width = metric.widthPixels;
            params.height = metric.heightPixels;
            ivk.setMaxWidth(params.width);
            ivk.setMaxHeight(params.height);
            byte[] b = op.invoke(hdr.getMsg(), ivk);
            try
            {
                EchartMapview.EChart_ResultMapViewCreate r = EchartMapview.EChart_ResultMapViewCreate.parseFrom(b);
                assert(mMapviewId == 0);
                mMapviewId = r.getMapviewId();
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

        setParams();
    }

    private void setParams()
    {
        {
            EchartMapview.EChart_InvokeMapViewSetDpi.Builder ivk = EchartMapview.EChart_InvokeMapViewSetDpi.newBuilder();
            Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
            hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_SET_DPI);
            ivk.setHdr(hdr);

            ivk.setMapviewId(mMapviewId);
            DisplayMetrics metric = new  DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            ivk.setDpi(metric.densityDpi);

            op.invoke(hdr.getMsg(), ivk);
        }

        {
            EchartMapview.EChart_InvokeMapViewSetScale.Builder ivk = EchartMapview.EChart_InvokeMapViewSetScale.newBuilder();
            Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
            hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_SET_SCALE);
            ivk.setHdr(hdr);

            ivk.setMapviewId(mMapviewId);
            ivk.setScale(80000);

            op.invoke(hdr.getMsg(), ivk);
        }

        {
            EchartMapview.EChart_InvokeMapViewSetScreenSize.Builder ivk = EchartMapview.EChart_InvokeMapViewSetScreenSize.newBuilder();
            Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
            hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_SET_SCREEN_SIZE);
            ivk.setHdr(hdr);

//            ImageView img = (ImageView)findViewById(R.id.imageView);
            ViewGroup.LayoutParams params = mImg.getLayoutParams();

            ivk.setMapviewId(mMapviewId);
            ivk.setWidth(params.width);
            ivk.setHeight(params.height);

            op.invoke(hdr.getMsg(), ivk);
        }

        {
            EchartMapview.EChart_InvokeMapViewSetCenter.Builder ivk = EchartMapview.EChart_InvokeMapViewSetCenter.newBuilder();
            Echart.EChart_InvokeHeader.Builder hdr = Echart.EChart_InvokeHeader.newBuilder();
            hdr.setMsg(Echart.EChart_MsgType.MSG_INVOKE_MAPVIEW_SET_CENTER);
            ivk.setHdr(hdr);

            ivk.setMapviewId(mMapviewId);
            ivk.setLongitude((float)INIT_LONGITUDE);
            ivk.setLatitude((float)INIT_LATITUDE);

            op.invoke(hdr.getMsg(), ivk);
        }
    }

    private void copy(int id, String filename)
    {
        try
        {
            InputStream in = mImg.getResources().openRawResource(id);
            int length = in.available();
            byte [] buffer = new byte[length];
            in.read(buffer);
            in.close();

            String path = mActivity.getFilesDir().getPath();
            File f = new File(path + "/" + filename);
            if(!f.exists())
            {
                f.createNewFile();

                FileOutputStream fs = new FileOutputStream(f);
                fs.write(buffer);
                fs.close();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {

        }
    }

    public int getMapViewId(){
        return mMapviewId;
    }
    public EChartOperation getOp(){
        return op;
    }
}
