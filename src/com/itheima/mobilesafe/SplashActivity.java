package com.itheima.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.activity.HomeActivity;
import com.itheima.mobilesafe.utils.StreamTools;


public class SplashActivity extends Activity {

    protected static final String TAG = "SplashActivity";
	protected static final int ENTER_HOME = 0;
	protected static final int SHOW_UPDATE_DIALOG = 1;
	protected static final int URL_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	protected static final int JSON_ERROR = 4;
	private TextView tv_splash_version;
	private TextView tv_splash_updateinfo;
	
	//������Ϣ
	private String description;
	//����apk��url��ַ
	private String apkurl;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ENTER_HOME://����������
				Log.e(TAG, "����������");
				enterHome();
				break;
			case SHOW_UPDATE_DIALOG://չʾ�����Ի���
				Log.e(TAG, "չʾ�����Ի���");
				showUpdateDialog();
				break;
			case URL_ERROR://��ʾurl����
				Toast.makeText(getApplicationContext(), "URL����", 1).show();
				enterHome();
				break;
			case NETWORK_ERROR://��ʾ�����쳣
				Toast.makeText(SplashActivity.this, "�����쳣", 1).show();
				enterHome();
				break;
			case JSON_ERROR://��ʾjson��������
				Toast.makeText(SplashActivity.this, "JSON��������", 1).show();
				enterHome();
				break;

			default:
				enterHome();
				break;
			}
		}
	};
	private SharedPreferences sp;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_updateinfo = (TextView) findViewById(R.id.tv_splash_updateinfo);
        tv_splash_version.setText("�汾�ţ�"+getVersion());
        
        //ͨ���ı�͸������ʵ�ֵ���Ч��
        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(1500);
        findViewById(R.id.rl_splash_root).startAnimation(aa);
        
        sp = getSharedPreferences("config", MODE_PRIVATE);
        //��ȡ�û����õ�������������
        boolean update = sp.getBoolean("update", false);
        
        // ��ʼ�����ݿ��ļ�
 		// ��asset�µ����ݿ� ������ϵͳ��Ŀ¼����
        copyDB("address.db");
        
        //�ж��Ƿ���������������
        if(update){
        	//��������
            checkUpdate();
        }else{
        	handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					enterHome();
				}
			}, 2000);
        }
        
    }
	/**
	 * �����ʲ�Ŀ¼�µ����ݿ��ļ�
	 * @param string
	 */
	private void copyDB(String dbfilename) {
		File file = new File(getFilesDir(), dbfilename);
		if (file.exists() && file.length() > 0) {
			Log.i(TAG, "���ݿ��ļ��Ѿ��������ˣ������ظ�����");
		} else {
			try {
				// ���ݿ��ļ�ֻ��Ҫ����һ�Σ�����Ѿ������ɹ��ˡ��Ժ�Ͳ���Ҫ�ظ��Ŀ�����
				AssetManager am = getAssets();
				InputStream is = am.open(dbfilename);
				// ����һ���ļ� /data/data/����/files/address.db
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.flush();
				is.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("�����°汾");
		//builder.setCancelable(false);//ǿ������
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
				dialog.dismiss();
			}
		});
		
		builder.setMessage(description);
		builder.setNegativeButton("��������", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//�ж�sd�Ƿ��ǹ���״̬
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					//����apk���ǰ�װ
					FinalHttp finalHttp = new FinalHttp();
					finalHttp.download(apkurl, Environment.getExternalStorageDirectory()+"/mobilesafe2.0.apk", new AjaxCallBack<File>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							Toast.makeText(getApplicationContext(), "����ʧ��", 1).show();
							t.printStackTrace();
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onLoading(long count, long current) {
							int progress = (int) (current*100 /count);
							tv_splash_updateinfo.setVisibility(View.VISIBLE);
							tv_splash_updateinfo.setText("���ؽ��ȣ�"+progress+"%");
							super.onLoading(count, current);
						}

						@Override
						public void onSuccess(File t) {
							installApk(t);
							super.onSuccess(t);
						}
						
						//��װӦ��
						private void installApk(File t) {
							Intent intent = new Intent();
							/*<action android:name="android.intent.action.VIEW" />
			                <category android:name="android.intent.category.DEFAULT" />
			                <data android:scheme="content" />
			                <data android:scheme="file" />
			                <data android:mimeType="application/vnd.android.package-archive" />*/
							intent.setAction("android.intent.action.VIEW");
							intent.addCategory("android.intent.category.DEFAULT");
							intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
							startActivity(intent);
						}
					});
				}else {
					Toast.makeText(getApplicationContext(), "sd��������", 1).show();
				}
			}
		});
		
		builder.setPositiveButton("�´���˵", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
				dialog.dismiss();
			}
		});
		
		builder.show();
	}

	/**
	 * ��ͼ������
	 */
	private void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		//�رյ�ǰҳ��
		finish();
	};
	
	/**
	 * ������������
	 */
	private void checkUpdate() {
		new Thread(){
			Message msg = new Message();
			long startTime = System.currentTimeMillis();
			public void run() {
				try {
					//������������
					URL url = new URL(getString(R.string.serverurl));
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					
					int code = conn.getResponseCode();//��Ӧ��
					if(code == 200){
						InputStream is = conn.getInputStream();
						// ת��String
						// ����������
						String result = StreamTools.readFromStream(is);
						Log.e(TAG, "result=="+result);
						
						//����json
						JSONObject object = new JSONObject(result);
						String version = (String) object.get("version");
						description = (String) object.get("description");
						apkurl = (String) object.get("apkurl");
						
						//�Ƚ��Ƿ����µİ汾
						if(version.equals(getVersion())){
							//û���°汾--������ҳ
							msg.what = ENTER_HOME;
						}else{
							//�и��°汾--��������ѡ��Ի���
							msg.what = SHOW_UPDATE_DIALOG;
						}
					}
				} catch (MalformedURLException e) {
					//url�����쳣
					msg.what = URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					//�����쳣
					msg.what = NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					//json�����쳣
					msg.what = JSON_ERROR;
					e.printStackTrace();
				}finally{
					long endTime = System.currentTimeMillis();
					long dTime = endTime - startTime;
					if(dTime<2000){//Ҫ��ʼҳ��ͣ��2
						SystemClock.sleep(2000-dTime);
					}
					//���ܳ���ʲô�����Ҫ������Ϣ
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	/**
	 * �õ�����İ汾��
	 * @return
	 */
	private String getVersion(){
		PackageManager pm = getPackageManager();
		try {
			//�൱�ڹ����嵥�ļ�
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}
