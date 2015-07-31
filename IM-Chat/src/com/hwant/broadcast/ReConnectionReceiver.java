package com.hwant.broadcast;

import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smackx.ping.packet.Ping;

import com.hwant.common.Common;
import com.hwant.services.IDoWork;
import com.hwant.services.IMService;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReConnectionReceiver extends BroadcastReceiver {
	public static final String PING_ALARM = "com.hwant.chat.alarm";// ping服务器闹钟BroadcastReceiver的Action
	public static final String PING_TIMEOUT = "com.hwant.chat.timeout";// 判断连接超时的闹钟BroadcastReceiver的Action
	public static Intent pingIntent = new Intent(PING_ALARM);
	public static Intent timeoutIntent = new Intent(PING_TIMEOUT);
	private IMService service = null;

	public ReConnectionReceiver(IMService service) {
		this.service = service;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(PING_ALARM)) {
			if (service.getConnection().isConnected()
					&& service.getConnection().isAuthenticated()) {
				Ping ping = new Ping();
				ping.setType(Type.GET);
				ping.setTo(Common.Service_IP);
				service.getConnection().sendPacket(ping);
				((AlarmManager) this.service
						.getSystemService(Context.ALARM_SERVICE)).set(
						AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis() + 6000,
						this.service.getpTimeoutIntent());// 此时需要启动超时判断的闹钟了，时间间隔为30+3秒
			}
		} else if (intent.getAction().equals(PING_TIMEOUT)) {
			// ping超时的处理
		}
	}

	// class PingTaskWork implements IDoWork {
	//
	// @Override
	// public Object doWhat() {
	// Ping ping = new Ping();
	// ping.setType(Type.GET);
	// ping.setTo(Common.Service_IP);
	// return null;
	// }
	//
	// @Override
	// public void Finish2Do(Object obj) {
	//
	// }
	//
	// }
}
