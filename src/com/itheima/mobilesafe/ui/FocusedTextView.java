package com.itheima.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * ��дTextView�ؼ�������һ�������н���
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
	
	//��ǰ�ؼ���û��������ý��㣬����ֻ����ƭAndroidϵͳ���������һ�ý���ķ�ʽȥ������Ⱦ
	@Override
	public boolean isFocused() {
		return true;
	}
}
