package com.yunjian.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.yunjian.adapter.WishAdapter;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.WishService;
import com.yunjian.util.Utils;
import com.yunjian.view.PullToRefreshView;
import com.yunjian.view.PullToRefreshView.OnFooterRefreshListener;
import com.yunjian.view.PullToRefreshView.OnHeaderRefreshListener;

public class WishActivity extends Activity implements OnHeaderRefreshListener,
		OnFooterRefreshListener, OnClickListener {
	private ImageView mainpage, wish, person;
	private PullToRefreshView mPullToRefreshView;
	private ListView listView;
	private Button productButton;
	private RadioGroup radioGroup;
	private RadioButton hottest;
	private RadioButton newest;
	private Button allbook,coursebook,english,japanese,technology,master,entertain;

	private WishAdapter adapter;
	private List<Map<String, Object>> list;
	private int page = 1;
	private String order_by = "clicks";
	private int type = 0;

	private WishService service;
	private OnQueryCompleteListener onQueryCompleteListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wish_book);
        initView();
		service = new WishService();
		onQueryCompleteListener = new OnQueryCompleteListener() {

			@Override
			public void onQueryComplete(QueryId queryId, Object result,
					EHttpError error) {
				// TODO Auto-generated method stub
				if(WishService.LISTWISH.equals(queryId)){
					if (result != null) {
						if (page == 1) {
							list = (List<Map<String, Object>>) result;
							adapter = new WishAdapter(WishActivity.this, list);
							listView.setAdapter(adapter);
						} else {
							List<Map<String, Object>> temp = (List<Map<String, Object>>) result;
							for (int i = 0; i < temp.size(); i++) {
								list.add(temp.get(i));
							}
							adapter.notifyDataSetChanged();
						}

					} else {
						Toast.makeText(WishActivity.this, "服务器连接失败", 2000).show();
					}
				}
				else if(queryId.equals(WishService.CLICKWISH)){
					System.out.println("click"+result);
				}
				
			}
		};
		// 启动后台服务
		resetService();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WishActivity.this,
						WishDetailActivity.class);
				intent.putExtra("wish_id", list.get(arg2).get("wish_id")
						.toString());
				startActivity(intent);
				
				service.clickListener(list.get(arg2).get("wish_id").toString(), onQueryCompleteListener);
			}
		});

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if (arg1 == hottest.getId()) {
					order_by = "clicks";
					page = 1;
					resetService();
				} else if (arg1 == newest.getId()) {
					order_by = "added_time";
					page = 1;
					resetService();
				}
			}
		});

		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		productButton.setOnClickListener(this);
	}

	public void resetService() {
		service.getWishes(type,String.valueOf(page), order_by,
				onQueryCompleteListener);
	}
	
	public void resetButtonColor(){
		allbook.setTextColor(Color.BLACK);
		coursebook.setTextColor(Color.BLACK);
		english.setTextColor(Color.BLACK);
		japanese.setTextColor(Color.BLACK);
		technology.setTextColor(Color.BLACK);
		master.setTextColor(Color.BLACK);
		entertain.setTextColor(Color.BLACK);
		coursebook.setBackgroundResource(R.drawable.white);
		allbook.setBackgroundResource(R.drawable.white);
		english.setBackgroundResource(R.drawable.white);
		japanese.setBackgroundResource(R.drawable.white);
		technology.setBackgroundResource(R.drawable.white);
		master.setBackgroundResource(R.drawable.white);
		entertain.setBackgroundResource(R.drawable.white);
	}
	
	public void initView()
	{
		mainpage = (ImageView) findViewById(R.id.main_books_btn);
		wish = (ImageView) findViewById(R.id.main_wishes_btn);
		person = (ImageView) findViewById(R.id.main_person_btn);

		mainpage.setOnClickListener(this);
		wish.setOnClickListener(this);
		person.setOnClickListener(this);
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		listView = (ListView) findViewById(R.id.wish_book_list);
		productButton = (Button) findViewById(R.id.wish_product_btn);
		radioGroup = (RadioGroup) findViewById(R.id.gendergroup);
		hottest = (RadioButton) findViewById(R.id.highest);
		newest = (RadioButton) findViewById(R.id.newest);
		allbook = (Button)findViewById(R.id.all);
		coursebook = (Button)findViewById(R.id.coursebook);
		english = (Button)findViewById(R.id.english);
		japanese = (Button)findViewById(R.id.japanese);
		technology = (Button)findViewById(R.id.technology);
		master = (Button)findViewById(R.id.master);
		entertain = (Button)findViewById(R.id.entertain);
		allbook.setOnClickListener(this);
		coursebook.setOnClickListener(this);
		english.setOnClickListener(this);
		japanese.setOnClickListener(this);
		technology.setOnClickListener(this);
		master.setOnClickListener(this);
		entertain.setOnClickListener(this);

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				page++;
				System.out.println("页数" + page);
				service.getWishes(type,String.valueOf(page), order_by,
						onQueryCompleteListener);
				mPullToRefreshView.onFooterRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 设置更新时间
				// mPullToRefreshView.onHeaderRefreshComplete("最近更新:01-23 12:01");
				list.clear();
				page = 1;
				service.getWishes(type,String.valueOf(page), order_by,
						onQueryCompleteListener);
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.wish_product_btn:
			if(Utils.user_id.equals("")){
				Intent intent3 = new Intent(WishActivity.this,LoginActivity.class);
				startActivity(intent3);
			}
			else {
				Intent intent = new Intent(WishActivity.this, AddWishActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.main_books_btn:
			Intent intent1 = new Intent(WishActivity.this,
					MainPageActivity.class);
			finish();
			startActivity(intent1);
			break;
		case R.id.main_wishes_btn:
			break;
		case R.id.main_person_btn:
			if(Utils.user_id.equals("")){
				Intent intent3 = new Intent(WishActivity.this,LoginActivity.class);
				startActivity(intent3);
			}
			else {
				Intent intent3 = new Intent(WishActivity.this,PersonActivity.class);
				startActivity(intent3);
				finish();
			}
			break;
		case R.id.all:
			type = 0;
			page = 1;
			resetService();
			resetButtonColor();
			allbook.setTextColor(this.getResources().getColor(R.color.seagreen));
			allbook.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		case R.id.coursebook:
			type = 1;
			page = 1;
			resetService();
			resetButtonColor();
			coursebook.setTextColor(this.getResources().getColor(R.color.seagreen));
			coursebook.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		case R.id.english:
			type = 2;
			page = 1;
			resetService();
			resetButtonColor();
			english.setTextColor(this.getResources().getColor(R.color.seagreen));
			english.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		case R.id.japanese:
			type = 3;
			page = 1;
			resetService();
			resetButtonColor();
			japanese.setTextColor(this.getResources().getColor(R.color.seagreen));
			japanese.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		case R.id.technology:
			type = 4;
			page = 1;
			resetService();
			resetButtonColor();
			technology.setTextColor(this.getResources().getColor(R.color.seagreen));
			technology.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		case R.id.master:
			type = 5;
			page = 1;
			resetService();
			resetButtonColor();
			master.setTextColor(this.getResources().getColor(R.color.seagreen));
			master.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		case R.id.entertain:
			type = 6;
			page = 1;
			resetService();
			resetButtonColor();
			entertain.setTextColor(this.getResources().getColor(R.color.seagreen));
			entertain.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		default:
			break;
		}
	}
}
