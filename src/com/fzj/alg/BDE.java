package com.fzj.alg;

import java.util.ArrayList;
import java.util.List;

import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.solution.ASolution;
import com.fzj.solution.FourTupple;
import com.fzj.utils.FileUtils;
import com.fzj.utils.MathUtils;

/** 
 * @author Fan Zhengjie 
 * @date 2017年2月3日 下午7:14:57 
 * @version 1.0 
 * @description 改进的DNSPSO算法
 */
public class BDE extends AStrategy{
	
	public ASolution m_aTC_gBest;//全局最优粒子
	private double m_aI8_f;// 用于DE的变异
	private double m_aI8_Cr;// 用于DE的交叉
	private int m_aI4_vmax;// 最大速度
	private int m_aI4_vmin;
	
	private FourTupple[] m_aTC_ft;
	private FourTupple m_aTC_ft_gBest;
	
    public BDE() {
    	super();
	}

    public BDE(int f_aI4_size,int f_aI4_max_nfe,int f_aI4_max_iter,SSModel f_aTC_ssm,String f_str_data_path){
		super(f_aI4_size, f_aI4_max_nfe, f_aI4_max_iter,f_aTC_ssm,f_str_data_path);
	}
    
	@Override
	protected void init() {
		
		this.m_aI8_f = 0.5;
		this.m_aI8_Cr = 0.9;
		this.m_aTC_population = new ASolution[m_aI4_size];
		this.m_aTC_ft = new FourTupple[m_aI4_size];
		
		this.m_aI4_vmax = 5;
		this.m_aI4_vmin = -5;
		
		m_str_alg_name = NameSpace.s_str_bde;
		m_str_file_name = FileUtils.getResultName(m_str_alg_name, NameSpace.s_str_file_txt,m_aI4_max_nfe);
	}
	

	
	/**
	 * 初始化种群后，位移的初始化
	 */
	private void initV(){
		for(int t_aI4_k=0;t_aI4_k<m_aI4_size;t_aI4_k++){
			ASolution t_aTC_particle = m_aTC_population[t_aI4_k];
			int[] t_aI4_x = t_aTC_particle.getM_rI4_x();
			t_aTC_particle.setM_rI8_v(int2doubleArray(t_aI4_x));
			t_aTC_particle.setM_rI4_v2(t_aI4_x);
		}
	}
	
	private void evaluate2(FourTupple f_aTC_s,int f_aI4_x){
		
		double[] t_rI8_x = f_aTC_s.getM_rI8_x();
		double t_aI8_f = Math.sin(2*Math.PI*(f_aI4_x-t_rI8_x[0]))*t_rI8_x[1]*Math.cos(2*Math.PI*(f_aI4_x-t_rI8_x[0])*t_rI8_x[2])+t_rI8_x[3];
		f_aTC_s.setM_aI8_g(t_aI8_f);
	}
	
