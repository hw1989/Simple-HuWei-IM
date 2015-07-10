package com.hwant.activity;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.Field;
import org.wind.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class SettingActivity extends BaseActivity implements OnClickListener{
	@ViewInject(id=R.id.rl_change_img)
	private RelativeLayout rl_changeimg=null;
	private Intent intent=null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.setting_layout);
		ActivityInject.getInstance().setInject(this);
		init();
	}
	private void init(){
		rl_changeimg.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.rl_change_img:
			intent=new Intent(this,SelectImgActivity.class);
			startActivity(intent);
			break;
		}
		
	}
}
