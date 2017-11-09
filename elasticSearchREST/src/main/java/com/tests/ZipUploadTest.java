package com.tests;
 
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.model.ZipPath;
import com.pluralsight.repository.ExcelReading;
import com.pluralsight.repository.StudentRepositoryStub;

@RunWith(MockitoJUnitRunner.class)
public class ZipUploadTest {

	@Mock
	ExcelReading reading;
	
	@InjectMocks
	private StudentRepositoryStub obj;	
	
	@Mock
	ZipPath pathToZip;

	@Test
	public void zipUploadFileTest() {
		
		String path = "D:\\Upload\\excel_zip\\2sheets.zip";
		
		when(pathToZip.getPathToFile()).thenReturn(path);
		
		List<List<String>> group = new ArrayList<List<String>>();

		when(reading.readContentOfExcel(any(InputStream.class))).thenReturn(group);
		
		assertEquals("JSON String is null", "{\"2sheets.xlsx\":[]}", obj.getZipFileContent(pathToZip));
		assertNotNull(obj.getZipFileContent(pathToZip));
	}

}
