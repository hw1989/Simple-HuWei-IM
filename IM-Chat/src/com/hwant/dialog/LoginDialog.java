package com.hwant.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;

public class LoginDialog extends Dialog {
	private LayoutInflater inflater = null;

	public LoginDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public LoginDialog(Context context, int theme) {
		super(context, theme);
	}

	public LoginDialog(Context context) {
		super(context);
	}

	public void init(int resid) {
		inflater = this.getLayoutInflater();
		this.addContentView(inflater.inflate(resid, null), new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		// Window window= this.getWindow();
		this.setCanceledOnTouchOutside(true);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
}
