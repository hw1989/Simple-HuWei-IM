package com.hwant.activity;

import java.lang.ref.WeakReference;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;
import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;
import org.wind.util.StringHelper;
import cn.bmob.v3.listener.SaveListener;
import com.hwant.asmack.AsmackInit;
import com.hwant.bomb.entity.BmobUserInfo;
import com.hwant.common.Common;
import com.hwant.services.IDoWork;
import com.hwant.services.IMService;
import com.hwant.services.TaskManager;
import com.hwant.services.IMService.SBinder;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	@ViewInject(id = R.id.et_regist_name)
	private EditText et_username = null;
	@ViewInject(id = R.id.et_regist_psw)
	private EditText et_userpsw = null;
	@ViewInject(id = R.id.btn_regist_sure)
	private Button btn_regist = null;
	@ViewInject(id = R.id.tv_regist_back)
	private TextView tv_back = null;
	private String username = "";
	private String userpsw = "";
	// 标记服务绑定
	private boolean flag = false;
	private ServiceConnection connection = null;
	private IMService service = null;
	private TaskManager manager = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.regist_layout);
		connection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				SBinder sbinder = (SBinder) binder;
				service = sbinder.getService();
				AsmackInit init = service.getAsmack();
				if (init != null) {
					if (init.getConnection().isConnected()
							&& init.getConnection().isAuthenticated()) {
						// 已经连接并登陆成功
						Intent intent = new Intent(RegisterActivity.this,
								IndexActivity.class);
						startActivity(intent);
					}
				}
				manager = sbinder.getTaskManager();
				manager.addTask(new ConnectServer());
			}
		};
		Intent intent = new Intent(this, IMService.class);
		startService(new Intent(this, IMService.class));
		bindService(intent, connection, Service.BIND_AUTO_CREATE);
		ActivityInject.getInstance().setInject(this);

		initEvent();

	}

	private void initEvent() {
		btn_regist.setOnClickListener(this);
		tv_back.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(connection!=null){
			unbindService(connection);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_regist_sure:
			username = et_username.getText().toString().trim();
			userpsw = et_userpsw.getText().toString().trim();
			if (StringHelper.isEmpty(username) || StringHelper.isEmpty(userpsw)) {
				Toast.makeText(this, "用户名和密码不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!flag) {
				Toast.makeText(this, "service没有绑定完成!", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			service.manager.addTask(new MyRegist(service, this));
			break;
		case R.id.tv_regist_back:
			finish();
			break;
		}
	}

	class MyRegist implements IDoWork {
		private IMService service = null;
		private WeakReference<Activity> weakReference = null;

		public MyRegist(IMService service, Activity activity) {
			weakReference = new WeakReference<Activity>(activity);
			this.service = service;
		}

		@Override
		public Object doWhat() {
			Registration registration = new Registration();
			// Registration reg = new Registration();
			// reg.setType(IQ.Type.SET);
			// reg.setTo(connection.getServiceName());
			// Map<String, String> map = new HashMap<String, String>();
			// map.put("username", account);
			// map.put("password", password);
			// reg.setAttributes(map);
			registration.setType(IQ.Type.SET);
			//
			registration.setTo(Common.DomainName);
			registration.setUsername(username);
			registration.setPassword(userpsw);
			registration.addAttribute("android", "geolo_createUser_android");// 这边addAttribute不能为空，否则出错。所以做个标志是android手机创建的吧！！！！！
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					registration.getPacketID()), new PacketTypeFilter(IQ.class));
			PacketCollector collector = this.service.getConnection()
					.createPacketCollector(filter);
			this.service.getConnection().sendPacket(registration);
			IQ result = (IQ) collector.nextResult(SmackConfiguration
					.getPacketReplyTimeout());
			collector.cancel();
			return result;
		}

		@Override
		public void Finish2Do(Object obj) {
			if (weakReference.get() != null) {
				if (obj == null) {

				} else {
					IQ result = (IQ) obj;
					if (result.getType() == IQ.Type.ERROR) {
						if (result.getError().toString().contains("409")) {
							// 账号已存在
						} else {

						}
					} else if (result.getType() == IQ.Type.RESULT) {
						BmobUserInfo userInfo = new BmobUserInfo();
						userInfo.setUserid(username);
						userInfo.save(RegisterActivity.this,
								new MySaveListener());
					} else {

					}
				}
			}
		}

	}

	class MySaveListener extends SaveListener {

		@Override
		public void onFailure(int arg0, String arg1) {
			Toast.makeText(RegisterActivity.this, arg1, Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onSuccess() {

		}

	}

	class ConnectServer implements IDoWork {
		@Override
		public Object doWhat() {
			service.getAsmack().startConnect();
			return null;
		}

		@Override
		public void Finish2Do(Object obj) {
			flag = true;
			Toast.makeText(RegisterActivity.this, "可以获取连接服务器的状态",
					Toast.LENGTH_LONG).show();
		}
	}
}
