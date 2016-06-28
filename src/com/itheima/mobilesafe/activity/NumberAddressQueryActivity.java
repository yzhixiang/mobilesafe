package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.AddressDao;

public class NumberAddressQueryActivity extends Activity {
	private static final String TAG = "NumberAddressQueryActivity";
	private EditText et_number;
	private TextView tv_result;
	
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		setContentView(R.layout.activity_activity_number_address_query);
		et_number = (EditText) findViewById(R.id.et_number);
		tv_result = (TextView) findViewById(R.id.tv_result);
		
		et_number.addTextChangedListener(new TextWatcher() {
			//当文本发生变化的时候调用的方法
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length() >= 3){
					String address = AddressDao.getAddress(s.toString());
					tv_result.setText(address);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	// 点击事件-查询号码的归属地
	public void queryNumberAddress(View v) {
		// 1.得到要查的电话号码
		String number = et_number.getText().toString().trim();
		// 2.判空
		if (TextUtils.isEmpty(number)) {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_number.startAnimation(shake);
			Toast.makeText(this, "您没有输入电话号码的呢，大哥", 1).show();
			
			//vibrator.vibrate(2000);//振动两秒钟
		    //-1不重复，非-1为从pattern的指定下标开始重复 
			vibrator.vibrate(new long[]{100,200,100,300,50,200}, 0);
			return;
		}else{
			// 3.如果不为空就去查询号码归属地
			// 查询方式：1，联网查询--可以用其他公共的webserver ;2.把数据库放在本地，到手机内部查询
			Log.i(TAG,"查询："+number+"的归属地");
			String address = AddressDao.getAddress(number);
			tv_result.setText(address);
		}
		
	}
}
