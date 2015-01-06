package com.yunjian.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.yunjian.connection.HttpUtils.EHttpError;
import com.yunjian.service.BookService;
import com.yunjian.service.OnQueryCompleteListener;
import com.yunjian.service.QueryId;
import com.yunjian.util.Utils;
import com.yunjian.util.SerializableMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.GridLayout;
import android.widget.Toast;

public class AddBookActivity extends Activity implements OnClickListener,OnFocusChangeListener{
	private ImageView camaraImageView;
	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;
    private EditText booknameEditText;
    private EditText bookpriceEditText;
    private EditText bookqualityEditText;
    private GridLayout qualityLayout;
    private EditText bookwhoEditText;
    private GridLayout whoLayout;
    private EditText bookhelpEditText;
    private EditText phoneEditText;
    private EditText qqEditText;
    private EditText wechatEditText;
    private Button coursebook,english,japanese,technology,master,entertain;
    private Button[] qualityButtons;
    private Button[] whoButtons;
    private ImageView backButton;
    private Button okButton;
    
    private BookService bookService;
    private OnQueryCompleteListener onQueryCompleteListener;
    
	private static final int PHOTO_REQUEST_CAREMA = 1;//����
	private static final int PHOTO_REQUEST_GALLERY = 2;//�������ѡ��
	private static final int PHOTO_REQUEST_CUT = 3;//���
	private File tempFile;
	private String img1="";
	private String img2="";
	private String img3="";
	//���������ϴ�ͼƬ������
	private int i=0;
	private String bookId = "";
	private Map<String, Object> map;
	private int booktype = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_book);
		if(Utils.IFEDITBOOK==1){
			Bundle bundle = getIntent().getExtras();  
	        SerializableMap serMap = (SerializableMap) bundle  
	                .get("bookinfo");
		    map = serMap.getMap();
		}
	    initView();
	    initButtons();
	}
	
	public void initView(){
		camaraImageView = (ImageView)findViewById(R.id.takephoto);
		imageView1 = (ImageView)findViewById(R.id.img1);
		imageView2 = (ImageView)findViewById(R.id.img2);
		imageView3 = (ImageView)findViewById(R.id.img3);
		booknameEditText = (EditText)findViewById(R.id.addbook_name);
		bookpriceEditText = (EditText)findViewById(R.id.addbook_price);
		bookqualityEditText = (EditText)findViewById(R.id.addbook_quality);
		bookwhoEditText = (EditText)findViewById(R.id.addbook_who);
		bookhelpEditText = (EditText)findViewById(R.id.addbook_help);
		qualityLayout = (GridLayout)findViewById(R.id.quality_ll);
		whoLayout = (GridLayout)findViewById(R.id.who_ll);
		phoneEditText = (EditText)findViewById(R.id.addbook_phone);
		qqEditText = (EditText)findViewById(R.id.addbook_qq);
		wechatEditText = (EditText)findViewById(R.id.addbook_wechat);
		coursebook = (Button)findViewById(R.id.coursebook);
		english = (Button)findViewById(R.id.english);
		japanese = (Button)findViewById(R.id.japanese);
		technology = (Button)findViewById(R.id.technology);
		master = (Button)findViewById(R.id.master);
		entertain = (Button)findViewById(R.id.entertain);
		okButton = (Button)findViewById(R.id.addbook_ok_img);
		backButton = (ImageView)findViewById(R.id.addbook_back_img);
		
		okButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		camaraImageView.setOnClickListener(this);
		coursebook.setOnClickListener(this);
		english.setOnClickListener(this);
		japanese.setOnClickListener(this);
		technology.setOnClickListener(this);
		master.setOnClickListener(this);
		entertain.setOnClickListener(this);
		
		bookqualityEditText.setOnFocusChangeListener(this);
		bookwhoEditText.setOnFocusChangeListener(this);
		bookhelpEditText.setOnFocusChangeListener(this);
		
		if(map!=null){
			System.out.println(map.toString());
			booknameEditText.setText(map.get("bookname").toString());
			bookpriceEditText.setText(map.get("price").toString());
			bookqualityEditText.setText(map.get("newness").toString());
			bookwhoEditText.setText(map.get("audience").toString());
			bookhelpEditText.setText(map.get("description").toString());
			phoneEditText.setText(map.get("mobile").toString());
			qqEditText.setText(map.get("qq").toString());
			wechatEditText.setText(map.get("weixin").toString());
			bookId = map.get("book_id").toString();
		}
		
		onQueryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				// TODO Auto-generated method stub
				if(result.equals(null)){
					Toast.makeText(AddBookActivity.this, "�������ӳ�ʱ", 2000).show();
				}
				else if(result.equals("success")){
					Toast.makeText(AddBookActivity.this, "�ϴ��ɹ�", 2000).show();
					Utils.IFEDITBOOK = 0;
					finish();
				}
				else {
					Toast.makeText(AddBookActivity.this, "�ϴ�ʧ��", 2000).show();
				}
			}
		};
		
		
	}
	
	public void initButtons(){
		qualityButtons = new Button[5];
		qualityButtons[0] = (Button)findViewById(R.id.new1);
		qualityButtons[1] = (Button)findViewById(R.id.new2);
		qualityButtons[2] = (Button)findViewById(R.id.new3);
		qualityButtons[3] = (Button)findViewById(R.id.new4);
		qualityButtons[4] = (Button)findViewById(R.id.new5);
		whoButtons = new Button[5];
		whoButtons[0] = (Button)findViewById(R.id.who1);
		whoButtons[1] = (Button)findViewById(R.id.who2);
		whoButtons[2] = (Button)findViewById(R.id.who3);
		whoButtons[3] = (Button)findViewById(R.id.who4);
		whoButtons[4] = (Button)findViewById(R.id.who5);
		Resources res = this.getResources(); //������onCreate�� 
		String[] arr = res.getStringArray(R.array.quality); 
		for(int i=0;i<5;i++){
			qualityButtons[i].setText(arr[i]);
			qualityButtons[i].setOnClickListener(this);
			whoButtons[i].setOnClickListener(this);
		}
		switch (booktype) {
		case 1:
			arr = res.getStringArray(R.array.coursebook); 
			for(int i=0;i<arr.length;i++)
				whoButtons[i].setText(arr[i]);
			break;
		case 2:
			arr = res.getStringArray(R.array.english); 
			for(int i=0;i<arr.length;i++)
				whoButtons[i].setText(arr[i]);
			break;
		case 3:
			arr = res.getStringArray(R.array.japanese); 
			for(int i=0;i<arr.length;i++)
				whoButtons[i].setText(arr[i]);
			break;
		case 4:
			arr = res.getStringArray(R.array.technology); 
			for(int i=0;i<arr.length;i++)
				whoButtons[i].setText(arr[i]);
			break;
		case 5:
			arr = res.getStringArray(R.array.master); 
			for(int i=0;i<arr.length;i++)
				whoButtons[i].setText(arr[i]);
			break;
		case 6:
			arr = res.getStringArray(R.array.entertain); 
			for(int i=0;i<arr.length;i++)
				whoButtons[i].setText(arr[i]);
			break;

		default:
			break;
		}
		
	}
	
	public void resetButtonColor(){
		coursebook.setTextColor(Color.BLACK);
		english.setTextColor(Color.BLACK);
		japanese.setTextColor(Color.BLACK);
		technology.setTextColor(Color.BLACK);
		master.setTextColor(Color.BLACK);
		entertain.setTextColor(Color.BLACK);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.addbook_back_img:
			finish();
			break;
		case R.id.addbook_ok_img:
			String bookname = booknameEditText.getText().toString();
			String bookquality = bookqualityEditText.getText().toString();
			String bookwho = bookwhoEditText.getText().toString();
			String bookhelp = bookhelpEditText.getText().toString();
			String phone = phoneEditText.getText().toString();
			String qq = qqEditText.getText().toString();
			String wechat = wechatEditText.getText().toString();
			if(bookname.equals("")||bookquality.equals("")
					||bookwho.equals("")||bookhelp.equals("")||bookpriceEditText.getText().toString().equals(""))
			{
				Toast.makeText(AddBookActivity.this, "��Ϣ��д������", 2000).show();
			}
			else if(phone.equals("")) {
				Toast.makeText(AddBookActivity.this, "�绰�����Ǳ������", 2000).show();
			}
			else {
				float bookprice = Float.valueOf(bookpriceEditText.getText().toString());
				Map<String, Object>map = new HashMap<String, Object>();
				map.put("user_id", Utils.user_id);
				map.put("bookname", bookname);
				map.put("book_id", bookId);
				map.put("username", Utils.username);
				map.put("type", booktype);
				map.put("bookprice", bookprice);
				map.put("newness", bookquality);
				map.put("audience", bookwho);
				map.put("description", bookhelp);
				map.put("mobile", phone);
				map.put("qq", qq);
				map.put("wexin", wechat);
				map.put("img1", img1);
				map.put("img2", img2);
				map.put("img3", img3);
				bookService = new BookService();
				bookService.addBook(map, onQueryCompleteListener);
			}
			break;
		case R.id.takephoto:
			if(i>2){
				Toast.makeText(AddBookActivity.this, "������ϴ�����ͼƬ�", 2000).show();
			}
			else {
				String[] tempStrings = new String[]{"�����ϴ�", "�������ѡ��"};
				Builder dialog = new AlertDialog.Builder(AddBookActivity.this)
					.setTitle("�ϴ�ͷ��").setItems(tempStrings, new MyOnItemClickListener());
				dialog.show();
			}
			break;
		case R.id.coursebook:
			booktype = 1;
			resetButtonColor();
			 initButtons();
			coursebook.setTextColor(this.getResources().getColor(R.color.seagreen));
			break;
		case R.id.english:
			booktype = 2;
			resetButtonColor();
			 initButtons();
			english.setTextColor(this.getResources().getColor(R.color.seagreen));
			break;
		case R.id.japanese:
			booktype = 3;
			resetButtonColor();
			 initButtons();
			japanese.setTextColor(this.getResources().getColor(R.color.seagreen));
			break;
		case R.id.technology:
			booktype = 4;
			resetButtonColor();
			 initButtons();
			technology.setTextColor(this.getResources().getColor(R.color.seagreen));
			break;
		case R.id.master:
			booktype = 5;
			resetButtonColor();
			 initButtons();
			master.setTextColor(this.getResources().getColor(R.color.seagreen));
			break;
		case R.id.entertain:
			booktype = 6;
			resetButtonColor();
			 initButtons();
			entertain.setTextColor(this.getResources().getColor(R.color.seagreen));
			break;
		case R.id.new1:
			bookqualityEditText.setText(qualityButtons[0].getText());
			break;
		case R.id.new2:
			bookqualityEditText.setText(qualityButtons[1].getText());
			break;
		case R.id.new3:
			bookqualityEditText.setText(qualityButtons[2].getText());
			break;
		case R.id.new4:
			bookqualityEditText.setText(qualityButtons[3].getText());
			break;
		case R.id.new5:
			bookqualityEditText.setText(qualityButtons[4].getText());
			break;
		case R.id.who1:
			bookwhoEditText.setText(whoButtons[0].getText());
			break;
		case R.id.who2:
			bookwhoEditText.setText(whoButtons[1].getText());
			break;
		case R.id.who3:
			bookwhoEditText.setText(whoButtons[2].getText());
			break;
		case R.id.who4:
			bookwhoEditText.setText(whoButtons[3].getText());
			break;
		case R.id.who5:
			bookwhoEditText.setText(whoButtons[4].getText());
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
							Utils.user_id);
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
		intent.putExtra("aspectY", 2);
		// �ü������ͼƬ�ĳߴ��С
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 400);

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
				Toast.makeText(AddBookActivity.this, "δ�ҵ��洢�����޷��洢��Ƭ��", 0).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			// �Ӽ���ͼƬ���ص�����
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				//photoImageView.setImageBitmap(bitmap);
				try {
					ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
					out.flush();
					out.close();
					byte[] buffer = out.toByteArray();  
			        byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
			        if(i==0){
			        	img1 = new String(encode);
			        	imageView1.setImageBitmap(bitmap);
			        	i++;
			        }
			        else if(i==1){
			        	img2 = new String(encode);
			        	imageView2.setImageBitmap(bitmap);
			        	i++;
					}
			        else {
			        	img3 = new String(encode);
			        	imageView3.setImageBitmap(bitmap);
			        	i++;
					}
			        
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
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

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.addbook_quality:
			qualityLayout.setVisibility(View.VISIBLE);
			whoLayout.setVisibility(View.GONE);
			break;
		case R.id.addbook_who:
			whoLayout.setVisibility(View.VISIBLE);
			qualityLayout.setVisibility(View.GONE);
			break;
		case R.id.addbook_help:
			qualityLayout.setVisibility(View.GONE);
			whoLayout.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}
	

}
