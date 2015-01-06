package com.yunjian.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunjian.connection.HttpUtils;
import com.yunjian.connection.HttpUtils.EHttpError;

public class UserCenterService {
	//��ȡ�û��ѷ����������鼮
	private final String GETGOODSLISTACTION = "book/getBooksByUser";
	//��ȡ�û�������Ը�鵥
	private final String GETWISHESLISTACTION = "wish/getWishesByUser";
	//�޸��ѷ����Ķ������״̬
	private final String SETBOOKSTATUS = "book/setBookStatus";
	//�޸��ѷ�������Ը����״̬
	private final String SETWISHSTATUS = "wish/setWishStatus";
	//�����Ϣ�б�
	private final String GETMESSAGELISTACTION = "user/getMessages";
	/*
	 * @function  ��ȡ�û��ѷ����������鼮
	 * @param     user_id
	 * @return    
	 */
	public void getBooksByUser(String userid,OnQueryCompleteListener onQueryCompleteListener){
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		parms.add(new BasicNameValuePair("user_id", userid));
		HttpUtils.makeAsyncPost(GETGOODSLISTACTION, parms, 
				new QueryCompleteHandler(onQueryCompleteListener, new QueryId()) {
					
					@Override
					public void handleResponse(String jsonResult, EHttpError error) {
						// TODO Auto-generated method stub
						Map<String, Object>books;
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
								Gson gson = new Gson();
								Type type = new TypeToken<Map<String, Object>>() {
								}.getType();
								books = gson.fromJson(jsonResult, type);
								List<Map<String, Object>>list = (List<Map<String, Object>>) books.get("books");
								this.completeListener.onQueryComplete(new QueryId(), list, error);
							    }
						else {
							this.completeListener.onQueryComplete(new QueryId(), null, error);
						}
					}
				});
	}
	
	/*
	 * @function  ��ȡ�û���������Ը�鵥
	 * @param     user_id
	 * @return    
	 */
	public void getWishesByUser(String userid,OnQueryCompleteListener onQueryCompleteListener){
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		parms.add(new BasicNameValuePair("user_id", userid));
		HttpUtils.makeAsyncPost(GETWISHESLISTACTION, parms, 
				new QueryCompleteHandler(onQueryCompleteListener, new QueryId()) {
					
					@Override
					public void handleResponse(String jsonResult, EHttpError error) {
						// TODO Auto-generated method stub
						Map<String, Object>wishes;
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							    System.out.println(jsonResult);
							    if(jsonResult.equals("[]")){
							    	this.completeListener.onQueryComplete(new QueryId(), jsonResult, error);
							    }
							    else{
									Gson gson = new Gson();
									Type type = new TypeToken<Map<String, Object>>() {
									}.getType();
									wishes = gson.fromJson(jsonResult, type);
									List<Map<String, Object>>list = (List<Map<String, Object>>) wishes.get("wishes");
									System.out.println("��Ը��"+wishes.toString());
									this.completeListener.onQueryComplete(new QueryId(), list, error);
							    }
						}
						else {
							this.completeListener.onQueryComplete(new QueryId(), null, error);
						}
					}
				});
	}
	
	/*
	 * @function   �޸��ѷ������״̬
	 * @param      
	 * @return
	 */
	public void setBookStatus(String userId,String book_id,int status,OnQueryCompleteListener onQueryCompleteListener){
		List<BasicNameValuePair>parms = new ArrayList<BasicNameValuePair>();
		parms.add(new BasicNameValuePair("user_id", userId));
		parms.add(new BasicNameValuePair("book_id", book_id));
		parms.add(new BasicNameValuePair("status", status+""));
		HttpUtils.makeAsyncPost(SETBOOKSTATUS, parms,
				new QueryCompleteHandler(onQueryCompleteListener, new QueryId()) {
					
					@Override
					public void handleResponse(String jsonResult, EHttpError error) {
						// TODO Auto-generated method stub
						if(jsonResult !=null&&error == EHttpError.KErrorNone){
							this.completeListener.onQueryComplete(new QueryId(), jsonResult, error);
						}
						else {
							this.completeListener.onQueryComplete(new QueryId(), null, error);
						}
					}
				});
	}
	
	/*
	 * @function   �޸��ѷ������״̬
	 * @param      
	 * @return
	 */
	public void setWishStatus(String userId,String username,String wish_id,int status,OnQueryCompleteListener onQueryCompleteListener){
		List<BasicNameValuePair>parms = new ArrayList<BasicNameValuePair>();
		parms.add(new BasicNameValuePair("user_id", userId));
		parms.add(new BasicNameValuePair("wish_id", wish_id));
		parms.add(new BasicNameValuePair("status", status+""));
		parms.add(new BasicNameValuePair("username", username));
		HttpUtils.makeAsyncPost(SETWISHSTATUS, parms,
				new QueryCompleteHandler(onQueryCompleteListener, new QueryId()) {
					
					@Override
					public void handleResponse(String jsonResult, EHttpError error) {
						// TODO Auto-generated method stub
						if(jsonResult !=null&&error == EHttpError.KErrorNone){
							this.completeListener.onQueryComplete(new QueryId(), jsonResult, error);
						}
						else {
							this.completeListener.onQueryComplete(new QueryId(), null, error);
						}
					}
				});
	}
	
	/*
	 * @function  �õ���Ϣ�б�
	 * @param     
	 * @return    
	 */
	public void getMessageList(String userId,OnQueryCompleteListener onQueryCompleteListener){
		List<BasicNameValuePair>parms = new ArrayList<BasicNameValuePair>();
		parms.add(new BasicNameValuePair("user_id", userId));
		HttpUtils.makeAsyncPost(GETMESSAGELISTACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener, new QueryId()) {
					
					@Override
					public void handleResponse(String jsonResult, EHttpError error) {
						// TODO Auto-generated method stub
						Map<String, Object>messages;
						if(jsonResult!=null&&error==EHttpError.KErrorNone){
							Gson gson = new Gson();
							Type type = new TypeToken<Map<String, Object>>() {
							}.getType();
							messages = gson.fromJson(jsonResult, type);
							List<Map<String, Object>>list = (List<Map<String, Object>>) messages.get("messages");
							System.out.println("��Ϣ�б�"+list);
							this.completeListener.onQueryComplete(new QueryId(), list, error);
						}
						else {
							this.completeListener.onQueryComplete(new QueryId(), null, error);
						}
					}
				});
	}

}
