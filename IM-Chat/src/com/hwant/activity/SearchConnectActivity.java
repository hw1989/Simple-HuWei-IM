package com.hwant.activity;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;
import org.wind.util.StringHelper;

import com.hwant.adapter.SearchConnectAdapter;
import com.hwant.entity.ConnectInfo;
import com.hwant.services.IDoWork;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchConnectActivity extends BaseActivity implements
		OnClickListener,OnItemClickListener{
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
		lv_search.setOnItemClickListener(this);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent=new Intent(this,AddConnectActivity.class);
		startActivity(intent);
	}

}
