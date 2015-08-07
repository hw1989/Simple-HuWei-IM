package com.hwant.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwant.activity.R;
import com.hwant.entity.Relationship;
import com.hwant.swipe.adapter.BaseSwipeAdapter;

public class ManageConnectAdapter extends BaseSwipeAdapter {
	private ArrayList<Relationship> list = null;
	private LayoutInflater inflater = null;

	public ManageConnectAdapter(Context context, ArrayList<Relationship> list) {
		this.inflater = LayoutInflater.from(context);
		if (list == null) {
			this.list = new ArrayList<Relationship>();
		} else {
			this.list = list;
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Relationship getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getSwipeLayoutResourceId(int position) {
		return R.id.sl_manage_item;
	}

	@Override
	public View generateView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.new_connect_item_layout, null,false);

		return view;
	}

	public void addData(ArrayList<Relationship> data) {
		if (data != null) {
			if (data.size() > 0) {
				list.addAll(data);
				notifyDataSetChanged();
			}
		}
	}

	@Override
	public void fillValues(int position, View convertView) {

	}
}
