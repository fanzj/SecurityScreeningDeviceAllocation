package com.fzj.alg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.fzj.compare.TimeComparator;
import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.solution.ASolution;
import com.fzj.solution.FireSparkSolution;
import com.fzj.solution.Fitness;
import com.fzj.solution.GASolution;
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
	protected int m_aI4_q;// ������Ա��
	protected double m_aI4_max_t; // ʱ������
	protected double m_rI8_wi[];// Σ����Ʒ��Ȩ��
	protected double m_rI8_t0j[];// ����ﵽʱ��
	protected double m_rI8_lj[];// ����j����󳤶�
	protected double m_rI8_vj[];// ����j�����
	protected double m_rI8_sk[];// �Զ��豸k�ļ���ٶ�
	protected double m_rI8_aij[][];// ÿ������Я��Σ��Ʒ�ĸ���
	protected double m_rI8_bik[][];// i��k���ĸ���
	protected double m_rI8_yiq[][];// Σ��Ʒi��q��⵽�ĸ���

	// �м����
	protected double m_rI8_tjk[][];// ���ʱ�䣬����j���豸k�������ʱ��
	protected double m_rI8_tj[];// ���ʱ�䣬j���˼���ʱ��

	protected Random m_aTC_random;// �����������

	protected List<Fitness> m_aTC_listOfFitness;// ���ڽ���ı�����ʾ
	protected ASolution[] m_aTC_population;// ��Ⱥ

	protected long m_aI8_max_time;// �����������ʱ��

	public AStrategy() {
	}

	public AStrategy(int f_aI4_size, int f_aI4_max_nfe, int f_aI4_max_iter, SSModel f_aTC_ssm, String f_str_data_path,
			long f_aI8_max_time) {
		this.m_aI4_size = f_aI4_size;
		this.m_aI4_max_nfe = f_aI4_max_nfe;
		this.m_aI4_max_iter = f_aI4_max_iter;
		this.m_aTC_ssm = f_aTC_ssm;
		this.m_str_data_path = f_str_data_path;
		this.m_aI8_max_time = f_aI8_max_time;

		this.m_aI4_cur_iter = 0;
		this.m_aI4_cur_nfe = 0;

		this.m_aI4_d = m_aTC_ssm.getM_aI4_d();
		this.m_aI4_m = m_aTC_ssm.getM_aI4_m();
		this.m_aI4_n = m_aTC_ssm.getM_aI4_n();
		this.m_aI4_k = m_aTC_ssm.getM_aI4_k();
		this.m_aI4_q = m_aTC_ssm.getM_aI4_q();
		this.m_aI4_max_t = m_aTC_ssm.getM_aI8_max_t();
		this.m_rI8_wi = m_aTC_ssm.getM_rI8_wi();
		this.m_rI8_t0j = m_aTC_ssm.getM_rI8_t0j();
		this.m_rI8_lj = m_aTC_ssm.getM_rI8_lj();
		this.m_rI8_vj = m_aTC_ssm.getM_rI8_vj();
		this.m_rI8_sk = m_aTC_ssm.getM_rI8_sk();
		this.m_rI8_aij = m_aTC_ssm.getM_rI8_aij();
		this.m_rI8_bik = m_aTC_ssm.getM_rI8_bik();
		this.m_rI8_yiq = m_aTC_ssm.getM_rI8_yiq();

		this.m_aTC_random = new Random(System.currentTimeMillis());
		this.m_aTC_listOfFitness = new ArrayList<>();

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
		if (Double.compare(f_aTC_s.getM_aI8_maxtime(), m_aI4_max_t) <= 0)
			return true;
		return false;
	}

	/**
	 * ����������� ���ڲ��Ϸ��Ľ�����Ӧ����������ʹ��Ϸ�
	 * 
	 * @param f_aTC_s
	 */
	protected void modify(ASolution f_aTC_s) {
	/*	calT2(f_aTC_s);

		while (!isValidSolution(f_aTC_s)) {
			int[] t_rI4_x = new int[m_aI4_d];
			for (int t_aI4_i = 0; t_aI4_i < m_aI4_d; t_aI4_i++) {
				if (t_aI4_i % 2 == 0)// Xj
					t_rI4_x[t_aI4_i] = m_aTC_random.nextInt(m_aI4_k) + 1;// [1,K]
				else// Yj
					t_rI4_x[t_aI4_i] = m_aTC_random.nextInt(m_aI4_q + 1);// [0,Q]
			}

			f_aTC_s.setM_rI4_x(t_rI4_x);
			calT2(f_aTC_s);
			
		}*/

	}

	/**
	 * @param f_aI4_j
	 *            ������
	 * @return
	 */
	private List<Integer> getPre(int f_aI4_j, int[] f_rI4_x) {
		List<Integer> t_aTC_list = new ArrayList<>();// ��Ű������
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_d; t_aI4_i++) {
			if (t_aI4_i % 2 == 0) {
				if (f_rI4_x[f_aI4_j * 2] == f_rI4_x[t_aI4_i]
						&& Double.compare(m_rI8_t0j[t_aI4_i / 2], m_rI8_t0j[f_aI4_j]) < 0)
					t_aTC_list.add(t_aI4_i / 2);
			}
		}
		return t_aTC_list;
	}

	private double[] calT1(ASolution f_aTC_s) {
		int[] t_rI4_x = f_aTC_s.getM_rI4_x();
		double[] t_rI8_t1 = new double[m_aI4_n];
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {// ��ÿ������
			int t_aI4_xj = t_rI4_x[t_aI4_j * 2];// ���߱���
			// t_rI8_t1[t_aI4_j] += m_rI8_t0j[t_aI4_j];
			// ����Pre(j)����
			List<Integer> t_aTC_pre = getPre(t_aI4_j, t_rI4_x);
			for (int t_aI4_i = 0; t_aI4_i < t_aTC_pre.size(); t_aI4_i++) {
				t_rI8_t1[t_aI4_j] += m_rI8_tjk[t_aTC_pre.get(t_aI4_i)][t_aI4_xj - 1];
			}
		}
		return t_rI8_t1;
	}

	private List<Integer> getBq(int f_aI4_yj, int[] f_rI4_x, double[] f_rI8_t1) {
		List<Integer> t_aTC_list = new ArrayList<>();// ��Ű������
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_d; t_aI4_i++) {
			if (t_aI4_i % 2 != 0) {
				int t_aI4_yj = f_rI4_x[t_aI4_i];// ���߱���
				if (f_aI4_yj != 0 && f_aI4_yj == t_aI4_yj)
					t_aTC_list.add((t_aI4_i - 1) / 2);
			}
		}
		return t_aTC_list;
	}

	private double calT2(ASolution f_aTC_s) {
		double[] t_rI8_t1j = calT1(f_aTC_s);
		int[] t_rI4_x = f_aTC_s.getM_rI4_x();
		double[] t_rI8_t2 = new double[m_aI4_n];
		double t_aI8_t = 0;
		for (int t_aI4_q = 0; t_aI4_q <= m_aI4_q; t_aI4_q++) {
			// ����Bq����
			List<Integer> t_aTC_Bq = getBq(t_aI4_q, t_rI4_x, t_rI8_t1j);
			// System.out.println("����ǰ��"+t_aTC_Bq);
			// Bq����
			Collections.sort(t_aTC_Bq, new TimeComparator(t_rI8_t1j));
			/*
			 * System.out.println("�����"+t_aTC_Bq); for(int
			 * i=0;i<t_aTC_Bq.size();i++){
			 * System.out.print(t_rI8_t1j[t_aTC_Bq.get(i)]);
			 * if(i<t_aTC_Bq.size()-1) System.out.print(" "); else
			 * System.out.println(); }
			 */
			for (int t_aI4_i = 0; t_aI4_i < t_aTC_Bq.size(); t_aI4_i++) {
				int t_aI4_num = t_aTC_Bq.get(t_aI4_i);// ������
				if (t_aI4_i == 0) {
					t_rI8_t2[t_aI4_num] += (t_rI8_t1j[t_aI4_num] + m_rI8_tj[t_aI4_num]);
				} else {
					int t_aI4_pre_num = t_aTC_Bq.get(t_aI4_i - 1);
					t_rI8_t2[t_aI4_num] = (Math.max(t_rI8_t1j[t_aI4_num], t_rI8_t2[t_aI4_pre_num])
							+ m_rI8_tj[t_aI4_num]);
				}
			}
		}
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			t_aI8_t = Math.max(t_aI8_t, t_rI8_t2[t_aI4_j]);
		}
		f_aTC_s.setM_aI8_maxtime(t_aI8_t);
		return t_aI8_t;
	}

	/*
	 * private List<Integer> getPre2(int f_aI4_j,int[] f_rI4_x,double[]
	 * f_rI8_t1){ List<Integer> t_aTC_list = new ArrayList<>();//��Ű������ for(int
	 * t_aI4_i=0;t_aI4_i<m_aI4_d;t_aI4_i++){ if(t_aI4_i%2!=0){ int t_aI4_yj =
	 * f_rI4_x[t_aI4_i];// ���߱��� if(t_aI4_yj !=0 &&
	 * f_rI4_x[f_aI4_j*2+1]==f_rI4_x[t_aI4_i] &&
	 * Double.compare(f_rI8_t1[(t_aI4_i-1)/2], f_rI8_t1[f_aI4_j])<0)
	 * t_aTC_list.add((t_aI4_i-1)/2); } } return t_aTC_list; }
	 */

	/*
	 * private double calT2(ASolution f_aTC_s){ double[] t_rI8_t1j =
	 * calT1(f_aTC_s); int[] t_rI4_x = f_aTC_s.getM_rI4_x(); double[] t_rI8_t2 =
	 * new double[m_aI4_n]; double t_aI8_t = 0; for(int
	 * t_aI4_j=0;t_aI4_j<m_aI4_n;t_aI4_j++){//��ÿ������ int t_aI4_yj =
	 * t_rI4_x[t_aI4_j * 2+1];// ���߱��� t_rI8_t2[t_aI4_j] += t_rI8_t1j[t_aI4_j];
	 * //����Pre(j)���� List<Integer> t_aTC_pre = getPre2(t_aI4_j,
	 * t_rI4_x,t_rI8_t1j); for(int
	 * t_aI4_i=0;t_aI4_i<t_aTC_pre.size();t_aI4_i++){ t_rI8_t2[t_aI4_j] +=
	 * m_rI8_tj[t_aTC_pre.get(t_aI4_i)]; }
	 * 
	 * t_aI8_t = Math.max(t_aI8_t, t_rI8_t2[t_aI4_j]); }
	 * f_aTC_s.setM_aI8_maxtime(t_aI8_t); return t_aI8_t; }
	 */

	/**
	 * ������ʱ��
	 */
	private void calDetectTime() {
		m_rI8_tj = new double[m_aI4_n];
		m_rI8_tjk = new double[m_aI4_n][m_aI4_k];
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			m_rI8_tj[t_aI4_j] = 5 * Math.sqrt(m_rI8_vj[t_aI4_j]);
			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
				m_rI8_tjk[t_aI4_j][t_aI4_k] = m_rI8_lj[t_aI4_j] / m_rI8_sk[t_aI4_k];
			}
		}
	}

	/**
	 * ������ʼ��
	 */
	protected abstract void init();

	/**
	 * ��Ⱥ��ʼ�� ����ʱ��Լ��
	 */
	protected void initPopulation(String f_str_alg_name) {
		for (int t_aI4_k = 0; t_aI4_k < m_aI4_size;) {
			ASolution t_aTC_solution = null;
			if (f_str_alg_name.equals(NameSpace.s_str_dnspso) || f_str_alg_name.equals(NameSpace.s_str_dednspso)
					|| f_str_alg_name.equals(NameSpace.s_str_de)) {
				t_aTC_solution = new ParticleSolution(m_aI4_d);
			} else if (f_str_alg_name.equals(NameSpace.s_str_fade)) {
				t_aTC_solution = new FireSparkSolution(m_aI4_d);
			} else if (f_str_alg_name.equals(NameSpace.s_str_wwo)) {
				t_aTC_solution = new WaveSolution(m_aI4_d);
			} else if (f_str_alg_name.equals(NameSpace.s_str_ga)){
				t_aTC_solution = new GASolution(m_aI4_d);
			}
			int[] t_rI4_x = new int[m_aI4_d];
			for (int t_aI4_i = 0; t_aI4_i < m_aI4_d; t_aI4_i++) {
				if (t_aI4_i % 2 == 0)// Xj
					t_rI4_x[t_aI4_i] = m_aTC_random.nextInt(m_aI4_k) + 1;// [1,K]
				else// Yj
					t_rI4_x[t_aI4_i] = m_aTC_random.nextInt(m_aI4_q + 1);// [0,Q]
			}

			t_aTC_solution.setM_rI4_x(t_rI4_x);
			calT2(t_aTC_solution);
			if (!isValidSolution(t_aTC_solution)) {
				continue;
			}
			// modify(m_aTC_population[t_aI4_k]);//������������ʱ��Լ��
			// System.out.println("k = "+t_aI4_k);
			m_aTC_population[t_aI4_k] = t_aTC_solution;
			evaluate(m_aTC_population[t_aI4_k]);
			t_aI4_k++;
		}
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
	 * ����Ӧ�ȼ��㡿 ������Ӧ�ȼ���Ľ�ض��Ѿ�����ʱ��Լ��
	 */
	protected void evaluate(ASolution f_aTC_solution) {
		calT2(f_aTC_solution);//new 
		int[] t_rI4_x = f_aTC_solution.getM_rI4_x();
		double t_aI8_fitness = 0.0;
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_m; t_aI4_i++) {
			for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
				int t_aI4_xj = t_rI4_x[t_aI4_j * 2];// ���߱���[1,K]
				int t_aI4_yj = t_rI4_x[t_aI4_j * 2 + 1];// [0,Q]
				// System.out.println("x = "+t_aI4_xj+", y = "+t_aI4_yj);

				t_aI8_fitness += (m_rI8_wi[t_aI4_i] * m_rI8_aij[t_aI4_i][t_aI4_j]
						* (1 - (1 - m_rI8_bik[t_aI4_i][t_aI4_xj - 1]) * (1 - m_rI8_yiq[t_aI4_i][t_aI4_yj])));
			}
		}
		
		f_aTC_solution.setM_cur_nfe(m_aI4_cur_nfe);
		f_aTC_solution.setM_cur_iter(m_aI4_cur_iter);
		//f_aTC_solution.setM_aI8_fitness(t_aI8_fitness);
		
		double t_rI8_px = 0;
		if(!isValidSolution(f_aTC_solution)){
			t_rI8_px = f_aTC_solution.getM_aI8_maxtime() - m_aI4_max_t;
		}
