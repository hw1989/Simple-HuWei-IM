package com.hwant.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class SearchView extends ViewGroup {
	private Paint paintl = null;
	private Paint paintm = null;
	private Paint paintr = null;
	private int width, height;
	private int innerradiu = 20;

	public SearchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SearchView(Context context) {
		super(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
	}

	private void init() {
		//
		paintl = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintl.setColor(Color.WHITE);
		paintl.setAntiAlias(true);
		paintl.setStyle(Style.FILL);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (height < innerradiu) {
			return;
		}
		RectF rectF=new RectF(0, 0, height,height);
        canvas.drawArc(rectF, 90, 180, true, paintl);
        //
        RectF rectF_right = new RectF(width-height, 0,width, height);
		canvas.drawArc(rectF_right, 270, 180, true, paintl);
	}
}
