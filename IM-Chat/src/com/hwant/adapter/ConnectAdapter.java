package com.hwant.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.wind.adapter.ViewHolder;
import org.wind.imageloader.ImageCache;
import org.wind.util.StringHelper;

import com.hwant.activity.R;
import com.hwant.entity.ConnectInfo;
import com.hwant.fragment.ConnectFragment;
import com.hwant.services.TaskManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConnectAdapter extends BaseExpandableListAdapter {
	private ViewHolder pholder = null;
	private ViewHolder cholder = null;
	private LayoutInflater inflater = null;
	private ArrayList<String> group = null;
	private HashMap<Integer, ArrayList<ConnectInfo>> map = null;
	private boolean isfling = false;
	private ConnectInfo connect = null;
	// 设置缓存
	private ImageCache cache = null;
	// 网络请求任务
	private TaskManager manager = null;
	private ConnectFragment fragment = null;

	public void setIsfling(boolean isfling) {
		this.isfling = isfling;
	}

	public ConnectAdapter(Context context, ArrayList<ConnectInfo> connectInfos,
			ConnectFragment fragment) {
		pholder = new ViewHolder();
		cholder = new ViewHolder();
		this.manager = manager;
		this.fragment = fragment;
		this.group = new ArrayList<String>();
		this.map = new HashMap<Integer, ArrayList<ConnectInfo>>();
		int index = 0;
		if (connectInfos != null && connectInfos.size() > 0) {
			for (ConnectInfo info : connectInfos) {
				if (!group.contains(info.getGroup())) {
					group.add(info.getGroup());
				}
				index = group.indexOf(info.getGroup());
				if (!this.map.containsKey(index)) {
					this.map.put(index, new ArrayList<ConnectInfo>());
				}
				this.map.get(index).add(info);
			}
		}
		inflater = LayoutInflater.from(context);
		cache = ImageCache.init();
	}

	public void setManager(TaskManager manager) {
		this.manager = manager;
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
		return true;
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
		ImageView iv_expand = (ImageView) pholder.getView(convertView,
				R.id.connect_list_pitem_expand);
		String groupname = group.get(groupPosition);
		if (StringHelper.isEmpty(groupname)) {
			tv_nickname.setText("我的好友");
		} else {
			tv_nickname.setText(String.valueOf(groupname));
		}
		if (isExpanded) {
			iv_expand.setImageResource(R.drawable.indicator_expanded);
		} else {
			iv_expand.setImageResource(R.drawable.indicator_unexpanded);
		}
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
		connect = map.get(groupPosition).get(childPosition);
		tv_nickname.setText(connect.getNickname());
		ImageView iv_image = (ImageView) pholder.getView(convertView,
				R.id.iv_connect_list_citem);
		iv_image.setTag(connect.getJid());
		Bitmap bitmap = null;
		// if (!StringHelper.isEmpty(connect.getUserimg())) {
		// if (cache.getCache().get(connect.getUserimg()) != null) {
		// bitmap = cache.getCache().get(connect.getUserimg());
		// iv_image.setImageBitmap(bitmap);
		// } else {
		// // 在非fling状态下，添加到网络请求
		// if (!isfling && manager != null) {
		// manager.addTask(this.fragment.new LoadImage(connect
		// .getJid()));
		// }
		// }
		// } else {
		// // 在非fling状态下，添加到网络请求
		// if (!isfling && manager != null) {
		// manager.addTask(this.fragment.new LoadImage(connect.getJid()));
		// }
		// }
		if (cache.getCache().get(connect.getJid()) != null) {
			bitmap = cache.getCache().get(connect.getJid());
			iv_image.setImageBitmap(bitmap);
		} else {
			if (!isfling && manager != null) {
				manager.addTask(this.fragment.new LoadImage(connect.getJid()));
			}
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/*
	 * 添加到缓存中
	 */
	public void putCache(String key, Bitmap value) {
		if (key != null && value != null) {
			cache.getCache().put(key, value);
		}
	}
}
