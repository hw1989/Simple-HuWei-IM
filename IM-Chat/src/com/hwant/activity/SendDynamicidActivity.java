package com.hwant.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import com.bmob.BmobProFile;
import com.hwant.adapter.SendDynamAdapter;
import com.hwant.common.Common;
import com.hwant.common.RecevierConst;
import com.hwant.view.XGridView;

import f.in;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class SendDynamicidActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	@ViewInject(id = R.id.tv_sdynamic_back)
	private TextView tv_back;
	@ViewInject(id = R.id.tv_sdynamic_send)
	private TextView tv_send;
	@ViewInject(id = R.id.et_sdynamic_content)
	private EditText et_content;
	@ViewInject(id = R.id.gv_sdynamic_imgs)
	private XGridView gv_imgs;
	private SendDynamAdapter adapter = null;
	private Intent intent = null;

	private Bitmap bitmap = null;
	private String item = "";
	private ArrayList<String> files = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.send_dynamicid_layout);
		ActivityInject.getInstance().setInject(this);
		init();
	}

	private void init() {
		tv_back.setOnClickListener(this);
		tv_send.setOnClickListener(this);
		gv_imgs.setOnItemClickListener(this);
		adapter = new SendDynamAdapter(this, null);
		gv_imgs.setAdapter(adapter);
		adapter.addItem("", 0);

		files = new ArrayList<String>();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sdynamic_back:
			finish();
			break;
		case R.id.tv_sdynamic_send:
			String content = et_content.getText().toString().trim();
			if ("".equals(content) && adapter.getCount() <= 1) {
				showToast("发表的内容不能为空!");
				return;
			}
			intent = new Intent();
			intent.setAction(RecevierConst.Service_Work_Dynamic);
			intent.putExtra("content",content);
			intent.putStringArrayListExtra("images", adapter.getAllItem());
			sendBroadcast(intent);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == adapter.getCount() - 1) {
			intent = new Intent(this, PickImgActivity.class);
			startActivityForResult(intent, 1);
		} else {

		}
	}

	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		if (resultcode == RESULT_OK) {
			if (requestcode == 1) {
				ArrayList<String> list = data.getStringArrayListExtra("images");
				adapter.addItems(list, adapter.getCount() - 1);
			}
		}
	}
}
