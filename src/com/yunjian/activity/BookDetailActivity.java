package com.yunjian.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunjian.adapter.BookDetailCommentAdapter;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.image.AsyncImageLoader;
import com.yunjian.image.ImageLoader;
import com.yunjian.service.BookService;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.UserManageService;
import com.yunjian.service.WishService;
import com.yunjian.util.Utils;
import com.yunjian.view.ConnectSellerPopwindow;
import com.yunjian.view.GestureListener;
import com.yunjian.view.InputPopwindow;
import com.yunjian.view.MyScrollView;
import com.yunjian.view.NoScrollListView;
import com.yunjian.view.ScrollListener;



public class BookDetailActivity extends Activity implements ScrollListener,OnClickListener {

	private LinearLayout front,middle;
	private TextView title, readTime,publishDays,price,
	userName,userTel,userQQ,userWinxin,basicCondition,suitCrowd,
	myEvaluation,showAll;
	private ImageView userImage;
	private LinearLayout llUserTel,llUserQQ,llUserWeChat;
	private MyScrollView bookDetailScrollView;
	private RelativeLayout header;
	private ImageButton back;
	private ImageView bookDetailImage1,bookDetailImage2,bookDetailImage3;
	private Button nextSeller,frontSeller;
	private NoScrollListView comment;
	private ImageView bottomComment,bottomConnect,bottomShare;
	private int headerHight, middleHight;
	private Boolean showFlag = true,showAllFlag=true;
	private Boolean first = true;
	private List<Map<String, Object>> list;
	private List<Map<String, Object>> commentlist;
	private Map<String, Object>map;
	private int curPage=0,maxPage=0;
	private InputMethodManager imm;
	private Handler mHandler;
	private LinearLayout commentll;
	private EditText commenteditText;
	private Button sendButton;
	private OnQueryCompleteListener onQueryCompleteListener;
	private ImageLoader imageLoader;
	private AsyncImageLoader pimageLoader;
	private UserManageService userManageService;
	private BookDetailCommentAdapter bookDetailCommentAdapter; 
	private BookService service;
	private String bookname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.book_detail);

		imageLoader = ImageLoader.getInstance(this);
				
				
		initView();
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		mHandler = new Handler();
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result!=null){
					if(queryId.equals(BookService.GETBOOKBYNAME)){
						list = (List<Map<String, Object>>) result;
						getInfomation();
					}
					else if(queryId.equals(WishService.GETCOMMENT)){
						commentlist = (List<Map<String, Object>>) result;

						System.out.println("result==============="+result.toString());
						bookDetailCommentAdapter = new BookDetailCommentAdapter(BookDetailActivity.this,commentlist);
//						SimpleAdapter adapter = new SimpleAdapter(BookDetailActivity.this, commentlist,
//								R.layout.book_detail_comment, new String[] { "username", "content",
//										"img" }, new int[] { R.id.title, R.id.info, R.id.img });

						comment.setAdapter(bookDetailCommentAdapter);
					}
					else if(queryId.equals(WishService.MAKECOMMENT)){
						if(result.equals("success")){
							Toast.makeText(BookDetailActivity.this, "评论成功", 2000).show();
//							commentlist.clear();
//							service.getBooksByName(bookname, onQueryCompleteListener);
//							System.out.println("133333333333");
							//bookDetailCommentAdapter.notifyDataSetChanged();
						}
						else {
							Toast.makeText(BookDetailActivity.this, "评论失败", 2000).show();
						}
					}
				}
			}
		};
		
		
		
		service = new BookService();
		Intent intent = getIntent();
		bookname = intent.getStringExtra("bookname");
		service.getBooksByName(bookname, onQueryCompleteListener);
		
		

	}

	private void getInfomation()  {
		// TODO Auto-generated method stub
		maxPage = list.size()-1;
		
		System.out.println("maxPage=============="+maxPage);

		System.out.println("curPage=============="+curPage);
		System.out.println("list===================="+list);
		
		title.setText((list.get(curPage).get("bookname")).toString());
		getDays();
		publishDays.setText(getDays()+"天前发布");
		price.setText(list.get(curPage).get("price").toString());
		userTel.setText(list.get(curPage).get("mobile").toString());
		readTime.setText(list.get(curPage).get("clicks").toString());
		
		map = list.get(curPage);
		new WishService().getWishComment(list.get(curPage).get("book_id").toString(), "1", onQueryCompleteListener);
		if(list.get(curPage).get("qq").toString().equals("")){
			llUserQQ.setVisibility(View.GONE);
		}else{
			llUserQQ.setVisibility(View.VISIBLE);
			userQQ.setText(list.get(curPage).get("qq").toString());
		}
		if(list.get(curPage).get("weixin").toString().equals("") ||list.get(curPage).get("weixin").toString().equals(null) ){
			llUserWeChat.setVisibility(View.GONE);
		}else{
			llUserWeChat.setVisibility(View.VISIBLE);
			userWinxin.setText(list.get(curPage).get("weixin").toString());
		}
		
		userName.setText(list.get(curPage).get("username").toString());
		
		basicCondition.setText(list.get(curPage).get("newness").toString());
		suitCrowd.setText(list.get(curPage).get("audience").toString());
		myEvaluation.setText(list.get(curPage).get("description").toString());
//		System.out.println("imgs====="+list.get(curPage).get("imgs").toString().substring(1,37));
//		System.out.println("imgs====="+list.get(curPage).get("imgs").toString().substring(39,75));
//		System.out.println("imgs====="+list.get(curPage).get("imgs").toString().substring(77,113));
		System.out.println("list========================="+list.get(curPage).get("imgs"));
		int length = list.get(curPage).get("imgs").toString().length();
		if(length<40){
			imageLoader.addTask(Utils.IMGURL+list.get(curPage).get("imgs").toString().substring(1,37), bookDetailImage1);
		}
		else if(length<80){
			imageLoader.addTask(Utils.IMGURL+list.get(curPage).get("imgs").toString().substring(1,37), bookDetailImage1);
			imageLoader.addTask(Utils.IMGURL+list.get(curPage).get("imgs").toString().substring(39,75), bookDetailImage2);
		}
		else{
			imageLoader.addTask(Utils.IMGURL+list.get(curPage).get("imgs").toString().substring(1,37), bookDetailImage1);
			imageLoader.addTask(Utils.IMGURL+list.get(curPage).get("imgs").toString().substring(39,75), bookDetailImage2);
			imageLoader.addTask(Utils.IMGURL+list.get(curPage).get("imgs").toString().substring(77,113), bookDetailImage3);	
		}
		imageLoader.addTask(Utils.IMGURL+list.get(curPage).get("imgs"), bookDetailImage2);
//		imageLoader.addTask(Utils.IMGURL+list.get(curPage).get("imgs"), bookDetailImage3);
		imageLoader.addTask(Utils.URL+list.get(curPage).get("user_id").toString(), userImage);
		
	}

	private int getDays() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date adddate = null;
		try {
			adddate = sdf.parse(list.get(curPage).get("added_time").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Date curDate = new Date(System.currentTimeMillis());//閼惧嘲褰囪ぐ鎾冲閺冨爼妫�

		Calendar cal0 = Calendar.getInstance();    
		cal0.setTime(adddate);
		Calendar cal1 = Calendar.getInstance();    
		cal1.setTime(curDate);    
		long time0 = cal0.getTimeInMillis(); 
		long time1 = cal1.getTimeInMillis(); 
		int days = (int) ((time1-time0)/(1000*3600*24));
		return days;
	}

	private void initView() {
		// TODO Auto-generated method stub
		middle = (LinearLayout)findViewById(R.id.book_detail_middle_layout);
		
				
		title = (TextView)findViewById(R.id.book_detail_title);
		readTime = (TextView)findViewById(R.id.book_detail_read_time);
		publishDays = (TextView)findViewById(R.id.book_detail_publish_days);
		price = (TextView)findViewById(R.id.book_detail_price);
		userImage = (ImageView)findViewById(R.id.book_detail_user_image);
		userName = (TextView)findViewById(R.id.book_detail_user_name);
		userTel = (TextView)findViewById(R.id.book_detail_user_tel);
		userQQ = (TextView)findViewById(R.id.book_detail_user_QQ);
		userWinxin = (TextView)findViewById(R.id.book_detail_user_weixin);
		basicCondition = (TextView)findViewById(R.id.book_detail_basic_condition);
		suitCrowd = (TextView)findViewById(R.id.book_detail_suit_crowd);
		myEvaluation = (TextView)findViewById(R.id.book_detail_my_evaluation);
		showAll = (TextView)findViewById(R.id.book_detail_show_all);
		
		llUserTel = (LinearLayout)findViewById(R.id.ll_book_user_tel);
		llUserQQ = (LinearLayout)findViewById(R.id.ll_book_user_qq);
		llUserWeChat = (LinearLayout)findViewById(R.id.ll_book_user_wechat);
		
		bookDetailScrollView = (MyScrollView)findViewById(R.id.book_detail_scrollView);
		commentll = (LinearLayout)findViewById(R.id.comment_ll);
		commenteditText = (EditText)findViewById(R.id.comment_et);
		sendButton = (Button)findViewById(R.id.comment_send_btn);
		header = (RelativeLayout)findViewById(R.id.header);
		
		back = (ImageButton)findViewById(R.id.bt_detail_back);
		
		bookDetailImage1 = (ImageView)findViewById(R.id.book_detail_image_1);
		bookDetailImage2 = (ImageView)findViewById(R.id.book_detail_image_2);
		bookDetailImage3 = (ImageView)findViewById(R.id.book_detail_image_3);
		
		nextSeller = (Button)findViewById(R.id.next_seller);
		frontSeller = (Button)findViewById(R.id.front_seller);
		
		comment = (NoScrollListView)findViewById(R.id.book_detail_comment);
		
		bottomComment = (ImageView)findViewById(R.id.book_detail_bottom_comment);
		bottomConnect = (ImageView)findViewById(R.id.book_detail_bottom_connect);
		bottomShare = (ImageView)findViewById(R.id.book_detail_bottom_share);
		back.setOnClickListener(this);
		showAll.setClickable(true);
		showAll.setOnClickListener(this);
		bottomComment.setClickable(true);
		bottomConnect.setClickable(true);
		bottomShare.setClickable(true);
		header.setLongClickable(true);
		header.setOnTouchListener(new MyGestureListener(this));
		nextSeller.setOnClickListener(this);
		frontSeller.setOnClickListener(this);
		bottomComment.setOnClickListener(this);
		bottomConnect.setOnClickListener(this);
		bottomShare.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		bookDetailScrollView.setScrollListener(this);
		bookDetailScrollView.smoothScrollTo(0, 0);

	}

	public class MyGestureListener extends GestureListener {

		public MyGestureListener(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return super.onTouch(v, event);
		}

		@Override
		public boolean left() {
			Bitmap bitmap = ((BitmapDrawable) bookDetailImage1.getDrawable())
					.getBitmap();
			bookDetailImage1.setImageBitmap(((BitmapDrawable) bookDetailImage3
					.getDrawable()).getBitmap());
			bookDetailImage3.setImageBitmap(((BitmapDrawable) bookDetailImage2
					.getDrawable()).getBitmap());
			bookDetailImage2.setImageBitmap(bitmap);
//			System.out.println("闁告碍鍨垫稊蹇擃煥閿燂拷;
			return super.left();
		}

		@Override
		public boolean right() {
			Bitmap bitmap = ((BitmapDrawable) bookDetailImage1.getDrawable())
					.getBitmap();
			bookDetailImage1.setImageBitmap(((BitmapDrawable) bookDetailImage2
					.getDrawable()).getBitmap());
			bookDetailImage2.setImageBitmap(((BitmapDrawable) bookDetailImage3
					.getDrawable()).getBitmap());
			bookDetailImage3.setImageBitmap(bitmap);
//			System.out.println("闁告碍鍨垫稊蹇擃煥閿燂拷;
			return super.right();
		}

	}


//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		super.onWindowFocusChanged(hasFocus);
//		headerHight = header.getHeight();
//		middleHight = middle.getHeight();
//	}

	@Override
	public void ScrollChanged(int x, int y) {
//		// TODO Auto-generated method stub
//		System.out.println("y===================" + y);
//		int temp = headerHight + middleHight;
//		if (showFlag) { 
//			if(y<headerHight){
////				System.out.println("闂呮劘妫�);
//				front.setVisibility(View.GONE);
//				showFlag = true;
//			}
//			if (y > headerHight) {
////				System.out.println("閺勫墽銇�);
//				front.setVisibility(View.VISIBLE);
//				if(y>temp){
//				showFlag = false;
//				}
//			}
//		} else { 
////			System.out.println("y<temp ======= " +(y<temp));
//			if (y <temp) {
////				System.out.println("闂呮劘妫�);
//				front.setVisibility(View.GONE);
//				if(y<headerHight){
//				showFlag = true;
//				}
//
//				System.out.println("first =========" +first);
//
//			}
//		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bt_detail_back:
			System.out.println("返回");
			this.finish();
			break;
		case R.id.book_detail_show_all:
			if(showAllFlag){
				basicCondition.setSingleLine(false);
				suitCrowd.setSingleLine(false);
				myEvaluation.setSingleLine(false);
				showAllFlag=false;
//				showAll.setText("鏀惰捣鈫�);
			}else{
				basicCondition.setSingleLine(true);
				suitCrowd.setSingleLine(true);
				myEvaluation.setSingleLine(true);
				showAllFlag=true;
				showAll.setText("鏄剧ず鍏ㄩ儴");
			}
			
			break;
			
		case R.id.front_seller:
			if(curPage>0){
				curPage--;
				getInfomation();
			}else{
				Toast.makeText(BookDetailActivity.this, "no front seller", 2000).show();
			}
			break;
		case R.id.next_seller:
			if(curPage != maxPage){
				curPage++;
				getInfomation();
			}else{
				Toast.makeText(BookDetailActivity.this, "no next seller", 2000).show();
			}
			break;
		case R.id.book_detail_bottom_comment:
			InputPopwindow inputPopwindow = new InputPopwindow(this,list.get(curPage).get("book_id").toString());
        	inputPopwindow.showAtLocation(this.findViewById(R.id.main_bottom), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			/*mHandler.postDelayed(new Runnable() {  
	              
	            @Override  
	            public void run() {  
	            	bookDetailScrollView.fullScroll(View.FOCUS_DOWN);  
	            }  
	        }, 100);  
			
//			bookDetailScrollView.fullScroll(View.FOCUS_DOWN);
			commentll.setFocusable(true);
			commentll.setFocusableInTouchMode(true);
			commentll.requestFocus();
			Timer timer = new Timer();
			timer.schedule(new TimerTask(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
				imm.showSoftInput(commentll,InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
				}
				
			}, 300);
//			imm.showSoftInputFromInputMethod(commentEt.getWindowToken(), 0);
 * 
*/			break;
		case R.id.comment_send_btn:
			String comment = commenteditText.getText().toString();
			if(comment.equals("")){
				Toast.makeText(this, "评论不能为空", 2000).show();
			}
			else{
				new WishService().makeWishComment(list.get(curPage).get("book_id").toString(), Utils.user_id, Utils.username, comment, onQueryCompleteListener);
			}

 			break;
		case R.id.book_detail_bottom_connect:
			ConnectSellerPopwindow connectSellerPopwindow = new ConnectSellerPopwindow(BookDetailActivity.this, map);
			connectSellerPopwindow.showAtLocation(this.findViewById(R.id.main_bottom), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.book_detail_bottom_share:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, "这本书不错喔");
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	        startActivity(Intent.createChooser(intent, "请选择"));
			break;
		default:
			break;
		}
	}

}

