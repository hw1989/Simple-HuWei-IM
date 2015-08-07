package com.hwant.activity;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DynamicidActivity extends BaseActivity implements OnClickListener {
	@ViewInject(id = R.id.tv_dynamic_back)
	private TextView tv_back;
	@ViewInject(id = R.id.iv_dynamicid_send)
	private ImageView iv_send;
	private Intent intent = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.dynamicid_layout);
		ActivityInject.getInstance().setInject(this);
		init();
	}

	private void init() {
		tv_back.setOnClickListener(this);
		iv_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_dynamic_back:
			finish();
			break;
		case R.id.iv_dynamicid_send:
			intent = new Intent(this, SendDynamicidActivity.class);
			startActivity(intent);
			break;
		}
	}
}
