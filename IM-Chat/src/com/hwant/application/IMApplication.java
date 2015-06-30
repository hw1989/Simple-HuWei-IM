package com.hwant.application;

import org.jivesoftware.smack.SmackAndroid;

import android.app.Application;

public class IMApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		SmackAndroid.init(this);
	}

	public void exit() {

	}
}
