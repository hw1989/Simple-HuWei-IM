package com.hwant.broadcast;

import com.hwant.adapter.ChatMessageAdapter;
import com.hwant.common.RecevierConst;
import com.hwant.entity.ChatMessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Adapter;

public class ChatReceiver extends BroadcastReceiver {
	private Adapter adapter = null;

	public ChatReceiver(Adapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (RecevierConst.Chat_One_Get.equals(intent.getAction())) {
			ChatMessage message = (ChatMessage) intent
					.getSerializableExtra("msg");
			ChatMessageAdapter cmadapter = (ChatMessageAdapter) adapter;
			cmadapter.addMessage(message);
		}
	}

}
