package com.yunjian.fragment;

import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yunjian.activity.R;
import com.yunjian.activity.WishDetailActivity;
import com.yunjian.adapter.MyWishesAdapter;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.UserCenterService;
import com.yunjian.util.Utils;

public class MyWishes extends Fragment{
	private View view;
	private ListView myWishes;
	private ImageView erroImageView;
	private ProgressBar progressBar;
	private MyWishesAdapter adapter;
	private List<Map<String, Object>>list;
	private OnQueryCompleteListener onQueryCompleteListener;
	private UserCenterService service;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.wisheslist, null);
		initView(view);
		return view;
	}
	
	public void initView(View view){
		myWishes = (ListView)view.findViewById(R.id.wisheslist);
		progressBar = (ProgressBar)view.findViewById(R.id.wishes_progress_bar);
		erroImageView = (ImageView)view.findViewById(R.id.erro_img);
		service = new UserCenterService();
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result.equals(null)){
					Toast.makeText(getActivity(), "�������ӳ�ʱ", 2000).show();
				}
				else {
					progressBar.setVisibility(View.GONE);
					list = (List<Map<String, Object>>) result;
					if(list.size()==0){
						erroImageView.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);
					}
					else {
						adapter = new MyWishesAdapter(getActivity(), list);
						myWishes.setAdapter(adapter);
					}
				}
			}
		};
		service.getWishesByUser(Utils.user_id, onQueryCompleteListener);
		
		myWishes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),WishDetailActivity.class);
				intent.putExtra("wish_id", list.get(arg2).get("wish_id").toString());
				startActivity(intent);
			}
		});
	}
}
