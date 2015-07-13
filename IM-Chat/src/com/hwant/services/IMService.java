package com.hwant.services;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;

import com.hwant.asmack.AsmackInit;
import com.hwant.asmack.MyRosterListener;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class IMService extends Service {
	public TaskManager manager = null;
	private XMPPConnection connection;

	public XMPPConnection getConnection() {
		return connection;
	}

	private AsmackInit asmack = null;

	public AsmackInit getAsmack() {
		return asmack;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		asmack = new AsmackInit(getApplication());
		connection = asmack.setConnect("192.168.192.86", 5222, false);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (manager != null) {
			manager.removeAll();
		}
	}

	public class SBinder extends Binder {

		public IMService getService() {
			return IMService.this;
		}

		public TaskManager getTaskManager() {
			return IMService.this.manager;
		}
	}

	/**
	 * 添加连接的监听
	 */
	public void addConnectListener() {
		MyRosterListener rosterlistener = new MyRosterListener(connection,
				getApplication());
		Roster roster = connection.getRoster();
		roster.addRosterListener(rosterlistener);
	}
}
