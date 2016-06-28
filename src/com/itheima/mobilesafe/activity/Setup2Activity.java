package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.ui.SettingView;

public class Setup2Activity extends BaseSetupActivity {
	private SettingView siv_bind_sim;
	private TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String sim = sp.getString("sim", null);
		siv_bind_sim = (SettingView) findViewById(R.id.siv_bind_sim);
		siv_bind_sim.setChecked(!TextUtils.isEmpty(sim));
		siv_bind_sim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if(siv_bind_sim.isChecked()){
					//��ɷǹ�ѡ״̬
					siv_bind_sim.setChecked(false);
					//ȡ����sim��
					editor.putString("sim", null);
				}else{
					//��ɹ�ѡ״̬
					siv_bind_sim.setChecked(true);
					//��sim��
					String sim = tm.getSimSerialNumber();
					editor.putString("sim", sim);
				}
				editor.commit();
			}
		});
	}

	@Override
	public void showNext() {
		String sim = sp.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			Toast.makeText(this, "sim����û�а�", 1).show();
			return ;
		}
		Intent intent = new Intent(this, Setup3Activity.class);
		startActivity(intent);
		finish();
		// ���Ŷ������������finish�ĺ�߻���startActivity�ĺ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
}
