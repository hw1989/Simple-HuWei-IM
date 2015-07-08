package com.hwant.view.faceview;

import java.util.ArrayList;

import org.wind.adapter.ViewHolder;
import org.wind.util.StringHelper;

import com.hwant.activity.R;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FaceListAdapter extends BaseAdapter {
	private ArrayList<String> list = null;
    private LayoutInflater inflater=null;
    private String str="";
    private Resources resources=null;
    //
    private String packname="";
	public FaceListAdapter(Context context, ArrayList<String> list) {
		inflater=LayoutInflater.from(context);
		this.resources=context.getResources();
		packname=context.getPackageName();
		if (list == null) {
			this.list = new ArrayList<String>();
		} else {
			this.list = list;
		}
	}

	@Override
	public int getCount() {

		return 21;
	}

	@Override
	public String getItem(int position) {
		if(position<=list.size()-1){
			return list.get(position);
		}else{
			return "";
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=inflater.inflate(R.layout.face_gvitem_layout, parent, false);
		}
		ImageView iv_face=(ImageView)ViewHolder.getInstance().getView(convertView, R.id.iv_gv_face);
		str=getItem(position);
		if(position==20){
			iv_face.setImageResource(R.drawable.goi);
		}else{
			if(StringHelper.isEmpty(str)){
				iv_face.setImageBitmap(null);
			}else{
				int id=resources.getIdentifier(str,"drawable", packname);
				iv_face.setImageResource(id);
			}
		}
		return convertView;
	}

}
