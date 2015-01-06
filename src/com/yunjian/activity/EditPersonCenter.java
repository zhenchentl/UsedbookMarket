package com.yunjian.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.image.AsyncImageLoader;
import com.yunjian.image.AsyncImageLoader.ImageCallback;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.service.UpLoadHeaderService;
import com.yunjian.service.UserManageService;
import com.yunjian.util.SerializableMap;
import com.yunjian.util.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditPersonCenter extends Activity implements OnClickListener,OnQueryCompleteListener,ImageCallback{
	private ImageView backButton;
	private ImageView photoImageView;
	private ImageView manImageView;
	private ImageView womanImageView;
	private ImageView secretImageView;
	private ImageView okImageButton;
	private EditText nickText;
	private EditText mobileText;
	private EditText qqText;
	private EditText wechatText;
	private String nick;
	//�Ա� ��0��ʾŮ��1��ʾ��,2��ʾ����
	private int sex = -1;

	//�ϴ�ͷ��service
	private UpLoadHeaderService upLoadHeaderService;
	private OnQueryCompleteListener onQueryCompleteListener;
	private AsyncImageLoader imageLoader;
	
	private static final int PHOTO_REQUEST_CAREMA = 1;//����
	private static final int PHOTO_REQUEST_GALLERY = 2;//�������ѡ��
	private static final int PHOTO_REQUEST_CUT = 3;//���
	private File tempFile;
	private Map<String, Object>map;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personcenter_edit);
		if(Utils.IFEDITPERSON==1){
			Bundle bundle = getIntent().getExtras();  
	        SerializableMap serMap = (SerializableMap) bundle  
	                .get("personinfo");
		    map = serMap.getMap();
		}
		initView();
	}
    
	public void initView(){
		backButton = (ImageView)findViewById(R.id.personedit_back_btn);
		photoImageView = (ImageView)findViewById(R.id.photo_img);
		manImageView = (ImageView)findViewById(R.id.man_img);
		womanImageView = (ImageView)findViewById(R.id.woman_img);
		secretImageView = (ImageView)findViewById(R.id.secret_img);
		okImageButton = (ImageView)findViewById(R.id.personedit_finish_btn);
		nickText = (EditText)findViewById(R.id.nickname_edt);
		mobileText = (EditText)findViewById(R.id.phone_edt);
		mobileText.setText(Utils.user_id);
		qqText = (EditText)findViewById(R.id.qq_edt);
		wechatText = (EditText)findViewById(R.id.wechat_edt);
		okImageButton.setClickable(true);
		backButton.setOnClickListener(this);
		photoImageView.setOnClickListener(this);
		manImageView.setOnClickListener(this);
		womanImageView.setOnClickListener(this);
		secretImageView.setOnClickListener(this);
		okImageButton.setOnClickListener(this);
		
		if(map!=null){
			
			nickText.setText(map.get("username").toString());
			mobileText.setText(map.get("mobile").toString());
			qqText.setText(map.get("qq").toString());
			wechatText.setText(map.get("weixin").toString());
			if(map.get("gender").equals(2.0))
				secretImageView.setBackgroundResource(R.drawable.pe_sex_secret_pressed);
			else if(map.get("gender").equals(0.0))
				womanImageView.setBackgroundResource(R.drawable.pe_sex_woman_pressed);
			else if (map.get("gender").equals(1.0)) {
				manImageView.setBackgroundResource(R.drawable.pe_sex_man_pressed);
			}
		}
		
		upLoadHeaderService = new UpLoadHeaderService();
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result.equals("success")){
					imageLoader.clearCache();
					imageLoader.loadDrawable(Utils.URL+Utils.user_id, EditPersonCenter.this);
					Toast.makeText(EditPersonCenter.this, "�ϴ��ɹ�", 2000).show();
				}
				else {
					Toast.makeText(EditPersonCenter.this, "�ϴ�ʧ��", 2000).show();
				}
			}
		};
		imageLoader = new AsyncImageLoader();
	}
	
	@Override
	public void imageLoaded(Drawable imageDrawable, String imageUrl) {
		// TODO Auto-generated method stub
		if(imageDrawable!=null){
			photoImageView.setImageDrawable(imageDrawable);
		}
	}
	
	public void updateSexImage(){
		womanImageView.setBackgroundResource(R.drawable.pe_sex_woman_unpressed);
		manImageView.setBackgroundResource(R.drawable.pe_sex_man_unpressed);
		secretImageView.setBackgroundResource(R.drawable.pe_sex_secret_unpressed);
	}
	

	@SuppressLint({ "WorldReadableFiles", "ShowToast" })
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.personedit_back_btn:
			finish();
			break;
		case R.id.photo_img:
			String[] tempStrings = new String[]{"�����ϴ�", "�������ѡ��"};
			Builder dialog = new AlertDialog.Builder(EditPersonCenter.this)
				.setTitle("�ϴ�ͷ��").setItems(tempStrings, new MyOnItemClickListener());
			dialog.show();
			break;
		case R.id.personedit_finish_btn:
			nick = nickText.getText().toString();
			String mobile = mobileText.getText().toString();
			String qq = qqText.getText().toString();
			String wechat = wechatText.getText().toString();
			if (nick.equals("")) {
				Toast.makeText(EditPersonCenter.this, "�㻹û���Լ�ȡ��������", 2000).show();
			}
			else if(mobile.equals("")){
				Toast.makeText(EditPersonCenter.this, "�绰�Ǳ���ѡ���", 2000).show();
			}
			else if (qq.equals("")&&wechat.equals("")) {
				Toast.makeText(EditPersonCenter.this, "��������һ����ϵ��ʽ��", 2000).show();
			}
			else if (sex == -1) {
				Toast.makeText(EditPersonCenter.this, "�ǵ�ѡ���Լ����Ա���", 2000).show();
			}
			else {
				Map<String, Object>map = new HashMap<String, Object>();
				map.put("user_id", Utils.user_id);
				map.put("nick", nick);
				map.put("sex", sex);
				map.put("mobile", mobile);
				map.put("qq", qq);
				map.put("wechat", wechat);
				SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_WORLD_READABLE);
				Editor editor = sharedPreferences.edit();
				editor.putString("username", nick);
				editor.commit();
				new UserManageService().SetUserInfo(map, this);
			}
			break;
		case R.id.man_img:
			updateSexImage();
			manImageView.setBackgroundResource(R.drawable.pe_sex_man_pressed);
			sex = 1;
			break;
		case R.id.woman_img:
			updateSexImage();
			womanImageView.setBackgroundResource(R.drawable.pe_sex_woman_pressed);
			sex = 0;
			break;
		case R.id.secret_img:
			updateSexImage();
			secretImageView.setBackgroundResource(R.drawable.pe_sex_secret_pressed);
			sex = 2;
			break;

		default:
			break;
		}
	}
	
	public class MyOnItemClickListener implements android.content.DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			if(arg1 == 1){
				// ����ϵͳͼ�⣬ѡ��һ��ͼƬ
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				// ����һ�����з���ֵ��Activity��������ΪPHOTO_REQUEST_GALLERY
				startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
			}else if(arg1 == 0){
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				// �жϴ洢���Ƿ�����ã����ý��д洢
				if (hasSdcard()) {
					tempFile = new File(Environment.getExternalStorageDirectory(),
							"123");
					// ���ļ��д���uri
					Uri uri = Uri.fromFile(tempFile);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				}
				// ����һ�����з���ֵ��Activity��������ΪPHOTO_REQUEST_CAREMA
				startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
			}
		}
		
	}
	
	private void crop(Uri uri) {
		// �ü�ͼƬ��ͼ
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// �ü���ı�����1��1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// �ü������ͼƬ�ĳߴ��С
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);

		intent.putExtra("outputFormat", "JPEG");// ͼƬ��ʽ
		intent.putExtra("noFaceDetection", true);// ȡ������ʶ��
		intent.putExtra("return-data", true);
		// ����һ�����з���ֵ��Activity��������ΪPHOTO_REQUEST_CUT
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	/*
	 * �ж�sdcard�Ƿ񱻹���
	 */
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressLint({ "ShowToast", "WorldReadableFiles" })
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			// ����᷵�ص�����
			if (data != null) {
				// �õ�ͼƬ��ȫ·��
				Uri uri = data.getData();
				crop(uri);
			}

		} else if (requestCode == PHOTO_REQUEST_CAREMA) {
			// ��������ص�����
			if (hasSdcard()) {
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(EditPersonCenter.this, "δ�ҵ��洢�����޷��洢��Ƭ��", 0).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			// �Ӽ���ͼƬ���ص�����
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				upLoadHeaderService.UpLoadHeader(bitmap, Utils.user_id, onQueryCompleteListener);
			}
			try {
				// ����ʱ�ļ�ɾ��
				tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}



	@SuppressLint({ "WorldReadableFiles", "ShowToast" })
	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		if (result.equals("success")) {
			Toast.makeText(EditPersonCenter.this, "�޸ĳɹ�", 2000).show();
			@SuppressWarnings("deprecation")
			SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_WORLD_READABLE);
			Editor editor = sharedPreferences.edit();
			editor.putString("username", nick);
			editor.commit();
			Utils.username = sharedPreferences.getString("username", "");
			finish();
		}
		else {
			Toast.makeText(EditPersonCenter.this, "�޸�ʧ��", 2000).show();
		}
	}
	

}
