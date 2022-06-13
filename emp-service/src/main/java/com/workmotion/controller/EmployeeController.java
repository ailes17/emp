package com.workmotion.controller;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.workmotion.common.exception.EMPException;
import com.workmotion.model.Employee;
import com.workmotion.service.EmployeeService;

/**
 * Rest entry calls class
 * @author Adel
 *
 */
@RestController
@RequestMapping(path="workmotion", produces = "application/json")
public class EmployeeController {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);
	
	private EmployeeService employeeService;
	
	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	
	/**
	 * Rest method to get an Employee by ID 
	 * URL : /workmotion/employee/{id}
	 * @param id Employee ID
	 * @return Employee
	 * @throws IllegalArgumentException IllegalArgumentException
	 */
	@GetMapping("/employee/{id}")
    public @ResponseBody 
    ResponseEntity<?> getEmployeeById(@PathVariable String id) throws IllegalArgumentException {
		try {
            return new ResponseEntity<>(employeeService.findEmployeeById(id), HttpStatus.OK);
        } catch (EMPException e) {
            LOG.error(String.format("get employee by id failed ", id), e);
            return new ResponseEntity<>(Collections.singletonMap("response", e.getMessage()), HttpStatus.valueOf(e.getStatusCode()));
        }
    }
	
	
	/**
	 * Rest method to get an Employees by firstName
	 * if firstname is null or empty, returns all list of employees
	 * URL : /workmotion/employees
	 * @param firstName Employee first name as a request parameter
	 * @return List of Employees
	 * @throws IllegalArgumentException
	 */
	@GetMapping("/employees")
    public @ResponseBody 
    ResponseEntity<?> getEmployees(@RequestParam(required = false) String firstName) throws IllegalArgumentException {
		try {
            return new ResponseEntity<>(employeeService.findEmployeesByFirstName(firstName), HttpStatus.OK);
        } catch (EMPException e) {
            LOG.error(String.format("get employees failed ", firstName), e);
            return new ResponseEntity<>(Collections.singletonMap("response", e.getMessage()), HttpStatus.valueOf(e.getStatusCode()));
        }
    }
	
	
	/**
	 * Create a new employee
	 * @param employee Employee to be created
	 * @return Employee created
	 * @throws IllegalArgumentException
	 */
	@PostMapping("/employee")
    public @ResponseBody
    ResponseEntity<?> postEmployee(@RequestBody Employee employee) throws IllegalArgumentException {
        try {
            return new ResponseEntity<>(employeeService.createNewEmployee(employee), HttpStatus.CREATED);
        } catch (EMPException e) {
            LOG.error(String.format("Adding new employee failed ", employee), e);
            return new ResponseEntity<>(Collections.singletonMap("response", e.getMessage()), HttpStatus.valueOf(e.getStatusCode()));
        }
    }
	
	
	/**
	 * Update an employee
	 * @param employee Employee to update
	 * @return Employee updated
	 * @throws IllegalArgumentException
	 */
	@PutMapping("/employee")
    public @ResponseBody
    ResponseEntity<?> updateEmployee(@RequestBody Employee employee) throws IllegalArgumentException {
        try {
            return new ResponseEntity<>(employeeService.updateEmployee(employee), HttpStatus.OK);
        } catch (EMPException e) {
            LOG.error(String.format("Updating employee failed ", employee), e);
            return new ResponseEntity<>(Collections.singletonMap("response", e.getMessage()), HttpStatus.valueOf(e.getStatusCode()));
        } catch (Exception e) {
        	LOG.error(String.format("Updating employee failed ", employee), e);
            return new ResponseEntity<>(Collections.singletonMap("response", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
