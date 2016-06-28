package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

public class LostFindActivity extends Activity {
	private SharedPreferences sp;
	private TextView tv_safe_number;
	private ImageView iv_protectting_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// �ж��Ƿ����ù������򵼣���������˾����ŵ�ǰҳ�棨�ֻ�����ҳ�棩,�������ת�������򵼵ĵ�һ��ҳ��
		boolean configed = sp.getBoolean("configed", false);
		if(configed){
			setContentView(R.layout.activity_lostfind);
			tv_safe_number = (TextView) findViewById(R.id.tv_safe_number);
			iv_protectting_status = (ImageView) findViewById(R.id.iv_protectting_status);
			tv_safe_number.setText(sp.getString("safenumber", "XXXX"));
			boolean protectting = sp.getBoolean("protectting", false);
			if(protectting){
				iv_protectting_status.setImageResource(R.drawable.lock);
			}else{
				iv_protectting_status.setImageResource(R.drawable.unlock);
			}
		}else{
			// ��ת�������򵼵ĵ�һ��ҳ��
			Intent intent = new Intent(this, Setup1Activity.class);
			startActivity(intent);
			// �رյ�ǰҳ�棻
			finish();
		}
	}
	
	/**
	 * ����¼� -���½����һ��������ҳ��
	 * 
	 * @param view
	 */
	public void reEnterSetting(View v){
		// ��ת�������򵼵ĵ�һ��ҳ��
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		// �رյ�ǰҳ�棻
		finish();
	}
}
