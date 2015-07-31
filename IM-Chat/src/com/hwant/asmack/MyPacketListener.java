package com.hwant.asmack;

import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.carbons.Carbon.Private;
import org.jivesoftware.smackx.ping.packet.Ping;
import org.wind.util.StringHelper;

import com.hwant.application.IMApplication;
import com.hwant.common.RecevierConst;
import com.hwant.entity.ChatMessage;
import com.hwant.entity.UserInfo;
import com.hwant.services.IMService;

import android.app.AlarmManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.gsm.SmsMessage.MessageClass;

public class MyPacketListener implements PacketListener {
	// private Context context;
	private ContentResolver resolver = null;
	private IMApplication application = null;
	private Intent intent = null;
	private IMService service = null;

	public MyPacketListener(Application application, IMService service) {
		this.application = (IMApplication) application;
		this.resolver = application.getApplicationContext()
				.getContentResolver();
		this.service = service;
	}

	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Message) {
			Message mess = (Message) packet;

			if (mess.getType() == Message.Type.chat) {
				if (StringHelper.isEmpty(mess.getBody())) {
					return;
				}
				// 单人聊天
				Uri uri = Uri.parse("content://org.hwant.im.chat/chat");
				ContentValues values = new ContentValues();
				int indexfrom = mess.getFrom().lastIndexOf("/");
				int indexto = mess.getTo().lastIndexOf("/");
				String mfrom = mess.getFrom(), mto = mess.getTo();
				if (indexfrom >= 0) {
					mfrom = mess.getFrom().substring(0, indexfrom);
				}
				if (indexto >= 0) {
					mto = mess.getTo().substring(0, indexto);
				}
				values.put("mfrom", mfrom);
				values.put("mto", mto);
				// 服务器传过来有后缀 /Spark /Smack
				// values.put("mfrom", mess.getFrom());
				// values.put("mto", application.user.getJid());
				values.put("message", mess.getBody());
				values.put("read", "0");
				values.put("user", application.user.getJid());
				Date date = new Date();
				values.put("time", String.valueOf(date.getTime()));
				resolver.insert(uri, values);
				intent = new Intent();
				// A与B聊天，C发消息给A会异常(当前界面出现C的消息)
				// ChatMessage message = new ChatMessage();
				// message.setMfrom(mess.getFrom());
				// message.setMessage(mess.getBody());
				// message.setMto(application.user.getJid());
				// UserInfo info = new UserInfo();
				// info.setJid(mess.getFrom());
				// message.setInfo(info);
				// intent.setAction(RecevierConst.Chat_One_Get);
				// intent.putExtra("msg", message);
				intent.setAction(RecevierConst.Chat_DB_Get);
				intent.putExtra("mfrom", mfrom);
				intent.putExtra("mto", mto);
				// 发送广播
				application.getApplicationContext().sendBroadcast(intent);
			} else if (mess.getType() == Message.Type.groupchat) {
				// 群聊人聊天

			}
		} else if (packet instanceof Ping) {
			if (service != null) {
				((AlarmManager) (application
						.getSystemService(Context.ALARM_SERVICE)))
						.cancel(service.getpTimeoutIntent());
			}
		}
	}

}
