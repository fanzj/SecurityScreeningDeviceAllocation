package com.fzj.config;
/** 
 * @author Fan Zhengjie 
 * @date 2017年1月28日 下午4:05:00 
 * @version 1.0 
 * @description 命名空间（规范）
 */
public class NameSpace {

	/**
	 * 读取路径
	 */
	//public static final String s_str_experiment_data_path = "experiment_data\\";
	public static final String s_str_experiment_data_path = "experiment_data2\\";
	
	/**
	 * 写入路径
	 */
	//public static final String s_str_experiment_result_path = "experiment_result\\";
	public static final String s_str_experiment_result_path = "experiment_result\\";

	/**
	 * 基本信息文件名 
	 * 存放N （危害物品种类数）
	 * 存放M （安检设备种类数）
	 * 存放R （安检场所数）
	 */
	public static final String s_str_basic_info = "basic_info.txt";
	
	/**
	 * 危险物品的权重
	 */
	public static final String s_str_wi = "w_i.txt";
	
	public static final String s_str_t0j = "t0_j.txt";
	
	/**
	 * 包裹j的长度
	 */
	public static final String s_str_lj = "l_j.txt";
	
	/**
	 * 包裹j的体积
	 */
	public static final String s_str_vj = "v_j.txt";
	
	/**
	 * 检测速度
	 */
	public static final String s_str_sk = "s_k.txt";
	
	
	public static final String s_str_aij = "a_ij.txt";
	
	/**
	 * i被k检测的概率
	 */
	public static final String s_str_bik = "b_ik.txt";
	
	/**
	 * i被q检测的概率
	 */
	public static final String s_str_yiq = "y_iq.txt";
	
	/**
	 * 第一组实验数据
	 */
	public static final String s_str_data_01 = "data_01\\";
	
	/**
	 * 第二组实验数据
	 */
	public static final String s_str_data_02 = "data_02\\";
	
	/**
	 * 第三组实验数据
	 */
	public static final String s_str_data_03 = "data_03\\";
	
	/**
	 * 第四组实验数据
	 */
	public static final String s_str_data_04 = "data_04\\";
	
	/**
	 * 第五组实验数据
	 */
	public static final String s_str_data_05 = "data_05\\";
	
	public static final String s_str_data_06 = "data_06\\";
	
	public static final String s_str_data_07 = "data_07\\";
	
	public static final String s_str_data_08 = "data_08\\";
	
	/**
	 * 危险物品种类数
	 */
	public static final String s_str_m = "HAZARDOUS_SUBSTANCE";
	
	/**
	 * 安检设备种类数
	 */
	public static final String s_str_n = "BAGGAGE";
	
	/**
	 * 安检场所数
	 */
	public static final String s_str_k = "SECURITY_SCREENING_DEVICE";
	
	public static final String s_str_q = "INSPECTOR";
	
	public static final String s_str_max_t = "MAX_T";
	

	
	/**
	 * PSO算法
	 */
	public static final String s_str_pso = "PSO";
	
	/**
	 * DNSPSO算法
	 */
	public static final String s_str_dnspso = "DNSPSO";
	
	/**
	 * FADE算法
	 */
	public static final String s_str_fade = "FADE";
	
	/**
	 * WWO算法
	 */
	public static final String s_str_wwo = "WWO";
	
	/**
	 * DE算法
	 */
	public static final String s_str_de = "DE";
	
	public static final String s_str_ga = "GA";
	
	/**
	 * 改进的DNSPSO
	 */
	public static final String s_str_dednspso = "DE_DNSPSO";
	
	/**
	 * txt文件
	 */
	public static final String s_str_file_txt = ".txt";
	
	/**
	 * excel文件
	 */
	public static final String s_str_file_excel = ".xlsx";
	
	/**
	 * 种群规模
	 */
	public static final String s_str_population_size = "population_size";
	
	/**
	 * 运行次数
	 */
	public static final String s_str_run_times = "run_times";
	
	/**
	 * 最大评价次数
	 */
	public static final String s_str_max_nfe = "max_nfe";
	
	/**
	 * 单次最大运行时间
	 */
	public static final String s_str_max_time = "max_time";
	
	
	
	public static final String s_str_max_iter = "max_iter";
	
	public static final String s_str_u_t = "upper_t";
	
	/**
	 * 正式运行配置
	 */
	public static final String s_str_run_config = "run.config";
	
	/**
	 * 测试运行配置
	 */
	public static final String s_str_run_test_config = "run_test.config";
	
}
