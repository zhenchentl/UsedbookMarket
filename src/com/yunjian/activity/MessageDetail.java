package com.yunjian.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MessageDetail extends Activity{
	private TextView messagedetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_detail);
		messagedetail = (TextView)findViewById(R.id.message_detail_txv);
		messagedetail.setText("你的心愿单：软件工程 已经被 Neo接下啦 并留下了联系方式，赶紧去联系他吧！\n" +
				               "手机  18742513130 \n QQ 1231232q321\n 微信 sdasdasdas");
	}
	

}
