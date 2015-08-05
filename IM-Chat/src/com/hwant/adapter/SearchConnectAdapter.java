package com.hwant.adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;
import org.wind.adapter.OtherAdapter;
import org.wind.adapter.ViewHolder;
import org.wind.util.StringHelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwant.activity.R;
import com.hwant.activity.SearchConnectActivity;
import com.hwant.common.Common;
import com.hwant.entity.ConnectInfo;
import com.hwant.services.IDoWork;

public class SearchConnectAdapter extends OtherAdapter<ConnectInfo> {
	private ConnectInfo info = null;
	private LruCache<String, Bitmap> cache = null;
	private Bitmap bitmap = null;
	private SearchConnectActivity activity = null;
    
	public SearchConnectAdapter(SearchConnectActivity activity,
			ArrayList<ConnectInfo> list) {
		super(activity, list);
		this.activity = activity;
		long size = Runtime.getRuntime().maxMemory() / 1024 / 1024 / 6;
		cache = new LruCache<String, Bitmap>((int) size) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	public void putCache(String key, Bitmap value) {
		if (!StringHelper.isEmpty(key.trim()) && value != null) {
			cache.put(key, value);
		}
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
		iv_img.setTag(info.getJid());
		tv_jid.setText(info.getJid());
		bitmap = cache.get(info.getJid());
		if (bitmap == null) {
			activity.service.manager.addTask(new LoadImage(info.getJid()));
		} else {
			iv_img.setImageBitmap(bitmap);
		}
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

	public void clearData() {
		this.list.clear();
		notifyDataSetChanged();
	}

	/**
	 * 加载联系人的头像并写入数据库中
	 */
	public class LoadImage implements IDoWork {
		private VCard vcard = null;
		// private Bitmap bitmap=null;
		private WeakReference<Activity> weakReference = null;
		private String user = "";
		private FileOutputStream fos = null;
		private SimpleDateFormat format = null;
		// 文件名
		private String filename = "";

		public LoadImage(String user) {
			this.user = user;
			vcard = new VCard();
			weakReference = new WeakReference<Activity>(activity);
			format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			filename = format.format(new Date()) + ".png";
		}

		@Override
		public Object doWhat() {
			Bitmap bitmap = null;
			if (StringHelper.isEmpty(user)) {
				return null;
			}
			if (!activity.service.getConnection().isConnected()
					|| !activity.service.getConnection().isAuthenticated()) {
				return null;
			}
			try {
				vcard.load(activity.service.getConnection(), user);
				if (vcard != null && vcard.getAvatar() != null) {
					bitmap = BitmapFactory.decodeByteArray(vcard.getAvatar(),
							0, vcard.getAvatar().length);
				}
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			if (bitmap != null) {
				// String name = format.format(new Date()) + ".png";
				try {
					fos = new FileOutputStream(new File(
							Environment.getExternalStorageDirectory()
									+ Common.Path_Image, filename));
					bitmap.compress(CompressFormat.PNG, 1, fos);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return bitmap;
		}

		@Override
		public void Finish2Do(Object obj) {
			if (obj != null || weakReference.get() != null) {
				ImageView iv_img = (ImageView) activity.lv_search
						.findViewWithTag(user);
				if (iv_img != null) {
					putCache(user, (Bitmap) obj);
					iv_img.setImageBitmap((Bitmap) obj);
				}
			}
		}
	}
}
