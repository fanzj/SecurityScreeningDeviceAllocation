package com.fzj.model;

import java.util.Map;
import com.fzj.config.NameSpace;
import com.fzj.utils.FileUtils;
import com.fzj.utils.MathUtils;

/**
 * @author Fan Zhengjie
 * @date 2017��1��28�� ����4:26:32
 * @version 1.0
 * @description �������ģ��
 */
public class SSModel {

	/**
	 * Σ����Ʒ������
	 */
	private int m_aI4_m;

	/**
	 * ������
	 */
	private int m_aI4_n;

	/**
	 * �豸
	 */
	private int m_aI4_k;

	/**
	 * ������Ա��
	 */
	private int m_aI4_q;

	/**
	 * ����ά��
	 */
	private int m_aI4_d;

	/**
	 * Σ����Ʒ��Ȩ��
	 */
	private double m_rI8_wi[];

	/**
	 * �����ĳ���
	 */
	private double m_rI8_lj[];

	/**
	 * ���������
	 */
	private double m_rI8_sj[];

	/**
	 * �����豸�ļ���ٶ�
	 */
	private double m_rI8_vk[];

	/**
	 * Я��Σ��Ʒ�ĸ���
	 */
	private double m_aI8_a;

	/**
	 * Я��Σ��Ʒi�ĸ���
	 */
	private double m_rI8_bi[];

	/**
	 * i��k���ĸ���
	 */
	private double m_rI8_ydik[][];

	/**
	 * i��������Ա���ĸ���
	 */
	private double m_rI8_ypi[];

	/**
	 * ÿ���������ɷ�����豸��
	 */
	private int m_aI4_b;

	/**
	 * ʱ������ϵ��
	 */
	private double m_aI8_landa;

	/**
	 * ��ʱ������
	 */
	private double m_aI8_u;

	public SSModel(String f_str_path) {
		FileUtils.s_aI1_operation = true;// ��ȡ·����ѡ��
		Map<String, Integer> t_aTC_map = FileUtils.readBasicInfo(f_str_path, NameSpace.s_str_basic_info);
		this.m_aI4_m = t_aTC_map.get(NameSpace.s_str_m);
		this.m_aI4_n = t_aTC_map.get(NameSpace.s_str_n);
		this.m_aI4_k = t_aTC_map.get(NameSpace.s_str_k);
		this.m_aI4_q = t_aTC_map.get(NameSpace.s_str_q);
		this.m_aI4_d = m_aI4_n * (m_aI4_k + 1);
		this.m_aI4_b = 3;
		this.m_aI8_landa = 15;
		this.m_aI8_u = m_aI8_landa * m_aI4_n;
		this.m_rI8_wi = FileUtils.read1D(f_str_path, NameSpace.s_str_wi, m_aI4_m);
		this.m_rI8_lj = FileUtils.read1D(f_str_path, NameSpace.s_str_lj, m_aI4_n);
		this.m_rI8_sj = FileUtils.read1D(f_str_path, NameSpace.s_str_sj, m_aI4_n);
		this.m_rI8_vk = FileUtils.read1D(f_str_path, NameSpace.s_str_vk, m_aI4_k);
		this.m_aI8_a = 0.1;
		this.m_rI8_bi = FileUtils.read1D(f_str_path, NameSpace.s_str_bi, m_aI4_m);
		this.m_rI8_ydik = FileUtils.read2D(f_str_path, NameSpace.s_str_ydik, m_aI4_m, m_aI4_k);
		this.m_rI8_ypi = FileUtils.read1D(f_str_path, NameSpace.s_str_ypi, m_aI4_m);
	}

	public int getM_aI4_m() {
		return m_aI4_m;
	}

	public void setM_aI4_m(int m_aI4_m) {
		this.m_aI4_m = m_aI4_m;
	}

	public int getM_aI4_n() {
		return m_aI4_n;
	}

	public void setM_aI4_n(int m_aI4_n) {
		this.m_aI4_n = m_aI4_n;
	}

