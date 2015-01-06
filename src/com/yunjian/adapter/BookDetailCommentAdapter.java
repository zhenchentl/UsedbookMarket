package com.yunjian.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunjian.activity.R;
import com.yunjian.image.ImageLoader;
import com.yunjian.util.Utils;

public class BookDetailCommentAdapter extends BaseAdapter{
	private List<Map<String, Object>>list;
	private LayoutInflater layoutInflater;
	private Context context;
	private ImageLoader mImageLoader;
	public BookDetailCommentAdapter(Context context,List<Map<String, Object>> list){
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
	          convertView = layoutInflater.inflate(R.layout.book_detail_comment, null); 
	          viewHolder = new ViewHolder(); 
	          viewHolder.title = (TextView) convertView.findViewById(R.id.title); 
	          viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
	          viewHolder.info = (TextView) convertView.findViewById(R.id.info);
	          convertView.setTag(viewHolder); 
	      } else
	      { 
	          viewHolder = (ViewHolder) convertView.getTag(); 
	      } 
	    //ͼƬ����
			try {
				mImageLoader.addTask(Utils.URL+list.get(position).get("user_id"), viewHolder.img);
			} catch (Exception e) {
			// TODO: handle exception
				e.printStackTrace();
			}
	      viewHolder.title.setText(list.get(position).get("username").toString());
	      viewHolder.info.setText(list.get(position).get("content").toString());
	      
	      return convertView; 
	}
	
	public class ViewHolder{
		  public ImageView img;
		  public TextView title;
		  public TextView info;
	}

}