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
	
	
	// �ڷ��������ڲ� ����һ���ڲ���
	// ���ô���ķ�ʽ ע��һ���㲥������
	// �ù㲥�����ߵĴ������ �����񱣳�һ���ˡ�
	private class OutCallReceiver extends BroadcastReceiver{
		private static final String TAG = "OutCallReceiver";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "���Ƿ����ڲ��Ĺ㲥�����ߣ��е绰���ȥ�ˡ�����");
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
		// ���ô���ķ�ʽ ע��㲥������
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
		// ����ֹͣ��ʱ�� ȡ��ע��㲥������
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}
}
