package com.yunjian.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yunjian.activity.R;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.image.ImageLoader;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchResultAdapter extends BaseAdapter implements OnQueryCompleteListener{
	private Context context;
	private LayoutInflater layoutInflater;
	private List<Map<String, Object>>list;
	private ImageLoader imageLoader;
	
	public SearchResultAdapter(Context context,List<Map<String, Object>>list){
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
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Item item = null;
		item = new Item();
		arg1 = layoutInflater.inflate(R.layout.search_result_item, null);
		item.imageView = (ImageView)arg1.findViewById(R.id.search_bookimage);
		item.nameText = (TextView)arg1.findViewById(R.id.search_bookname);
		item.sellerText = (TextView)arg1.findViewById(R.id.search_seller);
		item.booktypeText = (TextView)arg1.findViewById(R.id.search_booktype);
		item.timeText = (TextView)arg1.findViewById(R.id.search_time);
		item.connectButton = (Button)arg1.findViewById(R.id.connect_seller);
		
		item.nameText.setText(list.get(arg0).get("bookname").toString());
		item.sellerText.setText(list.get(arg0).get("username").toString());
//		switch(list.get(arg0).get("type").toString()){
//		case "1":
//			item.booktypeText.setText("教材资料");
//		case "2":
//			item.booktypeText.setText("英语强化");
//		case "3":
//			item.booktypeText.setText("日语强化");
//		case "4":
//			item.booktypeText.setText("技术养成");
//		case "5":
//			item.booktypeText.setText("考研专区");
//		case "6":
//			item.booktypeText.setText("休闲阅读");
//		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date adddate = null;
		try {
			adddate = sdf.parse(list.get(arg0).get("added_time").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("adddate================="+adddate);
		Date curDate = new Date(System.currentTimeMillis());//鑾峰彇褰撳墠鏃堕棿

		System.out.println("curDate================="+curDate);
		Calendar cal0 = Calendar.getInstance();    
		cal0.setTime(adddate);
		Calendar cal1 = Calendar.getInstance();    
		cal1.setTime(curDate);    
		long time0 = cal0.getTimeInMillis(); 
		long time1 = cal1.getTimeInMillis(); 
		int days = (int) ((time1-time0)/(1000*3600*24));
		
		item.timeText.setText(days+"天前发布");
		
		item.connectButton.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("联系卖家");
//				Toast.makeText(context,"联系卖家", 1000);
			}
		});
		return arg1;
	}
	
	public class Item{
		private ImageView imageView;
		private TextView nameText;
		private TextView sellerText;
		private TextView booktypeText;
		private TextView timeText;
		private Button connectButton;
		
	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		
	}

}
