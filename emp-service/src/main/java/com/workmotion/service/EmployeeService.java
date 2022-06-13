package com.workmotion.service;

import java.util.List;

import com.workmotion.common.exception.EMPException;
import com.workmotion.model.Employee;

public interface EmployeeService {

	
	/**
	 * Method service to create a new Employee
	 * @param employee Employee to create
	 * @return Employee created
	 * @throws EMPException EMPException
	 */
	Employee createNewEmployee(Employee employee) throws EMPException;

	
	/**
	 * Method service to find an employee by ID
	 * @param id Employee ID
	 * @return Employee
	 * @throws EMPException EMPException
	 */
	Employee findEmployeeById(String id) throws EMPException;

	
	/**
	 * Find employees by first name
	 * if first name is empty, return the list of all employees 
	 * @param firstName Employee first name
	 * @return List of Employee
	 * @throws EMPException EMPException
	 */
	List<Employee> findEmployeesByFirstName(String firstName) throws EMPException;

	
	/**
	 * Method service to Update an employee
	 * @param employee Employee to update
	 * @return Employee updated
	 * @throws EMPException EMPException
	 * @throws Exception Exception
	 */
	Employee updateEmployee(Employee employee) throws EMPException, Exception;

}
