package com.hwant.activity;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import com.hwant.fragment.FriendFragment;
import com.hwant.fragment.SettingFragment;
import com.hwant.services.TaskManager;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenu.OnMenuListener;
import com.special.ResideMenu.ResideMenuItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class IndexActivity extends BaseActivity implements OnClickListener {
	private FriendFragment friend = null;
	private ResideMenu menu = null;
	private ResideMenuItem item_setting;
	// 聊天
	@ViewInject(id = R.id.rbt_index_mess)
	private RadioButton rbtn_mess;
	// 联系人
	@ViewInject(id = R.id.rbt_index_connect)
	private RadioButton rbtn_connect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index_layout);
		ActivityInject.getInstance().setInject(this);
		friend = new FriendFragment();
		init();
		if (savedInstanceState == null)
			setMenuFragment(friend);

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
		rbtn_mess.setOnClickListener(this);
		rbtn_connect.setOnClickListener(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return menu.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rbt_index_mess:
			break;
		case R.id.rbt_index_connect:
			break;
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
	}
}
