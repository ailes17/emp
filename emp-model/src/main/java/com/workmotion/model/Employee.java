package com.workmotion.model;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Employee Entity for MongoDB
 * @author Adel
 *
 */
@Document(collection = "EMP_EMPLOYEE")
@TypeAlias("Employee")
public class Employee implements Serializable {

	private static final long serialVersionUID = 5070505637839376944L;
	
	/**
	 * MongoDB unique ID
	 */
	@Id
    private String id; 
	
	/**
	 * General info (not exhaustive)
	 */
	private String firstName;
	private String lastName;
	private String contractInformation;
	private Integer age;
	
	/**
	 * Different states info
	 */
	// Global States : ADDED -> IN_CHECK -> APPROVED -> ACTIVE
	private GlobalState globalState;
	
	// Security States : SECURITY_CHECK_STARTED -> SECURITY_CHECK_FINISHED
	private SecurityState securityState;
	
	// Work Permit States : WORK_PERMIT_CHECK_STARTED -> WORK_PERMIT_CHECK_PENDING_VERIFICATION -> WORK_PERMIT_CHECK_FINISHED
	private WorkPermitState workPermitState;
	
	
	public Employee() {
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}


	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}


	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	/**
	 * @return the contractInformation
	 */
	public String getContractInformation() {
		return contractInformation;
	}


	/**
	 * @param contractInformation the contractInformation to set
	 */
	public void setContractInformation(String contractInformation) {
		this.contractInformation = contractInformation;
	}


	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}


	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}


	/**
	 * @return the globalState
	 */
	public GlobalState getGlobalState() {
		return globalState;
	}


	/**
	 * @param globalState the globalState to set
	 */
	public void setGlobalState(GlobalState globalState) {
		this.globalState = globalState;
	}


	/**
	 * @return the securityState
	 */
	public SecurityState getSecurityState() {
		return securityState;
	}


	/**
	 * @param securityState the securityState to set
	 */
	public void setSecurityState(SecurityState securityState) {
		this.securityState = securityState;
	}


	/**
	 * @return the workPermitState
	 */
	public WorkPermitState getWorkPermitState() {
		return workPermitState;
	}


	/**
	 * @param workPermitState the workPermitState to set
	 */
	public void setWorkPermitState(WorkPermitState workPermitState) {
		this.workPermitState = workPermitState;
	}


	@Override
	public boolean equals(Object obj) {
		// If the object is compared with itself then return true 
        if (obj == this) {
            return true;
        }
 
        /* Check if o is an instance of Employee or not */
        if (!(obj instanceof Employee)) {
            return false;
        }
         
        // typecast obj to Employee so that we can compare data members
        Employee employee = (Employee) obj;
         
        // Compare the data members and return accordingly
        return Objects.equals(firstName, employee.getFirstName())
        		&& Objects.equals(lastName, employee.getLastName())
        		&& Objects.equals(age, employee.getAge())
        		&& Objects.equals(globalState, employee.getGlobalState())
        		&& Objects.equals(workPermitState, employee.getWorkPermitState())
        		&& Objects.equals(securityState, employee.getSecurityState());
	}
	
	
	
	
}
