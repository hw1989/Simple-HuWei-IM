package com.hwant.asmack;

import java.util.Collection;
import java.util.Date;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import com.hwant.application.IMApplication;
import com.hwant.db.MySQLiteOpenHelper;
import com.hwant.entity.ConnectInfo;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyRosterListener implements RosterListener {
	private Roster roster = null;
	private SQLiteDatabase sdb = null;
	// private SQLiteDatabase database = null;
	private MySQLiteOpenHelper helper = null;
	private XMPPConnection connection;
	private ContentResolver resolver = null;
	private Uri uri = null;
    private IMApplication application=null;
	public MyRosterListener(XMPPConnection connection,Application application) {
		this.application=(IMApplication)application;
		this.roster = connection.getRoster();
		helper = new MySQLiteOpenHelper(application.getApplicationContext());
		// database = helper.getWritableDatabase();
		resolver = application.getApplicationContext().getContentResolver();
		// 日期格式
	}

	// 添加联系人
	@Override
	public void entriesAdded(Collection<String> arg0) {
		RosterEntry entry = null;
		ConnectInfo info = null;
		// database.beginTransaction();
		for (String str : arg0) {
			entry = this.roster.getEntry(str);
			// info = new FriendInfo();
			// info.setJid(entry.getUser());
			Date date = new Date();
			ContentValues values = new ContentValues();
			values.put("jid", entry.getUser());
			values.put("date", String.valueOf(date.getTime()));
			values.put("status", -1);
			values.put("user", application.user.getJid());
			uri = Uri.parse("content://com.hwant.im.friend/friend");
			resolver.insert(uri, values);
			// Insert insert=new Insert(FriendInfo.class);

			// 写入数据库

			// String sql= Insert.into(FriendInfo.class).field(info,
			// values).getSQL();

			// values.put(key, value);
			// 写完数据库后请求的发送广播
		}
		// database.endTransaction();
	}

	@Override
	public void entriesDeleted(Collection<String> arg0) {

	}

	@Override
	public void entriesUpdated(Collection<String> arg0) {

	}

	@Override
	public void presenceChanged(Presence arg0) {

	}

}
