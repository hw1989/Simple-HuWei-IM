package com.hwant.activity;

import java.lang.ref.WeakReference;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPException;
import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import com.hwant.common.Common;
import com.hwant.entity.ConnectInfo;
import com.hwant.services.IDoWork;
import com.hwant.services.IMService;

import a.This;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AddConnectActivity extends BaseActivity implements OnClickListener {
	@ViewInject(id = R.id.tv_addconnect_back)
	private TextView tv_back;
	@ViewInject(id = R.id.btn_addconnect_msg)
	private Button btn_msg;
	@ViewInject(id = R.id.btn_addconnect_add)
	private Button btn_add;
	private ConnectInfo info = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.add_connect_layout);
		ActivityInject.getInstance().setInject(this);
		info = (ConnectInfo) getIntent().getSerializableExtra("connect");
		init();
		bindService();
	}

	private void init() {
		tv_back.setOnClickListener(this);
		btn_add.setOnClickListener(this);
		btn_msg.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_addconnect_back:
			finish();
			break;
		case R.id.btn_addconnect_msg:
			break;
		case R.id.btn_addconnect_add:
			service.manager.addTask(new AddConnectWork(service, this, info
					.getJid()));
			break;
		}
	}

	class AddConnectWork implements IDoWork {
		private IMService service = null;
		private WeakReference<Activity> weakReference = null;
		private String connect = null;

		public AddConnectWork(IMService service, Activity activity,
				String connect) {
			this.service = service;
			this.weakReference = new WeakReference<Activity>(activity);
			this.connect = connect;
		}

		@Override
		public Object doWhat() {
			if (service.getConnection().isConnected()
					&& service.getConnection().isAuthenticated()) {
				Roster roster = service.getConnection().getRoster();
				try {
					roster.createEntry(connect + "@" + Common.DomainName, null,
							new String[] { "firends" });
					return true;
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
			return false;
		}

		@Override
		public void Finish2Do(Object obj) {
			if (weakReference.get() != null) {
				boolean flag = (Boolean) obj;
				if (flag) {
					showToast("等待对方同意!");
				} else {
					showToast("联系人添加失败!");
				}
			}
		}

	}
}
