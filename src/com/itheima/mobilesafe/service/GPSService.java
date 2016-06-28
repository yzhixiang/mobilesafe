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
	
	//λ�÷���-�����ṩ-���綨λ����վ��λ��GPS��λ
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
		//ע�����λ�ñ仯
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = lm.getBestProvider(criteria , true);
		listener = new MyLocationListener();
		lm.requestLocationUpdates(provider, 0, 0, listener);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//ȡ��ע��λ�ü���
		lm.removeUpdates(listener);
		listener = null;
	}
	
	private class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			String longitude = "j:"+location.getLongitude()+"\n";
			String latitude = "w:"+location.getLatitude()+"\n";
			String accuracy = "a:"+location.getAccuracy()+"\n";
			//λ�ñ仯��Ҫ�����Ÿ���ȫ����
			//�������λ��
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
