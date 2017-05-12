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
	 * 存放M （危害物品种类数）
	 * 存放N （行李数）
	 * 存放K （安检设备种类数）
	 * 存放Q （安检人员数）
	 */
	public static final String s_str_basic_info = "basic_info.txt";
	
	/**
	 * 危险物品的权重
	 */
	public static final String s_str_wi = "w_i.txt";
	
	/**
	 * 时间上限系数
	 */
	public static final String s_str_landa = "landa.txt";
	
	/**
	 * 包裹j的长度
	 */
	public static final String s_str_lj = "l_j.txt";
	
	/**
	 * 包裹j的体积
	 */
	public static final String s_str_sj = "s_j.txt";
	
	/**
	 * 检测速度
	 */
	public static final String s_str_vk = "v_k.txt";
	
	/**
	 * 每个行李最多可分配的设备数
	 */
	public static final String s_str_b = "b.txt";
	
	/**
	 * 携带i的概率
	 */
	public static final String s_str_bi = "b_i.txt";
	
	/**
	 * i被k检测的概率
	 */
	public static final String s_str_ydik = "yd_ik.txt";
	
	/**
	 * i被k检测的概率
	 */
	public static final String s_str_ypi = "yp_i.txt";
	
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
	 * 行李数
	 */
	public static final String s_str_n = "BAGGAGE";
	
	/**
	 * 安检设备数
	 */
	public static final String s_str_k = "SECURITY_SCREENING_DEVICE";
	
	/**
	 * 安检人员数
	 */
	public static final String s_str_q = "SECURITY_SCREENING_DEVICE_PERSONNEL";
	
	
	
	/**
	 * DNSPSO算法
	 */
	public static final String s_str_amdnspso = "AMDNSPSO";
	
	public static final String s_str_bpso = "BPSO";
	
	public static final String s_str_ga = "GA";
	
	public static final String s_str_ibpso = "IBPSO";
	
	public static final String s_str_bde = "BDE";
	
	public static final String s_str_bwwo = "BWWO";
	
	public static final String s_str_ampso = "AMPSO";
	
	/**
	 * FADE算法
	 */
	public static final String s_str_fade = "FADE";
	
	/**
	 * WWO算法
	 */
	public static final String s_str_wwo = "WWO";
	
	
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
	 * 最大迭代次数
	 */
	public static final String s_str_max_iter = "max_iter";
	
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
	 * BPSO的最大迭代次数
	 */
	public static final String s_str_max_iter_bpso = "max_iter_bpso";
	
	public static final String s_str_max_iter_ga = "max_iter_ga";
	
	public static final String s_str_max_iter_ibpso = "max_iter_ibpso";
	
	public static final String s_str_max_iter_ampso = "max_iter_ampso";
	
	public static final String s_str_max_iter_bde = "max_iter_bde";
	
	
	/**
	 * 正式运行配置
	 */
	public static final String s_str_run_config = "run.config";
	
	/**
	 * 测试运行配置
	 */
	public static final String s_str_run_test_config = "run_test.config";
	
}
