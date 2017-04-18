package com.fzj.utils;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * 读取配置文件
 * @author dell
 * @date 2017年4月17日 下午4:15:35
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
