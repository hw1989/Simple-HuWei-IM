package com.hwant.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.wind.adapter.ViewHolder;
import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import com.hwant.entity.ImageInfo;
import com.hwant.services.IDoWork;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.LruCache;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class PickImgActivity extends BaseActivity implements
		LoaderCallbacks<Cursor>, OnItemClickListener, OnClickListener {
	private String[] STORE_IMAGES = { MediaStore.Images.Media.DATA, // 图片绝对路径
			MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
	private SimpleCursorAdapter adapter = null;
	@ViewInject(id = R.id.gv_pick_images)
	private GridView gv_images = null;
	@ViewInject(id = R.id.tv_pick_back)
	private TextView tv_back;
	@ViewInject(id = R.id.tv_pick_sure)
	private TextView tv_sure;
	private LruCache<String, Bitmap> cache = null;
	private ArrayList<String> list = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.pick_img_layout);
		ActivityInject.getInstance().setInject(this);
		list = new ArrayList<String>();
		adapter = new SimpleCursorAdapter(this, R.layout.pick_item_layout,
				null, STORE_IMAGES, new int[] { R.id.iv_pick_img,
						R.id.cb_pick_img }, 0);
		adapter.setViewBinder(new PickImageBinder());
		gv_images.setAdapter(adapter);
		gv_images.setOnItemClickListener(this);
		tv_back.setOnClickListener(this);
		tv_sure.setOnClickListener(this);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk <= 12) {
			// getLoaderManager().initLoader(0,null,this);
			getSupportLoaderManager().initLoader(0, null, this);
		} else {
			getSupportLoaderManager().initLoader(0, null, this);
		}
		long size = Runtime.getRuntime().maxMemory() / 8 / 1024 / 1024;
		// 设置缓存
		cache = new LruCache<String, Bitmap>((int) size) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getHeight() * value.getRowBytes();
			}
		};
		bindService();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		CursorLoader loader = new CursorLoader(this,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,
				null, null, null);

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		adapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

	class PickImageBinder implements ViewBinder {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			String path = cursor.getString(0);
			if (view instanceof ImageView) {
				ImageView iv_image = (ImageView) view;
				iv_image.setTag(path);
				iv_image.setImageResource(R.drawable.aio_image_default);
				if (cache.get(path) != null) {
					iv_image.setImageBitmap(cache.get(path));
				} else {
					service.manager.addTask(new LoadImageTask(
							PickImgActivity.this, cursor.getString(0)));
				}
			} else if (view instanceof CheckBox) {
				CheckBox cb_select = (CheckBox) view;
				if (list.contains(path)) {
					cb_select.setChecked(true);
				} else {
					cb_select.setChecked(false);
				}
			}
			return true;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (adapter != null && adapter.getCursor() != null) {
			adapter.getCursor().close();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String tag = (String) view.findViewById(R.id.iv_pick_img).getTag();
		CheckBox cb_select = (CheckBox) view.findViewById(R.id.cb_pick_img);
		if (list.contains(tag)) {
			list.remove(tag);
			cb_select.setChecked(false);
		} else {
			list.add(tag);
			cb_select.setChecked(true);
		}
	}

	class LoadImageTask implements IDoWork {
		private WeakReference<Activity> weakReference = null;
		private String path = "";

		public LoadImageTask(Activity activity, String path) {
			weakReference = new WeakReference<Activity>(activity);
			this.path = path;
		}

		@Override
		public Object doWhat() {
			Bitmap bitmap = null;
			Options options = new Options();
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeFile(this.path, options);
			int width = options.outWidth;
			int height = options.outHeight;
			int size = Math.min(width, height);
			int insample = (size / 150 == 0 ? 1 : size / 150);
			options.inSampleSize = insample;
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(this.path, options);
			ImageInfo info = new ImageInfo();
			info.setPath(path);
			info.setBitmap(bitmap);
			return info;
		}

		@Override
		public void Finish2Do(Object obj) {
			if (weakReference.get() != null) {
				if (obj != null) {
					ImageInfo info = (ImageInfo) obj;
					ImageView iv = (ImageView) gv_images.findViewWithTag(info
							.getPath());
					cache.put(info.getPath(), info.getBitmap());
					if (iv != null) {
						iv.setImageBitmap(info.getBitmap());
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_pick_back:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.tv_pick_sure:
			Intent intent = new Intent();
			intent.putStringArrayListExtra("images", list);
			setResult(RESULT_OK, intent);
			finish();
			break;
		}

	}
}
