package com.hwant.activity;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AddConnectActivity extends BaseActivity implements OnClickListener {
	@ViewInject(id = R.id.tv_addconnect_back)
	private TextView tv_back;
	@ViewInject(id = R.id.btn_addconnect_msg)
	private Button btn_msg;
	@ViewInject(id = R.id.btn_addconnect_add)
	private Button btn_add;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.add_connect_layout);
		ActivityInject.getInstance().setInject(this);
		init();
	}

	private void init() {
		tv_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_addconnect_back:
			finish();
			break;
		case R.id.btn_addconnect_msg:
			break;
		case R.id.btn_addconnect_add:
			
			break;
		}
	}

}
