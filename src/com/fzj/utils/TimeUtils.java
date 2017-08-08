package com.fzj.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author dell
 * @date 2017年3月8日 下午8:01:30
 * @title 时间工具类
 * @description 
 */
public class TimeUtils {

	/**
	 * 以指定格式返回当前时间
	 * @return
	 */
	public static String getCurrentTime(){
		SimpleDateFormat t_aTC_sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String t_str_time = t_aTC_sdf.format(new Date());
		return t_str_time;
	}
	
	/**
	 * 自定义格式返回当前时间
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
		long mileSec = diff % 1000;//毫秒 +
		long seconds = diff / 1000;//秒
		long sec = seconds % 60;//秒 +
		long minute = seconds / 60;//分
		long min = minute % 60;//分 +
		long hour = minute / 60; //小時+
		time += hour+"小时,"+min+"分,"+sec+"秒,"+mileSec+"毫秒";
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
