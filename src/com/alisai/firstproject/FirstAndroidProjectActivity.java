package com.alisai.firstproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

public class FirstAndroidProjectActivity extends Activity {
	
	MapView mMapView ;
	ArcGISTiledMapServiceLayer tiledLayer;
	private final static int Fix_TapPoint_Location = 1;
	private final static int Fixed_TapPoint_Location = 1;
	private GraphicsLayer gLayer;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FirstAndroidProjectActivity","Calling FirstAndroidProjectActivity.onCreate()");
        
        setContentView(R.layout.main);
        
        /* 
         * 方式一：以代码方式获取 MapView
         */
        mMapView = (MapView)findViewById(R.id.map);
        
		tiledLayer = new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");
		//tiledLayer = new ArcGISTiledMapServiceLayer("http://192.168.88.1:7080/PBS/rest/services/worldstreetmap/MapServer");

		mMapView.addLayer(tiledLayer);	

		/*
		 * 自定义背景颜色、背景网格
		 * setMapBackground(int bkColor, int gridColor, float gridSize, float gridLineSize)
		 */
		mMapView.setMapBackground(0xffffffff, Color.TRANSPARENT, 0, 0);
		
		/*
		 * 启用经向环绕
		 * enableWrapAround(boolean enable)
		 */
		mMapView.enableWrapAround(true);
		
		gLayer = new GraphicsLayer();
		mMapView.addLayer(gLayer);
		
		
		/*
		 * 长按以转至影像底图，查看以代码方式获取 MapView 的方法
		 */
		
		mMapView.setOnLongPressListener(new OnLongPressListener(){

			public void onLongPress(float x, float y) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(FirstAndroidProjectActivity.this, ImageServiceActivity.class);
				
				Polygon currentExtent = mMapView.getExtent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("currentExtent", currentExtent);
				
				intent.putExtras(bundle);
				
				startActivity(intent);
			}			
		});
		
		mMapView.setOnSingleTapListener(new OnSingleTapListener(){

			public void onSingleTap(float x, float y) {
				// TODO Auto-generated method stub
				gLayer.removeAll();
				Point tapPoint = mMapView.toMapPoint(x, y);
				gLayer.addGraphic(new Graphic(tapPoint, new SimpleMarkerSymbol(Color.BLUE, 16, SimpleMarkerSymbol.STYLE.DIAMOND)));
				Intent intent = new Intent();
				intent.setClass(FirstAndroidProjectActivity.this, ImageServiceActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("tapPoint", tapPoint);
				intent.putExtras(bundle);
				startActivityForResult(intent, Fix_TapPoint_Location);
			}
			
		});
		
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == Fix_TapPoint_Location){
			if(resultCode == Fixed_TapPoint_Location){
				Point fixedPoint = (Point)data.getExtras().getSerializable("fixedPoint");
				gLayer.addGraphic(new Graphic(fixedPoint, new SimpleMarkerSymbol(Color.BLUE, 16, SimpleMarkerSymbol.STYLE.CROSS)));	
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("FirstAndroidProjectActivity","Calling FirstAndroidProjectActivity.onRestart()");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("FirstAndroidProjectActivity","Calling FirstAndroidProjectActivity.onStart()");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("FirstAndroidProjectActivity","Calling FirstAndroidProjectActivity.onStop()");
	}

	@Override 
	protected void onDestroy() { 
		super.onDestroy();
		Log.i("FirstAndroidProjectActivity","Calling FirstAndroidProjectActivity.onDestroy()");
 }
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.pause();
		Log.i("FirstAndroidProjectActivity","Calling FirstAndroidProjectActivity.onPause()");
 }
	@Override 	
	protected void onResume() {
		super.onResume();
		mMapView.unpause();
		Log.i("FirstAndroidProjectActivity","Calling FirstAndroidProjectActivity.onResume()");
	}

}