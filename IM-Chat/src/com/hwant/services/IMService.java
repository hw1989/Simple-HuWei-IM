package com.hwant.services;

import com.hwant.asmack.AsmackInit;
import com.hwant.common.RecevierConst;
import com.hwant.common.WhatConst;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class IMService extends Service {
	private AsmackInit asmack = null;
	private Handler handler = null;

	@Override
	public void onCreate() {
		super.onCreate();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case WhatConst.Connect_Server:
					sendConnectBroadcast(msg);
					break;
				case WhatConst.Login_Server:
                     sendLoginBroadcast(msg);
					break;
				}
			}
		};
		asmack = new AsmackInit(this);
		asmack.setConnect("192.168.192.34", 5222, false);
		connectServer();
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
	}

	// 连接服务器
	public void connectServer() {
		new Thread() {
			public void run() {
				Message message = handler.obtainMessage(
						WhatConst.Connect_Server, asmack.startConnect());
				message.sendToTarget();
			};
		}.start();
	}

	// 连接后发送广播
	public void sendConnectBroadcast(Message msg) {
		Intent intent = new Intent(RecevierConst.Server_Connect);
		intent.putExtra("status", (Boolean) msg.obj);
		sendBroadcast(intent);
	}

	// 登陆后发送广播
	public void sendLoginBroadcast(Message msg) {
		Intent intent = new Intent(RecevierConst.Server_Login);
		intent.putExtra("status", (Boolean) msg.obj);
		sendBroadcast(intent);
	}

	// 登陆账号
	public void login(final String name, final String psw) {
		new Thread() {
			public void run() {
				Message message = handler.obtainMessage(WhatConst.Login_Server,
						asmack.setLogin(name, psw));
				message.sendToTarget();
			};
		}.start();
	}
}
