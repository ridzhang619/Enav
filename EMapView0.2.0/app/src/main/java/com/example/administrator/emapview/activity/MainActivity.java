package com.example.administrator.emapview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.administrator.emapview.R;
import com.example.administrator.emapview.view.BaseMapLayer;
import com.example.administrator.emapview.view.MatrixNavigateMapView;
import com.example.administrator.emapview.view.PersistableLayer;
import com.example.administrator.emapview.view.SubLayer;
import com.example.administrator.emapview.view.VesselLayer;
import com.ndk.echart.EChartOperation;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private SubLayer subLayerCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);        //透明导航栏

        final MatrixNavigateMapView mapView = (MatrixNavigateMapView)findViewById(R.id.enav_map_view);
        BaseMapLayer baseMapLayer = new BaseMapLayer(this,mapView);
        final int mapViewId = baseMapLayer.getMapViewId();
        final EChartOperation op = baseMapLayer.getOp();
        PersistableLayer layer = new PersistableLayer(mapViewId,op);
        final int persistableId = layer.getPersistableId();
        mapView.initNavigateMapView(this,op,mapViewId);
        final VesselLayer vl = new VesselLayer(mapViewId,op,mapView);


        Button bt1 = (Button)findViewById(R.id.handle);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setDrawMode(MatrixNavigateMapView.MODE_HANDLE);
            }
        });
        Button bt2 = (Button)findViewById(R.id.draw);//line
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SubLayer subLayer = new SubLayer(mapViewId,op,persistableId,mapView);
                mapView.setSubLayer(subLayer);
                mapView.setDrawMode(MatrixNavigateMapView.MODE_DRAW_LINE);
            }
        });
        Button bt3 = (Button)findViewById(R.id.draw1);//polygon
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SubLayer subLayer = new SubLayer(mapViewId,op,persistableId,mapView);
                mapView.setSubLayer(subLayer);
                mapView.setDrawMode(MatrixNavigateMapView.MODE_DRAW_POLYGON);
            }
        });
        Button bt4 = (Button)findViewById(R.id.draw2);//circle
        bt4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               subLayerCircle = new SubLayer(mapViewId,op,persistableId,mapView);
                mapView.setSubLayer(subLayerCircle);
                mapView.setDrawMode(MatrixNavigateMapView.MODE_DRAW_CIRCLE);
            }
        });
        Button bt5 = (Button)findViewById(R.id.draw3);
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                vl.updateVesselLayer(2223,random.nextInt(360),(int)System.currentTimeMillis()/1000,
                        (float)MatrixNavigateMapView.INIT_LONGITUDE,
                        (float)MatrixNavigateMapView.INIT_LATITUDE);
            }
        });
        Button bt6 = (Button)findViewById(R.id.draw4);
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                vl.updateVesselLayer(2222,random.nextInt(360),(int)System.currentTimeMillis()/1000,
                        (float)MatrixNavigateMapView.INIT_LONGITUDE,
                        (float)MatrixNavigateMapView.INIT_LATITUDE);
            }
        });
        Button bt7 = (Button)findViewById(R.id.draw5);
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subLayerCircle.destroySubLayer(subLayerCircle.getSubLayerId());
            }
        });
    }
}
