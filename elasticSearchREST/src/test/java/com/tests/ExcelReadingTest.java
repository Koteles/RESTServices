package com.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.junit.Test;

import com.services.ExcelReading;


public class ExcelReadingTest {

	ExcelReading reading = mock(ExcelReading.class);
	
	private Map<String, List<List<String>>> myMap = new LinkedHashMap<String, List<List<String>>>();
	
	@Test
	public void zipMapTest() {	
		
		//when(reading.readContentOfExcel()).thenReturn();
		
		int numberOfExcelFiles = 0;
		File zipFile = new File("D:\\Upload\\excel_zip\\basic3.zip");
		ZipFile zip = null;
		String fileName = null;
		try {
			zip = new ZipFile(zipFile);

			Enumeration<?> enu = zip.entries();
			while (enu.hasMoreElements()) {

				ZipEntry zipEntry = (ZipEntry) enu.nextElement();

				InputStream stream = zip.getInputStream(zipEntry);
				fileName = zipEntry.getName();
				
				myMap.put(fileName, reading.readContentOfExcel(stream));
				
				numberOfExcelFiles++;

			}
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals("Number of excel files in the zip is not correct", numberOfExcelFiles, myMap.size());
		assertEquals(null, myMap.get("Markus"));
		assertEquals(true, myMap.containsKey(fileName));
	

	}
	
}
