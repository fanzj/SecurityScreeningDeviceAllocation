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
	protected int m_aI4_q;// 安检人员数
	protected double m_aI4_max_t; // 时间上限
	protected double m_rI8_wi[];// 危险物品的权重
	protected double m_rI8_t0j[];// 最早达到时间
	protected double m_rI8_lj[];// 包裹j的最大长度
	protected double m_rI8_vj[];// 包裹j的体积
	protected double m_rI8_sk[];// 自动设备k的检测速度
	protected double m_rI8_aij[][];// 每个包裹携带危险品的概率
	protected double m_rI8_bik[][];// i被k检测的概率
	protected double m_rI8_yiq[][];// 危险品i被q检测到的概率

	// 中间变量
	protected double m_rI8_tjk[][];// 检测时间，包裹j被设备k检测所需时间
	protected double m_rI8_tj[];// 检测时间，j被人检测的时间

	protected Random m_aTC_random;// 随机数生成器

	protected List<Fitness> m_aTC_listOfFitness;// 用于结果的保存显示
	protected ASolution[] m_aTC_population;// 种群

	protected long m_aI8_max_time;// 单次最大运行时间

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
		if (Double.compare(f_aTC_s.getM_aI8_maxtime(), m_aI4_max_t) <= 0)
			return true;
		return false;
	}

	/**
	 * 【解的修正】 对于不合法的解做相应的修正操作使其合法
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
	 *            行李编号
	 * @return
	 */
	private List<Integer> getPre(int f_aI4_j, int[] f_rI4_x) {
		List<Integer> t_aTC_list = new ArrayList<>();// 存放包裹编号
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
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {// 对每个行李
			int t_aI4_xj = t_rI4_x[t_aI4_j * 2];// 决策变量
			// t_rI8_t1[t_aI4_j] += m_rI8_t0j[t_aI4_j];
			// 构造Pre(j)集合
			List<Integer> t_aTC_pre = getPre(t_aI4_j, t_rI4_x);
			for (int t_aI4_i = 0; t_aI4_i < t_aTC_pre.size(); t_aI4_i++) {
				t_rI8_t1[t_aI4_j] += m_rI8_tjk[t_aTC_pre.get(t_aI4_i)][t_aI4_xj - 1];
			}
		}
		return t_rI8_t1;
	}

	private List<Integer> getBq(int f_aI4_yj, int[] f_rI4_x, double[] f_rI8_t1) {
		List<Integer> t_aTC_list = new ArrayList<>();// 存放包裹编号
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_d; t_aI4_i++) {
			if (t_aI4_i % 2 != 0) {
				int t_aI4_yj = f_rI4_x[t_aI4_i];// 决策变量
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
			// 构造Bq集合
			List<Integer> t_aTC_Bq = getBq(t_aI4_q, t_rI4_x, t_rI8_t1j);
			// System.out.println("排序前："+t_aTC_Bq);
			// Bq排序
			Collections.sort(t_aTC_Bq, new TimeComparator(t_rI8_t1j));
			/*
			 * System.out.println("排序后："+t_aTC_Bq); for(int
			 * i=0;i<t_aTC_Bq.size();i++){
			 * System.out.print(t_rI8_t1j[t_aTC_Bq.get(i)]);
			 * if(i<t_aTC_Bq.size()-1) System.out.print(" "); else
			 * System.out.println(); }
			 */
			for (int t_aI4_i = 0; t_aI4_i < t_aTC_Bq.size(); t_aI4_i++) {
				int t_aI4_num = t_aTC_Bq.get(t_aI4_i);// 行李编号
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
	 * f_rI8_t1){ List<Integer> t_aTC_list = new ArrayList<>();//存放包裹编号 for(int
	 * t_aI4_i=0;t_aI4_i<m_aI4_d;t_aI4_i++){ if(t_aI4_i%2!=0){ int t_aI4_yj =
	 * f_rI4_x[t_aI4_i];// 决策变量 if(t_aI4_yj !=0 &&
	 * f_rI4_x[f_aI4_j*2+1]==f_rI4_x[t_aI4_i] &&
	 * Double.compare(f_rI8_t1[(t_aI4_i-1)/2], f_rI8_t1[f_aI4_j])<0)
	 * t_aTC_list.add((t_aI4_i-1)/2); } } return t_aTC_list; }
	 */

	/*
	 * private double calT2(ASolution f_aTC_s){ double[] t_rI8_t1j =
	 * calT1(f_aTC_s); int[] t_rI4_x = f_aTC_s.getM_rI4_x(); double[] t_rI8_t2 =
	 * new double[m_aI4_n]; double t_aI8_t = 0; for(int
	 * t_aI4_j=0;t_aI4_j<m_aI4_n;t_aI4_j++){//对每个行李 int t_aI4_yj =
	 * t_rI4_x[t_aI4_j * 2+1];// 决策变量 t_rI8_t2[t_aI4_j] += t_rI8_t1j[t_aI4_j];
	 * //构造Pre(j)集合 List<Integer> t_aTC_pre = getPre2(t_aI4_j,
	 * t_rI4_x,t_rI8_t1j); for(int
	 * t_aI4_i=0;t_aI4_i<t_aTC_pre.size();t_aI4_i++){ t_rI8_t2[t_aI4_j] +=
	 * m_rI8_tj[t_aTC_pre.get(t_aI4_i)]; }
	 * 
	 * t_aI8_t = Math.max(t_aI8_t, t_rI8_t2[t_aI4_j]); }
	 * f_aTC_s.setM_aI8_maxtime(t_aI8_t); return t_aI8_t; }
	 */

	/**
	 * 计算检测时间
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
	 * 参数初始化
	 */
	protected abstract void init();

	/**
	 * 种群初始化 满足时间约束
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
			// modify(m_aTC_population[t_aI4_k]);//修正，以满足时间约束
			// System.out.println("k = "+t_aI4_k);
			m_aTC_population[t_aI4_k] = t_aTC_solution;
			evaluate(m_aTC_population[t_aI4_k]);
			t_aI4_k++;
		}
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
	 * 【适应度计算】 进行适应度计算的解必定已经满足时间约束
	 */
	protected void evaluate(ASolution f_aTC_solution) {
		calT2(f_aTC_solution);//new 
		int[] t_rI4_x = f_aTC_solution.getM_rI4_x();
		double t_aI8_fitness = 0.0;
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_m; t_aI4_i++) {
			for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
				int t_aI4_xj = t_rI4_x[t_aI4_j * 2];// 决策变量[1,K]
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
	 * 【种群进化】
	 */
	protected abstract void evolution();

	/**
	 * 【问题求解】
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
