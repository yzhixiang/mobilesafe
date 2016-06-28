package com.itheima.mobilesafe.ui;

import com.itheima.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * �����View�������������TextView һ��CheckBox ��һ��View
 * @author yzx
 *
 */
public class SettingView extends RelativeLayout {

	private CheckBox cb_status;
	private TextView tv_desc;
	private TextView tv_title;
	
	private String desc_on;
	private String desc_off;

	private void initView(Context context) {
		//�Ѳ����ļ�--->View����
		View.inflate(context, R.layout.setting_item_view, this);
		cb_status = (CheckBox) findViewById(R.id.cb_status);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}
	
	//��������ʽ��ʱ��
	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
		
	}

	//�ڲ����ļ�ʹ�õ�ʱ�����Ȼ�ĵ�����
	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima.mobilesafe", "title");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima.mobilesafe", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima.mobilesafe", "desc_off");
		
		tv_title.setText(title);
		setDesc(desc_off);//�ռ�Ĭ���ǹر�״̬ͼ
	}
	
	//�ڴ���ʵ������ʹ���õ�
	public SettingView(Context context) {
		super(context);
		initView(context);
	}
	
	/**
	 * �õ���Ͽռ��״̬���Ƿ���ѡ��״̬
	 */
	public boolean isChecked(){
		return cb_status.isChecked();
	}
	
	/**
	 * ������Ͽռ��״̬
	 */
	public void setChecked(boolean isChecked){
		cb_status.setChecked(isChecked);
		if(isChecked){
			setDesc(desc_on);
		}else{
			setDesc(desc_off);
		}
	}
	
	/**
	 * ������Ͽռ��������Ϣ
	 */
	public void setDesc(String desc){
		tv_desc.setText(desc);
	}
}
