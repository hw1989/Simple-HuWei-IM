package com.hwant.fragment;

import java.util.ArrayList;

import com.hwant.activity.ChatActivity;
import com.hwant.activity.IndexActivity;
import com.hwant.activity.R;
import com.hwant.adapter.ConnectAdapter;
import com.hwant.application.IMApplication;
import com.hwant.entity.ConnectInfo;
import com.hwant.services.TaskManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class ConnectFragment extends Fragment implements OnGroupCollapseListener,OnGroupExpandListener,OnChildClickListener,OnItemClickListener{
	private IMApplication application = null;
	private TaskManager manager = null;
	private IndexActivity activity = null;
	private ExpandableListView elv_friend = null;
	private ArrayList<ConnectInfo> list = null;
	// 内容接受者
	private ContentResolver resolver = null;
    private ConnectAdapter adapter=null;
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
//		elv_friend.setOnGroupCollapseListener(this);
//		elv_friend.setOnGroupExpandListener(this);
		elv_friend.setOnChildClickListener(this);
//		elv_friend.setOnItemClickListener(this);
		getGroup();
		adapter = new ConnectAdapter(getActivity(), list);
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

	@Override
	public void onGroupCollapse(int groupPosition) {
		
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Toast.makeText(getActivity(),"456",Toast.LENGTH_SHORT).show();
		ConnectInfo info=adapter.getChild(groupPosition, childPosition);
		Intent intent=new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("info",info);
		startActivity(intent);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(getActivity(),"123",Toast.LENGTH_SHORT).show();
	}

}
