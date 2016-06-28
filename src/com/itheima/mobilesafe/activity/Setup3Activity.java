package com.itheima.mobilesafe.activity;

import com.itheima.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupActivity {
	private EditText et_safe_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setup3);
		
		et_safe_number = (EditText) findViewById(R.id.et_safe_number);
		et_safe_number.setText(sp.getString("safenumber", ""));
	}

	@Override
	public void showNext() {
		String safenumber = et_safe_number.getText().toString().trim();
		if(TextUtils.isEmpty(safenumber)){
			Toast.makeText(this, "��ȫ���벻��Ϊ��", 1).show();
			return ;
		}
		Editor editor = sp.edit();
		editor.putString("safenumber", safenumber);
		editor.commit();
		
		Intent intent = new Intent(this, Setup4Activity.class);
		startActivity(intent);
		finish();
		// ���Ŷ������������finish�ĺ�߻���startActivity�ĺ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {
		Intent intent = new Intent(this, Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
	
	// ����¼�-��ת��һ��ҳ��
	public void selectContact(View v){
		Intent intent = new Intent(this, SelectContactActivity.class);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//����û���жϷ�����ͷ����룬��Ϊ��ֻ��һ����ת��Ϣ��ͼ
		if(data == null)
			return;
		String number = data.getStringExtra("number").replace("-", "");
		et_safe_number.setText(number);
	}
}
