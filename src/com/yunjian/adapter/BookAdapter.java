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

public class BookAdapter extends BaseAdapter{
	private List<Map<String, Object>>list;
	private LayoutInflater layoutInflater;
	private Context context;
	private ImageLoader mImageLoader;
	public BookAdapter(Context context,List<Map<String, Object>> list){
		this.context = context;
		this.layoutInflater=LayoutInflater.from(context);
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

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		 ViewHolder viewHolder; 
	      if (convertView == null) 
	      { 
	          convertView = layoutInflater.inflate(R.layout.picture_item, null); 
	          viewHolder = new ViewHolder(); 
	          viewHolder.title = (TextView) convertView.findViewById(R.id.title); 
	          viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
	          viewHolder.count = (TextView) convertView.findViewById(R.id.name);
	          convertView.setTag(viewHolder); 
	      } else
	      { 
	          viewHolder = (ViewHolder) convertView.getTag(); 
	      } 
	    //Õº∆¨º”‘ÿ
			try {
				mImageLoader.addTask(Utils.IMGURL+list.get(position).get("img"), viewHolder.image);
			} catch (Exception e) {
			// TODO: handle exception
				e.printStackTrace();
			}
	      viewHolder.title.setText(list.get(position).get("bookname").toString());
	      viewHolder.count.setText(list.get(position).get("count").toString()+"∏ˆ¬Ùº“");
	      
	      return convertView; 
	}
	
	public class ViewHolder{
		  public TextView title; 
		  public ImageView image;
		  public TextView count;
		  public TextView address;
	}

}
