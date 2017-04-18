package com.fzj.utils.excel;

/**
 * 
 * @author Fantasy
 *
 */
public class Util {

	/**
	 * get postfix of the path
	 * @param path
	 * @return
	 */
	public static String getPostfix(String f_str_path) {
		if (f_str_path == null || Common.EMPTY.equals(f_str_path.trim())) {
			return Common.EMPTY;
		}
		if (f_str_path.contains(Common.POINT)) {
			return f_str_path.substring(f_str_path.lastIndexOf(Common.POINT) + 1, f_str_path.length());
		}
		return Common.EMPTY;
	}
}
