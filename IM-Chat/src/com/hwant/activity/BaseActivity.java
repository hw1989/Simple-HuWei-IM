package com.hwant.activity;

import org.wind.util.FileUtils;

import com.hwant.application.IMApplication;
import com.hwant.common.Common;
import com.hwant.services.IMService;
import com.hwant.services.TaskManager;
import com.hwant.services.IMService.SBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {
	private ServiceConnection connection = null;
	public IMService service = null;
	public TaskManager manager = null;
	// 标记是否绑定
	public IMApplication application = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		application = (IMApplication) getApplication();
		createPath();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (connection != null) {
			unbindService(connection);
		}
	}

	public void bindService() {
		connection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				SBinder sbinder = (SBinder) binder;
				service = sbinder.getService();
				manager = sbinder.getTaskManager();
				bindFinished(manager);
			}
		};
		Intent intent = new Intent(this, IMService.class);
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}

	public void createPath() {
		// 创建缓存文件路径
		FileUtils.createorexistsPath(Environment.getExternalStorageDirectory()
				+ Common.Path_Cache, true);
		// 创建目录文件路径
		FileUtils.createorexistsPath(Environment.getExternalStorageDirectory()
				+ Common.Path_Image, true);
		// 创建媒体的文件的路径
		FileUtils.createorexistsPath(Environment.getExternalStorageDirectory()
				+ Common.Path_Media, true);
	}

	public void bindFinished(TaskManager manager) {

	}
}
