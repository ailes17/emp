package com.workmotion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.workmotion.common.exception.EMPException;
import com.workmotion.common.exception.EMPMongoException;
import com.workmotion.common.helper.EmployeeCheckHelper;
import com.workmotion.model.Employee;
import com.workmotion.model.GlobalState;
import com.workmotion.mongo.service.MongoService;

/**
 * Employee Service class to handle Rest services 
 * @author Adel
 *
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

	private MongoService mongoService;
	
	@Autowired
	public EmployeeServiceImpl(MongoService mongoService) {
		this.mongoService = mongoService;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Employee findEmployeeById(String id) throws EMPException {
		
		if (Strings.isNullOrEmpty(id)) {
			throw new EMPException("Employee Id can't be null or empty", HttpStatus.BAD_REQUEST.value());
		}
		
		Employee result = null;
		
		try {
			result = mongoService.findEmployeeById(id);
		} catch (EMPMongoException e) {
			throw new EMPException("Error when trying to fetch from DB", HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		
		return result;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Employee> findEmployeesByFirstName(String firstName) throws EMPException {
		
		List<Employee> result = null;
		
		try {
			result = mongoService.findEmployeesByfirstName(firstName);
		} catch (EMPMongoException e) {
			throw new EMPException("Error when trying to fetch from DB", HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		
		return result;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Employee createNewEmployee(Employee employee) throws EMPException {
		EmployeeCheckHelper.checkEmployeeValues(employee);
		
		Employee result = null;
		
		try {
			if (mongoService.doesEmployeeExist(employee.getFirstName(), employee.getLastName(), employee.getAge())) {
				throw new EMPException("Employee does already exist", HttpStatus.NOT_FOUND.value());
			}
			
			EmployeeCheckHelper.initializeState(employee);
			
			result = mongoService.addEmployee(employee);
		} catch (EMPMongoException e) {
			throw new EMPException("Error when trying to save to DB", HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		
		return result;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Employee updateEmployee(Employee employee) throws EMPException, Exception {
		EmployeeCheckHelper.checkEmployeeValues(employee);
		EmployeeCheckHelper.checkEmployeeId(employee);
		Employee result = null;
		
		Employee dbEmployee = null;
		try {
			
			// In case the employee is used by another process, we need to make sure it's unlocked
			// In this case, an employee locked is an employee with the GlobalState set to Pending
			dbEmployee = mongoService.findAndLockEmployee(employee.getId());
			if (dbEmployee == null) {
				throw new EMPException("Employee doesn't exist", HttpStatus.NOT_FOUND.value());
			}
			if (dbEmployee.getGlobalState() == GlobalState.PENDING) {
				throw new EMPException("Employee locked by another process", HttpStatus.UNAUTHORIZED.value());
			}
			
			EmployeeCheckHelper.processEmployeeStates(dbEmployee, employee);
			
			result = mongoService.updateEmployee(employee);
			
		} catch (EMPMongoException e) {
			throw new EMPException("Error when trying to update to DB", HttpStatus.INTERNAL_SERVER_ERROR.value());
		} catch (EMPException emp) {
			// We need to unlock the employee before exiting this method
			if (dbEmployee != null && dbEmployee.getGlobalState() != GlobalState.PENDING) {
				mongoService.findAndUnLockEmployee(dbEmployee.getId(), dbEmployee.getGlobalState());
			}
			throw emp;
		}
		
		return result;
	}

}
