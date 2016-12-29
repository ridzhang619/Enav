package com.example.administrator.emapview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.administrator.emapview.R;
import com.example.administrator.emapview.view.MatrixNavigateMapView;
import com.example.administrator.emapview.view.SubLayer;
import com.example.administrator.emapview.view.VesselLayer;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    VesselLayer vl = new VesselLayer();
    SubLayer sl1 = new SubLayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        final MatrixNavigateMapView view = (MatrixNavigateMapView)findViewById(R.id.enav_map_view);
        view.initNavigateMapView(this);

        Button bt1 = (Button)findViewById(R.id.handle);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setDrawMode(MatrixNavigateMapView.MODE_HANDLE);
            }
        });
        Button bt2 = (Button)findViewById(R.id.draw);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubLayer sl = new SubLayer();
                view.setSubLayer(sl);
                view.setDrawMode(MatrixNavigateMapView.MODE_DRAW_LINE);
            }
        });
        Button bt3 = (Button)findViewById(R.id.draw1);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubLayer sl = new SubLayer();
                view.setSubLayer(sl);
                view.setDrawMode(MatrixNavigateMapView.MODE_DRAW_POLYGON);
            }
        });
        Button bt4 = (Button)findViewById(R.id.draw2);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setSubLayer(sl1);//通过mapview 将id 和 op 对象带给subLayer
                view.setDrawMode(MatrixNavigateMapView.MODE_DRAW_CIRCLE);
            }
        });
        Button bt5 = (Button)findViewById(R.id.draw3);
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view.setVessel(vl);//通过mapview 将id 和 op 对象带给vesselLayer
                Random random = new Random();
                vl.updateVesselLayer(1111,random.nextInt(360),(int)System.currentTimeMillis()/1000,
                        (float)MatrixNavigateMapView.INIT_LONGITUDE,
                        (float)MatrixNavigateMapView.INIT_LATITUDE);
            }
        });
        Button bt6 = (Button)findViewById(R.id.draw4);
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                view.setVessel(vl);//通过mapview 将id 和 op 对象带给vesselLayer
                vl.updateVesselLayer(2222,random.nextInt(360),(int)System.currentTimeMillis()/1000,
                        (float)MatrixNavigateMapView.INIT_LONGITUDE,
                        (float)MatrixNavigateMapView.INIT_LATITUDE);
            }
        });
        Button bt7 = (Button)findViewById(R.id.draw5);
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sl1.destroySubLayer();
            }
        });
    }
}
