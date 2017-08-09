package com.fzj.alg.thread;

import java.util.Properties;

import com.fzj.config.NameSpace;
import com.fzj.utils.MProperties;

/** 
 * @author Fan Zhengjie 
 * @date 2017年2月3日 下午6:21:02 
 * @version 1.0 
 * @description
 */
public class RunThread {
	
	public static final int m_aI4_sec = 1000;

	public static void main(String[] args) {
		Properties t_aTC_p = MProperties.get(NameSpace.s_str_run_config);
		int t_aI4_size = Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_population_size));
		int t_aI4_run_times = Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_run_times));
		int t_aI4_max_nfe = Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_nfe));
		long t_aI8_max_time = (long) (m_aI4_sec * Double.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_time)));//转换成毫秒
		String t_str_data_path = NameSpace.s_str_data_05;
		
		Thread t_aTC_dnspso_thread = new AlgThread(NameSpace.s_str_dnspso,t_aI4_size, t_aI4_max_nfe, Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_iter)), t_aI4_run_times,t_str_data_path,t_aI8_max_time);
		t_aTC_dnspso_thread.start();
		
		Thread t_aTC_de_dnspso_thread = new AlgThread(NameSpace.s_str_dednspso,t_aI4_size, t_aI4_max_nfe, Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_iter)), t_aI4_run_times,t_str_data_path,t_aI8_max_time);
		t_aTC_de_dnspso_thread.start();
		
		Thread t_aTC_fade_thread = new AlgThread(NameSpace.s_str_fade,t_aI4_size, t_aI4_max_nfe, Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_iter)), t_aI4_run_times,t_str_data_path,t_aI8_max_time);
		t_aTC_fade_thread.start();
		
		Thread t_aTC_wwo_thread = new AlgThread(NameSpace.s_str_wwo,t_aI4_size, t_aI4_max_nfe, Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_iter)), t_aI4_run_times,t_str_data_path,t_aI8_max_time);
		t_aTC_wwo_thread.start();
		
		Thread t_aTC_de_thread = new AlgThread(NameSpace.s_str_de,t_aI4_size, t_aI4_max_nfe, Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_iter)), t_aI4_run_times,t_str_data_path,t_aI8_max_time);
		t_aTC_de_thread.start();
		
		Thread t_aTC_ga_thread = new AlgThread(NameSpace.s_str_ga,t_aI4_size, t_aI4_max_nfe, Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_iter)), t_aI4_run_times,t_str_data_path,t_aI8_max_time);
		t_aTC_ga_thread.start();
	}
}
