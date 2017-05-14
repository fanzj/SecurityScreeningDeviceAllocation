package com.fzj.data;

import java.math.BigDecimal;
import java.util.Random;

import com.fzj.config.NameSpace;
import com.fzj.utils.FileUtils;
import com.fzj.utils.MathUtils;

/**
 * @author dell
 * @date 2017年1月2日 下午9:27:48
 * @version 1.0
 * @Description 随机生成试验数据
 */
public class RandomGenerateData {

	/**
	 * 危害物品种类数
	 */
	private int m_aI4_m;

	/**
	 * 包裹数
	 */
	private int m_aI4_n;

	/**
	 * 安检设备种类数
	 */
	private int m_aI4_k;

	/**
	 * 安检人员数
	 */
	private int m_aI4_q;



	/**
	 * 随机数生成器
	 */
	private static Random s_aTC_random = new Random(System.currentTimeMillis());

	public RandomGenerateData(int f_aI4_m, int f_aI4_n, int f_aI4_k, int f_aI4_q) {
		this.m_aI4_m = f_aI4_m;
		this.m_aI4_n = f_aI4_n;
		this.m_aI4_k = f_aI4_k;
		this.m_aI4_q = f_aI4_q;
	}

	/**
	 * 随机生成[f_aI8_low,f_aI8_upper]之间的随机浮点数
	 * 
	 * @param f_aI8_low
	 * @param f_aI8_upper
	 */
	private static double randDoubleAToB(double f_aI8_low, double f_aI8_upper) {
		BigDecimal t_aTC_low = new BigDecimal(f_aI8_low);
		BigDecimal t_aTC_upper = new BigDecimal(f_aI8_upper);
		BigDecimal t_aTC_rand = new BigDecimal(s_aTC_random.nextDouble());
		return t_aTC_low.add((t_aTC_upper.subtract(t_aTC_low)).multiply(t_aTC_rand)).doubleValue();
	}

