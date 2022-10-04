package com.develhope.CRUDtest;

import com.develhope.CRUDtest.controllers.StudentController;
import com.develhope.CRUDtest.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class StudentControllerTest {

	@Autowired
	private StudentController studentController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void studentControllerLoads() {
		assertThat(studentController).isNotNull();
	}

	private Student getStudentFromId(Long id) throws Exception{
		MvcResult mvcResult = this.mockMvc.perform(get("/students/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		try {
			String studentJSON = mvcResult.getResponse().getContentAsString();
			Student student = objectMapper.readValue(studentJSON, Student.class);

			assertThat(student).isNotNull();
			assertThat(student.getId()).isNotNull();

			return student;
		}catch (Exception e){
			return null;
		}
	}

	private Student createAStudent() throws Exception {
		Student student = new Student();
		student.setName("Mattia");
		student.setSurname("Oleandro");
		student.setWorking(true);
		return createAStudent(student);
	}

	private Student createAStudent(Student student) throws Exception {
		MvcResult mvcResult = createAStudentRequest(student);
		Student studentFromResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Student.class);

		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isNotNull();

		return studentFromResponse;
	}

	private MvcResult createAStudentRequest() throws Exception {
		Student student = new Student();
		student.setName("Mattia");
		student.setSurname("Oleandro");
		student.setWorking(true);
		return createAStudentRequest(student);
	}

	private MvcResult createAStudentRequest(Student student) throws Exception {
		if(student == null) return null;
		String studentJSON = objectMapper.writeValueAsString(student);
		return this.mockMvc.perform(post("/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	void createAStudentTest() throws Exception {
		Student studentFromResponse = createAStudent();
	}

	@Test
	void readStudentsList() throws Exception {
		createAStudentRequest();

		MvcResult mvcResult =this.mockMvc.perform(get("/students/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<Student> studentsFromResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
		System.out.println("Students in database are: " + studentsFromResponse.size());
		assertThat(studentsFromResponse.size()).isNotZero();
	}

	@Test
	void readSingleStudent() throws Exception {
		Student student = createAStudent();
		Student studentFromResponse = getStudentFromId(student.getId());
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
	}

	@Test
	void updateStudent() throws Exception{
		Student student = createAStudent();

		String newName = "Billie";
		String newSurname = "Banks";
		student.setName(newName);
		student.setSurname(newSurname);

		String studentJSON = objectMapper.writeValueAsString(student);

		MvcResult mvcResult = this.mockMvc.perform(put("/students/"+student.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();

		Student studentFromResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponse.getName()).isEqualTo(newName);

		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponseGet.getName()).isEqualTo(newName);
	}

	@Test
	void deleteStudent() throws Exception{
		Student student = createAStudent();
		assertThat(student.getId()).isNotNull();

		this.mockMvc.perform(delete("/students/"+student.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	void activateStudent() throws Exception{
		Student student = createAStudent();
		assertThat(student.getId()).isNotNull();

		MvcResult mvcResult = this.mockMvc.perform(put("/students/"+student.getId()+"/working?working=true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponse.isWorking()).isEqualTo(true);

		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet).isNotNull();
		assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponseGet.isWorking()).isEqualTo(true);
	}

}
