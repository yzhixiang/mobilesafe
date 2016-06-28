package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	// 1.��������ʶ����
	protected GestureDetector detector;
	
	protected SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 2.ʵ��������ʶ����
		detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				
				//����/��  - ��Ļ������
				if(Math.abs(velocityX) < 100){
					Toast.makeText(getApplicationContext(), "����̫��", 1).show();
					return true;
				}
				
				//��Ļб��
				if(Math.abs(e1.getRawY()-e2.getRawY())>100){
					Toast.makeText(getApplicationContext(), "��������������", 1).show();
					return true;
				}
				
				if((e2.getRawX() - e1.getRawX()) > 200){
					System.out.println("��ʾ��һ��ҳ��");
					showPre();
					return true;
				}
				
				if((e1.getRawX() - e2.getRawX()) > 200){
					System.out.println("��ʾ��һ��ҳ��");
					showNext();
					return true;
				}
				
				return super.onFling(e1, e2, velocityX, velocityY);
			}
			
		});
	}
	
	// 3.ʹ������ʶ����
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	// ����¼�-�����ʱ�������һ��Ҫô
	public void next(View view) {
		showNext();
	}
	//����¼�-������һ��ҳ��
	public void pre(View view) {
		showPre();
	}
	
	public abstract void showNext();
	
	public abstract void showPre();
}
