package com.hwant.utils;

import java.io.File;

import org.wind.util.StringHelper;

import com.hwant.common.Common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FileUtils {
	public static boolean isExistsImg(String filename) {
		if (filename != null && StringHelper.isEmpty(filename.trim())) {
			return false;
		}
		File file = new File(Environment.getExternalStorageDirectory()+Common.Path_Image,
				filename);
		return file.exists();
	}
	public static Bitmap getImageBitemap(String filename){
		File file = new File(Environment.getExternalStorageDirectory()+Common.Path_Image,
				filename);
		Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
		return bitmap;
		
	}
}
