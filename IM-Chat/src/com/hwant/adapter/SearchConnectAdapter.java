package com.hwant.adapter;

import java.util.ArrayList;

import org.wind.adapter.OtherAdapter;
import org.wind.adapter.ViewHolder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwant.activity.R;
import com.hwant.entity.ConnectInfo;

public class SearchConnectAdapter extends OtherAdapter<ConnectInfo> {
	private ConnectInfo info = null;

	public SearchConnectAdapter(Context context, ArrayList<ConnectInfo> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.search_connect_item_layout,
					parent, false);
		}
		ImageView iv_img = ViewHolder.getInstance().getView(convertView,
				R.id.iv_search_img);
		TextView tv_jid = ViewHolder.getInstance().getView(convertView,
				R.id.tv_search_jid);
		TextView tv_nick = ViewHolder.getInstance().getView(convertView,
				R.id.tv_search_nickname);
		info = getItem(position);
		tv_jid.setText(info.getJid());
		return convertView;
	}

	public void setData(ArrayList<ConnectInfo> connectInfos) {
		if (connectInfos != null) {
			if (connectInfos.size() > 0) {
				this.list.clear();
				this.list.addAll(connectInfos);
				notifyDataSetChanged();
			}
		}
	}
	public void clearData(){
		this.list.clear();
		notifyDataSetChanged();
	}
}
