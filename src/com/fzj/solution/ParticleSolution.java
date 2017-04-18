package com.fzj.solution;
/** 
 * @author Fan Zhengjie 
 * @date 2017年1月28日 下午2:52:29 
 * @version 1.0 
 * @description PSO算法和DNSPSO算法的一个粒子，相当于一个解
 */
public class ParticleSolution extends ASolution implements Cloneable{
	
	private int[] m_rI4_v;//速度向量
	
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
