package com.yunjian.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.yunjian.adapter.BookAdapter;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.service.BookService;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.util.Utils;
import com.yunjian.view.MyGridView;
import com.yunjian.view.MyScrollView;
import com.yunjian.view.PullToRefreshView;
import com.yunjian.view.ScrollListener;
import com.yunjian.view.PullToRefreshView.OnFooterRefreshListener;
import com.yunjian.view.PullToRefreshView.OnHeaderRefreshListener;

public class MainPageActivity extends Activity implements
OnHeaderRefreshListener, OnFooterRefreshListener,ScrollListener,OnClickListener
{ 
	private ImageView mainpage,wish,person;
	private MyScrollView myScrollView;
	private LinearLayout mainPageDown;
	private LinearLayout mainPageUp;
	private LinearLayout middleLayout;
	private LinearLayout middle_textGone;
	private boolean upDown = true;
	 private PullToRefreshView mPullToRefreshView;
	 private TextView middleText;
	 private TextView middleTextGone;
	 private LinearLayout topSelect1;
	 private LinearLayout topSelect2;
	 private LinearLayout topSelect3;
	 private LinearLayout topSelect4;
	 private LinearLayout topSelect5;
	 private LinearLayout topSelect6;
	 private TextView editText;
	 private LinearLayout mainPageSelect;
     private MyGridView gridView; 
     private Button addBookButton;
     private RadioGroup radioGroup,radioGroup1;
     private RadioButton highest,highest1;
     private RadioButton newest,newest1;
     
     private int mainPageSelectHigh,middleHigh;
     private int page = 1;
     //书籍分类
     private String type = "1";
     private String order_by  = "added_time";
     
     private BookService service;
     private OnQueryCompleteListener onQueryCompleteListener;
     private List<Map<String, Object>>booklist;   
     private BookAdapter bookAdapter;
     
     
 
     @Override
 	protected void onCreate(Bundle savedInstanceState) {
 		// TODO Auto-generated method stub
    	 super.onCreate(savedInstanceState);
 		setContentView(R.layout.main_page);
 		SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_WORLD_READABLE);
 		if(sharedPreferences.getString("user_id", null)!= null){
 			System.out.println(sharedPreferences.getString("user_id", null));
 			Utils.user_id = sharedPreferences.getString("user_id", null);
 			Utils.password = sharedPreferences.getString("password", null);
 			Utils.username = sharedPreferences.getString("password", "匿名用户");
 		}
 		initView();
        initView2();
        
        gridView.setOnItemClickListener(new OnItemClickListener() 
            { 
                @Override
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
                { 
                	Intent intent = new Intent(MainPageActivity.this,BookDetailActivity.class);
                	intent.putExtra("bookname", booklist.get(position).get("bookname").toString());
                    startActivity(intent);
                }
               
            });
        
        mainPageDown.setClickable(true);
        mainPageUp.setClickable(true);
        
        myScrollView.smoothScrollTo(0,0);
        editText.setClickable(true);
        editText.setOnClickListener(this);
	}
    
    public void initView(){
    	mainpage = (ImageView)findViewById(R.id.main_books_btn);
		wish = (ImageView)findViewById(R.id.main_wishes_btn);
		person = (ImageView)findViewById(R.id.main_person_btn);
		
		mainpage.setOnClickListener(this);
		wish.setOnClickListener(this);
		person.setOnClickListener(this);
    	addBookButton = (Button)findViewById(R.id.add_newbook_btn);
    	myScrollView = (MyScrollView)findViewById(R.id.main_page_scrollView);
        mainPageUp = (LinearLayout)findViewById(R.id.main_page_up);
        mainPageDown = (LinearLayout)findViewById(R.id.main_page_down);
        mPullToRefreshView = (PullToRefreshView)findViewById(R.id.main_pull_refresh_view);
        middleLayout = (LinearLayout)findViewById(R.id.middle_layout);
        middle_textGone = (LinearLayout)findViewById(R.id.middle_layout_gone);
        mainPageSelect = (LinearLayout)findViewById(R.id.main_page_select); 
        gridView = (MyGridView)findViewById(R.id.gridview);	
        editText = (TextView)findViewById(R.id.et);
        middleText = (TextView)findViewById(R.id.middle_text);
        middleTextGone = (TextView)findViewById(R.id.middle_text_gone);
        
        radioGroup = (RadioGroup)findViewById(R.id.gendergroup);
        highest = (RadioButton)findViewById(R.id.highest);
        newest = (RadioButton)findViewById(R.id.newest);
        radioGroup1 = (RadioGroup)findViewById(R.id.gendergroup1);
        highest1 = (RadioButton)findViewById(R.id.highest1);
        newest1 = (RadioButton)findViewById(R.id.newest1);
        
    	addBookButton.setOnClickListener(this);
    	mPullToRefreshView.setOnHeaderRefreshListener(this);
 		mPullToRefreshView.setOnFooterRefreshListener(this);
 		myScrollView.setScrollListener(this);
 		mainPageDown.setOnClickListener(this);
 		mainPageUp.setOnClickListener(this);
 		
 		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if(arg1 == highest.getId()){
					order_by = "clicks";
					page = 1;
					resetService();
				}
				else if(arg1 == newest.getId()){
					order_by = "added_time";
					page = 1;
					resetService();
				}
			}
		});
		radioGroup1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						if(arg1 == highest1.getId()){
							order_by = "clicks";
							page = 1;
							resetService();
						}
						else if(arg1 == newest1.getId()){
							order_by = "added_time";
							page = 1;
							resetService();
						}
					}
				});
 		
 		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result !=null){
					
					if(page == 1 ){
						booklist = (List<Map<String, Object>>) result;
//						Gson gson = new Gson();
//						System.out.println("map===================="+gson.toJson(booklist));
//						
//						SharedPreferences sharedPreferences = getSharedPreferences("main_page_list", MODE_PRIVATE);
//						Editor editor = sharedPreferences.edit();
//						String json = gson.toJson(booklist);
//						System.out.println("json===================="+gson.toJson(json));
//						
//						editor.putString("main_page_list", json);
//						editor.commit();
//						
//						SharedPreferences sp = getSharedPreferences(
//								"main_page_list", MODE_PRIVATE);
//						String book = sp.getString("main_page_list", "");
//						System.out.println("book========"+book);
//						System.out.println("booklist============"+booklist.toString());
						bookAdapter = new BookAdapter(MainPageActivity.this, booklist);
//						int totalHeight = 0;    
//				        for (int i = 0; i < bookAdapter.getCount(); i++) {    
//				            View listItem = bookAdapter.getView(i, null, gridView);    
//				            listItem.measure(0, 0);    
//				            totalHeight += listItem.getMeasuredHeight();    
//				        }    
//				        ViewGroup.LayoutParams params = gridView.getLayoutParams();    
//				        params.height = totalHeight + (gridView.getHeight() * (bookAdapter.getCount()- 1));
//				        params.height = params.height/3;
//				        params.height += 55;//if without this statement,the listview will be a little short    
				        
						gridView.setAdapter(bookAdapter);
					}
					else {
						List<Map<String, Object>>temp = (List<Map<String, Object>>) result;
						for(int i=0;i<temp.size();i++){
							booklist.add(temp.get(i));
						}
					     bookAdapter.notifyDataSetChanged();
					}
				}
			}
		};
		
		service = new BookService();
 		resetService();
    }
    
    public void initView2(){
    	 topSelect1 = (LinearLayout)findViewById(R.id.top_select_1);
         topSelect2 = (LinearLayout)findViewById(R.id.top_select_2);
         topSelect3 = (LinearLayout)findViewById(R.id.top_select_3);
         topSelect4 = (LinearLayout)findViewById(R.id.top_select_4);
         topSelect5 = (LinearLayout)findViewById(R.id.top_select_5);
         topSelect6 = (LinearLayout)findViewById(R.id.top_select_6);
         
         topSelect1.setOnClickListener(this);
         topSelect2.setOnClickListener(this);
         topSelect3.setOnClickListener(this);
         topSelect4.setOnClickListener(this);
         topSelect5.setOnClickListener(this);
         topSelect6.setOnClickListener(this);
    }
    
    public void resetService(){
    	service.getBooksByType(type, order_by, page+"", onQueryCompleteListener);
    }

    @Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				page++;
				System.out.println("页数"+page);
				service.getBooksByType(type, order_by, page+"", onQueryCompleteListener);
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
				//mPullToRefreshView.onHeaderRefreshComplete("最近更新:01-23 12:01");
				booklist.clear();
				page = 1;
				service.getBooksByType(type, order_by, page+"", onQueryCompleteListener);
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);

	}

    @Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		mainPageSelectHigh = mainPageSelect.getHeight();
		middleHigh = middleLayout.getHeight()+mainPageUp.getHeight();
	}
    
	@Override
	public void ScrollChanged(int x, int y) {
		// TODO Auto-generated method stub
		if(y >= mainPageSelectHigh)
		{
//			front.setVisibility(View.VISIBLE);
			if(upDown){
				upDown = false;	
				mainPageUp.setVisibility(View.GONE);
				mainPageSelect.setVisibility(View.GONE);
				mainPageDown.setVisibility(View.VISIBLE);
				middle_textGone.setVisibility(View.VISIBLE);
				middleLayout.setVisibility(View.GONE);
				myScrollView.scrollTo(0,0);
			}
		}
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.add_newbook_btn:
			if(Utils.user_id.equals("")){
				Intent intent3 = new Intent(MainPageActivity.this,LoginActivity.class);
				startActivity(intent3);
			}
			else {
				Utils.IFEDITBOOK = 0;
				Intent intent = new Intent(MainPageActivity.this,AddBookActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.main_page_down:
			mainPageDown.setVisibility(View.GONE);
			mainPageSelect.setVisibility(View.VISIBLE);
			mainPageUp.setVisibility(View.VISIBLE);
			middle_textGone.setVisibility(View.GONE);
			middleLayout.setVisibility(View.VISIBLE);
			upDown=true;
			break;
		case R.id.main_page_up:
			mainPageDown.setVisibility(View.VISIBLE);
			mainPageSelect.setVisibility(View.GONE);
			mainPageUp.setVisibility(View.GONE);				
			middle_textGone.setVisibility(View.VISIBLE);
			middleLayout.setVisibility(View.GONE);
			upDown=false;
			break;
		case R.id.top_select_1:
			type = "1";
			middleText.setText("教材资料");
			middleTextGone.setText("教材资料");
			resetService();
			break;
		case R.id.top_select_2:
			type = "2";
			middleText.setText("英语专区");
			middleTextGone.setText("英语专区");
			resetService();
			break;
		case R.id.top_select_3:
			type = "3";
			middleText.setText("日语专区");
			middleTextGone.setText("日语专区");
			resetService();
			break;
		case R.id.top_select_4:
			type = "4";
			middleText.setText("技术养成");
			middleTextGone.setText("技术养成");
			resetService();
			break;
		case R.id.top_select_5:
			type = "5";
			middleText.setText("考研相关");
			middleTextGone.setText("考研相关");
			resetService();
			break;
		case R.id.top_select_6:
			type = "6";
			middleText.setText("休闲阅读");
			middleTextGone.setText("休闲阅读");
			resetService();
			break;
		case R.id.main_books_btn:
			break;
		case R.id.main_wishes_btn:
			Intent intent2 = new Intent(MainPageActivity.this,WishActivity.class);
			finish();
			startActivity(intent2);		
			break;
		case R.id.main_person_btn:
			if(Utils.user_id.equals("")){
				Intent intent3 = new Intent(MainPageActivity.this,LoginActivity.class);
				startActivity(intent3);
			}
			else {
				Intent intent3 = new Intent(MainPageActivity.this,PersonActivity.class);
				startActivity(intent3);
				finish();
			}
			break;
		case R.id.et:
			Intent intent1 = new Intent(MainPageActivity.this,SearchResult.class);
			startActivity(intent1);
		default:
			break;
		}
	}
}
