package com.yunjian.activity;

import java.util.HashMap;
import java.util.Map;

import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.util.Utils;
import com.yunjian.service.WishService;
import com.yunjian.util.SerializableMap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddWishActivity extends Activity implements OnClickListener{
	private ImageView backButton;
	private ImageView okButton;
	private ImageView photoImageView;
	private EditText wishnameEditText;
	private EditText wishdescripEditText;
	private EditText phoneEditText;
	private EditText qqEditText;
	private EditText wechaText;
	private Button coursebook,english,japanese,technology,master,entertain;
	
	private WishService wishService;
	private OnQueryCompleteListener onQueryCompleteListener;
	private String wishId = "";
	private int type  = -1;
	private Map<String, Object> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_wish);
		if(Utils.IFEDITWISH==1){
			Bundle bundle = getIntent().getExtras();  
	        SerializableMap serMap = (SerializableMap) bundle  
	                .get("wishinfo");
		    map = serMap.getMap();
		}
		initView();
	}
	public void initView(){
		backButton = (ImageView)findViewById(R.id.addwish_back_img);
		okButton = (ImageView)findViewById(R.id.addwish_ok_img);
		photoImageView = (ImageView)findViewById(R.id.addwish_takephoto);
		wishnameEditText = (EditText)findViewById(R.id.addwish_name);
		wishdescripEditText = (EditText)findViewById(R.id.addwish_description);
		phoneEditText = (EditText)findViewById(R.id.addwish_phone);
		qqEditText = (EditText)findViewById(R.id.addwish_qq);
		wechaText = (EditText)findViewById(R.id.addwish_wechat);
		coursebook = (Button)findViewById(R.id.addwish_coursebook);
		english = (Button)findViewById(R.id.addwish_english);
		japanese = (Button)findViewById(R.id.addwish_japanese);
		technology = (Button)findViewById(R.id.addwish_technology);
		master = (Button)findViewById(R.id.addwish_master);
		entertain = (Button)findViewById(R.id.addwish_entertain);
		
		backButton.setOnClickListener(this);
		okButton.setOnClickListener(this);
		photoImageView.setOnClickListener(this);
		coursebook.setOnClickListener(this);
		english.setOnClickListener(this);
		japanese.setOnClickListener(this);
		technology.setOnClickListener(this);
		master.setOnClickListener(this);
		entertain.setOnClickListener(this);
		
		if(map!=null){
			wishnameEditText.setText(map.get("bookname").toString());
			wishdescripEditText.setText(map.get("description").toString());
			phoneEditText.setText(map.get("mobile").toString());
			qqEditText.setText(map.get("qq").toString());
			wechaText.setText(map.get("weixin").toString());
			wishId = map.get("wish_id").toString();
		}
		
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result.equals(null)){
					Toast.makeText(AddWishActivity.this, "网络连接超时", 2000).show();
				}
				else if(result.equals("success")){
					Toast.makeText(AddWishActivity.this, "发布成功", 2000).show();
					Utils.IFEDITWISH = 0;
					finish();
				}
				else {
					Toast.makeText(AddWishActivity.this, "发布失败", 2000).show();
				}
			}
		};
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.addwish_back_img:
			finish();
			break;
		case R.id.addwish_ok_img:
			String wishname = wishnameEditText.getText().toString();
			String wishdescrip = wishdescripEditText.getText().toString();
			String phone = phoneEditText.getText().toString();
			String qq = qqEditText.getText().toString();
			String wechat = wechaText.getText().toString();
			if(wishname.equals("")||wishdescrip.equals("")){
				Toast.makeText(AddWishActivity.this, "所填信息不完整呢", 2000).show();
			}
			else if (type == -1) {
				Toast.makeText(AddWishActivity.this, "为你的心愿单选个分类吧", 2000).show();
			}
			else if(phone.equals("")){
				Toast.makeText(AddWishActivity.this, "大侠，留下你的电话吧", 2000).show();
			}
			else {
				Map<String, Object>map = new HashMap<String, Object>();
				map.put("user_id", Utils.user_id);
				map.put("username", Utils.username);
				map.put("wish_id", wishId);
				map.put("bookname", wishname);
				map.put("description", wishdescrip);
				map.put("mobile", phone);
				map.put("qq", qq);
				map.put("wexin", wechat);
				map.put("type", type);
				wishService = new WishService();
				wishService.addWish(map, onQueryCompleteListener);
			}
			break;
		case R.id.addwish_takephoto:
			
			break;
		case R.id.addwish_coursebook:
		    type = 1;	
		    resetButtonColor();
		    coursebook.setBackgroundResource(R.drawable.addwish_btn_pressed);
			break;
		case R.id.addwish_english:
			type = 2;
			resetButtonColor();
			 english.setBackgroundResource(R.drawable.addwish_btn_pressed);
			break;
		case R.id.addwish_japanese:
			type = 3;
			resetButtonColor();
			 japanese.setBackgroundResource(R.drawable.addwish_btn_pressed);
			break;
		case R.id.addwish_technology:
			type = 4;
			resetButtonColor();
			 technology.setBackgroundResource(R.drawable.addwish_btn_pressed);
			break;
		case R.id.addwish_master:
			type = 5;
			resetButtonColor();
			 master.setBackgroundResource(R.drawable.addwish_btn_pressed);
			break;
		case R.id.addwish_entertain:
			type = 6;
			resetButtonColor();
			 entertain.setBackgroundResource(R.drawable.addwish_btn_pressed);
			break;

		default:
			break;
		}
	}
	
	public void resetButtonColor(){
		coursebook.setTextColor(Color.BLACK);
		english.setTextColor(Color.BLACK);
		japanese.setTextColor(Color.BLACK);
		technology.setTextColor(Color.BLACK);
		master.setTextColor(Color.BLACK);
		entertain.setTextColor(Color.BLACK);
		coursebook.setBackgroundResource(R.drawable.addwish_btn_unpressed);
		english.setBackgroundResource(R.drawable.addwish_btn_unpressed);
		japanese.setBackgroundResource(R.drawable.addwish_btn_unpressed);
		technology.setBackgroundResource(R.drawable.addwish_btn_unpressed);
		master.setBackgroundResource(R.drawable.addwish_btn_unpressed);
		entertain.setBackgroundResource(R.drawable.addwish_btn_unpressed);
	}

}
