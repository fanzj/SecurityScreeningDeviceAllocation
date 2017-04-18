package com.fzj.compare;

import java.util.Comparator;

import com.fzj.solution.ASolution;

/**
 * 
 * @author dell
 * @date 2017年2月18日 上午10:07:01
 * @title 适应度比较器
 * @description
 */
public class FitnessComparator implements Comparator<ASolution> {

	@Override
	public int compare(ASolution o1, ASolution o2) {
		if(Double.compare(o1.getM_aI8_fitness(), o2.getM_aI8_fitness())>0)
			return -1;
		else
			return 1;
	}

}
