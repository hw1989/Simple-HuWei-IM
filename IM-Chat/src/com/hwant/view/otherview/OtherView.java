package com.hwant.view.otherview;

import java.util.ArrayList;
import java.util.HashMap;

import com.hwant.activity.R;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.LinearLayout.LayoutParams;

public class OtherView extends LinearLayout implements OnPageChangeListener,
		OnItemClickListener {
	// private ArrayList<String> faces = new ArrayList<String>();
	private SparseIntArray otherArray = null;
	private ViewPager vp_face = null;
	private View pageview = null;
	private int pages = 0;
	private int pagecount = 4 * 2;
	private View facelist = null;
	private OtherListAdapter listadapter = null;
	// 页面的索引
	private RadioGroup rg_index = null;
	// 设置item点击的监听
	private IOtherListItemListener otherListItemListener = null;

	

	public void setOtherListItemListener(
			IOtherListItemListener otherListItemListener) {
		this.otherListItemListener = otherListItemListener;
	}

	public OtherView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public OtherView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public OtherView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		otherArray = new SparseIntArray();
		otherArray.put(R.drawable.grb, R.string.more_videophone);
		otherArray.put(R.drawable.grc, R.string.more_file);
		otherArray.put(R.drawable.gre, R.string.more_tip);
		otherArray.put(R.drawable.gri, R.string.more_music);
		otherArray.put(R.drawable.grk, R.string.more_location);
		otherArray.put(R.drawable.grn, R.string.more_camera);
		otherArray.put(R.drawable.iaj, R.string.more_pgone);
		otherArray.put(R.drawable.ial, R.string.more_photo);
		otherArray.put(R.drawable.iam, R.string.more_shake);
		// otherArray=new SparseArray<String>();
		// otherArray.put(R.drawable.grb,"视频通话");
		// otherArray.put(R.drawable.grc,"文件");
		// otherArray.put(R.drawable.gre,"提醒");
		// otherArray.put(R.drawable.gri,"音乐");
		// otherArray.put(R.drawable.grk,"位置");
		// otherArray.put(R.drawable.grn,"拍照");
		// otherArray.put(R.drawable.iaj,"IM电话");
		// otherArray.put(R.drawable.ial,"照片");
		// otherArray.put(R.drawable.iam,"抖一抖");
		LayoutInflater inflater = LayoutInflater.from(context);
		pageview = inflater.inflate(R.layout.otherview_layout, null, false);
		rg_index = (RadioGroup) pageview.findViewById(R.id.rg_otherpage_index);
		vp_face = (ViewPager) pageview.findViewById(R.id.vp_chat_otherview);
		pages = otherArray.size() % pagecount == 0 ? otherArray.size()
				/ pagecount : otherArray.size() / pagecount + 1;
		ArrayList<View> views = new ArrayList<View>();
		LayoutParams params = new LayoutParams(30, 30);
		params.setMargins(20, 0, 20, 0);
		for (int i = 0; i < pages; i++) {
			RadioButton rb_page = new RadioButton(context);
			rb_page.setButtonDrawable(android.R.color.transparent);
			rb_page.setBackgroundResource(R.drawable.dot_page_select);
			rb_page.setLayoutParams(params);
			rg_index.addView(rb_page, params);
			facelist = inflater.inflate(R.layout.others_list_layout, null,
					false);
			GridView gv_face = (GridView) facelist.findViewById(R.id.gv_others);
			listadapter = new OtherListAdapter(context, otherArray, i);
			// 设置item被点击
			gv_face.setOnItemClickListener(this);
			gv_face.setAdapter(listadapter);
			views.add(facelist);
		}
		OtherPageAdapter pageadapter = new OtherPageAdapter(views);
		vp_face.setAdapter(pageadapter);
		vp_face.setOnPageChangeListener(this);
		vp_face.setCurrentItem(0);
		setDotSelect(0);
		this.addView(pageview, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
	}

	private ArrayList<String> getList(ArrayList<String> list, int start, int end) {
		ArrayList<String> cache = new ArrayList<String>();
		for (int i = start; i < list.size() && i <= end; i++) {
			cache.add(list.get(i));
		}
		return cache;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		setDotSelect(arg0);
	}

	private void setDotSelect(int index) {
		RadioButton rb_select = (RadioButton) rg_index.getChildAt(index);
		rb_select.setChecked(true);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (otherListItemListener != null) {
			otherListItemListener.OVitemclick(vp_face.getCurrentItem(), position);
		}

	}
}
