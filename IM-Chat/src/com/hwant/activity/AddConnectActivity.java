package com.hwant.activity;

import java.text.BreakIterator;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AddConnectActivity extends BaseActivity implements OnClickListener {
	@ViewInject(id = R.id.tv_addconnect_back)
	private TextView tv_back;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.add_connect_layout);
		ActivityInject.getInstance().setInject(this);
		tv_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_addconnect_back:
			finish();
			break;
		}
	}
}
