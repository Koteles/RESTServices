package com.pluralsight.repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.ZipPath;
import com.services.ExcelReading;

public class ZipFileRepositoryStub implements ZipFileRepository{

	@Inject
	private ExcelReading reading;
	
	public String getZipFileContent(ZipPath pathToZip) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Map<String, List<List<String>>> myMap = new LinkedHashMap<String, List<List<String>>>();
		File file = new File(pathToZip.getPathToFile());
		
		try {
			ZipFile zipFile = new ZipFile(file);
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {

				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				if(!zipEntry.isDirectory()) {
				InputStream stream = zipFile.getInputStream(zipEntry);
				String fileName = zipEntry.getName();
				myMap.put(fileName, reading.readContentOfExcel(stream));
				}
			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(myMap);
		String json = null;
		try {
			json = objectMapper.writeValueAsString(myMap);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return json;
	}
}
