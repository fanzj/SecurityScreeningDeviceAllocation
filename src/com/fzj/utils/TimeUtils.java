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
}
