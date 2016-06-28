package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	// 1.定义手势识别器
	protected GestureDetector detector;
	
	protected SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 2.实例化手势识别器
		detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				
				//像数/秒  - 屏幕滑动慢
				if(Math.abs(velocityX) < 100){
					Toast.makeText(getApplicationContext(), "滑动太慢", 1).show();
					return true;
				}
				
				//屏幕斜滑
				if(Math.abs(e1.getRawY()-e2.getRawY())>100){
					Toast.makeText(getApplicationContext(), "不可以这样滑动", 1).show();
					return true;
				}
				
				if((e2.getRawX() - e1.getRawX()) > 200){
					System.out.println("显示上一个页面");
					showPre();
					return true;
				}
				
				if((e1.getRawX() - e2.getRawX()) > 200){
					System.out.println("显示下一个页面");
					showNext();
					return true;
				}
				
				return super.onFling(e1, e2, velocityX, velocityY);
			}
			
		});
	}
	
	// 3.使用手势识别器
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	// 点击事件-点击的时候进入下一个要么
	public void next(View view) {
		showNext();
	}
	//点击事件-进入上一个页面
	public void pre(View view) {
		showPre();
	}
	
	public abstract void showNext();
	
	public abstract void showPre();
}
