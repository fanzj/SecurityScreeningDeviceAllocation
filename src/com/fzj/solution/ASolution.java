package com.fzj.solution;

/**
 * @author Fan Zhengjie
 * @date 2017��1��28�� ����3:06:39
 * @version 1.0
 * @description �����
 */
public class ASolution implements Cloneable {

	protected int m_aI4_d;// ά��
	protected double m_aI8_fitness;// ��Ӧ��ֵ
	protected int[] m_rI4_x;// λ����������ʾ�������
	protected int m_cur_iter;// ��ǰ��������
	protected int m_cur_nfe;// ��ǰ��Ӧ�����۴���

	protected double m_aI8_td;// ��ǰ���䷽���İ����豸���ʱ��
	protected double m_aI8_tp;// ��ǰ���䷽���İ�����Ա���ʱ��
	protected int[] m_rI4_bag;// ÿ���������䵽���豸��
	protected int[] m_rI4_fac;// ÿ���������䵽���豸��������

	// pso & bpso
	private double[] m_rI8_v;// �ٶ�����
	
	//ibpso
	private int[] m_rI4_v2;
	
	//ga
	private double m_aI8_pi;//�ۻ���������

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
