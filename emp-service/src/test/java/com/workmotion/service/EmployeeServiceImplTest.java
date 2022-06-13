package com.workmotion.service;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.workmotion.common.exception.EMPException;
import com.workmotion.model.Employee;
import com.workmotion.mongo.repository.EmployeeRepository;
import com.workmotion.service.config.EMPFongoConfig;


@ActiveProfiles("fakemongo")
@ContextConfiguration(classes = {EMPFongoConfig.class})
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceImplTest {

	
	@Autowired
	private EmployeeServiceImpl employeeService;
	
	@Autowired 
	private EmployeeRepository repo;
	
	@BeforeEach
	public void setUp() {
		repo.deleteAll();
	}
	
	
	@Test
	public void findEmployeeByIdTest() throws EMPException {
		assertThrows(EMPException.class, () -> {
			employeeService.findEmployeeById(null);
	    });
		
		
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		employee1 = employeeService.createNewEmployee(employee1);
		
		final String id = employee1.getId();
		assertDoesNotThrow(() -> {
			employeeService.findEmployeeById(id);
	    });
		
		Employee employee2 = employeeService.findEmployeeById(id);
		
		assertEquals(employee1, employee2);
	}
	
	@Test
	public void findEmployeesByFirstNameTest() throws EMPException {
		
		assertEquals(employeeService.findEmployeesByFirstName("Bill").size(), 0);
		
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		employee1 = employeeService.createNewEmployee(employee1);
		
		
		Employee employee2 = employeeService.findEmployeesByFirstName("Bill").get(0);
		assertEquals(employee1, employee2);
	}
	
	@Test
	public void createNewEmployeeTest() throws EMPException {
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		employee1 = employeeService.createNewEmployee(employee1);
		
		Employee employee2 = employeeService.findEmployeeById(employee1.getId());
		assertEquals(employee1, employee2);
		
		EMPException exception = assertThrows(EMPException.class, () -> {
			employeeService.createNewEmployee(createEmployee("Bill", "Gate", 35));
	    });
		
		assertTrue(exception.getMessage().contains("Employee does already exist"));
	}
	
	@Test
	public void updateEmployeeTest() throws Exception {
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		employee1 = employeeService.createNewEmployee(employee1);
		
		employee1.setAge(45);
		
		employee1 = employeeService.updateEmployee(employee1);
		assertEquals(employee1.getAge(), Integer.valueOf(45));
	}
	
	
	
	private Employee createEmployee(String firstname, String lastname, Integer age) {
		Employee result = new Employee();
		result.setFirstName(firstname);
		result.setLastName(lastname);
		result.setAge(age);
		return result;
	}
	
}
