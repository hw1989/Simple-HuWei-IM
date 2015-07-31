package com.hwant.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;
import org.wind.util.StringHelper;

import com.hwant.adapter.SearchConnectAdapter;
import com.hwant.common.Common;
import com.hwant.entity.ConnectInfo;
import com.hwant.fragment.ConnectFragment;
import com.hwant.services.IDoWork;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchConnectActivity extends BaseActivity implements
		OnClickListener {
	@ViewInject(id = R.id.et_input_connect)
	private EditText et_input;
	@ViewInject(id = R.id.tv_connect_search)
	private TextView tv_search;
	@ViewInject(id = R.id.lv_search)
	public ListView lv_search;
	@ViewInject(id = R.id.tv_search_back)
	private TextView tv_back;
	@ViewInject(id = R.id.iv_search_inputclear)
	private ImageView iv_clear;
	private SearchConnectAdapter adapter = null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.search_layout);
		ActivityInject.getInstance().setInject(this);
		init();
		// 绑定服务
		bindService();
	}

	private void init() {
		tv_search.setOnClickListener(this);
		tv_back.setOnClickListener(this);
		iv_clear.setOnClickListener(this);
		et_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count == 0) {
					iv_clear.setVisibility(View.GONE);
				} else {
					iv_clear.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		adapter = new SearchConnectAdapter(this, null);
		lv_search.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_connect_search:
			String connect = et_input.getText().toString();
			if (StringHelper.isEmpty(connect)) {
				Toast.makeText(this, "搜索内容不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (service == null) {
				Toast.makeText(this, "未完成搜索初始化!", Toast.LENGTH_SHORT).show();
				return;
			}
			service.manager.addTask(new SearchConnect(connect, this));
			break;
		case R.id.tv_search_back:
			finish();
			break;
		case R.id.iv_search_inputclear:
			et_input.getText().clear();
			adapter.clearData();
			iv_clear.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	class SearchConnect implements IDoWork {
		private String name = "";
		private WeakReference<Activity> weakReference = null;

		public SearchConnect(String name, Activity activity) {
			this.name = name;
			this.weakReference = new WeakReference<Activity>(activity);
		}

		@Override
		public Object doWhat() {
			ArrayList<ConnectInfo> list = new ArrayList<ConnectInfo>();
			if (service.getConnection().isConnected()
					&& service.getConnection().isAuthenticated()) {
				UserSearchManager manager = new UserSearchManager(
						service.getConnection());
				Form searchform = null;
				try {
					searchform = manager.getSearchForm("search."
							+ service.getConnection().getServiceName());
					Form answerform = searchform.createAnswerForm();
					answerform.setAnswer("Username", true);
					answerform.setAnswer("search", this.name);
					ReportedData data = manager.getSearchResults(answerform,
							"search."
									+ service.getConnection().getServiceName());
					// column:jid,Username,Name,Email
					Iterator<Row> iterator = data.getRows();
					Row row = null;
					while (iterator.hasNext()) {
						ConnectInfo connect = new ConnectInfo();
						row = iterator.next();
						connect.setJid(row.getValues("jid").next().toString());
						connect.setJid(row.getValues("Username").next()
								.toString());
						list.add(connect);
					}
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
			return list;
		}

		@Override
		public void Finish2Do(Object obj) {
			if (weakReference.get() != null) {
				if (obj != null) {
					ArrayList<ConnectInfo> list = (ArrayList<ConnectInfo>) obj;
					adapter.setData(list);
				}
			}
		}

	}

//	/**
//	 * 加载联系人的头像并写入数据库中
//	 */
//	public class LoadImage implements IDoWork {
//		private VCard vcard = null;
//		// private Bitmap bitmap=null;
//		private WeakReference<Activity> weakReference = null;
//		private String user = "";
//		private FileOutputStream fos = null;
//		private SimpleDateFormat format = null;
//		// 文件名
//		private String filename = "";
//
//		public LoadImage(String user) {
//			this.user = user;
//			vcard = new VCard();
//			weakReference = new WeakReference<Activity>(SearchConnectActivity.this);
//			format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//			filename = format.format(new Date()) + ".png";
//		}
//
//		@Override
//		public Object doWhat() {
//			Bitmap bitmap = null;
//			if (StringHelper.isEmpty(user)) {
//				return null;
//			}
//			if (!service.getConnection().isConnected()
//					|| !service.getConnection().isAuthenticated()) {
//				return null;
//			}
//			try {
//				vcard.load(service.getConnection(), user);
//				if (vcard != null && vcard.getAvatar() != null) {
//					bitmap = BitmapFactory.decodeByteArray(vcard.getAvatar(),
//							0, vcard.getAvatar().length);
//				}
//			} catch (XMPPException e) {
//				e.printStackTrace();
//			}
//			if (bitmap != null) {
//				// String name = format.format(new Date()) + ".png";
//				try {
//					fos = new FileOutputStream(new File(
//							Environment.getExternalStorageDirectory()
//									+ Common.Path_Image, filename));
//					bitmap.compress(CompressFormat.PNG, 1, fos);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				} finally {
//					if (fos != null) {
//						try {
//							fos.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//			return bitmap;
//		}
//
//		@Override
//		public void Finish2Do(Object obj) {
//			if (obj != null || weakReference.get() != null) {
//				ImageView iv_img = (ImageView) lv_search.findViewWithTag(user);
//				if (iv_img != null) {
//					adapter.putCache(user, (Bitmap) obj);
//					iv_img.setImageBitmap((Bitmap) obj);
//				}
//			}
//		}
//	}

}
