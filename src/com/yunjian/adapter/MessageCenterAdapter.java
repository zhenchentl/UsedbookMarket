package com.yunjian.adapter;

import java.util.List;
import java.util.Map;

import com.yunjian.activity.R;
import com.yunjian.image.ImageLoader;
import com.yunjian.util.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageCenterAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater layoutInflater;
	private List<Map<String, Object>>list;
	private ImageLoader mImageLoader;
	
	public MessageCenterAdapter(Context context,List<Map<String, Object>>list){
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.list = list;
		mImageLoader = ImageLoader.getInstance(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Item item = null;
		if(item == null){
			item = new Item();
			arg1 = layoutInflater.inflate(R.layout.message_item, null);
			item.imageView = (ImageView)arg1.findViewById(R.id.message_icon);
			item.messageType = (TextView)arg1.findViewById(R.id.message_type);
			item.messageContent = (TextView)arg1.findViewById(R.id.message_content);
			item.messageTime = (TextView)arg1.findViewById(R.id.message_time);
			item.messageAfterType = (TextView)arg1.findViewById(R.id.message_aftertype);
			String type = list.get(arg0).get("type").toString();
			if(type.equals("0.0")){
				item.messageType.setText("ϵͳ��Ϣ");
				item.imageView.setBackgroundResource(R.drawable.message_system_icon);
				item.messageContent.setText(list.get(arg0).get("content").toString());
			}
			else if(type.equals("1.0")){
				item.messageAfterType.setText("�������������鼮");
				item.messageType.setText(list.get(arg0).get("username").toString());
				item.messageContent.setText(list.get(arg0).get("content").toString());
				item.messageTime.setText(list.get(arg0).get("time").toString());
				//ͼƬ����
				try {
					mImageLoader.addTask(Utils.IMGURL+list.get(arg0).get("img"), item.imageView);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			else if(type.equals("2.0")){
				item.messageAfterType.setText("�����������Ը�鵥");
				item.messageType.setText(list.get(arg0).get("username").toString());
				item.messageContent.setText(list.get(arg0).get("content").toString());
				item.messageTime.setText(list.get(arg0).get("time").toString());
				//ͼƬ����
				try {
					mImageLoader.addTask(Utils.URL+list.get(arg0).get("user_id"), item.imageView);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			else if(type.equals("3.0")){
				item.messageType.setText("��Ը��֪ͨ");
				item.imageView.setBackgroundResource(R.drawable.message_wish_icon);
				item.messageContent.setText("�����Ը����"+list.get(arg0).get("username").toString()+"������");
				item.messageTime.setText(list.get(arg0).get("time").toString());
			}
			
			arg1.setTag(item);
		}
		else{
			item=(Item)arg1.getTag();
		}
		return arg1;
	}
	
	private class Item{
		private ImageView imageView;
		private TextView messageType;
		private TextView messageContent;
		private TextView messageTime;
		private TextView messageAfterType;
	}

}
