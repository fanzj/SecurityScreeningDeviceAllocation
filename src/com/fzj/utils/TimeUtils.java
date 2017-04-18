package com.fzj.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author dell
 * @date 2017��3��8�� ����8:01:30
 * @title ʱ�乤����
 * @description 
 */
public class TimeUtils {

	/**
	 * ��ָ����ʽ���ص�ǰʱ��
	 * @return
	 */
	public static String getCurrentTime(){
		SimpleDateFormat t_aTC_sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String t_str_time = t_aTC_sdf.format(new Date());
		return t_str_time;
	}
	
	/**
	 * �Զ����ʽ���ص�ǰʱ��
	 * @param f_aTC_sdf
	 * @return
	 */
	public static String getCurrentTime(SimpleDateFormat f_aTC_sdf){
		String t_str_time = f_aTC_sdf.format(new Date());
		return t_str_time;
	}
}
