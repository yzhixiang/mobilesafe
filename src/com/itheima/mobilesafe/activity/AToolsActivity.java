package com.itheima.mobilesafe.activity;

import com.itheima.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AToolsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	//点击事件-跳转到号码归属地查询的页面
	public void numberAddressQuery(View v){
		Intent intent = new Intent(this, NumberAddressQueryActivity.class);
		startActivity(intent);
	}
}
