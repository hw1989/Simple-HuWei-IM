package com.hwant.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class IMService extends Service {
	public TaskManager manager = null;

	@Override
	public void onCreate() {
		super.onCreate();
		manager = TaskManager.init();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new SBinder();
	}

	public class SBinder extends Binder {

		public IMService getService() {
			return IMService.this;
		}
		public TaskManager getTaskManager(){
			return IMService.this.manager;
		}
	}
}
