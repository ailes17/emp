package com.workmotion.common.helper;

import org.springframework.http.HttpStatus;

import com.google.common.base.Strings;
import com.workmotion.common.exception.EMPException;
import com.workmotion.model.Employee;
import com.workmotion.model.GlobalState;
import com.workmotion.model.SecurityState;
import com.workmotion.model.WorkPermitState;

/**
 * Helper class for Employee
 * Methods used mainly to check and update the values for Employee entity
 * @author Adel
 *
 */
public class EmployeeCheckHelper {

	/**
	 * Method called every time an employee is sent through restAPi
	 * Verifies the values and throws EMPException
	 * @param employee Employee entity to verify
	 * @throws EMPException EMPException thrown in case of nonconformity
	 */
	public static void checkEmployeeValues(Employee employee) throws EMPException {
		if (Strings.isNullOrEmpty(employee.getFirstName())) {
			throw new EMPException("Employee first name can't be null or empty", HttpStatus.BAD_REQUEST.value());
		}
		if (Strings.isNullOrEmpty(employee.getLastName())) {
			throw new EMPException("Employee last name can't be null or empty", HttpStatus.BAD_REQUEST.value());
		}
		if (employee.getAge() == null) {
			throw new EMPException("Employee age can't be null or empty", HttpStatus.BAD_REQUEST.value());
		}
	}
	
	
	/**
	 * Method to check the employee ID
	 * If it's empty or null, it throws EMPException
	 * @param employee Employee entity to verify
	 * @throws EMPException EMPException thrown in case of nonconformity
	 */
	public static void checkEmployeeId(Employee employee) throws EMPException {
		if (Strings.isNullOrEmpty(employee.getId())) {
			throw new EMPException("Employee Id can't be null or empty", HttpStatus.BAD_REQUEST.value());
		}
	}

	
	/**
	 * Method to initialize the Employee states
	 * GlobalState is set to ADDED and the substates to null
	 * @param employee Employee entity to initialize
	 */
	public static void initializeState(Employee employee) {
		employee.setGlobalState(GlobalState.ADDED);
		employee.setSecurityState(null);
		employee.setWorkPermitState(null);
	}

	
	/**
	 * Method to process the employee state
	 * This method is called at every update of the entity Employee
	 * The global state and the substates are updated following the transaction rules
	 * @param dbEmployee Employee entity in the database
	 * @param employee Employee entity from rest call
	 * @throws EMPException EMPException thrown in case of nonconformity
	 */
	public static void processEmployeeStates(Employee dbEmployee, Employee employee) throws EMPException {
		GlobalState oldGS = dbEmployee.getGlobalState();
		GlobalState newGS = employee.getGlobalState();
		
		WorkPermitState oldWS = dbEmployee.getWorkPermitState();
		WorkPermitState newWS = employee.getWorkPermitState();
		
		SecurityState oldSS = dbEmployee.getSecurityState();
		SecurityState newSS = employee.getSecurityState();
		
		/**
		 * We do a check on each level of the process :
		 * ADDED -> IN_CHECK -> APPROVED -> ACTIVE
		 */
		checkGlobalStates(oldGS, newGS);
		checkAddedState(oldGS, newGS, oldWS, newWS, oldSS, newSS);
		checkInCheckState(oldGS, newGS, oldWS, newWS, oldSS, newSS, employee);
		checkApprovedState(oldGS, newGS, oldWS, newWS, oldSS, newSS);
		checkActiveState(oldGS, newGS, oldWS, newWS, oldSS, newSS);
		
		/**
		 * If all checks are valid, we do a control on the In_Check substates and see
		 * if the automatic approve state can be applied
		 */
		checkAndUpdateAutomaticStates(oldGS, newWS, newSS, employee);
	}


	private static void checkGlobalStates(GlobalState oldGS, GlobalState newGS) throws EMPException {
		if (oldGS != newGS && oldGS.nextState() != newGS) {
			throw new EMPException(newGS + " is not a valid transition from " + oldGS, HttpStatus.BAD_REQUEST.value());
		}
	}
	
