package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GPSService extends Service {
	
	//位置服务-可以提供-网络定位，基站定位，GPS定位
	private LocationManager lm;
	private SharedPreferences sp;
	private MyLocationListener listener;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//注册监听位置变化
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = lm.getBestProvider(criteria , true);
		listener = new MyLocationListener();
		lm.requestLocationUpdates(provider, 0, 0, listener);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//取消注册位置监听
		lm.removeUpdates(listener);
		listener = null;
	}
	
	private class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			String longitude = "j:"+location.getLongitude()+"\n";
			String latitude = "w:"+location.getLatitude()+"\n";
			String accuracy = "a:"+location.getAccuracy()+"\n";
			//位置变化就要发短信给安全号码
			//保存最后位置
			Editor editor = sp.edit();
			editor.putString("lastlocation", longitude+latitude+accuracy);
			editor.commit();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
