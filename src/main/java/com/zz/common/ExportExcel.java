package com.zz.common;


import com.google.common.collect.Lists;
import com.zz.annotation.ExcelField;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * 导出Excel文件（导出“XLSX”格式，支持大数据量导出   @see org.apache.poi.ss.SpreadsheetVersion）
 * @author zhangzheng
 */
public class ExportExcel {

	private static final Logger log = LoggerFactory.getLogger(ExportExcel.class);
			
	/**
	 * 工作薄对象
	 */
	private XSSFWorkbook wb;
	
	/**
	 * 工作表对象
	 */
	private Sheet sheet;
	
	/**
	 * 样式列表
	 */
	private Map<String, CellStyle> styles;
	
	/**
	 * 当前行号
	 */
	private int rownum;
	
	/**
	 * 注解列表（Object[]{ ExcelField, Field/Method }）
	 */
	List<Object[]> annotationList = Lists.newArrayList();

	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param cls 实体对象，通过annotation.ExportField获取标题
	 */
	public ExportExcel(String title, Class<?> cls){
		// Get annotation field 
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs){
			ExcelField ef = f.getAnnotation(ExcelField.class);
			if (ef != null){
					annotationList.add(new Object[]{ef, f});
			}
		}
		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms){
			ExcelField ef = m.getAnnotation(ExcelField.class);
			if (ef != null){
					annotationList.add(new Object[]{ef, m});
			}
		}
		// 列数据排序
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});
		// Initialize
		List<String> headerList = Lists.newArrayList();
		for (Object[] os : annotationList){
			String t = ((ExcelField)os[0]).title();
			headerList.add(t);
		}
		initialize(title, headerList);
	}

	public ExportExcel(FileInputStream is, Class<?> cls) throws IOException{
		// Get annotation field
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs){
			ExcelField ef = f.getAnnotation(ExcelField.class);
			if (ef != null){
				annotationList.add(new Object[]{ef, f});
			}
		}
		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms){
			ExcelField ef = m.getAnnotation(ExcelField.class);
			if (ef != null){
				annotationList.add(new Object[]{ef, m});
			}
		}
		// 列数据排序
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});

		initialize(is);

//		setDataList(list, startRow);
	}
	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headers 表头数组
	 */
	public ExportExcel(String title, String[] headers) {
		initialize(title, Lists.newArrayList(headers));
	}
	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headerList 表头列表
	 */
	public ExportExcel(String title, List<String> headerList) {
		initialize(title, headerList);
	}
	
	/**
	 * 初始化函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headerList 表头列表
	 */
	private void initialize(String title, List<String> headerList) {
		this.wb = new XSSFWorkbook();
		this.sheet = wb.createSheet(title);
		this.styles = createStyles(wb);
		// 处理标题
		if (StringUtils.isNotBlank(title)){
			Row titleRow = sheet.createRow(rownum++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));  //标题样式
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
					titleRow.getRowNum(), titleRow.getRowNum(), headerList.size()-1));
		}
		// 处理头部
		if (headerList == null){
			throw new RuntimeException("excel头部List不能为空");
		}
		Row headerRow = sheet.createRow(rownum++);
		headerRow.setHeightInPoints(16);
		for (int i = 0; i < headerList.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			cell.setCellValue(headerList.get(i));
			sheet.autoSizeColumn(i);
		}
		for (int i = 0; i < headerList.size(); i++) {  
			int colWidth = sheet.getColumnWidth(i)*2;
	        sheet.setColumnWidth(i, colWidth < 4000 ? 4000 : colWidth);
		}
	}

	private void initialize(FileInputStream is) throws IOException{
		this.wb = new XSSFWorkbook(is);
		this.sheet = wb.getSheetAt(0);
		this.styles = createStyles(wb);
	}
	
	/**
	 * 创建表格样式
	 * @param wb 工作薄对象
	 * @return 样式列表
	 */
	private Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font titleFont = wb.createFont();
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(titleFont);
		styles.put("title", style);

		style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);
		style.setFont(dataFont);
		styles.put("data", style);
		
		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_LEFT);
		styles.put("data1", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_CENTER);
		styles.put("data2", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		styles.put("data3", style);
		
		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setWrapText(true);//设置自动换行
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(headerFont);
		styles.put("header", style);
		
		return styles;
	}

	/**
	 * 添加一行
	 * @return 行对象
	 */
	private Row addRow(){
		return sheet.createRow(rownum++);
	}
	

	/**
	 * 添加一个单元格
	 * @param row 添加的行
	 * @param column 添加列号
	 * @param val 添加值
	 * @return 单元格对象
	 */
