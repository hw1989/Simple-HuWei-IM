package com.hwant.broadcast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.wind.adapter.OtherAdapter;
import com.hwant.adapter.ChatMessageAdapter;
import com.hwant.common.RecevierConst;
import com.hwant.entity.ChatMessage;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class ChatReceiver extends BroadcastReceiver {
	private OtherAdapter<ChatMessage> adapter = null;
	private ContentResolver resolver = null;
	private SimpleDateFormat format = null;
	private String mfrom = "", mto = "";
	private Uri uri = null;

	public ChatReceiver(OtherAdapter<ChatMessage> adapter, String mfrom,
			String mto) {
		this.adapter = adapter;
		format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		uri = Uri.parse("content://org.hwant.im.chat/chat");
		this.mfrom=mfrom;
		this.mto=mto;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (RecevierConst.Chat_One_Get.equals(intent.getAction())) {
			// 判断消息是否来自当前人
			if (!mto.equals(intent.getStringExtra("mfrom"))) {
				return;
			}
			ChatMessage message = (ChatMessage) intent
					.getSerializableExtra("msg");
			ChatMessageAdapter cmadapter = (ChatMessageAdapter) adapter;
			// cmadapter.addMessage(message);
			cmadapter.addItem(message, 0);
		} else if (RecevierConst.Chat_DB_Get.equals(intent.getAction())) {
//			String s=intent.getStringExtra("mto");
			// 判断消息是否来自当前人
			if (!mto.equals(intent.getStringExtra("mfrom"))) {
				return;
			}
			resolver = context.getContentResolver();
			// 从数据库中获取
			ChatMessage lastmsg = adapter.getLastItem();
			Cursor cursor = null;
			// 获取最新的sql where的语句不同
			if (lastmsg == null) {
				cursor = resolver.query(uri, null,
						"  user=?  and ( mfrom=? or mto=?)  ", new String[] {
								mfrom, mto, mto }, "  time desc limit 0,10");
			} else {
				String lasttime = lastmsg.getTime();
				cursor = resolver.query(uri, null,
						"  user=? and time>? and ( mfrom=? or mto=?)  ",
						new String[] { mfrom, lasttime, mto, mto },
						"  time desc limit 0,10");
			}
			cursor.moveToFirst();
			ArrayList<ChatMessage> list = new ArrayList<ChatMessage>();
			while (!cursor.isAfterLast()) {
				ChatMessage message = new ChatMessage();
				message.setMfrom(cursor.getString(cursor
						.getColumnIndex("mfrom")));
				message.setMessage(cursor.getString(cursor
						.getColumnIndex("message")));
				message.setTime(cursor.getString(cursor.getColumnIndex("time")));
				list.add(message);
				cursor.moveToNext();
			}
			cursor.close();
			adapter.addItems(list,adapter.getCount());
		}
	}

}
