package com.fzj.alg;

import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.solution.ASolution;
import com.fzj.utils.FileUtils;

/**
 * @author Fan Zhengjie
 * @date 2017��2��3�� ����7:14:57
 * @version 1.0
 * @description DNSPSO�㷨
 */
public class BPSO extends AStrategy {

	private double m_aI8_c1;// ѧϰ����
	private double m_aI8_c2;// ѧϰ����
	private int m_aI4_vmax;// ����ٶ�
	private int m_aI4_vmin;
	private double m_aI8_w;// ����Ȩ��
	public ASolution m_aTC_gBest;// ȫ����������
	private ASolution[] m_aTC_pBest;// ��ʷ��������

	public BPSO() {
		super();
	}

	public BPSO(int f_aI4_size, int f_aI4_max_nfe, int f_aI4_max_iter, SSModel f_aTC_ssm, String f_str_data_path) {
		super(f_aI4_size, f_aI4_max_nfe, f_aI4_max_iter, f_aTC_ssm, f_str_data_path);
	}

	@Override
	protected void init() {
		this.m_aI8_c1 = 1.496180;
		this.m_aI8_c2 = 1.496180;

		this.m_aI8_w = 0.729844;
		this.m_aTC_population = new ASolution[m_aI4_size];

		this.m_aI4_vmax = 5;
		this.m_aI4_vmin = -5;

		m_str_alg_name = NameSpace.s_str_bpso;
		m_str_file_name = FileUtils.getResultName(m_str_alg_name, NameSpace.s_str_file_txt, m_aI4_max_nfe);

	}

	/**
	 * ����Ȩ�صĸ���
	 */
	private void updateW() {
		//m_aI8_w = m_aI8_wmax - ((double) m_aI4_cur_iter / (double) m_aI4_max_iter) * (m_aI8_wmax - m_aI8_wmin);
	}

	/**
	 * ��ʼ����Ⱥ��λ�Ƶĳ�ʼ��
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
	 * ȫ�����ź���ʷ�������ӵĸ���
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
	 * ����pBest��gBest
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
	 * �����ٶ�λ��
	 * 
	 * @param f_aTC_solution
	 * @param f_aI4_index
	 */
	private void updateXV(ASolution f_aTC_solution, int f_aI4_index) {
		double t_aI8_r1, t_aI8_r2;
		int[] t_rI4_xp = m_aTC_pBest[f_aI4_index].getM_rI4_x();
		int[] t_rI4_xg = m_aTC_gBest.getM_rI4_x();
		int[] t_rI4_cur_x = f_aTC_solution.getM_rI4_x();
		double[] t_rI8_cur_v = f_aTC_solution.getM_rI8_v();
		int[] t_rI4_new_x = new int[m_aI4_d];
		double[] t_rI8_new_v = new double[m_aI4_d];
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k + 1; t_aI4_k++) {
				int t_aI4_pos = t_aI4_j * (m_aI4_k + 1) + t_aI4_k;
				t_aI8_r1 = m_aTC_random.nextDouble();
				t_aI8_r2 = m_aTC_random.nextDouble();
				t_rI8_new_v[t_aI4_pos] = m_aI8_w * t_rI8_cur_v[t_aI4_pos]
						+ m_aI8_c1 * t_aI8_r1 * (t_rI4_xp[t_aI4_pos] - t_rI4_cur_x[t_aI4_pos])
						+ m_aI8_c2 * t_aI8_r2 * (t_rI4_xg[t_aI4_pos] - t_rI4_cur_x[t_aI4_pos]);
				if (Double.compare(t_rI8_new_v[t_aI4_pos], m_aI4_vmax) > 0) {
					t_rI8_new_v[t_aI4_pos] = m_aI4_vmax;
				} else if (Double.compare(t_rI8_new_v[t_aI4_pos], m_aI4_vmin) < 0) {
					t_rI8_new_v[t_aI4_pos] = m_aI4_vmin;
				}
				double t_aI8_s = 1 / (1 + Math.pow(Math.E, -t_rI8_new_v[t_aI4_pos]));
				if (Double.compare(m_aTC_random.nextDouble(), t_aI8_s) < 0) {
					t_rI4_new_x[t_aI4_pos] = 1;
				} else {
					t_rI4_new_x[t_aI4_pos] = 0;
				}
			}
		}

		f_aTC_solution.setM_rI4_x(t_rI4_new_x);
		f_aTC_solution.setM_rI8_v(t_rI8_new_v);
		f_aTC_solution.setM_cur_iter(m_aI4_cur_iter);
		f_aTC_solution.setM_cur_nfe(m_aI4_cur_nfe);
	}

	@Override
	protected void evolution() {
		while (m_aI4_cur_nfe < m_aI4_max_nfe) {
			/// start
			for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {

				ASolution t_aTC_solution = (ASolution) m_aTC_population[t_aI4_i];
				updateXV(t_aTC_solution, t_aI4_i);

				// System.out.println("����ǰ��");
				// printSolution(t_aTC_solution);
				modify(t_aTC_solution);// ����
				// System.out.println("������");
				// printSolution(t_aTC_solution);
				evaluate(t_aTC_solution);
				saveFitness(m_aTC_gBest);
				m_aI4_cur_nfe++;// ���۴�������

				updateBest(t_aI4_i);
			}
			updateW();
			m_aI4_cur_iter++;
		}
		System.out.println(m_str_alg_name + " m_aI4_cur_nfe = " + m_aI4_cur_nfe);
		System.out.println(m_str_alg_name + " m_aI4_cur_iter = " + m_aI4_cur_iter);
	}

	@Override
	protected ASolution solve(int f_aI4_p) {
		// ��ʼ��
		{
			init();
			initPopulation(NameSpace.s_str_bpso);
			initV();
			initBest();
		}

		// ��ӡ���������ڲ��Ե���
		{
			
			 /* System.out.println("==========��ʼ����Ⱥ==========");
			  printPopulation(m_aTC_population);
			  System.out.println("==========��ʷ������Ⱥ==========");
			  printPopulation(m_aTC_pBest);
			  System.out.println("==========ȫ����������==========");
			  printSolution(m_aTC_gBest);*/
			 
		}

		// ��Ⱥ����
		{
			evolution();
		}

		// ����ı���
		{
			String t_str_result_content = getResultContent(f_aI4_p, m_aTC_gBest);
			FileUtils.saveFile(m_str_data_path, m_str_alg_name, m_str_file_name, t_str_result_content);
		}

		return m_aTC_gBest;
	}

	public static void main(String[] args) {
		SSModel t_aTC_ssm = new SSModel(NameSpace.s_str_data_02);
		AStrategy t_aTC_strategy = new BPSO(10, 1000, 20, t_aTC_ssm, NameSpace.s_str_data_02);
		t_aTC_strategy.solve(0);
	}

}
