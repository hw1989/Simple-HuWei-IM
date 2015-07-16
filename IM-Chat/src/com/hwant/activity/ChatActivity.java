package com.hwant.activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.Field;
import org.wind.annotation.ViewInject;
import org.wind.util.StringHelper;

import com.hwant.adapter.ChatMessageAdapter;
import com.hwant.broadcast.ChatReceiver;
import com.hwant.common.Common;
import com.hwant.common.RecevierConst;
import com.hwant.entity.ChatMessage;
import com.hwant.entity.ConnectInfo;
import com.hwant.entity.UserInfo;
import com.hwant.pulltorefresh.PullToRefreshBase;
import com.hwant.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.hwant.pulltorefresh.PullToRefreshListView;
import com.hwant.services.IDoWork;
import com.hwant.view.faceview.FaceView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.FieldPacker;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends BaseActivity implements OnClickListener {
	@ViewInject(id = R.id.et_input_message)
	private EditText et_input;
	@ViewInject(id = R.id.btn_send_message)
	private Button btn_send;
	@ViewInject(id = R.id.lv_chat_message)
	private PullToRefreshListView lv_message;
	@ViewInject(id = R.id.iv_add_face)
	private ImageView iv_addface;
	@ViewInject(id = R.id.fv_facelist)
	private FaceView fv_face;
	@ViewInject(id = R.id.tv_chat_back)
	private TextView tv_back;
	private ConnectInfo connect = null;

	private Uri inserUri = null;
	private ContentResolver resolver = null;
	private ChatMessageAdapter adapter = null;
	// 广播接收器
	private ChatReceiver receiver = null;
	// 头像
	private Bitmap selfimg = null;
	private Bitmap connectimg = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.chat_layout);
		connect = (ConnectInfo) getIntent().getSerializableExtra("info");
		ActivityInject.getInstance().setInject(this);
		adapter = new ChatMessageAdapter(getApplication(), null);
		receiver = new ChatReceiver(adapter);
		IntentFilter filter = new IntentFilter();
		filter.addAction(RecevierConst.Chat_One_Get);
		registerReceiver(receiver, filter);
		// 绑定服务
		bindService();
		init();
		inserUri = Uri.parse("content://org.hwant.im.chat/chat");
		resolver = getContentResolver();
	}

	private void init() {
		btn_send.setOnClickListener(this);
		iv_addface.setOnClickListener(this);
		tv_back.setOnClickListener(this);
		// 设置下拉时的加载
		lv_message.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                ChatMessage lastmsg=adapter.getLastObj();
                PreChatRecord record=null;
                if(lastmsg==null){
                	Date date=new Date();
                	record=new PreChatRecord(ChatActivity.this,application.user.getJid(),connect.getJid(),String.valueOf(date.getTime()));
                }else{
                	record=new PreChatRecord(ChatActivity.this,application.user.getJid(),connect.getJid(),lastmsg.getTime());
                }
                service.manager.addTask(record);
			}
		});
		lv_message.setAdapter(adapter);

		if (application.user.getUserimg() != null) {
			File file1 = new File(Environment.getExternalStorageDirectory()
					.toString() + Common.Path_Image,
					application.user.getUserimg());
			if (file1.isFile() && file1.exists()) {
				selfimg = BitmapFactory.decodeFile(file1.getAbsolutePath());
			}
		}
		if (connect.getUserimg() != null) {
			File file2 = new File(Environment.getExternalStorageDirectory()
					.toString() + Common.Path_Image, connect.getUserimg());
			if (file2.isFile() && file2.exists()) {
				connectimg = BitmapFactory.decodeFile(file2.getAbsolutePath());
			}
		}
		adapter.setChatImg(selfimg, connectimg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send_message:
			String message = et_input.getText().toString();
			if (StringHelper.isEmpty(message)) {
				Toast.makeText(this, "内容不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			service.manager.addTask(new SendMessage());
			break;
		case R.id.iv_add_face:
			fv_face.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_chat_back:
			finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}

	class SendMessage implements IDoWork {
		@Override
		public Object doWhat() {
			boolean flag = service.getAsmack().sendMessage(
					application.user.getJid(), connect.getJid(),
					et_input.getText().toString());
			return flag;
		}

		@Override
		public void Finish2Do(Object obj) {
			//
			Boolean flag = (Boolean) obj;
			ContentValues values = new ContentValues();
			// if(flag){
			// Toast.makeText(ChatActivity.this,"发送:"+flag,Toast.LENGTH_SHORT).show();
			values.put("mfrom", application.user.getJid());
			values.put("mto", connect.getJid());
			values.put("message", et_input.getText().toString());
			values.put("read", "1");
			values.put("user", application.user.getJid());
			Date date = new Date();
			values.put("time", String.valueOf(date.getTime()));
			resolver.insert(inserUri, values);
			ChatMessage message = new ChatMessage();
			message.setMfrom(application.user.getJid());
			message.setMessage(et_input.getText().toString());
			message.setInfo(application.user);
			adapter.addMessage(message);
			// }
		}
	}

	class PreChatRecord implements IDoWork {
		private ContentResolver resolver = null;
		private String login = "";
		private String chatto = "";
		private String lasttime = "";
		private WeakReference<Activity> weak = null;

		/**
		 * 获取当前用户(多用户登陆)的聊天信息 ，防止不同用户出现混乱
		 * 
		 * @param user登陆用户
		 * @param chatto当前用户的联系人
		 * @param lasttime最后一条记录的时间
		 */
		public PreChatRecord(Activity activity, String login, String chatto,
				String lasttime) {
			this.login = login;
			this.chatto = chatto;
			this.lasttime = lasttime;
			weak = new WeakReference<Activity>(activity);
		}

		@Override
		public Object doWhat() {
			ArrayList<ChatMessage> list = new ArrayList<ChatMessage>();
			// 防止当前activity被销毁
			resolver = getApplicationContext().getContentResolver();
			Uri uri = Uri.parse("content://org.hwant.im.chat/chat");
			Cursor cursor = resolver.query(uri, null,
					"  user=? and time<? and ( mfrom=? or mto=?)  ",
					new String[] { login, lasttime, chatto, chatto },
					" order by time asc ");
			cursor.moveToFirst();
			while (cursor.isAfterLast()) {
				ChatMessage message = new ChatMessage();
				message.setMfrom(cursor.getString(cursor
						.getColumnIndex("mfrom")));
				message.setMessage(cursor.getString(cursor
						.getColumnIndex("message")));
				// message.setInfo(application.user);
				list.add(message);
				cursor.moveToNext();
			}
			cursor.close();
			return list;
		}

		@Override
		public void Finish2Do(Object obj) {
            
		}

	}
}
