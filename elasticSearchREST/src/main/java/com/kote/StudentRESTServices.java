package com.kote;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.model.Student;
import com.pluralsight.repository.StudentRepository;

@Path("elasticsearch")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class StudentRESTServices {

	@Inject
	private StudentRepository studentRepository;

	@GET
	@Path("{index}/{type}")
	public List<Student> getAllStudents(@PathParam("index") String index, @PathParam("type") String type) {
		return studentRepository.findAllStudents(index, type);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{index}/{type}/{id}")
	public Response getStudent(@PathParam("index") String index, @PathParam("type") String type,
			@PathParam("id") String studentId) {

		if (studentId == null || studentId.length() > 3) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		Student student = studentRepository.findStudent(index, type, studentId);

		if (student == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		return Response.ok().entity(student).build();
	}
	
	@GET
	@Path("{index}/{type}" + "/height")
	public List<Student> getStudentByHeight(
		@PathParam("index") String index, @PathParam("type") String type,
		@QueryParam("from") double from,
		@QueryParam("to") double to) {

		System.out.println(from + " " + to);
		return studentRepository.findStudentsByHeight(index, type, from, to);

	}	

	@POST
	@Path("{index}/{type}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes(MediaType.APPLICATION_JSON)
	public Student createStudent(Student student, @PathParam("index") String index, @PathParam("type") String type) {

		studentRepository.addStudent(student, index, type);
		return student;
	}
	

	@DELETE
	@Path("{index}/{type}/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteStudent(@PathParam("index") String index, @PathParam("type") String type,
			@PathParam("id") String studentId) {

		if (studentId == null || studentId.length() > 3) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		studentRepository.deleteStudent(index, type, studentId);
		String message = " deleted successful";
		return Response.ok(message).build();
	}

}
