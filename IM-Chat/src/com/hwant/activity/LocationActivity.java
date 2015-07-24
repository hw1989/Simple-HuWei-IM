package com.hwant.activity;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import com.baidu.mapapi.map.MapView;

import android.os.Bundle;

public class LocationActivity extends BaseActivity {
	@ViewInject(id = R.id.mv_location)
	private MapView mv_location;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.location_layout);
		ActivityInject.getInstance().setInject(this);
	}
}
