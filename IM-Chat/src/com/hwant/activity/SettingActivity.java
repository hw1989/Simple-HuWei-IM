package com.hwant.activity;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.Field;
import org.wind.annotation.ViewInject;
import org.wind.util.StringHelper;

import com.hwant.activity.R.id;
import com.hwant.broadcast.ReceiverListener;
import com.hwant.broadcast.UserInfoReceiver;
import com.hwant.common.RecevierConst;
import com.hwant.utils.DialogUtils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends BaseActivity implements OnClickListener,
		ReceiverListener {
	@ViewInject(id = R.id.rl_change_img)
	private RelativeLayout rl_changeimg = null;
	@ViewInject(id = R.id.iv_userinfo_icon)
	private ImageView iv_userinfo_icon = null;
	@ViewInject(id = R.id.tv_setting_back)
	private TextView tv_back;
	@ViewInject(id = R.id.tv_setting_chat_clear)
	private TextView tv_clear;
	private Intent intent = null;
	// 设置用户信息改变的广播接受器
	private UserInfoReceiver receiver = null;
	private ContentResolver resolver = null;
	private DialogUtils dialog = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.setting_layout);
		ActivityInject.getInstance().setInject(this);
		init();
		resolver = getContentResolver();
		// 初始化对话框
		dialog = new DialogUtils(this);

	}

	private void init() {
		tv_back.setOnClickListener(this);
		rl_changeimg.setOnClickListener(this);
		tv_clear.setOnClickListener(this);
		receiver = new UserInfoReceiver(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(RecevierConst.User_Info_Icon);
		registerReceiver(receiver, filter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_change_img:
			intent = new Intent(this, SelectImgActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_setting_back:
			finish();
			break;
		case R.id.tv_setting_chat_clear:
			dialog.setLayoutID(R.layout.chat_delete_layout);
			// 设置对话框的点击事件
			dialog.setViewClick(this, R.id.tv_chat_delete_sure,
					R.id.tv_chat_delete_cancel);
			dialog.show();
			break;
		case R.id.tv_chat_delete_sure:
			Uri uri = Uri.parse("content://org.hwant.im.chat/chat");
			resolver.delete(uri, null, null);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			Toast.makeText(this, "数据删除成功!", Toast.LENGTH_SHORT).show();
			break;
		case R.id.tv_chat_delete_cancel:
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
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
	}

	@Override
	public void onReceive(Intent intent) {
		if (RecevierConst.User_Info_Icon.equals(intent.getAction())) {
			String filename = intent.getStringExtra("value");
			if (!StringHelper.isEmpty(filename)) {
				// 设置application里的设置
				application.user.setUserimg(filename);
				// 修改登录用户数据库
				Uri uri = Uri.parse("content://com.hwant.im.login/user");
				ContentValues values = new ContentValues();
				values.put("userimg", filename);
				resolver.update(uri, values, " jid=? ",
						new String[] { application.user.getJid() });
				Bitmap bitmap = BitmapFactory.decodeFile(filename);
				iv_userinfo_icon.setImageBitmap(bitmap);
			}
		}
	}

}
