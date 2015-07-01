package com.hwant.application;

import org.jivesoftware.smack.SmackAndroid;

import android.app.Application;

public class IMApplication extends Application {
    //确定是否连接
	public boolean isconnect=false;
	//确定是否登陆成功
	public boolean islogin=false;
	@Override
	public void onCreate() {
		super.onCreate();
		SmackAndroid.init(this);
	}
}
