package com.hwant.adapter;

import java.util.ArrayList;

import org.wind.adapter.ViewHolder;

import com.hwant.activity.R;
import com.hwant.application.IMApplication;
import com.hwant.entity.ChatMessage;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatMessageAdapter extends BaseAdapter {
	private LayoutInflater inflater = null;
	private ArrayList<ChatMessage> list = null;
	private ChatMessage message = null;
	private IMApplication application;

	public ChatMessageAdapter(Application application,
			ArrayList<ChatMessage> list) {
		this.application = (IMApplication) application;
		this.inflater = LayoutInflater
				.from(application.getApplicationContext());
		if (list == null) {
			this.list = new ArrayList<ChatMessage>();
		} else {
			this.list = list;
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ChatMessage getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addMessage(ChatMessage message) {
		if (message != null) {
			list.add(message);
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.chat_item_layout, parent,
					false);
		}
		RelativeLayout ll_left = (RelativeLayout) ViewHolder.getInstance()
				.getView(convertView, R.id.rl_chat_item_left);
		RelativeLayout ll_right = (RelativeLayout) ViewHolder.getInstance()
				.getView(convertView, R.id.rl_chat_item_right);
		TextView tv_self = (TextView) ViewHolder.getInstance().getView(
				convertView, R.id.tv_chat_righttxt);
		TextView tv_other = (TextView) ViewHolder.getInstance().getView(
				convertView, R.id.tv_chat_lefttxt);
		message = getItem(position);
		if (application.user.getJid().equals(message.getInfo().getJid())) {
			ll_left.setVisibility(View.GONE);
			ll_right.setVisibility(View.VISIBLE);
			tv_self.setText(String.valueOf(message.getMessage()));
		} else {
			ll_right.setVisibility(View.GONE);
			ll_left.setVisibility(View.VISIBLE);
			tv_other.setText(String.valueOf(message.getMessage()));
		}
		return convertView;
	}

}
