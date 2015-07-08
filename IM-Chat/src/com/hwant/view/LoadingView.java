package com.hwant.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class LoadingView extends LinearLayout {

	public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LoadingView(Context context) {
		super(context);
	}
    private void init(){
    	LayoutInflater inflater= LayoutInflater.from(getContext());
    	
    }
    public void startAnimation(){
    	
    }
    public void stopAnimation(){
    	
    }
}
