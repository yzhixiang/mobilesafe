package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.reciver.MyAdmin;

public class OpenAdminActivity extends Activity {

	// �豸���Է���
	private DevicePolicyManager dpm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		
		dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		openAdmin();
//		dpm.lockNow();
//		dpm.resetPassword("123", 0);
		finish();
	}
	/**
	 * ����¼�-һ������+����
	 * @param v
	 */
	public void lockScreen(View v) {
		ComponentName  who  = new ComponentName(this,MyAdmin.class);
		if(dpm.isAdminActive(who)){
			dpm.lockNow();
			dpm.resetPassword("", 0);//�����������Ϊ""����û������
			
//			dpm.wipeData(0);
			//sdcard��ʽ������˵����ȫ����ɾ��
//			dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
		}else{
			openAdmin();
		}
	}
	
	// ����¼�-����򿪳����豸����ԱȨ��
	public void openAdmin(View view){
		openAdmin();
	}
	
	private void openAdmin() {
		//������ͼ������������豸����Ա
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		//�����
		ComponentName  mDeviceAdminSample  = new ComponentName(this,MyAdmin.class);
		//��Ȩ���Ǹ����
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        //��ӽ�˵
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
        		"���ҿ���һ�����������Խ����������ֹ͵��");
        startActivity(intent);
	}
	
	//����¼�-ж�ش��г����豸����ԱȨ�޵�Ӧ��
	public void unInstall(View v){
		//1.����ʧȥ�����豸����Ա
		ComponentName who = new ComponentName(this, MyAdmin.class);
		dpm.removeActiveAdmin(who);
		//2.������ͨӦ��
//		  <action android:name="android.intent.action.VIEW" />
//      <action android:name="android.intent.action.DELETE" />
//      <category android:name="android.intent.category.DEFAULT" />
//      <data android:scheme="package" />
		Intent intent = new Intent();
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:"+getPackageName()));	
		startActivity(intent);
	}

}
