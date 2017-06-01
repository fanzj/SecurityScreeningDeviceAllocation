package com.fzj.solution;
/** 
 * @author Fan Zhengjie 
 * @date 2017年1月28日 下午2:52:29 
 * @version 1.0 
 * @description PSO算法和DNSPSO算法的一个粒子，相当于一个解
 */
public class GASolution extends ASolution implements Cloneable{
	
	
	
	public GASolution() {
		super();
	}
	
	public GASolution(int f_aI4_d){
		super(f_aI4_d);
	}
	
	


	@Override
	public GASolution clone() {
		GASolution t_aTC_particle = null;
		try{
			t_aTC_particle = (GASolution) super.clone();
			t_aTC_particle.m_rI4_x = this.m_rI4_x.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return t_aTC_particle;
	}
}
