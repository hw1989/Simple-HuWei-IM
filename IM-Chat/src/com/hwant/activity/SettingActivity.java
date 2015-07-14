package com.hwant.activity;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.Field;
import org.wind.annotation.ViewInject;
import org.wind.util.StringHelper;

import com.hwant.broadcast.ReceiverListener;
import com.hwant.broadcast.UserInfoReceiver;
import com.hwant.common.RecevierConst;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends BaseActivity implements OnClickListener,
		ReceiverListener {
	@ViewInject(id = R.id.rl_change_img)
	private RelativeLayout rl_changeimg = null;
	@ViewInject(id = R.id.iv_userinfo_icon)
	private ImageView iv_userinfo_icon = null;
	@ViewInject(id = R.id.tv_setting_back)
	private TextView tv_back;
	private Intent intent = null;
	// 设置用户信息改变的广播接受器
	private UserInfoReceiver receiver = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.setting_layout);
		ActivityInject.getInstance().setInject(this);
		init();
	}

	private void init() {
		tv_back.setOnClickListener(this);
		rl_changeimg.setOnClickListener(this);
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
				//设置application里的设置
				application.user.setUserimg(filename);
				Bitmap bitmap = BitmapFactory.decodeFile(filename);
				iv_userinfo_icon.setImageBitmap(bitmap);
			}
		}
	}

}
