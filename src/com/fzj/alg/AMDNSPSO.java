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
 * @description DNSPSO算法
 */
public class AMDNSPSO extends AStrategy {

	private double m_aI8_c1;// 学习因子
	private double m_aI8_c2;// 学习因子
	private int m_aI4_vmax;// 最大速度
	private int m_aI4_vmin;
	private double m_aI8_w;// 惯性权重

	public ASolution m_aTC_gBest;// 全局最优粒子
	private ASolution[] m_aTC_pBest;// 历史最优粒子

	// DNSPSO相比PSO增加的参数
	private double m_aI8_pr;// 用于多样性增加机制
	private double m_aI8_pns;// 用于邻域搜索策略
	private int m_aI4_K;
	private double m_aI8_r1, m_aI8_r2, m_aI8_r3, m_aI8_r4, m_aI8_r5, m_aI8_r6;// 用于邻域搜索生成的随机数
	
	private FourTupple[] m_aTC_ft;
	private FourTupple m_aTC_ft_gBest;
	private FourTupple[] m_aTC_ft_pBest;

	public AMDNSPSO() {
		super();
	}

	public AMDNSPSO(int f_aI4_size, int f_aI4_max_nfe, int f_aI4_max_iter, SSModel f_aTC_ssm, String f_str_data_path) {
		super(f_aI4_size, f_aI4_max_nfe, f_aI4_max_iter, f_aTC_ssm, f_str_data_path);
	}

	@Override
	protected void init() {
		this.m_aI8_c1 = 1.49618;
		this.m_aI8_c2 = 1.49618;
		this.m_aI8_pr = 0.9;
		this.m_aI8_pns = 0.6;
		this.m_aI4_K = 2;

		this.m_aI8_w = 0.729844;
		this.m_aTC_population = new ASolution[m_aI4_size];
		this.m_aTC_ft = new FourTupple[m_aI4_size];

		this.m_aI4_vmax = 5;
		this.m_aI4_vmin = -5;

		m_str_alg_name = NameSpace.s_str_amdnspso;
		m_str_file_name = FileUtils.getResultName(m_str_alg_name, NameSpace.s_str_file_txt, m_aI4_max_nfe);

	}

	/**
	 * 惯量权重的更新
	 */
	private void updateW() {
		//m_aI8_w = m_aI8_wmax - ((double) m_aI4_cur_iter / (double) m_aI4_max_iter) * (m_aI8_wmax - m_aI8_wmin);
	}

