package com.hwant.activity;

import org.wind.adapter.ViewHolder;
import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;

import android.R.integer;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class PickImgActivity extends BaseActivity implements
		LoaderCallbacks<Cursor>, OnItemClickListener {
	private String[] STORE_IMAGES = {
			MediaStore.Images.Media.DATA, // 图片绝对路径
			MediaStore.Images.Media.DISPLAY_NAME,
			MediaStore.Images.Media.LATITUDE,
			MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media._ID };
	// private String[] STORE_IMAGES = { MediaStore.Images.Media.DISPLAY_NAME,
	// MediaStore.Images.Media.DATA};
	private SimpleCursorAdapter adapter = null;
	@ViewInject(id = R.id.gv_pick_images)
	private GridView gv_images = null;
	@ViewInject(id = R.id.tv_pick_back)
	private TextView tv_back;
	@ViewInject(id = R.id.tv_pick_sure)
	private TextView tv_sure;
	// 使用异步加载图片
	private LoadImageTask task = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.pick_img_layout);
		ActivityInject.getInstance().setInject(this);

		adapter = new SimpleCursorAdapter(this, R.layout.pick_item_layout,
				null, STORE_IMAGES, new int[] { R.id.iv_pick_img,
						R.id.cb_pick_img }, 0);
		adapter.setViewBinder(new PickImageBinder());
		gv_images.setAdapter(adapter);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk <= 12) {
			// getLoaderManager().initLoader(0,null,this);
			getSupportLoaderManager().initLoader(0, null, this);
		} else {
			getSupportLoaderManager().initLoader(0, null, this);
		}
		task = new LoadImageTask();
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
			if (columnIndex == 0) {
				// ImageView iv_image = ViewHolder.getInstance().getView(view,
				// R.id.iv_pick_img);
				ImageView iv_image = (ImageView) view;
				iv_image.setTag(cursor.getString(0));
				task.execute(cursor.getString(0));
			}
			// Log.e("info", columnIndex+" -------------  " +
			// cursor.getString(columnIndex));
			return true;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	class LoadImageTask extends AsyncTask<String, Void, String> {
		private LruCache<String, Bitmap> cache = null;

		public LoadImageTask() {
			long size = Runtime.getRuntime().maxMemory() / 8 / 1024 / 1024;
			cache = new LruCache<String, Bitmap>((int) size) {

				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getHeight() * value.getRowBytes();
				}
			};
		}

		@Override
		protected String doInBackground(String... params) {
			Bitmap bitmap = null;
			if (cache.get(params[0]) != null) {
				bitmap = cache.get(params[0]);
			} else {
				Options options = new Options();
				options.inJustDecodeBounds = true;
				bitmap = BitmapFactory.decodeFile(params[0]);
				int width = options.outWidth;
				int height = options.outHeight;
				int size = Math.max(width, height);
				int insample = (size / 100 == 0 ? 1 : size / 100);
				options.inSampleSize = insample;
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(params[0]);
				cache.put(params[0], bitmap);
			}
			return params[0];
		}

		@Override
		protected void onPostExecute(String params) {
			if (cache.get(params) != null) {
				ImageView iView = (ImageView) (gv_images
						.findViewWithTag(params));
				iView.setImageBitmap(cache.get(params));
			}
		}

	}
}
