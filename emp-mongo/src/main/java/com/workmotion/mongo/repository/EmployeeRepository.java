package com.workmotion.mongo.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.workmotion.model.Employee;

/**
 * Employee Repository, automanaged by Spring
 * @author Adel
 *
 */
public interface EmployeeRepository extends MongoRepository<Employee,Integer> {
	
	/**
	 * Find Employee by ID
	 * @param id Employee ID
	 * @return Employee or null
	 */
	@Query("{ 'id' : ?0 }")
	Employee findEmployeeById(String id);
	
	
	/**
	 * Find Employee by his first name
	 * @param firstName Employee First name
	 * @return List of Employees or null
	 */
	@Query("{ 'firstName' : ?0 }")
	List<Employee> findEmployeeByFirstName(String firstName);

	
	/**
	 * Find employee by first name, last name and age
	 * @param firstName Employee First name
	 * @param lastName Employee Last name
	 * @param age Employee Age
	 * @return List of Employees or null
	 */
	@Query("{ 'firstName' : ?0 , 'lastName' : ?1 'age' : ?2}")
	List<Employee> findEmployeeByFirstNameLastNameAge(String firstName, String lastName, Integer age);
}

