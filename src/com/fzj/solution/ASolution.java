package com.fzj.solution;

/**
 * @author Fan Zhengjie
 * @date 2017年1月28日 下午3:06:39
 * @version 1.0
 * @description 抽象解
 */
public class ASolution implements Cloneable {

	protected int m_aI4_d;// 维度
	protected double m_aI8_fitness;// 适应度值
	protected int[] m_rI4_x;// 位移向量，表示分配策略
	protected int m_cur_iter;// 当前进化代数
	protected int m_cur_nfe;// 当前适应度评价次数

	protected double m_aI8_td;// 当前分配方案的安检设备检测时间
	protected double m_aI8_tp;// 当前分配方案的安检人员检测时间
	protected int[] m_rI4_bag;// 每个包裹分配到的设备数
	protected int[] m_rI4_fac;// 每个包裹分配到的设备和行李数

	// pso & bpso
	private double[] m_rI8_v;// 速度向量
	
	//ibpso
	private int[] m_rI4_v2;
	
	//ga
	private double m_aI8_pi;//累积进化概率

	public ASolution() {
	}

	public ASolution(int f_aI4_d) {
		this.m_aI4_d = f_aI4_d;
	}

	public int getM_aI4_d() {
		return m_aI4_d;
	}

	public void setM_aI4_d(int m_aI4_d) {
		this.m_aI4_d = m_aI4_d;
	}

	public double getM_aI8_fitness() {
		return m_aI8_fitness;
	}

	public void setM_aI8_fitness(double m_aI8_fitness) {
		this.m_aI8_fitness = m_aI8_fitness;
	}

	public int[] getM_rI4_x() {
		return m_rI4_x;
	}

	public void setM_rI4_x(int[] m_rI4_x) {
		this.m_rI4_x = m_rI4_x;
	}

	public int getM_cur_iter() {
		return m_cur_iter;
	}

	public void setM_cur_iter(int m_cur_iter) {
		this.m_cur_iter = m_cur_iter;
	}

	public int getM_cur_nfe() {
		return m_cur_nfe;
	}

	public void setM_cur_nfe(int m_cur_nfe) {
		this.m_cur_nfe = m_cur_nfe;
	}

	public double getM_aI8_td() {
		return m_aI8_td;
	}

	public void setM_aI8_td(double m_aI8_td) {
		this.m_aI8_td = m_aI8_td;
	}

	public double getM_aI8_tp() {
		return m_aI8_tp;
	}

	public void setM_aI8_tp(double m_aI8_tp) {
		this.m_aI8_tp = m_aI8_tp;
	}

	public int[] getM_rI4_bag() {
		return m_rI4_bag;
	}

	public void setM_rI4_bag(int[] m_rI4_bag) {
		this.m_rI4_bag = m_rI4_bag;
	}

	public int[] getM_rI4_fac() {
		return m_rI4_fac;
	}

	public void setM_rI4_fac(int[] m_rI4_fac) {
		this.m_rI4_fac = m_rI4_fac;
	}

	public void setM_rI8_v(double[] m_rI8_v) {
		this.m_rI8_v = m_rI8_v;
	}

	public double[] getM_rI8_v() {
		return m_rI8_v;
	}
	
	public double getM_aI8_pi() {
		return m_aI8_pi;
	}

	public void setM_aI8_pi(double m_aI8_pi) {
		this.m_aI8_pi = m_aI8_pi;
	}
	
	

	public int[] getM_rI4_v2() {
		return m_rI4_v2;
	}

	public void setM_rI4_v2(int[] m_rI4_v2) {
		this.m_rI4_v2 = m_rI4_v2;
	}

	@Override
	public ASolution clone() {
		ASolution t_aTC_s = null;
		try {
			t_aTC_s = (ASolution) super.clone();
			t_aTC_s.m_rI4_x = this.m_rI4_x.clone();
			t_aTC_s.m_rI8_v = this.m_rI8_v.clone();
			t_aTC_s.m_rI4_v2 = this.m_rI4_v2.clone();
			t_aTC_s.m_rI4_bag = this.m_rI4_bag.clone();
			t_aTC_s.m_rI4_fac = this.m_rI4_fac.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return t_aTC_s;
	}

}
