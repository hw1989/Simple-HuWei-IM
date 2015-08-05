package com.hwant.db;

import org.wind.util.StringHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class RelationshipProvider extends ContentProvider {
    public static final String AUTHORITY = "org.hwant.im.relationship";
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		matcher.addURI(AUTHORITY, "relation", 1);
		matcher.addURI(AUTHORITY, "relation/#", 2);
	}
    private MySQLiteOpenHelper sqlite=null;
    private SQLiteDatabase database=null;
	@Override
	public boolean onCreate() {
		sqlite=new MySQLiteOpenHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder=new SQLiteQueryBuilder();
		builder.setTables("relationship");
		switch(matcher.match(uri)){
		case 1:
			break;
		case 2:
			builder.appendWhere(" _id=");
			builder.appendWhere(uri.getPathSegments().get(1));
			break;
		case 3:
			break;
		case 4:
			break;
		default:
			throw new IllegalArgumentException("uri错误!");
		}
		database=sqlite.getReadableDatabase();
//		Cursor cursor =builder.query(database, projection, selection, selectionArgs, null,null,Common.Table_Order_ASC);
		Cursor cursor =builder.query(database, projection, selection, selectionArgs, null,null,sortOrder);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		String str="";
		switch(matcher.match(uri)){
		case 1:
			str="vnd.android.cursor.dir/org.hwant.im.relationship";
			break;
		case 2:
			str="vnd.android.cursor.item/org.hwant.im.relationship";
			break;
		}
		return str;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(values==null){
			throw new IllegalArgumentException("对象不能为null!");
		}
		if(matcher.match(uri)!=1){
			int i=matcher.match(uri);
			throw new IllegalArgumentException("uri在insert中错误!");
		}
		database=sqlite.getWritableDatabase();
		database.insert("relationship", null, values);
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		database=sqlite.getWritableDatabase();
		int count=0;
		switch(matcher.match(uri)){
		case 1:
		    count=database.delete("relationship", selection, selectionArgs);
			break;
		case 2:
			StringBuilder builder=new StringBuilder();
			String str=uri.getPathSegments().get(1);
			if(StringHelper.isEmpty(selection)){
				builder.append(" _id= ").append(str);
			}else{
				builder.append(" _id=").append(str).append(" and (").append(selection).append(" )");
			}
			count=database.delete("relationship", selection, selectionArgs);
			break;
		case 3:
			break;
		case 4:
			break;
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if(values==null){
			throw new IllegalArgumentException("参数不能为null!");
		}
		if(matcher.match(uri)!=1||matcher.match(uri)!=2){
			throw new IllegalArgumentException("update操作的uri错误!");
		}
		database=sqlite.getWritableDatabase();
		int count=0;
		if(matcher.match(uri)==1){
			count=database.update("relationship", values, selection, selectionArgs);
		}else if(matcher.match(uri)==3){
			
		}
		return count;
	}

}
