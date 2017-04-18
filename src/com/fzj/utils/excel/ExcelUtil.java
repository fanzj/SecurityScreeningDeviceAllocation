package com.fzj.utils.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.fzj.solution.Fitness;

public class ExcelUtil {
	
	public void writeExcel(List<Fitness> f_aTC_list, String f_str_path,String f_str_sheetName) throws Exception {
		if (f_aTC_list == null) {
			return;
		} else if (f_str_path == null || Common.EMPTY.equals(f_str_path)) {
			return;
		} else {
			String t_str_postfix = Util.getPostfix(f_str_path);
			if (!Common.EMPTY.equals(t_str_postfix)) {
				if (Common.OFFICE_EXCEL_2003_POSTFIX.equals(t_str_postfix)) {
					writeXls(f_aTC_list, f_str_path, f_str_sheetName);
				} else if (Common.OFFICE_EXCEL_2010_POSTFIX.equals(t_str_postfix)) {
					writeXlsx(f_aTC_list, f_str_path, f_str_sheetName);
				}
			}else{
				System.out.println(f_str_path + Common.NOT_EXCEL_FILE);
			}
		}
	}

	private String getValue(XSSFCell f_aTC_xssfRow) {
		if (f_aTC_xssfRow.getCellType() == f_aTC_xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(f_aTC_xssfRow.getBooleanCellValue());
		} else if (f_aTC_xssfRow.getCellType() == f_aTC_xssfRow.CELL_TYPE_NUMERIC) {
			return String.valueOf(f_aTC_xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(f_aTC_xssfRow.getStringCellValue());
		}
	}

	private String getValue(HSSFCell f_aTC_hssfCell) {
		if (f_aTC_hssfCell.getCellType() == f_aTC_hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(f_aTC_hssfCell.getBooleanCellValue());
		} else if (f_aTC_hssfCell.getCellType() == f_aTC_hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(f_aTC_hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(f_aTC_hssfCell.getStringCellValue());
		}
	}
	
	public void writeXls(List<Fitness> f_aTC_list, String f_str_path,String f_str_sheetName) throws Exception {
		if (f_aTC_list == null) {
			return;
		}
		int t_aI4_countColumnNum = f_aTC_list.size();
		HSSFWorkbook t_aTC_book = new HSSFWorkbook();
		HSSFSheet t_aTC_sheet = t_aTC_book.createSheet(f_str_sheetName);
		// option at first row.
		HSSFRow t_aTC_firstRow = t_aTC_sheet.createRow(0);
		HSSFCell[] t_aTC_firstCells = new HSSFCell[t_aI4_countColumnNum];
		String[] t_rstr_options = { "id", "fitness"};
		for (int t_aI4_j = 0; t_aI4_j < t_rstr_options.length; t_aI4_j++) {
			t_aTC_firstCells[t_aI4_j] = t_aTC_firstRow.createCell(t_aI4_j);
			t_aTC_firstCells[t_aI4_j].setCellValue(new HSSFRichTextString(t_rstr_options[t_aI4_j]));
		}
		//
		for (int t_aI4_i = 0; t_aI4_i < t_aI4_countColumnNum; t_aI4_i++) {
			HSSFRow t_aTC_row = t_aTC_sheet.createRow(t_aI4_i + 1);
			Fitness t_aTC_f = f_aTC_list.get(t_aI4_i);
			for (int t_aI4_column = 0; t_aI4_column < t_rstr_options.length; t_aI4_column++) {
				HSSFCell t_aTC_id = t_aTC_row.createCell(0);
				HSSFCell t_aTC_fitness = t_aTC_row.createCell(1);
				
				t_aTC_id.setCellValue(t_aTC_f.getM_aI4_nfe());
				t_aTC_fitness.setCellValue(t_aTC_f.getM_aI8_fitness());
				
			}
		}
		File t_aTC_file = new File(f_str_path);
		OutputStream t_aTC_os = new FileOutputStream(t_aTC_file);
		System.out.println(Common.WRITE_DATA + f_str_path);
		t_aTC_book.write(t_aTC_os);
		t_aTC_os.close();
	}
	
	public void writeXlsx(List<Fitness> f_aTC_list, String f_str_path,String f_str_sheetName) throws Exception {
		if (f_aTC_list == null) {
			return;
		}
		//XSSFWorkbook
		int t_aI4_countColumnNum = f_aTC_list.size();
		XSSFWorkbook t_aTC_book = new XSSFWorkbook();
		XSSFSheet t_aTC_sheet = t_aTC_book.createSheet(f_str_sheetName);
		// option at first row.
		XSSFRow t_aTC_firstRow = t_aTC_sheet.createRow(0);
		XSSFCell[] t_aTC_firstCells = new XSSFCell[t_aI4_countColumnNum];
		String[] t_rstr_options = { "id","fitness"};
		for (int t_aI4_j = 0; t_aI4_j < t_rstr_options.length; t_aI4_j++) {
			t_aTC_firstCells[t_aI4_j] = t_aTC_firstRow.createCell(t_aI4_j);
			t_aTC_firstCells[t_aI4_j].setCellValue(new XSSFRichTextString(t_rstr_options[t_aI4_j]));
		}
		//
		for (int t_aI4_i = 0; t_aI4_i < t_aI4_countColumnNum; t_aI4_i++) {
			XSSFRow t_aTC_row = t_aTC_sheet.createRow(t_aI4_i + 1);
			Fitness t_aTC_f = f_aTC_list.get(t_aI4_i);
			for (int t_aI4_column = 0; t_aI4_column < t_rstr_options.length; t_aI4_column++) {
				XSSFCell t_aTC_id = t_aTC_row.createCell(0);
				XSSFCell t_aTC_fitness = t_aTC_row.createCell(1);
				
				t_aTC_id.setCellValue(t_aTC_f.getM_aI4_nfe());
				t_aTC_fitness.setCellValue(t_aTC_f.getM_aI8_fitness());
			}
		}
		File t_aTC_file = new File(f_str_path);
		OutputStream t_aTC_os = new FileOutputStream(t_aTC_file);
		System.out.println(Common.WRITE_DATA + f_str_path);
		t_aTC_book.write(t_aTC_os);
		t_aTC_os.close();
	}
}
