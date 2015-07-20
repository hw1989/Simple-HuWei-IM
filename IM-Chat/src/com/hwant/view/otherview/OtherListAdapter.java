package com.hwant.view.otherview;

import org.wind.adapter.ViewHolder;

import com.hwant.activity.R;

import android.R.integer;
import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OtherListAdapter extends BaseAdapter {
	private SparseIntArray sparseArray = null;
	private LayoutInflater inflater = null;
	private int page = 0;

	public OtherListAdapter(Context context, SparseIntArray sparseArray,
			int page) {
		if (sparseArray == null) {
			this.sparseArray = new SparseIntArray();
		} else {
			this.sparseArray = sparseArray;
		}
		inflater = LayoutInflater.from(context);
		this.page = page;
	}

	@Override
	public int getCount() {
		// return sparseArray.size();
		// Math.min(sparseArray.size(),(page+1)*8);
		return sparseArray.size() >= (page + 1) * 8 ? 8
				: sparseArray.size() % 8;
	}

	@Override
	public Integer getItem(int position) {
		//某一页的position
		return sparseArray.get(sparseArray.keyAt(position+8*page));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.others_gvitem_layout,
					parent, false);
		}
		ImageView iv_icon = ViewHolder.getInstance().getView(convertView,
				R.id.iv_gv_others);
		TextView tv_name = ViewHolder.getInstance().getView(convertView,
				R.id.tv_gv_othersname);
		int id = sparseArray.keyAt(position);
		// String name = getItem(position);
		iv_icon.setImageResource(id);
		tv_name.setText(getItem(position));
		return convertView;
	}

}
