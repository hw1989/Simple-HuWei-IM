package com.hwant.activity;

import java.util.ArrayList;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import com.baidu.location.x;
import com.hwant.adapter.ManageConnectAdapter;
import com.hwant.entity.Relationship;
import com.hwant.view.xlistview.XListView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class NewConnectActivity extends BaseActivity implements OnClickListener {
	@ViewInject(id = R.id.xlv_newconnect_list)
	private XListView xlv_list = null;
	private ManageConnectAdapter adapter = null;
	private ContentResolver resolver = null;
	private Uri uri = null;
	@ViewInject(id = R.id.tv_newconnect_back)
	private TextView tv_back = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.new_connect_layout);
		ActivityInject.getInstance().setInject(this);
		resolver = getContentResolver();
		uri = Uri.parse("content://org.hwant.im.relationship/relation");
		init();
	}

	private void init() {
		tv_back.setOnClickListener(this);
		adapter = new ManageConnectAdapter(this, null);
		xlv_list.setPullLoadEnable(false);
		xlv_list.setPullRefreshEnable(false);
		xlv_list.setAdapter(adapter);
		Cursor cursor = resolver.query(uri, null, null, null, null);
		ArrayList<Relationship> list = new ArrayList<Relationship>();
		if (cursor != null) {
			cursor.moveToFirst();
			Relationship relationship = null;
			while (!cursor.isAfterLast()) {
				relationship = new Relationship();
				list.add(relationship);
				cursor.moveToNext();
			}
			cursor.close();
		}
		adapter.addData(list);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_newconnect_back:
			finish();
			break;
		}

	}
}
