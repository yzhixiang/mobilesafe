package com.itheima.mobilesafe.ui;

import com.itheima.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 特殊的View，里面包含两个TextView 一个CheckBox 和一个View
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
		//把布局文件--->View对象
		View.inflate(context, R.layout.setting_item_view, this);
		cb_status = (CheckBox) findViewById(R.id.cb_status);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}
	
	//当设置样式的时候
	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
		
	}

	//在布局文件使用的时候会自然的调用它
	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima.mobilesafe", "title");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima.mobilesafe", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima.mobilesafe", "desc_off");
		
		tv_title.setText(title);
		setDesc(desc_off);//空间默认是关闭状态图
	}
	
	//在代码实例化的使用用到
	public SettingView(Context context) {
		super(context);
		initView(context);
	}
	
	/**
	 * 得到组合空间的状态，是否是选中状态
	 */
	public boolean isChecked(){
		return cb_status.isChecked();
	}
	
	/**
	 * 设置组合空间的状态
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
	 * 设置组合空间的描述信息
	 */
	public void setDesc(String desc){
		tv_desc.setText(desc);
	}
}
