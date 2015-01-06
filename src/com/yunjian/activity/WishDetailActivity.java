package com.yunjian.activity;

import java.util.List;
import java.util.Map;
import com.yunjian.adapter.WishCommentAdapter;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.image.ImageLoader;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.WishService;
import com.yunjian.util.Utils;
import com.yunjian.view.HelpAchievePop;
import com.yunjian.view.InputPopwindow;
import com.yunjian.view.NoScrollListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WishDetailActivity extends Activity implements OnClickListener{
	
	private WishService service;
	private OnQueryCompleteListener onQueryCompleteListener;
	private ImageView bookImageView;
	private ImageView photoImageView;
	private TextView personname;
	private TextView bookname;
	private TextView booktype;
	private TextView phoneTextView;
	private TextView qqTextView;
	private TextView description;
	private TextView wishdetailCommentEmpty;
	private NoScrollListView wishdetailCommentList;
	private ImageView backButton;
	private ImageView shareButton,comment,help;
	
	private WishCommentAdapter adapter;
	private int page = 1;
	private Map<String, Object>map;
	public static String wish_id;
	private List<Map<String, Object>>list;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wish_detail);
		
		initView();
	
	}
	
	public void initView(){
		bookImageView = (ImageView)findViewById(R.id.wishdetail_bookphoto_img);
		photoImageView = (ImageView)findViewById(R.id.wishdetail_user_image);
		personname = (TextView)findViewById(R.id.wishdetail_user_name);
		bookname = (TextView)findViewById(R.id.wishdetail_bookname_txv);
		booktype = (TextView)findViewById(R.id.wishdetail_booktype_txv);
		phoneTextView = (TextView)findViewById(R.id.wishdetail_user_tel);
		qqTextView = (TextView)findViewById(R.id.wishdetail_user_QQ);
		description = (TextView)findViewById(R.id.wishdetail_description_txv);
		backButton = (ImageView)findViewById(R.id.wishdetail_back_img);
		shareButton = (ImageView)findViewById(R.id.wishdetail_share_img);
		comment = (ImageView)findViewById(R.id.wishdetail_comment_img);
		help = (ImageView)findViewById(R.id.wishdetail_help_img);
		wishdetailCommentEmpty = (TextView)findViewById(R.id.wishdetail_comment_enpty);
		wishdetailCommentList = (NoScrollListView)findViewById(R.id.wishdetail_commentlist);  
		shareButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		comment.setOnClickListener(this);
		help.setOnClickListener(this);
		
		Intent intent = getIntent();
		wish_id = intent.getStringExtra("wish_id");
		service = new WishService();
		
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result != null){
					System.out.println("queryId==========="+queryId.toString());
					System.out.println("listwish========"+WishService.LISTWISH);
					System.out.println("clickwish========"+WishService.CLICKWISH);
					System.out.println("getcomment========"+WishService.GETCOMMENT);
					System.out.println("makecomment========"+WishService.MAKECOMMENT);
					System.out.println("makecomment========"+WishService.GetWishDetail);
					if(queryId.equals(WishService.GetWishDetail)){
						map = (Map<String, Object>) result;
						bookname.setText(map.get("bookname").toString());
					    description.setText(map.get("description").toString());
					    phoneTextView.setText(map.get("mobile").toString());
					    qqTextView.setText(map.get("qq").toString());
					    personname.setText(map.get("username").toString());
					    booktype.setText("分类："+Utils.booktypeStrings[Integer.valueOf(map.get("type").toString().substring(0, 1))-1]);
					    ImageLoader mImageLoader = ImageLoader.getInstance(WishDetailActivity.this);
					  //图片加载
						try {
							mImageLoader.addTask(Utils.URL+map.get("user_id").toString(), photoImageView);
						} catch (Exception e) {
						// TODO: handle exception
							e.printStackTrace();
						}
					} else if(queryId.equals(WishService.GETCOMMENT)){
						list = (List<Map<String, Object>>) result;
						if(list.size()==0){
							wishdetailCommentEmpty.setVisibility(View.VISIBLE);
						}
						adapter = new WishCommentAdapter(WishDetailActivity.this, list);
						wishdetailCommentList.setAdapter(adapter);
					}
				}
			}
		};
		service.getWishDetail(wish_id, onQueryCompleteListener);
//		FragmentManager fragmentManager = getFragmentManager();
//		fragmentManager.beginTransaction().replace(R.id.wishdetail_comment_layout, new WishDetailCommentFragment()).commit();
		service.getWishComment(wish_id,"1",onQueryCompleteListener);
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
        case R.id.wishdetail_back_img:
			finish();
			break;
        case R.id.wishdetail_share_img:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, "这本书不错喔");
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	        startActivity(Intent.createChooser(intent, "请选择"));
			break;
        case R.id.wishdetail_comment_img:
        	InputPopwindow inputPopwindow = new InputPopwindow(this,wish_id);
        	inputPopwindow.showAtLocation(this.findViewById(R.id.main_ll), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        	break;
        case R.id.wishdetail_help_img:
        	if(map.get("user_id").toString().equals(Utils.user_id)){
        		Toast.makeText(this, "这是你自己的心愿单喔", 2000).show();
        	}
        	else {
        		HelpAchievePop helpAchievePop = new HelpAchievePop(this,map);
    			helpAchievePop.showAtLocation(((Activity) this)
    					.findViewById(R.id.main_ll), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			}
        	break;
		default:
			break;
		}
	}

}