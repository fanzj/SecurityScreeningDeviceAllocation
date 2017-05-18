package com.fzj.data;

import java.math.BigDecimal;
import java.util.Random;

import com.fzj.config.NameSpace;
import com.fzj.utils.FileUtils;
import com.fzj.utils.MathUtils;

/**
 * @author dell
 * @date 2017��1��2�� ����9:27:48
 * @version 1.0
 * @Description ���������������
 */
public class RandomGenerateData {
	
	/**
	 * Σ����Ʒ������
	 */
	private int m_aI4_m;
	
	/**
	 * ������
	 */
	private int m_aI4_n;
	
	/**
	 * �����豸������
	 */
	private int m_aI4_k;
	
	/**
	 * ������Ա��
	 */
	private int m_aI4_q;
	
	/**
	 * ʱ������
	 */
	//private double m_aI8_max_t;
	
	
	/**
	 * �����������
	 */
	private static Random s_aTC_random = new Random(System.currentTimeMillis());
	
	public RandomGenerateData(int f_aI4_m,int f_aI4_n,int f_aI4_k,int f_aI4_q){
		this.m_aI4_m = f_aI4_m;
		this.m_aI4_n = f_aI4_n;
		this.m_aI4_k = f_aI4_k;
		this.m_aI4_q = f_aI4_q;
		//this.m_aI8_max_t = f_aI8_max_t;
	}
	
	/**
	 * �������[f_aI8_low,f_aI8_upper]֮������������
	 * @param f_aI8_low
	 * @param f_aI8_upper
	 */
	private static double randDoubleAToB(double f_aI8_low,double f_aI8_upper){
		BigDecimal t_aTC_low = new BigDecimal(f_aI8_low);
		BigDecimal t_aTC_upper = new BigDecimal(f_aI8_upper);
		BigDecimal t_aTC_rand = new BigDecimal(s_aTC_random.nextDouble());
		return t_aTC_low.add((t_aTC_upper.subtract(t_aTC_low)).multiply(t_aTC_rand)).doubleValue();
	}
	
