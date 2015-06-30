package com.hwant.asmack;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.wind.net.NetUtil;

import android.app.Service;
import android.content.Intent;
import android.util.Log;

import com.hwant.common.RecevierConst;

public class AsmackInit {
	private static int TimeOut = 5000;
	private Service service = null;
	private ConnectionConfiguration configuration = null;
	private XMPPConnection connection = null;

	public AsmackInit(Service service) {
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
		if (NetUtil.getNetStatus(this.service) == NetUtil.NET_STATUS_NONE) {
			// 发布连接网络失败的广播
			Intent intent = new Intent(RecevierConst.NET_STATUS_FAILURE);
			this.service.sendBroadcast(intent);
			return null;
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
		return connection;
	}

	public boolean setLogin(String name, String psw) {
		boolean flag = false;
		if (connection == null) {
			return flag;
		}
		try {
			connection.connect();
			if (!connection.isConnected()) {
				return flag;
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		try {
			if (connection.isConnected()) {
				connection.login(name, psw);
				flag = true;
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
}
