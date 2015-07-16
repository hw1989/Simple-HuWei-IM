package com.hwant.activity;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import com.hwant.application.IMApplication;
import com.hwant.common.Common;
import com.hwant.dialog.LoginDialog;
import com.hwant.services.IDoWork;
import com.hwant.services.IMService;
import com.hwant.services.IMService.SBinder;
import com.hwant.services.TaskManager;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
	private IMApplication application = null;
	private ContentResolver resolver = null;

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
		application = (IMApplication) getApplication();
		bindService(intent, connection, Service.BIND_AUTO_CREATE);
		init();
		resolver = getContentResolver();
	}

	class mContentObserver extends ContentObserver {

		public mContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// TODO Auto-generated method stub
			super.onChange(selfChange, uri);
		}
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
			boolean flag = service.getAsmack().setLogin(
					"huwei" + Common.DomainName, "123456");
			return flag;
		}

		@Override
		public void Finish2Do(Object obj) {
			Boolean flag = (Boolean) obj;
			if (obj != null && flag) {
				application.user.setJid("huwei" + Common.DomainName);
				// 插入登陆用户的信息，已经做了重复的处理
				Uri uri = Uri.parse("content://com.hwant.im.login/user");
				ContentValues values = new ContentValues();
				values.put("jid", "huwei" + Common.DomainName);
				values.put("password", "123456");
				resolver.insert(uri, values);
				// 获取登陆用户的信息
				Cursor cursor = resolver.query(uri, null, " jid=? ",
						new String[] { "huwei" + Common.DomainName }, null);
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					application.user.setUserimg(cursor.getString(cursor
							.getColumnIndex("userimg")));
					cursor.moveToNext();
				}
				cursor.close();
				service.addConnectListener();
				Intent intent = new Intent(LoginActivity.this,
						IndexActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
				Toast.makeText(LoginActivity.this, "可以获取连接服务器的状态",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(LoginActivity.this, "登陆失败!", Toast.LENGTH_LONG)
						.show();
			}

		}
	}

//	class Register implements IDoWork {
//
//		@Override
//		public Object doWhat() {
//			// 将用户存到数据库
//			return null;
//		}
//
//		@Override
//		public void Finish2Do(Object obj) {
//
//		}
//
//	}
}
