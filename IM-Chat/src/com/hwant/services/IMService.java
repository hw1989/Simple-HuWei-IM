package com.hwant.services;

import org.wind.util.PreferenceUtils;

import com.hwant.asmack.AsmackInit;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class IMService extends Service {
//	private Handler mhandler = new Handler();
	private AsmackInit asmack = null;

	@Override
	public void onCreate() {
		super.onCreate();
		asmack = new AsmackInit(this);
	}
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	// TODO Auto-generated method stub
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
	}

	public void login(final String name, final String psw) {
		new Thread() {
			public void run() {
				asmack.setConnect("192.168.192.55", 5222, false);
				if (asmack.setLogin(name, psw)) {
					Log.e("info", name+"  "+psw);
				}
			};
		}.start();
	}
}
