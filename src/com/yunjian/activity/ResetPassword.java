package com.yunjian.activity;

import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.util.Utils;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.UserManageService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ResetPassword extends Activity{
	private EditText originpasswordEditText;
	private EditText passwordEditText;
	private EditText repasswordEditText;
	private ImageView backImageView;
	private Button okButton;
	private String passString;
	
	public UserManageService service;
	private OnQueryCompleteListener onQueryCompleteListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editpassword);
		initView();
	}
	
	public void initView(){
		backImageView = (ImageView)findViewById(R.id.back);
		originpasswordEditText = (EditText)findViewById(R.id.reset_phone);
		passwordEditText = (EditText)findViewById(R.id.reset_password);
		repasswordEditText = (EditText)findViewById(R.id.reset_re_password);
		okButton = (Button)findViewById(R.id.reset_ok);
		service = new UserManageService();
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String userString = originpasswordEditText.getText().toString();
				passString = passwordEditText.getText().toString();
				String repassString = repasswordEditText.getText().toString();
				if(!Utils.password.equals(userString)){
					Toast.makeText(ResetPassword.this, "原密码输入有误", 2000).show();
				}
				else if(passString.equals("")){
					Toast.makeText(ResetPassword.this, "密码不能为空", 2000).show();
				}
				else if (!passString.equals(repassString)) {
					Toast.makeText(ResetPassword.this, "两次密码输入不一致", 2000).show();
				}
				else {
					service.ResetPassword(Utils.user_id, passString, onQueryCompleteListener);
				}
			}
		});
		
		backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result.equals("success")){
					Toast.makeText(ResetPassword.this, "修改成功", 2000).show();
					SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_WORLD_WRITEABLE);
					Editor editor = sharedPreferences.edit();
					editor.putString("password", passString);
					editor.commit();
					finish();
				}
				else {
					Toast.makeText(ResetPassword.this, "修改失败", 2000).show();
				}
			}
		};
	}
	

}
