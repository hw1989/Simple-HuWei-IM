package com.hwant.broadcast;

import com.hwant.common.RecevierConst;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class XMPPRecevier extends BroadcastReceiver {
	private IXMPPWork work;

	public void setIXMPPWork(IXMPPWork work) {
		this.work = work;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (RecevierConst.Connect_Server.equals(intent.getAction())) {
			if (work != null) {
				work.dowhat(intent);
			}
		}
	}

}
