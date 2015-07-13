package com.hwant.broadcast;

import com.hwant.common.RecevierConst;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UserInfoReceiver extends BroadcastReceiver {
	private ReceiverListener listener = null;

	public UserInfoReceiver(ReceiverListener listener) {
		this.listener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (RecevierConst.User_Info_Icon.equals(intent.getAction())) {
			// 用户图像发生改变
			if (listener != null) {
				listener.onReceive(intent);
			}
		}
	}

}
