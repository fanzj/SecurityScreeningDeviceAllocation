package com.fzj.solution;

public class FourTupple implements Cloneable {

	private double[] m_rI8_x;
	private double[] m_rI8_v;
	private double m_aI8_g;
	protected int m_cur_iter;// 当前进化代数
	protected int m_cur_nfe;// 当前适应度评价次数

	public double[] getM_rI8_x() {
		return m_rI8_x;
	}

	public void setM_rI8_x(double[] m_rI8_x) {
		this.m_rI8_x = m_rI8_x;
	}

	public double[] getM_rI8_v() {
		return m_rI8_v;
	}

	public void setM_rI8_v(double[] m_rI8_v) {
		this.m_rI8_v = m_rI8_v;
	}

	public double getM_aI8_g() {
		return m_aI8_g;
	}

	public void setM_aI8_g(double m_aI8_g) {
		this.m_aI8_g = m_aI8_g;
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

	@Override
	public FourTupple clone() {
		FourTupple t_aTC_s = null;
		try {
			t_aTC_s = (FourTupple) super.clone();
			t_aTC_s.m_rI8_x = this.m_rI8_x.clone();
			t_aTC_s.m_rI8_v = this.m_rI8_v.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return t_aTC_s;
	}
}
