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
	// �豸���Է���
	private DevicePolicyManager dpm;

	@Override
	public void onReceive(Context context, Intent intent) {
		dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for(Object pdu : pdus){
			SmsMessage sms = SmsMessage.createFromPdu((byte[])pdu);
			// ������
			String sender = sms.getOriginatingAddress();
			String body = sms.getMessageBody();
			String safenumber = sp.getString("safenumber", "");
			if(sender.contains(safenumber)){
				if("location".equals(body)){
					System.out.println("�õ��ֻ���λ��");
					Intent locationService = new Intent(context, GPSService.class);
					context.startService(locationService);
					String lastlocation = sp.getString("lastlocation", "");
					if(TextUtils.isEmpty(lastlocation)){
						SmsManager.getDefault().sendTextMessage(safenumber, null, "getting location !!!!!", null, null);
					}else{
						SmsManager.getDefault().sendTextMessage(safenumber, null, lastlocation, null, null);
					}
					abortBroadcast();//����
				}else if("alarm".equals(body)){
					System.out.println("���ű�������");
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					// ��ʵ���ֻ���������ʱ��,�ñ�������ѭ������
					//player.setLooping(true);
					player.setVolume(1.0f, 1.0f);
					player.start();
					abortBroadcast();//����
				}else if("wipedata".equals(body)){
					System.out.println("Զ��ɾ������");
					ComponentName who = new ComponentName(context, MyAdmin.class);
					if (dpm.isAdminActive(who)) {
						dpm.wipeData(0);//�ָ��ɳ�������
						// sdcard��ʽ������˵����ȫ����ɾ��
						dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
					} else {
						openAdmin(context);
					}
					abortBroadcast();//����
				}else if("lockscreen".equals(body)){
					System.out.println("Զ������");
					ComponentName who = new ComponentName(context,MyAdmin.class);
					if(dpm.isAdminActive(who)){
						dpm.lockNow();
						dpm.resetPassword("123", 0);
					}else{
						openAdmin(context);
					}
					abortBroadcast();//����
				}
			}
		}
	}
	
	private void openAdmin(Context context) {
		Intent intent =new Intent(context,OpenAdminActivity.class);
		context.startActivity(intent);
		
	}
}
