package com.workmotion.common.helper;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.workmotion.model.Employee;
import com.workmotion.model.GlobalState;
import com.workmotion.model.SecurityState;
import com.workmotion.model.WorkPermitState;
import com.workmotion.common.exception.EMPException;

public class EmployeeCheckHelperTest {
	

	@Test
	public void initializeStateTest() {
		Employee employee1 = createEmployee("Bill", "Gate", 35);
		
		assertEquals(employee1.getGlobalState(), null);
		assertEquals(employee1.getWorkPermitState(), null);
		assertEquals(employee1.getSecurityState(), null);
		
		EmployeeCheckHelper.initializeState(employee1);
		
		assertEquals(employee1.getGlobalState(), GlobalState.ADDED);
		assertEquals(employee1.getWorkPermitState(), null);
		assertEquals(employee1.getSecurityState(), null);
	}
	
	@Test
	public void processEmployeeStatesTest1() {
		Employee dbEmployee = createEmployee("Bill", "Gate", 35);
		dbEmployee.setGlobalState(GlobalState.ADDED);
		Employee employee = createEmployee("Bill", "Gate", 35);
		employee.setGlobalState(GlobalState.ACTIVE);
		
		EMPException exception = assertThrows(EMPException.class, () -> {
			EmployeeCheckHelper.processEmployeeStates(dbEmployee, employee);
	    });
		
		assertTrue(exception.getMessage().contains("is not a valid transition"));
	}
	
	
	@Test
	public void processEmployeeStatesTest2() {
		Employee dbEmployee = createEmployee("Bill", "Gate", 35);
		dbEmployee.setGlobalState(GlobalState.IN_CHECK);
		dbEmployee.setSecurityState(SecurityState.SECURITY_CHECK_STARTED);
		Employee employee = createEmployee("Bill", "Gate", 35);
		employee.setGlobalState(GlobalState.IN_CHECK);
		employee.setSecurityState(SecurityState.SECURITY_CHECK_FINISHED);
		
		assertDoesNotThrow(() -> {
			EmployeeCheckHelper.processEmployeeStates(dbEmployee, employee);
	    });
	}
	
	@Test
	public void processEmployeeStatesTest3() {
		Employee dbEmployee = createEmployee("Bill", "Gate", 35);
		dbEmployee.setGlobalState(GlobalState.IN_CHECK);
		dbEmployee.setSecurityState(SecurityState.SECURITY_CHECK_FINISHED);
		Employee employee = createEmployee("Bill", "Gate", 35);
		employee.setGlobalState(GlobalState.IN_CHECK);
		employee.setSecurityState(SecurityState.SECURITY_CHECK_STARTED);
		
		EMPException exception = assertThrows(EMPException.class, () -> {
			EmployeeCheckHelper.processEmployeeStates(dbEmployee, employee);
	    });
		
		assertTrue(exception.getMessage().contains("is not a valid transition"));
	}
	
	
	@Test
	public void processEmployeeStatesTest4() {
		Employee dbEmployee = createEmployee("Bill", "Gate", 35);
		dbEmployee.setGlobalState(GlobalState.IN_CHECK);
		dbEmployee.setSecurityState(SecurityState.SECURITY_CHECK_STARTED);
		dbEmployee.setWorkPermitState(WorkPermitState.WORK_PERMIT_CHECK_STARTED);
		
		Employee employee = createEmployee("Bill", "Gate", 35);
		employee.setGlobalState(GlobalState.IN_CHECK);
		employee.setSecurityState(SecurityState.SECURITY_CHECK_FINISHED);
		employee.setWorkPermitState(WorkPermitState.WORK_PERMIT_CHECK_PENDING_VERIFICATION);
		
		EMPException exception = assertThrows(EMPException.class, () -> {
			EmployeeCheckHelper.processEmployeeStates(dbEmployee, employee);
	    });
		
		assertTrue(exception.getMessage().contains("Only one In_Check substate is allowed"));
	}
	
	
	@Test
	public void processEmployeeStatesTest5() {
		Employee dbEmployee = createEmployee("Bill", "Gate", 35);
		dbEmployee.setGlobalState(GlobalState.IN_CHECK);
		dbEmployee.setSecurityState(SecurityState.SECURITY_CHECK_STARTED);
		dbEmployee.setWorkPermitState(WorkPermitState.WORK_PERMIT_CHECK_FINISHED);
		
		Employee employee = createEmployee("Bill", "Gate", 35);
		employee.setGlobalState(GlobalState.IN_CHECK);
		employee.setSecurityState(SecurityState.SECURITY_CHECK_FINISHED);
		employee.setWorkPermitState(WorkPermitState.WORK_PERMIT_CHECK_FINISHED);
		
		assertDoesNotThrow(() -> {
			EmployeeCheckHelper.processEmployeeStates(dbEmployee, employee);
	    });
		
		assertEquals(employee.getGlobalState(), GlobalState.APPROVED);
	}
	
	private Employee createEmployee(String firstname, String lastname, Integer age) {
		Employee result = new Employee();
		result.setFirstName(firstname);
		result.setLastName(lastname);
		result.setAge(age);
		return result;
	}
}
