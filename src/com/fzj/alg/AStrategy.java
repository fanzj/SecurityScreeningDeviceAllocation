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
 * @date 2017年1月28日 下午2:50:40
 * @version 1.0
 * @description 抽象策略类
 */
public abstract class AStrategy {

	protected String m_str_data_path;// 实验数据路径，例：data_01\\
	protected String m_str_alg_name;// 算法名，当前使用何种算法，例：PSO
	protected String m_str_file_name;// 保存文件的名字

	protected int m_aI4_max_iter;// 最大进化次数
	protected int m_aI4_max_nfe;// 最大评价次数

	protected int m_aI4_size;// 种群规模
	protected int m_aI4_d;// 问题维度
	protected int m_aI4_cur_iter;// 当前进化代数
	protected int m_aI4_cur_nfe;// 当前评价次数

	protected SSModel m_aTC_ssm;// 安检设备调度模型
	protected int m_aI4_m;// 危险物品种类数
	protected int m_aI4_n;// 包裹数
	protected int m_aI4_k;// 安检设备种类
	protected int m_aI4_p;// 每个包裹所分配的最大设备数
	protected double m_rI8_wi[];// 危险物品的权重
	protected int m_rI4_yk[];// 设备k是自动设备还是手动设备
	protected double m_rI8_lj[];// 包裹j的最大长度
	protected double m_rI8_sj[];// 包裹j的体积
	protected double m_rI8_vak[];// 自动设备k的检测速度
	protected double m_rI8_vhk[];// 手动设备k的检测速度
	protected double m_aI8_ta;// 自动设备检测的时间上限
	protected double m_aI8_th;// 手动检测设备的时间上限
	protected double m_rI8_ck[];// 设备k的容积
	protected double m_aI8_a;// 每个包裹携带危险品的概率
	protected double m_rI8_bi[];// 携带危险品为i的概率
	protected double m_rI8_yik[][];// 危险品i被k检测到的概率

	protected double m_rI8_tjk[][];// 检测时间，包裹j被设备k检测所需时间

	protected Random m_aTC_random;// 随机数生成器

	protected int m_aI4_low;// 约束下限
	protected int m_aI4_upper;// 约束上限
	
	protected List<Fitness> m_aTC_listOfFitness;// 用于结果的保存显示
	protected ASolution[] m_aTC_population;// 种群

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

