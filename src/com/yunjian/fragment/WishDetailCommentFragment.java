package com.yunjian.fragment;

import java.util.List;
import java.util.Map;

import com.yunjian.activity.R;
import com.yunjian.activity.WishDetailActivity;
import com.yunjian.adapter.WishCommentAdapter;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.WishService;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class WishDetailCommentFragment extends Fragment implements OnQueryCompleteListener{
	private ListView listView;
	private TextView textView;
	private WishService service;
	private List<Map<String, Object>>list;
	private WishCommentAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.wishdetail_comment_fragment, null);
		initView(view);
		return view;
	}
	
	public void initView(View view){
		listView = (ListView)view.findViewById(R.id.wishdetail_commentlist);
		textView = (TextView)view.findViewById(R.id.wishdetail_comment_enpty);
		service = new WishService();
		service.getWishComment(WishDetailActivity.wish_id, "1", this);
	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		if(result!=null){
			list = (List<Map<String, Object>>) result;
			if(list.size()==0){
				textView.setVisibility(View.VISIBLE);
			}
			adapter = new WishCommentAdapter(getActivity(), list);
			listView.setAdapter(adapter);
		}
	}
	

}
