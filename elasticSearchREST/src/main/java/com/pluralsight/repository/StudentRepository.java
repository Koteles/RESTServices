package com.pluralsight.repository;

import java.util.List;

import com.model.Student;


public interface StudentRepository {

	List<Student> findAllStudents(String index, String type);

	Student findStudent(String index, String type, String studentId);
	
	List<Student> findStudentsByHeight(String index, String type, double from, double to);

	void addStudent(Student s, String index, String type);

	void deleteStudent(String index, String type, String studentId);
	

}