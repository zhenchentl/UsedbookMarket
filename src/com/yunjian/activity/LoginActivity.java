package com.yunjian.activity;


import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.UserManageService;
import com.yunjian.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
	private EditText username;
	private EditText password;
	private Button loginButton;
	private Button registerButton;
	private String usernameString;
	private String passwordString;
	
    private UserManageService service;
    private OnQueryCompleteListener queryCompleteListener;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initView();
	}
	
	public void initView(){
		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);
		loginButton = (Button)findViewById(R.id.login_btn);
		registerButton = (Button)findViewById(R.id.register_btn);
		service = new UserManageService();
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				usernameString = username.getText().toString();
				passwordString = password.getText().toString();
				if(usernameString.equals("")){
					Toast.makeText(LoginActivity.this, "用户名不能为空", 2000).show();
				}
				else if(passwordString.equals("")){
					Toast.makeText(LoginActivity.this, "密码不能为空", 2000).show();
				}
				else {
					service.UserLogin(usernameString, passwordString, queryCompleteListener);
				}
				
			}
		});
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
		
		
		queryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result.equals("success")){
					Toast.makeText(LoginActivity.this, "登录成功", 2000).show();
					SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_WORLD_READABLE);
					Editor editor = sharedPreferences.edit();
					editor.putString("user_id", usernameString);
					editor.putString("password", passwordString);
					Utils.user_id = usernameString;
					Utils.password = passwordString;
					editor.commit();
					finish();
				}
				else {
					Toast.makeText(LoginActivity.this, "用户名或密码错误", 2000).show();
				}
			}
		};
	}
	

}
