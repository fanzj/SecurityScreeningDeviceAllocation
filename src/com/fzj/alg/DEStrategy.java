package com.fzj.alg;

import java.util.ArrayList;
import java.util.List;

import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.solution.ASolution;
import com.fzj.solution.FireSparkSolution;
import com.fzj.solution.ParticleSolution;
import com.fzj.utils.FileUtils;

/** 
 * @author Fan Zhengjie 
 * @date 2017年2月3日 下午7:14:57 
 * @version 1.0 
 * @description 改进的DNSPSO算法
 */
public class DEStrategy extends AStrategy{
	

	private int m_aI4_xmax;//位移上限
	private int m_aI4_xmin;
	private int m_aI4_ymax;//位移上限
	private int m_aI4_ymin;
	public ParticleSolution m_aTC_gBest;//全局最优粒子
	private ParticleSolution[] m_aTC_pBest;//历史最优粒子
	

	
	private double m_aI8_f;// 用于DE的变异
	private double m_aI8_Cr;// 用于DE的交叉
	
    public DEStrategy() {
    	super();
	}

    public DEStrategy(int f_aI4_size,int f_aI4_max_nfe,int f_aI4_max_iter,SSModel f_aTC_ssm,String f_str_data_path, long f_aI8_max_time){
		super(f_aI4_size, f_aI4_max_nfe, f_aI4_max_iter,f_aTC_ssm,f_str_data_path, f_aI8_max_time);
	}
    
	@Override
	protected void init() {
	
		this.m_aI8_f = 0.5;
		this.m_aI8_Cr = 0.9;
	
		this.m_aTC_population = new ParticleSolution[m_aI4_size];
	
		this.m_aI4_xmax = m_aI4_k;
		this.m_aI4_xmin = 1;
		this.m_aI4_ymax = m_aI4_q;
		this.m_aI4_ymin = 0;
		
		m_str_alg_name = NameSpace.s_str_de;
		m_str_file_name = FileUtils.getResultName(m_str_alg_name, NameSpace.s_str_file_txt,m_aI4_max_nfe);
	}
	

	
	/**
	 * 初始化种群后，位移的初始化
	 */
	private void initV(){
		for(int t_aI4_k=0;t_aI4_k<m_aI4_size;t_aI4_k++){
			ParticleSolution t_aTC_particle = (ParticleSolution) m_aTC_population[t_aI4_k];
			int[] t_aI4_x = t_aTC_particle.getM_rI4_x();
			t_aTC_particle.setM_rI4_v(t_aI4_x);
		}
	}
	/**
	 * 全局最优和历史最优粒子的更新
	 */
	private void initBest(){
		this.m_aTC_pBest = new ParticleSolution[m_aI4_size];
		this.m_aTC_gBest = new ParticleSolution(m_aI4_d);
		m_aTC_gBest.setM_aI8_fitness(Double.MIN_VALUE);
		
		for(int t_aI4_k=0;t_aI4_k<m_aI4_size;t_aI4_k++){
			m_aTC_pBest[t_aI4_k] = ((ParticleSolution) (m_aTC_population[t_aI4_k])).clone();
			if(Double.compare(m_aTC_gBest.getM_aI8_fitness(), m_aTC_pBest[t_aI4_k].getM_aI8_fitness())<0){
				m_aTC_gBest = (ParticleSolution) m_aTC_pBest[t_aI4_k].clone();
			}
		}
	}
	
	/**
	 * 更新pBest和gBest
	 * @param f_aI4_k
	 */
	private void updateBest(int f_aI4_k){
		if(Double.compare(m_aTC_pBest[f_aI4_k].getM_aI8_fitness(), m_aTC_population[f_aI4_k].getM_aI8_fitness())<0){
			m_aTC_pBest[f_aI4_k] = ((ParticleSolution) m_aTC_population[f_aI4_k]).clone();
		}
		if(Double.compare(m_aTC_gBest.getM_aI8_fitness(), m_aTC_pBest[f_aI4_k].getM_aI8_fitness())<0){
			m_aTC_gBest = m_aTC_pBest[f_aI4_k].clone();
		}
	}
	

