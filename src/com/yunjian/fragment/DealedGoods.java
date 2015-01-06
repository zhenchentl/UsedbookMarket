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

import com.yunjian.activity.BookDetailActivity;
import com.yunjian.activity.R;
import com.yunjian.adapter.DealedGoodsAdapter;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.UserCenterService;
import com.yunjian.util.Utils;

public class DealedGoods extends Fragment{
	public boolean isCommitAgain = false;
	private View view;
	private ListView dealedGoods;
	private ImageView erroImageView;
	private ProgressBar progressBar;
	private List<Map<String, Object>>list;
	private DealedGoodsAdapter adapter;
	private OnQueryCompleteListener onQueryCompleteListener;
	private UserCenterService service;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.goodslist, null);
		initView(view);
		return view;
	}
	
	public boolean getIsCommitAgain(){
    	return isCommitAgain;
    }
	
	public void initView(View view){
		dealedGoods = (ListView)view.findViewById(R.id.goodslist);
		progressBar = (ProgressBar)view.findViewById(R.id.goods_progress_bar);
		erroImageView = (ImageView)view.findViewById(R.id.erro_img);
		service = new UserCenterService();
		
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result.equals(null)){
					Toast.makeText(getActivity(), "网络连接超时", 2000).show();
				}
				else {
					progressBar.setVisibility(View.GONE);
					list = (List<Map<String, Object>>) result;
					System.out.println("书单"+list.toString());
					if(list.size()==0){
						erroImageView.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);
					}
					else {
						adapter = new DealedGoodsAdapter(getActivity(), list);
						dealedGoods.setAdapter(adapter);
					}
				}
			}
		};
		service.getBooksByUser(Utils.user_id, onQueryCompleteListener);
		dealedGoods.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),BookDetailActivity.class);
				intent.putExtra("bookname", list.get(arg2).get("bookname").toString());
				startActivity(intent);
			}
		});
	}
	

}
