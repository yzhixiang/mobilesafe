package com.itheima.mobilesafe.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompleteReciver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;

	@Override
	public void onReceive(Context context, Intent intent) {
		//1.��ȡ�ϴα����sim����Ϣ
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		//���������Ѿ�����
		if(sp.getBoolean("protectting", false)){
			String saveSim = sp.getString("sim", "")+"yzx";//ģ�����sim��
			
			//2.�õ���ǰ��sim����Ϣ
			tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String curSim = tm.getSimSerialNumber();
			
			//3.�Ƚ��Ƿ�һ�£�����ͷ����Ÿ���ȫ����
			if(saveSim.equals(curSim)){
				//ʲôҲ���ø�
			}else{
				//�����Ÿ���ȫ����
				Toast.makeText(context, "sim �Ѿ����.....", 1).show();
				System.out.println("sim �Ѿ����.....");
				SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", ""), null, "sim change from yzx...", null, null);
			}
		}
	}

}