	@Override
	protected void evolution() {
		long t_aI8_start = System.currentTimeMillis();
		while(m_aI4_cur_nfe<=m_aI4_max_nfe) {
			//第三阶段
			{
				// 遍历种群中的每个解，应用DE的变异、交叉、选择操作
				int t_aI4_r1, t_aI4_r2, t_aI4_r3;
				for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
					ParticleSolution t_aTC_particle = (ParticleSolution) m_aTC_population[t_aI4_i];
					int[] t_rI4_xi = t_aTC_particle.getM_rI4_x();

					// 变异
					t_aI4_r1 = m_aTC_random.nextInt(m_aI4_size);
					while (t_aI4_r1 == t_aI4_i) {
						t_aI4_r1 = m_aTC_random.nextInt(m_aI4_size);
					}
					t_aI4_r2 = m_aTC_random.nextInt(m_aI4_size);
					while (t_aI4_i == t_aI4_r2 || t_aI4_r1 == t_aI4_r2) {
						t_aI4_r2 = m_aTC_random.nextInt(m_aI4_size);
					}
					t_aI4_r3 = m_aTC_random.nextInt(m_aI4_size);
					while (t_aI4_i == t_aI4_r3 || t_aI4_r1 == t_aI4_r3 || t_aI4_r2 == t_aI4_r3) {
						t_aI4_r3 = m_aTC_random.nextInt(m_aI4_size);
					}

					ParticleSolution t_aTC_particle1 = (ParticleSolution) m_aTC_population[t_aI4_r1];
					ParticleSolution t_aTC_particle2 = (ParticleSolution) m_aTC_population[t_aI4_r2];
					ParticleSolution t_aTC_particle3 = (ParticleSolution) m_aTC_population[t_aI4_r3];
					int[] t_rI4_xr1 = t_aTC_particle1.getM_rI4_x();
					int[] t_rI4_xr2 = t_aTC_particle2.getM_rI4_x();
					int[] t_rI4_xr3 = t_aTC_particle3.getM_rI4_x();

					int[] t_rI4_vig2 = new int[m_aI4_d];// 变异产生的中间变体
					for (int t_aI4_j = 0; t_aI4_j < m_aI4_d; t_aI4_j++) {
						t_rI4_vig2[t_aI4_j] = (int) (t_rI4_xr1[t_aI4_j]
								+ Math.round(m_aI8_f * (t_rI4_xr2[t_aI4_j] - t_rI4_xr3[t_aI4_j])));
						if(t_aI4_j%2==0){
							if(t_rI4_vig2[t_aI4_j] > m_aI4_xmax){
								t_rI4_vig2[t_aI4_j] = m_aI4_xmax;
							}else if(t_rI4_vig2[t_aI4_j] < m_aI4_xmin){
								t_rI4_vig2[t_aI4_j] = m_aI4_xmin;
							}
						}else {
							if(t_rI4_vig2[t_aI4_j] > m_aI4_ymax){
								t_rI4_vig2[t_aI4_j] = m_aI4_ymax;
							}else if(t_rI4_vig2[t_aI4_j] < m_aI4_ymin){
								t_rI4_vig2[t_aI4_j] = m_aI4_ymin;
							}
						}
					}

					// 交叉
					int t_aI4_ri = m_aTC_random.nextInt(m_aI4_size);
					int[] t_rI4_ui = new int[m_aI4_d];
					for (int t_aI4_j = 0; t_aI4_j < m_aI4_d; t_aI4_j++) {
						double t_aI8_r = m_aTC_random.nextDouble();
						if (Double.compare(t_aI8_r, m_aI8_Cr) < 0 || t_aI4_j == t_aI4_ri) {
							t_rI4_ui[t_aI4_j] = t_rI4_vig2[t_aI4_j];
						} else {
							t_rI4_ui[t_aI4_j] = t_rI4_xi[t_aI4_j];
						}
					}

					ParticleSolution t_aTC_part = ((ParticleSolution) m_aTC_population[t_aI4_i]).clone();
					t_aTC_part.setM_rI4_x(t_rI4_ui);
					modify(t_aTC_part);
					evaluate(t_aTC_part);
					saveFitness(m_aTC_gBest);
					m_aI4_cur_nfe++;

					if (Double.compare(t_aTC_part.getM_aI8_fitness(), t_aTC_particle.getM_aI8_fitness()) >= 0) {
						m_aTC_population[t_aI4_i] = t_aTC_part;
					}
					updateBest(t_aI4_i);
				}
			}
			
			m_aI4_cur_iter++;
		}
		
		System.out.println(m_str_alg_name+" m_aI8_time = "+(System.currentTimeMillis() - t_aI8_start)/1000.0);
		System.out.println(m_str_alg_name+" m_aI4_cur_nfe = "+m_aI4_cur_nfe);
		System.out.println(m_str_alg_name+" m_aI4_cur_iter = "+m_aI4_cur_iter);
	}
	
	
	
	
	
	@Override
	protected ASolution solve(int f_aI4_p) {
		//初始化
		{
			init();
			initPopulation(NameSpace.s_str_dednspso);
			initV();
			initBest();
		}
				
		//打印函数，用于测试调试
		{
			/*System.out.println("==========初始化种群==========");
			printPopulation(m_aTC_population);
			System.out.println("==========历史最优种群==========");
			printPopulation(m_aTC_pBest);
			System.out.println("==========全局最优粒子==========");
			printSolution(m_aTC_gBest);*/
		}
				
		//种群进化
		{
			evolution();
		}
				
		//结果的保存
		{
			String t_str_result_content = getResultContent(f_aI4_p, m_aTC_gBest);
			FileUtils.saveFile(m_str_data_path, m_str_alg_name, m_str_file_name, t_str_result_content);
		}
		return m_aTC_gBest;
	}
	
	public static void main(String[] args) {
		SSModel t_aTC_ssm = new SSModel(NameSpace.s_str_data_01);
		AStrategy t_aTC_strategy = new DEStrategy(10, 1000, 20, t_aTC_ssm, NameSpace.s_str_data_01,1000);	
	    t_aTC_strategy.solve(0);
	}
	

}
