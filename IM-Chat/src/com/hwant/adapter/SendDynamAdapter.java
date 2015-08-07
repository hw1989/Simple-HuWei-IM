package com.hwant.adapter;

import java.util.ArrayList;

import org.wind.adapter.OtherAdapter;
import org.wind.adapter.ViewHolder;

import com.hwant.activity.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SendDynamAdapter extends OtherAdapter<String> {
	private LruCache<String, Bitmap> cache = null;
	private String item = "";
	private Bitmap bitmap = null;
	private Options options = null;

	public SendDynamAdapter(Context context, ArrayList<String> list) {
		super(context, list);
		long size = (Runtime.getRuntime().maxMemory() / 1024 / 1024) / 6;
		cache = new LruCache<String, Bitmap>((int) size) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return super.sizeOf(key, value);
			}
		};
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.senddynam_img_item_layout,
					parent, false);
		}
		ImageView iv_img = ViewHolder.getInstance().getView(convertView,
				R.id.iv_sdynam_item);
		item = getItem(position);
		if ("".equals(item.trim())) {
			iv_img.setImageResource(R.drawable.fml);
		} else {
			bitmap = cache.get(item);
			if (bitmap == null) {
				options = new Options();
				options.inJustDecodeBounds = true;
				bitmap = BitmapFactory.decodeFile(item, options);
				int max = Math.max(options.outWidth, options.outHeight);
				options.inSampleSize = (max / 300 == 0 ? 1 : max / 300);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(item, options);
				cache.put(item, bitmap);
				iv_img.setImageBitmap(bitmap);
			} else {
				iv_img.setImageBitmap(bitmap);
			}
		}
		return convertView;
	}

}
