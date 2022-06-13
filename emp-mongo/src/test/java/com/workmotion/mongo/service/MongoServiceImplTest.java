package com.workmotion.mongo.service;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.workmotion.common.exception.EMPMongoException;
import com.workmotion.model.Employee;
import com.workmotion.model.GlobalState;
import com.workmotion.mongo.config.EMPFongoConfig;
import com.workmotion.mongo.repository.EmployeeRepository;

@ActiveProfiles("fakemongo")
@ContextConfiguration(classes = {EMPFongoConfig.class})
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoServiceImplTest {
	
	@Autowired 
	private MongoService mongoService;
	
	@Autowired 
	private EmployeeRepository repo;
	
	@BeforeEach
    public void setUp() {
		repo.deleteAll();
    }
	
	@Test
	public void addEmployeeTest() throws EMPMongoException {
		List<Employee> employees = repo.findAll();
		Assert.assertEquals(employees.size(), 0);
		
		
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		mongoService.addEmployee(employee1);
		
		employees = repo.findAll();
		Assert.assertEquals(employees.size(), 1);
		Assert.assertEquals(employees.get(0).getFirstName(), "Bill");
		Assert.assertEquals(employees.get(0).getLastName(), "Gate");
		Assert.assertEquals(employees.get(0).getAge(), Integer.valueOf(35));
	}
	
	@Test
	public void doesEmployeeExistTest() throws EMPMongoException {
		
		Assert.assertFalse(mongoService.doesEmployeeExist("Bill", "Gate", 35));
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		mongoService.addEmployee(employee1);
		Assert.assertTrue(mongoService.doesEmployeeExist("Bill", "Gate", 35));
	}
	
	
	@Test
	public void findEmployeeByIdTest() throws EMPMongoException {
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		employee1 = mongoService.addEmployee(employee1);
		
		Employee employee2 = mongoService.findEmployeeById(employee1.getId());
		
		Assert.assertEquals(employee1, employee2);
	}
	
	@Test
	public void findEmployeesByfirstNameTest() throws EMPMongoException {
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		employee1 = mongoService.addEmployee(employee1);
		Employee employee2 = createEmployee("Jeff", "Bezos", 55);
		employee2 = mongoService.addEmployee(employee2);
		
		Assert.assertEquals(mongoService.findEmployeesByfirstName("Bill").size(), 1);
		Assert.assertEquals(mongoService.findEmployeesByfirstName(null).size(), 2);
	}
	
	@Test
	public void updateEmployeeTest() throws EMPMongoException {
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		employee1 = mongoService.addEmployee(employee1);
		
		employee1.setAge(45);
		mongoService.updateEmployee(employee1);
		
		Employee employee2 = mongoService.findEmployeeById(employee1.getId());
		Assert.assertEquals(employee2.getAge(), Integer.valueOf(45));
	}
	
	@Test
	public void findAndLockEmployeeTest() throws EMPMongoException {
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		employee1.setGlobalState(GlobalState.ADDED);
		employee1 = mongoService.addEmployee(employee1);
		
		mongoService.findAndLockEmployee(employee1.getId());
		Employee employee2 = mongoService.findEmployeeById(employee1.getId());
		
		Assert.assertEquals(employee2.getGlobalState(), GlobalState.PENDING);
	}
	
	@Test
	public void findAndUnLockEmployeeTest() throws Exception {
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		employee1.setGlobalState(GlobalState.ADDED);
		employee1 = mongoService.addEmployee(employee1);
		
		mongoService.findAndLockEmployee(employee1.getId());
		Employee employee2 = mongoService.findEmployeeById(employee1.getId());
		
		Assert.assertEquals(employee2.getGlobalState(), GlobalState.PENDING);
		mongoService.findAndUnLockEmployee(employee2.getId(), GlobalState.ADDED);
		
		Employee employee3 = mongoService.findEmployeeById(employee2.getId());
		Assert.assertEquals(employee3.getGlobalState(), GlobalState.ADDED);
	}
	
	
	private Employee createEmployee(String firstname, String lastname, Integer age) {
		Employee result = new Employee();
		result.setFirstName(firstname);
		result.setLastName(lastname);
		result.setAge(age);
		return result;
	}
}
