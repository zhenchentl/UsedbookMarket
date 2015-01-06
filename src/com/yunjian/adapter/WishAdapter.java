package com.yunjian.adapter;

import java.util.List;
import java.util.Map;

import com.yunjian.activity.R;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.image.ImageLoader;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.UserCenterService;
import com.yunjian.util.Utils;
import com.yunjian.view.HelpAchievePop;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WishAdapter extends BaseAdapter implements OnQueryCompleteListener{
	private LayoutInflater layoutInflater;
	private Context context;
	private List<Map<String, Object>>list;
	private ImageLoader mImageLoader;
	private UserCenterService service;
	private int pos = 0;
	
	public WishAdapter(Context context,List<Map<String, Object>>list){
		this.context = context;
		this.list = list;
		this.layoutInflater = LayoutInflater.from(context);
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
		if (item == null) {
			item = new Item();
			arg1 = layoutInflater.inflate(R.layout.list_item, null);
			item.imageView = (ImageView)arg1.findViewById(R.id.wish_book_img);
			item.titleTextView = (TextView)arg1.findViewById(R.id.wish_bookname_txv);
			item.reasonTextView = (TextView)arg1.findViewById(R.id.wish_description_txv);
			item.achieveButton = (Button)arg1.findViewById(R.id.wish_helpachieve_btn);
			item.wanterTextView = (TextView)arg1.findViewById(R.id.wish_wanter_txv);
			pos = arg0;
			item.titleTextView.setText(list.get(arg0).get("bookname").toString());
			item.reasonTextView.setText(list.get(arg0).get("description").toString());
			item.wanterTextView.setText("【发布人】"+list.get(arg0).get("username").toString());
			arg1.setTag(item);
		}
		else {
			item = (Item)arg1.getTag();
		}
		//图片加载
		try {
			//mImageLoader.addTask(Utils.IMGURL+list.get(arg0).get("img"), item.imageView);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		item.achieveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(list.get(pos).get("user_id").toString().equals(Utils.user_id)){
					Toast.makeText(context, "这是你自己的心愿单喔", 2000).show();
				}
				else {
					HelpAchievePop helpAchievePop = new HelpAchievePop(context,list.get(pos));
					helpAchievePop.showAtLocation(((Activity) context)
							.findViewById(R.id.wishmain), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				}
			}
		});
		return arg1;
	}
	
	private class Item{
		private ImageView imageView;
		private TextView titleTextView;
		private TextView wanterTextView;
		private TextView reasonTextView;
		private Button achieveButton;
	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		if(result.equals("success")){
			Toast.makeText(context, "已经接下心愿单", 2000).show();
		}
	}

}
