package com.hwant.view.faceview;

import java.util.ArrayList;

import org.wind.util.StringHelper;

import com.hwant.activity.R;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FaceView extends LinearLayout implements OnPageChangeListener,
		OnItemClickListener {
	private ArrayList<String> faces = new ArrayList<String>();
	private ViewPager vp_face = null;
	private View pageview = null;
	private int pages = 0;
	private View facelist = null;
	private FaceListAdapter listadapter = null;
	// 页面的索引
	private RadioGroup rg_index = null;

	public FaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public FaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FaceView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		// { "ebg", "ebh", "ebj", "ebl", "ebn", "ebo", "ebp",
		// "ebq", "ebr", "ebs", "ebt", "ebu", "ebv", "ebw", "ebz", "eca",
		// "ecb", "ecc", "ecd", "ece", "ecf" };
		faces.add("ebg");
		faces.add("ebh");
		faces.add("ebj");
		faces.add("ebl");
		faces.add("ebn");
		faces.add("ebo");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebh");
		faces.add("ebj");
		faces.add("ebl");
		faces.add("ebn");
		faces.add("ebo");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		faces.add("ebg");
		LayoutInflater inflater = LayoutInflater.from(context);
		pageview = inflater.inflate(R.layout.face_layout, null, false);
		rg_index = (RadioGroup) pageview.findViewById(R.id.rg_facepage_index);
		vp_face = (ViewPager) pageview.findViewById(R.id.vp_chat_face);
		pages = faces.size() % 20 == 0 ? faces.size() / 20
				: faces.size() / 20 + 1;
		ArrayList<View> views = new ArrayList<View>();
		LayoutParams params = new LayoutParams(30, 30);
		params.setMargins(20, 0, 20, 0);
		for (int i = 0; i < pages; i++) {
			RadioButton rb_page = new RadioButton(context);
			// TextView tv=new TextView(context);
			rb_page.setButtonDrawable(android.R.color.transparent);

			rb_page.setBackgroundResource(R.drawable.dot_page_select);
			rb_page.setLayoutParams(params);
			// rb_page.setTextAppearance(getContext(),
			// R.style.rg_facepage_index);
			rg_index.addView(rb_page, params);
			facelist = inflater.inflate(R.layout.face_list_layout, null, false);
			GridView gv_face = (GridView) facelist.findViewById(R.id.gv_face);
			// 设置点击item的事件
			gv_face.setOnItemClickListener(this);
			int end = Math.min(20 * (i + 1) - 1, faces.size());
			listadapter = new FaceListAdapter(context, getList(faces, 20 * i,
					end));
			gv_face.setAdapter(listadapter);
			views.add(facelist);
		}
		FacePageAdapter pageadapter = new FacePageAdapter(views);
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

	private FaceViewListener listener = null;

	public void setListener(FaceViewListener listener) {
		this.listener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (listener != null) {
			FaceListAdapter adapter = (FaceListAdapter) parent.getAdapter();
			String str=adapter.getItem(position);
			if(StringHelper.isEmpty(str.trim())){
				listener.onFaceItemClick(position, "");
			}else{
				listener.onFaceItemClick(position,str);
			}
		}
	}
}
