package com.fzj.utils;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * ��ȡ�����ļ�
 * @author dell
 * @date 2017��4��17�� ����4:15:35
 * @title
 * @description
 */
public class MProperties {
	public static Properties get(String file) {
		Properties p = new Properties();
		try {
			FileInputStream in = new FileInputStream(file);
			p.load(in);
			in.close();
			System.out.println("Load properties successfully !");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
}
