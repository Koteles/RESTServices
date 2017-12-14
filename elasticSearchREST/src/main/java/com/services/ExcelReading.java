package com.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReading {

	public List<List<String>> readContentOfExcel(InputStream inputFile) {

		List<String> list = new ArrayList<String>();

		List<List<String>> group = new ArrayList<List<String>>();

		try {

			Sheet sheet = null;
			Workbook wBook = WorkbookFactory.create(inputFile); 
			
			int numberOfSheets = 0;
	
			numberOfSheets = wBook.getNumberOfSheets();

			for (int i = 0; i < numberOfSheets; i++) {
				sheet = wBook.getSheetAt(i);
				
				list.add("The sheet " + sheet.getSheetName() + " contains:");
				group.add(new ArrayList<String>(list));
				list.clear();

				Row row;
				Cell cell;
				// Iterate through each rows from first sheet

				Iterator<Row> rowIterator = sheet.iterator();

				while (rowIterator.hasNext()) {
					row = rowIterator.next();

					// For each row, iterate through each columns
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {

						cell = cellIterator.next();

						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_BOOLEAN:
							list.add(cell.getBooleanCellValue() + ",");
							break;
						case Cell.CELL_TYPE_NUMERIC:
							list.add(cell.getNumericCellValue() + ",");
							break;
						case Cell.CELL_TYPE_STRING:
							list.add(cell.getStringCellValue() + ",");
							break;

						case Cell.CELL_TYPE_BLANK:
							list.add("" + ",");
							break;
						default:
							list.add(cell + ",");

						}

					}
					// removing the last comma
					String last = list.get(list.size() - 1);
					int index = last.lastIndexOf(",");
					last = new StringBuilder(last).replace(index, index + 1, "").toString();

					list.set(list.size() - 1, last);

					group.add(new ArrayList<String>(list));
					list.clear();

				}

			}
		}	catch(InvalidFormatException e) {
			
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return group;
	}

}