	/**
	 * ���ɻ�����Ϣ
	 * ���M ��Σ����Ʒ��������
	 * ���N ����������
	 * ���K �������豸����
	 * @param f_str_path
	 */
	private void genBasicInfo(String f_str_path){
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_basic_info, m_aI4_m+","+m_aI4_n+","+m_aI4_k+","+m_aI4_q);
	}
	
	/**
	 * ����Σ����Ʒ��Ȩ��
	 * @param f_str_path
	 */
	private void genWi(String f_str_path){
		StringBuffer t_aTC_res = new StringBuffer();
		double t_aI8_sum = 0;//�ۻ�����
		double[] t_aI8_res = new double[m_aI4_m];
		int[] t_aI4_ran = new int[m_aI4_m];
		for(int t_aI4_i=0;t_aI4_i<m_aI4_m;t_aI4_i++){
			t_aI4_ran[t_aI4_i] = s_aTC_random.nextInt(1000);
			t_aI8_sum += t_aI4_ran[t_aI4_i];
		}
		for(int t_aI4_i=0;t_aI4_i<m_aI4_m;t_aI4_i++){
			t_aI8_res[t_aI4_i] = t_aI4_ran[t_aI4_i]*1.0/t_aI8_sum;
			t_aTC_res.append(MathUtils.formatD8(t_aI8_res[t_aI4_i]));
			if(t_aI4_i<m_aI4_m-1)
				t_aTC_res.append(" ");
		}
		//System.out.format("%f + %f\n", t_aI8_sum,1-t_aI8_sum);
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_wi, t_aTC_res.toString());
	}
	
	/**
	 * �����ٶ�
	 * @param f_str_path
	 */
	private void genSk(String f_str_path){
		StringBuffer t_aTC_res = new StringBuffer();
		for(int t_aI4_j=0;t_aI4_j<m_aI4_k;t_aI4_j++){
			t_aTC_res.append(MathUtils.formatD(randDoubleAToB(0, 3), 3));
			if(t_aI4_j < m_aI4_k-1){
				t_aTC_res.append(" ");
			}
		}
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_sk, t_aTC_res.toString());
	}
	
	/**
	 * ����ÿ����������󳤶�
	 * @param f_str_path
	 */
	private void genLj(String f_str_path){
		StringBuffer t_aTC_res = new StringBuffer();
		for(int t_aI4_j=0;t_aI4_j<m_aI4_n;t_aI4_j++){
			t_aTC_res.append(MathUtils.formatD(randDoubleAToB(0, 3), 3));
			if(t_aI4_j < m_aI4_n-1){
				t_aTC_res.append(" ");
			}
		}
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_lj, t_aTC_res.toString());
	}
	
	private void gent0j(String f_str_path){
		StringBuffer t_aTC_res = new StringBuffer();
		for(int t_aI4_j=0;t_aI4_j<m_aI4_n;t_aI4_j++){
			t_aTC_res.append(MathUtils.formatD(randDoubleAToB(0, 20), 3));
			if(t_aI4_j < m_aI4_n-1){
				t_aTC_res.append(" ");
			}
		}
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_t0j, t_aTC_res.toString());
	}
	
	/**
	 * ����ÿ�����������
	 * @param f_str_path
	 */
	private void genVj(String f_str_path){
		StringBuffer t_aTC_res = new StringBuffer();
		for(int t_aI4_j=0;t_aI4_j<m_aI4_n;t_aI4_j++){
			t_aTC_res.append(MathUtils.formatD(randDoubleAToB(0, 10), 3));
			if(t_aI4_j < m_aI4_n-1){
				t_aTC_res.append(" ");
			}
		}
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_vj, t_aTC_res.toString());
	}
	
	/**
	 * j�к���i�ĸ���
	 * @param f_str_path
	 */
	private void genAij(String f_str_path){
		StringBuffer t_aTC_res = new StringBuffer();
		for(int t_aI4_i=0;t_aI4_i<m_aI4_m;t_aI4_i++){
			for(int t_aI4_j=0;t_aI4_j<m_aI4_n;t_aI4_j++){
				t_aTC_res.append(MathUtils.formatD8(randDoubleAToB(0, 1)));
				if(t_aI4_j < m_aI4_n-1){
					t_aTC_res.append(" ");
				}else if(t_aI4_i < m_aI4_m-1) {
					t_aTC_res.append("\n");
				}
			}
		}
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_aij, t_aTC_res.toString());
	}
	
	/**
	 * Σ��Ʒi���豸k��⵽�ĸ���
	 * @param f_str_path
	 */
	private void genBik(String f_str_path){
		StringBuffer t_aTC_res = new StringBuffer();
		for(int t_aI4_i=0;t_aI4_i<m_aI4_m;t_aI4_i++){
			for(int t_aI4_k=0;t_aI4_k<m_aI4_k;t_aI4_k++){
				t_aTC_res.append(MathUtils.formatD8(randDoubleAToB(0, 1)));
				if(t_aI4_k < m_aI4_k-1){
					t_aTC_res.append(" ");
				}else if(t_aI4_i < m_aI4_m-1) {
					t_aTC_res.append("\n");
				}
			}
		}
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_bik, t_aTC_res.toString());
	}
	
	/**
	 * Σ��Ʒi����q��⵽�ĸ���
	 * @param f_str_path
	 */
	private void genYiq(String f_str_path){
		StringBuffer t_aTC_res = new StringBuffer();
		for(int t_aI4_i=0;t_aI4_i<m_aI4_m;t_aI4_i++){
			for(int t_aI4_q=0;t_aI4_q<m_aI4_q+1;t_aI4_q++){
				if(t_aI4_q==0){
					t_aTC_res.append("0");
				}else {
					t_aTC_res.append(MathUtils.formatD8(randDoubleAToB(0, 1)));
				}
				if(t_aI4_q < m_aI4_q){
					t_aTC_res.append(" ");
				}else if(t_aI4_i < m_aI4_m-1) {
					t_aTC_res.append("\n");
				}
			}
		}
		FileUtils.saveFile(f_str_path, "", NameSpace.s_str_yiq, t_aTC_res.toString());
	}


	
	/**
	 * ���������������
	 * @param f_str_path ·��
	 */
	private void genExpData(String f_str_path){
		FileUtils.s_aI1_operation = true;//��ʾ��ǰ����Ϊ����ʵ�����ݵ�·��
		genBasicInfo(f_str_path);
		genWi(f_str_path);
		genSk(f_str_path);
		genLj(f_str_path);
		genVj(f_str_path);
		genBik(f_str_path);
		genYiq(f_str_path);
		genAij(f_str_path);
		gent0j(f_str_path);
	}
	
	public static void main(String[] args) {
		//RandomGenerateData t_aTC_genData = new RandomGenerateData(5, 24, 6,4);//T=250
		//RandomGenerateData t_aTC_genData = new RandomGenerateData(12, 210, 18,12);//T=450
		//RandomGenerateData t_aTC_genData = new RandomGenerateData(23, 450, 42,30);//T=580
		RandomGenerateData t_aTC_genData = new RandomGenerateData(32, 1024, 64,40);//T=650
		t_aTC_genData.genExpData("data_04");
		System.out.println("�������ɳɹ���");
	}
}
