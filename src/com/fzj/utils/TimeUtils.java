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
	
	public static String getRunTime(long start, long end){
		String time = "";
		long diff = end - start;
		long mileSec = diff % 1000;//���� +
		long seconds = diff / 1000;//��
		long sec = seconds % 60;//�� +
		long minute = seconds / 60;//��
		long min = minute % 60;//�� +
		long hour = minute / 60; //С�r+
		time += hour+"Сʱ,"+min+"��,"+sec+"��,"+mileSec+"����";
		return time;
	}
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		try {
			Thread.sleep(30121);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end  = System.currentTimeMillis();
		System.out.println(getRunTime(start, end));
	}
}
