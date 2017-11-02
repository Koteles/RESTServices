package com.pluralsight.repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.inject.Inject;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.model.Student;
import com.model.ZipPath;

public class StudentRepositoryStub implements StudentRepository {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private Student student;
	
	@Inject
	private Logger logger;
	
	@Inject
	private Client client;
	
	@Inject
	private ExcelReading reading;

	public List<Student> findAllStudents(String index, String type) {

		final int scrollSize = 1000;
		SearchResponse response = null;
		int i = 0;
		List<Student> students = new ArrayList<Student>();
		final SearchRequestBuilder addSort = client.prepareSearch(index).setTypes(type)
				.setQuery(QueryBuilders.matchAllQuery()).setSize(scrollSize).addSort("id", SortOrder.ASC);

		while (response == null || response.getHits().hits().length != 0) {

			response = addSort.setFrom(i * scrollSize).get();

			for (SearchHit hit : response.getHits()) {

				try {
					student = objectMapper.readValue(hit.getSourceAsString(), Student.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				students.add(student);
			}
			i++;
		}
		logger.info("Getting all students");
		return students;
	}

	public Student findStudent(String index, String type, String studentId) {

		final GetResponse response = client.prepareGet(index, type, studentId).get();

		final String search = response.getSourceAsString();

		try {
			student = objectMapper.readValue(search, Student.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.info("Getting student with ID " + studentId);
		return student;

	}

	public String getZipFileContent(ZipPath pathToZip) {
		
		Map<String, List<List<String>>> myMap = new LinkedHashMap<String, List<List<String>>>();
		File file = new File(pathToZip.getPathToFile());
		
		try {
			ZipFile zipFile = new ZipFile(file);
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {

				ZipEntry zipEntry = (ZipEntry) enu.nextElement();

				InputStream stream = zipFile.getInputStream(zipEntry);
				String fileName = zipEntry.getName();
				myMap.put(fileName, reading.xlsx(stream, fileName));

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
	
	public void addStudent(Student student, String index, String type) {

		final String id = student.getId();
		String json = null;

		try {
			json = objectMapper.writeValueAsString(student);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		client.prepareIndex(index, type, id).setSource(json).get();
		logger.info("Adding student " + json);
		
	}

	public void deleteStudent(String index, String type, String studentId) {

		client.prepareDelete(index, type, studentId).get();
		logger.info("Deleting student with ID " + studentId);
	}
}
