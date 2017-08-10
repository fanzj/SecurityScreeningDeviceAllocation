package com.fzj.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fzj.config.NameSpace;

/**
 * 文件操作类
 * @author dell
 * @date 2017年1月2日 下午8:53:46
 * @version 1.0
 * @Description 文件的读写
 */
public class FileUtils {
	
	/**
	 * 操作方式选择开关
	 */
	public static boolean s_aI1_operation = false;
	
	/**
	 * 获取当前的为何种操作
	 * @return 返回路径
	 */
	public static String getCurrentPath(){
		if(s_aI1_operation){
			return NameSpace.s_str_experiment_data_path;
		}else {
			return NameSpace.s_str_experiment_result_path;
		}
	}
	
	/**
	 * 获取结果保存的路径
	 * @param f_str_data_path
	 * @param f_str_alg_name
	 * @return
	 */
	public static String getPath(String f_str_data_path,String f_str_alg_name){
		return getCurrentPath()+f_str_data_path+f_str_alg_name+"\\";
	}
	
	/**
	 * 根据算法名和时间生成不同类型的文件
	 * @param f_str_alg_name 算法名称
	 * @param f_str_file_type 文件类型
	 * @param f_aI4_max_nfe 最大评价次数
	 * @return
	 */
	public static String getResultName(String f_str_alg_name,String f_str_file_type,int f_aI4_max_nfe){
		SimpleDateFormat t_aTC_sdf = new SimpleDateFormat("yyyyMM");
		String t_str_file_name = f_str_alg_name.toLowerCase()+"_"+t_aTC_sdf.format(new Date())
				+"_nfe="+f_aI4_max_nfe+f_str_file_type;
		return t_str_file_name;
	}
	
	/**
	 * 保存内容到文件 
	 * 1.随机生成数据保存
	 * 2.实验结果保存
	 * @param f_str_data_path 路径名 data_01\\
	 * @param f_str_alg_name 算法名 pso
	 * @param f_str_file_name 文件名 1.txt
	 * @param f_str_content 保存的内容 ???
	 */
	public static void saveFile(String f_str_data_path, String f_str_alg_name,String f_str_file_name,String f_str_content){
		try {
			File t_aTC_file = new File(getCurrentPath()+f_str_data_path+f_str_alg_name);
			if(!t_aTC_file.exists()){
				t_aTC_file.mkdirs();
			}
			FileWriter t_aTC_fileWriter = new FileWriter(getCurrentPath()+f_str_data_path+f_str_alg_name+"\\"+f_str_file_name, true);//追加方式写
			t_aTC_fileWriter.write(f_str_content);
			t_aTC_fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取基本信息【n,m,r】
	 * @param f_str_path 文件路径
	 * @param f_str_filename 文件名
	 * @return
	 */
	public static Map<String, String> readBasicInfo(String f_str_path,String f_str_filename){
		Map<String, String> t_aTC_map = new HashMap<>();
		BufferedReader t_aTC_br = null;
		try {
		    t_aTC_br = new BufferedReader(new InputStreamReader(new FileInputStream(getCurrentPath()+f_str_path+f_str_filename)));
			String t_str_s = t_aTC_br.readLine();
			String[] t_rstr_s = t_str_s.split(",");
			t_aTC_map.put(NameSpace.s_str_m, t_rstr_s[0]);
			t_aTC_map.put(NameSpace.s_str_n, (t_rstr_s[1]));
			t_aTC_map.put(NameSpace.s_str_k, (t_rstr_s[2]));
			t_aTC_map.put(NameSpace.s_str_q, (t_rstr_s[3]));
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(t_aTC_br!=null){
				try {
					t_aTC_br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return t_aTC_map;
	}
	
	/**
	 * 读取一维数组
	 * @param f_str_path 文件路径
	 * @param f_str_filename 文件名
	 * @param f_aI4_len 数组长度
	 * @return
	 */
	public static double[] read1D(String f_str_path,String f_str_filename,int f_aI4_len){
		double[] t_rI8_1d = new double[f_aI4_len];
		BufferedReader t_aTC_br = null;
		try {
			t_aTC_br = new BufferedReader(new InputStreamReader(new FileInputStream(getCurrentPath()+f_str_path+f_str_filename)));
			String t_str_s = t_aTC_br.readLine();
			String[] t_rstr_s = t_str_s.split(" ");
			for(int t_aI4_i=0;t_aI4_i<t_rstr_s.length;t_aI4_i++){
				t_rI8_1d[t_aI4_i] = Double.valueOf(t_rstr_s[t_aI4_i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(t_aTC_br!=null){
				try {
					t_aTC_br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return t_rI8_1d;
	}
	
	/**
	 * 读取一维数组
	 * @param f_str_path 文件路径
	 * @param f_str_filename 文件名
	 * @param f_aI4_len 数组长度
	 * @return
	 */
	public static int[] read1I(String f_str_path,String f_str_filename,int f_aI4_len){
		int[] t_rI4_1d = new int[f_aI4_len];
		BufferedReader t_aTC_br = null;
		try {
			t_aTC_br = new BufferedReader(new InputStreamReader(new FileInputStream(getCurrentPath()+f_str_path+f_str_filename)));
			String t_str_s = t_aTC_br.readLine();
			String[] t_rstr_s = t_str_s.split(" ");
			for(int t_aI4_i=0;t_aI4_i<t_rstr_s.length;t_aI4_i++){
				t_rI4_1d[t_aI4_i] = Integer.valueOf(t_rstr_s[t_aI4_i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(t_aTC_br!=null){
				try {
					t_aTC_br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return t_rI4_1d;
	}
	
	/**
	 * 读取二维数组
	 * @param f_str_path 文件路径
	 * @param f_str_filename 文件名
	 * @return
	 */
	public static double[][] read2D(String f_str_path,String f_str_filename,int f_aI4_len1,int f_aI4_len2){
		double[][] t_rI8_2d = new double[f_aI4_len1][f_aI4_len2];
		BufferedReader t_aTC_br = null;
		try {
			t_aTC_br = new BufferedReader(new InputStreamReader(new FileInputStream(getCurrentPath()+f_str_path+f_str_filename)));
			String t_str_s = "";
			String[] t_rstr_s = null;
			for(int t_aI4_i=0;t_aI4_i<f_aI4_len1;t_aI4_i++){
				t_str_s = t_aTC_br.readLine();
				t_rstr_s = t_str_s.split(" ");
				for(int t_aI4_j=0;t_aI4_j<f_aI4_len2;t_aI4_j++){
					t_rI8_2d[t_aI4_i][t_aI4_j] = Double.valueOf(t_rstr_s[t_aI4_j]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(t_aTC_br!=null){
				try {
					t_aTC_br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return t_rI8_2d;
	}
	
	public static void print(double[] d){
		for(int i=0;i<d.length;i++){
			System.out.print(d[i]);
			if(i<d.length-1)
				System.out.print(" ");
			else {
				System.out.println();
			}
		}
	}
	
	
}
