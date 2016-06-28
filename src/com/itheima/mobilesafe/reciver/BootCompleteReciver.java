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
		//1.读取上次保存的sim卡信息
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		//防盗保护已经开启
		if(sp.getBoolean("protectting", false)){
			String saveSim = sp.getString("sim", "")+"yzx";//模拟跟换sim卡
			
			//2.得到当前的sim卡信息
			tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String curSim = tm.getSimSerialNumber();
			
			//3.比较是否一致，否则就发短信给安全号码
			if(saveSim.equals(curSim)){
				//什么也不用干
			}else{
				//发短信给安全号码
				Toast.makeText(context, "sim 已经变更.....", 1).show();
				System.out.println("sim 已经变更.....");
				SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", ""), null, "sim change from yzx...", null, null);
			}
		}
	}

}
