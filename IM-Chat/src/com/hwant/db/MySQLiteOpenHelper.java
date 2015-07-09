package com.hwant.db;

import org.wind.database.TableHelper;

import com.hwant.common.Common;
import com.hwant.entity.ChatMessage;
import com.hwant.entity.ConnectInfo;

import android.R.integer;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public MySQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public MySQLiteOpenHelper(Context context) {
		this(context, Common.DB_Name, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		TableHelper friend = new TableHelper(ConnectInfo.class);
		TableHelper chatMessage = new TableHelper(ChatMessage.class);
		db.beginTransaction();
		try {
			db.execSQL(friend.getSQL());
			db.execSQL(chatMessage.getSQL());
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			// db.close();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
