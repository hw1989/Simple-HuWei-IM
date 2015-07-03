package com.hwant.asmack;

import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.wind.database.Insert;

import com.hwant.db.MySQLiteOpenHelper;
import com.hwant.entity.FriendInfo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyRosterListener implements RosterListener {
	private Roster roster = null;
	private SQLiteDatabase sdb = null;
	private SQLiteDatabase database = null;
	private MySQLiteOpenHelper helper = null;
	private XMPPConnection connection;

	public MyRosterListener(XMPPConnection connection, Context context) {
		this.roster = connection.getRoster();
		helper = new MySQLiteOpenHelper(context);
		database = helper.getWritableDatabase();
	}

	// 添加联系人
	@Override
	public void entriesAdded(Collection<String> arg0) {
		RosterEntry entry = null;
		FriendInfo info=null;
		database.beginTransaction();
		for (String str : arg0) {
			entry = this.roster.getEntry(str);
			info=new FriendInfo();
			info.setJid(entry.getUser());
//			Insert insert=new Insert(FriendInfo.class);
			
			// 写入数据库
			ContentValues values=new ContentValues();
			String sql= Insert.into(FriendInfo.class).field(info, values).getSQL();
			int i=0;
			i++;
//			values.put(key, value);
			// 写完数据库后请求的发送广播
		}
		database.endTransaction();
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
