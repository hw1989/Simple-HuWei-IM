package com.hwant.asmack;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.carbons.Carbon.Private;

import android.content.Context;
import android.net.Uri;
import android.telephony.gsm.SmsMessage.MessageClass;

public class MyPacketListener implements PacketListener {
	private Context context;

	public MyPacketListener(Context context) {
		this.context = context;
	}

	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Message) {
			Message mess = (Message) packet;

			if (mess.getType() == Message.Type.chat) {
				// 单人聊天
				Uri uri=Uri.parse("content://org.hwant.im.chat");
			} else if (mess.getType() == Message.Type.groupchat) {
				// 群聊人聊天
			}

		}
	}

}
