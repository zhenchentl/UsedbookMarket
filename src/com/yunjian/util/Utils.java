package com.yunjian.util;

import com.yunjian.connection.ConnectionConfigReader;


public class Utils {
	public static String user_id = "";
	public static String password = ""; 
	public static String username = "";
	
	public static String URL = ConnectionConfigReader.DEFAULT_REMOTE_HOST
			+ "img/getUserImg?user_id=";
	
	public static String IMGURL = ConnectionConfigReader.DEFAULT_REMOTE_HOST
			+ "img/getImg?img_id=";
	//�Ƿ��Ǳ༭�鼮����  0�����ǣ�1������
	public static int IFEDITBOOK = 0; 
	//�Ƿ��Ǳ༭��Ը�鵥����  0�����ǣ�1������
    public static int IFEDITWISH = 0; 
    //�Ƿ��Ǳ༭������Ϣ����  0�����ǣ�1������
    public static int IFEDITPERSON = 0;
    
    public static String[]booktypeStrings = {"�̲�����","Ӣ��ǿ��","����ǿ��","��������","����ר��","�����Ķ�"};
}
