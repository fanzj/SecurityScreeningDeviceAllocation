package com.fzj.solution;

/**
 * @author Fan Zhengjie
 * @date 2017��1��28�� ����3:06:39
 * @version 1.0
 * @description �����
 */
public abstract class ASolution {

	protected int m_aI4_d;// ά��
	protected double m_aI8_fitness;// ��Ӧ��ֵ
	protected int[] m_rI4_x;// λ����������ʾ�������
	protected int m_cur_iter;// ��ǰ��������
	protected int m_cur_nfe;// ��ǰ��Ӧ�����۴���
	
	//Լ���ж�
	protected double m_aI8_ta;// ��ǰ���䷽�����Զ����ʱ��
	protected double m_aI8_th;// ��ǰ���䷽�����ֶ����ʱ��
	protected double m_rI8_vol[];//�����豸�ĵ�ǰ������
	protected int[] m_rI4_bag;//ÿ���������䵽���豸��

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

	public double getM_aI8_ta() {
		return m_aI8_ta;
	}

	public void setM_aI8_ta(double m_aI8_ta) {
		this.m_aI8_ta = m_aI8_ta;
	}

	public double getM_aI8_th() {
		return m_aI8_th;
	}

	public void setM_aI8_th(double m_aI8_th) {
		this.m_aI8_th = m_aI8_th;
	}


	public double[] getM_rI8_vol() {
		return m_rI8_vol;
	}

	public void setM_rI8_vol(double[] m_rI8_vol) {
		this.m_rI8_vol = m_rI8_vol;
	}

	public int[] getM_rI4_bag() {
		return m_rI4_bag;
	}

	public void setM_rI4_bag(int[] m_rI4_bag) {
		this.m_rI4_bag = m_rI4_bag;
	}

	
	
}
