package com.fzj.model;

import java.util.Map;
import com.fzj.config.NameSpace;
import com.fzj.utils.FileUtils;
import com.fzj.utils.MathUtils;

/**
 * @author Fan Zhengjie
 * @date 2017年1月28日 下午4:26:32
 * @version 1.0
 * @description 安检调度模型
 */
public class SSModel {

	/**
	 * 危险物品种类数
	 */
	private int m_aI4_m;

	/**
	 * 包裹数
	 */
	private int m_aI4_n;

	/**
	 * 设备
	 */
	private int m_aI4_k;

	/**
	 * 每个包裹最多可以分配的设备数
	 */
	private int m_aI4_p;

	/**
	 * 问题维度
	 */
	private int m_aI4_d;

	/**
	 * 危险物品的权重
	 */
	private double m_rI8_wi[];

	/**
	 * 自动设备or手动设备
	 */
	private int m_rI4_yk[];

	/**
	 * 包裹的长度
	 */
	private double m_rI8_lj[];

	/**
	 * 包裹的体积
	 */
	private double m_rI8_sj[];

	/**
	 * 自动设备的检测速度
	 */
	private double m_rI8_vak[];

	/**
	 * 手动检测设备的速度
	 */
	private double m_rI8_vhk[];

	/**
	 * 自动检测时间上限
	 */
	private double m_aI8_ta;

	/**
	 * 手动检测时间上限
	 */
	private double m_aI8_th;

	/**
	 * 设备的容积
	 */
	private double m_rI8_ck[];

	/**
	 * 携带危险品的概率
	 */
	private double m_aI8_a;

	/**
	 * 携带危险品i的概率
	 */
	private double m_rI8_bi[];

	/**
	 * i被k检测的概率
	 */
	private double m_rI8_yik[][];

	public SSModel(String f_str_path) {
		FileUtils.s_aI1_operation = true;// 读取路径的选择
		Map<String, Integer> t_aTC_map = FileUtils.readBasicInfo(f_str_path, NameSpace.s_str_basic_info);
		this.m_aI4_m = t_aTC_map.get(NameSpace.s_str_m);
		this.m_aI4_n = t_aTC_map.get(NameSpace.s_str_n);
		this.m_aI4_k = t_aTC_map.get(NameSpace.s_str_k);
		this.m_aI4_p = t_aTC_map.get(NameSpace.s_str_p);
		this.m_aI4_d = m_aI4_n * m_aI4_p;
		this.m_rI8_wi = FileUtils.read1D(f_str_path, NameSpace.s_str_wi, m_aI4_m);
		this.m_rI4_yk = FileUtils.read1I(f_str_path, NameSpace.s_str_yk, m_aI4_k);
		this.m_rI8_lj = FileUtils.read1D(f_str_path, NameSpace.s_str_lj, m_aI4_n);
		this.m_rI8_sj = FileUtils.read1D(f_str_path, NameSpace.s_str_sj, m_aI4_n);
		this.m_rI8_vak = FileUtils.read1D(f_str_path, NameSpace.s_str_vak, m_aI4_k);
		this.m_rI8_vhk = FileUtils.read1D(f_str_path, NameSpace.s_str_vhk, m_aI4_k);
		this.m_aI8_ta = 15 * m_aI4_n;
		this.m_aI8_th = 30 * m_aI4_n;
		this.m_rI8_ck = FileUtils.read1D(f_str_path, NameSpace.s_str_ck, m_aI4_k);
		this.m_aI8_a = 0.1;
		this.m_rI8_bi = FileUtils.read1D(f_str_path, NameSpace.s_str_bi, m_aI4_m);
		this.m_rI8_yik = FileUtils.read2D(f_str_path, NameSpace.s_str_yik, m_aI4_m, m_aI4_k);
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

	public int[] getM_rI4_yk() {
		return m_rI4_yk;
	}

	public void setM_rI4_yk(int[] m_rI4_yk) {
		this.m_rI4_yk = m_rI4_yk;
	}

	public double[] getM_rI8_lj() {
		return m_rI8_lj;
	}

	public void setM_rI8_li(double[] m_rI8_lj) {
		this.m_rI8_lj = m_rI8_lj;
	}

	public double[] getM_rI8_sj() {
		return m_rI8_sj;
	}

	public void setM_rI8_sj(double[] m_rI8_sj) {
		this.m_rI8_sj = m_rI8_sj;
	}

	public double[] getM_rI8_vak() {
		return m_rI8_vak;
	}

	public void setM_rI8_vak(double[] m_rI8_vak) {
		this.m_rI8_vak = m_rI8_vak;
	}

	public double[] getM_rI8_vhk() {
		return m_rI8_vhk;
	}

	public void setM_rI8_vhk(double[] m_rI8_vhk) {
		this.m_rI8_vhk = m_rI8_vhk;
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

	public double[] getM_rI8_ck() {
		return m_rI8_ck;
	}

	public void setM_rI8_ck(double[] m_rI8_ck) {
		this.m_rI8_ck = m_rI8_ck;
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

	public double[][] getM_rI8_yik() {
		return m_rI8_yik;
	}

	public void setM_rI8_yik(double[][] m_rI8_yik) {
		this.m_rI8_yik = m_rI8_yik;
	}

	public int getM_aI4_p() {
		return m_aI4_p;
	}

	public void setM_aI4_p(int m_aI4_p) {
		this.m_aI4_p = m_aI4_p;
	}

	public void setM_rI8_lj(double[] m_rI8_lj) {
		this.m_rI8_lj = m_rI8_lj;
	}

	/**
	 * 测试数据是否读取成功
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SSModel t_aTC_ssm = new SSModel(NameSpace.s_str_data_02);
		System.out.println("1.基本信息：");
		System.out.println("m = " + t_aTC_ssm.getM_aI4_n());
		System.out.println("n = " + t_aTC_ssm.getM_aI4_m());
		System.out.println("k = " + t_aTC_ssm.getM_aI4_k());

		System.out.println("2.wi:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_wi());

		System.out.println("3.yk:");
		MathUtils.print1D(t_aTC_ssm.getM_rI4_yk());

		System.out.println("4.vak:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_vak());

		System.out.println("5.vhk:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_vhk());

		System.out.println("6.ck:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_ck());

		System.out.println("7.Lj:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_lj());

		System.out.println("8.Sj:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_sj());

		System.out.println("9.Ta:");
		System.out.println("Ta = " + 10 * t_aTC_ssm.m_aI4_n);

		System.out.println("10.Th:");
		System.out.println("Th = " + 20 * t_aTC_ssm.m_aI4_n);

		System.out.println("11.a:");
		System.out.println("a = " + 0.1);

		System.out.println("12.Bi:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_bi());

		System.out.println("13.Yik:");
		MathUtils.print2D(t_aTC_ssm.getM_rI8_yik());

	}

}
