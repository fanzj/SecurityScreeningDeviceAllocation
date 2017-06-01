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
public class DE_DNSPSOStrategy extends AStrategy{
	
	private double m_aI8_c1;//学习因子
	private double m_aI8_c2;//学习因子
	private int m_aI4_vmax;//最大速度
	private int m_aI4_vmin;
	private int m_aI4_xmax;//位移上限
	private int m_aI4_xmin;
	private int m_aI4_ymax;//位移上限
	private int m_aI4_ymin;
	private double m_aI8_w;//惯性权重
	private double m_aI8_wmin;//最小权重系数
	private double m_aI8_wmax;//最大权重系数
	public ParticleSolution m_aTC_gBest;//全局最优粒子
	private ParticleSolution[] m_aTC_pBest;//历史最优粒子
	
	//DNSPSO相比PSO增加的参数
	private double m_aI8_pr;//用于多样性增加机制
	private double m_aI8_pns;//用于邻域搜索策略
	private int m_aI4_K;
	private double m_aI8_r1,m_aI8_r2,m_aI8_r3,m_aI8_r4,m_aI8_r5,m_aI8_r6;//用于邻域搜索生成的随机数
	
	private double m_aI8_f;// 用于DE的变异
	private double m_aI8_Cr;// 用于DE的交叉
	
    public DE_DNSPSOStrategy() {
    	super();
	}

    public DE_DNSPSOStrategy(int f_aI4_size,int f_aI4_max_nfe,int f_aI4_max_iter,SSModel f_aTC_ssm,String f_str_data_path, long f_aI8_max_time){
		super(f_aI4_size, f_aI4_max_nfe, f_aI4_max_iter,f_aTC_ssm,f_str_data_path, f_aI8_max_time);
	}
    
	@Override
	protected void init() {
		this.m_aI8_c1 = 1.49618;//1.49618
		this.m_aI8_c2 = 1.49618;
		this.m_aI8_wmin = 0.4;
		this.m_aI8_wmax = 0.9;
		this.m_aI8_pr = 0.9;
		this.m_aI8_pns = 0.6;
		this.m_aI4_K = 2;
		
		this.m_aI8_f = 0.5;
		this.m_aI8_Cr = 0.9;
		
		this.m_aI8_w = m_aI8_wmax;
		this.m_aTC_population = new ParticleSolution[m_aI4_size];
	
		this.m_aI4_vmax = 3;
		this.m_aI4_vmin = -3;
		this.m_aI4_xmax = m_aI4_k;
		this.m_aI4_xmin = 1;
		this.m_aI4_ymax = m_aI4_q;
		this.m_aI4_ymin = 0;
		
		m_str_alg_name = NameSpace.s_str_dednspso;
		m_str_file_name = FileUtils.getResultName(m_str_alg_name, NameSpace.s_str_file_txt,m_aI4_max_nfe);
	}
	
	/**
	 * 惯量权重的更新
	 */
	private void updateW(){
		m_aI8_w = m_aI8_wmax - ((double) m_aI4_cur_iter / (double) m_aI4_max_iter) * (m_aI8_wmax - m_aI8_wmin);
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
			
			//第二阶段
			{
				//评价次数m_aI4_size * 2
				randomR();
				for(int t_aI4_i=0;t_aI4_i<m_aI4_size;t_aI4_i++){
					double t_aI8_randi = m_aTC_random.nextDouble();
					if(Double.compare(t_aI8_randi, m_aI8_pns)<=0){
						ParticleSolution t_aTC_Pi = (ParticleSolution) m_aTC_population[t_aI4_i];
						ParticleSolution t_aTC_Li = LNS(t_aTC_Pi,t_aI4_i);//局部邻域搜索
						modify(t_aTC_Li);
						evaluate(t_aTC_Li);
						saveFitness(m_aTC_gBest);
						m_aI4_cur_nfe++;
						ParticleSolution t_aTC_Gi = GNS(t_aTC_Pi,t_aI4_i);//全局邻域搜索
						modify(t_aTC_Gi);
						evaluate(t_aTC_Gi);
						saveFitness(m_aTC_gBest);
						m_aI4_cur_nfe++;
						
						m_aTC_population[t_aI4_i] = selectFittest(t_aTC_Pi, t_aTC_Li, t_aTC_Gi);
					}
					updateBest(t_aI4_i);
				}
			}
			
			updateW();
			m_aI4_cur_iter++;
		} 
		
