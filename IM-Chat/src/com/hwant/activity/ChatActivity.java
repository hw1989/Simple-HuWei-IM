package com.hwant.activity;

import java.util.Date;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.Field;
import org.wind.annotation.ViewInject;
import org.wind.util.StringHelper;

import com.hwant.adapter.ChatMessageAdapter;
import com.hwant.broadcast.ChatReceiver;
import com.hwant.common.RecevierConst;
import com.hwant.entity.ChatMessage;
import com.hwant.entity.ConnectInfo;
import com.hwant.entity.UserInfo;
import com.hwant.services.IDoWork;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.FieldPacker;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ChatActivity extends BaseActivity implements OnClickListener {
	@ViewInject(id = R.id.et_input_message)
	private EditText et_input;
	@ViewInject(id = R.id.btn_send_message)
	private Button btn_send;
	@ViewInject(id=R.id.lv_chat_message)
	private ListView lv_message;
	private ConnectInfo connect = null;
	
    private Uri inserUri=null;
    private ContentResolver resolver=null;
    private ChatMessageAdapter adapter=null;
    //广播接收器
    private ChatReceiver receiver=null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.chat_layout);
		connect = (ConnectInfo) getIntent().getSerializableExtra("info");
		ActivityInject.getInstance().setInject(this);
		adapter=new ChatMessageAdapter(getApplication(), null);
		receiver=new ChatReceiver(adapter);
		IntentFilter filter=new IntentFilter();
		filter.addAction(RecevierConst.Chat_One_Get);
		registerReceiver(receiver, filter);
		// 绑定服务
		bindService();
		init();
		inserUri=Uri.parse("content://org.hwant.im.chat/chat");
		resolver=getContentResolver();
	}

	private void init() {
		btn_send.setOnClickListener(this);
		lv_message.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send_message:
			String message=et_input.getText().toString();
			if(StringHelper.isEmpty(message)){
				Toast.makeText(this,"内容不能为空!",Toast.LENGTH_SHORT).show();
				return;
			}
			service.manager.addTask(new SendMessage());
			break;
		}
	}
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if(receiver!=null){
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
			ContentValues values=new ContentValues();
//			if(flag){
//			Toast.makeText(ChatActivity.this,"发送:"+flag,Toast.LENGTH_SHORT).show();
				values.put("mfrom", application.user.getJid());
				values.put("mto", connect.getJid());
				values.put("message", et_input.getText().toString());
				values.put("read", "1");
				values.put("user",application.user.getJid());
				Date date=new Date();
				values.put("time",String.valueOf(date.getTime()));
				resolver.insert(inserUri, values);
				ChatMessage message=new ChatMessage();
				message.setMfrom(application.user.getJid());
				message.setMessage(et_input.getText().toString());
				message.setInfo(application.user);
				adapter.addMessage(message);
//			}
		}
	}
	
}
