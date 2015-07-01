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
		if (RecevierConst.Server_Connect.equals(intent.getAction())) {
			if (work != null) {
				work.connectDoWhat(intent);
			}
		}else if(RecevierConst.Server_Login.equals(intent.getAction())){
			if (work != null) {
				work.loginDoWhat(intent);
			}
		}
	}

}