		System.out.println(m_str_alg_name+" m_aI8_time = "+(System.currentTimeMillis() - t_aI8_start)/1000.0);
		System.out.println(m_str_alg_name+" m_aI4_cur_nfe = "+m_aI4_cur_nfe);
		System.out.println(m_str_alg_name+" m_aI4_cur_iter = "+m_aI4_cur_iter);
	}
	
	/**
	 * 每代生成随机系数
	 */
	private void randomR(){
		m_aI8_r1 = m_aTC_random.nextDouble();
		m_aI8_r2 = m_aTC_random.nextDouble();
		while(Double.compare(m_aI8_r1+m_aI8_r2, 1)>0){
			m_aI8_r2 = m_aTC_random.nextDouble();
		}
		m_aI8_r3 = 1 - m_aI8_r1 - m_aI8_r2;
		m_aI8_r4 = m_aTC_random.nextDouble();
		m_aI8_r5 = m_aTC_random.nextDouble();
		while(Double.compare(m_aI8_r4+m_aI8_r5, 1)>0){
			m_aI8_r5 = m_aTC_random.nextDouble();
		}
		m_aI8_r6 = 1 - m_aI8_r4 - m_aI8_r5;
	}
	
	/**
	 * 局部搜索策略
	 * 生成Li
	 * @param f_aTC_Pi
	 * @param f_aI4_index
	 * @return
	 */
	private ParticleSolution LNS(ParticleSolution f_aTC_Pi,int f_aI4_index){
		ParticleSolution t_aTC_Li = f_aTC_Pi.clone();
		int[] t_rI4_lxi = new int[m_aI4_d];
		int[] t_rI4_lvi = new int[m_aI4_d];
		int[] t_rI4_pbestxi = m_aTC_pBest[f_aI4_index].getM_rI4_x();
		int[] t_rI4_xi = f_aTC_Pi.getM_rI4_x();
		int[] t_rI4_vi = f_aTC_Pi.getM_rI4_v();
		
		List<Integer> t_aTC_list = new ArrayList<Integer>();
		for(int t_aI4_i=1;t_aI4_i<=m_aI4_K;t_aI4_i++){
			t_aTC_list.add((f_aI4_index+t_aI4_i)%m_aI4_size);
			t_aTC_list.add((f_aI4_index-t_aI4_i+m_aI4_size)%m_aI4_size);
		}
		
		int t_aI4_x = m_aTC_random.nextInt(t_aTC_list.size());
		int t_aI4_y = m_aTC_random.nextInt(t_aTC_list.size());
		while(t_aI4_x==t_aI4_y){
			t_aI4_y = m_aTC_random.nextInt(t_aTC_list.size());
		}
		int t_aI4_c = t_aTC_list.get(t_aI4_x);
		int t_aI4_d = t_aTC_list.get(t_aI4_y);
		ParticleSolution t_aTC_Pc = (ParticleSolution) m_aTC_population[t_aI4_c];
		int[] t_rI4_xc = t_aTC_Pc.getM_rI4_x();
		ParticleSolution t_aTC_Pd = (ParticleSolution) m_aTC_population[t_aI4_d];
		int[] t_rI4_xd = t_aTC_Pd.getM_rI4_x();
		
		for(int t_aI4_j=0;t_aI4_j<m_aI4_d;t_aI4_j++){
			double t_aI8_temp_x = m_aI8_r1 * t_rI4_xi[t_aI4_j] + m_aI8_r2 * t_rI4_pbestxi[t_aI4_j] + m_aI8_r3 * (t_rI4_xc[t_aI4_j]-t_rI4_xd[t_aI4_j]);
			t_rI4_lxi[t_aI4_j] = new Double(Math.round(t_aI8_temp_x)).intValue();
			t_rI4_lvi[t_aI4_j] = t_rI4_vi[t_aI4_j];
		
			if(t_aI4_j%2==0){
				if(t_rI4_lxi[t_aI4_j] > m_aI4_xmax){
					t_rI4_lxi[t_aI4_j] = m_aI4_xmax;
				}else if(t_rI4_lxi[t_aI4_j] < m_aI4_xmin){
					t_rI4_lxi[t_aI4_j] = m_aI4_xmin;
				}
			}else {
				if(t_rI4_lxi[t_aI4_j] > m_aI4_ymax){
					t_rI4_lxi[t_aI4_j] = m_aI4_ymax;
				}else if(t_rI4_lxi[t_aI4_j] < m_aI4_ymin){
					t_rI4_lxi[t_aI4_j] = m_aI4_ymin;
				}
			}
		}
		t_aTC_Li.setM_rI4_x(t_rI4_lxi);
		t_aTC_Li.setM_rI4_v(t_rI4_lvi);
		
		return t_aTC_Li;
	}
	
	/**
	 * 全局邻域搜素策略
	 * @param f_aTC_Pi
	 * @param f_aI4_index
	 * @return
	 */
	private ParticleSolution GNS(ParticleSolution f_aTC_Pi,int f_aI4_index){
		ParticleSolution t_aTC_Gi = f_aTC_Pi.clone();
		int[] t_rI4_gxi = new int[m_aI4_d];
		int[] t_rI4_gvi = new int[m_aI4_d];
		int[] t_rI4_gbestxi = m_aTC_gBest.getM_rI4_x();
		int[] t_rI4_xi = f_aTC_Pi.getM_rI4_x();
		int[] t_rI4_vi = f_aTC_Pi.getM_rI4_v();
		
		int t_aI4_e = m_aTC_random.nextInt(m_aI4_size);
		int t_aI4_f = m_aTC_random.nextInt(m_aI4_size);
		while(t_aI4_e==f_aI4_index){
			t_aI4_e = m_aTC_random.nextInt(m_aI4_size);
		}
		while(t_aI4_f == t_aI4_e || t_aI4_f == f_aI4_index){
			t_aI4_f = m_aTC_random.nextInt(m_aI4_size);
		}
		
		ParticleSolution t_aTC_Pe = (ParticleSolution) m_aTC_population[t_aI4_e];
		int[] t_rI4_xe = t_aTC_Pe.getM_rI4_x();
		ParticleSolution t_aTC_Pf = (ParticleSolution) m_aTC_population[t_aI4_f];
		int[] t_rI4_xf = t_aTC_Pf.getM_rI4_x();
		
		for(int t_aI4_j=0;t_aI4_j<m_aI4_d;t_aI4_j++){
			double t_aI8_temp_x = m_aI8_r4 * t_rI4_xi[t_aI4_j] + m_aI8_r5 * t_rI4_gbestxi[t_aI4_j] + m_aI8_r6 * (t_rI4_xe[t_aI4_j]-t_rI4_xf[t_aI4_j]);
			t_rI4_gxi[t_aI4_j] = new Double(Math.round(t_aI8_temp_x)).intValue();
			t_rI4_gvi[t_aI4_j] = t_rI4_vi[t_aI4_j];
			if(t_aI4_j%2==0){
				if(t_rI4_gxi[t_aI4_j] > m_aI4_xmax){
					t_rI4_gxi[t_aI4_j] = m_aI4_xmax;
				}else if(t_rI4_gxi[t_aI4_j] < m_aI4_xmin){
					t_rI4_gxi[t_aI4_j] = m_aI4_xmin;
				}
			}else {
				if(t_rI4_gxi[t_aI4_j] > m_aI4_ymax){
					t_rI4_gxi[t_aI4_j] = m_aI4_ymax;
				}else if(t_rI4_gxi[t_aI4_j] < m_aI4_ymin){
					t_rI4_gxi[t_aI4_j] = m_aI4_ymin;
				}
			}
		}
		
		t_aTC_Gi.setM_rI4_x(t_rI4_gxi);
		t_aTC_Gi.setM_rI4_v(t_rI4_gvi);
		return t_aTC_Gi;
	}
	
	/**
	 * 从a,b,c三个粒子中选择最佳的一个返回
	 * @param f_aTC_a
	 * @param f_aTC_b
	 * @param f_aTC_c
	 * @return
	 */
	private ParticleSolution selectFittest(ParticleSolution f_aTC_a,ParticleSolution f_aTC_b,ParticleSolution f_aTC_c){
		ParticleSolution t_aTC_particle = null;
		if(Double.compare(f_aTC_a.getM_aI8_fitness(), f_aTC_b.getM_aI8_fitness())>0){
			if(Double.compare(f_aTC_a.getM_aI8_fitness(),f_aTC_c.getM_aI8_fitness())>0)
				t_aTC_particle = f_aTC_a;
			else
				t_aTC_particle = f_aTC_c;
		}else {
			if(Double.compare(f_aTC_b.getM_aI8_fitness(), f_aTC_c.getM_aI8_fitness())>0)
				t_aTC_particle = f_aTC_b;
			else
				t_aTC_particle = f_aTC_c;
		}
		
		return t_aTC_particle;
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
		AStrategy t_aTC_strategy = new DE_DNSPSOStrategy(10, 1000, 20, t_aTC_ssm, NameSpace.s_str_data_01,1000);	
	    t_aTC_strategy.solve(0);
	}
	

}
