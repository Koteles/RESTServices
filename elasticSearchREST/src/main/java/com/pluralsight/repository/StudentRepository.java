package com.pluralsight.repository;

import java.util.List;
import java.util.Map;

import com.model.Student;
import com.model.ZipPath;

public interface StudentRepository {

	List<Student> findAllStudents(String index, String type);

	Student findStudent(String index, String type, String studentId);

	void addStudent(Student s, String index, String type);

	void deleteStudent(String index, String type, String studentId);
	
	String getZipFileContent(ZipPath pathToZip);

}