package com.yunjian.adapter;

import java.util.List;
import java.util.Map;

import com.yunjian.activity.AddBookActivity;
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

public class DealedGoodsAdapter extends BaseAdapter{
	private List<Map<String, Object>>list;
	private LayoutInflater layoutInflater;
	private Context context;
	private ImageLoader mImageLoader;
	private OnQueryCompleteListener onQueryCompleteListener;
	private UserCenterService service;
	
	public DealedGoodsAdapter(Context context,List<Map<String, Object>> list){
		this.context = context;
		this.layoutInflater=LayoutInflater.from(context);
		this.list = list;
		mImageLoader = ImageLoader.getInstance(context);
	}
	
	public class Item{
		public ImageView imageView;
		public TextView nameTextView;
		public TextView statusTextView;
		public Button editButton;
		public Button selloutButton;
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

	@SuppressWarnings("unused")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Item item = null;
		if(item == null){
			item = new Item();
			arg1 = layoutInflater.inflate(R.layout.goodslist_item, null);
			item.imageView = (ImageView)arg1.findViewById(R.id.bookphoto);
			item.nameTextView = (TextView)arg1.findViewById(R.id.bookname);
			item.statusTextView = (TextView)arg1.findViewById(R.id.bookstaus);
			item.editButton = (Button)arg1.findViewById(R.id.edit_btn);
			item.selloutButton = (Button)arg1.findViewById(R.id.sellout_btn);
			
			item.nameTextView.setText(list.get(arg0).get("bookname").toString());
			String status = list.get(arg0).get("status").toString();
			if(status.equals("0.0"))
				item.statusTextView.setText("���ڳ���");
			else{			
				item.statusTextView.setText("���۳�");
				item.selloutButton.setVisibility(View.GONE);
			}
			arg1.setTag(item);
		}
		else {
			item = (Item)arg1.getTag();
		}
		//ͼƬ����
		try {
			String imgs = list.get(arg0).get("imgs").toString().substring(1, 37);
			System.out.println(imgs);
			mImageLoader.addTask(Utils.IMGURL+imgs, item.imageView);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result.equals("success")){
					notifyDataSetChanged();
					Toast.makeText(context, "�ɹ��۳�", 2000).show();
				}
				else {
					Toast.makeText(context, "�۳�ʧ��", 2000).show();
				}
			}
		};
		ItemClickListener itemClickListener = new ItemClickListener(arg0);
		//�۳�����
		item.selloutButton.setOnClickListener(itemClickListener);
		//�༭����
		item.editButton.setOnClickListener(itemClickListener);
		
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
				Intent intent = new Intent(context,AddBookActivity.class);
				Bundle bundle = new Bundle();
				Map<String,Object> data=list.get(position);  
	            SerializableMap tmpmap=new SerializableMap();  
	            tmpmap.setMap(data);  
	            bundle.putSerializable("bookinfo", tmpmap);  
	            intent.putExtras(bundle);
	            Utils.IFEDITBOOK = 1;
				context.startActivity(intent);
			}
			else if(arg0.getId()==R.id.sellout_btn){
				new AlertDialog.Builder(context)   
				.setTitle("ȷ���۳�")  
				.setMessage("�鼮���ۺ󽫲�����ʾ��������Ҳ����������Ʒ���Ƿ�ȷ���۳�")  
				.setPositiveButton("��", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								service = new UserCenterService();
								service.setBookStatus(Utils.user_id, list.get(position).get("book_id").toString(), 1, onQueryCompleteListener);
							}
						})  
				.setNegativeButton("��", null)  
				.show();  
			}
		}
    	
    } 
}
