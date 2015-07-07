package com.hwant.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.wind.adapter.ViewHolder;

import com.hwant.activity.R;
import com.hwant.entity.ConnectInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ConnectAdapter extends BaseExpandableListAdapter {
	// private HashMap<String, ArrayList<FriendInfo>> map = null;
	private ViewHolder pholder = null;
	private ViewHolder cholder = null;
	private LayoutInflater inflater = null;
	private ArrayList<String> group = null;
	private HashMap<Integer, ArrayList<ConnectInfo>> map = null;

	public ConnectAdapter(Context context, ArrayList<ConnectInfo> connectInfos) {
		pholder = new ViewHolder();
		cholder = new ViewHolder();
		this.group = new ArrayList<String>();
		this.map = new HashMap<Integer, ArrayList<ConnectInfo>>();
		int index=0;
		if (connectInfos != null && connectInfos.size() > 0) {
			for (ConnectInfo info : connectInfos) {
               if(!group.contains(info.getGroup())){
            	   group.add(info.getGroup());
               }
               index=group.indexOf(info.getGroup());
               if(!this.map.containsKey(index)){
            	   this.map.put(index,new ArrayList<ConnectInfo>());
               }
               this.map.get(index).add(info);
			}
		}
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getGroupCount() {
		return this.group.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int count = 0;
		for (int i = 0; i < getGroupCount(); i++) {
			ArrayList<ConnectInfo> list = map.get(i);
			if (list != null) {
				count += list.size();
			}
		}
		return count;
	}

	@Override
	public String getGroup(int groupPosition) {
		return group.get(groupPosition);
	}

	@Override
	public ConnectInfo getChild(int groupPosition, int childPosition) {
		ArrayList<ConnectInfo> list = map.get(groupPosition);
		return list.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		int count = 0;
		for (int i = 0; i < groupPosition; i++) {
			ArrayList<ConnectInfo> list = map.get(groupPosition);
			if (list != null) {
				count += list.size();
			}
		}
		count += childPosition;
		return count;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.connect_list_pitem_layout,
					parent, false);
		}
		TextView tv_nickname = (TextView) pholder.getView(convertView,
				R.id.connect_list_pitem_groupname);
		tv_nickname.setText(group.get(groupPosition));
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.connect_list_citem_layout,
					parent, false);
		}
		TextView tv_nickname = (TextView) pholder.getView(convertView,
				R.id.connect_list_citem_nickname);
		tv_nickname.setText(group.get(groupPosition));
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
