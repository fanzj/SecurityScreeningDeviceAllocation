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

	public static void main(String[] args) {
		Properties t_aTC_p = MProperties.get(NameSpace.s_str_run_config);
		int t_aI4_size = Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_population_size));
		int t_aI4_run_times = Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_run_times));
		int t_aI4_max_nfe = Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_nfe));
		String t_str_data_path = NameSpace.s_str_data_03;
		
		Thread t_aTC_bde_thread = new AlgThread(NameSpace.s_str_bde,t_aI4_size, t_aI4_max_nfe, Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_iter_bde)), t_aI4_run_times,t_str_data_path);
		t_aTC_bde_thread.start();
		
		Thread t_aTC_ampso_thread = new AlgThread(NameSpace.s_str_ampso,t_aI4_size, t_aI4_max_nfe, Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_iter_ampso)), t_aI4_run_times,t_str_data_path);
		t_aTC_ampso_thread.start();
		
		Thread t_aTC_ibpso_thread = new AlgThread(NameSpace.s_str_ibpso,t_aI4_size, t_aI4_max_nfe, Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_iter_ibpso)), t_aI4_run_times,t_str_data_path);
		t_aTC_ibpso_thread.start();
		
		Thread t_aTC_bpso_thread = new AlgThread(NameSpace.s_str_bpso,t_aI4_size, t_aI4_max_nfe, Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_iter_bpso)), t_aI4_run_times,t_str_data_path);
		t_aTC_bpso_thread.start();
		
		Thread t_aTC_ga_thread = new AlgThread(NameSpace.s_str_ga,t_aI4_size, t_aI4_max_nfe, Integer.valueOf(t_aTC_p.getProperty(NameSpace.s_str_max_iter_ga)), t_aI4_run_times,t_str_data_path);
		t_aTC_ga_thread.start();
		
		
	}
}
