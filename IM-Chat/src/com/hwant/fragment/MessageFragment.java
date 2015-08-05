package com.hwant.fragment;

import com.hwant.activity.R;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessageFragment extends Fragment {
	private ContentResolver resolver = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resolver = getActivity().getContentResolver();

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.message_list_layout, container,
				false);
		
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private void getMessages() {
		Uri uri = Uri.parse("content://org.hwant.im.chat/chat");
		// resolver.query(uri, null," ", selectionArgs, sortOrder);
	}
}
