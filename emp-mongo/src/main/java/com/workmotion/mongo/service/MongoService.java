package com.workmotion.mongo.service;

import java.util.List;

import com.workmotion.common.exception.EMPMongoException;
import com.workmotion.model.Employee;
import com.workmotion.model.GlobalState;

public interface MongoService {

	/**
	 * Method to create a new employee Entity
	 * @param employee Employee entity to be added
	 * @return Employee entity created
	 * @throws EMPMongoException EMPMongoException
	 */
	public Employee addEmployee(Employee employee) throws EMPMongoException;
	
	
	/**
	 * Update an employee entity in database
	 * @param employee Employee entity to be updated
	 * @return Employee updated
	 * @throws EMPMongoException EMPMongoException
	 */
	public Employee updateEmployee(Employee employee) throws EMPMongoException;
	
	
	/**
	 * Check if the employee does exist in the database using FirstName, LastName and Age
	 * @param firstname Employee first name
	 * @param lastname Employee last name
	 * @param age Employee age
	 * @return true or false
	 * @throws EMPMongoException EMPMongoException
	 */
	public boolean doesEmployeeExist(String firstname, String lastname, Integer age) throws EMPMongoException;
	
	
	/**
	 * Find an employee with his ID
	 * @param id Employee ID
	 * @return Employee or null
	 * @throws EMPMongoException EMPMongoException
	 */
	public Employee findEmployeeById(String id) throws EMPMongoException;
	
	
	/**
	 * Find employees by their first name
	 * @param firstName Employees first name
	 * @return List of Employee
	 * @throws EMPMongoException EMPMongoException
	 */
	public List<Employee> findEmployeesByfirstName(String firstName) throws EMPMongoException;

	
	/**
	 * Find an employee by his ID and lock the resource (by changing his globalState to PENDING)
	 * @param id Employee ID
	 * @return Employee
	 * @throws EMPMongoException EMPMongoException
	 */
	public Employee findAndLockEmployee(String id) throws EMPMongoException;

	
	/**
	 * Find an employee by ID and Unlock his state (by changing his globalState to his previous one)
	 * @param id Employee Id
	 * @param previousState the previous GlobalState before lock
	 * @return Employee
	 * @throws Exception Exception
	 */
	public Employee findAndUnLockEmployee(String id, GlobalState previousState) throws Exception;
}
