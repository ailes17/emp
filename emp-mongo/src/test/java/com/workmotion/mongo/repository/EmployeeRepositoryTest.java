package com.workmotion.mongo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.workmotion.model.Employee;
import com.workmotion.mongo.config.EMPFongoConfig;

@ActiveProfiles("fakemongo")
@ContextConfiguration(classes = {EMPFongoConfig.class})
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeRepositoryTest {
	
	
	@Autowired
	private EmployeeRepository employeeRepo;
	
	@BeforeEach
    public void setUp() {
		employeeRepo.deleteAll();
    }
	
	
	@Test
	public void findEmployeeByIdTest() {
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		employee1 = employeeRepo.save(employee1);
		
		assertEquals(employeeRepo.findEmployeeById(employee1.getId()), employee1);
	}
	
	
	@Test
	public void findEmployeeByFirstNameTest() {
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		employee1 = employeeRepo.save(employee1);
		
		assertEquals(employeeRepo.findEmployeeByFirstName("Bill").get(0), employee1);
		assertEquals(employeeRepo.findEmployeeByFirstName("Jeff").size(), 0);
	}
	
	
	@Test
	public void findEmployeeByFirstNameLastNameAgeTest() {
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		employee1 = employeeRepo.save(employee1);
		
		assertEquals(employeeRepo.findEmployeeByFirstNameLastNameAge("Bill", "Gate", 35).get(0), employee1);
		assertEquals(employeeRepo.findEmployeeByFirstNameLastNameAge("Jeff", "Bezos", 55).size(), 0);
	}
	
	
	
	
	
	private Employee createEmployee(String firstname, String lastname, Integer age) {
		Employee result = new Employee();
		result.setFirstName(firstname);
		result.setLastName(lastname);
		result.setAge(age);
		return result;
	}
}
