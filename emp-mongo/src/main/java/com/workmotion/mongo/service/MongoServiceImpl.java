package com.workmotion.mongo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.workmotion.common.exception.EMPMongoException;
import com.workmotion.model.Employee;
import com.workmotion.model.GlobalState;
import com.workmotion.mongo.repository.EmployeeRepository;

/**
 * MongoService class to handle all DB logic
 * @author Adel
 *
 */
@Service
public class MongoServiceImpl implements MongoService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Employee addEmployee(Employee employee) throws EMPMongoException {
		Employee result = null;
		try {
			result = employeeRepository.save(employee);
		} catch (IllegalArgumentException e) {
			throw new EMPMongoException(e);
		}
		return result;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean doesEmployeeExist(String firstname, String lastname, Integer age) throws EMPMongoException {
		boolean result = false;
		try {
			result = !employeeRepository.findEmployeeByFirstNameLastNameAge(firstname, lastname, age).isEmpty();
		} catch (IllegalArgumentException e) {
			throw new EMPMongoException(e);
		}
		return result;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Employee findEmployeeById(String id) throws EMPMongoException {
		Employee result = null;
		try {
			result = employeeRepository.findEmployeeById(id);
		} catch (IllegalArgumentException e) {
			throw new EMPMongoException(e);
		}
		return result;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Employee> findEmployeesByfirstName(String firstName) throws EMPMongoException {
		List<Employee> result = new ArrayList<>();
		try {
			if (Strings.isNullOrEmpty(firstName)) {
				result = employeeRepository.findAll();
			} else {
				result = employeeRepository.findEmployeeByFirstName(firstName);
			}
		} catch (IllegalArgumentException e) {
			throw new EMPMongoException(e);
		}
		return result;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Employee updateEmployee(Employee employee) throws EMPMongoException {
		Employee result = null;
		try {
			result = employeeRepository.save(employee);
		} catch (IllegalArgumentException e) {
			throw new EMPMongoException(e);
		}
		return result;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Employee findAndLockEmployee(String id) throws EMPMongoException {
		Employee result = null;
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(id));
			Update update = new Update();
			update.set("globalState", GlobalState.PENDING);
			result = mongoTemplate.findAndModify(query, update, Employee.class);
		} catch (IllegalArgumentException e) {
			throw new EMPMongoException(e);
		}
		return result;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Employee findAndUnLockEmployee(String id, GlobalState previousState) throws Exception {
		Employee result = null;
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(id));
			Update update = new Update();
			update.set("globalState", previousState);
			result = mongoTemplate.findAndModify(query, update, Employee.class);
		} catch (IllegalArgumentException e) {
			throw new Exception("Unable to unlock the employee", e);
		}
		return result;
	}

}
