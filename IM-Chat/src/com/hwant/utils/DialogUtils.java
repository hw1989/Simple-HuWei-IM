package com.hwant.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

public class DialogUtils extends Dialog {

	public DialogUtils(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public DialogUtils(Context context, int theme) {
		super(context, theme);
	}

	public DialogUtils(Context context) {
		super(context);
	}

	public void setLayoutID(int layoutID) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(layoutID, null, false);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setCancelable(true);
		// setTitle(null);
		// 去掉原有的系统样式
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(layoutID);
		// this.setContentView(view, params);
	}

	public void setViewClick(android.view.View.OnClickListener listener,
			int... viewID) {
		for (int id : viewID) {
			findViewById(id).setOnClickListener(listener);
		}
	}

}
