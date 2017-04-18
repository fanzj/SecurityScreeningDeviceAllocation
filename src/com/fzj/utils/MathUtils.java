package com.fzj.utils;

import java.util.Random;

/** 
 * @author Fan Zhengjie 
 * @date 2017��1��5�� ����4:57:31 
 * @version 1.0 
 * @description
 */
public class MathUtils {
	
	private static Random s_aTC_random = new Random();
	
	/**
	 * ��������[f_aI4_a,f_aI4_b]�������
	 * @param f_aI8_a
	 * @param f_aI8_b
	 * @return
	 */
	public static double getDoubleAToB(double f_aI8_a,double f_aI8_b){
		return f_aI8_a + s_aTC_random.nextDouble()*(f_aI8_b-f_aI8_a);
	}
	
	/**
	 * ��������[f_aI4_a,f_aI4_b]�������
	 * @param f_aI4_a
	 * @param f_aI4_b
	 * @return
	 */
	public static int getIntAtoB(int f_aI4_a,int f_aI4_b){
		return f_aI4_a + s_aTC_random.nextInt(f_aI4_b-f_aI4_a+1);
	}
    
    /**
     * ����8λС��
     * @param d
     * @return
     */
    public static String formatD8(double f_aI8_d) {
        return String.format("%.8f", f_aI8_d);
    }
    
    /**
     * ����f_aI4_digitλС��
     * @param f_aI8_d
     * @param f_aI4_digit
     * @return
     */
    public static String formatD(double f_aI8_d,int f_aI4_digit){
    	return String.format("%."+f_aI4_digit+"f", f_aI8_d);
    }
    
	/**
	 * һά���������ӡ���������ڲ��ԣ�
	 * @param f_rI8_d
	 */
	public static void print1D(double[] f_rI8_d){
		for(int t_aI4_i=0;t_aI4_i<f_rI8_d.length;t_aI4_i++){
			//System.out.print(MathUtils.formatD8(f_rI8_d[t_aI4_i]));
			System.out.print(f_rI8_d[t_aI4_i]);
			if(t_aI4_i<f_rI8_d.length-1)
				System.out.print(" ");
			else
				System.out.println();
		}
	}
	
	/**
	 * һά���������ӡ���������ڲ��ԣ�
	 * @param f_rI8_d
	 */
	public static void print1D(int[] f_rI4_d){
		for(int t_aI4_i=0;t_aI4_i<f_rI4_d.length;t_aI4_i++){
			System.out.print(f_rI4_d[t_aI4_i]);
			if(t_aI4_i<f_rI4_d.length-1)
				System.out.print(" ");
			else
				System.out.println();
		}
	}
	
	/**
	 * ��ά���������ӡ���������ڲ��ԣ�
	 * @param f_rI8_d
	 */
	public static void print2D(double[][] f_rI8_d){
		for(int t_aI4_i=0;t_aI4_i<f_rI8_d.length;t_aI4_i++){
			for(int t_aI4_j=0;t_aI4_j<f_rI8_d[t_aI4_i].length;t_aI4_j++){
				//System.out.print(MathUtils.formatD8(f_rI8_d[t_aI4_i][t_aI4_j]));
				System.out.print(f_rI8_d[t_aI4_i][t_aI4_j]);
				if(t_aI4_j<f_rI8_d[t_aI4_i].length-1)
					System.out.print(" ");
				else 
					System.out.println();
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getIntAtoB(2, 7));
	}
}
