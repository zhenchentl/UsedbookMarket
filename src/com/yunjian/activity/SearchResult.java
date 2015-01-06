package com.yunjian.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunjian.adapter.SearchResultAdapter;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.service.BookService;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.view.HistoryList;
import com.yunjian.view.Node;

public class SearchResult extends Activity implements OnClickListener {
	private ImageButton backButton;
	private AutoCompleteTextView searchEditText;
	private ListView historyListView,resultListView;
	private Button searchButton;
	private BookService service;
	private Button allbook,coursebook,english,japanese,technology,master,entertain;
	private OnQueryCompleteListener onQueryCompleteListener;
	private int maxHistory=10;
	private int type = 0;
	private TextView clear;
	private ArrayAdapter<String> adapter,completeAdapter;
	private SearchResultAdapter searchResultAdapter;
	private List<String> data;
	private List<Map<String, Object>> list;
	private ScrollView historyLayout;
	private InputMethodManager imm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		initView();
		service = new BookService();
		searchEditText.requestFocus();
		
	}

	public void initView() {
		backButton = (ImageButton) findViewById(R.id.search_result_back);
		searchEditText = (AutoCompleteTextView) findViewById(R.id.search_et);
		historyListView = (ListView) findViewById(R.id.search_history_list);
		resultListView = (ListView) findViewById(R.id.search_result_list);
		searchButton = (Button) findViewById(R.id.search_btn);
		clear = (TextView)findViewById(R.id.clear_history);
		historyLayout = (ScrollView)findViewById(R.id.history_layout);
		
		clear.setClickable(true);
		clear.setOnClickListener(this);
		data = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getData());
		completeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, data);
		
		
		searchEditText.setAdapter(completeAdapter);
		historyListView.setAdapter(adapter);
		historyListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				searchEditText.setText(data.get(arg2).toString());
			}
			
		});
		
		
		resultListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SearchResult.this,BookDetailActivity.class);
            	intent.putExtra("bookname", list.get(arg2).get("bookname").toString());
                startActivity(intent);
			}
			
		});
		
		backButton.setOnClickListener(this);
		searchButton.setOnClickListener(this);
		
		onQueryCompleteListener = new OnQueryCompleteListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onQueryComplete(QueryId queryId, Object result,
					EHttpError error) {
				// TODO Auto-generated method stub
				
				if (result != null) {
						list = (List<Map<String, Object>>) result;
						searchResultAdapter = new SearchResultAdapter(SearchResult.this, list);
						resultListView.setAdapter(searchResultAdapter);
						searchResultAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(SearchResult.this, "服务器连接失败", 2000).show();
				}
			}
		};
		
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


	private List<String> getData() {
		// TODO Auto-generated method stub
		HistoryList hisList = getHistory();
		Node temp = hisList.head;
		while(temp!=null){
			data.add(temp.data);
			temp = temp.next;
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	private HistoryList getHistory(){
		SharedPreferences sharedPreferences = getSharedPreferences(
				"searchHistory", MODE_PRIVATE);
		String history = sharedPreferences.getString("searchHistory", null);
		HistoryList hisList = new HistoryList(history);
		
		return hisList;		
	}
	
	private void saveHistory(String s) {
		// TODO Auto-generated method stub
			HistoryList hisList = getHistory();
			hisList.add(s);
			SharedPreferences sharedPreferences = getSharedPreferences("searchHistory", MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putString("searchHistory",hisList.changetoString());
			editor.commit();

		}
	
	public void resetService(){
		service.searchBook(searchEditText.getText().toString(),type+"","1",onQueryCompleteListener);
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.search_btn:
			
			if(searchEditText.getText().toString().equals("")){
				historyLayout.setVisibility(View.VISIBLE);
				resultListView.setVisibility(View.GONE);
			}
			else{
				
				saveHistory(searchEditText.getText().toString());
				data.clear();
				getData();
				adapter.notifyDataSetChanged();
				
				historyLayout.setVisibility(View.GONE);
				resultListView.setVisibility(View.VISIBLE);
				//searchEditText.setText("");	
				resetService();
				/*imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
				searchEditText.setInputType(InputType.TYPE_NULL);*/
			}
			
			break;
		case R.id.clear_history:
			SharedPreferences sharedPreferences = getSharedPreferences("searchHistory", MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putString("searchHistory","");
			editor.commit();
			data.clear();
			getData();
			adapter.notifyDataSetChanged();
			break;
		case R.id.all:
			type = 0;
			resetService();
			resetButtonColor();
			allbook.setTextColor(this.getResources().getColor(R.color.seagreen));
			allbook.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		case R.id.coursebook:
			type = 1;
			resetService();
			resetButtonColor();
			coursebook.setTextColor(this.getResources().getColor(R.color.seagreen));
			coursebook.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		case R.id.english:
			type = 2;
			resetService();
			resetButtonColor();
			english.setTextColor(this.getResources().getColor(R.color.seagreen));
			english.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		case R.id.japanese:
			type = 3;
			resetService();
			resetButtonColor();
			japanese.setTextColor(this.getResources().getColor(R.color.seagreen));
			japanese.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		case R.id.technology:
			type = 4;
			resetService();
			resetButtonColor();
			technology.setTextColor(this.getResources().getColor(R.color.seagreen));
			technology.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		case R.id.master:
			type = 5;
			resetService();
			resetButtonColor();
			master.setTextColor(this.getResources().getColor(R.color.seagreen));
			master.setBackgroundResource(R.drawable.wish_type_bg);
			break;
		case R.id.entertain:
			type = 6;
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
