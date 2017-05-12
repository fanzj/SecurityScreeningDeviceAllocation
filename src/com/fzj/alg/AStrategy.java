package com.fzj.alg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.solution.ASolution;
import com.fzj.solution.Fitness;
import com.fzj.utils.MathUtils;

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

	//�������
	protected SSModel m_aTC_ssm;// �����豸����ģ��
	protected int m_aI4_m;// Σ����Ʒ������
	protected int m_aI4_n;// ������
	protected int m_aI4_k;// �����豸����
	protected int m_aI4_q;// ������Ա��
	protected double m_rI8_wi[];// Σ����Ʒ��Ȩ��
	protected double m_rI8_lj[];// ����j����󳤶�
	protected double m_rI8_sj[];// ����j�����
	protected double m_rI8_vk[];// �Զ��豸k�ļ���ٶ�
	protected double m_aI8_a;// ÿ������Я��Σ��Ʒ�ĸ���
	protected double m_rI8_bi[];// Я��Σ��ƷΪi�ĸ���
	protected double m_rI8_ydik[][];// Σ��Ʒi��k��⵽�ĸ���
	protected double m_rI8_ypi[];// i��������Ա���ĸ���
	protected int m_aI4_b;//ÿ���������ɷ�����豸��

	//�м����
	protected double m_rI8_tdjk[][];// ���ʱ�䣬����j���豸k�������ʱ��
	protected double m_rI8_tpj[];//���ʱ�䣬����j��������Ա���ʱ��
	protected double m_aI8_u;//�ܵ�ʱ������
	protected int m_aI4_z;//�����������İ�����Ա��
	
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
		this.m_aI4_q = m_aTC_ssm.getM_aI4_q();
		this.m_rI8_wi = m_aTC_ssm.getM_rI8_wi();
		this.m_rI8_lj = m_aTC_ssm.getM_rI8_lj();
		this.m_rI8_sj = m_aTC_ssm.getM_rI8_sj();
		this.m_rI8_vk = m_aTC_ssm.getM_rI8_vk();
		this.m_aI8_a = m_aTC_ssm.getM_aI8_a();
		this.m_rI8_bi = m_aTC_ssm.getM_rI8_bi();
		this.m_rI8_ydik = m_aTC_ssm.getM_rI8_ydik();
		this.m_rI8_ypi = m_aTC_ssm.getM_rI8_ypi();
		this.m_aI4_b = m_aTC_ssm.getM_aI4_b();
		this.m_aI8_u = m_aTC_ssm.getM_aI8_u();

		this.m_aTC_random = new Random(System.currentTimeMillis());
		this.m_aTC_listOfFitness = new ArrayList<>();
		
		this.m_aI4_low = 0;
		this.m_aI4_upper = 1;

		calDetectTime();// ������ʱ��
	}

	/**
	 * ���Ƿ�Ϊ�Ϸ��⡿ 
	 * ����Լ�� 1.MAX(Td��Tp)<=U
	 * 2.ÿ�������������B�������豸
	 * 3.ÿ���������ٷ����һ�������豸����Ա
	 * 
	 * @param f_aTC_s
	 * @return
	 */
	protected boolean isValidSolution(ASolution f_aTC_s) {
		double t_aI8_time = Math.max(f_aTC_s.getM_aI8_td(), f_aTC_s.getM_aI8_tp());
		if(Double.compare(t_aI8_time, m_aI8_u)>0)
			return false;
		int[] t_rI4_bag = f_aTC_s.getM_rI4_bag();
		int[] t_rI4_fac = f_aTC_s.getM_rI4_fac();
		for(int t_aI4_j=0;t_aI4_j<m_aI4_n;t_aI4_j++){
			if(t_rI4_bag[t_aI4_j]>m_aI4_b || t_rI4_fac[t_aI4_j]<1)
				return false;
		}
		return true;
	}
	
	private void recal(ASolution f_aTC_s){
		int[] t_rI4_x = f_aTC_s.getM_rI4_x();
		int[] t_rI4_bag = new int[m_aI4_n];
		int[] t_rI4_fac = new int[m_aI4_n];
		
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			int t_aI4_device = 0;
			int t_aI4_all = 0;
			for(int t_aI4_k=0;t_aI4_k<m_aI4_k+1;t_aI4_k++){
				int t_aI4_pos = t_aI4_j*(m_aI4_k+1) + t_aI4_k;
				if(t_aI4_k<m_aI4_k){
					t_aI4_device += t_rI4_x[t_aI4_pos];
					t_aI4_all += t_rI4_x[t_aI4_pos];
					continue;
				}
				t_aI4_all += t_rI4_x[t_aI4_pos];
			}
			t_rI4_bag[t_aI4_j] = t_aI4_device;
			t_rI4_fac[t_aI4_j] = t_aI4_all;
		}
		f_aTC_s.setM_rI4_bag(t_rI4_bag);
		f_aTC_s.setM_rI4_fac(t_rI4_fac);
		
		calTdTp(f_aTC_s);
	}

	
	/**
	 * ����������� ���ڲ��Ϸ��Ľ�����Ӧ����������ʹ��Ϸ�
	 * 
	 * @param f_aTC_s
	 */
	protected void modify(ASolution f_aTC_s) {
		recal(f_aTC_s);
		if(isValidSolution(f_aTC_s))
			return;
		
		while(!isValidSolution(f_aTC_s)){
			
			int[] t_rI4_x = f_aTC_s.getM_rI4_x();
			int[] t_rI4_bag = f_aTC_s.getM_rI4_bag();
			int[] t_rI4_fac = f_aTC_s.getM_rI4_fac();
			
			//��������������ʹ֮����
			for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; ) {
				int t_aI4_device = 0;
				int t_aI4_all = 0;
				for(int t_aI4_k=0;t_aI4_k<m_aI4_k+1;t_aI4_k++){
					int t_aI4_pos = t_aI4_j*(m_aI4_k+1) + t_aI4_k;
					if(t_aI4_k<m_aI4_k){
						t_aI4_device += t_rI4_x[t_aI4_pos];
						t_aI4_all += t_rI4_x[t_aI4_pos];
						continue;
					}
					t_aI4_all += t_rI4_x[t_aI4_pos];
				}
				
				if(t_aI4_device>m_aI4_b){//Υ��Լ��
					int t_aI4_p = MathUtils.getIntAtoB(t_aI4_j*(m_aI4_k+1), (t_aI4_j+1)*(m_aI4_k+1)-2);
					while(t_rI4_x[t_aI4_p]==0){
						t_aI4_p = MathUtils.getIntAtoB(t_aI4_j*(m_aI4_k+1), (t_aI4_j+1)*(m_aI4_k+1)-2);
					}
					t_rI4_x[t_aI4_p]=0;
				}else if (t_aI4_all<1) {
					int t_aI4_p = MathUtils.getIntAtoB(t_aI4_j*(m_aI4_k+1), (t_aI4_j+1)*(m_aI4_k+1)-1);
					while(t_rI4_x[t_aI4_p]==1){
						t_aI4_p = MathUtils.getIntAtoB(t_aI4_j*(m_aI4_k+1), (t_aI4_j+1)*(m_aI4_k+1)-1);
					}
					t_rI4_x[t_aI4_p]=1;
				}
				else {
					t_rI4_bag[t_aI4_j] = t_aI4_device;
					t_rI4_fac[t_aI4_j] = t_aI4_all;
					t_aI4_j++;
				}
			}
			
			//�����������㣬����ʱ���Ƿ�Խ��
			recal(f_aTC_s);
			double t_aI8_time = Math.max(f_aTC_s.getM_aI8_td(), f_aTC_s.getM_aI8_tp());
			while(Double.compare(t_aI8_time, m_aI8_u)>0){
				//System.out.println("ʱ��Խ��");
				int t_aI4_p = m_aTC_random.nextInt(m_aI4_d);
				while(t_rI4_x[t_aI4_p]==0){
					t_aI4_p = m_aTC_random.nextInt(m_aI4_d);
				}
				t_rI4_x[t_aI4_p]=0;
				calTdTp(f_aTC_s);
				t_aI8_time = Math.max(f_aTC_s.getM_aI8_td(), f_aTC_s.getM_aI8_tp());
			}
			recal(f_aTC_s);
		}
		
	}


	/**
	 * ������ʱ��
	 */
	private void calDetectTime() {
		m_rI8_tdjk = new double[m_aI4_n][m_aI4_k];
		m_rI8_tpj = new double[m_aI4_n];
		for(int t_aI4_j=0;t_aI4_j<m_aI4_n;t_aI4_j++){
			for(int t_aI4_k=0;t_aI4_k<m_aI4_k+1;t_aI4_k++){
				if(t_aI4_k<m_aI4_k){
					m_rI8_tdjk[t_aI4_j][t_aI4_k] = m_rI8_lj[t_aI4_j]/m_rI8_vk[t_aI4_k];
				}
			}
			m_rI8_tpj[t_aI4_j] = 10*Math.sqrt(m_rI8_sj[t_aI4_j]);
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
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
			ASolution t_aTC_solution = null;
			t_aTC_solution = new ASolution(m_aI4_d);
			/*if (f_str_alg_name.equals(NameSpace.s_str_bpso)) {
				t_aTC_solution = new ASolution(m_aI4_d);
			} else if (f_str_alg_name.equals(NameSpace.s_str_dnspso)) {
				t_aTC_solution = new ASolution(m_aI4_d);
			} else if (f_str_alg_name.equals(NameSpace.s_str_dednspso)) {
				t_aTC_solution = new ASolution(m_aI4_d);
			}else if (f_str_alg_name.equals(NameSpace.s_str_wwo)) {
				t_aTC_solution = new ASolution(m_aI4_d);
			}*/
			int[] t_rI4_x = new int[m_aI4_d];
			int[] t_rI4_bag = new int[m_aI4_n];
			int[] t_rI4_fac = new int[m_aI4_n];
			for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
				int t_aI4_device = 0;
				int t_aI4_all = 0;
				while(t_aI4_device>m_aI4_b || t_aI4_all<1){//��������Լ��
					t_aI4_device = 0;
					t_aI4_all = 0;
					for(int t_aI4_k=0;t_aI4_k<m_aI4_k+1;t_aI4_k++){
						int t_aI4_pos = t_aI4_j*(m_aI4_k+1) + t_aI4_k;
						t_rI4_x[t_aI4_pos] = m_aTC_random.nextInt(2);
						if(t_aI4_k<m_aI4_k){
							t_aI4_device += t_rI4_x[t_aI4_pos];
							t_aI4_all += t_rI4_x[t_aI4_pos];
							continue;
						}
						t_aI4_all += t_rI4_x[t_aI4_pos];
					}
				}
				t_rI4_bag[t_aI4_j] = t_aI4_device;
				t_rI4_fac[t_aI4_j] = t_aI4_all;
			}

			t_aTC_solution.setM_rI4_x(t_rI4_x);
			t_aTC_solution.setM_rI4_bag(t_rI4_bag);
			t_aTC_solution.setM_rI4_fac(t_rI4_fac);
			m_aTC_population[t_aI4_i] = t_aTC_solution;
			modify(m_aTC_population[t_aI4_i]);
			evaluate(m_aTC_population[t_aI4_i]);
			
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
	 * ����Td��Tp
	 * @param f_aTC_solution
	 */
	protected void calTdTp(ASolution f_aTC_solution){
		int t_rI4_x[] = f_aTC_solution.getM_rI4_x();
		double t_rI8_tk[] = new double[m_aI4_k];//���������豸�ļ��ʱ��
		double t_aI8_td = 0;
		double t_aI8_tp = 0;
		int t_aI4_z = 0;
		List<Double> t_aTC_bag = new ArrayList<>();
		for(int t_aI4_j=0;t_aI4_j<m_aI4_n;t_aI4_j++){
			for(int t_aI4_k=0;t_aI4_k<m_aI4_k+1;t_aI4_k++){
				int t_aI4_pos = t_aI4_j*(m_aI4_k+1) + t_aI4_k;
				int t_aI4_dev = t_aI4_pos%(m_aI4_k+1);
				if(t_aI4_dev<m_aI4_k){
					t_rI8_tk[t_aI4_dev] += t_rI4_x[t_aI4_pos]*m_rI8_tdjk[t_aI4_j][t_aI4_k];
				}else {
					if(t_rI4_x[t_aI4_pos]==1){
						t_aTC_bag.add(m_rI8_tpj[t_aI4_j]);
						t_aI4_z ++;
					}
				}
			}
		}
		for(int t_aI4_k=0;t_aI4_k<m_aI4_k;t_aI4_k++){
			t_aI8_td = Math.max(t_aI8_td, t_rI8_tk[t_aI4_k]);
		}
		
		Collections.sort(t_aTC_bag);
		int t_aI4_o = t_aI4_z/m_aI4_q-1;
		for(int t_aI4_l=0;t_aI4_l<=t_aI4_o;t_aI4_l++){
			int t_aI4_j = t_aI4_l*m_aI4_q + t_aI4_z%m_aI4_q;
			t_aI8_tp += (t_aTC_bag.get(t_aI4_j)+t_aTC_bag.get(t_aTC_bag.size()-1));
		}
		f_aTC_solution.setM_aI8_td(t_aI8_td);
		f_aTC_solution.setM_aI8_tp(t_aI8_tp);
	}

	/**
	 * ����Ӧ�ȼ��� & ͬʱԼ�������ļ��㡿 1.���ʱ����м��� 2.�����豸����İ����� 3.�����豸�ĵ�ǰ������
	 */
	protected void evaluate(ASolution f_aTC_solution) {
		calTdTp(f_aTC_solution);
		int[] t_rI4_x = f_aTC_solution.getM_rI4_x();
		double t_aI8_fitness = 0.0;
		double t_aI8_pij = 0;
		double t_aI8_sum = 0;
		double[][] t_rI8_p = new double[m_aI4_m][m_aI4_n];
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_m; t_aI4_i++) {
			for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
				double t_aI8_fail = 1;
				for (int t_aI4_k = 0; t_aI4_k < m_aI4_k+1; t_aI4_k++) {
					int t_aI4_pos = t_aI4_j * (m_aI4_k+1) + t_aI4_k;
					if(t_aI4_k<m_aI4_k){
						t_aI8_fail *= (Math.pow(1 - m_rI8_ydik[t_aI4_i][t_aI4_k], t_rI4_x[t_aI4_pos]));
					//	break;
					}else {
						t_aI8_fail *= Math.pow(1-m_rI8_ypi[t_aI4_i], t_rI4_x[t_aI4_pos]);
					}
				}
				
				t_aI8_pij = 1 - t_aI8_fail;
				//System.out.println("pij = "+t_aI8_pij);
				t_rI8_p[t_aI4_i][t_aI4_j] = t_aI8_pij;
				
				//t_aI8_sum += m_aI8_a * m_rI8_bi[t_aI4_i] * t_rI8_p[t_aI4_i][t_aI4_j];
				t_aI8_fitness += m_rI8_wi[t_aI4_i] * m_aI8_a * m_rI8_bi[t_aI4_i] * t_rI8_p[t_aI4_i][t_aI4_j];
			}
		}
		
	/*	for (int t_aI4_i = 0; t_aI4_i < m_aI4_m; t_aI4_i++) {
			for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
				t_aI8_fitness += m_rI8_wi[t_aI4_i] * ((m_aI8_a * m_rI8_bi[t_aI4_i] * t_rI8_p[t_aI4_i][t_aI4_j])/t_aI8_sum);
			}
		}*/
		//System.out.println("t_aI8_sum = "+t_aI8_sum+", t_aI8_fitness = "+t_aI8_fitness);
		f_aTC_solution.setM_cur_nfe(m_aI4_cur_nfe);
		f_aTC_solution.setM_cur_iter(m_aI4_cur_iter);
		f_aTC_solution.setM_aI8_fitness(t_aI8_fitness);
		
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
		int[] t_rI8_fac = f_aTC_solution.getM_rI4_fac();
		t_aTC_sb.append(f_aI4_p+" solution:\n");
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k+1; t_aI4_k++) {
				int t_aI4_pos = t_aI4_j * (m_aI4_k+1) + t_aI4_k;
				t_aTC_sb.append(t_rI4_x[t_aI4_pos]);
				if (t_aI4_k < m_aI4_k)
					t_aTC_sb.append(" ");
				else if (t_aI4_j < m_aI4_n - 1) {
					t_aTC_sb.append("(" + t_rI4_bag[t_aI4_j] +","+t_rI8_fac[t_aI4_j]+ ")|");
				} else {
					t_aTC_sb.append("(" + t_rI4_bag[t_aI4_j] +","+t_rI8_fac[t_aI4_j]+ ")|fitness = " + f_aTC_solution.getM_aI8_fitness()
							+ "|td = " + f_aTC_solution.getM_aI8_td() + "|tp = " + f_aTC_solution.getM_aI8_tp() + "|");
				}
			}
		}
		
		t_aTC_sb.append((isValidSolution(f_aTC_solution) ? "valid" : "invalid")+"\n");
		
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
		int[] t_rI8_fac = f_aTC_solution.getM_rI4_fac();
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k+1; t_aI4_k++) {
				int t_aI4_pos = t_aI4_j * (m_aI4_k+1) + t_aI4_k;
				System.out.print(t_rI4_x[t_aI4_pos]);
				if (t_aI4_k < m_aI4_k)
					System.out.print(" ");
				else if (t_aI4_j < m_aI4_n - 1) {
					System.out.print("(" + t_rI4_bag[t_aI4_j] +","+t_rI8_fac[t_aI4_j] +")|");
				} else {
					System.out.print("(" + t_rI4_bag[t_aI4_j]+","+t_rI8_fac[t_aI4_j] + ")|fitness = " + f_aTC_solution.getM_aI8_fitness()
							+ "|td = " + f_aTC_solution.getM_aI8_td() + "|tp = " + f_aTC_solution.getM_aI8_tp() + "|");
				}
			}
		}
		System.out.println((isValidSolution(f_aTC_solution) ? "valid" : "invalid"));
		
	}
	
	protected double[] int2doubleArray(int[] f_rI4_arr){
		double[] t_rI8_res = new double[f_rI4_arr.length];
		for(int t_aI4_i=0;t_aI4_i<f_rI4_arr.length;t_aI4_i++){
			t_rI8_res[t_aI4_i] = f_rI4_arr[t_aI4_i];
		}
		return t_rI8_res;
	}


}
