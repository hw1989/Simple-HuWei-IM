package com.hwant.application;

import java.io.File;

import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.wind.util.PreferenceUtils;

import com.hwant.asmack.AsmackInit;
import com.hwant.common.Common;
import com.hwant.db.MySQLiteOpenHelper;
import com.hwant.entity.UserInfo;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class IMApplication extends Application {
	// 确定是否连接
	// public boolean isconnect = false;
	// 确定是否登陆成功
	// public boolean islogin = false;
	private PreferenceUtils sharepreference = null;
	// public boolean hasdb = false;
	// 标记是否创建过数据库
	public MySQLiteOpenHelper helper = null;
	// 登陆人的信息
	public UserInfo user = null;

	@Override
	public void onCreate() {
		super.onCreate();
		SmackAndroid.init(this);
		user = new UserInfo();
		sharepreference = PreferenceUtils.init(this, Common.IM_Config);
		// 判断是否创建过数据库
		// hasdb = sharepreference.getBool("database", false);
		helper = new MySQLiteOpenHelper(this);
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
				getFilesDir() + File.separator + Common.DB_File_Name, null);
		helper.onCreate(database);
		sharepreference.putBoolen("database", true);
	}

}
