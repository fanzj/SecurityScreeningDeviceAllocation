package com.fzj.alg;

import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.solution.ASolution;
import com.fzj.solution.FourTupple;
import com.fzj.utils.FileUtils;
import com.fzj.utils.MathUtils;

/**
 * @author Fan Zhengjie
 * @date 2017��2��3�� ����7:14:57
 * @version 1.0
 * @description DNSPSO�㷨
 */
public class AMPSO extends AStrategy {

	private double m_aI8_c1;// ѧϰ����
	private double m_aI8_c2;// ѧϰ����
	private int m_aI4_vmax;// ����ٶ�
	private int m_aI4_vmin;
	private double m_aI8_w;// ����Ȩ��
	
	public ASolution m_aTC_gBest;// ȫ����������
	private ASolution[] m_aTC_pBest;// ��ʷ��������
	
	private FourTupple[] m_aTC_ft;
	private FourTupple m_aTC_ft_gBest;
	private FourTupple[] m_aTC_ft_pBest;


	public AMPSO() {
		super();
	}

	public AMPSO(int f_aI4_size, int f_aI4_max_nfe, int f_aI4_max_iter, SSModel f_aTC_ssm, String f_str_data_path) {
		super(f_aI4_size, f_aI4_max_nfe, f_aI4_max_iter, f_aTC_ssm, f_str_data_path);
	}

	@Override
	protected void init() {
		this.m_aI8_c1 = 1.49618;
		this.m_aI8_c2 = 1.49618;
		

		this.m_aI8_w = 0.729844;
		this.m_aTC_population = new ASolution[m_aI4_size];
		this.m_aTC_ft = new FourTupple[m_aI4_size];

		this.m_aI4_vmax = 5;
		this.m_aI4_vmin = -5;

		m_str_alg_name = NameSpace.s_str_ampso;
		m_str_file_name = FileUtils.getResultName(m_str_alg_name, NameSpace.s_str_file_txt, m_aI4_max_nfe);

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
	
	private void evaluate2(FourTupple f_aTC_s,int f_aI4_x){
		
		double[] t_rI8_x = f_aTC_s.getM_rI8_x();
		double t_aI8_f = Math.sin(2*Math.PI*(f_aI4_x-t_rI8_x[0]))*t_rI8_x[1]*Math.cos(2*Math.PI*(f_aI4_x-t_rI8_x[0])*t_rI8_x[2])+t_rI8_x[3];
		f_aTC_s.setM_aI8_g(t_aI8_f);
	}
	
	@Override
	protected void initPopulation(String f_str_alg_name) {
		super.initPopulation(f_str_alg_name);
		double[] t_rI8_x = new double[4];
		double[] t_rI8_v = new double[4];
		double[] t_rI8_a = {0,1,1,0};
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
			
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
	
	/*@Override
	protected void initPopulation(String f_str_alg_name) {//����ÿ����һ��
		double[] t_rI8_x = new double[4];
		double[] t_rI8_v = new double[4];
		double[] t_rI8_a = {0,1,1,0};
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
			ASolution t_aTC_solution = new ASolution(m_aI4_d);
			int[] t_rI4_x = new int[m_aI4_d];
			
			if(t_aI4_i>0){
				for(int t_aI4_j=0;t_aI4_j<4;t_aI4_j++){
					t_rI8_x[t_aI4_j] = MathUtils.getDoubleAToB(m_aI4_vmin, m_aI4_vmax);
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
	}*/

	/**
	 * ȫ�����ź���ʷ�������ӵĸ���
	 */
	private void initBest() {
		this.m_aTC_pBest = new ASolution[m_aI4_size];
		this.m_aTC_gBest = new ASolution(m_aI4_d);
		m_aTC_gBest.setM_aI8_fitness(Double.MIN_VALUE);
		
		this.m_aTC_ft_pBest = new FourTupple[m_aI4_size];
		this.m_aTC_ft_gBest = new FourTupple();

		for (int t_aI4_k = 0; t_aI4_k < m_aI4_size; t_aI4_k++) {
			m_aTC_pBest[t_aI4_k] = m_aTC_population[t_aI4_k].clone();
			m_aTC_ft_pBest[t_aI4_k] = m_aTC_ft[t_aI4_k].clone();
			if (Double.compare(m_aTC_gBest.getM_aI8_fitness(), m_aTC_pBest[t_aI4_k].getM_aI8_fitness()) < 0) {
				m_aTC_gBest = m_aTC_pBest[t_aI4_k].clone();
				m_aTC_ft_gBest = m_aTC_ft_pBest[t_aI4_k].clone();
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
			m_aTC_ft_pBest[f_aI4_k] = m_aTC_ft_pBest[f_aI4_k].clone();
		}
		if (Double.compare(m_aTC_gBest.getM_aI8_fitness(), m_aTC_pBest[f_aI4_k].getM_aI8_fitness()) < 0) {
			m_aTC_gBest = m_aTC_pBest[f_aI4_k].clone();
			m_aTC_ft_gBest = m_aTC_ft_pBest[f_aI4_k].clone();
		}
	}

	/**
	 * �����ٶ�λ��
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
			/*if(Double.compare(t_rI8_new_x[t_aI4_j], ?)>0){//���Խ�硾������
				
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

	@Override
	protected void evolution() {
		while (m_aI4_cur_nfe < m_aI4_max_nfe) {
			// ��һ�׶�
			{
				for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
					FourTupple t_aTC_pit = m_aTC_ft[t_aI4_i];
					// ��������Pi(t)
					updateXV(t_aTC_pit, t_aI4_i);// ��������Pi���ٶȺ�λ��
					updateAsolution(t_aTC_pit, t_aI4_i);
					modify(m_aTC_population[t_aI4_i]);
					evaluate(m_aTC_population[t_aI4_i]);
					saveFitness(m_aTC_gBest);
					m_aI4_cur_nfe++;
					
					updateBest(t_aI4_i);
				}
			}
			//printPopulation(m_aTC_population);
			
		//	updateW();
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
			initPopulation(NameSpace.s_str_ampso);
			initV();
			initBest();
		}

		// ��ӡ���������ڲ��Ե���
		{
			/*System.out.println("==========��ʼ����Ⱥ==========");
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
		AStrategy t_aTC_strategy = new AMPSO(10, 5000, 100, t_aTC_ssm, NameSpace.s_str_data_02);
		t_aTC_strategy.solve(0);
	}

}
