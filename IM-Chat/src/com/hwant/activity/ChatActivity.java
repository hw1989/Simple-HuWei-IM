package com.hwant.activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;
import org.wind.media.MediaComm;
import org.wind.media.VoiceManager;
import org.wind.util.StringHelper;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.hwant.adapter.ChatMessageAdapter;
import com.hwant.broadcast.ChatReceiver;
import com.hwant.common.Common;
import com.hwant.common.MapCommon;
import com.hwant.common.RecevierConst;
import com.hwant.entity.ChatMessage;
import com.hwant.entity.ConnectInfo;
import com.hwant.entity.ContentEntity;
import com.hwant.pulltorefresh.PullToRefreshBase;
import com.hwant.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.hwant.pulltorefresh.PullToRefreshListView;
import com.hwant.services.IDoWork;
import com.hwant.services.TaskManager;
import com.hwant.utils.MessageUtils;
import com.hwant.view.faceview.FaceView;
import com.hwant.view.faceview.FaceViewListener;
import com.hwant.view.otherview.IOtherListItemListener;
import com.hwant.view.otherview.OtherView;

import android.R.integer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends BaseActivity implements OnClickListener,
		IOtherListItemListener, OnCheckedChangeListener, OnTouchListener {
	@ViewInject(id = R.id.et_input_message)
	private EditText et_input;
	@ViewInject(id = R.id.btn_send_message)
	private Button btn_send;
	@ViewInject(id = R.id.lv_chat_message)
	private PullToRefreshListView lv_message;
	@ViewInject(id = R.id.iv_add_face)
	private ImageView iv_addface;
	// 添加更多
	@ViewInject(id = R.id.iv_chat_more)
	private ImageView iv_other;
	// 表情控件
	@ViewInject(id = R.id.fv_facelist)
	private FaceView fv_face;
	// 其他功能控件
	@ViewInject(id = R.id.ov_otherslist)
	private OtherView ov_other;
	@ViewInject(id = R.id.tv_chat_back)
	private TextView tv_back;
	// 发送内容的类型
	@ViewInject(id = R.id.cb_msg_type)
	private CheckBox cb_msgtype;
	// 输入型的
	@ViewInject(id = R.id.ll_msg_input)
	private LinearLayout ll_input;
	// 长按录音的按键
	@ViewInject(id = R.id.btn_msg_voice)
	private Button btn_voice;
	// 录音的动画
	@ViewInject(id = R.id.ll_voice_anim)
	private LinearLayout ll_voice;
	private ConnectInfo connect = null;

	private Uri inserUri = null;
	private ContentResolver resolver = null;
	private ChatMessageAdapter adapter = null;
	// 广播接收器 可以使用 ContentObserver替代
	private ChatReceiver receiver = null;
	// 头像
	private Bitmap selfimg = null;
	private Bitmap connectimg = null;
	private InputMethodManager manager = null;
	// 定位信息
	private LocationClient client;
	private Handler handler = null;
	// 产生时间的文件名
	private String filename = "";
	private SimpleDateFormat format = null;
	// 文件转换
	private FileTransferManager ftmanager = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.chat_layout);
		connect = (ConnectInfo) getIntent().getSerializableExtra("info");
		ActivityInject.getInstance().setInject(this);
		adapter = new ChatMessageAdapter(getApplication(), null);
		receiver = new ChatReceiver(adapter, application.user.getJid(),
				connect.getJid());
		IntentFilter filter = new IntentFilter();
		filter.addAction(RecevierConst.Chat_One_Get);
		filter.addAction(RecevierConst.Chat_DB_Get);
		registerReceiver(receiver, filter);
		// 绑定服务
		bindService();
		init();
		inserUri = Uri.parse("content://org.hwant.im.chat/chat");
		resolver = getContentResolver();

		// ContentProvider provider=null;
		// resolver.registerContentObserver(uri, notifyForDescendents, observer)
	}

	private void init() {
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 设置其他功能的item点击监听
		ov_other.setOtherListItemListener(this);
		btn_send.setOnClickListener(this);
		iv_addface.setOnClickListener(this);
		tv_back.setOnClickListener(this);
		iv_other.setOnClickListener(this);
		fv_face.setListener(new FaceItemClick());
		// 设置选择状态发生改变的监听
		cb_msgtype.setOnCheckedChangeListener(this);
		// 开始录音
		btn_voice.setOnTouchListener(this);
		// 输入文本是显示发送的按钮
		et_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if ("".equals(s)) {
					btn_send.setVisibility(View.GONE);
					cb_msgtype.setVisibility(View.VISIBLE);
				} else {
					btn_send.setVisibility(View.VISIBLE);
					cb_msgtype.setVisibility(View.GONE);
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
		// 初始化地位信息
		client = application.mLocationClient;
		// 设置下拉时的加载
		lv_message.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				ChatMessage lastmsg = adapter.getFristItem();
				PreChatRecord record = null;
				if (lastmsg == null) {
					Date date = new Date();
					record = new PreChatRecord(ChatActivity.this,
							application.user.getJid(), connect.getJid(), String
									.valueOf(date.getTime()));
				} else {
					record = new PreChatRecord(ChatActivity.this,
							application.user.getJid(), connect.getJid(),
							lastmsg.getTime());
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
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == MediaComm.What_Voice_Finish) {
					// // 将音频文件转为字符串
					// byte[] buffere=FileUtils.file2ByteArray(filename);
					// //文件转换后的字符串
					// String file2str=StringHelper.byteArray2Base64(buffere);
					// service.manager.addTask(new
					// SendMessage(MessageUtils.setVoice(file2str)));
					// 确认连接正常
					service.manager.addTask(new TransferFile(filename));
				}
			}
		};
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
			service.manager.addTask(new SendMessage(message));
			break;
		case R.id.iv_add_face:
			// et_input.clearFocus();
			manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			ov_other.setVisibility(View.GONE);
			if (fv_face.getVisibility() == View.GONE) {
				fv_face.setVisibility(fv_face.VISIBLE);
			} else {
				fv_face.setVisibility(fv_face.GONE);
			}
			break;
		case R.id.tv_chat_back:
			finish();
			break;
		case R.id.iv_chat_more:
			manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			fv_face.setVisibility(View.GONE);
			if (ov_other.getVisibility() == View.GONE) {
				ov_other.setVisibility(fv_face.VISIBLE);
			} else {
				ov_other.setVisibility(fv_face.GONE);
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		client.stop();
	}

	class SendMessage implements IDoWork {
		private String content = "";

		public SendMessage(String content) {
			this.content = content;
		}

		@Override
		public Object doWhat() {
			// boolean flag = service.getAsmack().sendMessage(
			// application.user.getJid(), connect.getJid(),
			// et_input.getText().toString());
			boolean flag = service.getAsmack().sendMessage(
					application.user.getJid(), connect.getJid(), content);
			return flag;
		}

		@Override
		public void Finish2Do(Object obj) {
			//
			Boolean flag = (Boolean) obj;
			ContentValues values = new ContentValues();
			// if(flag){
			values.put("mfrom", application.user.getJid());
			values.put("mto", connect.getJid());
			// values.put("message", et_input.getText().toString());
			values.put("message", content);
			values.put("read", "1");
			values.put("user", application.user.getJid());
			Date date = new Date();
			String time = String.valueOf(date.getTime());
			values.put("time", time);
			resolver.insert(inserUri, values);
			ChatMessage message = new ChatMessage();
			message.setMfrom(application.user.getJid());
			// message.setMessage(et_input.getText().toString());
			message.setMessage(content);
			message.setTime(time);
			message.setInfo(application.user);
			// adapter.addMessage(message);
			adapter.addItem(message, adapter.getCount());
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
					"  time desc limit 0,10");
			// 显示的结果与查询的结果顺序相反
			cursor.moveToLast();
			while (!cursor.isBeforeFirst()) {
				ChatMessage message = new ChatMessage();
				message.setMfrom(cursor.getString(cursor
						.getColumnIndex("mfrom")));
				message.setMessage(cursor.getString(cursor
						.getColumnIndex("message")));
				message.setTime(cursor.getString(cursor.getColumnIndex("time")));
				list.add(message);
				cursor.moveToPrevious();
			}
			cursor.close();
			return list;
		}

		@Override
		public void Finish2Do(Object obj) {
			if (weak != null) {
				if (weak.get() != null) {
					lv_message.onRefreshComplete();
					if (obj != null) {
						ArrayList<ChatMessage> list = (ArrayList<ChatMessage>) obj;
						if (list.size() > 0) {
							// adapter.addMessage(list);
							adapter.addItems(list, 0);
						}
					}
				}
			}
		}

	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType(MapCommon.TYPE_Loc_Gcj);
		int span = 1000;
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		client.setLocOption(option);
	}

	@Override
	public void OVitemclick(int page, int position) {
		if (page == 0) {
			if (position == 4) {
				client.start();
				// 定位，发送位置
				initLocation();
				if (application.getBdLocation() == null) {
					Toast.makeText(this, "获取位置失败!", Toast.LENGTH_SHORT).show();
					return;
				}
				service.manager.addTask(new SendMessage(MessageUtils
						.setLocation(application.getBdLocation())));
				client.stop();
			}else if(position==7){
				Intent intent=new Intent(this,PickImgActivity.class);
				startActivity(intent);
			}
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cb_msg_type:
			if (isChecked) {
				// 显示语音的界面
				ll_input.setVisibility(View.GONE);
				btn_voice.setVisibility(View.VISIBLE);
			} else {
				// 显示文本的界面
				ll_input.setVisibility(View.VISIBLE);
				btn_voice.setVisibility(View.GONE);
			}
			break;
		}
	}

	private VoiceManager voiceManager = null;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			ll_voice.setVisibility(View.VISIBLE);
			createPath();
			filename = Environment.getExternalStorageDirectory()
					+ Common.Path_Media + format.format(new Date()) + ".3gp";
			voiceManager = new VoiceManager(filename, handler);
			voiceManager.startRecord();
			break;
		case MotionEvent.ACTION_UP:
			ll_voice.setVisibility(View.GONE);
			if (voiceManager != null) {
				voiceManager.stopRecord();
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			ll_voice.setVisibility(View.GONE);
			if (voiceManager != null) {
				voiceManager.stopRecord();
			}
			break;
		}
		return true;
	}

	class TransferFile implements IDoWork {
		private String filepath;

		public TransferFile(String filepath) {
			this.filepath = filepath;
		}

		@Override
		public Object doWhat() {
			if (service.getConnection().isConnected()
					&& service.getConnection().isAuthenticated()) {
				ftmanager = new FileTransferManager(service.getConnection());
				OutgoingFileTransfer transfer = ftmanager
						.createOutgoingFileTransfer(connect.getJid() + "/Smack");
				File file = new File(this.filepath);
				try {
					transfer.sendFile(file, "huwei");
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		public void Finish2Do(Object obj) {
		}

	}

	@Override
	public void bindFinished(TaskManager manager) {
		service.manager.addTask(new PreChatRecord(this, application.user
				.getJid(), connect.getJid(), String.valueOf(new Date()
				.getTime())));
	}

	class FaceItemClick implements FaceViewListener {
		@Override
		public void onFaceItemClick(int position, String content) {
			if (position != 20) {
				String faceStr = MessageUtils.setFace(content);
				
				ContentEntity entity = new ContentEntity();
				entity.setType(MessageUtils.TYPE_FACE);
				entity.setMessage(content);
				entity.setStart(0);
				entity.setEnd(faceStr.length());
				SpannableStringBuilder builder = MessageUtils.getFaceContent(
						ChatActivity.this, faceStr, entity);
				et_input.append(builder);
				// et_input.setText(builder);
			} else {
				if (et_input.getText().length() > 0) {
					int posi = getEditCursor(et_input);
					et_input.getText().delete(posi - 1, posi);
				}
			}
		}
	}

	// 获取光标的位置
	public int getEditCursor(EditText editText) {
		return editText.getSelectionStart();
	}
}
