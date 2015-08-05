package com.hwant.asmack;

import java.io.File;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.wind.net.NetUtil;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.telephony.gsm.SmsMessage.MessageClass;
import android.util.Log;

import com.hwant.common.Common;
import com.hwant.common.RecevierConst;
import com.hwant.services.IMService;

public class AsmackInit {
	private static int TimeOut = 5000;
	private Context context = null;
	private ConnectionConfiguration configuration = null;
	private XMPPConnection connection = null;
	private Application application = null;
	private IMService service = null;

	// private FileTransferListener ftlistener = null;

	public synchronized XMPPConnection getConnection() {
		return connection;
	}

	public AsmackInit(Application application, IMService service) {
		this.application = application;
		this.context = application.getApplicationContext();
		this.service = service;
	}

	/**
	 * 创建连接对象
	 * 
	 * @param ip连接服务器的地址
	 * @param port端口
	 * @param security是否加密
	 */
	public XMPPConnection setConnect(String ip, int port, boolean security) {
		// 判断网络发布广播
		if (NetUtil.getNetStatus(this.context) == NetUtil.NET_STATUS_NONE) {
			// 发布连接网络失败的广播
			Intent intent = new Intent(RecevierConst.Net_Stauts_Failure);
			this.context.sendBroadcast(intent);
			return null;
		}
		try {
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		configuration = new ConnectionConfiguration(ip, port);
		// 设置自动连接
		configuration.setReconnectionAllowed(true);
		// 设置是否在登陆的时候告诉服务器，默认为true
		configuration.setSendPresence(true);
		configuration.setCompressionEnabled(false);
		configuration.setDebuggerEnabled(false);
		configuration.setSecurityMode(SecurityMode.disabled);
		connection = new XMPPConnection(configuration);
		// 设置连接的监听
		// connection.addConnectionListener(new AConnectionListener());
		PacketTypeFilter filter = new PacketTypeFilter(Message.class);
		// 对发送消息进行监听
		connection.addPacketListener(new MyPacketListener(application,
				this.service), filter);
		connection.addPacketInterceptor(new PacketInterceptor() {

			@Override
			public void interceptPacket(Packet packet) {
			}
		}, null);

		// connection.addPacketListener(new PacketListener() {
		//
		// @Override
		// public void processPacket(Packet packet) {
		// }
		// }, null);
		return connection;
	}

	/**
	 * 开始与服务器建立连接
	 */
	public boolean startConnect() {
		boolean flag = false;
		if (connection == null) {
			return flag;
		}
		try {
			connection.connect();
			if (connection.isConnected()) {
				flag = true;
				// 文件传输时的监听
				ServiceDiscoveryManager sdmanager = ServiceDiscoveryManager
						.getInstanceFor(connection);
				if (sdmanager == null) {
					sdmanager = new ServiceDiscoveryManager(connection);
				}
				sdmanager.addFeature("http://jabber.org/protocol/disco#info");
				sdmanager.addFeature("jabber:iq:privacy");
				FileTransferManager manager = new FileTransferManager(
						connection);
				FileTransferNegotiator.setServiceEnabled(connection, true);
				// 设置文件传输的监听
				manager.addFileTransferListener(new ChatFileTransferListener(
						this.application));
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 开始登陆账号
	 */
	public boolean setLogin(String name, String psw) {
		boolean flag = false;
		try {
			if (connection != null && connection.isConnected()) {
				connection.login(name, psw);
				// 判断登陆后是否认证成功
				flag = connection.isAuthenticated();
				Presence presence = new Presence(Type.available);
				connection.sendPacket(presence);
				// if (flag) {
				// // 设置监听
				// Roster roster=connection.getRoster();
				// roster.addRosterListener(new
				// MyRosterListener(connection,this.context));
				// }
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return flag;
	}

	class AConnectionListener implements ConnectionListener {

		@Override
		public void connectionClosed() {

		}

		@Override
		public void connectionClosedOnError(Exception arg0) {
		}

		@Override
		public void reconnectingIn(int arg0) {

		}

		@Override
		public void reconnectionFailed(Exception arg0) {
			// 发生错误时连接关闭
			Log.e("info", "error  1" + arg0.getMessage());
		}

		@Override
		public void reconnectionSuccessful() {
		}

	}

	/**
	 * 单人聊天
	 * 
	 * @param from
	 * @param to
	 * @param content
	 */
	public boolean sendMessage(String from, String to, String content) {
		Message msg = new Message();
		msg.setFrom(from);
		msg.setTo(to);
		msg.setType(Message.Type.chat);
		msg.setBody(content);
		if (connection.isConnected() && connection.isAuthenticated()) {
			try {
				connection.sendPacket(msg);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
