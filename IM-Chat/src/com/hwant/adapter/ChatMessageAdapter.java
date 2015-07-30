package com.hwant.adapter;

import java.util.ArrayList;
import org.wind.adapter.OtherAdapter;
import org.wind.adapter.ViewHolder;
import com.hwant.activity.R;
import com.hwant.application.IMApplication;
import com.hwant.entity.ChatMessage;
import com.hwant.entity.ContentEntity;
import com.hwant.utils.MessageUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatMessageAdapter extends OtherAdapter<ChatMessage> {

	// private LayoutInflater inflater = null;
	// private ArrayList<ChatMessage> list = null;
	private ChatMessage message = null;
	private IMApplication application;
	private Bitmap selfimg = null, connectimg = null;
	private ArrayList<ContentEntity> entities = null;
	private CharSequence builder = null;
	private LruCache<String, Bitmap> cache = null;
	private Bitmap bitmap = null;
	private BitmapDrawable drawable = null;

	public ChatMessageAdapter(Context context, ArrayList<ChatMessage> list) {
		super(context, list);
		this.application = (IMApplication) context;
		long size = Runtime.getRuntime().maxMemory() / 1024 / 1024 / 6;
		cache = new LruCache<String, Bitmap>((int) size) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.chat_item_layout, parent,
					false);
		}
		RelativeLayout ll_left = (RelativeLayout) ViewHolder.getInstance()
				.getView(convertView, R.id.rl_chat_item_left);
		RelativeLayout ll_right = (RelativeLayout) ViewHolder.getInstance()
				.getView(convertView, R.id.rl_chat_item_right);
		TextView tv_self = (TextView) ViewHolder.getInstance().getView(
				convertView, R.id.tv_chat_righttxt);
		TextView tv_other = (TextView) ViewHolder.getInstance().getView(
				convertView, R.id.tv_chat_lefttxt);
		ImageView iv_left = (ImageView) ViewHolder.getInstance().getView(
				convertView, R.id.iv_chat_left);
		ImageView iv_right = (ImageView) ViewHolder.getInstance().getView(
				convertView, R.id.iv_chat_right);
		message = getItem(position);
		// if (application.user.getJid().equals(message.getInfo().getJid()))
		entities = MessageUtils.getMessageContent(message.getMessage());
		if (application.user.getJid().equals(message.getMfrom())) {
			ll_left.setVisibility(View.GONE);
			ll_right.setVisibility(View.VISIBLE);
			if (entities.size() > 0) {
				// 非文本的信息
				if (MessageUtils.TYPE_IMG
						.equalsIgnoreCase(entities.get(0).type)) {
					// 处理图片
					bitmap = cache.get(entities.get(0).message);
					if (bitmap == null) {
						bitmap = BitmapFactory
								.decodeFile(entities.get(0).message);
						cache.put(entities.get(0).message, bitmap);
						drawable = new BitmapDrawable(bitmap);
						drawable.setBounds(0, 0, 200, 200);
						tv_self.setBackground(drawable);
					} else {
						drawable = new BitmapDrawable(bitmap);
						drawable.setBounds(0, 0, 200, 200);
						tv_self.setBackground(drawable);
					}
				} else if (MessageUtils.TYPE_FACE.equalsIgnoreCase(entities
						.get(0).type)) {
					builder = MessageUtils.getFaceContent(application,
							String.valueOf(message.getMessage()), entities);
					tv_self.setText(builder);
				}
			} else {
				// 文本信息
				tv_self.setText(message.getMessage());
			}
			if (selfimg == null) {
				iv_right.setImageResource(R.drawable.gco);
			} else {
				iv_right.setImageBitmap(selfimg);
			}
		}
		// ----------------------------------------------------------------
		else {
			ll_right.setVisibility(View.GONE);
			ll_left.setVisibility(View.VISIBLE);
			if (entities.size() > 0) {
				
				// 非文本的信息
				if (MessageUtils.TYPE_IMG
						.equalsIgnoreCase(entities.get(0).type)) {
					// 处理图片
					bitmap = cache.get(entities.get(0).message);
					if (bitmap == null) {
						bitmap = BitmapFactory
								.decodeFile(entities.get(0).message);
						cache.put(entities.get(0).message, bitmap);
						drawable = new BitmapDrawable(bitmap);
						drawable.setBounds(0, 0, 200, 200);
						tv_other.setBackground(drawable);
					} else {
						drawable = new BitmapDrawable(bitmap);
						drawable.setBounds(0, 0, 200, 200);
						tv_other.setBackground(drawable);
					}
				} else if (MessageUtils.TYPE_FACE.equalsIgnoreCase(entities
						.get(0).type)) {
					builder = MessageUtils.getFaceContent(application,
							String.valueOf(message.getMessage()), entities);
					tv_other.setText(builder);
				}
//				builder = MessageUtils.getFaceContent(application,
//						String.valueOf(message.getMessage()), entities);
//				tv_other.setText(builder);
			} else {
				tv_other.setText(message.getMessage());
			}
			// tv_other.setText(String.valueOf(message.getMessage()));
			if (connectimg == null) {
				iv_left.setImageResource(R.drawable.gco);
			} else {
				iv_left.setImageBitmap(connectimg);
			}
		}
		return convertView;
	}

	/*
	 * 当头像发生改变时调用
	 */
	public void setChatImg(Bitmap self, Bitmap connect) {
		if (self != null) {
			selfimg = self;
		}
		if (connect != null) {
			connectimg = connect;
		}
		if (self != null && connect != null) {
			notifyDataSetChanged();
		}
	}
}