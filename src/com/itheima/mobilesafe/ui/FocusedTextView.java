package com.itheima.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 重写TextView控件，让其一出生就有焦点
 * @author yzx
 *
 */
public class FocusedTextView extends TextView {

	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	//当前控件并没有真正获得焦点，我们只是欺骗Android系统，让其以我获得焦点的方式去处理渲染
	@Override
	public boolean isFocused() {
		return true;
	}
}
