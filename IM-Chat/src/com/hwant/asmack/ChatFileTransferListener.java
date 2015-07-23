package com.hwant.asmack;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.wind.util.StringHelper;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.hwant.application.IMApplication;
import com.hwant.common.Common;
import com.hwant.common.RecevierConst;
import com.hwant.entity.ChatMessage;
import com.hwant.entity.UserInfo;
import com.hwant.utils.MessageUtils;

public class ChatFileTransferListener implements FileTransferListener {
	private ContentResolver resolver = null;
	private Uri uri = null;
	private Intent intent = null;
	private IMApplication application = null;

	public ChatFileTransferListener(Application application) {
		this.application = (IMApplication) application;
		this.resolver = application.getContentResolver();
		this.uri = Uri.parse("content://org.hwant.im.chat/chat");
		this.intent = new Intent();
	}

	@Override
	public void fileTransferRequest(final FileTransferRequest request) {
		// new Thread() {
		// @Override
		// public void run() {
		try {
			File file = new File(Environment.getExternalStorageDirectory()
					+ Common.Path_Media + request.getFileName());
			if (!file.exists()) {
				Log.i("have no file", file.getPath());
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			String requestor = request.getRequestor();
			IncomingFileTransfer transfer = request.accept();
			// 保存文件
			transfer.recieveFile(file);
			ContentValues values = new ContentValues();
			int indexfrom = requestor.lastIndexOf("/");
			int indexto = application.user.getJid().lastIndexOf("/");
			String mfrom = "", mto = "";
			if (indexfrom >= 0) {
				mfrom = requestor.substring(0, indexfrom);
			}
			if (indexto >= 0) {
				mto = application.user.getJid().substring(0, indexto);
			}
			values.put("mfrom", mfrom);
			values.put("mto", mto);
			// 服务器传过来有后缀 /Spark /Smack
			// values.put("mfrom", mess.getFrom());
			// values.put("mto", application.user.getJid());
			// 使用正则表达式转换
			values.put("message", MessageUtils.setVoice(file.getAbsolutePath()));
			values.put("read", "0");
			values.put("user", application.user.getJid());
			Date date = new Date();
			values.put("time", String.valueOf(date.getTime()));
			resolver.insert(uri, values);
			//A与B聊天，C发消息给A会异常(当前界面出现C的消息)
//			ChatMessage message = new ChatMessage();
//			message.setMfrom(mess.getFrom());
//			message.setMessage(mess.getBody());
//			message.setMto(application.user.getJid());
//			UserInfo info = new UserInfo();
//			info.setJid(mess.getFrom());
//			message.setInfo(info);
//			intent.setAction(RecevierConst.Chat_One_Get);
//			intent.putExtra("msg", message);
			intent.setAction(RecevierConst.Chat_DB_Get);
			intent.putExtra("mfrom", mfrom);
			intent.putExtra("mto", mto);
//			// 发送广播
			application.getApplicationContext().sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
		// }.start();
	}

}
