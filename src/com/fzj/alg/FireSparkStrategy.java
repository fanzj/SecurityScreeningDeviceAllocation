package com.fzj.alg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.fzj.compare.FitnessComparator;
import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.solution.ASolution;
import com.fzj.solution.FireSparkSolution;
import com.fzj.utils.FileUtils;
import com.fzj.utils.MathUtils;

/**
 * @author Fan Zhengjie
 * @date 2016��11��11�� ����3:58:28
 * @version 1.0
 * @description
 */
public class FireSparkStrategy extends AStrategy {

	private double m_aI8_epsilon = 0.0001;
	private int m_aI4_Smin;
	private int m_aI4_Smax;
	private double m_aI8_Ax;
	private int m_aI4_mx;
	private int m_aI4_mm;
	public FireSparkSolution m_aTC_bestFireSpark;// ��Ⱥ�����Ž�
	private FireSparkSolution m_aTC_worstFireSpark;// ��Ⱥ������
	private FireSparkSolution m_aTC_best;// R�����Ž�

	private List<ASolution> m_aTC_R;// ����̻��������ɵĻ���
	private List<ASolution> m_aTC_P;
	private int[] m_rI4_Si;// �̻�i���ɵĻ�����
	private double[] m_rI8_Ai;// �̻�i���Ƶ����
	private boolean[] m_rI1_isSelected;

	private double m_aI8_f;// ����DE�ı���
	private double m_aI8_Cr;// ����DE�Ľ���

	public FireSparkStrategy() {
		super();
	}

	public FireSparkStrategy(int f_aI4_size, int f_aI4_max_nfe, int f_aI4_max_iter, SSModel f_aTC_ssm,
			String f_str_data_path) {
		super(f_aI4_size, f_aI4_max_nfe, f_aI4_max_iter, f_aTC_ssm, f_str_data_path);
	}

	@Override
	protected void init() {
		this.m_aI4_Smin = 2;
		this.m_aI4_Smax = 20;
		this.m_aI4_mm = 25;
		this.m_aI4_mx = 5;
		this.m_aI8_Ax = (m_aI4_upper - 1) / 7.0;
		this.m_aI8_f = 0.5;
		this.m_aI8_Cr = 0.9;

		m_aTC_population = new FireSparkSolution[m_aI4_size];
		m_aTC_R = new ArrayList<>();

		m_aTC_bestFireSpark = new FireSparkSolution(m_aI4_d);
		m_aTC_bestFireSpark.setM_aI8_fitness(Double.MIN_VALUE);
		m_aTC_worstFireSpark = new FireSparkSolution(m_aI4_d);
		m_aTC_worstFireSpark.setM_aI8_fitness(Double.MAX_VALUE);
		m_aTC_best = new FireSparkSolution(m_aI4_d);
		m_aTC_best.setM_aI8_fitness(Double.MIN_VALUE);

		m_rI4_Si = new int[m_aI4_size];
		m_rI8_Ai = new double[m_aI4_size];
		m_rI1_isSelected = new boolean[m_aI4_size];

		m_str_alg_name = NameSpace.s_str_fade;
		m_str_file_name = FileUtils.getResultName(m_str_alg_name, NameSpace.s_str_file_txt, m_aI4_max_nfe);

	}

