package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.activity.OpenAdminActivity;

public class Setup4Activity extends BaseSetupActivity {
	private SharedPreferences sp;
	private CheckBox cb_protecting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		cb_protecting = (CheckBox) findViewById(R.id.cb_protecting);
		boolean protectting = sp.getBoolean("protectting", true);
		if(protectting){
			cb_protecting.setText("当前状态：手机防盗保护已经开启");
			openAdmin();
		}else{
			cb_protecting.setText("当前状态：手机防盗保护已经关闭");
		}
		cb_protecting.setChecked(protectting);
		cb_protecting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor editor = sp.edit();
				editor.putBoolean("protectting", isChecked);
				editor.commit();
				if(isChecked){
					cb_protecting.setText("当前状态：手机防盗保护已经开启");
					openAdmin();
				}else{
					cb_protecting.setText("当前状态：手机防盗保护已经关闭");
				}
			}
		});
	}

	protected void openAdmin() {
		Intent intent =new Intent(Setup4Activity.this,OpenAdminActivity.class);
		startActivity(intent);
	}

	@Override
	public void showNext() {
		Editor editor = sp.edit();
		editor.putBoolean("configed", true);
		editor.commit();
		Intent intent = new Intent(this, LostFindActivity.class);
		startActivity(intent);
		finish();
		// 播放动画方法最好在finish的后边或者startActivity的后边
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {
		Intent intent = new Intent(this, Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
}
