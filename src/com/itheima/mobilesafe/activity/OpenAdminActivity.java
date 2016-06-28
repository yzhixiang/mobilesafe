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

	// 设备策略服务
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
	 * 点击事件-一键锁屏+密码
	 * @param v
	 */
	public void lockScreen(View v) {
		ComponentName  who  = new ComponentName(this,MyAdmin.class);
		if(dpm.isAdminActive(who)){
			dpm.lockNow();
			dpm.resetPassword("", 0);//如果密码设置为""就是没有密码
			
//			dpm.wipeData(0);
			//sdcard格式化或者说数据全部被删除
//			dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
		}else{
			openAdmin();
		}
	}
	
	// 点击事件-代码打开超级设备管理员权限
	public void openAdmin(View view){
		openAdmin();
	}
	
	private void openAdmin() {
		//定义意图，动作是添加设备管理员
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		//组件名
		ComponentName  mDeviceAdminSample  = new ComponentName(this,MyAdmin.class);
		//授权给那个组件
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        //添加解说
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
        		"打开我可以一键锁屏，可以禁用相机，防止偷拍");
        startActivity(intent);
	}
	
	//点击事件-卸载带有超级设备管理员权限的应用
	public void unInstall(View v){
		//1.让其失去超级设备管理员
		ComponentName who = new ComponentName(this, MyAdmin.class);
		dpm.removeActiveAdmin(who);
		//2.当成普通应用
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