	@Override
	protected void evolution() {
		while (m_aI4_cur_nfe < m_aI4_max_nfe) {
			m_aTC_R.clear();// ��Ż���
			double t_aI8_sumS = 0.0, t_aI8_sumA = 0.0;
			for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {// ������Ⱥ�е�ÿ���̻�
				FireSparkSolution t_aTC_spark = (FireSparkSolution) m_aTC_population[t_aI4_i];
				t_aI8_sumS += (m_aTC_bestFireSpark.getM_aI8_fitness() - t_aTC_spark.getM_aI8_fitness());// ���ڼ���Si
				t_aI8_sumA += (t_aTC_spark.getM_aI8_fitness() - m_aTC_worstFireSpark.getM_aI8_fitness());// ���ڼ���Ai
			}

			for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {// ������Ⱥ�е�ÿ���̻�
				FireSparkSolution t_aTC_fireSpark = (FireSparkSolution) m_aTC_population[t_aI4_i];
				int[] t_rI4_xi = t_aTC_fireSpark.getM_rI4_x();
				// ����Si��Ai
				m_rI4_Si[t_aI4_i] = (int) Math.round(m_aI4_mm * ((m_aTC_worstFireSpark.getM_aI8_fitness()
						- t_aTC_fireSpark.getM_aI8_fitness() - m_aI8_epsilon) / (t_aI8_sumS - m_aI8_epsilon)));
				m_rI8_Ai[t_aI4_i] = m_aI8_Ax
						* ((t_aTC_fireSpark.getM_aI8_fitness() - m_aTC_bestFireSpark.getM_aI8_fitness() - m_aI8_epsilon)
								/ (t_aI8_sumA - m_aI8_epsilon));
				if (m_rI4_Si[t_aI4_i] < m_aI4_Smin)
					m_rI4_Si[t_aI4_i] = m_aI4_Smin;
				else if (m_rI4_Si[t_aI4_i] > m_aI4_Smax)
					m_rI4_Si[t_aI4_i] = m_aI4_Smax;

				// ���̻�i����t_aTC_fireSpark���б���1
				for (int t_aI4_j = 0; t_aI4_j < m_rI4_Si[t_aI4_i]; t_aI4_j++) {
					FireSparkSolution t_aTC_spark = t_aTC_fireSpark.clone();// ����һ������
					int[] t_rI4_xj = t_aTC_spark.getM_rI4_x();
					int t_aI4_z = (int) Math.round(m_aI4_d * m_aTC_random.nextDouble());// ����z���������ɢ��
					for (int t_aI4_k = 0; t_aI4_k < t_aI4_z; t_aI4_k++) {// ÿ�������൱������ά�ȵ�ĳ��ά
						int t_aI4_dd = m_aTC_random.nextInt(m_aI4_d);// �������һά
						t_rI4_xj[t_aI4_dd] = (int) (t_rI4_xi[t_aI4_dd]
								+ Math.round(m_rI8_Ai[t_aI4_i] * MathUtils.getDoubleAToB(-1, 1)));
						if (t_rI4_xj[t_aI4_dd] > m_aI4_upper)
							t_rI4_xj[t_aI4_dd] = m_aI4_upper;
						if (t_rI4_xj[t_aI4_dd] < m_aI4_low)
							t_rI4_xj[t_aI4_dd] = m_aI4_low;
					}
					t_aTC_spark.setM_rI4_x(t_rI4_xj);
					modify(t_aTC_spark);
					evaluate(t_aTC_spark);
					saveFitness(m_aTC_bestFireSpark);
					m_aI4_cur_nfe++;
					m_aTC_R.add(t_aTC_spark);
				}
			}

			// ��S�����ѡ��mx���̻���Ϊ����P���б���2
			m_aTC_P = new ArrayList<>();
			for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
				m_rI1_isSelected[t_aI4_i] = false;
			}
			for (int t_aI4_i = 0; t_aI4_i < m_aI4_mx; t_aI4_i++) {
				int t_aI4_index = m_aTC_random.nextInt(m_aI4_size);
				while (m_rI1_isSelected[t_aI4_index]) {
					t_aI4_index = m_aTC_random.nextInt(m_aI4_size);
				}
				m_aTC_P.add(((FireSparkSolution) m_aTC_population[t_aI4_index]).clone());
				m_rI1_isSelected[t_aI4_index] = true;
			}

			// ����P���ϵ�ÿһ���̻����б���2
			for (int t_aI4_i = 0; t_aI4_i < m_aTC_P.size(); t_aI4_i++) {
				ASolution t_aTC_s1 = m_aTC_P.get(t_aI4_i);
				int[] t_rI4_xi = t_aTC_s1.getM_rI4_x();
				FireSparkSolution t_aTC_s2 = ((FireSparkSolution)t_aTC_s1).clone();
				int[] t_rI4_xj = t_aTC_s2.getM_rI4_x();
				int t_aI4_z = (int) Math.round(m_aI4_d * m_aTC_random.nextDouble());
				for (int t_aI4_k = 0; t_aI4_k < t_aI4_z; t_aI4_k++) {// ÿ�������൱������ά�ȵ�ĳ��ά
					int t_aI4_dd = m_aTC_random.nextInt(m_aI4_d);// �������һά
					t_rI4_xj[t_aI4_dd] = (int) Math.round((1 + m_aTC_random.nextGaussian() * t_rI4_xi[t_aI4_dd]));
					if (t_rI4_xj[t_aI4_dd] > m_aI4_upper)
						t_rI4_xj[t_aI4_dd] = m_aI4_upper;
					if (t_rI4_xj[t_aI4_dd] < m_aI4_low)
						t_rI4_xj[t_aI4_dd] = m_aI4_low;
				}
				t_aTC_s2.setM_rI4_x(t_rI4_xj);
				modify(t_aTC_s2);
				evaluate(t_aTC_s2);
				saveFitness(m_aTC_bestFireSpark);
				m_aI4_cur_nfe++;
				m_aTC_R.add(t_aTC_s2);
			}

			// R = R��S(SΪ��Ⱥ)
			for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
				m_aTC_R.add(((FireSparkSolution) m_aTC_population[t_aI4_i]).clone());
			}

