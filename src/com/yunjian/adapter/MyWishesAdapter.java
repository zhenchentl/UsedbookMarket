package com.yunjian.adapter;

import java.util.List;
import java.util.Map;

import com.yunjian.activity.AddWishActivity;
import com.yunjian.activity.R;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.image.ImageLoader;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.UserCenterService;
import com.yunjian.util.Utils;
import com.yunjian.util.SerializableMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyWishesAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater layoutInflater;
	private List<Map<String, Object>>list;
	private ImageLoader imageLoader;
	private UserCenterService service;
	private OnQueryCompleteListener onQueryCompleteListener;
	
	public MyWishesAdapter(Context context,List<Map<String, Object>>list){
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.list = list;
		imageLoader = ImageLoader.getInstance(context);
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
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Item item = null;
		if(item == null){
			item = new Item();
			arg1 = layoutInflater.inflate(R.layout.wisheslist_item,null);
			item.imageView = (ImageView)arg1.findViewById(R.id.wishphoto);
			item.wishname = (TextView)arg1.findViewById(R.id.wishname);
			item.wishstatus = (TextView)arg1.findViewById(R.id.wishstaus);
			item.editButton = (Button)arg1.findViewById(R.id.edit_btn);
			item.achieveButton = (Button)arg1.findViewById(R.id.achieve_btn);
			
			item.wishname.setText(list.get(arg0).get("bookname").toString());
			String status = list.get(arg0).get("status").toString();
			if(status.equals("1.0")){
				item.wishstatus.setText("�����Ը�����˽�����");
				item.achieveButton.setVisibility(View.GONE);
			}
			else {
				item.wishstatus.setText("�����Ը��������ʾ����");
			}
			arg1.setTag(item);
		}
		else {
			item = (Item)arg1.getTag();
		}
		//����ͼƬ
		//imageLoader.addTask(Utils.IMGURL+list.get(arg0).get("img"), item.imageView);
		
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result.equals("success")){
					Toast.makeText(context, "��Ը��ʵ��", 2000).show();
				}
				else {
					Toast.makeText(context, "ʵ��ʧ��", 2000).show();
				}
			}
		};
		ItemClickListener itemClickListener = new ItemClickListener(arg0);
		//�༭����
		item.editButton.setOnClickListener(itemClickListener);
		//ʵ�ּ���
		item.achieveButton.setOnClickListener(itemClickListener);
		
		return arg1;
	}
	public class ItemClickListener implements OnClickListener{
		private int position;
		public ItemClickListener(int position){
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0.getId()==R.id.edit_btn){
				Intent intent = new Intent(context,AddWishActivity.class);
				Bundle bundle = new Bundle();
				Map<String,Object> data=list.get(position);  
	            SerializableMap tmpmap=new SerializableMap();  
	            tmpmap.setMap(data);  
	            bundle.putSerializable("wishinfo", tmpmap);  
	            intent.putExtras(bundle);
	            Utils.IFEDITWISH  = 1;
				context.startActivity(intent);
			}
			else if (arg0.getId()==R.id.achieve_btn) {
				new AlertDialog.Builder(context)   
				.setTitle("ȷ���۳�")  
				.setMessage("�鼮���ۺ󽫲�����ʾ��������Ҳ����������Ʒ���Ƿ�ȷ���۳�")  
				.setPositiveButton("��", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								service = new UserCenterService();
								service.setWishStatus(Utils.user_id, Utils.username,list.get(position).get("wish_id").toString(), 1, onQueryCompleteListener);
							}
						})  
				.setNegativeButton("��", null)  
				.show();  
			}
		}
		
	}
	
	private class Item{
		private ImageView imageView;
		private TextView wishname;
		private TextView wishstatus;
		private Button editButton;
		private Button achieveButton;
	}

}
