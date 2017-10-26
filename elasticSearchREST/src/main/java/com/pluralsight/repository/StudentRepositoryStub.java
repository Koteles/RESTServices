package com.pluralsight.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

public class StudentRepositoryStub implements StudentRepository {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private Student student;
	
	@Inject
	private Logger logger;
	
	@Inject
	private Client client;

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
