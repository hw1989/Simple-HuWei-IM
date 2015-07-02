package com.hwant.application;

import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;

import com.hwant.asmack.AsmackInit;

import android.app.Application;

public class IMApplication extends Application {
	// 确定是否连接
	public boolean isconnect = false;
	// 确定是否登陆成功
	public boolean islogin = false;
	//
	private AsmackInit asmack = null;

	public synchronized AsmackInit getAsmack() {
		return asmack;
	}
	public synchronized XMPPConnection getConnection() {
		return asmack.getConnection();
	}
	@Override
	public void onCreate() {
		super.onCreate();
		SmackAndroid.init(this);
		asmack = new AsmackInit(this);
		asmack.setConnect("192.168.192.50", 5222, false);
	}
}
