package com.hwant.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;
import org.wind.util.FileUtils;

import com.hwant.common.Common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

public class SelectImgActivity extends BaseActivity implements OnClickListener {
	@ViewInject(id = R.id.ll_select_image)
	private LinearLayout ll_select = null;
	@ViewInject(id = R.id.btn_close_selectimg)
	private Button btn_close;
	@ViewInject(id = R.id.btn_pick_camera)
	private Button btn_camera;
	@ViewInject(id = R.id.btn_pick_photo)
	private Button btn_photo;
	private DisplayMetrics metrics = null;
	// private int ll_height = 0;
	private TranslateAnimation animation = null;
	// 应用的高度，不包括状态栏的高度
	private int height = 0;
	private Intent intent = null;
	private static final int Pick_Camera = 1;
	private static final int Pick_Photo = 2;
	private static final int Pick_Cut = 3;
	private SimpleDateFormat format = null;
	//文件名
	private String filename="";
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.select_img_layout);
		ActivityInject.getInstance().setInject(this);
		metrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		init();
	}

	private void init() {
		format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		btn_close.setOnClickListener(this);
		btn_camera.setOnClickListener(this);
		btn_photo.setOnClickListener(this);
		int width = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		ll_select.measure(width, height);
		// ll_height = ll_select.getMeasuredHeight();
		// float density= getResources().getDisplayMetrics().density;
		//
		// ll_height=(int)(ll_select.getMeasuredHeight()/metrics.density+0.5);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_close_selectimg:
			this.finish();
			break;
		case R.id.btn_pick_photo:
			intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, 1);
			break;
		case R.id.btn_pick_camera:
			filename = format.format(new Date()) + ".png";
			intent = new Intent("android.media.action.IMAGE_CAPTURE");
			// 判断是否存在sd卡
			if (true) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory()+Common.Path_Image, filename);
				// 从文件中创建uri
				Uri uri = Uri.fromFile(tempFile);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			}
			startActivityForResult(intent, Pick_Camera);
			break;
		}
	}

	// 需要在这个方法里获取状态栏的高度
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		int ll_height = ll_select.getMeasuredHeight();
		final int ll_width = ll_select.getMeasuredWidth();
		// 设置居下的高度
		int marginbottom = 20;
		Rect outRect = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
		height = outRect.bottom;
		// 屏幕高度-容器高度-状态栏高度-居下距离
		final int lastY = height - ll_height - outRect.top - marginbottom;
		animation = new TranslateAnimation(0, 0, metrics.heightPixels, lastY);
		animation.setFillAfter(true);
		animation.setDuration(200);
		final int anim_bottom = height - outRect.top - marginbottom;
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				ll_select.clearAnimation();
				ll_select.layout(0, lastY, ll_width, anim_bottom);
			}
		});
		ll_select.startAnimation(animation);
	}

	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		if (resultcode == Pick_Camera) {
			//判断有内存卡
            if(true){
            	if(FileUtils.createorexistsPath(, create)){
            		
            	}
            }
		} else if (resultcode == Pick_Photo) {
			if (data != null) {
				Uri uri = data.getData();
				imageCut(uri);
			}
		}
	}

	/*
	 * 剪切图片
	 */
	private void imageCut(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);

		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		startActivityForResult(intent, Pick_Cut);
	}
}
