package com.hwant.activity;

import java.lang.ref.WeakReference;

import org.jivesoftware.smack.Roster;
import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;
import org.wind.util.PreferenceUtils;
import org.wind.util.StringHelper;

import com.hwant.application.IMApplication;
import com.hwant.broadcast.IXMPPWork;
import com.hwant.broadcast.XMPPRecevier;
import com.hwant.common.RecevierConst;
import com.hwant.dialog.LoginDialog;
import com.hwant.services.IDoWork;
import com.hwant.services.IMService;
import com.hwant.services.IMService.SBinder;
import com.hwant.services.TaskManager;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	@ViewInject(id = R.id.btn_login)
	private Button btn_login;
	// 登陆名
	@ViewInject(id = R.id.et_login_name)
	private EditText et_login_name;
	// 登陆密码
	@ViewInject(id = R.id.et_login_psw)
	private EditText et_login_psw;
	private ServiceConnection connection = null;
	private IMService service = null;
	// 登陆时的对话框
	private LoginDialog dialog = null;
	// private PreferenceUtils sharepreference = null;
	private TaskManager manager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// IntentFilter filter = new IntentFilter();

		connection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				SBinder sbinder = (SBinder) binder;
				service = sbinder.getService();
				manager = sbinder.getTaskManager();
				manager.addTask(new ConnectServer());
			}
		};
		Intent intent = new Intent(this, IMService.class);
		startService(new Intent(this, IMService.class));

		setContentView(R.layout.login_layout);
		// filter.addAction(RecevierConst.Server_Connect);
		// filter.addAction(RecevierConst.Server_Login);
		bindService(intent, connection, Service.BIND_AUTO_CREATE);
		init();

	}

	private void init() {
		// 初始化
		dialog = new LoginDialog(this, R.style.dialog);
		ActivityInject.getInstance().setInject(this);
		btn_login.setOnClickListener(this);
		// sharepreference = PreferenceUtils.init(getApplication());
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		if (manager != null) {
			manager.removeAll();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (connection != null) {
			unbindService(connection);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			String name = et_login_name.getText().toString();
			String psw = et_login_psw.getText().toString();
			// if (StringHelper.isEmpty(name) || StringHelper.isEmpty(psw)) {
			// if (dialog != null && !dialog.isShowing()) {
			// dialog.init(R.layout.dialog_login_tip_layout);
			// dialog.show();
			// }
			// return;
			// }
			manager.addTask(new LoginServer());
			break;
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
			Toast.makeText(LoginActivity.this, "可以获取连接服务器的状态",
					Toast.LENGTH_LONG).show();
		}
	}

	class LoginServer implements IDoWork {

		@Override
		public Object doWhat() {
			service.getAsmack().setLogin("huwei", "123456");
			return null;
		}

		@Override
		public void Finish2Do(Object obj) {
			service.addConnectListener();
			Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
			Toast.makeText(LoginActivity.this, "可以获取连接服务器的状态",
					Toast.LENGTH_LONG).show();
		}

	}
}
