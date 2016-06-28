package com.itheima.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.itheima.mobilesafe.R;

public class SelectContactActivity extends Activity {
	private ListView list_contact;
	private List<Map<String, String>> data;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			list_contact.setAdapter(new SimpleAdapter(SelectContactActivity.this, data, R.layout.list_select_contact_item, 
					new String[]{"name","number"},  new int[] { R.id.tv_name,R.id.tv_number }));
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		list_contact = (ListView) findViewById(R.id.list_contact);
		//在子线程中去做,如果联系人多的话就要加载一段时间，这样用子线程就可以直接跳到另一个activity，之后显示加载图标
		new Thread(){
			public void run() {
				data = getContacts();
				handler.sendEmptyMessage(0);
			};
		}.start();
		
		list_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String number = data.get(position).get("number");
				Intent data = new Intent();
				data.putExtra("number", number);
				setResult(1, data);
				//关闭当前页面
				finish();
			}
		});
	}
	
	/**
	 * 得到手机里面所有的联系人
	 * 
	 * @return
	 */
	private List<Map<String, String>> getContacts() {
		List<Map<String, String>> maps = new ArrayList<Map<String,String>>();
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri uriData = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" }, null, null, null);
		while (cursor.moveToNext()) {
			String contact_id = cursor.getString(0);
			if(contact_id != null){
				Map<String, String> map = new HashMap<String, String>();
				Cursor dataCursor = resolver.query(uriData, new String[]{"mimetype", "data1"}, "raw_contact_id=?", new String[]{contact_id}, null);
				while (dataCursor.moveToNext()) {
					String mimetype = dataCursor.getString(0);
					String data1 = dataCursor.getString(1);
					if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
						// 电话号码
						map.put("number", data1);
					} else if ("vnd.android.cursor.item/name".equals(mimetype)) {
						// 姓名
						map.put("name", data1);
					}
				}
				maps.add(map);
			}
		}
		return maps;
	}
}
