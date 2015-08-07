package com.hwant.broadcast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.hwant.common.Common;
import com.hwant.common.RecevierConst;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;

public class ServiceReceiver extends BroadcastReceiver {
	private SimpleDateFormat format = null;
	private Options options = null;
	private String root = "";

	public ServiceReceiver() {
		format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		options = new Options();
		root = Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(RecevierConst.Service_Work_Dynamic)) {
			// 图片路径
			ArrayList<String> images = intent.getStringArrayListExtra("images");
			String dynamic_content = intent.getStringExtra("content");
			ArrayList<String> files = new ArrayList<String>();
			String[] paths = new String[images.size() - 1];
			// 发表说说
			FileOutputStream foStream = null;
			File file = null;
			for (int i = 0; i < images.size() - 1; i++) {
				String filename = format.format(new Date()) + ".png";
				file = new File(root + Common.Path_Cache + filename);
				String item = images.get(i);
				options.inJustDecodeBounds = true;
				Bitmap bitmap = BitmapFactory.decodeFile(item, options);
				int max = Math.max(options.outWidth, options.outHeight);
				options.inSampleSize = (max / 300 == 0 ? 1 : max / 300);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(item, options);
				try {
					foStream = new FileOutputStream(file);
					bitmap.compress(CompressFormat.PNG, 1, foStream);
					files.add(filename);
					// 路径
					paths[i] = (root + Common.Path_Cache + filename);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					if (foStream != null) {
						try {
							foStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			BmobProFile.getInstance(context).uploadBatch(paths,
					new MyUploadBatchListener());
		}
	}

	class MyUploadBatchListener implements UploadBatchListener {

		@Override
		public void onError(int arg0, String arg1) {

		}

		@Override
		public void onProgress(int arg0, int arg1, int arg2, int arg3) {

		}

		@Override
		public void onSuccess(boolean arg0, String[] arg1, String[] arg2,
				BmobFile[] arg3) {

		}
	}
}
