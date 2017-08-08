package com.fzj.model;

import java.util.Map;
import java.util.Properties;

import com.fzj.config.NameSpace;
import com.fzj.utils.FileUtils;
import com.fzj.utils.MProperties;
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
	 * 安检人员
	 */
	private int m_aI4_q;

	/**
	 * 时间上限
	 */
	private double m_aI8_max_t;

	/**
	 * 问题维度
	 */
	private int m_aI4_d;

	/**
	 * 危险物品的权重
	 */
	private double m_rI8_wi[];

	/**
	 * 最早到达时间
	 */
	private double m_rI8_t0j[];

	/**
	 * 包裹的长度
	 */
	private double m_rI8_lj[];

	/**
	 * 包裹的体积
	 */
	private double m_rI8_vj[];

	/**
	 * 设备检测速度
	 */
	private double m_rI8_sk[];

	/**
	 * j携带危险品i的概率
	 */
	private double m_rI8_aij[][];

	/**
	 * i被k检测的概率
	 */
	private double m_rI8_bik[][];

	/**
	 * i被q检测的概率
	 */
	private double m_rI8_yiq[][];
	
	Properties t_aTC_p = MProperties.get(NameSpace.s_str_run_config);

	public SSModel(String f_str_path) {
		FileUtils.s_aI1_operation = true;// 读取路径的选择
		Map<String, String> t_aTC_map = FileUtils.readBasicInfo(f_str_path, NameSpace.s_str_basic_info);
		this.m_aI4_m = Integer.parseInt(t_aTC_map.get(NameSpace.s_str_m));
		this.m_aI4_n = Integer.parseInt(t_aTC_map.get(NameSpace.s_str_n));
		this.m_aI4_k = Integer.parseInt(t_aTC_map.get(NameSpace.s_str_k));
		this.m_aI4_q = Integer.parseInt(t_aTC_map.get(NameSpace.s_str_q));
		this.m_aI8_max_t = Integer.parseInt(t_aTC_p.getProperty(NameSpace.s_str_u_t));
		this.m_aI4_d = m_aI4_n * 2;
		this.m_rI8_wi = FileUtils.read1D(f_str_path, NameSpace.s_str_wi, m_aI4_m);
		this.m_rI8_t0j = FileUtils.read1D(f_str_path, NameSpace.s_str_t0j, m_aI4_n);
		this.m_rI8_lj = FileUtils.read1D(f_str_path, NameSpace.s_str_lj, m_aI4_n);
		this.m_rI8_vj = FileUtils.read1D(f_str_path, NameSpace.s_str_vj, m_aI4_n);
		this.m_rI8_sk = FileUtils.read1D(f_str_path, NameSpace.s_str_sk, m_aI4_k);
		this.m_rI8_aij = FileUtils.read2D(f_str_path, NameSpace.s_str_aij, m_aI4_m, m_aI4_n);
		this.m_rI8_bik = FileUtils.read2D(f_str_path, NameSpace.s_str_bik, m_aI4_m, m_aI4_k);
		this.m_rI8_yiq = FileUtils.read2D(f_str_path, NameSpace.s_str_yiq, m_aI4_m, m_aI4_q+1);
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

	public double getM_aI8_max_t() {
		return m_aI8_max_t;
	}

	public void setM_aI8_max_t(double m_aI8_max_t) {
		this.m_aI8_max_t = m_aI8_max_t;
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

	public double[] getM_rI8_t0j() {
		return m_rI8_t0j;
	}

	public void setM_rI8_t0j(double[] m_rI8_t0j) {
		this.m_rI8_t0j = m_rI8_t0j;
	}

	public double[] getM_rI8_lj() {
		return m_rI8_lj;
	}

	public void setM_rI8_lj(double[] m_rI8_lj) {
		this.m_rI8_lj = m_rI8_lj;
	}

	public double[] getM_rI8_vj() {
		return m_rI8_vj;
	}

	public void setM_rI8_vj(double[] m_rI8_vj) {
		this.m_rI8_vj = m_rI8_vj;
	}

	public double[] getM_rI8_sk() {
		return m_rI8_sk;
	}

	public void setM_rI8_sk(double[] m_rI8_sk) {
		this.m_rI8_sk = m_rI8_sk;
	}

	public double[][] getM_rI8_aij() {
		return m_rI8_aij;
	}

	public void setM_rI8_aij(double[][] m_rI8_aij) {
		this.m_rI8_aij = m_rI8_aij;
	}

	public double[][] getM_rI8_bik() {
		return m_rI8_bik;
	}

	public void setM_rI8_bik(double[][] m_rI8_bik) {
		this.m_rI8_bik = m_rI8_bik;
	}

	public double[][] getM_rI8_yiq() {
		return m_rI8_yiq;
	}

	public void setM_rI8_yiq(double[][] m_rI8_yiq) {
		this.m_rI8_yiq = m_rI8_yiq;
	}

	/**
	 * 测试数据是否读取成功
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SSModel t_aTC_ssm = new SSModel(NameSpace.s_str_data_01);
		System.out.println("1.基本信息：");
		System.out.println("m = " + t_aTC_ssm.getM_aI4_n());
		System.out.println("n = " + t_aTC_ssm.getM_aI4_m());
		System.out.println("k = " + t_aTC_ssm.getM_aI4_k());
		System.out.println("q = " + t_aTC_ssm.getM_aI4_q());
		System.out.println("t = " + t_aTC_ssm.getM_aI8_max_t());

		System.out.println("2.wi:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_wi());

		System.out.println("3.t0j:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_t0j());

		System.out.println("4.lj:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_lj());

		System.out.println("5.vj:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_vj());

		System.out.println("6.sk:");
		MathUtils.print1D(t_aTC_ssm.getM_rI8_sk());

		System.out.println("7.aij:");
		MathUtils.print2D(t_aTC_ssm.getM_rI8_aij());

		System.out.println("8.bik:");
		MathUtils.print2D(t_aTC_ssm.getM_rI8_bik());

		System.out.println("9.yiq:");
		MathUtils.print2D(t_aTC_ssm.getM_rI8_yiq());

	}

}
