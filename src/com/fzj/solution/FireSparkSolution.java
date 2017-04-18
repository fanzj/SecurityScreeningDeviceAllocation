package com.fzj.solution;

/**
 * @author Fan Zhengjie
 * @date 2016��11��11�� ����3:49:58
 * @version 1.0
 * @description �̻������ǣ���ʾһ�����н�
 */
public class FireSparkSolution extends ASolution implements Cloneable{

	private double m_aI8_Pi;// �ۻ�����

	public FireSparkSolution() {}

	public FireSparkSolution(int f_aI4_d) {
		super(f_aI4_d);
	}

	public double getM_aI8_Pi() {
		return m_aI8_Pi;
	}
	
	public void setM_aI8_Pi(double m_aI8_Pi) {
		this.m_aI8_Pi = m_aI8_Pi;
	}
	
	public FireSparkSolution clone() {
		FireSparkSolution t_aTC_fireSpark = null;
		try {
			t_aTC_fireSpark = (FireSparkSolution) super.clone();
			t_aTC_fireSpark.m_rI4_x = this.m_rI4_x.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return t_aTC_fireSpark;
	}

}
