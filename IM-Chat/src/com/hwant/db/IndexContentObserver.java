package com.hwant.db;

import android.content.UriMatcher;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

public class IndexContentObserver extends ContentObserver {
	private Handler handler = null;
	private UriMatcher matcher = null;

	public static final int Code_Connect = 1;

	public IndexContentObserver(Handler handler) {
		super(handler);
		this.handler = handler;
		matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(ConnectProvider.AUTHORITY, "friend", Code_Connect);
	}

	@Override
	public boolean deliverSelfNotifications() {
		// TODO Auto-generated method stub
		return super.deliverSelfNotifications();
	}

	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);
	}

	@Override
	public void onChange(boolean selfChange, Uri uri) {
		Message msg = handler.obtainMessage();
		if (matcher.match(uri) == Code_Connect) {
			msg.what = Code_Connect;
		}
		msg.sendToTarget();
	}

}