//		System.out.println("t_rI8_px = "+t_rI8_px);
//		System.out.println("t_aI8_fitness - t_rI8_px = "+(t_aI8_fitness-t_rI8_px));
		f_aTC_solution.setM_aI8_fitness(t_aI8_fitness-5*t_rI8_px);

		
	}

	/**
	 * ����Ⱥ������
	 */
	protected abstract void evolution();

	/**
	 * ��������⡿
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
		t_aTC_sb.append(f_aI4_p + " solution:\n");
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_d; t_aI4_j++) {
			t_aTC_sb.append(t_rI4_x[t_aI4_j]);
			if (t_aI4_j % 2 != 0)
				t_aTC_sb.append('|');
			else
				t_aTC_sb.append(' ');
		}
		t_aTC_sb.append("fitness = " + f_aTC_solution.getM_aI8_fitness() + "|time = "
				+ f_aTC_solution.getM_aI8_maxtime() + "|" + isValidSolution(f_aTC_solution) + "\n");
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
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_d; t_aI4_j++) {
			System.out.print(t_rI4_x[t_aI4_j]);
			if (t_aI4_j % 2 != 0)
				System.out.print("|");
			else
				System.out.print(" ");
		}
		System.out.println("fitness = " + f_aTC_solution.getM_aI8_fitness() + "|time = "
				+ f_aTC_solution.getM_aI8_maxtime() + "|" + isValidSolution(f_aTC_solution));
	}

}
