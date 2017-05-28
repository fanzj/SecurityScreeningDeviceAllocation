package com.fzj.alg.thread;

import com.fzj.alg.AlgRun;

/**
 * 算法线程类
 * @author Fantasy
 *
 */
public class AlgThread extends Thread{
	
	protected String m_str_alg_name;//算法名称
	protected int m_aI4_size;//种群规模
    protected int m_aI4_max_nfe;//最大评价次数
	protected int m_aI4_max_iter;//最大迭代次数
	protected int m_aI4_run_times;//算法运行次数
	protected String m_str_data_path;//数据路径
	protected long m_aI8_max_time;//最大运行时间
	
	public AlgThread(){}
	
	public AlgThread(String f_str_alg_name,int f_aI4_size,int f_aI4_max_nfe,int f_aI4_max_iter,int f_aI4_run_times,String f_str_data_path, long f_aI8_max_time){
		this.m_str_alg_name = f_str_alg_name;
		this.m_aI4_size = f_aI4_size;
		this.m_aI4_max_nfe = f_aI4_max_nfe;
		this.m_aI4_max_iter = f_aI4_max_iter;
		this.m_aI4_run_times = f_aI4_run_times;
		this.m_str_data_path = f_str_data_path;
		this.m_aI8_max_time = f_aI8_max_time;
	}
	
	

	@Override
	public void run() {
	
		AlgRun t_aTC_run = new AlgRun(m_str_alg_name,m_str_data_path,m_aI4_size,m_aI4_max_nfe,m_aI4_max_iter,m_aI8_max_time);
		t_aTC_run.algRun(m_aI4_run_times);
		
	}
	
	

}