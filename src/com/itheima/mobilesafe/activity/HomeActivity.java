package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.MD5Uitls;

public class HomeActivity extends Activity {
	
	//功能列表名称
	private static String names[] = {"手机防盗","通讯卫士","应用管理",
		"进程管理","流量统计","手机杀毒",
		"缓存清理","高级工具","设置中心"};
	
	//功能列表图片id
	private static int ids[] = {R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
		R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
		R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};

	private GridView list_home;

	private SharedPreferences sp;

	private AlertDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		list_home = (GridView) findViewById(R.id.list_home);
		list_home.setAdapter(new HomeAdapter());
		list_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (position) {
				case 0://进入手机防盗功能
					showLostFindDialog();//弹出密码框
					break;
				case 7://进入高级工具
					intent = new Intent(HomeActivity.this, AToolsActivity.class);
					startActivity(intent);
					break;
				case 8://进入设置中心
					intent = new Intent(HomeActivity.this, SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
			}
		});
	}
	
	protected void showLostFindDialog() {
		//判断是否设置过密码，如果没有设置就弹出设置对话框，否则弹出输入对话框
		if(isSetupPwd()){
			//弹出输入对话框
			showEnterDialog();
		}else{
			//弹出设置对话框
			showSetupPwdDialog();
		}
	}
	
	/**
	 * 设置密码对话框
	 */
	private void showSetupPwdDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View contentView = View.inflate(this, R.layout.dialog_setup_password, null);
		final TextView et_password = (TextView) contentView.findViewById(R.id.et_password);
		final TextView et_password_confirm = (TextView) contentView.findViewById(R.id.et_password_confirm);
		Button ok = (Button) contentView.findViewById(R.id.ok);
		Button cancel = (Button) contentView.findViewById(R.id.cancel);
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//消除对话框
				dialog.dismiss();
			}
		});
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//1.得到密码
				String password = et_password.getText().toString().trim();
				String password_confirm = et_password_confirm.getText().toString().trim();
				
				//2. 判空
				if(TextUtils.isEmpty(password) || TextUtils.isEmpty(password_confirm)){
					Toast.makeText(HomeActivity.this, "密码不能为空", 1).show();
					return ;
				}
				
				//3.判断密码是否一致
				if(password.equals(password_confirm)){
					//4.保存密码&进入手机防盗页面
					Editor editor = sp.edit();
					editor.putString("password", MD5Uitls.encoder(password));
					editor.commit();
					System.out.println("密码正确，保存密码，进入手机防盗页面");
					dialog.dismiss();
					
					//进入手机防盗页面
					Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
					startActivity(intent);//不要关闭主页面
				}else{
					Toast.makeText(HomeActivity.this, "密码不一致", 1).show();
				}
				
			}
		});
		
		dialog = builder.create();
		dialog.setView(contentView,0,0,0,0);
		dialog.show();
	}
	
	/**
	 * 输入对话框
	 */
	private void showEnterDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View contentView = View.inflate(this, R.layout.dialog_enter_password, null);
		final TextView et_password = (TextView) contentView.findViewById(R.id.et_password);
		Button ok = (Button) contentView.findViewById(R.id.ok);
		Button cancel = (Button) contentView.findViewById(R.id.cancel);
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//消除对话框
				dialog.dismiss();
			}
		});
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//1.得到密码
				String password = et_password.getText().toString().trim();
				String save_password = sp.getString("password", "");
				
				//2. 判空
				if(TextUtils.isEmpty(password) ){
					Toast.makeText(HomeActivity.this, "密码不能为空", 1).show();
					return ;
				}
				
				//密码正确进入手机防盗页面
				if(save_password.equals(MD5Uitls.encoder(password))){
					System.out.println("密码正确，进入主页面");
					dialog.dismiss();
					
					//进入手机防盗页面
					Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
					startActivity(intent);//不要关闭主页面
				}else{
					Toast.makeText(getApplicationContext(), "密码不正确", 1).show();
				}
			}
		});
		
		dialog = builder.create();
		dialog.setView(contentView,0,0,0,0);
		dialog.show();
	}

	/**
	 * 判断是否设置过密码
	 * @author yzx
	 *
	 */
	private boolean isSetupPwd(){
		String password = sp.getString("password", null);
		return !TextUtils.isEmpty(password);
	}
	
	class HomeAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.list_home_item, null);
			ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_name.setText(names[position]);
			iv_icon.setImageResource(ids[position]);
			return view;
		}
		
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
}
