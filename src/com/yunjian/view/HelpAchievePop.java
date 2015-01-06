package com.yunjian.view;


import java.util.Map;

import com.yunjian.activity.R;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.image.ImageLoader;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.UserCenterService;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class HelpAchievePop extends PopupWindow implements OnClickListener,OnQueryCompleteListener{
	private TextView tellTextView,qqTextView,wechaTextView;
	private Button smsButton,callButton;
	private ImageView photoImageView;
	private TextView username;
	private Map<String, Object>map;
	private View view; 
	private ImageLoader mImageLoader;
	private Context context;
	public HelpAchievePop(Context context,Map<String, Object>map){
		super(context);  
		this.context = context;
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        view = inflater.inflate(R.layout.help_achieve_pop, null);  
        this.map = map;
        mImageLoader = ImageLoader.getInstance(context);
        initView(view);
      //设置SelectPicPopupWindow的View  
        this.setContentView(view);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.FILL_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0x55000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);  
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.hc_pop_sms_btn:
			Intent sendIntent = new Intent(Intent.ACTION_SENDTO);  
		    sendIntent.setData(Uri.parse("smsto:" + map.get("mobile")));  
		    sendIntent.putExtra("sms_body", "你好，同学，你的心愿单我可以帮你实现喔");  
		    context.startActivity(sendIntent); 
			break;
		case R.id.hc_pop_call_btn:
			Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + map.get("mobile")));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			break;
		case R.id.hc_pop_qq_txv:
			ClipboardManager copy = (ClipboardManager) context 
            .getSystemService(Context.CLIPBOARD_SERVICE); 
             copy.setText(map.get("qq").toString()); 
             Toast.makeText(context, "已复制到黏贴板", 2000).show();
			break;
		case R.id.hc_pop_wechat_txv:
			ClipboardManager copy1 = (ClipboardManager) context 
            .getSystemService(Context.CLIPBOARD_SERVICE); 
             copy1.setText(map.get("weixin").toString()); 
             Toast.makeText(context, "已复制到黏贴板", 2000).show();
			break;
		case R.id.hc_pop_tell_txv:
			new UserCenterService().setWishStatus(Utils.user_id, Utils.username, map.get("wish_id").toString(), 2, this);
			break;

		default:
			break;
		}
	}
	
	
	public void initView(View view){
		tellTextView = (TextView) view.findViewById(R.id.hc_pop_tell_txv);
        qqTextView = (TextView) view.findViewById(R.id.hc_pop_qq_txv);  
        wechaTextView = (TextView) view.findViewById(R.id.hc_pop_wechat_txv);  
        username = (TextView) view.findViewById(R.id.hc_pop_user_name);  
        smsButton = (Button)view.findViewById(R.id.hc_pop_sms_btn);
        callButton = (Button)view.findViewById(R.id.hc_pop_call_btn);
        photoImageView = (ImageView)view.findViewById(R.id.hc_pop_user_img);
        smsButton.setOnClickListener(this);
        callButton.setOnClickListener(this);
        tellTextView.setOnClickListener(this);
        qqTextView.setOnClickListener(this);
        wechaTextView.setOnClickListener(this);
        
		username.setText(Utils.username);
		qqTextView.setText("QQ:"+map.get("qq").toString());
		wechaTextView.setText("微信:"+map.get("weixin").toString());
		//图片加载
		try {
			mImageLoader.addTask(Utils.URL+Utils.user_id, photoImageView);
		} catch (Exception e) {
		// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		if(result.equals("success")){
			Toast.makeText(context, "已成功通知对方，耐心等待他联系你吧", 2000).show();
			dismiss();
		}
		else {
			Toast.makeText(context, "好像出错了呢，再试一次吧", 2000).show();
		}
			
	}

}
