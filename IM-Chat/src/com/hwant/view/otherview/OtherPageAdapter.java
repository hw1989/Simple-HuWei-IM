package com.hwant.view.otherview;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class OtherPageAdapter extends PagerAdapter {
	private ArrayList<View> list = null;

	public OtherPageAdapter(ArrayList<View> list) {
		if (list == null) {
			this.list = new ArrayList<View>();
		} else {
			this.list = list;
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = list.get(position);
		container.removeView(view);

	}

	@Override
	public int getItemPosition(Object object) {
		return list.indexOf(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = list.get(position);
		container.addView(view);
		return view;
	}

}