	/**
	 * 生成基本信息 存放M （危害物品种类数） 存放N （包裹数） 存放K （安检设备数） 存放Q （安检人员数） 存放B
	 * （每个行李最多可分配的设备数）
	 * 
	 * @param f_str_path
	 */
	private void genBasicInfo(String f_str_path) {
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_basic_info,
				m_aI4_m + "," + m_aI4_n + "," + m_aI4_k + "," + m_aI4_q);
	}

	/**
	 * 生成危险物品的权重
	 * 
	 * @param f_str_path
	 */
	private void genWi(String f_str_path) {
		StringBuffer t_aTC_res = new StringBuffer();
		double t_aI8_sum = 0;// 累积概率
		double[] t_aI8_res = new double[m_aI4_m];
		int[] t_aI4_ran = new int[m_aI4_m];
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_m; t_aI4_i++) {
			t_aI4_ran[t_aI4_i] = s_aTC_random.nextInt(1000);
			t_aI8_sum += t_aI4_ran[t_aI4_i];
		}
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_m; t_aI4_i++) {
			t_aI8_res[t_aI4_i] = t_aI4_ran[t_aI4_i] * 1.0 / t_aI8_sum;
			t_aTC_res.append(MathUtils.formatD8(t_aI8_res[t_aI4_i]));
			if (t_aI4_i < m_aI4_m - 1)
				t_aTC_res.append(" ");
		}
		// System.out.format("%f + %f\n", t_aI8_sum,1-t_aI8_sum);
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_wi, t_aTC_res.toString());
	}

	/**
	 * 生成安检设备的速度
	 * 
	 * @param f_str_path
	 */
	private void genVk(String f_str_path) {
		StringBuffer t_aTC_res = new StringBuffer();
		for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
			t_aTC_res.append(MathUtils.formatD(randDoubleAToB(1, 3), 3));
			if (t_aI4_k < m_aI4_k - 1) {
				t_aTC_res.append(" ");
			}
		}
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_vk, t_aTC_res.toString());
	}

	/**
	 * 生成每个包裹的最大长度
	 * 
	 * @param f_str_path
	 */
	private void genLj(String f_str_path) {
		StringBuffer t_aTC_res = new StringBuffer();
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			t_aTC_res.append(MathUtils.formatD(randDoubleAToB(0, 10), 3));
			if (t_aI4_j < m_aI4_n - 1) {
				t_aTC_res.append(" ");
			}
		}
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_lj, t_aTC_res.toString());
	}

	/**
	 * 生成每个包裹的体积
	 * 
	 * @param f_str_path
	 */
	private void genSj(String f_str_path) {
		StringBuffer t_aTC_res = new StringBuffer();
		for (int t_aI4_j = 0; t_aI4_j < m_aI4_n; t_aI4_j++) {
			t_aTC_res.append(MathUtils.formatD(randDoubleAToB(0, 30), 3));
			if (t_aI4_j < m_aI4_n - 1) {
				t_aTC_res.append(" ");
			}
		}
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_sj, t_aTC_res.toString());
	}

	/**
	 * 携带危险品为i的概率
	 * 
	 * @param f_str_path
	 */
	private void genBi(String f_str_path) {
		StringBuffer t_aTC_res = new StringBuffer();
		double t_aI8_sum = 0;// 累积概率
		double[] t_aI8_res = new double[m_aI4_m];
		int[] t_aI4_ran = new int[m_aI4_m];
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_m; t_aI4_i++) {
			t_aI4_ran[t_aI4_i] = s_aTC_random.nextInt(1000);
			t_aI8_sum += t_aI4_ran[t_aI4_i];
		}
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_m; t_aI4_i++) {
			t_aI8_res[t_aI4_i] = t_aI4_ran[t_aI4_i] * 1.0 / t_aI8_sum;
			t_aTC_res.append(MathUtils.formatD8(t_aI8_res[t_aI4_i]));
			if (t_aI4_i < m_aI4_m - 1)
				t_aTC_res.append(" ");
		}

		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_bi, t_aTC_res.toString());
	}

	/**
	 * 危险品i被设备k检测到的概率
	 * 
	 * @param f_str_path
	 */
	private void genYdik(String f_str_path) {
		StringBuffer t_aTC_res = new StringBuffer();
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_m; t_aI4_i++) {
			for (int t_aI4_k = 0; t_aI4_k < m_aI4_k; t_aI4_k++) {
				//t_aTC_res.append(MathUtils.formatD8(randDoubleAToB(0.6, 0.9)));
				t_aTC_res.append(MathUtils.formatD8(randDoubleAToB(0, 1)));
				if (t_aI4_k < m_aI4_k - 1) {
					t_aTC_res.append(" ");
				} else if (t_aI4_i < m_aI4_m - 1) {
					t_aTC_res.append("\n");
				}
			}
		}
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_ydik, t_aTC_res.toString());
	}

	/**
	 * 危险品i被安检人员检测到的概率
	 * 
	 * @param f_str_path
	 */
	private void genYpi(String f_str_path) {
		StringBuffer t_aTC_res = new StringBuffer();
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_m; t_aI4_i++) {
//			t_aTC_res.append(MathUtils.formatD8(randDoubleAToB(0.3, 0.7)));
			t_aTC_res.append(MathUtils.formatD8(randDoubleAToB(0, 1)));
			if (t_aI4_i < m_aI4_m - 1) {
				t_aTC_res.append(" ");
			}
		}
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_ypi, t_aTC_res.toString());
	}

	/**
	 * 随机生成试验数据
	 * 
	 * @param f_str_path
	 *            路径
	 */
	private void genExpData(String f_str_path) {
		FileUtils.s_aI1_operation = true;// 表示当前环境为生成实验数据的路径
		genBasicInfo(f_str_path);
		genWi(f_str_path);
		genVk(f_str_path);
		genLj(f_str_path);
		genSj(f_str_path);
		genBi(f_str_path);
		genYdik(f_str_path);
		genYpi(f_str_path);
	}

	public static void main(String[] args) {
		RandomGenerateData t_aTC_genData = new RandomGenerateData(57,780,32,30);
		t_aTC_genData.genExpData("data_04");
		System.out.println("数据生成成功！");
	}
}
