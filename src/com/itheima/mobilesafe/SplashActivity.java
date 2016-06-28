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
	
	//描述信息
	private String description;
	//更新apk的url地址
	private String apkurl;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ENTER_HOME://进入主界面
				Log.e(TAG, "进入主界面");
				enterHome();
				break;
			case SHOW_UPDATE_DIALOG://展示升级对话框
				Log.e(TAG, "展示升级对话框");
				showUpdateDialog();
				break;
			case URL_ERROR://提示url错误
				Toast.makeText(getApplicationContext(), "URL错误", 1).show();
				enterHome();
				break;
			case NETWORK_ERROR://提示网络异常
				Toast.makeText(SplashActivity.this, "网络异常", 1).show();
				enterHome();
				break;
			case JSON_ERROR://提示json解析错误
				Toast.makeText(SplashActivity.this, "JSON解析出错", 1).show();
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
        tv_splash_version.setText("版本号："+getVersion());
        
        //通过改变透明度来实现淡入效果
        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(1500);
        findViewById(R.id.rl_splash_root).startAnimation(aa);
        
        sp = getSharedPreferences("config", MODE_PRIVATE);
        //获取用户设置的升级提醒设置
        boolean update = sp.getBoolean("update", false);
        
        // 初始化数据库文件
 		// 把asset下的数据库 拷贝到系统的目录里面
        copyDB("address.db");
        
        //判断是否设置了升级提醒
        if(update){
        	//请求联网
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
	 * 拷贝资产目录下的数据库文件
	 * @param string
	 */
	private void copyDB(String dbfilename) {
		File file = new File(getFilesDir(), dbfilename);
		if (file.exists() && file.length() > 0) {
			Log.i(TAG, "数据库文件已经拷贝过了，无需重复拷贝");
		} else {
			try {
				// 数据库文件只需要拷贝一次，如果已经拷贝成功了。以后就不需要重复的拷贝了
				AssetManager am = getAssets();
				InputStream is = am.open(dbfilename);
				// 创建一个文件 /data/data/包名/files/address.db
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
		builder.setTitle("发现新版本");
		//builder.setCancelable(false);//强制升级
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
				dialog.dismiss();
			}
		});
		
		builder.setMessage(description);
		builder.setNegativeButton("立即升级", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//判断sd是否是挂载状态
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					//下载apk覆盖安装
					FinalHttp finalHttp = new FinalHttp();
					finalHttp.download(apkurl, Environment.getExternalStorageDirectory()+"/mobilesafe2.0.apk", new AjaxCallBack<File>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							Toast.makeText(getApplicationContext(), "下载失败", 1).show();
							t.printStackTrace();
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onLoading(long count, long current) {
							int progress = (int) (current*100 /count);
							tv_splash_updateinfo.setVisibility(View.VISIBLE);
							tv_splash_updateinfo.setText("下载进度："+progress+"%");
							super.onLoading(count, current);
						}

						@Override
						public void onSuccess(File t) {
							installApk(t);
							super.onSuccess(t);
						}
						
						//安装应用
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
					Toast.makeText(getApplicationContext(), "sd卡不可用", 1).show();
				}
			}
		});
		
		builder.setPositiveButton("下次再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
				dialog.dismiss();
			}
		});
		
		builder.show();
	}

	/**
	 * 进图主界面
	 */
	private void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		//关闭当前页面
		finish();
	};
	
	/**
	 * 联网请求数据
	 */
	private void checkUpdate() {
		new Thread(){
			Message msg = new Message();
			long startTime = System.currentTimeMillis();
			public void run() {
				try {
					//联网请求数据
					URL url = new URL(getString(R.string.serverurl));
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					
					int code = conn.getResponseCode();//响应码
					if(code == 200){
						InputStream is = conn.getInputStream();
						// 转换String
						// 面向组件编程
						String result = StreamTools.readFromStream(is);
						Log.e(TAG, "result=="+result);
						
						//解析json
						JSONObject object = new JSONObject(result);
						String version = (String) object.get("version");
						description = (String) object.get("description");
						apkurl = (String) object.get("apkurl");
						
						//比较是否有新的版本
						if(version.equals(getVersion())){
							//没有新版本--进入主页
							msg.what = ENTER_HOME;
						}else{
							//有更新版本--弹出升级选择对话框
							msg.what = SHOW_UPDATE_DIALOG;
						}
					}
				} catch (MalformedURLException e) {
					//url错误异常
					msg.what = URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					//网络异常
					msg.what = NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					//json解析异常
					msg.what = JSON_ERROR;
					e.printStackTrace();
				}finally{
					long endTime = System.currentTimeMillis();
					long dTime = endTime - startTime;
					if(dTime<2000){//要初始页面停留2
						SystemClock.sleep(2000-dTime);
					}
					//不管出现什么情况都要发送消息
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	/**
	 * 得到软件的版本名
	 * @return
	 */
	private String getVersion(){
		PackageManager pm = getPackageManager();
		try {
			//相当于功能清单文件
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}
