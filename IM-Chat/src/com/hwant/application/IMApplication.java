package com.hwant.application;

import org.jivesoftware.smack.SmackAndroid;

import android.app.Application;

public class IMApplication extends Application {
	private SmackAndroid smack = null;

	@Override
	public void onCreate() {
		super.onCreate();
//		smack = SmackAndroid.init(getApplicationContext());
	}
    public void exit(){
    	
    }
}
