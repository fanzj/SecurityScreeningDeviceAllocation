package com.fzj.alg.thread;

import com.fzj.alg.AlgRun;

/**
 * �㷨�߳���
 * @author Fantasy
 *
 */
public class AlgThread extends Thread{
	
	protected String m_str_alg_name;//�㷨����
	protected int m_aI4_size;//��Ⱥ��ģ
    protected int m_aI4_max_nfe;//������۴���
	protected int m_aI4_max_iter;//����������
	protected int m_aI4_run_times;//�㷨���д���
	protected String m_str_data_path;//����·��
	
	public AlgThread(){}
	
	public AlgThread(String f_str_alg_name,int f_aI4_size,int f_aI4_max_nfe,int f_aI4_max_iter,int f_aI4_run_times,String f_str_data_path){
		this.m_str_alg_name = f_str_alg_name;
		this.m_aI4_size = f_aI4_size;
		this.m_aI4_max_nfe = f_aI4_max_nfe;
		this.m_aI4_max_iter = f_aI4_max_iter;
		this.m_aI4_run_times = f_aI4_run_times;
		this.m_str_data_path = f_str_data_path;
	}
	
	

	@Override
	public void run() {
	
		AlgRun t_aTC_run = new AlgRun(m_str_alg_name,m_str_data_path,m_aI4_size,m_aI4_max_nfe,m_aI4_max_iter);
		t_aTC_run.algRun(m_aI4_run_times);
		
	}
	
	

}