package com.yunjian.activity;

import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.fragment.DealedGoods;
import com.yunjian.fragment.MyWishes;
import com.yunjian.image.AsyncImageLoader;
import com.yunjian.image.AsyncImageLoader.ImageCallback;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.UserManageService;
import com.yunjian.util.SerializableMap;
import com.yunjian.util.Utils;
import com.yunjian.view.CircleImageView;

public class PersonActivity extends Activity implements OnClickListener,ImageCallback{
	private ImageView mainpage,wish,person;
	private ImageView messageImageView;
	private ImageView settingImageView;
	private ImageView editImageView;
	private CircleImageView photoImageView;
	private Button goodsButton;
	private Button wishesButton;
	private TextView nickTextView;
	private TextView phoneTextView;
	private TextView qqTextView;
	private ImageView sexImageView;
	
	private Intent intent;
	private FragmentManager fManager;
	private FragmentTransaction fTransaction;
	private Fragment goodFragment,wishFragment;
	
	private UserManageService userManageService;
	private OnQueryCompleteListener onQueryCompleteListener;
	private AsyncImageLoader imageLoader;
	private Map<String, Object>map;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personcenter);
		if(Utils.user_id.equals("")){
			Intent intent = new Intent(PersonActivity.this,LoginActivity.class);
			startActivity(intent);
		}
		else {
			initView();
			getFragment();
			fManager.beginTransaction().add(R.id.list_ll, goodFragment).commit();
			goodsButton.setBackgroundResource(R.drawable.pc_btn_pressed);
		}
		
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.message_btn:
			intent.setClass(PersonActivity.this,MessageCenter.class);
			startActivity(intent);
			break;
		case R.id.setting_btn:
			intent.setClass(PersonActivity.this,SettingCenter.class);
			startActivity(intent);
			break;
		case R.id.goods:
			if(!goodFragment.isAdded()){
				fTransaction = fManager.beginTransaction();
				fTransaction.replace(R.id.list_ll, goodFragment);
				fTransaction.commit();
			}
			resetBunttonColor();
			goodsButton.setBackgroundResource(R.drawable.pc_btn_pressed);
			break;
		case R.id.wishes:
			if(!wishFragment.isAdded()){
				fTransaction = fManager.beginTransaction();
				fTransaction.replace(R.id.list_ll, wishFragment);
				fTransaction.commit();
			}
			resetBunttonColor();
			wishesButton.setBackgroundResource(R.drawable.pc_btn_pressed);
			break;
		case R.id.edit_img:
			intent.setClass(PersonActivity.this, EditPersonCenter.class);
			Utils.IFEDITPERSON = 1;
			Bundle bundle = new Bundle();
			SerializableMap tmpmap=new SerializableMap();  
            tmpmap.setMap(map);  
            bundle.putSerializable("personinfo", tmpmap);  
            intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.main_books_btn:
			Intent intent3 = new Intent(PersonActivity.this,MainPageActivity.class);
			finish();
			startActivity(intent3);
			break;
		case R.id.main_wishes_btn:
			Intent intent2 = new Intent(PersonActivity.this,WishActivity.class);
			finish();
			startActivity(intent2);		
			break;
		case R.id.main_person_btn:
			break;
		default:
			break;
		}
	}
	
	public void getFragment(){
		wishFragment = new MyWishes();
		goodFragment = new DealedGoods();
	}
	
	public void resetBunttonColor(){
		goodsButton.setBackgroundResource(R.drawable.pc_btn_unpressed);
		wishesButton.setBackgroundResource(R.drawable.pc_btn_unpressed);
	}
	
	public void initView(){
		messageImageView = (ImageView)findViewById(R.id.message_btn);
		settingImageView = (ImageView)findViewById(R.id.setting_btn);
		editImageView = (ImageView)findViewById(R.id.edit_img);
		photoImageView = (CircleImageView)findViewById(R.id.user_icon);
		goodsButton = (Button)findViewById(R.id.goods);
		wishesButton = (Button)findViewById(R.id.wishes);
		nickTextView = (TextView)findViewById(R.id.nick_txv);
		sexImageView = (ImageView)findViewById(R.id.sex_img);
		phoneTextView = (TextView)findViewById(R.id.phone_txv);
		qqTextView = (TextView)findViewById(R.id.qq_txv);
		intent = new Intent();
		fManager = getFragmentManager();
		mainpage = (ImageView)findViewById(R.id.main_books_btn);
		wish = (ImageView)findViewById(R.id.main_wishes_btn);
		person = (ImageView)findViewById(R.id.main_person_btn);
		
		mainpage.setOnClickListener(this);
		wish.setOnClickListener(this);
		person.setOnClickListener(this);
		
		messageImageView.setOnClickListener(this);
		settingImageView.setOnClickListener(this);
		editImageView.setOnClickListener(this);
		goodsButton.setOnClickListener(this);
		wishesButton.setOnClickListener(this);
		
		userManageService = new UserManageService();
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				map = (Map<String, Object>) result;
				
				if(map.get("username")==null){
					nickTextView.setText("淘书者");
					map.put("username", "淘书者");
				}
				else
					nickTextView.setText(map.get("username").toString());
				if(map.get("mobile") == null){
					phoneTextView.setText("手机："+Utils.user_id);
					map.put("mobile", "");
				}
				else
					phoneTextView.setText("手机："+map.get("mobile").toString());
				if(map.get("weixin")!= null){
					qqTextView.setText("微信："+map.get("weixin").toString());
				}
				else
					map.put("weixin", "");
				if(map.get("qq")!= null)
				     qqTextView.setText("QQ："+map.get("qq").toString());
				else
					map.put("qq", "");
				if(map.get("gender").equals(2.0))
					sexImageView.setImageResource(R.drawable.pe_sex_secret_pressed);
				else if(map.get("gender").equals(0.0))
					sexImageView.setImageResource(R.drawable.pe_sex_woman_pressed);
				else if (map.get("gender").equals(1.0)) {
					sexImageView.setImageResource(R.drawable.pe_sex_man_pressed);
				}
				
			}
		};
		userManageService.getUserInfo(Utils.user_id, onQueryCompleteListener);
		imageLoader = new AsyncImageLoader();
		imageLoader.loadDrawable(Utils.URL+Utils.user_id, PersonActivity.this);
	}

	@Override
	public void imageLoaded(Drawable imageDrawable, String imageUrl) {
		// TODO Auto-generated method stub
		if(imageDrawable !=null){
			
			BitmapDrawable bd = (BitmapDrawable) imageDrawable;
			Bitmap bm = bd.getBitmap();
			photoImageView.setBackgroundResource(R.drawable.transparent);
			photoImageView.setBitmap(bm);
		}
	}


}