	/**
	 * 初始化种群后，位移的初始化
	 */
	private void initV() {
		for (int t_aI4_k = 0; t_aI4_k < m_aI4_size; t_aI4_k++) {
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
	protected void initPopulation(String f_str_alg_name) {
		super.initPopulation(f_str_alg_name);
		
		double[] t_rI8_a = {0,1,1,0};
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
			double[] t_rI8_x = new double[4];
			double[] t_rI8_v = new double[4];
			for(int t_aI4_j=0;t_aI4_j<4;t_aI4_j++){
				t_rI8_x[t_aI4_j] = t_rI8_a[t_aI4_j];
				t_rI8_v[t_aI4_j] = t_rI8_a[t_aI4_j];
			}
			
			FourTupple t_aTC_f = new FourTupple();
			t_aTC_f.setM_rI8_x(t_rI8_x);
			t_aTC_f.setM_rI8_v(t_rI8_v);
			m_aTC_ft[t_aI4_i] = t_aTC_f;
		}
	}
	
	/**
	 * 全局最优和历史最优粒子的更新
	 */
	private void initBest() {
		this.m_aTC_pBest = new ASolution[m_aI4_size];
		this.m_aTC_gBest = new ASolution(m_aI4_d);
		m_aTC_gBest.setM_aI8_fitness(Double.MIN_VALUE);
		
		this.m_aTC_ft_pBest = new FourTupple[m_aI4_size];
		this.m_aTC_ft_gBest = new FourTupple();

		for (int t_aI4_k = 0; t_aI4_k < m_aI4_size; t_aI4_k++) {
			m_aTC_pBest[t_aI4_k] = (m_aTC_population[t_aI4_k]).clone();
			m_aTC_ft_pBest[t_aI4_k] = m_aTC_ft[t_aI4_k].clone();
			if (Double.compare(m_aTC_gBest.getM_aI8_fitness(), m_aTC_pBest[t_aI4_k].getM_aI8_fitness()) < 0) {
				m_aTC_gBest = m_aTC_pBest[t_aI4_k].clone();
				m_aTC_ft_gBest = m_aTC_ft_pBest[t_aI4_k].clone();
			}
		}
	}

	/**
	 * 更新pBest和gBest
	 * 
	 * @param f_aI4_k
	 */
	private void updateBest(int f_aI4_k) {
		if (Double.compare(m_aTC_pBest[f_aI4_k].getM_aI8_fitness(), m_aTC_population[f_aI4_k].getM_aI8_fitness()) < 0) {
			m_aTC_pBest[f_aI4_k] = (m_aTC_population[f_aI4_k]).clone();
			m_aTC_ft_pBest[f_aI4_k] = m_aTC_ft_pBest[f_aI4_k].clone();
		}
		if (Double.compare(m_aTC_gBest.getM_aI8_fitness(), m_aTC_pBest[f_aI4_k].getM_aI8_fitness()) < 0) {
			m_aTC_gBest = m_aTC_pBest[f_aI4_k].clone();
			m_aTC_ft_gBest = m_aTC_ft_pBest[f_aI4_k].clone();
		}
	}

	/**
	 * 更新速度位移
	 * 
	 * @param f_aTC_solution
	 * @param f_aI4_index
	 */
	private void updateXV(FourTupple f_aTC_solution, int f_aI4_index) {
		double t_aI8_r1, t_aI8_r2;
		double[] t_rI8_xp = m_aTC_ft_pBest[f_aI4_index].getM_rI8_x();
		double[] t_rI8_xg = m_aTC_ft_gBest.getM_rI8_x();
		double[] t_rI8_cur_x = f_aTC_solution.getM_rI8_x();
		double[] t_rI8_cur_v = f_aTC_solution.getM_rI8_v();
		double[] t_rI8_new_x = new double[m_aI4_d];
		double[] t_rI8_new_v = new double[m_aI4_d];
		for (int t_aI4_j = 0; t_aI4_j < 4; t_aI4_j++) {
			t_aI8_r1 = m_aTC_random.nextDouble();
			t_aI8_r2 = m_aTC_random.nextDouble();
			t_rI8_new_v[t_aI4_j] = m_aI8_w * t_rI8_cur_v[t_aI4_j]
						+ m_aI8_c1 * t_aI8_r1 * (t_rI8_xp[t_aI4_j] - t_rI8_cur_x[t_aI4_j])
						+ m_aI8_c2 * t_aI8_r2 * (t_rI8_xg[t_aI4_j] - t_rI8_cur_x[t_aI4_j]);
				if (Double.compare(t_rI8_new_v[t_aI4_j], m_aI4_vmax) > 0) {
					t_rI8_new_v[t_aI4_j] = m_aI4_vmax;
				} else if (Double.compare(t_rI8_new_v[t_aI4_j], m_aI4_vmin) < 0) {
					t_rI8_new_v[t_aI4_j] = m_aI4_vmin;
				}
			t_rI8_new_x[t_aI4_j] = t_rI8_cur_x[t_aI4_j] + t_rI8_new_v[t_aI4_j];
			/*if(Double.compare(t_rI8_new_x[t_aI4_j], ?)>0){//如果越界【保留】
				
			}*/
		}

		f_aTC_solution.setM_rI8_x(t_rI8_new_x);
		f_aTC_solution.setM_rI8_v(t_rI8_new_v);
		f_aTC_solution.setM_cur_iter(m_aI4_cur_iter);
		f_aTC_solution.setM_cur_nfe(m_aI4_cur_nfe);
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
	
	private double getFitFromFourTupple(FourTupple f_aTC_s, int f_aI4_index){
		ASolution t_aTC_so = m_aTC_population[f_aI4_index].clone();
		int t_rI4_x[] = t_aTC_so.getM_rI4_x();
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_d; t_aI4_i++) {
			evaluate2(f_aTC_s,t_aI4_i);
			if(Double.compare(f_aTC_s.getM_aI8_g(), 0)>0){
				t_rI4_x[t_aI4_i] = 1;
			}else {
				t_rI4_x[t_aI4_i] = 0;
			}
		}
		evaluate(t_aTC_so);
		return t_aTC_so.getM_aI8_fitness();
	}

	@Override
	protected void evolution() {
		while (m_aI4_cur_nfe < m_aI4_max_nfe) {
			// 第一阶段
			{
				// 评价次数m_aI4_size * 2
				for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
					FourTupple t_aTC_pit = m_aTC_ft[t_aI4_i];
					// 生成粒子Pi(t)
					updateXV(t_aTC_pit, t_aI4_i);// 计算粒子Pi的速度和位移
					/*updateAsolution(t_aTC_pit, t_aI4_i);
					modify(m_aTC_population[t_aI4_i]);*/
					// 生成粒子Pi(t+1)
					FourTupple t_aTC_pit2 = t_aTC_pit.clone();
					updateXV(t_aTC_pit2, t_aI4_i);
					/*updateAsolution(t_aTC_pit2, t_aI4_i);
					modify(m_aTC_population[t_aI4_i]);
					evaluate(m_aTC_population[t_aI4_i]);
					saveFitness(m_aTC_gBest);
					m_aI4_cur_nfe++;*/
					// 开始多样性提高机制
					FourTupple t_aTC_tpi2 = dem(t_aTC_pit, t_aTC_pit2);// 评价次数增加
					updateAsolution(t_aTC_tpi2, t_aI4_i);
					modify(m_aTC_population[t_aI4_i]);
					evaluate(m_aTC_population[t_aI4_i]);
					saveFitness(m_aTC_gBest);
					m_aI4_cur_nfe++;
					// 从t_aTC_pit2和t_aTC_tpi2中选择较好的一个作为t_aTC_pit2
					if (Double.compare(getFitFromFourTupple(t_aTC_tpi2, t_aI4_i), getFitFromFourTupple(t_aTC_pit2, t_aI4_i)) > 0) {
						t_aTC_pit2 = t_aTC_tpi2;
					}
					m_aTC_ft[t_aI4_i] = t_aTC_pit2;// 保存
					updateBest(t_aI4_i);
				}
			}

			// 第二阶段
			{
				// 评价次数m_aI4_size * 2v    
				randomR();
				for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
					double t_aI8_randi = m_aTC_random.nextDouble();
					if (Double.compare(t_aI8_randi, m_aI8_pns) <= 0) {
						FourTupple t_aTC_Pi = m_aTC_ft[t_aI4_i];
						FourTupple t_aTC_Li = LNS(t_aTC_Pi, t_aI4_i);// 局部邻域搜索
						updateAsolution(t_aTC_Li, t_aI4_i);
						modify(m_aTC_population[t_aI4_i]);
						evaluate(m_aTC_population[t_aI4_i]);
						saveFitness(m_aTC_gBest);
						m_aI4_cur_nfe++;
						FourTupple t_aTC_Gi = GNS(t_aTC_Pi, t_aI4_i);// 全局邻域搜索
						modify(m_aTC_population[t_aI4_i]);
						evaluate(m_aTC_population[t_aI4_i]);
						saveFitness(m_aTC_gBest);
						m_aI4_cur_nfe++;
						m_aTC_ft[t_aI4_i] = selectFittest(t_aTC_Pi, t_aTC_Li, t_aTC_Gi,t_aI4_i);
					}
					updateBest(t_aI4_i);
				}
			}

			updateW();
			m_aI4_cur_iter++;
			// System.out.println(m_str_alg_name+" m_aI4_cur_nfe =
			// "+m_aI4_cur_nfe);
			// System.out.println(m_str_alg_name+" m_aI4_cur_iter =
			// "+m_aI4_cur_iter);
		}
		System.out.println(m_str_alg_name + " m_aI4_cur_nfe = " + m_aI4_cur_nfe);
		System.out.println(m_str_alg_name + " m_aI4_cur_iter = " + m_aI4_cur_iter);
	}

	/**
	 * 多样性提高机制
	 * 
	 * @param f_aTC_pit
	 * @param f_aTC_pit2
	 * @return
	 */
	private FourTupple dem(FourTupple f_aTC_pit, FourTupple f_aTC_pit2) {
		// 生成新的粒子TPi2
		FourTupple t_aTC_TPi2 = f_aTC_pit2.clone();
		double[] t_rI8_txi2 = new double[4];
		double[] t_rI8_tvi2 = new double[4];

		double[] t_rI8_xit = f_aTC_pit.getM_rI8_x();
		double[] t_rI8_xit2 = f_aTC_pit2.getM_rI8_x();
		double[] t_rI8_vit2 = f_aTC_pit2.getM_rI8_v();

		double t_aI8_randj = 0;
		for (int t_aI4_j = 0; t_aI4_j < 4; t_aI4_j++) {
			t_aI8_randj = m_aTC_random.nextDouble();
			if (Double.compare(t_aI8_randj, m_aI8_pr) < 0) {
				t_rI8_txi2[t_aI4_j] = t_rI8_xit2[t_aI4_j];
			} else {
				t_rI8_txi2[t_aI4_j] = t_rI8_xit[t_aI4_j];
			}
			t_rI8_tvi2[t_aI4_j] = t_rI8_vit2[t_aI4_j];
		}
		t_aTC_TPi2.setM_rI8_x(t_rI8_txi2);
		t_aTC_TPi2.setM_rI8_v(t_rI8_tvi2);
		//modify(t_aTC_TPi2);
		//evaluate(t_aTC_TPi2);
	//	saveFitness(m_aTC_gBest);
	//	m_aI4_cur_nfe++;
		return t_aTC_TPi2;
	}

	/**
	 * 每代生成随机系数
	 */
	private void randomR() {
		m_aI8_r1 = m_aTC_random.nextDouble();
		m_aI8_r2 = m_aTC_random.nextDouble();
		while (Double.compare(m_aI8_r1 + m_aI8_r2, 1) > 0) {
			m_aI8_r2 = m_aTC_random.nextDouble();
		}
		m_aI8_r3 = 1 - m_aI8_r1 - m_aI8_r2;
		m_aI8_r4 = m_aTC_random.nextDouble();
		m_aI8_r5 = m_aTC_random.nextDouble();
		while (Double.compare(m_aI8_r4 + m_aI8_r5, 1) > 0) {
			m_aI8_r5 = m_aTC_random.nextDouble();
		}
		m_aI8_r6 = 1 - m_aI8_r4 - m_aI8_r5;
	}

	/**
	 * 局部搜索策略 生成Li
	 * 
	 * @param f_aTC_Pi
	 * @param f_aI4_index
	 * @return
	 */
	private FourTupple LNS(FourTupple f_aTC_Pi, int f_aI4_index) {
		FourTupple t_aTC_Li = f_aTC_Pi.clone();
		double[] t_rI8_lxi = new double[4];
		double[] t_rI8_lvi = new double[4];
		double[] t_rI8_pbestxi = m_aTC_ft_pBest[f_aI4_index].getM_rI8_x();
		double[] t_rI8_xi = f_aTC_Pi.getM_rI8_x();
		double[] t_rI8_vi = f_aTC_Pi.getM_rI8_v();

		List<Integer> t_aTC_list = new ArrayList<Integer>();
		for (int t_aI4_i = 1; t_aI4_i <= m_aI4_K; t_aI4_i++) {
			t_aTC_list.add((f_aI4_index + t_aI4_i) % m_aI4_size);
			t_aTC_list.add((f_aI4_index - t_aI4_i + m_aI4_size) % m_aI4_size);
		}

		int t_aI4_x = m_aTC_random.nextInt(t_aTC_list.size());
		int t_aI4_y = m_aTC_random.nextInt(t_aTC_list.size());
		while (t_aI4_x == t_aI4_y) {
			t_aI4_y = m_aTC_random.nextInt(t_aTC_list.size());
		}
		int t_aI4_c = t_aTC_list.get(t_aI4_x);
		int t_aI4_d = t_aTC_list.get(t_aI4_y);
		FourTupple t_aTC_Pc = m_aTC_ft[t_aI4_c];
		double[] t_rI8_xc = t_aTC_Pc.getM_rI8_x();
		FourTupple t_aTC_Pd = m_aTC_ft[t_aI4_d];
		double[] t_rI8_xd = t_aTC_Pd.getM_rI8_x();

		for (int t_aI4_j = 0; t_aI4_j < 4; t_aI4_j++) {
			
			t_rI8_lxi[t_aI4_j] = m_aI8_r1 * t_rI8_xi[t_aI4_j]
						+ m_aI8_r2 * t_rI8_pbestxi[t_aI4_j] + m_aI8_r3 * (t_rI8_xc[t_aI4_j] - t_rI8_xd[t_aI4_j]);
			/*if (Double.compare(t_rI8_lxi[t_aI4_j], m_aI4_vmax) > 0) {
				t_rI8_lxi[t_aI4_j] = m_aI4_vmax;
			} else if (Double.compare(t_rI8_lxi[t_aI4_j], m_aI4_vmin) < 0) {
				t_rI8_lxi[t_aI4_j] = m_aI4_vmin;
			}*/
				
			t_rI8_lvi[t_aI4_j] = t_rI8_vi[t_aI4_j];
		}

		t_aTC_Li.setM_rI8_x(t_rI8_lxi);
		t_aTC_Li.setM_rI8_v(t_rI8_lvi);

		return t_aTC_Li;
	}

	/**
	 * 全局邻域搜素策略
	 * 
	 * @param f_aTC_Pi
	 * @param f_aI4_index
	 * @return
	 */
	private FourTupple GNS(FourTupple f_aTC_Pi, int f_aI4_index) {
		FourTupple t_aTC_Gi = f_aTC_Pi.clone();
		double[] t_rI8_gxi = new double[4];
		double[] t_rI8_gvi = new double[4];
		double[] t_rI8_gbestxi = m_aTC_ft_gBest.getM_rI8_x();
		double[] t_rI8_xi = f_aTC_Pi.getM_rI8_x();
		double[] t_rI8_vi = f_aTC_Pi.getM_rI8_v();

		int t_aI4_e = m_aTC_random.nextInt(m_aI4_size);
		int t_aI4_f = m_aTC_random.nextInt(m_aI4_size);
		while (t_aI4_e == f_aI4_index) {
			t_aI4_e = m_aTC_random.nextInt(m_aI4_size);
		}
		while (t_aI4_f == t_aI4_e || t_aI4_f == f_aI4_index) {
			t_aI4_f = m_aTC_random.nextInt(m_aI4_size);
		}

		FourTupple t_aTC_Pe = m_aTC_ft[t_aI4_e];
		double[] t_rI8_xe = t_aTC_Pe.getM_rI8_x();
		FourTupple t_aTC_Pf = m_aTC_ft[t_aI4_f];
		double[] t_rI8_xf = t_aTC_Pf.getM_rI8_x();

		for (int t_aI4_j = 0; t_aI4_j < 4; t_aI4_j++) {
			
			t_rI8_gxi[t_aI4_j] = m_aI8_r4 * t_rI8_xi[t_aI4_j]
					+ m_aI8_r5 * t_rI8_gbestxi[t_aI4_j] + m_aI8_r6 * (t_rI8_xe[t_aI4_j] - t_rI8_xf[t_aI4_j]);
			t_rI8_gvi[t_aI4_j] = t_rI8_vi[t_aI4_j];
				
			
		}

		t_aTC_Gi.setM_rI8_x(t_rI8_gxi);
		t_aTC_Gi.setM_rI8_v(t_rI8_gvi);
		return t_aTC_Gi;
	}

	/**
	 * 从a,b,c三个粒子中选择最佳的一个返回
	 * 
	 * @param f_aTC_a
	 * @param f_aTC_b
	 * @param f_aTC_c
	 * @return
	 */
	private FourTupple selectFittest(FourTupple f_aTC_a, FourTupple f_aTC_b, FourTupple f_aTC_c,int f_aI4_i) {
		double t_aI4_fita = getFitFromFourTupple(f_aTC_a, f_aI4_i);
		double t_aI4_fitb = getFitFromFourTupple(f_aTC_b, f_aI4_i);
		double t_aI4_fitc = getFitFromFourTupple(f_aTC_c, f_aI4_i);
		FourTupple t_aTC_res = null;
		if (Double.compare(t_aI4_fita, t_aI4_fitb) > 0) {
			if (Double.compare(t_aI4_fita, t_aI4_fitc) > 0)
				t_aTC_res = f_aTC_a;
			else
				t_aTC_res = f_aTC_c;
		} else {
			if (Double.compare(t_aI4_fitb, t_aI4_fitc) > 0)
				t_aTC_res = f_aTC_b;
			else
				t_aTC_res = f_aTC_c;
		}

		return t_aTC_res;
	}

	@Override
	protected ASolution solve(int f_aI4_p) {
		// 初始化
		{
			init();
			initPopulation(NameSpace.s_str_amdnspso);
			initV();
			initBest();
		}

		// 打印函数，用于测试调试
		{
			/*System.out.println("==========初始化种群==========");
			printPopulation(m_aTC_population);
			System.out.println("==========历史最优种群==========");
			printPopulation(m_aTC_pBest);
			System.out.println("==========全局最优粒子==========");
			printSolution(m_aTC_gBest);*/
		}

		// 种群进化
		{
			evolution();
		}

		// 结果的保存
		{

			String t_str_result_content = getResultContent(f_aI4_p, m_aTC_gBest);
			FileUtils.saveFile(m_str_data_path, m_str_alg_name, m_str_file_name, t_str_result_content);

		}

		return m_aTC_gBest;
	}

	public static void main(String[] args) {
		SSModel t_aTC_ssm = new SSModel(NameSpace.s_str_data_02);
		AStrategy t_aTC_strategy = new AMDNSPSO(10, 1000, 500, t_aTC_ssm, NameSpace.s_str_data_02);
		t_aTC_strategy.solve(0);
	}

}
