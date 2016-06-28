package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.itheima.mobilesafe.db.dao.AddressDao;

public class AddressService extends Service {
	private TelephonyManager tm;
	private MyPhoneListener listener;
	private OutCallReceiver receiver;
	
	
	// 在服务代码的内部 创建一个内部类
	// 利用代码的方式 注册一个广播接受者
	// 让广播接收者的存活周期 跟服务保持一致了。
	private class OutCallReceiver extends BroadcastReceiver{
		private static final String TAG = "OutCallReceiver";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "我是服务内部的广播接受者，有电话打出去了。。。");
			String phone = getResultData();
			String result = AddressDao.getAddress(phone);
			Toast.makeText(context, result, 1).show();
		} 
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// 采用代码的方式 注册广播接受者
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}
	
	private class MyPhoneListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case PhoneStateListener.LISTEN_CALL_STATE:
				String address = AddressDao.getAddress(incomingNumber);
				Toast.makeText(getApplicationContext(), address, 1).show();
				break;

			default:
				break;
			}
		}
	}
	
	@Override
	public void onDestroy() {
		// 服务停止的时候 取消注册广播接受者
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}
}
