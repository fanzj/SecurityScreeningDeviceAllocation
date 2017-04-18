package com.fzj.alg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.solution.ASolution;
import com.fzj.solution.FireSparkSolution;
import com.fzj.solution.Fitness;
import com.fzj.solution.ParticleSolution;
import com.fzj.solution.WaveSolution;

/**
 * @author Fan Zhengjie
 * @date 2017��1��28�� ����2:50:40
 * @version 1.0
 * @description ���������
 */
public abstract class AStrategy {

	protected String m_str_data_path;// ʵ������·��������data_01\\
	protected String m_str_alg_name;// �㷨������ǰʹ�ú����㷨������PSO
	protected String m_str_file_name;// �����ļ�������

	protected int m_aI4_max_iter;// ����������
	protected int m_aI4_max_nfe;// ������۴���

	protected int m_aI4_size;// ��Ⱥ��ģ
	protected int m_aI4_d;// ����ά��
	protected int m_aI4_cur_iter;// ��ǰ��������
	protected int m_aI4_cur_nfe;// ��ǰ���۴���

	protected SSModel m_aTC_ssm;// �����豸����ģ��
	protected int m_aI4_m;// Σ����Ʒ������
	protected int m_aI4_n;// ������
	protected int m_aI4_k;// �����豸����
	protected int m_aI4_p;// ÿ�����������������豸��
	protected double m_rI8_wi[];// Σ����Ʒ��Ȩ��
	protected int m_rI4_yk[];// �豸k���Զ��豸�����ֶ��豸
	protected double m_rI8_lj[];// ����j����󳤶�
	protected double m_rI8_sj[];// ����j�����
	protected double m_rI8_vak[];// �Զ��豸k�ļ���ٶ�
	protected double m_rI8_vhk[];// �ֶ��豸k�ļ���ٶ�
	protected double m_aI8_ta;// �Զ��豸����ʱ������
	protected double m_aI8_th;// �ֶ�����豸��ʱ������
	protected double m_rI8_ck[];// �豸k���ݻ�
	protected double m_aI8_a;// ÿ������Я��Σ��Ʒ�ĸ���
	protected double m_rI8_bi[];// Я��Σ��ƷΪi�ĸ���
	protected double m_rI8_yik[][];// Σ��Ʒi��k��⵽�ĸ���

	protected double m_rI8_tjk[][];// ���ʱ�䣬����j���豸k�������ʱ��

	protected Random m_aTC_random;// �����������

	protected int m_aI4_low;// Լ������
	protected int m_aI4_upper;// Լ������
	
	protected List<Fitness> m_aTC_listOfFitness;// ���ڽ���ı�����ʾ
	protected ASolution[] m_aTC_population;// ��Ⱥ

	public AStrategy() {
	}

	public AStrategy(int f_aI4_size, int f_aI4_max_nfe, int f_aI4_max_iter, SSModel f_aTC_ssm, String f_str_data_path) {
		this.m_aI4_size = f_aI4_size;
		this.m_aI4_max_nfe = f_aI4_max_nfe;
		this.m_aI4_max_iter = f_aI4_max_iter;
		this.m_aTC_ssm = f_aTC_ssm;
		this.m_str_data_path = f_str_data_path;

		this.m_aI4_cur_iter = 0;
		this.m_aI4_cur_nfe = 0;

		this.m_aI4_d = m_aTC_ssm.getM_aI4_d();
		this.m_aI4_m = m_aTC_ssm.getM_aI4_m();
		this.m_aI4_n = m_aTC_ssm.getM_aI4_n();
		this.m_aI4_k = m_aTC_ssm.getM_aI4_k();
		this.m_aI4_p = m_aTC_ssm.getM_aI4_p();
		this.m_rI8_wi = m_aTC_ssm.getM_rI8_wi();
		this.m_rI4_yk = m_aTC_ssm.getM_rI4_yk();
		this.m_rI8_lj = m_aTC_ssm.getM_rI8_lj();
		this.m_rI8_sj = m_aTC_ssm.getM_rI8_sj();
		this.m_rI8_vak = m_aTC_ssm.getM_rI8_vak();
		this.m_rI8_vhk = m_aTC_ssm.getM_rI8_vhk();
		this.m_aI8_ta = m_aTC_ssm.getM_aI8_ta();
		this.m_aI8_th = m_aTC_ssm.getM_aI8_th();
		this.m_rI8_ck = m_aTC_ssm.getM_rI8_ck();
		this.m_aI8_a = m_aTC_ssm.getM_aI8_a();
		this.m_rI8_bi = m_aTC_ssm.getM_rI8_bi();
		this.m_rI8_yik = m_aTC_ssm.getM_rI8_yik();

		this.m_aTC_random = new Random(System.currentTimeMillis());
		this.m_aTC_listOfFitness = new ArrayList<>();
		
		this.m_aI4_low = 0;
		this.m_aI4_upper = 1;

		calDetectTime();// ������ʱ��
	}

