package com.hwant.activity;

import com.hwant.services.IMService;
import com.hwant.services.IMService.SBinder;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity implements OnClickListener {
	private Button btn_login = null;
	private ServiceConnection connection = null;
	private IMService service = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		connection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				SBinder sbinder = (SBinder) binder;
				service = sbinder.getService();
			}
		};
		Intent intent = new Intent(this, IMService.class);
		startService(new Intent(this, IMService.class));
		bindService(intent, connection, Service.BIND_AUTO_CREATE);
		setContentView(R.layout.login_layout);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (connection != null) {
			unbindService(connection);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			service.login("huwei", "123456");
			break;
		}

	}
}
