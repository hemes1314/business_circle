package com.ishansong.tools.businessCircle.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;

/**
 * excel处理工具类
 * @author wubin
 * @date 2017/10/10
 **/
public class ExcelUtils {

	/**
	 * 获取单元格的值
	 *
	 * @author wubin 2017年10月10日
	 * @param cell
	 * @return
	 */
	public static Object getCellValue(Cell cell) {
		//判断非空
		if (null == cell) {
			return null;
		}

		Object resultObj;
		//根据单元格类型，取值赋值
		switch (cell.getCellType()) {
			//空值
			case Cell.CELL_TYPE_BLANK:
				resultObj = null;
			break;

			//布尔类型
			case Cell.CELL_TYPE_BOOLEAN:
				resultObj = cell.getBooleanCellValue();
			break;

			//错误
			case Cell.CELL_TYPE_ERROR:
				resultObj = null;
			break;

			//公式类型
			case Cell.CELL_TYPE_FORMULA:
				Workbook wb = cell.getSheet().getWorkbook();
				CreationHelper crateHelper = wb.getCreationHelper();
				FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
				resultObj = getCellValue(evaluator.evaluateInCell(cell));
			break;

			//数值类型
			case Cell.CELL_TYPE_NUMERIC:
				//是否是日期
				if (DateUtil.isCellDateFormatted(cell)) {
					resultObj = cell.getDateCellValue();
				} else {
					resultObj = NumberToTextConverter.toText(cell.getNumericCellValue());
				}
			break;

			//字符串类型
			case Cell.CELL_TYPE_STRING:
				resultObj = cell.getRichStringCellValue().getString();
			break;

			//其他类型
			default:
				resultObj = null;
			break;
		}
		return resultObj;
	}

}
