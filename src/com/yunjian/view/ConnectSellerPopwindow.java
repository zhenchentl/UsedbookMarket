package com.yunjian.view;


import java.util.Map;

import com.yunjian.activity.R;
import com.yunjian.image.ImageLoader;
import com.yunjian.util.Utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ConnectSellerPopwindow extends PopupWindow implements OnClickListener{
	private TextView phoneTextView,qqTextView,wechaTextView,usernameTextView;
	private ImageView photoImageView;
	private ImageLoader mImageLoader;
	private Context context;
	private Map<String, Object> map;
	private View view; 
	public ConnectSellerPopwindow(Context context,Map<String, Object>map){
		super(context);  
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.map = map;
        mImageLoader = ImageLoader.getInstance(context);
        view = inflater.inflate(R.layout.connect_seller_pop, null);  
        phoneTextView = (TextView) view.findViewById(R.id.hc_pop_phone_txv);  
        qqTextView = (TextView) view.findViewById(R.id.hc_pop_qq_txv);  
        wechaTextView = (TextView) view.findViewById(R.id.hc_pop_wechat_txv);  
        photoImageView = (ImageView)view.findViewById(R.id.hc_pop_user_img);
        usernameTextView = (TextView)view.findViewById(R.id.hc_pop_user_name);
        
        phoneTextView.setOnClickListener(this);
        qqTextView.setOnClickListener(this);
        wechaTextView.setOnClickListener(this);
        
        phoneTextView.setText("�ֻ� "+map.get("mobile").toString());
        qqTextView.setText("QQ "+map.get("qq").toString());
        wechaTextView.setText("΢�� "+map.get("weixin").toString());
        usernameTextView.setText(map.get("username").toString());
      //ͼƬ����
  		try {
  			mImageLoader.addTask(Utils.URL+map.get("user_id").toString(), photoImageView);
  		} catch (Exception e) {
  		// TODO: handle exception
  			e.printStackTrace();
  		}
      //����SelectPicPopupWindow��View  
        this.setContentView(view);  
        //����SelectPicPopupWindow��������Ŀ�  
        this.setWidth(LayoutParams.FILL_PARENT);  
        //����SelectPicPopupWindow��������ĸ�  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //����SelectPicPopupWindow��������ɵ��  
        this.setFocusable(true);
        
        //����SelectPicPopupWindow�������嶯��Ч��  
        //this.setAnimationStyle(R.style.AnimBottom);
        
        //ʵ����һ��ColorDrawable��ɫΪ��͸��  
        ColorDrawable dw = new ColorDrawable(0x55000000);  
        //����SelectPicPopupWindow��������ı���  
        this.setBackgroundDrawable(dw);  
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.hc_pop_phone_txv:
			Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + map.get("mobile")));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			break;
		case R.id.hc_pop_qq_txv:
			ClipboardManager copy = (ClipboardManager) context 
            .getSystemService(Context.CLIPBOARD_SERVICE); 
             copy.setText(map.get("qq").toString()); 
             Toast.makeText(context, "�Ѹ��Ƶ������", 2000).show();
			break;
		case R.id.hc_pop_wechat_txv:
			ClipboardManager copy1 = (ClipboardManager) context 
            .getSystemService(Context.CLIPBOARD_SERVICE); 
             copy1.setText(map.get("weixin").toString()); 
             Toast.makeText(context, "�Ѹ��Ƶ������", 2000).show();
			break;

		default:
			break;
		}
	}

}