	/**
	 * ���Ƿ�Ϊ�Ϸ��⡿ ����Լ�� 1.ÿ����� �Զ����ʱ��С�����ޣ��ֶ����ʱ��С������ 2.ÿ���� ���䵽�����豸���������ڷ�Χ֮��
	 * 3.ÿ���������ٷ����һ���豸
	 * 
	 * @param f_aTC_s
	 * @return
	 */
	protected boolean isValidSolution(ASolution f_aTC_s) {
		// ʱ��Լ��1������
		if (Double.compare(f_aTC_s.getM_aI8_ta(), m_aI8_ta) > 0 || Double.compare(f_aTC_s.getM_aI8_th(), m_aI8_th) > 0)
			return false;
		// ��֤Լ��2
		double t_rI8_vol[] = f_aTC_s.getM_rI8_vol();
		for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
			if (Double.compare(t_rI8_vol[t_aI4_k], m_rI8_ck[t_aI4_k]) > 0)
				return false;
		}
		// ��֤Լ��3
		int t_rI4_bag[] = f_aTC_s.getM_rI4_bag();
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			if (t_rI4_bag[t_aI4_j] <= 0)
				return false;
		}
		return true;
	}

	/**
	 * ����������� ���ڲ��Ϸ��Ľ�����Ӧ����������ʹ��Ϸ�
	 * 
	 * @param f_aTC_s
	 */
	protected void modify(ASolution f_aTC_s) {
		reCal(f_aTC_s);
		int t_aI4_c = 0;// ѭ������
		while (!isValidSolution(f_aTC_s)) {
			t_aI4_c++;
			int[] t_rI4_x = f_aTC_s.getM_rI4_x();
			double t_aI8_ta = f_aTC_s.getM_aI8_ta();// �Զ����ʱ��
			double t_aI8_th = f_aTC_s.getM_aI8_th();// �ֶ����ʱ��
			int[] t_rI4_bag = f_aTC_s.getM_rI4_bag();// ���䵽ÿ���������豸��
			double[] t_rI8_vol = f_aTC_s.getM_rI8_vol();// ����������豸�ĵ�ǰ����

			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {// �������豸
				int t_aI4_con2 = 0;
				while (Double.compare(t_rI8_vol[t_aI4_k], m_rI8_ck[t_aI4_k]) > 0) {// ���Υ��Լ��2
					t_aI4_con2++;
					//System.out.println("t_aI4_con2 = "+t_aI4_con2);
					if(t_aI4_con2>20)//��ֹ������ѭ��
						break;
					int t_aI4_j = m_aTC_random.nextInt(m_aI4_n);
					int t_aI4_pos = t_aI4_j * m_aI4_k + t_aI4_k;
					//��bug �������������������ѭ����
					if (t_rI4_x[t_aI4_pos] == 1 && t_rI4_bag[t_aI4_j] >1) {// Լ��3
						t_rI4_x[t_aI4_pos] = 0;
						t_rI8_vol[t_aI4_k] -= (m_rI8_sj[t_aI4_j] * m_rI4_yk[t_aI4_k]);
						t_rI4_bag[t_aI4_j]--;
						t_aI8_ta -= m_rI8_tjk[t_aI4_j][t_aI4_k] * m_rI4_yk[t_aI4_k];
						t_aI8_th -= m_rI8_tjk[t_aI4_j][t_aI4_k] * (1 - m_rI4_yk[t_aI4_k]);
					}
					
				}
			}
			//�����ʱ���ѺϷ��������
			if(isValidSolution(f_aTC_s)){
				//System.out.println("valid solution - 1");
				return;
			}
				
			
			for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {// ÿ������
				//int t_aI4_test_2 = 0;
				while (t_rI4_bag[t_aI4_j] < 1) {// Υ��Լ��3
					int t_aI4_k = m_aTC_random.nextInt(m_aI4_k);
					int t_aI4_pos = t_aI4_j * m_aI4_k + t_aI4_k;
				//	System.out.println("t_aI4_test_2 = "+(t_aI4_test_2++));
					if (t_rI4_x[t_aI4_pos] == 0 && Double.compare(t_rI8_vol[t_aI4_k], m_rI8_ck[t_aI4_k]) <= 0) {// Լ��2	
						t_rI4_x[t_aI4_pos] = 1;
						t_rI8_vol[t_aI4_k] += (m_rI8_sj[t_aI4_j] * m_rI4_yk[t_aI4_k]);
						t_rI4_bag[t_aI4_j]++;
						t_aI8_ta += m_rI8_tjk[t_aI4_j][t_aI4_k] * m_rI4_yk[t_aI4_k];
						t_aI8_th += m_rI8_tjk[t_aI4_j][t_aI4_k] * (1 - m_rI4_yk[t_aI4_k]);
					}
					
				}
			}

			//�����ʱ���ѺϷ��������
			if(isValidSolution(f_aTC_s)){
				//System.out.println("valid solution - 2");
				return;
			}
			
			int t_aI4_con_t = 0;
			while(Double.compare(f_aTC_s.getM_aI8_ta(), m_aI8_ta)>0 || Double.compare(f_aTC_s.getM_aI8_th(), m_aI8_th)>0){
				t_aI4_con_t++;
				//System.out.println("t_aI4_con2 = "+t_aI4_con2);
				if(t_aI4_con_t>20)//��ֹ������ѭ��
					break;
				int t_aI4_j = m_aTC_random.nextInt(m_aI4_n);
				int t_aI4_k = m_aTC_random.nextInt(m_aI4_k);
				int t_aI4_pos = t_aI4_j * m_aI4_k + t_aI4_k;
				//��bug �������������������ѭ����
				if (t_rI4_x[t_aI4_pos] == 1 && t_rI4_bag[t_aI4_j] >1) {// Լ��3
					t_rI4_x[t_aI4_pos] = 0;
					t_rI8_vol[t_aI4_k] -= (m_rI8_sj[t_aI4_j] * m_rI4_yk[t_aI4_k]);
					t_rI4_bag[t_aI4_j]--;
					t_aI8_ta -= m_rI8_tjk[t_aI4_j][t_aI4_k] * m_rI4_yk[t_aI4_k];
					t_aI8_th -= m_rI8_tjk[t_aI4_j][t_aI4_k] * (1 - m_rI4_yk[t_aI4_k]);
				}
					
			}
		
			//�����ʱ���ѺϷ��������
			if(isValidSolution(f_aTC_s)){
				//System.out.println("valid solution - 3");
				return;
			}
			
			if (t_aI4_c > 100) {// 20�λ�û�����������´���һ���½�
				t_aI4_c = 0;
				//System.out.println("new solution");
				createNewSolution(f_aTC_s);
			}
			//System.out.println("t_aI4_c = "+t_aI4_c);
		}
	}

	/**
	 * ���¼���
	 * 
	 * @param f_aTC_s
	 * @param f_aI4_k
	 *            �豸���
	 */
	private void reCal(ASolution f_aTC_s) {
		int[] t_rI4_x = f_aTC_s.getM_rI4_x();
		double t_aI8_ta = 0;// �Զ����ʱ��
		double t_aI8_th = 0;// �ֶ����ʱ��
		int[] t_rI4_bag = new int[m_aI4_n];// �����������䵽���豸��
		double[] t_rI8_vol = new double[m_aI4_k];// ����������豸�ĵ�ǰ����

		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
				int t_aI4_pos = t_aI4_j * m_aI4_k + t_aI4_k;
				// �Զ����ʱ��
				t_aI8_ta += m_rI8_tjk[t_aI4_j][t_aI4_k] * t_rI4_x[t_aI4_pos] * m_rI4_yk[t_aI4_k];
				// �ֶ����ʱ��
				t_aI8_th += m_rI8_tjk[t_aI4_j][t_aI4_k] * t_rI4_x[t_aI4_pos] * (1 - m_rI4_yk[t_aI4_k]);
				// �����������䵽���豸��
				t_rI4_bag[t_aI4_j] += t_rI4_x[t_aI4_pos];
				// �����豸�ĵ�ǰ������
				t_rI8_vol[t_aI4_k] += (m_rI8_sj[t_aI4_j] * t_rI4_x[t_aI4_pos] * m_rI4_yk[t_aI4_k]);
			}
		}
		f_aTC_s.setM_aI8_ta(t_aI8_ta);
		f_aTC_s.setM_aI8_th(t_aI8_th);
		f_aTC_s.setM_rI4_bag(t_rI4_bag);
		f_aTC_s.setM_rI8_vol(t_rI8_vol);
	}

	/**
	 * ������ʱ��
	 */
	private void calDetectTime() {
		m_rI8_tjk = new double[m_aI4_n][m_aI4_k];
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
				if (m_rI4_yk[t_aI4_k] == 1) {// �Զ��豸
					m_rI8_tjk[t_aI4_j][t_aI4_k] = m_rI8_lj[t_aI4_j] / m_rI8_vak[t_aI4_k];
				} else {// �ֶ��豸
					m_rI8_tjk[t_aI4_j][t_aI4_k] = m_rI8_sj[t_aI4_j] / m_rI8_vhk[t_aI4_k];
				}
			}
		}
	}

	/**
	 * ������ʼ��
	 */
	protected abstract void init();

	/**
	 * ��Ⱥ��ʼ��
	 */
	protected void initPopulation(String f_str_alg_name) {
		for (int t_aI4_k = 0; t_aI4_k < m_aI4_size; t_aI4_k++) {
			ASolution t_aTC_solution = null;
			if (f_str_alg_name.equals(NameSpace.s_str_dnspso) || f_str_alg_name.equals(NameSpace.s_str_dednspso)) {
				t_aTC_solution = new ParticleSolution(m_aI4_d);
			} else if (f_str_alg_name.equals(NameSpace.s_str_fade)) {
				t_aTC_solution = new FireSparkSolution(m_aI4_d);
			} else if (f_str_alg_name.equals(NameSpace.s_str_wwo)) {
				t_aTC_solution = new WaveSolution(m_aI4_d);
			}
			int[] t_rI4_x = new int[m_aI4_d];
			for (int t_aI4_i = 0; t_aI4_i < m_aI4_d; t_aI4_i++) {
				t_rI4_x[t_aI4_i] = m_aTC_random.nextInt(2);
			}

			t_aTC_solution.setM_rI4_x(t_rI4_x);
			m_aTC_population[t_aI4_k] = t_aTC_solution;
			modify(m_aTC_population[t_aI4_k]);
			System.out.println("modify end");
			//reCal(m_aTC_population[t_aI4_k]);
			evaluate(m_aTC_population[t_aI4_k]);
			
			
		}
	}

	/**
	 * �������½⡿
	 * 
	 * @param f_aTC_solution
	 * @return
	 */
	protected ASolution createNewSolution(ASolution f_aTC_solution) {

		if (f_aTC_solution != null) {
			int[] t_rI4_x = new int[m_aI4_d];
			for (int t_aI4_i = 0; t_aI4_i < m_aI4_d; t_aI4_i++) {
				t_rI4_x[t_aI4_i] = m_aTC_random.nextInt(2);
				f_aTC_solution.setM_rI4_x(t_rI4_x);
				modify(f_aTC_solution);
				evaluate(f_aTC_solution);
			}
		}

		return f_aTC_solution;
	}

	/**
	 * ÿ��50�����۱�����Ӧ��
	 * 
	 * @param f_aTC_solution
	 */
	protected void saveFitness(ASolution f_aTC_solution) {
		if (m_aI4_cur_nfe % 50 == 0 || m_aI4_cur_nfe == (m_aI4_max_nfe - 1)) {
			Fitness t_aTC_fitness = new Fitness(m_aI4_cur_nfe, f_aTC_solution.getM_aI8_fitness());
			m_aTC_listOfFitness.add(t_aTC_fitness);
		}
	}

	/**
	 * ����Ӧ�ȼ��� & ͬʱԼ�������ļ��㡿 1.���ʱ����м��� 2.�����豸����İ����� 3.�����豸�ĵ�ǰ������
	 */
	protected void evaluate(ASolution f_aTC_solution) {
		int[] t_rI4_x = f_aTC_solution.getM_rI4_x();
		double t_aI8_fitness = 0.0;
		double t_aI8_pij = 0;
		double[][] t_rI8_p = new double[m_aI4_m][m_aI4_n];
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_m; t_aI4_i++) {
			for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
				double t_aI8_fail = 1;
				for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
					int t_aI4_pos = t_aI4_j * m_aI4_k + t_aI4_k;
					t_aI8_fail *= ((1 - m_rI8_yik[t_aI4_i][t_aI4_k]) * t_rI4_x[t_aI4_pos]);
				}
				t_aI8_pij = 1 - t_aI8_fail;
				//System.out.println("t_aI8_pij = "+t_aI8_pij);
				t_rI8_p[t_aI4_i][t_aI4_j] = t_aI8_pij;
				t_aI8_fitness += m_rI8_wi[t_aI4_i] * m_rI8_bi[t_aI4_i] * t_rI8_p[t_aI4_i][t_aI4_j];
			}
		}
		
		/*for (int t_aI4_i = 0; t_aI4_i < m_aI4_m; t_aI4_i++) {
			for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
				t_aI8_fitness += m_rI8_wi[t_aI4_i] * m_rI8_bi[t_aI4_i] * t_rI8_p[t_aI4_i][t_aI4_j];
			}
		}*/
		f_aTC_solution.setM_cur_nfe(m_aI4_cur_nfe);
		f_aTC_solution.setM_cur_iter(m_aI4_cur_iter);
		f_aTC_solution.setM_aI8_fitness(t_aI8_fitness * m_aI8_a);
		//reCal(f_aTC_solution);
	}

	/**
	 * ��Ⱥ����
	 */
	protected abstract void evolution();

	/**
	 * �������
	 */
	protected abstract ASolution solve(int f_aI4_p);

	/**
	 * ���Ž�Ľ����ȡ
	 * 
	 * @param f_aI4_p
	 * @param f_aTC_solution
	 *            ���Ž�
	 * @return
	 */
	protected String getResultContent(int f_aI4_p, ASolution f_aTC_solution) {
		StringBuffer t_aTC_sb = new StringBuffer();
		int[] t_rI4_x = f_aTC_solution.getM_rI4_x();
		int[] t_rI4_bag = f_aTC_solution.getM_rI4_bag();
		double[] t_rI8_vol = f_aTC_solution.getM_rI8_vol();
		t_aTC_sb.append(f_aI4_p+" solution:\n");
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
				t_aTC_sb.append(t_rI4_x[t_aI4_j * m_aI4_k + t_aI4_k]);
				if (t_aI4_k < m_aI4_k - 1)
					t_aTC_sb.append(" ");
				else if (t_aI4_j < m_aI4_n - 1) {
					t_aTC_sb.append("(" + t_rI4_bag[t_aI4_j] + ")|");
				} else {
					t_aTC_sb.append("(" + t_rI4_bag[t_aI4_j] + ")|fitness = " + f_aTC_solution.getM_aI8_fitness()
							+ "|ta = " + f_aTC_solution.getM_aI8_ta() + "|th = " + f_aTC_solution.getM_aI8_th() + "|");
				}
			}
		}
		for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
			t_aTC_sb.append(t_rI8_vol[t_aI4_k]);
			if (t_aI4_k < m_aI4_k - 1)
				t_aTC_sb.append(" ");
			else {
				t_aTC_sb.append("|" + (isValidSolution(f_aTC_solution) ? "valid" : "invalid")+"\n");
			}
		}
		return t_aTC_sb.toString();
	}

	/**
	 * ��Ⱥ��ӡ���������ڵ��ԣ�
	 */
	protected void printPopulation(ASolution[] f_aTC_solution) {
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
			ASolution t_aTC_solution = f_aTC_solution[t_aI4_i];
			printSolution(t_aTC_solution);
		}
	}

	/**
	 * ��ӡ���������ڵ��ԣ�
	 */
	protected void printPopulation(List<ASolution> f_aTC_solution) {
		for (int t_aI4_i = 0; t_aI4_i < f_aTC_solution.size(); t_aI4_i++) {
			ASolution t_aTC_solution = f_aTC_solution.get(t_aI4_i);
			printSolution(t_aTC_solution);
		}
	}

	/**
	 * ������Ĵ�ӡ���������ڵ��ԣ�
	 */
	protected void printSolution(ASolution f_aTC_solution) {
		int[] t_rI4_x = f_aTC_solution.getM_rI4_x();
		int[] t_rI4_bag = f_aTC_solution.getM_rI4_bag();
		double[] t_rI8_vol = f_aTC_solution.getM_rI8_vol();
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
				System.out.print(t_rI4_x[t_aI4_j * m_aI4_k + t_aI4_k]);
				if (t_aI4_k < m_aI4_k - 1)
					System.out.print(" ");
				else if (t_aI4_j < m_aI4_n - 1) {
					System.out.print("(" + t_rI4_bag[t_aI4_j] + ")|");
				} else {
					System.out.print("(" + t_rI4_bag[t_aI4_j] + ")|fitness = " + f_aTC_solution.getM_aI8_fitness()
							+ "|ta = " + f_aTC_solution.getM_aI8_ta() + "|th = " + f_aTC_solution.getM_aI8_th() + "|");
				}
			}
		}
		for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
			System.out.print(t_rI8_vol[t_aI4_k]);
			if (t_aI4_k < m_aI4_k - 1)
				System.out.print(" ");
			else {
				System.out.println("|" + (isValidSolution(f_aTC_solution) ? "valid" : "invalid"));
			}
		}
	}

	protected void printConstraint() {
		System.out.println("==========Լ��==========");
		System.out.println("Ta = " + m_aI8_ta + ", Th = " + m_aI8_th);
		for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
			System.out.print(m_rI8_ck[t_aI4_k]);
			if (t_aI4_k < m_aI4_k - 1)
				System.out.print(" ");
			else
				System.out.println();
		}
	}

}
