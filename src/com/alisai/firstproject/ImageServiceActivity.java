package com.alisai.firstproject;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ImageServiceActivity extends Activity {
	private MapView imageMapView;
	private ArcGISTiledMapServiceLayer imageTiledLayer;
	private GraphicsLayer gLayer;
	private Point tapPoint;
	private Button btnBack;
	private final static int Fixed_TapPoint_Location = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		Log.i("ImageServiceActivity","Calling ImageServiceActivity.onCreate()");
		
		imageMapView = new MapView(this);
		imageTiledLayer = new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer");
		//imageTiledLayer = new ArcGISTiledMapServiceLayer("http://192.168.88.1:7080/PBS/rest/services/worldsimagemap/MapServer");
		imageMapView.addLayer(imageTiledLayer);
		setContentView(imageMapView);
		
		gLayer = new GraphicsLayer();
		imageMapView.addLayer(gLayer);
		
		Intent intent = getIntent();
	    Bundle bundle = intent.getExtras();
	    Polygon currentExtent = (Polygon)bundle.getSerializable("currentExtent");
		if(currentExtent!=null){
			imageMapView.setExtent(currentExtent);
		}
		
		tapPoint = (Point)bundle.getSerializable("tapPoint");
		if(tapPoint!=null){
			gLayer.addGraphic(new Graphic(tapPoint, new SimpleMarkerSymbol(Color.RED, 16, SimpleMarkerSymbol.STYLE.CIRCLE)));
			imageMapView.zoomTo(tapPoint, 0.5f);
			
			Toast.makeText(ImageServiceActivity.this, "长按屏幕以修改所添加的触摸点", Toast.LENGTH_LONG).show();
			imageMapView.setOnLongPressListener(new OnLongPressListener(){

				public void onLongPress(float x, float y) {
					// TODO Auto-generated method stub
					tapPoint = imageMapView.toMapPoint(x, y);
					gLayer.removeAll();
					gLayer.addGraphic(new Graphic(tapPoint, new SimpleMarkerSymbol(Color.RED, 16, SimpleMarkerSymbol.STYLE.CIRCLE)));
					btnBack = new Button(ImageServiceActivity.this);
					btnBack.setText("返  回");
					RelativeLayout.LayoutParams relativeLayoutlp = new RelativeLayout.LayoutParams(100,
			                RelativeLayout.LayoutParams.WRAP_CONTENT);
					relativeLayoutlp.addRule(RelativeLayout.ALIGN_BOTTOM, RelativeLayout.TRUE);
					imageMapView.addView(btnBack, relativeLayoutlp);
					btnBack.setOnClickListener(new OnClickListener(){

						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent= new Intent();
							intent.setClass(ImageServiceActivity.this, FirstAndroidProjectActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("fixedPoint", tapPoint);
							intent.putExtras(bundle);
							setResult(Fixed_TapPoint_Location, intent);
							ImageServiceActivity.this.finish();
						}
						
					});
				}
				
			});
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("ImageServiceActivity","Calling ImageServiceActivity.onStart()");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		imageMapView.pause();
		Log.i("ImageServiceActivity","Calling ImageServiceActivity.onPause()");
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("ImageServiceActivity","Calling ImageServiceActivity.onRestart()");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		imageMapView.unpause();
		Log.i("ImageServiceActivity","Calling ImageServiceActivity.onResume()");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("ImageServiceActivity","Calling ImageServiceActivity.onStop()");
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("ImageServiceActivity","Calling ImageServiceActivity.onDestroy()");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}

}
