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
			//���ı������仯��ʱ����õķ���
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

	// ����¼�-��ѯ����Ĺ�����
	public void queryNumberAddress(View v) {
		// 1.�õ�Ҫ��ĵ绰����
		String number = et_number.getText().toString().trim();
		// 2.�п�
		if (TextUtils.isEmpty(number)) {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_number.startAnimation(shake);
			Toast.makeText(this, "��û������绰������أ����", 1).show();
			
			//vibrator.vibrate(2000);//��������
		    //-1���ظ�����-1Ϊ��pattern��ָ���±꿪ʼ�ظ� 
			vibrator.vibrate(new long[]{100,200,100,300,50,200}, 0);
			return;
		}else{
			// 3.�����Ϊ�վ�ȥ��ѯ���������
			// ��ѯ��ʽ��1��������ѯ--����������������webserver ;2.�����ݿ���ڱ��أ����ֻ��ڲ���ѯ
			Log.i(TAG,"��ѯ��"+number+"�Ĺ�����");
			String address = AddressDao.getAddress(number);
			tv_result.setText(address);
		}
		
	}
}
