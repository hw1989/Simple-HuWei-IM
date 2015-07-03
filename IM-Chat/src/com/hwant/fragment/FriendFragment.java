package com.hwant.fragment;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;

import com.hwant.activity.IndexActivity;
import com.hwant.activity.LoginActivity;
import com.hwant.activity.R;
import com.hwant.application.IMApplication;
import com.hwant.services.IDoWork;
import com.hwant.services.TaskManager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FriendFragment extends Fragment {
	private IMApplication application = null;
	private TaskManager manager = null;
	private IndexActivity activity = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (IndexActivity) getActivity();
		application = (IMApplication) (getActivity().getApplication());
		manager = activity.manager;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.friends_layout, container, false);
		// manager.addTask(new GetFriend());
		return view;
	}

	
}
