package com.fzj.solution;
/** 
 * @author Fan Zhengjie 
 * @date 2017��1��28�� ����2:52:29 
 * @version 1.0 
 * @description PSO�㷨��DNSPSO�㷨��һ�����ӣ��൱��һ����
 */
public class ParticleSolution extends ASolution implements Cloneable{
	
	private int[] m_rI4_v;//�ٶ�����
	
	public ParticleSolution() {
		super();
	}
	
	public ParticleSolution(int f_aI4_d){
		super(f_aI4_d);
	}
	

	public int[] getM_rI4_v() {
		return m_rI4_v;
	}

	public void setM_rI4_v(int[] m_rI4_v) {
		this.m_rI4_v = m_rI4_v;
	}

	@Override
	public ParticleSolution clone() {
		ParticleSolution t_aTC_particle = null;
		try{
			t_aTC_particle = (ParticleSolution) super.clone();
			t_aTC_particle.m_rI4_v = this.m_rI4_v.clone();
			t_aTC_particle.m_rI4_x = this.m_rI4_x.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return t_aTC_particle;
	}
}
