package com.fzj.model;

import java.util.Map;

import com.fzj.config.NameSpace;
import com.fzj.utils.FileUtils;

/**
 * @author Fan Zhengjie
 * @date 2017��8��9�� ����3:37:08
 * @version 1.0
 * @description ����Σ��Ʒ���ֵ�Ƶ������
 */
public class HazardFreqInterval {
	
	public static String calInterval(String filePath, String fileName){
		FileUtils.s_aI1_operation = true;
		Map<String, String> map = FileUtils.readBasicInfo(filePath, NameSpace.s_str_basic_info);
		int m = Integer.parseInt(map.get(NameSpace.s_str_m));
		int n = Integer.parseInt(map.get(NameSpace.s_str_n));
		double[][] aij = FileUtils.read2D(filePath, fileName, m, n);
		
		double sum[] = new double[m];
		double total = 0;
		int minPos = -1, maxPos = -1;
		double minVal = Double.MAX_VALUE, maxVal = Double.MIN_VALUE;
		for(int i=0;i<m;i++){
			sum[i] = 0;
			for(int j=0;j<n;j++){
				sum[i] += aij[i][j];
			}
			total += sum[i];
			
			if(Double.compare(sum[i], minVal)<0){
				minVal = sum[i];
				minPos = i;
			}
			if(Double.compare(sum[i], maxVal)>0){
				maxVal = sum[i];
				maxPos = i;
			}
		}
		for(int i=0;i<m;i++){
			sum[i] /= total;
		}
		//FileUtils.print(sum);
		return String.format("[%f,%f]", sum[minPos],sum[maxPos]);
	}

	// ���
	public static void main(String[] args) {
		for (int i = 1; i <= 8; i++) {
			String filePath = NameSpace.s_str_data + String.format("%02d\\", i);
			String fileName = NameSpace.s_str_aij;//��ȡ·��
			System.out.println(calInterval(filePath,fileName));
		}
	}

}