	@Override
	protected void initPopulation(String f_str_alg_name) {//这样每个都一样
		double[] t_rI8_x = new double[4];
		double[] t_rI8_v = new double[4];
		double[] t_rI8_a = {0,1,1,0};
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
			ASolution t_aTC_solution = new ASolution(m_aI4_d);
			int[] t_rI4_x = new int[m_aI4_d];
			
			if(t_aI4_i>0){
				for(int t_aI4_j=0;t_aI4_j<4;t_aI4_j++){
					t_rI8_x[t_aI4_j] = MathUtils.getDoubleAToB(-5, m_aI4_vmax);
					t_rI8_v[t_aI4_j] = t_rI8_x[t_aI4_j];
				}
			}else {
				for(int t_aI4_j=0;t_aI4_j<4;t_aI4_j++){
					t_rI8_x[t_aI4_j] = t_rI8_a[t_aI4_j];
					t_rI8_v[t_aI4_j] = t_rI8_a[t_aI4_j];
				}
			}
			
			FourTupple t_aTC_f = new FourTupple();
			t_aTC_f.setM_rI8_x(t_rI8_x);
			t_aTC_f.setM_rI8_v(t_rI8_v);
			m_aTC_ft[t_aI4_i] = t_aTC_f;
			
			for(int t_aI4_j=0;t_aI4_j<m_aI4_d;t_aI4_j++){	
				evaluate2(t_aTC_f,t_aI4_j);
				if(Double.compare(t_aTC_f.getM_aI8_g(), 0)>0){
					t_rI4_x[t_aI4_j] = 1;
				}else {
					t_rI4_x[t_aI4_j] = 0;
				}
			}

			t_aTC_solution.setM_rI4_x(t_rI4_x);
		
			m_aTC_population[t_aI4_i] = t_aTC_solution;
			modify(m_aTC_population[t_aI4_i]);
			evaluate(m_aTC_population[t_aI4_i]);
			
		}
	}
	
	/**
	 * 全局最优和历史最优粒子的更新
	 */
	private void initBest(){
		this.m_aTC_gBest = new ASolution(m_aI4_d);
		m_aTC_gBest.setM_aI8_fitness(Double.MIN_VALUE);
		
		for(int t_aI4_k=0;t_aI4_k<m_aI4_size;t_aI4_k++){
			if(Double.compare(m_aTC_gBest.getM_aI8_fitness(), m_aTC_population[t_aI4_k].getM_aI8_fitness())<0){
				m_aTC_gBest =  m_aTC_population[t_aI4_k].clone();
			}
		}
	}
	
	/**
	 * 更新pBest和gBest
	 * @param f_aI4_k
	 */
	private void updateBest(int f_aI4_k){
		
		if(Double.compare(m_aTC_gBest.getM_aI8_fitness(), m_aTC_population[f_aI4_k].getM_aI8_fitness())<0){
			m_aTC_gBest = m_aTC_population[f_aI4_k].clone();
			m_aTC_ft_gBest = m_aTC_ft[f_aI4_k].clone();
		}
	}
	
	private void updateAsolution(FourTupple f_aTC_s, int f_aI4_index){
		ASolution t_aTC_so = m_aTC_population[f_aI4_index];
		int t_rI4_x[] = t_aTC_so.getM_rI4_x();
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_d; t_aI4_i++) {
			evaluate2(f_aTC_s,t_aI4_i);
			if(Double.compare(f_aTC_s.getM_aI8_g(), 0)>0){
				t_rI4_x[t_aI4_i] = 1;
			}else {
				t_rI4_x[t_aI4_i] = 0;
			}
		}
	}
	
	private ASolution generateFT(FourTupple f_aTC_s,ASolution f_aTC_so){
		ASolution t_aTC_so = f_aTC_so.clone();
		int[] t_rI4_x = t_aTC_so.getM_rI4_x();
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
			evaluate2(f_aTC_s,t_aI4_i);
			if(Double.compare(f_aTC_s.getM_aI8_g(), 0)>0){
				t_rI4_x[t_aI4_i] = 1;
			}else {
				t_rI4_x[t_aI4_i] = 0;
			}
		}
		modify(t_aTC_so);
		evaluate(t_aTC_so);
		saveFitness(m_aTC_gBest);
		m_aI4_cur_nfe++;
		return t_aTC_so;
	}
	

	@Override
	protected void evolution() {
		while(m_aI4_cur_nfe < m_aI4_max_nfe){
			//第三阶段
			{
				// 遍历种群中的每个解，应用DE的变异、交叉、选择操作
				int t_aI4_r1, t_aI4_r2, t_aI4_r3;
				for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
					FourTupple t_aTC_pit = m_aTC_ft[t_aI4_i];
					double[] t_rI8_xi = t_aTC_pit.getM_rI8_x();
				
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

					FourTupple t_aTC_particle1 =  m_aTC_ft[t_aI4_r1];
					FourTupple t_aTC_particle2 =  m_aTC_ft[t_aI4_r2];
					FourTupple t_aTC_particle3 =  m_aTC_ft[t_aI4_r3];
					double[] t_rI4_xr1 = t_aTC_particle1.getM_rI8_x();
					double[] t_rI4_xr2 = t_aTC_particle2.getM_rI8_x();
					double[] t_rI4_xr3 = t_aTC_particle3.getM_rI8_x();
					

					double[] t_rI4_vig2 = new double[4];// 变异产生的中间变体
					for (int t_aI4_j = 0; t_aI4_j < 4; t_aI4_j++) {
						t_rI4_vig2[t_aI4_j] =  (t_rI4_xr1[t_aI4_j] + m_aI8_f * (t_rI4_xr2[t_aI4_j] - t_rI4_xr3[t_aI4_j]));
					/*	if (Double.compare(t_rI4_vig2[t_aI4_j], m_aI4_vmax) > 0) {
							t_rI4_vig2[t_aI4_j] = m_aI4_vmax;
						} else if (Double.compare(t_rI4_vig2[t_aI4_j], m_aI4_vmin) < 0) {
							t_rI4_vig2[t_aI4_j] = m_aI4_vmin;
						}*/
					}

					// 交叉
					int t_aI4_ri = m_aTC_random.nextInt(m_aI4_size);
					double[] t_rI8_ui = new double[4];
					for (int t_aI4_j = 0; t_aI4_j < 4; t_aI4_j++) {
						double t_aI8_r = m_aTC_random.nextDouble();
						if (Double.compare(t_aI8_r, m_aI8_Cr) < 0 || t_aI4_j == t_aI4_ri) {
							t_rI8_ui[t_aI4_j] = t_rI4_vig2[t_aI4_j];
						} else {
							t_rI8_ui[t_aI4_j] = t_rI8_xi[t_aI4_j];
						}
					}

					FourTupple t_aTC_part = m_aTC_ft[t_aI4_i].clone();
					t_aTC_part.setM_rI8_x(t_rI8_ui);
					updateAsolution(t_aTC_part, t_aI4_i);
					modify(m_aTC_population[t_aI4_i]);
					evaluate(m_aTC_population[t_aI4_i]);
					saveFitness(m_aTC_gBest);
					m_aI4_cur_nfe++;

					ASolution t_aTC_so1 = generateFT(t_aTC_part, m_aTC_population[t_aI4_i]);
					ASolution t_aTC_so2 = generateFT(t_aTC_pit, m_aTC_population[t_aI4_i]);
					if(Double.compare(t_aTC_so1.getM_aI8_fitness(), t_aTC_so2.getM_aI8_fitness())>=0){
						m_aTC_ft[t_aI4_i] = t_aTC_part;
					}
					
					updateBest(t_aI4_i);
				}
			}
			
			m_aI4_cur_iter++;
		}
		System.out.println(m_str_alg_name+" m_aI4_cur_nfe = "+m_aI4_cur_nfe);
		System.out.println(m_str_alg_name+" m_aI4_cur_iter = "+m_aI4_cur_iter);
	}

	@Override
	protected ASolution solve(int f_aI4_p) {
		//初始化
		{
			init();
			initPopulation(NameSpace.s_str_bde);
			initV();
			initBest();
		}
				
		//打印函数，用于测试调试
		{
			/*System.out.println("==========初始化种群==========");
			printPopulation(m_aTC_population);
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
		AStrategy t_aTC_strategy = new BDE(10, 1000, 20, t_aTC_ssm, NameSpace.s_str_data_01);	
	    t_aTC_strategy.solve(0);
	}
	

}
