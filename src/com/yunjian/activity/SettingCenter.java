package com.yunjian.activity;

import com.yunjian.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettingCenter extends Activity implements OnClickListener{
	private ImageButton backImageButton;
	private TextView resetpassTextView;
	private Button logoutButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		initView();
	}
	
	public void initView(){
		backImageButton = (ImageButton)findViewById(R.id.back_btn);
		resetpassTextView = (TextView)findViewById(R.id.setting_resetpass_txv);
		logoutButton = (Button)findViewById(R.id.log_out);
		backImageButton.setOnClickListener(this);
		resetpassTextView.setOnClickListener(this);
		logoutButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.back_btn:
			finish();
			break;
		case R.id.setting_resetpass_txv:
			Intent intent = new Intent(this,ResetPassword.class);
			startActivity(intent);
			break;
		case R.id.log_out:
			SharedPreferences sharedPreferences = getSharedPreferences("userInfo",Activity.MODE_WORLD_WRITEABLE);
			Editor editor = sharedPreferences.edit();
			editor.remove("user_id");
			editor.remove("username");
			editor.remove("password");
			editor.commit();
			Utils.user_id = "";
			Utils.username = "";
			finish();
			break;

		default:
			break;
		}
	}
	

}
