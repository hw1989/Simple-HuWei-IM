package com.hwant.activity;

import java.util.ArrayList;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import com.hwant.adapter.ManageConnectAdapter;
import com.hwant.entity.Relationship;
import com.hwant.view.xlistview.XListView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

public class NewConnectActivity extends BaseActivity {
	@ViewInject(id = R.id.xlv_newconnect_list)
	private XListView xlv_list = null;
	private ManageConnectAdapter adapter = null;
	private ContentResolver resolver = null;
	private Uri uri = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.new_connect_layout);
		ActivityInject.getInstance().setInject(this);
		resolver = getContentResolver();
		uri = Uri.parse("org.hwant.im.relationship/relation");
		init();
	}

	private void init() {
		adapter = new ManageConnectAdapter(this, null);
		Cursor cursor = resolver.query(uri, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			ArrayList<Relationship> list = new ArrayList<Relationship>();
			Relationship relationship = null;
			while (!cursor.isAfterLast()) {
				relationship = new Relationship();
				list.add(relationship);
			}
			cursor.close();
		}

	}
}
