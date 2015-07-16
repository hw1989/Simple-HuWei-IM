package com.hwant.db;

import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class UserProvider extends ContentProvider {
	private SQLiteDatabase database = null;
	private MySQLiteOpenHelper sqlite = null;
	private static String AUTHORITY = "com.hwant.im.login";
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		matcher.addURI(AUTHORITY, "user", 1);
		matcher.addURI(AUTHORITY, "user/#", 2);
	}

	@Override
	public boolean onCreate() {
		sqlite = new MySQLiteOpenHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		database = sqlite.getReadableDatabase();
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables("usertable");
		switch (matcher.match(uri)) {
		case 1:

			break;
		case 2:
			builder.appendWhere(" _id='" + uri.getPathSegments().get(1) + "' ");
			break;
		default:
			throw new IllegalArgumentException("uri查询发生错误!");
		}
		Cursor cursor = builder.query(database, projection, selection,
				selectionArgs, null, null, null);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		String type = "";
		switch (matcher.match(uri)) {
		case 1:
			type = "vnd.android.cursor.dir/com.hwant.im.login";
			break;
		case 2:
			type = "vnd.android.cursor.item/com.hwant.im.login";
			break;
		default:
			throw new IllegalArgumentException("uri格式错误!");
		}
		return type;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (matcher.match(uri) != 1) {
			throw new IllegalArgumentException("uri格式错误!");
		}
		if (values == null) {
			throw new IllegalArgumentException("insert时参数不能为空错误!");
		}
		database = sqlite.getWritableDatabase();
		// database.insert("usertable", "jid", values);
		// long id = database.replace("usertable", null, values);
		// Uri uri2 = ContentUris.withAppendedId(uri, id);
		// insert or ignore into table (fields) values (values);或replace into
		// table (fields) values (values);
		StringBuilder builder = new StringBuilder(
				"insert or ignore into usertable (");
		for (String field : values.keySet()) {
			builder.append(field).append(",");
		}
		builder.deleteCharAt(builder.lastIndexOf(","));
		builder.append(") values (");
		for (String field : values.keySet()) {
			builder.append("?,");
		}
		builder.deleteCharAt(builder.lastIndexOf(","));
		builder.append(")");
		// Object[] objects=new Object[values.size()];
		ArrayList<Object> objects = new ArrayList<Object>();
		for (String field : values.keySet()) {
			objects.add(values.get(field));
		}
		database.execSQL(builder.toString(), objects.toArray());
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (matcher.match(uri) != 1) {
			throw new IllegalArgumentException("uri格式错误!");
		}
		database = sqlite.getWritableDatabase();
		int count = database.delete("usertable", selection, selectionArgs);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if (matcher.match(uri) == UriMatcher.NO_MATCH) {
			return 0;
		}
		if (values == null) {
			throw new IllegalArgumentException("update时参数不能为空错误!");
		}
		database = sqlite.getWritableDatabase();
		int count = database.update("usertable", values, selection,
				selectionArgs);
		return count;
	}

}