		calDetectTime();// 计算检测时间
	}

	/**
	 * 【是否为合法解】 满足约束 1.每个解的 自动检测时间小于上限，手动检测时间小于上限 2.每个解 分配到各个设备的容量都在范围之内
	 * 3.每个包裹至少分配给一个设备
	 * 
	 * @param f_aTC_s
	 * @return
	 */
	protected boolean isValidSolution(ASolution f_aTC_s) {
		// 时间约束1不满足
		if (Double.compare(f_aTC_s.getM_aI8_ta(), m_aI8_ta) > 0 || Double.compare(f_aTC_s.getM_aI8_th(), m_aI8_th) > 0)
			return false;
		// 验证约束2
		double t_rI8_vol[] = f_aTC_s.getM_rI8_vol();
		for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
			if (Double.compare(t_rI8_vol[t_aI4_k], m_rI8_ck[t_aI4_k]) > 0)
				return false;
		}
		// 验证约束3
		int t_rI4_bag[] = f_aTC_s.getM_rI4_bag();
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			if (t_rI4_bag[t_aI4_j] <= 0)
				return false;
		}
		return true;
	}

	/**
	 * 【解的修正】 对于不合法的解做相应的修正操作使其合法
	 * 
	 * @param f_aTC_s
	 */
	protected void modify(ASolution f_aTC_s) {
		reCal(f_aTC_s);
		int t_aI4_c = 0;// 循环次数
		while (!isValidSolution(f_aTC_s)) {
			t_aI4_c++;
			int[] t_rI4_x = f_aTC_s.getM_rI4_x();
			double t_aI8_ta = f_aTC_s.getM_aI8_ta();// 自动检测时间
			double t_aI8_th = f_aTC_s.getM_aI8_th();// 手动检测时间
			int[] t_rI4_bag = f_aTC_s.getM_rI4_bag();// 分配到每个包裹的设备数
			double[] t_rI8_vol = f_aTC_s.getM_rI8_vol();// 分配给各个设备的当前容量

			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {// 检测各类设备
				int t_aI4_con2 = 0;
				while (Double.compare(t_rI8_vol[t_aI4_k], m_rI8_ck[t_aI4_k]) > 0) {// 检测违反约束2
					t_aI4_con2++;
					//System.out.println("t_aI4_con2 = "+t_aI4_con2);
					if(t_aI4_con2>20)//防止陷入死循环
						break;
					int t_aI4_j = m_aTC_random.nextInt(m_aI4_n);
					int t_aI4_pos = t_aI4_j * m_aI4_k + t_aI4_k;
					//【bug 如果不作处理，易陷入死循环】
					if (t_rI4_x[t_aI4_pos] == 1 && t_rI4_bag[t_aI4_j] >1) {// 约束3
						t_rI4_x[t_aI4_pos] = 0;
						t_rI8_vol[t_aI4_k] -= (m_rI8_sj[t_aI4_j] * m_rI4_yk[t_aI4_k]);
						t_rI4_bag[t_aI4_j]--;
						t_aI8_ta -= m_rI8_tjk[t_aI4_j][t_aI4_k] * m_rI4_yk[t_aI4_k];
						t_aI8_th -= m_rI8_tjk[t_aI4_j][t_aI4_k] * (1 - m_rI4_yk[t_aI4_k]);
					}
					
				}
			}
			//如果此时解已合法，则结束
			if(isValidSolution(f_aTC_s)){
				//System.out.println("valid solution - 1");
				return;
			}
				
			
			for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {// 每个包裹
				//int t_aI4_test_2 = 0;
				while (t_rI4_bag[t_aI4_j] < 1) {// 违反约束3
					int t_aI4_k = m_aTC_random.nextInt(m_aI4_k);
					int t_aI4_pos = t_aI4_j * m_aI4_k + t_aI4_k;
				//	System.out.println("t_aI4_test_2 = "+(t_aI4_test_2++));
					if (t_rI4_x[t_aI4_pos] == 0 && Double.compare(t_rI8_vol[t_aI4_k], m_rI8_ck[t_aI4_k]) <= 0) {// 约束2	
						t_rI4_x[t_aI4_pos] = 1;
						t_rI8_vol[t_aI4_k] += (m_rI8_sj[t_aI4_j] * m_rI4_yk[t_aI4_k]);
						t_rI4_bag[t_aI4_j]++;
						t_aI8_ta += m_rI8_tjk[t_aI4_j][t_aI4_k] * m_rI4_yk[t_aI4_k];
						t_aI8_th += m_rI8_tjk[t_aI4_j][t_aI4_k] * (1 - m_rI4_yk[t_aI4_k]);
					}
					
				}
			}

			//如果此时解已合法，则结束
			if(isValidSolution(f_aTC_s)){
				//System.out.println("valid solution - 2");
				return;
			}
			
			int t_aI4_con_t = 0;
			while(Double.compare(f_aTC_s.getM_aI8_ta(), m_aI8_ta)>0 || Double.compare(f_aTC_s.getM_aI8_th(), m_aI8_th)>0){
				t_aI4_con_t++;
				//System.out.println("t_aI4_con2 = "+t_aI4_con2);
				if(t_aI4_con_t>20)//防止陷入死循环
					break;
				int t_aI4_j = m_aTC_random.nextInt(m_aI4_n);
				int t_aI4_k = m_aTC_random.nextInt(m_aI4_k);
				int t_aI4_pos = t_aI4_j * m_aI4_k + t_aI4_k;
				//【bug 如果不作处理，易陷入死循环】
				if (t_rI4_x[t_aI4_pos] == 1 && t_rI4_bag[t_aI4_j] >1) {// 约束3
					t_rI4_x[t_aI4_pos] = 0;
					t_rI8_vol[t_aI4_k] -= (m_rI8_sj[t_aI4_j] * m_rI4_yk[t_aI4_k]);
					t_rI4_bag[t_aI4_j]--;
					t_aI8_ta -= m_rI8_tjk[t_aI4_j][t_aI4_k] * m_rI4_yk[t_aI4_k];
					t_aI8_th -= m_rI8_tjk[t_aI4_j][t_aI4_k] * (1 - m_rI4_yk[t_aI4_k]);
				}
					
			}
		
			//如果此时解已合法，则结束
			if(isValidSolution(f_aTC_s)){
				//System.out.println("valid solution - 3");
				return;
			}
			
			if (t_aI4_c > 100) {// 20次还没修正，就重新创建一个新解
				t_aI4_c = 0;
				//System.out.println("new solution");
				createNewSolution(f_aTC_s);
			}
			//System.out.println("t_aI4_c = "+t_aI4_c);
		}
	}

	/**
	 * 重新计算
	 * 
	 * @param f_aTC_s
	 * @param f_aI4_k
	 *            设备编号
	 */
	private void reCal(ASolution f_aTC_s) {
		int[] t_rI4_x = f_aTC_s.getM_rI4_x();
		double t_aI8_ta = 0;// 自动检测时间
		double t_aI8_th = 0;// 手动检测时间
		int[] t_rI4_bag = new int[m_aI4_n];// 各个包裹分配到的设备数
		double[] t_rI8_vol = new double[m_aI4_k];// 分配给各个设备的当前容量

		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
				int t_aI4_pos = t_aI4_j * m_aI4_k + t_aI4_k;
				// 自动检测时间
				t_aI8_ta += m_rI8_tjk[t_aI4_j][t_aI4_k] * t_rI4_x[t_aI4_pos] * m_rI4_yk[t_aI4_k];
				// 手动检测时间
				t_aI8_th += m_rI8_tjk[t_aI4_j][t_aI4_k] * t_rI4_x[t_aI4_pos] * (1 - m_rI4_yk[t_aI4_k]);
				// 各个包裹分配到的设备数
				t_rI4_bag[t_aI4_j] += t_rI4_x[t_aI4_pos];
				// 各个设备的当前容纳量
				t_rI8_vol[t_aI4_k] += (m_rI8_sj[t_aI4_j] * t_rI4_x[t_aI4_pos] * m_rI4_yk[t_aI4_k]);
			}
		}
		f_aTC_s.setM_aI8_ta(t_aI8_ta);
		f_aTC_s.setM_aI8_th(t_aI8_th);
		f_aTC_s.setM_rI4_bag(t_rI4_bag);
		f_aTC_s.setM_rI8_vol(t_rI8_vol);
	}

	/**
	 * 计算检测时间
	 */
	private void calDetectTime() {
		m_rI8_tjk = new double[m_aI4_n][m_aI4_k];
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
				if (m_rI4_yk[t_aI4_k] == 1) {// 自动设备
					m_rI8_tjk[t_aI4_j][t_aI4_k] = m_rI8_lj[t_aI4_j] / m_rI8_vak[t_aI4_k];
				} else {// 手动设备
					m_rI8_tjk[t_aI4_j][t_aI4_k] = m_rI8_sj[t_aI4_j] / m_rI8_vhk[t_aI4_k];
				}
			}
		}
	}

	/**
	 * 参数初始化
	 */
	protected abstract void init();

	/**
	 * 种群初始化
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
	 * 【构造新解】
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
	 * 每隔50次评价保存适应度
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
	 * 【适应度计算 & 同时约束条件的计算】 1.检测时间进行计算 2.各个设备分配的包裹数 3.各个设备的当前容纳量
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
	 * 种群进化
	 */
	protected abstract void evolution();

	/**
	 * 问题求解
	 */
	protected abstract ASolution solve(int f_aI4_p);

	/**
	 * 最优解的结果获取
	 * 
	 * @param f_aI4_p
	 * @param f_aTC_solution
	 *            最优解
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
	 * 种群打印函数（用于调试）
	 */
	protected void printPopulation(ASolution[] f_aTC_solution) {
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
			ASolution t_aTC_solution = f_aTC_solution[t_aI4_i];
			printSolution(t_aTC_solution);
		}
	}

	/**
	 * 打印函数（用于调试）
	 */
	protected void printPopulation(List<ASolution> f_aTC_solution) {
		for (int t_aI4_i = 0; t_aI4_i < f_aTC_solution.size(); t_aI4_i++) {
			ASolution t_aTC_solution = f_aTC_solution.get(t_aI4_i);
			printSolution(t_aTC_solution);
		}
	}

	/**
	 * 单个解的打印函数（用于调试）
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
		System.out.println("==========约束==========");
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