//	public Cell addCell(Row row, int column, Object val){
//		return this.addCell(row, column, val, 1, Class.class);
//	}
	
	/**
	 * 添加一个单元格
	 * @param row 添加的行
	 * @param column 添加列号
	 * @param val 添加值
	 * @param align 对齐方式（1：靠左；2：居中；3：靠右）
	 * @return 单元格对象
	 */
	private Cell addCell(Row row, int column, Object val, int align, String dtFormat, String cellFormat, Class<?> fieldType){
		Cell cell = row.createCell(column);
		String cellFormatString = "@";
		try {
			if(val == null){
				cell.setCellValue("");
			}else if(fieldType != Class.class){
				cell.setCellValue((String)fieldType.getMethod("setValue", Object.class).invoke(null, val));
			}else{
				if(val instanceof String) {
					cell.setCellValue((String) val);
				}else if(val instanceof Short) {
					cell.setCellValue((Short) val);
					cellFormatString = "0";
				}else if(val instanceof Integer) {
					cell.setCellValue((Integer) val);
					cellFormatString = "0";
				}else if(val instanceof Long) {
					cell.setCellValue((Long) val);
					cellFormatString = "0";
				}else if(val instanceof Double) {
					cell.setCellValue((Double) val);
					cellFormatString = "0.00";
				}else if(val instanceof Float) {
					cell.setCellValue((Float) val);
					cellFormatString = "0.00";
				}else if(val instanceof BigDecimal) {
					cell.setCellValue(((BigDecimal) val).doubleValue());
					cellFormatString = "#,##0.00";
				}else if(val instanceof Date) {
					if("yyyy-MM-dd".equals(dtFormat)){
						SimpleDateFormat sdf= new SimpleDateFormat(dtFormat);
						String valStr=sdf.format(val);
						cell.setCellValue(valStr);
					}else{
						cell.setCellValue((Date) val);
						cellFormatString = dtFormat;
					}
				}else if(val instanceof Collection){
					StringBuilder stringBuilder = new StringBuilder();
					if(((Collection)val).size()>0){
						for(String item:(Collection<String>)val){
							stringBuilder.append(item);
							stringBuilder.append("|");
						}
					}
					if(stringBuilder.length()>0){
						stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("|"));
					}
					String value = stringBuilder.toString();
					cell.setCellValue(value);
				}else {
					cell.setCellValue((String)Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(), 
						"fieldtype."+val.getClass().getSimpleName()+"Type")).getMethod("setValue", Object.class).invoke(null, val));
				}
			}

			if(StringUtils.isEmpty(cellFormat)){
				// 如果未设置单元格样式，则使用默认样式
				cellFormat = cellFormatString;
			}
			cell.setCellStyle(getCellStyle(column, align, cellFormat));
		} catch (Exception ex) {
			log.info("Set cell value ["+row.getRowNum()+","+column+"] error: " + ex.toString());
			cell.setCellValue("");
		}
		return cell;
	}

	/**
	 * 创建单元格样式
	 * @param column
	 * @param align
	 * @param cellFormatString
	 * @return
	 */
	private CellStyle getCellStyle(int column, int align, String cellFormatString){
		CellStyle style = styles.get("data_column_" + column);
		if (style == null) {
			style = wb.createCellStyle();
			style.cloneStyleFrom(styles.get("data" + (align >= 1 && align <= 3 ? align : "")));
			style.setDataFormat(wb.createDataFormat().getFormat(cellFormatString));
			styles.put("data_column_" + column, style);
		}
		return style;
	}

	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * @return list 数据列表
	 */
	public <E> ExportExcel setDataList(List<E> list){
		for (E e : list){
			int colunm = 0;
			Row row = this.addRow();
			StringBuilder sb = new StringBuilder();
			for (Object[] os : annotationList){
				ExcelField ef = (ExcelField)os[0];
                Object val = getCellValue(ef, os, e);
				this.addCell(row, colunm++, val, ef.align(), ef.dtFormat(), ef.cellFormat(), ef.fieldType());
				sb.append(val + ", ");
			}
		}
		return this;
	}

    /**
     * 获取需要填入到cell中的数据
     * @param ef
     * @param os
     * @param e
     * @param <E>
     * @return
     */
	private <E> Object getCellValue(ExcelField ef, Object[] os, E e){
	    Object val = null;
        try {
            if (StringUtils.isNotBlank(ef.value())) {
                val = Reflections.invokeGetter(e, ef.value());
            } else {
                if (os[1] instanceof Field) {
                    val = Reflections.invokeGetter(e, ((Field) os[1]).getName());
                } else if (os[1] instanceof Method) {
                    val = Reflections.invokeMethod(e, ((Method) os[1]).getName(), new Class[]{}, new Object[]{});
                }
            }


        } catch (Exception ex) {
            // Failure to ignore

            val = "";
        }

        return val;
    }

    /**
     * 从指定行开始插入数据，插入行数为数据量
     * @param list
     * @param startRow
     * @param <E>
     * @return
     */
	public <E> ExportExcel setDataList(List<E> list, int startRow){
		if(null == list || list.isEmpty()){
			return this;
		}

		int rowIndex = startRow;
		// 根据数据数量在模板指定行进行插入
		sheet.shiftRows(startRow, sheet.getLastRowNum(),list.size(),true,false);
        for (E e : list) {
            int colunm = 0;
			Row row = sheet.createRow(rowIndex);

            StringBuilder sb = new StringBuilder();
            for (Object[] os : annotationList) {
                ExcelField ef = (ExcelField) os[0];
                Object val = getCellValue(ef, os, e);
				addCell(row, colunm, val, ef.align(), ef.dtFormat(), ef.cellFormat(), ef.fieldType());
				sb.append(val + ", ");
				colunm++;
			}
			rowIndex++;

        }
        return this;
	}
	
	/**
	 * 输出数据流
	 * @param os 输出数据流
	 */
	public ExportExcel write(OutputStream os) throws IOException{
		wb.write(os);
		return this;
	}
	
	/**
	 * 输出到客户端
	 * @param fileName 输出文件名
	 */
	public ExportExcel write(HttpServletResponse response, String fileName) throws IOException{
		response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename="+fileName);
		write(response.getOutputStream());
		return this;
	}
	
	/**
	 * 输出到文件
	 * @param name 输出文件名
	 */
	public ExportExcel writeFile(String name) throws FileNotFoundException, IOException{
		FileOutputStream os = new FileOutputStream(name);
		this.write(os);
		return this;
	}

	/**
	 *	将文件写入 temp 目录，并插入附件表，返回下载url
	 * @param mdk 目录
	 * @param fileName 文件名不带目录
	 * @return 返回下载url
	 * @throws FileNotFoundException
     * @throws IOException
     */
	public String  writeFile(String mdk,String fileName) throws  IOException{
		SimpleDateFormat sdf0= new SimpleDateFormat("yyyyMMddHHmmss");
		String fileNmDateStr = sdf0.format(new Date());
		fileName= fileName + "_" + fileNmDateStr + ".xlsx";
		SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");
		String dateString = sdf.format(new Date());
		 mdk = mdk+File.separator+dateString;
		File mdkDirecotry = new File(mdk);
		if (!mdkDirecotry.exists() || !mdkDirecotry.isDirectory()) {
			mdkDirecotry.mkdirs();
		}
		String realFilePath = mdk+File.separator+fileName;
		FileOutputStream os = new FileOutputStream(realFilePath);
		this.write(os);
		return fileName;
	}

	/**
	 * 将workbook对象转化为输入流：过程是利用ByteArrayOutputStream为缓存，在将此ByteArrayOutputStream转化为InputStream
	 * 利用到了ByteArrayOutputStream来做缓存，先将文件写入其中，然后将其转为字节数组，最后利用ByteArrayInputStream转为输入流，供后续使用
	 * @return
	 */
	public  InputStream getExcelStream() {
		InputStream in = null;
		try
		{
			//临时
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			//workbook转换为ByteArrayOutputStream
			this.wb.write(out);
			byte [] bookByteAry = out.toByteArray();
			in = new ByteArrayInputStream(bookByteAry);
		}
		catch (Exception e)
		{
			System.out.println("workbook转换为ByteArrayOutputStream失败");
		}
		return in;
	}

	public ResponseEntity<Resource> getResponseEntity(){
		Resource resource  = new InputStreamResource(this.getExcelStream());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add("charset", "utf-8");
		headers.add("Content-Disposition", "attachement;filename*=UTF-8''");
		return ResponseEntity.ok().headers(headers)
				.contentType(MediaType.parseMediaType("application/x-msdownload"))
				.body(resource);
	}
	

	
	/**
	 * 导出测试
	 */
//	public static void main(String[] args) throws Throwable {
//
//		List<String> headerList = Lists.newArrayList();
//		for (int i = 1; i <= 10; i++) {
//			headerList.add("表头"+i);
//		}
//
//		List<Object> dataRowList = Lists.newArrayList();
//		for (int i = 1; i <= headerList.size(); i++) {
//			//dataRowList.add("数据"+i);
//			dataRowList.add(new Date());
//		}
//
//		List<List<Object>> dataList = Lists.newArrayList();
//		for (int i = 1; i <=100; i++) {
//			dataList.add(dataRowList);
//		}
//
//		ExportExcel ee = new ExportExcel("表格标题", headerList);
//
//		for (int i = 0; i < dataList.size(); i++) {
//			Row row = ee.addRow();
//			for (int j = 0; j < dataList.get(i).size(); j++) {
//				ee.addCell(row, j, dataList.get(i).get(j));
//			}
//		}
//
//		ee.writeFile("target/export.xlsx");
//
//
//		log.debug("Export success.");
//
//	}

}
