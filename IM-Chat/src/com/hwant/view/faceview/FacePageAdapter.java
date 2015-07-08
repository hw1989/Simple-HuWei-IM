package com.hwant.view.faceview;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class FacePageAdapter extends PagerAdapter {
	private ArrayList<View> views=null;
    public FacePageAdapter(ArrayList<View> views){
    	if(views!=null){
    		this.views=views;
    	}else{
    		this.views=new ArrayList<View>();
    	}
    }
    
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view=views.get(position);
		container.removeView(view);
	}

	@Override
	public int getItemPosition(Object object) {
		return views.indexOf(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view=views.get(position);
		container.addView(view);
		return view;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

}
