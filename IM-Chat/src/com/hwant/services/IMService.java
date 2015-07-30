package com.hwant.services;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;

import com.hwant.asmack.AsmackInit;
import com.hwant.asmack.MyRosterListener;
import com.hwant.broadcast.ReConnectionReceiver;
import com.hwant.common.Common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

public class IMService extends Service {
	public TaskManager manager = null;
	private XMPPConnection connection;
	private ReConnectionReceiver connReceiver = null;
    //设置闹钟的pendingintent
	private PendingIntent pAlarmIntent = null;
	public PendingIntent getpAlarmIntent() {
		return pAlarmIntent;
	}


	//设置ping超时的pending
	private PendingIntent pTimeoutIntent = null;

	public PendingIntent getpTimeoutIntent() {
		return pTimeoutIntent;
	}

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
		connection = asmack.setConnect(Common.Service_IP, Common.Service_Port, false);
		manager = TaskManager.init();
		pAlarmIntent= PendingIntent.getBroadcast(
				getApplicationContext(), 0, ReConnectionReceiver.pingIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		pTimeoutIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, ReConnectionReceiver.timeoutIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);// 超时闹钟
		connReceiver = new ReConnectionReceiver(this);
		// 设置ping和超时
		IntentFilter connFilter = new IntentFilter();
		connFilter.addAction(ReConnectionReceiver.PING_ALARM);
		connFilter.addAction(ReConnectionReceiver.PING_TIMEOUT);
		registerReceiver(connReceiver, connFilter);
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
		if (connReceiver != null) {
			// 取消广播的注册
			unregisterReceiver(connReceiver);
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
		// 设置ping服务器的闹钟
		((AlarmManager) getSystemService(Context.ALARM_SERVICE))
				.setInexactRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis() + 10 * 60 * 1000,
						(long) (10 * 60 * 1000), pAlarmIntent);// 10分钟ping以此服务器
		
	}
}