	public int getM_aI4_k() {
		return m_aI4_k;
	}

	public void setM_aI4_k(int m_aI4_k) {
		this.m_aI4_k = m_aI4_k;
	}

	public int getM_aI4_q() {
		return m_aI4_q;
	}

	public void setM_aI4_q(int m_aI4_q) {
		this.m_aI4_q = m_aI4_q;
	}

	public int getM_aI4_d() {
		return m_aI4_d;
	}

	public void setM_aI4_d(int m_aI4_d) {
		this.m_aI4_d = m_aI4_d;
	}

	public double[] getM_rI8_wi() {
		return m_rI8_wi;
	}

	public void setM_rI8_wi(double[] m_rI8_wi) {
		this.m_rI8_wi = m_rI8_wi;
	}

	public double[] getM_rI8_lj() {
		return m_rI8_lj;
	}

	public void setM_rI8_lj(double[] m_rI8_lj) {
		this.m_rI8_lj = m_rI8_lj;
	}

	public double[] getM_rI8_sj() {
		return m_rI8_sj;
	}

	public void setM_rI8_sj(double[] m_rI8_sj) {
		this.m_rI8_sj = m_rI8_sj;
	}

	public double[] getM_rI8_vk() {
		return m_rI8_vk;
	}

	public void setM_rI8_vk(double[] m_rI8_vk) {
		this.m_rI8_vk = m_rI8_vk;
	}

	public double getM_aI8_a() {
		return m_aI8_a;
	}

	public void setM_aI8_a(double m_aI8_a) {
		this.m_aI8_a = m_aI8_a;
	}

	public double[] getM_rI8_bi() {
		return m_rI8_bi;
	}

	public void setM_rI8_bi(double[] m_rI8_bi) {
		this.m_rI8_bi = m_rI8_bi;
	}

	public double[][] getM_rI8_ydik() {
		return m_rI8_ydik;
	}

	public void setM_rI8_ydik(double[][] m_rI8_ydik) {
		this.m_rI8_ydik = m_rI8_ydik;
	}

	public double[] getM_rI8_ypi() {
		return m_rI8_ypi;
	}

	public void setM_rI8_ypi(double[] m_rI8_ypi) {
		this.m_rI8_ypi = m_rI8_ypi;
	}

	public int getM_aI4_b() {
		return m_aI4_b;
	}

	public void setM_aI4_b(int m_aI4_b) {
		this.m_aI4_b = m_aI4_b;
	}

	public double getM_aI8_landa() {
		return m_aI8_landa;
	}

	public void setM_aI8_landa(double m_aI8_landa) {
		this.m_aI8_landa = m_aI8_landa;
	}

	public double getM_aI8_u() {
		return m_aI8_u;
	}

	public void setM_aI8_u(double m_aI8_u) {
		this.m_aI8_u = m_aI8_u;
	}

	/**
	 * ���������Ƿ��ȡ�ɹ�
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SSModel t_aTC_ssm = new SSModel(NameSpace.s_str_data_01);
		System.out.println("1.info��");
		System.out.println("m = " + t_aTC_ssm.getM_aI4_m());
		System.out.println("n = " + t_aTC_ssm.getM_aI4_n());
		System.out.println("k = " + t_aTC_ssm.getM_aI4_k());
		System.out.println("q = "+t_aTC_ssm.m_aI4_q);

		System.out.println("2.wi:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_wi());

		System.out.println("3.vk:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_vk());

		System.out.println("4.Lj:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_lj());

		System.out.println("5.Sj:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_sj());

		System.out.println("6.Bi:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_bi());

		System.out.println("7.Ydik:");
		MathUtils.print2D(t_aTC_ssm.getM_rI8_ydik());
		
		System.out.println("8.Ypi:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_ypi());
		
		System.out.println("9.U:");
		System.out.println(t_aTC_ssm.getM_aI8_u());

	}

}
