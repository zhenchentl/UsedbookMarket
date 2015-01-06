package com.yunjian.view;

import com.yunjian.activity.R;
import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.WishService;
import com.yunjian.util.Utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

public class InputPopwindow extends PopupWindow implements OnQueryCompleteListener{
	private EditText commentEditText;
	private Button sendButton;
	private Context context;
	public InputPopwindow(final Context context ,final String wishId){
		 super(context); 
		 LayoutInflater inflater = (LayoutInflater) context  
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	        View view = inflater.inflate(R.layout.input, null);
            this.context = context; 
	        sendButton = (Button) view.findViewById(R.id.send_comment_btn);  
	        commentEditText = (EditText)view.findViewById(R.id.comment_edt);
	        sendButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String comment = commentEditText.getText().toString();
					if(comment.equals("")){
						Toast.makeText(context, "���۲���Ϊ��", 2000).show();
					}
					else {
						new WishService().makeWishComment(wishId, Utils.user_id, Utils.username, comment, InputPopwindow.this);
						popupInputMethodWindow2();
						dismiss();
					}
				}
			});
	      //����SelectPicPopupWindow��View  
	        this.setContentView(view);  
	        //����SelectPicPopupWindow��������Ŀ�  
	        this.setWidth(LayoutParams.FILL_PARENT);  
	        //����SelectPicPopupWindow��������ĸ�  
	        this.setHeight(LayoutParams.WRAP_CONTENT);  
	        //����SelectPicPopupWindow��������ɵ��  
	        this.setFocusable(true);
	        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	        // ����������������ʧ
			this.setOutsideTouchable(false);
	        //ʵ����һ��ColorDrawable��ɫΪ��͸��  
	        ColorDrawable dw = new ColorDrawable(0xffffffff);  
	        //����SelectPicPopupWindow��������ı���  
	        this.setBackgroundDrawable(dw); 
	        
	        popupInputMethodWindow();
	}
	
	private void popupInputMethodWindow() {  
		Handler handler =new Handler();
	    handler.postDelayed(new Runnable() {  
	        @Override  
	        public void run() {  
	        	InputMethodManager imm = (InputMethodManager) commentEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
	            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
	        }  
	    }, 0);  
	}
	
	// �첽�������
		private void popupInputMethodWindow2() {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					InputMethodManager imm = (InputMethodManager) commentEditText
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					// �õ�InputMethodManager��ʵ��
					if (imm.isActive())
						// �������
						imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
								InputMethodManager.HIDE_NOT_ALWAYS);
					// �ر�����̣�����������ͬ������������л�������ر�״̬��
				}
			}, 0);
		}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		if(result.equals("failed")){
			Toast.makeText(context, "����ʧ��", 2000).show();
		}
		else if(result.equals("success")){
			Toast.makeText(context, "���۳ɹ�", 2000).show();
		}
	}  

}
