package com.yunjian.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.yunjian.adapter.MessageCenterAdapter;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.UserCenterService;
import com.yunjian.util.Utils;

public class MessageCenter extends Activity implements OnClickListener{
	private ImageView backImageButton;
	private Button clearButton;
	private ListView messageListView;
	private ImageView erroImageView;
	private ProgressBar progressBar;
	private List<Map<String, Object>>list;
	private MessageCenterAdapter adapter;
	
	private UserCenterService service;
	private OnQueryCompleteListener onQueryCompleteListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personcenter_message);
		initView();
	}
	public void initView(){
		backImageButton = (ImageView)findViewById(R.id.message_back_btn);
		clearButton = (Button)findViewById(R.id.clear_message_btn);
		messageListView = (ListView)findViewById(R.id.message_listview);
		progressBar = (ProgressBar)findViewById(R.id.message_progress_bar);
		erroImageView = (ImageView)findViewById(R.id.erro_img);
		
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result!=null){
					progressBar.setVisibility(View.GONE);
					list = (List<Map<String, Object>>) result;
					if(list.size()==0){
						erroImageView.setVisibility(View.VISIBLE);
					}
					else {
						adapter = new MessageCenterAdapter(MessageCenter.this, list);
						messageListView.setAdapter(adapter);
					}
				}
			}
		};
		service = new UserCenterService();
		service.getMessageList(Utils.user_id, onQueryCompleteListener);
		
		backImageButton.setOnClickListener(this);
		clearButton.setOnClickListener(this);
		messageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(list.get(arg2).get("type").toString().equals("2.0")){
					Intent intent = new Intent(MessageCenter.this,WishDetailActivity.class);
					intent.putExtra("wish_id", list.get(arg2).get("object_id").toString());
					startActivity(intent);
				}
				else if(list.get(arg2).get("type").toString().equals("1.0")){
					Intent intent = new Intent(MessageCenter.this,BookDetailActivity.class);
					intent.putExtra("bookname", list.get(arg2).get("bookname").toString());
					startActivity(intent);
				}
				else {
					Intent intent1 = new Intent(MessageCenter.this,MessageDetail.class);
					startActivity(intent1);
					
				}
			}
		});
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.back_btn:
			finish();
			break;
		case R.id.clear_message_btn:
			list.clear();
			adapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}

}
