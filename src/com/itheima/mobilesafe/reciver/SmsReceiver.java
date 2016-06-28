package com.itheima.mobilesafe.reciver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.activity.OpenAdminActivity;
import com.itheima.mobilesafe.service.GPSService;

public class SmsReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	// 设备策略服务
	private DevicePolicyManager dpm;

	@Override
	public void onReceive(Context context, Intent intent) {
		dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for(Object pdu : pdus){
			SmsMessage sms = SmsMessage.createFromPdu((byte[])pdu);
			// 发送者
			String sender = sms.getOriginatingAddress();
			String body = sms.getMessageBody();
			String safenumber = sp.getString("safenumber", "");
			if(sender.contains(safenumber)){
				if("location".equals(body)){
					System.out.println("得到手机的位置");
					Intent locationService = new Intent(context, GPSService.class);
					context.startService(locationService);
					String lastlocation = sp.getString("lastlocation", "");
					if(TextUtils.isEmpty(lastlocation)){
						SmsManager.getDefault().sendTextMessage(safenumber, null, "getting location !!!!!", null, null);
					}else{
						SmsManager.getDefault().sendTextMessage(safenumber, null, lastlocation, null, null);
					}
					abortBroadcast();//拦截
				}else if("alarm".equals(body)){
					System.out.println("播放报警音乐");
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					// 真实的手机防盗功能时候,让报警音乐循环播放
					//player.setLooping(true);
					player.setVolume(1.0f, 1.0f);
					player.start();
					abortBroadcast();//拦截
				}else if("wipedata".equals(body)){
					System.out.println("远程删除数据");
					ComponentName who = new ComponentName(context, MyAdmin.class);
					if (dpm.isAdminActive(who)) {
						dpm.wipeData(0);//恢复成出厂设置
						// sdcard格式化或者说数据全部被删除
						dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
					} else {
						openAdmin(context);
					}
					abortBroadcast();//拦截
				}else if("lockscreen".equals(body)){
					System.out.println("远程锁屏");
					ComponentName who = new ComponentName(context,MyAdmin.class);
					if(dpm.isAdminActive(who)){
						dpm.lockNow();
						dpm.resetPassword("123", 0);
					}else{
						openAdmin(context);
					}
					abortBroadcast();//拦截
				}
			}
		}
	}
	
	private void openAdmin(Context context) {
		Intent intent =new Intent(context,OpenAdminActivity.class);
		context.startActivity(intent);
		
	}
}
