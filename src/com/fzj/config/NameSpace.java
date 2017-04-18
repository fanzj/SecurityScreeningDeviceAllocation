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
	public static final String s_str_experiment_data_path = "experiment_data\\";
	
	/**
	 * 写入路径
	 */
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
	
	/**
	 * 设备k是自动设备还是手动设备
	 */
	public static final String s_str_yk = "y_k.txt";
	
	/**
	 * 包裹j的长度
	 */
	public static final String s_str_lj = "l_j.txt";
	
	/**
	 * 包裹j的体积
	 */
	public static final String s_str_sj = "s_j.txt";
	
	/**
	 * 自动检测速度
	 */
	public static final String s_str_vak = "va_k.txt";
	
	/**
	 * 手动检测速度
	 */
	public static final String s_str_vhk = "vh_k.txt";
	
	/**
	 * 设备k的容积
	 */
	public static final String s_str_ck = "c_k.txt";
	
	/**
	 * 携带i的概率
	 */
	public static final String s_str_bi = "b_i.txt";
	
	/**
	 * i被k检测的概率
	 */
	public static final String s_str_yik = "y_ik.txt";
	
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
	
	public static final String s_str_p = "PTHDEVICE";
	
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
	 * PWWO算法
	 */
	public static final String s_str_pwwo = "PWWO";
	
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
	 * DNSPSO的最大迭代次数
	 */
	public static final String s_str_max_iter_dnspso = "max_iter_dnspso";
	
	/**
	 * DE_DNSPSO的最大迭代次数
	 */
	public static final String s_str_max_iter_de_dnspso = "max_iter_de_dnspso";
	
	/**
	 * FADE的最大迭代次数
	 */
	public static final String s_str_max_iter_fade = "max_iter_fade";
	
	/**
	 * WWO的最大迭代次数
	 */
	public static final String s_str_max_iter_wwo = "max_iter_wwo";
	
	/**
	 * 正式运行配置
	 */
	public static final String s_str_run_config = "run.config";
	
	/**
	 * 测试运行配置
	 */
	public static final String s_str_run_test_config = "run_test.config";
	
}