	private static void checkAddedState(GlobalState oldGS, GlobalState newGS, WorkPermitState oldWS,
			WorkPermitState newWS, SecurityState oldSS, SecurityState newSS) throws EMPException {
		/**
		 * Scenarios for Added Process
		 */
		if (newGS == GlobalState.ADDED) {
			/**
			 * SScenario where the subStates have changed
			 */
			if (oldWS != newWS || oldSS != newSS) {
				throw new EMPException("Invalid modification for the substates while at Added state", HttpStatus.BAD_REQUEST.value());
			}
		}
		
	}
	
	private static void checkInCheckState(GlobalState oldGS, GlobalState newGS, WorkPermitState oldWS,
			WorkPermitState newWS, SecurityState oldSS, SecurityState newSS, Employee employee) throws EMPException {
		/**
		 * Scenarios for In_Check Process
		 */
		if (newGS == GlobalState.IN_CHECK) {
			
			/**
			 * Scenario where we have started the In_Check Process
			 */
			if (oldGS != newGS) {
				// Check the new subStates if they are valid
				if (newWS != null && newWS != WorkPermitState.WORK_PERMIT_CHECK_STARTED) {
					throw new EMPException(newWS + " is not a valid transition from " + oldWS, HttpStatus.BAD_REQUEST.value());
				}
				if (newSS != null && newSS != SecurityState.SECURITY_CHECK_STARTED) {
					throw new EMPException(newSS + " is not a valid transition from " + oldSS, HttpStatus.BAD_REQUEST.value());
				}
				
				// In case where we have started the In_Check Process and the subStates are left for null, we can initialize them
				employee.setSecurityState(SecurityState.SECURITY_CHECK_STARTED);
				employee.setWorkPermitState(WorkPermitState.WORK_PERMIT_CHECK_STARTED);
			}
			/**
			 * Scenario where we are still in the In_Check Process
			 */
			else {
				// Check if only one Substate has been modified (Both modifications not allowed)
				if (oldSS != newSS && oldWS != newWS) {
					throw new EMPException("Only one In_Check substate is allowed for modification", HttpStatus.BAD_REQUEST.value());
				}
				
				// Now we check the validity of transition for the substates
				if (oldSS != newSS && oldSS.nextState() != newSS) {
					throw new EMPException(newSS + " is not a valid transition from " + oldSS, HttpStatus.BAD_REQUEST.value());
				}
				if (oldWS != newWS && oldWS.nextState() != newWS) {
					throw new EMPException(newWS + " is not a valid transition from " + oldWS, HttpStatus.BAD_REQUEST.value());
				}
			}
		}
		
	}
	
	private static void checkApprovedState(GlobalState oldGS, GlobalState newGS, WorkPermitState oldWS,
			WorkPermitState newWS, SecurityState oldSS, SecurityState newSS) throws EMPException {
		/**
		 * Scenarios for Approved
		 */
		if (newGS == GlobalState.APPROVED) {
			/**
			 * Scenario where the globalState has changed
			 */
			if (oldGS != newGS) {
				throw new EMPException("Approved is an automatic state, it should not be manually set", HttpStatus.BAD_REQUEST.value());
			}
			/**
			 * Scenario where the globalState hasn't changed, but we need to check the subStates
			 */
			else if (oldWS != newWS || oldSS != newSS) {
				throw new EMPException("Invalid modification for the substates while at Approved state", HttpStatus.BAD_REQUEST.value());
			}
		}
	}
	
	private static void checkActiveState(GlobalState oldGS, GlobalState newGS, WorkPermitState oldWS,
			WorkPermitState newWS, SecurityState oldSS, SecurityState newSS) throws EMPException {
		/**
		 * Scenarios for Active
		 */
		if (newGS == GlobalState.ACTIVE) {
			/**
			 * Scenario where the subStates have changed
			 */
			if (oldWS != newWS || oldSS != newSS) {
				throw new EMPException("Invalid modification for the substates while at Active state", HttpStatus.BAD_REQUEST.value());
			}
		}
	}
	
	private static void checkAndUpdateAutomaticStates(GlobalState oldGS, WorkPermitState newWS, SecurityState newSS, Employee employee) {
		// In case where we have both In_Check substates finished, we update the global state to Approved
		if (oldGS == GlobalState.IN_CHECK && newWS == WorkPermitState.WORK_PERMIT_CHECK_FINISHED && newSS == SecurityState.SECURITY_CHECK_FINISHED) {
			employee.setGlobalState(GlobalState.APPROVED);
		}		
	}

}
