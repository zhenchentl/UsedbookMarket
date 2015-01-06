package com.yunjian.activity;

import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.util.CheckMobile;
import com.yunjian.util.Utils;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.UserManageService;

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

public class RegisterActivity extends Activity{
	private EditText phoneEditText;
	private EditText passwordEditText;
	private EditText repasswordEditText;
	private Button okButton;
	private String userString,passString;
	
	public UserManageService service;
	private OnQueryCompleteListener onQueryCompleteListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		initView();
	}
	
	public void initView(){
		phoneEditText = (EditText)findViewById(R.id.register_phone);
		passwordEditText = (EditText)findViewById(R.id.register_password);
		repasswordEditText = (EditText)findViewById(R.id.register_re_password);
		okButton = (Button)findViewById(R.id.register_ok);
		service = new UserManageService();
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userString = phoneEditText.getText().toString();
			    passString = passwordEditText.getText().toString();
				String repassString = repasswordEditText.getText().toString();
				if(!CheckMobile.isMobileNO(userString)){
					Toast.makeText(RegisterActivity.this, "电话号码输入有误", 2000).show();
				}
				else if (!passString.equals(repassString)) {
					Toast.makeText(RegisterActivity.this, "两次密码输入不一致", 2000).show();
				}
				else {
					service.userRegister(userString, passString, onQueryCompleteListener);
				}
			}
		});
		
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result.equals("success")){
					Toast.makeText(RegisterActivity.this, "注册成功,赶紧去完善一下你的个人信息吧", 2000).show();
					SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_WORLD_READABLE);
					Editor editor = sharedPreferences.edit();
					editor.putString("user_id", userString);
					editor.putString("password", passString);
					Utils.user_id = userString;
					Utils.password = passString;
					editor.commit();
					Intent intent = new Intent(RegisterActivity.this,EditPersonCenter.class);
					startActivity(intent);
					finish();
				}
				else {
					Toast.makeText(RegisterActivity.this, "注册成功", 2000).show();
				}
			}
		};
	}
	

}
