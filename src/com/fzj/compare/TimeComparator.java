package com.fzj.compare;

import java.util.Comparator;

public class TimeComparator implements Comparator<Integer>{
	
	private double[] m_rI8_t;
	
	public TimeComparator(double[] f_rI8_t) {
		this.m_rI8_t = f_rI8_t;
	}

	@Override
	public int compare(Integer o1, Integer o2) {
		if(Double.compare(m_rI8_t[o1], m_rI8_t[o2])<0)
			return -1;
		else
			return 1;
	}

	

}
