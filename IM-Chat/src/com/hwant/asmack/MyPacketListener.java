package com.hwant.asmack;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.carbons.Carbon.Private;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.ping.packet.Ping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wind.util.StringHelper;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindCallback;

import com.baidu.navisdk.ui.widget.NewerGuideDialog;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.DownloadListener;
import com.hwant.application.IMApplication;
import com.hwant.common.Common;
import com.hwant.common.RecevierConst;
import com.hwant.db.RelationshipProvider;
import com.hwant.entity.ChatMessage;
import com.hwant.entity.ConnectInfo;
import com.hwant.entity.UserInfo;
import com.hwant.services.IDoWork;
import com.hwant.services.IMService;

import f.in;

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
	private Uri uri = null;

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
				uri = Uri.parse("content://org.hwant.im.chat/chat");
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
		} else if (packet instanceof Presence) {
			Presence presence = (Presence) packet;
			String from = presence.getFrom();
			String to = presence.getTo();
			Type type = presence.getType();
			BmobQuery query = new BmobQuery("userinfo");
			query.addWhereEqualTo("userid", from);
			query.setLimit(1);
			ContentValues values = new ContentValues();
			query.findObjects(application, new MyBmobFind(application));
//			resolver=application.getContentResolver();
			if (Presence.Type.subscribe.equals(type)) {
				uri = Uri.parse("content://org.hwant.im.relationship/relation");
				
				values.put("mfrom", from);
				values.put("mto", to);
				values.put("muser", application.user.getJid());
				values.put("date", String.valueOf(new Date().getTime()));
				values.put("state", 1);
				resolver.insert(uri, values);
				// 好友申请
//				intent = new Intent();
//				intent.setAction(RecevierConst.Connect_Subscribed);
			} else if (Presence.Type.subscribed.equals(type)) {
				uri = Uri.parse("content://org.hwant.im.relationship/relation");
				
				values.put("mfrom", from);
				values.put("mto", to);
				values.put("muser", application.user.getJid());
				values.put("date", String.valueOf(new Date().getTime()));
				values.put("state", 2);
				resolver.insert(uri, values);
				// 同意添加为好友
//				intent = new Intent();
//				intent.setAction(RecevierConst.Connect_Subscribe);
			} else if (Presence.Type.unsubscribe.equals(type)) {
				uri = Uri.parse("content://org.hwant.im.relationship/relation");
				
				values.put("mfrom", from);
				values.put("mto", to);
				values.put("muser", application.user.getJid());
				values.put("date", String.valueOf(new Date().getTime()));
				values.put("state", 3);
				resolver.insert(uri, values);
				// 拒绝添加好友
//				intent = new Intent();
//				intent.setAction(RecevierConst.Connect_Unsubscribe);
			} else if (Presence.Type.unsubscribed.equals(type)) {
				uri = Uri.parse("content://org.hwant.im.relationship/relation");
				
				values.put("mfrom", from);
				values.put("mto", to);
				values.put("muser", application.user.getJid());
				values.put("date", String.valueOf(new Date().getTime()));
				values.put("state", 4);
				resolver.insert(uri, values);
				// 删除好友
//				intent = new Intent();
//				intent.setAction(RecevierConst.Connect_Unsubscribed);
			} else if (Presence.Type.unavailable.equals(type)) {
				// 联系人下线
				intent = new Intent();
				intent.setAction(RecevierConst.Connect_Unavailable);
			} else if (Presence.Type.available.equals(type)) {
				// 联系人在线
				intent = new Intent();
				intent.setAction(RecevierConst.Connect_Available);
			}
			if (intent != null) {
				// intent.putExtra("connect", info);
				// service.sendBroadcast(intent);
			}
		}
	}

	class MyBmobFind implements FindCallback {
		private Context context = null;

		public MyBmobFind(Context context) {
			this.context = context;
		}

		@Override
		public void onFailure(int arg0, String arg1) {

		}

		@Override
		public void onSuccess(JSONArray array) {
			try {
				JSONObject jobj = array.getJSONObject(0);
				ContentResolver resolver = this.context.getContentResolver();
				ContentValues values = new ContentValues();
				values.put("objid", jobj.getString("objectid"));
				values.put("image", jobj.getString("userimg"));
				Uri uri = Uri.parse("org.hwant.im.relationship/relation");
				resolver.update(uri, values, " jid=? ",
						new String[] { jobj.getString("userid") });
				BmobProFile.getInstance(application).download(
						jobj.getString("userimg"), new DownloadListener() {

							@Override
							public void onError(int arg0, String arg1) {

							}

							@Override
							public void onSuccess(String arg0) {

							}

							@Override
							public void onProgress(String arg0, int arg1) {

							}
						});
				// jobj.getString("userid");
				// jobj.getString("userimg");
				// 发广播
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	class GetConnectInfoWork implements IDoWork {
		private IMService service;
		private String jid = "";

		public GetConnectInfoWork(IMService service, String jid) {
			this.service = service;
			this.jid = jid;
		}

		@Override
		public Object doWhat() {
			VCard vCard = new VCard();
			if (service.getConnection().isConnected()
					&& service.getConnection().isAuthenticated()) {
				try {
					vCard.load(service.getConnection(), jid + "@"
							+ Common.DomainName);
					ContentValues values = new ContentValues();
					values.put("nickname", vCard.getNickName());
					Uri uri = Uri.parse("org.hwant.im.relationship/relation");
					ContentResolver resolver = this.service
							.getContentResolver();
					resolver.update(uri, values, " jid=? ",
							new String[] { jid });
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		public void Finish2Do(Object obj) {
			// 发广播
		}

	}
}
