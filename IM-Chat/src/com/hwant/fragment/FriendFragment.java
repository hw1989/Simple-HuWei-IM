package com.hwant.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import com.hwant.activity.IndexActivity;
import com.hwant.activity.R;
import com.hwant.adapter.ConnectAdapter;
import com.hwant.application.IMApplication;
import com.hwant.entity.ConnectInfo;
import com.hwant.services.IDoWork;
import com.hwant.services.TaskManager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class FriendFragment extends Fragment {
	private IMApplication application = null;
	private TaskManager manager = null;
	private IndexActivity activity = null;
	private ExpandableListView elv_friend = null;
	// private ArrayList<String> groups=null;
	private ArrayList<ConnectInfo> list = null;
	// 内容接受者
	private ContentResolver resolver = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (IndexActivity) getActivity();
		application = (IMApplication) (getActivity().getApplication());
		manager = activity.manager;
		// groups=new ArrayList<String>();
		// friends=new HashMap<Integer, ArrayList<ConnectInfo>>();
		list = new ArrayList<ConnectInfo>();
		resolver = getActivity().getContentResolver();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.friends_layout, container, false);
		elv_friend = (ExpandableListView) view.findViewById(R.id.elv_connect);
		getGroup();
		ConnectAdapter adapter = new ConnectAdapter(getActivity(), list);
		elv_friend.setAdapter(adapter);
		return view;
	}

	public void getGroup() {
		Uri uri = Uri.parse("content://com.hwant.im.friend/friend");
		Cursor cursor = resolver.query(uri, null, null, null, null);
		cursor.moveToFirst();
		ConnectInfo connect = null;
		while (!cursor.isAfterLast()) {
			connect = new ConnectInfo();
			connect.setJid(cursor.getString(cursor.getColumnIndex("jid")));
			connect.setName(cursor.getString(cursor.getColumnIndex("name")));
			connect.setGroup(cursor.getString(cursor.getColumnIndex("fgroup")));
			connect.setNickname(cursor.getString(cursor
					.getColumnIndex("nickname")));
			list.add(connect);
			cursor.moveToNext();
		}
		cursor.close();
	}

}
