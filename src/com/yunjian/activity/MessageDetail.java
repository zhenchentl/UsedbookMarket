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
		messagedetail.setText("�����Ը����������� �Ѿ��� Neo������ ����������ϵ��ʽ���Ͻ�ȥ��ϵ���ɣ�\n" +
				               "�ֻ�  18742513130 \n QQ 1231232q321\n ΢�� sdasdasdas");
	}
	

}
