package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.AddressService;
import com.itheima.mobilesafe.ui.ServiceStatusUtils;
import com.itheima.mobilesafe.ui.SettingView;

public class SettingActivity extends Activity {
	//自动更新设置
	private SettingView siv_update;
	//归属地显示设置
	private SettingView siv_showaddress;
	private Intent showAddressIntent;
	
	//保存设置参数的
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setting);
		siv_update = (SettingView) findViewById(R.id.siv_update);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean update = sp.getBoolean("update", false);
		//初始化是否提示升级的勾选情况
		siv_update.setChecked(update);
		siv_update.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if(siv_update.isChecked()){
					//变成非选中状态
					siv_update.setChecked(false);
					editor.putBoolean("update", false);
				}else{
					//变成选中状态
					siv_update.setChecked(true);
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});
		
		//归属地显示设置
		siv_showaddress = (SettingView) findViewById(R.id.siv_show_address);
		showAddressIntent = new Intent(this,AddressService.class);
		if(ServiceStatusUtils.isServiceRunning(this, "com.itheima.mobilesafe.service.AddressService")){
			siv_showaddress.setChecked(true);
		}else{
			siv_showaddress.setChecked(false);
		}
		siv_showaddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(siv_showaddress.isChecked()){
					siv_showaddress.setChecked(false);
					stopService(showAddressIntent);
				} else {
					siv_showaddress.setChecked(true);
					startService(showAddressIntent);
				}
			}
		});
	}
}
