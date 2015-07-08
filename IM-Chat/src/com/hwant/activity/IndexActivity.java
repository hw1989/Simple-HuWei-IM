package com.hwant.activity;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import com.hwant.fragment.ConnectFragment;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class IndexActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {
	private ConnectFragment friend = null;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index_layout);
		ActivityInject.getInstance().setInject(this);
		friend = new ConnectFragment();
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
		cb_connect.setOnCheckedChangeListener(this);
		cb_dynamic.setOnCheckedChangeListener(this);
		cb_mess.setOnCheckedChangeListener(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return menu.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {
		// switch (v.getId()) {
		// case R.id.ib_index_mess:
		// break;
		// case R.id.ib_index_connect:
		// break;
		// }
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			switch(buttonView.getId()){
			case R.id.cb_index_connect:
				cb_dynamic.setChecked(false);
				cb_mess.setChecked(false);
				break;
			case R.id.cb_index_dynamic:
				cb_connect.setChecked(false);
				cb_mess.setChecked(false);
				break;
			case R.id.cb_index_mess:
				cb_dynamic.setChecked(false);
				cb_connect.setChecked(false);
				break;
			}
		}
	}

}
