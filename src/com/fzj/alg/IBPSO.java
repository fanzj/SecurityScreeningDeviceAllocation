package com.fzj.alg;

import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.solution.ASolution;
import com.fzj.utils.FileUtils;

/**
 * @author Fan Zhengjie
 * @date 2017年2月3日 下午7:14:57
 * @version 1.0
 * @description DNSPSO算法
 */
public class IBPSO extends AStrategy {

	public ASolution m_aTC_gBest;// 全局最优粒子
	private ASolution[] m_aTC_pBest;// 历史最优粒子

	public IBPSO() {
		super();
	}

	public IBPSO(int f_aI4_size, int f_aI4_max_nfe, int f_aI4_max_iter, SSModel f_aTC_ssm, String f_str_data_path) {
		super(f_aI4_size, f_aI4_max_nfe, f_aI4_max_iter, f_aTC_ssm, f_str_data_path);
	}

	@Override
	protected void init() {
		this.m_aTC_population = new ASolution[m_aI4_size];

		m_str_alg_name = NameSpace.s_str_ibpso;
		m_str_file_name = FileUtils.getResultName(m_str_alg_name, NameSpace.s_str_file_txt, m_aI4_max_nfe);

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

	/**
	 * 全局最优和历史最优粒子的更新
	 */
	private void initBest() {
		this.m_aTC_pBest = new ASolution[m_aI4_size];
		this.m_aTC_gBest = new ASolution(m_aI4_d);
		m_aTC_gBest.setM_aI8_fitness(Double.MIN_VALUE);

		for (int t_aI4_k = 0; t_aI4_k < m_aI4_size; t_aI4_k++) {
			m_aTC_pBest[t_aI4_k] = (m_aTC_population[t_aI4_k]).clone();
			if (Double.compare(m_aTC_gBest.getM_aI8_fitness(), m_aTC_pBest[t_aI4_k].getM_aI8_fitness()) < 0) {
				m_aTC_gBest = m_aTC_pBest[t_aI4_k].clone();
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
		}
		if (Double.compare(m_aTC_gBest.getM_aI8_fitness(), m_aTC_pBest[f_aI4_k].getM_aI8_fitness()) < 0) {
			m_aTC_gBest = m_aTC_pBest[f_aI4_k].clone();
		}
	}

	/**
	 * 更新速度位移
	 * 
	 * @param f_aTC_solution
	 * @param f_aI4_index
	 */
	private void updateXV(ASolution f_aTC_solution, int f_aI4_index) {
		int t_aI4_w1, t_aI4_w2;
		int[] t_rI4_xp = m_aTC_pBest[f_aI4_index].getM_rI4_x();
		int[] t_rI4_xg = m_aTC_gBest.getM_rI4_x();
		int[] t_rI4_cur_x = f_aTC_solution.getM_rI4_x();
		int[] t_rI4_cur_v = f_aTC_solution.getM_rI4_v2();
		int[] t_rI4_new_x = new int[m_aI4_d];
		int[] t_rI4_new_v = new int[m_aI4_d];
		
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k + 1; t_aI4_k++) {
				int t_aI4_pos = t_aI4_j * (m_aI4_k + 1) + t_aI4_k;
				t_aI4_w1 = m_aTC_random.nextInt(2);
				t_aI4_w2 = m_aTC_random.nextInt(2);
				t_rI4_new_v[t_aI4_pos] = t_aI4_w1 & (t_rI4_xp[t_aI4_pos] ^ t_rI4_cur_x[t_aI4_pos]) + t_aI4_w2 & (t_rI4_xg[t_aI4_pos] ^ t_rI4_cur_x[t_aI4_pos]);
				t_rI4_new_x[t_aI4_pos] = t_rI4_cur_x[t_aI4_pos] ^ t_rI4_new_v[t_aI4_pos];
			}
		}

		f_aTC_solution.setM_rI4_x(t_rI4_new_x);
		f_aTC_solution.setM_rI4_v2(t_rI4_new_v);
		f_aTC_solution.setM_cur_iter(m_aI4_cur_iter);
		f_aTC_solution.setM_cur_nfe(m_aI4_cur_nfe);
	}

	@Override
	protected void evolution() {
		while (m_aI4_cur_nfe < m_aI4_max_nfe) {
			/// start
			for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {

				ASolution t_aTC_solution = m_aTC_population[t_aI4_i];
				updateXV(t_aTC_solution, t_aI4_i);

				// System.out.println("修正前：");
				// printSolution(t_aTC_solution);
				modify(t_aTC_solution);// 修正
				// System.out.println("修正后：");
				// printSolution(t_aTC_solution);
				evaluate(t_aTC_solution);
				saveFitness(m_aTC_gBest);
				m_aI4_cur_nfe++;// 评价次数增加

				updateBest(t_aI4_i);
			}
			m_aI4_cur_iter++;
		}
		System.out.println(m_str_alg_name + " m_aI4_cur_nfe = " + m_aI4_cur_nfe);
		System.out.println(m_str_alg_name + " m_aI4_cur_iter = " + m_aI4_cur_iter);
	}

	@Override
	protected ASolution solve(int f_aI4_p) {
		// 初始化
		{
			init();
			initPopulation(NameSpace.s_str_bpso);
			initV();
			initBest();
		}

		// 打印函数，用于测试调试
		{
			
			// System.out.println("==========初始化种群==========");
			// printPopulation(m_aTC_population);
			// System.out.println("==========历史最优种群==========");
			// printPopulation(m_aTC_pBest);
			// System.out.println("==========全局最优粒子==========");
			// printSolution(m_aTC_gBest);
			 
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
		SSModel t_aTC_ssm = new SSModel(NameSpace.s_str_data_01);
		AStrategy t_aTC_strategy = new IBPSO(10, 1000, 500, t_aTC_ssm, NameSpace.s_str_data_01);
		t_aTC_strategy.solve(0);
	}

}
