package com.bibliofreaks.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
public class XlsDemo {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        LoadProperties.load();
		Workbook wb = new HSSFWorkbook();
		Sheet products = wb.createSheet("products");
		CreationHelper createHelper = wb.getCreationHelper();
	    FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(LoadProperties.Prop("SHOP_2_GRAB_FEED_PATH")+".xls");
			Row row = products.createRow(1);
			products.getTopRow();
			
			row.createCell(0).setCellValue(1.2);
		    row.createCell(1).setCellValue(
		         createHelper.createRichTextString("This is a string"));
		    row.createCell(2).setCellValue(true);
		    CellStyle cellStyle = wb.createCellStyle();
		    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(("m/d/yy h:mm")));
		    Cell cell  = row.createCell(3);
		    cell.setCellValue(new Date());
		    cell.setCellStyle(cellStyle);
		    
		    Sheet bookdetails = wb.getSheet("products");
		    for (Row horizontalrow : bookdetails) {
		      for (Cell cel : horizontalrow) {
		        // Do something here
		    	  
		      }
		    }

			try {
				wb.write(fileOut);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				fileOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    	   
	}

}
