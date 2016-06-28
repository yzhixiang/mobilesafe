package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.itheima.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
		
	}
	
	@Override
	public void showNext() {
		Intent intent = new Intent(this, Setup2Activity.class);
		startActivity(intent);
		finish();
		// 播放动画方法最好在finish的后边或者startActivity的后边
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		
	}


	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		
	}
}
