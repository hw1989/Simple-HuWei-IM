package com.hwant.activity;

import com.hwant.activity.LoginActivity.ConnectServer;
import com.hwant.services.IMService;
import com.hwant.services.TaskManager;
import com.hwant.services.IMService.SBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {
	private ServiceConnection connection = null;
	private IMService service = null;
	public TaskManager manager = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		connection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				SBinder sbinder = (SBinder) binder;
				service = sbinder.getService();
				manager = sbinder.getTaskManager();
				bindFinished(manager);
			}
		};
		Intent intent = new Intent(this, IMService.class);
		// startService(intent);
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (connection != null) {
			unbindService(connection);
		}
	}

	public void bindFinished(TaskManager manager) {

	}
}