			// ��ʼ���в�����Ӳ���
			m_aTC_population = new FireSparkSolution[m_aI4_size];
	//		System.out.println(m_aTC_R.size());
//			System.out.println("����ǰ��");
//			printPopulation(m_aTC_R);
			sortRByF();
//			System.out.println("�����");
//			printPopulation(m_aTC_R);
			m_aTC_best = ((FireSparkSolution) m_aTC_R.get(0)).clone();
			while (m_aTC_R.size() > 2 * m_aI4_size) {
				m_aTC_R.remove(m_aTC_R.size() - 1);
			}

			// ��R�����ѡ��m_aI4_size���������Ǽ�����Ⱥ��ô��x��R����һ��ѡ�����
			// �˴��������̶�ѡ��
			countRate(m_aTC_R);// �����ۻ�����
			int t_aI4_k = 0;
			for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
				double t_aI8_r = (m_aTC_random.nextInt(65535) % 1000) / 1000.0;
				for (int t_aI4_j = 0; t_aI4_j < m_aTC_R.size(); t_aI4_j++) {
					FireSparkSolution t_aTC_spark = (FireSparkSolution) m_aTC_R.get(t_aI4_j);
					if (Double.compare(t_aTC_spark.getM_aI8_Pi(), t_aI8_r) >= 0) {
						m_aTC_population[t_aI4_k++] = t_aTC_spark.clone();
						break;
					}
				}
			}

			// ������Ⱥ�е�ÿ���⣬Ӧ��DE�ı��졢���桢ѡ�����
			int t_aI4_r1, t_aI4_r2, t_aI4_r3;
			for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
				FireSparkSolution t_aTC_spark = (FireSparkSolution) m_aTC_population[t_aI4_i];
				int[] t_rI4_xi = t_aTC_spark.getM_rI4_x();

				// ����
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

				FireSparkSolution t_aTC_spark1 = (FireSparkSolution) m_aTC_population[t_aI4_r1];
				FireSparkSolution t_aTC_spark2 = (FireSparkSolution) m_aTC_population[t_aI4_r2];
				FireSparkSolution t_aTC_spark3 = (FireSparkSolution) m_aTC_population[t_aI4_r3];
				int[] t_rI4_xr1 = t_aTC_spark1.getM_rI4_x();
				int[] t_rI4_xr2 = t_aTC_spark2.getM_rI4_x();
				int[] t_rI4_xr3 = t_aTC_spark3.getM_rI4_x();

				int[] t_rI4_vig2 = new int[m_aI4_d];// ����������м����
				for (int t_aI4_j = 0; t_aI4_j < m_aI4_d; t_aI4_j++) {
					t_rI4_vig2[t_aI4_j] = (int) (t_rI4_xr1[t_aI4_j]
							+ Math.round(m_aI8_f * (t_rI4_xr2[t_aI4_j] - t_rI4_xr3[t_aI4_j])));
					if (t_rI4_vig2[t_aI4_j] > m_aI4_upper)
						t_rI4_vig2[t_aI4_j] = m_aI4_upper;
					if (t_rI4_vig2[t_aI4_j] < m_aI4_low)
						t_rI4_vig2[t_aI4_j] = m_aI4_low;
				}

				// ����
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

				FireSparkSolution t_aTC_fireSpark = ((FireSparkSolution) m_aTC_population[t_aI4_i]).clone();
				t_aTC_fireSpark.setM_rI4_x(t_rI4_ui);
				modify(t_aTC_fireSpark);
				evaluate(t_aTC_fireSpark);
				saveFitness(m_aTC_bestFireSpark);
				m_aI4_cur_nfe++;

				if (Double.compare(t_aTC_fireSpark.getM_aI8_fitness(), t_aTC_spark.getM_aI8_fitness()) >= 0) {
					m_aTC_population[t_aI4_i] = t_aTC_fireSpark;
				}
			}

			// ��m_aTC_bestFireSpark��Ϊ��Ⱥ����õĽ�
			updateBestAndWorst();
			if (Double.compare(m_aTC_bestFireSpark.getM_aI8_fitness(), m_aTC_best.getM_aI8_fitness()) < 0) {
				int t_aI4_index = m_aTC_random.nextInt(m_aI4_size);
				if (Double.compare(m_aTC_population[t_aI4_index].getM_aI8_fitness(),
						m_aTC_worstFireSpark.getM_aI8_fitness()) < 0) {
					m_aTC_worstFireSpark = ((FireSparkSolution) m_aTC_population[t_aI4_index]).clone();
				}
				m_aTC_population[t_aI4_index] = m_aTC_best.clone();
				m_aTC_bestFireSpark = m_aTC_best.clone();
			}

			m_aI4_cur_iter++;
		}
		System.out.println(m_str_alg_name+" m_aI4_cur_nfe = "+m_aI4_cur_nfe);
		System.out.println(m_str_alg_name+" m_aI4_cur_iter = "+m_aI4_cur_iter);
	}

	/**
	 * ���ź����ĸ���
	 */
	private void updateBestAndWorst() {
		for (int t_aI4_k = 0; t_aI4_k < m_aI4_size; t_aI4_k++) {
			if (Double.compare(m_aTC_bestFireSpark.getM_aI8_fitness(),
					m_aTC_population[t_aI4_k].getM_aI8_fitness()) < 0) {
				m_aTC_bestFireSpark = ((FireSparkSolution) m_aTC_population[t_aI4_k]).clone();
			}
			if (Double.compare(m_aTC_worstFireSpark.getM_aI8_fitness(),
					m_aTC_population[t_aI4_k].getM_aI8_fitness()) > 0) {
				m_aTC_worstFireSpark = ((FireSparkSolution) m_aTC_population[t_aI4_k]).clone();
			}
		}
	}

	/**
	 * ��R����Ӧ�ȴӴ�С����
	 */
	private synchronized void sortRByF() {
		Collections.sort(m_aTC_R, new FitnessComparator());
	}

	/**
	 * ������Ⱥ����������ۻ����ʣ�ǰ�����Ѿ�����������������Ӧ�ȣ� ��Ϊ���̶�ѡ����Ե�һ����
	 * 
	 * @param f_aTC_list
	 */
	private void countRate(List<ASolution> f_aTC_list) {
		double t_aI8_sumFitness = 0.0;
		for (int t_aI4_i = 0; t_aI4_i < f_aTC_list.size(); t_aI4_i++) {
			FireSparkSolution t_aTC_spark = (FireSparkSolution) f_aTC_list.get(t_aI4_i);
			t_aI8_sumFitness += t_aTC_spark.getM_aI8_fitness();
		}

		FireSparkSolution t_aTC_spark = (FireSparkSolution) f_aTC_list.get(0);
		t_aTC_spark.setM_aI8_Pi(t_aTC_spark.getM_aI8_fitness() / t_aI8_sumFitness);
		for (int t_aI4_i = 1; t_aI4_i < f_aTC_list.size(); t_aI4_i++) {
			FireSparkSolution t_aTC_spark2 = (FireSparkSolution) f_aTC_list.get(t_aI4_i);
			FireSparkSolution t_aTC_spark3 = (FireSparkSolution) f_aTC_list.get(t_aI4_i - 1);
			t_aTC_spark2.setM_aI8_Pi(t_aTC_spark2.getM_aI8_fitness() / t_aI8_sumFitness + t_aTC_spark3.getM_aI8_Pi());
		}
	}

	@Override
	protected ASolution solve(int f_aI4_p) {
		// ��ʼ��
		{
			init();
			initPopulation(NameSpace.s_str_fade);
			updateBestAndWorst();
		}

		// ��ӡ���������ڲ��Ե���
		{
			
			/*  System.out.println("==========��ʼ����Ⱥ==========");
			  printPopulation(m_aTC_population);
			  System.out.println("==========��Ⱥ������==========");
			  printSolution(m_aTC_bestFireSpark);
			  System.out.println("==========��Ⱥ�����==========");
			  printSolution(m_aTC_worstFireSpark);*/
			 
		}

		// ��Ⱥ����
		{
			evolution();
		}

		// ����ı���
		{
			String t_str_result_content = getResultContent(f_aI4_p, m_aTC_bestFireSpark);
			FileUtils.saveFile(m_str_data_path, m_str_alg_name, m_str_file_name, t_str_result_content);
		}

		return m_aTC_bestFireSpark;
	}

	public static void main(String[] args) {

		SSModel t_aTC_ssm = new SSModel(NameSpace.s_str_data_01);
		AStrategy t_aTC_strategy = new FireSparkStrategy(10, 1000, 500, t_aTC_ssm, NameSpace.s_str_data_01);
		t_aTC_strategy.solve(0);

	}

}
