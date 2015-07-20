package com.hwant.activity;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import com.hwant.fragment.ConnectFragment;
import com.hwant.fragment.MessageFragment;
import com.hwant.services.IDoWork;
import com.hwant.services.TaskManager;
import com.hwant.utils.FileUtils;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenu.OnMenuListener;
import com.special.ResideMenu.ResideMenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class IndexActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {
	private ConnectFragment connect = null;
	private MessageFragment message = null;
	private ResideMenu menu = null;
	private ResideMenuItem item_setting;
	// 聊天
	@ViewInject(id = R.id.cb_index_mess)
	private CheckBox cb_mess;
	// 联系人
	@ViewInject(id = R.id.cb_index_connect)
	private CheckBox cb_connect;
	// 动态
	@ViewInject(id = R.id.cb_index_dynamic)
	private CheckBox cb_dynamic;
	@ViewInject(id = R.id.iv_index_icon)
	private ImageView iv_myicon;
	@ViewInject(id = R.id.tv_index_addconnect)
	private TextView tv_addconnect;
	private Intent intent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index_layout);
		ActivityInject.getInstance().setInject(this);
		connect = new ConnectFragment();
		message = new MessageFragment();
		init();
		if (savedInstanceState == null)
			setMenuFragment(connect);
		bindService();
		// 设置登陆用户的图片
		setImage();
	}

	private void init() {
		menu = new ResideMenu(this);
		menu.setBackground(R.drawable.menu_background);
		menu.attachToActivity(this);
		menu.setMenuListener(new MyMenuLister());
		menu.setScaleValue(0.6f);
		item_setting = new ResideMenuItem(this, R.drawable.ic_launcher, "设置");
		item_setting.setOnClickListener(this);
		menu.addMenuItem(item_setting, ResideMenu.DIRECTION_LEFT);
		// 禁止右滑
		menu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
		// 设置点击事件
		cb_connect.setOnCheckedChangeListener(this);
		cb_dynamic.setOnCheckedChangeListener(this);
		cb_mess.setOnCheckedChangeListener(this);
		iv_myicon.setOnClickListener(this);
		tv_addconnect.setOnClickListener(this);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return menu.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {
		if (v == item_setting) {
			intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
		} else {
			switch (v.getId()) {
			case R.id.iv_index_icon:
				menu.openMenu(ResideMenu.DIRECTION_LEFT);
				break;
			case R.id.tv_index_addconnect:
				intent = new Intent(this, SearchConnectActivity.class);
				startActivity(intent);
			}
		}
	}

	class MyMenuLister implements OnMenuListener {

		@Override
		public void openMenu() {

		}

		@Override
		public void closeMenu() {

		}

	}

	public void setMenuFragment(Fragment fragment) {
		menu.clearIgnoredViewList();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, fragment, "setting")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	@Override
	public void bindFinished(TaskManager manager) {
		// manager.addTask(friend.new GetFriend());
		connect.refreshAdapter(manager);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.cb_index_connect:
				setMenuFragment(connect);
				cb_dynamic.setChecked(false);
				cb_mess.setChecked(false);
				break;
			case R.id.cb_index_dynamic:
				cb_connect.setChecked(false);
				cb_mess.setChecked(false);
				break;
			case R.id.cb_index_mess:
				setMenuFragment(message);
				cb_dynamic.setChecked(false);
				cb_connect.setChecked(false);
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		// 释放绑定的service
		super.onDestroy();
	}

	/**
	 * 获取用户信息的任务
	 */
	class UserInfo implements IDoWork {

		@Override
		public Object doWhat() {
			return null;
		}

		@Override
		public void Finish2Do(Object obj) {

		}

	}

	/**
	 * 设置图片
	 */
	public void setImage() {
		String filename = application.user.getUserimg();
		if (FileUtils.isExistsImg(filename)) {
			iv_myicon.setImageBitmap(FileUtils.getImageBitemap(filename));
		} else {
			// 图片不存在，需要在服务器上获取
		}
	}
}
