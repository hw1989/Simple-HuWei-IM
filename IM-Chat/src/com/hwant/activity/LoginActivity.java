package com.hwant.activity;

import java.io.IOException;

import org.apache.harmony.javax.security.sasl.SaslException;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private Button btn_login = null;
	private XMPPConnection connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		ConnectionConfiguration configuration = new ConnectionConfiguration(
				"192.168.192.", 3306);
		// 允许自动连接
		configuration.setReconnectionAllowed(true);
		configuration.setSendPresence(true);
		connection = new XMPPConnection(configuration) {

			@Override
			protected void connectInternal() throws SmackException,
					IOException, XMPPException {
				// TODO Auto-generated method stub

			}

			@Override
			public String getConnectionID() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getUser() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isAnonymous() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isAuthenticated() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isConnected() {
				Toast.makeText(getApplicationContext(), "123!",
						Toast.LENGTH_SHORT).show();
				return false;
			}

			@Override
			public boolean isSecureConnection() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isUsingCompression() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void login(String arg0, String arg1, String arg2)
					throws XMPPException, SmackException, SaslException,
					IOException {
				// TODO Auto-generated method stub

			}

			@Override
			public void loginAnonymously() throws XMPPException,
					SmackException, SaslException, IOException {
				// TODO Auto-generated method stub

			}

			@Override
			protected void sendPacketInternal(Packet arg0)
					throws NotConnectedException {
				// TODO Auto-generated method stub

			}

			@Override
			protected void shutdown() {
				// TODO Auto-generated method stub

			}
		};

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			try {
				connection.connect();
			} catch (SmackException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			try {
				connection.login("admin", "123456");
			} catch (SaslException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SmackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}

	}
	public static Chat chat;  
    public static ChatManager chatManager;  
    // 注册单人对话监听  
//    protected  void RegisterMessageListener() {  
//        chatManager = xmppConnection.getChatManager(); 
//        
//        chatManager.addChatListener(new ChatManagerListener() {  
//  
//            public void chatCreated(Chat chat, boolean arg1) {   
//                chat.addMessageListener(new MessageListener() {  //通过添加一个messaagelitener 来接收消息  
//                	@Override
//                	public void processMessage(Chat arg0, Message message) {  
//                        String msg = message.getBody();  //消息主体  
////                        sendhandlemsg(message.getFrom(), msg, false);  
//                    }  
//                });  
//  
//            }  
//        });  
//    }
}
